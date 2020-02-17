package com.deepmodi.shareden.ui.MyAccount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.Edits;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deepmodi.shareden.ActivityUserFollower;
import com.deepmodi.shareden.ActivityUserFollowing;
import com.deepmodi.shareden.ActivityUserRequest;
import com.deepmodi.shareden.Adapter.HomePostImageViewAdapter;
import com.deepmodi.shareden.EditProfileAcivity;
import com.deepmodi.shareden.Interface.UploadItemClickListner;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.UploadActivity;
import com.deepmodi.shareden.ViewHolder.MyPostViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.MyPostView;
import com.deepmodi.shareden.model.UserPost;
import com.deepmodi.shareden.model.UserRegister;
import com.deepmodi.shareden.ui.MyPost.MyPostFragment;
import com.deepmodi.shareden.utils.FileUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;

public class MyAccountFragment extends Fragment {

        private MyAccountViewModel mViewModel;
        AppBarLayout appBarLayout;
        private RoundedImageView profile_img;
        private DatabaseReference reference,referenceMyPost;
        private UserRegister register = new UserRegister();
        private StorageReference referenceStorage;
        private String tempUrl;
        private TextView edit_user_name,edit_user_level,edit_user_desc;
        private List<String> myIds = new ArrayList<>();
        private List<UserPost> post = new ArrayList<>();

    UserRegister updatedRegister = new UserRegister();

    public static MyAccountFragment newInstance() {
        return new MyAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.my_account_fragment, container, false);

        //init database
        Paper.init(view.getContext());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users").child((String)Paper.book().read(Common.USER_FINAL_NUMBER));

        FirebaseDatabase databaseMyPost = FirebaseDatabase.getInstance();
        referenceMyPost = databaseMyPost.getReference("MyPost").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        referenceStorage = storage.getReference("UsersImages");

        UploadItemClickListner itemClickListner;
        TextView follower_textView = view.findViewById(R.id.follower_textView);
        TextView following_textView = view.findViewById(R.id.following_textView);
        TextView requests_textView = view.findViewById(R.id.requests_textView);

        follower_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ActivityUserFollower.class));
            }
        });

        following_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ActivityUserFollowing.class));
            }
        });

        requests_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ActivityUserRequest.class));
            }
        });

        profile_img = view.findViewById(R.id.profile_img);
        edit_user_name = view.findViewById(R.id.profile_user_name);
        edit_user_desc = view.findViewById(R.id.profile_user_about_me);
        edit_user_level = view.findViewById(R.id.profile_user_level);
        AppCompatButton btn_edit = view.findViewById(R.id.btn_edit_profile);
        edit_user_name.setEnabled(false);
        edit_user_level.setEnabled(false);
        edit_user_desc.setEnabled(false);

        TextView user_phone_number_id = view.findViewById(R.id.profile_user_phone_number);
        user_phone_number_id.setText(Paper.book().read(Common.USER_FINAL_NUMBER).toString());
        final MyAccountFragment activity = this;
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(v.getContext(),activity);
            }
        });

       loadUserInformation(reference);
       //loadMyPost();

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                     Log.d("MyAccout","Btn pressed");
               register.setUserName(edit_user_name.getText().toString());
               register.setUserLevel(edit_user_level.getText().toString());
               register.setUserDesc(edit_user_desc.getText().toString());
               reference.setValue(register);
               updateUserInformation(referenceMyPost);
                Toast.makeText(v.getContext(), "All the informations are saved.", Toast.LENGTH_LONG).show();
                btn_save.setText("Changes Are Saved");
                */
                Intent intent = new Intent(getContext(),EditProfileAcivity.class);
                intent.putExtra("userName",register.getUserName());
                intent.putExtra("userOccupation",register.getUserLevel());
                intent.putExtra("userPhoneNumber",register.getUserNumber());
                intent.putExtra("userBio",register.getUserDesc());
                startActivity(intent);
            }
        });
        return view;
    }

    private void updateUserInformation(final DatabaseReference reference) {
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
                       post.get(i).setUserLevel(edit_user_level.getText().toString());
                       post.get(i).setUserImg(Paper.book().read(Common.USER_IMAGE_LINK).toString());
                   }

                   for(int i=0; i<post.size(); i++)
                   {
                       referenceMyPost.child(myIds.get(i)).setValue(post.get(i));
                   }
               }
           }
       },3000);
    }

    private void loadUserInformation(DatabaseReference query) {
        query.addValueEventListener(new ValueEventListener() {
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
                   Log.e("MyAccoutException :",e.getMessage());
                   profile_img.setImageResource(R.drawable.usr_img);
               }
               edit_user_name.setText(register.getUserName());
               edit_user_level.setText(register.getUserLevel());
               edit_user_desc.setText(register.getUserDesc());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openImageChooser(final Context context, final MyAccountFragment activity) {
        final CharSequence[] sequences = {"Choose your image","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose from gallery");
        builder.setItems(sequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sequences[which].equals("Choose your image"))
                {
                    CropImage.activity().start(context,activity);
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
                    File finalImage = FileUtil.from(Objects.requireNonNull(getContext()), resultUri);

                    File compressedImage = new Compressor(getContext())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(finalImage);

                    //String resultUri = result.getUri().toString();
                    if (resultUri != null) {
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setTitle("Loading...");
                        dialog.show();
                        final StorageReference ref = referenceStorage.child("images/" + Paper.book().read(Common.USER_FINAL_NUMBER));
                        ref.putFile(Uri.fromFile(compressedImage))
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        dialog.setMessage("Loading : " + (float) progress + "%");
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        dialog.dismiss();
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                tempUrl = uri.toString();
                                                Paper.book().write(Common.USER_IMAGE_LINK,tempUrl);
                                                register.setUserImg(tempUrl);
                                                reference.setValue(register);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception exception = result.getError();
                    Toast.makeText(getActivity(), "" + exception, Toast.LENGTH_SHORT).show();
                }
            }
    }
    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference.goOffline();
        Log.d("MyAccountFragmnet : ","Database goes online on resume");
    }

    @Override
    public void onStop() {
        super.onStop();
        DatabaseReference.goOffline();
        Log.d("MyAccountFragmnet : ","Database goes offline on stop");
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference.goOnline();
        Log.d("MyAccountFragmnet : ","Database goes online in start");
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseReference.goOffline();
        Log.d("MyAccountFragmnet : ","Database goes offline in pause");
    }
}
