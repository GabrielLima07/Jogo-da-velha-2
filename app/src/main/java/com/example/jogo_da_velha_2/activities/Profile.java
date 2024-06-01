package com.example.jogo_da_velha_2.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jogo_da_velha_2.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Profile extends AppCompatActivity {
    private ImageView profilePic;
    private TextView partidasJogadas, userName, winCount, ranking, amigos;
    private Uri photoUri;

    private final ActivityResultLauncher<String> requestImagePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openImageChooser();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        profilePic.setImageBitmap(bitmap);
                        uploadImageToServer(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Profile.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                        profilePic.setImageBitmap(bitmap);
                        uploadImageToServer(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Profile.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String userId = currentUser.getObjectId();
        backBtn();
        getUserDetails(userId);
        setupEditButton();
        setupCameraButton();
    }

    private void backBtn() {
        ImageButton voltar = findViewById(R.id.button);
        voltar.setOnClickListener(v -> finish());
    }

    private void getUserDetails(String objectId) {
        partidasJogadas = findViewById(R.id.partidasJogadas);
        userName = findViewById(R.id.user_name);
        winCount = findViewById(R.id.winCount);
        ranking = findViewById(R.id.ranking);
        amigos = findViewById(R.id.amigos);
        profilePic = findViewById(R.id.profile_pic);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                if (object != null) {
                    userName.setText(object.getString("username"));
                    winCount.setText(String.valueOf(object.getInt("winCount")));
                    ranking.setText(String.valueOf(object.getInt("ranking")));
                    amigos.setText(String.valueOf(object.getInt("amigos")));
                    partidasJogadas.setText(String.valueOf(object.getInt("partidasJogadas")));

                    ParseFile profilePicture = object.getParseFile("profilePic");
                    if (profilePicture != null) {
                        profilePicture.getDataInBackground((data, e1) -> {
                            if (e1 == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                profilePic.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(Profile.this, "Erro ao carregar a foto: " + e1.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(Profile.this, "Erro ao buscar o objeto: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupEditButton() {
        ImageButton editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> checkPermissionAndOpenImageChooser());
    }

    private void setupCameraButton() {
        ImageButton cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(v -> checkPermissionAndOpenCamera());
    }

    private void checkPermissionAndOpenImageChooser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                requestImagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                requestImagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, "com.example.jogo_da_velha_2.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                takePictureLauncher.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void uploadImageToServer(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        ParseFile file = new ParseFile("profile_pic.jpg", byteArray);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("profilePic", file);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(Profile.this, "Foto do perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Profile.this, "Erro ao salvar a foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Profile.this, "Erro ao fazer upload da foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
