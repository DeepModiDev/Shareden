package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.UserPost;
import com.deepmodi.shareden.model.UserRegister;
import com.deepmodi.shareden.ui.MyAccount.MyAccountFragment;
import com.deepmodi.shareden.utils.FileUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.paperdb.Paper;

public class EditProfileAcivity extends AppCompatActivity {

    EditText edit_user_name,edit_user_occupation,edit_user_phone,edit_user_bio;
    Button btn_update;
    RoundedImageView profile_img;
    ImageView btn_select_image_imageview;
    String userName,userOccupation,userPhone,userBio;

    private FirebaseStorage storageEdit;
    private StorageReference referenceStorage;
    private FirebaseDatabase data_base,databaseMyPostEdit;
    private DatabaseReference referenceEdit,referenceMyPostEdit;
    private UserRegister register = new UserRegister();
    private List<String> myIds = new ArrayList<>();
    private List<UserPost> post = new ArrayList<>();
    private ProgressDialog progressDialog;
    private UploadImageAsyncTask uploadImageAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_acivity);

        //Paper init
        Paper.init(this);

        //firebase init
        data_base = FirebaseDatabase.getInstance();
        referenceEdit = data_base.getReference("Users").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString());

        databaseMyPostEdit = FirebaseDatabase.getInstance();
        referenceMyPostEdit = databaseMyPostEdit.getReference("MyPost").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString());

        storageEdit = FirebaseStorage.getInstance();
        referenceStorage = storageEdit.getReference("UsersImages");

        //init UI elements
        edit_user_bio = findViewById(R.id.edit_user_bio);
        edit_user_name = findViewById(R.id.edit_user_name);
        edit_user_occupation = findViewById(R.id.edit_user_occupation);
        edit_user_phone = findViewById(R.id.edit_user_phone);

        btn_update = findViewById(R.id.btn_update);
        profile_img = findViewById(R.id.profile_img);

        btn_select_image_imageview = findViewById(R.id.btn_select_image_imageview);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        }

        loadUserInformation(referenceEdit);


        edit_user_name.setText(register.getUserName());
        edit_user_phone.setText(register.getUserNumber());
        edit_user_phone.setEnabled(false);
        edit_user_occupation.setText(register.getUserLevel());
        edit_user_bio.setText(register.getUserDesc());
        try
        {
            Picasso.get().load(Paper.book().read(Common.USER_IMAGE_LINK).toString()).error(R.drawable.usr_img).into(profile_img);
        }catch(Exception e)
        {
            Log.e("EditProfileActivity",e.getMessage());
        }

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_select_image_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(EditProfileAcivity.this);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setUserName(edit_user_name.getText().toString());
                register.setUserLevel(edit_user_occupation.getText().toString());
                register.setUserDesc(edit_user_bio.getText().toString());
                referenceEdit.setValue(register);
               // updateUserInformation(referenceMyPostEdit);
            }
        });
    }


    private void openImageChooser(final EditProfileAcivity activity) {
        final CharSequence[] sequences = {"Choose your image","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose from gallery");
        builder.setCancelable(false);
        builder.setItems(sequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sequences[which].equals("Choose your image"))
                {
                    CropImage.activity().start(activity);
                }else if(sequences[which].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                final Uri resultUri = result.getUri();
                try {
                    File finalImage = FileUtil.from(Objects.requireNonNull(this), resultUri);

                    File compressedImage = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(finalImage);
                    //String resultUri = result.getUri().toString();
                    if (resultUri != null) {
                        progressDialog = new ProgressDialog(this);
                        uploadImageAsyncTask = new UploadImageAsyncTask();
                        uploadImageAsyncTask.execute(compressedImage);
                    }
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception exception = result.getError();
                Log.e("EditProfileActivity",exception.toString());
            }
        }
    }

    private void loadUserInformation(DatabaseReference query) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                register = dataSnapshot.getValue(UserRegister.class);
                try {
                    if(register.getUserImg()!=null)
                    {
                        Picasso.get().load(register.getUserImg()).placeholder(R.drawable.usr_img).into(profile_img);
                        Paper.book().write(Common.USER_IMAGE_LINK,register.getUserImg());
                    }
                    //Log.d("MyAccountMessage","Message :"+register.getUserName()+register.getUserImg());
                }catch (Exception e)
                {
                    Log.e("EditProfileActivity :",e.getMessage());
                    profile_img.setImageResource(R.drawable.usr_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   /* private void updateUserInformation(final DatabaseReference reference) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    myIds.add(dataSnapshot1.getKey());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myIds.size() > 0)
                {
                    for(int i=0; i<myIds.size(); i++)
                    {
                        reference.child(myIds.get(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                post.add(dataSnapshot.getValue(UserPost.class));
                                // Log.d("MyAccountFragment ","Post"+post.get(0).getPostId());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                if(post.size() > 0)
                {
                    //Log.d("MyAccountFragment :",post.get(0).toString());
                    for(int i=0; i<post.size(); i++)
                    {
                        post.get(i).setUserFullName(edit_user_name.getText().toString());
                        post.get(i).setUserLevel(edit_user_bio.getText().toString());
                        post.get(i).setUserImg(Paper.book().read(Common.USER_IMAGE_LINK).toString());
                    }
                    for(int i=0; i<post.size(); i++)
                    {
                        referenceMyPostEdit.child(myIds.get(i)).setValue(post.get(i));
                    }
                }
            }
        },3000);
    }
    */

    private class UploadImageAsyncTask extends AsyncTask<File,Double,String> {
        String result;
        double progress;

        @Override
        protected String doInBackground(File... files) {
            final StorageReference ref = referenceStorage.child("images/" + Paper.book().read(Common.USER_FINAL_NUMBER));
            ref.putFile(Uri.fromFile(files[0]))
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            progress = (double) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    result = uri.toString();
                                    Paper.book().write(Common.USER_IMAGE_LINK, result);
                                    register.setUserImg(result);
                                    referenceEdit.setValue(register);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });
            publishProgress(progress);
            return result;
        }

        @Override
        protected void onProgressUpdate(Double... values){
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Loading : " + values[0] + "%");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String uri) {
            super.onPostExecute(uri);
            Toast.makeText(EditProfileAcivity.this, "Image uploaded successfully.", Toast.LENGTH_SHORT).show();

        }
    }
}
