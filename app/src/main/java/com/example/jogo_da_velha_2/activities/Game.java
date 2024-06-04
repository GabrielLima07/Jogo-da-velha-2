package com.example.jogo_da_velha_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jogo_da_velha_2.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends AppCompatActivity {
    ImageButton seta;
    private ImageButton[][] boardButtons;
    private String[][] board;
    private int roundCount;
    private TextView timer;
    private CountDownTimer countDownTimer;
    private final long turn_time = 15000;
    private String currentPlayerId;
    private String playerXId;
    private String playerOId;
    private String matchId;
    private String tableId;
    private ParseObject matchObject;
    private ParseObject tableObject;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        seta();

        timer = findViewById(R.id.timer);
        Intent intent = getIntent();
        matchId = intent.getStringExtra("matchId");
        tableId = intent.getStringExtra("tableId");
        playerXId = intent.getStringExtra("playerXId");
        playerOId = intent.getStringExtra("playerOId");

        initializeBoard();

        fetchMatch(matchId);
    }

    private void seta() {
        seta = findViewById(R.id.seta);

        Intent i = new Intent(Game.this, MainActivity.class);
        i.putExtra("objectId", ParseUser.getCurrentUser().getObjectId());

        seta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Game.this)
                        .setTitle("Sair da partida")
                        .setMessage("Tem certeza que quer sair da partida?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Não", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void initializeBoard() {
        boardButtons = new ImageButton[5][5];
        board = new String[5][5];
        boardButtons[0][0] = findViewById(R.id.imageButton1);
        boardButtons[0][1] = findViewById(R.id.imageButton2);
        boardButtons[0][2] = findViewById(R.id.imageButton3);
        boardButtons[0][3] = findViewById(R.id.imageButton4);
        boardButtons[0][4] = findViewById(R.id.imageButton5);
        boardButtons[1][0] = findViewById(R.id.imageButton6);
        boardButtons[1][1] = findViewById(R.id.imageButton7);
        boardButtons[1][2] = findViewById(R.id.imageButton8);
        boardButtons[1][3] = findViewById(R.id.imageButton9);
        boardButtons[1][4] = findViewById(R.id.imageButton10);
        boardButtons[2][0] = findViewById(R.id.imageButton11);
        boardButtons[2][1] = findViewById(R.id.imageButton12);
        boardButtons[2][2] = findViewById(R.id.imageButton13);
        boardButtons[2][3] = findViewById(R.id.imageButton14);
        boardButtons[2][4] = findViewById(R.id.imageButton15);
        boardButtons[3][0] = findViewById(R.id.imageButton16);
        boardButtons[3][1] = findViewById(R.id.imageButton17);
        boardButtons[3][2] = findViewById(R.id.imageButton18);
        boardButtons[3][3] = findViewById(R.id.imageButton19);
        boardButtons[3][4] = findViewById(R.id.imageButton20);
        boardButtons[4][0] = findViewById(R.id.imageButton21);
        boardButtons[4][1] = findViewById(R.id.imageButton22);
        boardButtons[4][2] = findViewById(R.id.imageButton23);
        boardButtons[4][3] = findViewById(R.id.imageButton24);
        boardButtons[4][4] = findViewById(R.id.imageButton25);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                final int row = i;
                final int col = j;
                boardButtons[i][j].setOnClickListener(v -> makeMove(row, col));
            }
        }
    }

    private void makeMove(int row, int col) {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (!currentPlayerId.equals(currentUser.getObjectId())) {
            // Não é a vez do jogador
            Log.d("ERRO", "makeMove1");
            return;
        }

        String currentMove = board[row][col];
        if (currentMove != null && !currentMove.isEmpty()) {
            // Retangulo preenchido
            return;
        }

        if (currentPlayerId.equals(playerXId)) {
            board[row][col] = "X";
            boardButtons[row][col].setImageResource(R.drawable.x);
        } else if (currentPlayerId.equals(playerOId)) {
            board[row][col] = "O";
            boardButtons[row][col].setImageResource(R.drawable.o);
        }

        this.roundCount++;

        checkAndHandleGameEnd();

        updateBoardState(() -> {
            switchPlayer();
            fetchMatch(matchId);
        });
    }

    private void updateBoardState(Runnable callback) {
        List<String> boardList = convertBoardToList(board);
        matchObject.put("boardState", boardList);
        matchObject.saveInBackground(e -> {
            if (e != null) {
                e.printStackTrace();
            } else {
                callback.run();
            }
        });
    }

    private void fetchMatch(String matchId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        query.getInBackground(matchId, (match, e) -> {
            if (e == null && match != null) {
                matchObject = match;
                List<String> boardList = matchObject.getList("boardState");
                board = convertListToBoard(boardList, 5, 5);
                currentPlayerId = matchObject.getString("current_player_id");

                ParseUser winnerUser = matchObject.getParseUser("winner");
                if (winnerUser != null) {
                    String winnerId = winnerUser.getObjectId();
                    String currentUserId = ParseUser.getCurrentUser().getObjectId();
                    if (winnerId.equals(currentUserId)) {
                        showGameEndDialog(winnerId, false);
                    } else {
                        showGameEndDialog(winnerId, false);
                    }
                }

                updateBoardUI();
            } else {
                e.printStackTrace();
            }
        });
    }

    private void updateBoardUI() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if ("X".equals(board[i][j])) {
                    boardButtons[i][j].setImageResource(R.drawable.x);
                } else if ("O".equals(board[i][j])) {
                    boardButtons[i][j].setImageResource(R.drawable.o);
                } else {
                    boardButtons[i][j].setImageResource(R.drawable.retangulo);
                }
            }
        }
    }

    private String getCurrentPlayerId() {
        return matchObject != null ? matchObject.getString("current_player_id") : null;
    }

    private void switchPlayer() {
        if (currentPlayerId.equals(playerXId)) {
            currentPlayerId = playerOId;
        } else {
            currentPlayerId = playerXId;
        }
        postCurrentPlayerId(currentPlayerId, () -> {
            Log.d("switchPlayer", currentPlayerId);
            fetchMatch(matchId);
        });
    }

    private void postCurrentPlayerId(String playerId, Runnable callback) {
        if (matchObject != null) {
            matchObject.put("current_player_id", playerId);
            matchObject.saveInBackground(e -> {
                if (e == null) {
                    Log.d("postCurrentPlayerId", playerId);
                    callback.run();
                } else {
                    e.printStackTrace();
                }
            });
        }
    }

    private List<String> convertBoardToList(String[][] board) {
        List<String> boardList = new ArrayList<>();
        for (String[] row : board) {
            boardList.addAll(Arrays.asList(row));
        }
        return boardList;
    }

    private String[][] convertListToBoard(List<String> boardList, int numRows, int numCols) {
        String[][] board = new String[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                board[i][j] = boardList.get(i * numCols + j);
            }
        }
        return board;
    }

    private Handler handler = new Handler();
    private Runnable fetchMatchRunnable = new Runnable() {
        @Override
        public void run() {
            fetchMatch(matchId);
            handler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(fetchMatchRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(fetchMatchRunnable);
    }

    private boolean checkForWin() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (board[i][j].equals(board[i][j + 1]) && board[i][j].equals(board[i][j + 2]) && board[i][j].equals(board[i][j + 3]) && !board[i][j].isEmpty()) {
                    return true;
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j].equals(board[i + 1][j]) && board[i][j].equals(board[i + 2][j]) && board[i][j].equals(board[i + 3][j]) && !board[i][j].isEmpty()) {
                    return true;
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (board[i][j].equals(board[i + 1][j + 1]) && board[i][j].equals(board[i + 2][j + 2]) && board[i][j].equals(board[i + 3][j + 3]) && !board[i][j].isEmpty()) {
                    return true;
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 3; j < 5; j++) {
                if (board[i][j].equals(board[i + 1][j - 1]) && board[i][j].equals(board[i + 2][j - 2]) && board[i][j].equals(board[i + 3][j - 3]) && !board[i][j].isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void showGameEndDialog(String winnerId, boolean isDraw) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String currentUserId = currentUser.getObjectId();
        String message;

        if (isDraw) {
            message = "O jogo terminou em empate!";
        } else if (winnerId.equals(currentUserId)) {
            message = "Parabéns! Você ganhou!";
        } else {
            message = "Você perdeu!";
        }

        new AlertDialog.Builder(Game.this)
                .setTitle("Fim do jogo")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    startActivity(new Intent(Game.this, MainActivity.class).putExtra("objectId", currentUserId));
                })
                .show();
    }

    private void checkAndHandleGameEnd() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (checkForWin()) {
            postWinner(currentUser);
            showGameEndDialog(currentUser.getObjectId(), false);
        } else if (roundCount == 25) {
            postDraw();
            showGameEndDialog(null, true);
        }
    }

    private void postDraw() {
        if (matchObject != null) {
            matchObject.put("winner", "draw");
            matchObject.saveInBackground(e -> {
                if (e == null) {
                    Log.d("postDraw", "Draw saved");

                    incrementUserStats(playerXId, false);
                    incrementUserStats(playerOId, false);
                } else {
                    e.printStackTrace();
                }
            });
        }
    }

    private void postWinner(ParseUser winnerUser) {
        if (matchObject != null) {
            matchObject.put("winner", winnerUser);
            matchObject.saveInBackground(e -> {
                if (e == null) {
                    Log.d("postWinner", "Winner saved: " + winnerUser.getUsername());

                    incrementUserStats(winnerUser.getObjectId(), true);

                    String loserUserId = winnerUser.getObjectId().equals(playerXId) ? playerOId : playerXId;
                    incrementUserStats(loserUserId, false);

                    notifyPlayers(winnerUser);
                } else {
                    e.printStackTrace();
                }
            });
        }
    }

    private void notifyPlayers(ParseUser winnerUser) {
        String winnerId = winnerUser.getObjectId();
        String currentUserId = ParseUser.getCurrentUser().getObjectId();

        if (winnerId.equals(currentUserId)) {
            showGameEndDialog(winnerId, false);
        } else {
            showGameEndDialog(winnerId, false);
        }
    }

    private void incrementUserStats(String userId, boolean isWinner) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(userId, (user, e) -> {
            if (e == null && user != null) {
                if (isWinner) {
                    user.increment("winCount");
                }
                user.increment("partidasJogadas");
                user.saveInBackground(error -> {
                    if (error != null) {
                        error.printStackTrace();
                    }
                });
            } else if (e != null) {
                e.printStackTrace();
            }
        });
    }

}