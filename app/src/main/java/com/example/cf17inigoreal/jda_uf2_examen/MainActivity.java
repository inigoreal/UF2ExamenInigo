package com.example.cf17inigoreal.jda_uf2_examen;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    ImageView imageGallery;
    Button pujarButton;
    EditText descInc, aulaInc;
    int NUM_IMAGE_GALLERY = 1;
    Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageReference = FirebaseStorage.getInstance().getReference("elements");
        databaseReference = FirebaseDatabase.getInstance().getReference("elements");

        imageGallery = findViewById(R.id.openGallery);
        descInc = findViewById(R.id.descInc);
        aulaInc = findViewById(R.id.aulaInc);
        pujarButton = findViewById(R.id.pujarFire);

        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        pujarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pujarElement();
            }
        });


    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, NUM_IMAGE_GALLERY);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NUM_IMAGE_GALLERY && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imageGallery.setImageURI(imageUri);
        }

    }
    private void pujarElement() {
        if(imageUri!=null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String uploadId = databaseReference.push().getKey();
                        UploadElement uploadElement = new UploadElement(uploadId, descInc.getText().toString(), aulaInc.getText().toString(), downloadUri.toString(),false);
                        databaseReference.child(uploadId).setValue(uploadElement);

                    }
                }
            });
            Toast.makeText(this,"Correcte",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
