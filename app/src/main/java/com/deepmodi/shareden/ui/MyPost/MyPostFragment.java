package com.deepmodi.shareden.ui.MyPost;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.deepmodi.shareden.Adapter.HomePostImageViewAdapter;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.UploadActivity;
import com.deepmodi.shareden.ViewHolder.MyPostViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.MyPostView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import io.paperdb.Paper;


public class MyPostFragment extends Fragment {

    private DatabaseReference referenceMyPost,referenceGeneralPost;
    private FirebaseStorage MyStorage;
    private StorageReference referenceMyStorage;
    private RecyclerView recyclerView_my_posts;
    private HomePostImageViewAdapter homePostImageViewAdapter;
    private FirebaseRecyclerAdapter<MyPostView,MyPostViewHolder> adapterMyPost;

    public static MyPostFragment newInstance()
    {
        return new MyPostFragment();
    }
    public MyPostFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);
        //init database
        Paper.init(view.getContext());

        MyStorage = FirebaseStorage.getInstance();

        FirebaseDatabase databaseMyPost = FirebaseDatabase.getInstance();
        referenceMyPost = databaseMyPost.getReference("MyPost");

        FirebaseDatabase databaseGeneralPost = FirebaseDatabase.getInstance();
        referenceGeneralPost = databaseGeneralPost.getReference("UserPost");
        recyclerView_my_posts = view.findViewById(R.id.myPost_recylcerView_main);

        loadMyPost();

        return view;
    }

    private void loadMyPost() {
        try {
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MyPostView>()
                    .setQuery(referenceMyPost.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()), MyPostView.class)
                    .build();

            adapterMyPost = new FirebaseRecyclerAdapter<MyPostView, MyPostViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final MyPostViewHolder holder, int position, @NonNull final MyPostView model) {
                    holder.my_post_user_name.setText(model.getUserFullName());
                    holder.my_post_time.setText(model.getPostTime());
                    holder.my_post_book_description.setText(model.getUserBookDescription());
                    holder.my_post_user_book_name.setText(String.format("Book Name : %s", model.getUserBookName()));
                    holder.my_post_book_author.setText(String.format("Book Author : %s", model.getUserBookAuthor()));
                    Picasso.get().load(model.getUserImg()).error(R.mipmap.ic_launcher).into(holder.my_post_profile_img);
                    holder.my_post_user_level.setText(model.getUserLevel());
                    homePostImageViewAdapter = new HomePostImageViewAdapter(getContext(),model.getUserUploadBookList());
                    holder.user_edit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            PopupMenu popupMenu = new PopupMenu(v.getContext(),holder.user_edit_btn);
                            popupMenu.inflate(R.menu.edit_post_menu);
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId())
                                    {
                                        case R.id.item_delete:
                                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                            builder.setTitle("Alert!!!");
                                            builder.setMessage("Do you really wants to delete the post?");
                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        referenceMyPost.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(model.getPostId()).removeValue();
                                                        referenceGeneralPost.child(model.getPostId()).removeValue();
                                                        for (int i=0; i<model.getUserUploadBookList().size(); i++)
                                                        {
                                                            //Log.d("MyAccountFragment",model.getUserUploadBookList().get(i));
                                                            referenceMyStorage = MyStorage.getReferenceFromUrl(model.getUserUploadBookList().get(i));
                                                            referenceMyStorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("MyAccountFragment","Delete images successfully");
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                    // Uh-oh, an error occurred!
                                                                    Log.d("HomeFragment", "onFailure: did not delete file");
                                                                }
                                                            });
                                                        }
                                                        Toast.makeText(v.getContext(), "Post Deleted successfully.", Toast.LENGTH_SHORT).show();

                                                    }catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                            break;
                                        case R.id.item_edit:
                                            Intent intent = new Intent(v.getContext(), UploadActivity.class);
                                            //intent.putExtra("EditBookData",model);
                                            //intent.putExtra("BookEnableId",true);
                                            intent.putExtra("BookUpdateName",model.getUserBookName());
                                            intent.putExtra("BookUpdateDescription",model.getUserBookDescription());
                                            intent.putExtra("BookId",model.getPostId());
                                            intent.putExtra("BookUpdateBookAuthor",model.getUserBookAuthor());
                                            intent.putExtra("BookEnableId",true);
                                            intent.putStringArrayListExtra("BookImageList", (ArrayList<String>) model.getUserUploadBookList());
                                            intent.putStringArrayListExtra("superImageList", (ArrayList<String>) model.getUserUploadBookList());
                                            startActivity(intent);
                                            break;
                                    }
                                    return false;
                                }
                            });
                            popupMenu.show();

                        }
                    });
                    holder.my_post_home_images_recyclerView.setAdapter(homePostImageViewAdapter);
                    homePostImageViewAdapter.notifyDataSetChanged();
                    holder.my_post_home_images_recyclerView.setHasFixedSize(true);
                }

                @NonNull
                @Override
                public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item_view,parent,false);
                    return new MyPostViewHolder(view);
                }
            };
            adapterMyPost.startListening();
            recyclerView_my_posts.setAdapter(adapterMyPost);
            recyclerView_my_posts.setHasFixedSize(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
