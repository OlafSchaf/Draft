package com.example.iadraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private ImageView close;
    private ImageView image_added;
    private TextView post;
    SocialAutoCompleteTextView description;
    private TextView condition;
    private Uri imageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close=findViewById(R.id.close);
        image_added=findViewById(R.id.image_added);
        post=findViewById(R.id.post);
        description=findViewById(R.id.description);
        condition=findViewById(R.id.condition);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainPage.class ));
                finish();
            }
        });
        CropImage.activity().start(PostActivity.this);
        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                upload();
            }

            private void upload()
            {

                if (imageUri!=null)
                {
                    StorageReference storage= FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getfileExtension(imageUri));
                    StorageTask uploading = storage.putFile(imageUri);
                    uploading.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful())
                            {
                               throw task.getException();
                            }
                            return storage.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                           Uri downloadUri = task.getResult();
                           imageUrl = downloadUri.toString();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                            String iD = reference.push().getKey();

                            HashMap<String, Object> storeMap = new HashMap<>();
                            storeMap.put("ID", iD);
                            storeMap.put("imageurl", imageUrl);
                            storeMap.put("Description", description.getText().toString());
                            storeMap.put("Condition", condition.getText().toString());
                            storeMap.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            reference.child(iD).setValue(storeMap);

                            startActivity(new Intent(PostActivity.this, MainPage.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) 
                        {
                            Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {
                    Toast.makeText(PostActivity.this, "Failed, please reselect image and try again", Toast.LENGTH_SHORT).show();
                }
                    
            }
        });

    }

    private String getfileExtension(Uri uri)
    {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            image_added.setImageURI(imageUri);


        }
        else
        {
            Toast.makeText(this, "Failed, Please try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainPage.class));
            finish();
        }

    }
}