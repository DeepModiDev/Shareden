package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MyPostViewHolder extends RecyclerView.ViewHolder {

    private String postId;
    private String userFullName;
    private String userLevel;
    private String userImg;
    private String userBookDescription;
    private String postTime;
    private List<String> userUploadBookList;
    private String userBookName;
    private String userBookAuthor;

    public RoundedImageView my_post_profile_img;
    public TextView my_post_user_name;
    public TextView my_post_user_level;
    public TextView my_post_user_book_name;
    public TextView my_post_book_author;
    public TextView my_post_book_description;
    public TextView my_post_time;
    public Button user_edit_btn;
    public RecyclerView my_post_home_images_recyclerView;

    public MyPostViewHolder(@NonNull View itemView) {
        super(itemView);
        my_post_profile_img = itemView.findViewById(R.id.id_my_post_profile_img);
        my_post_user_name = itemView.findViewById(R.id.id_my_post_full_name);
        my_post_user_level = itemView.findViewById(R.id.id_my_post_user_level);
        my_post_user_book_name = itemView.findViewById(R.id.id_my_post_book_name);
        my_post_book_description = itemView.findViewById(R.id.id_my_post_book_description);
        my_post_time = itemView.findViewById(R.id.id_my_post_time);
        user_edit_btn = itemView.findViewById(R.id.btn_edit_post);
        my_post_book_author = itemView.findViewById(R.id.id_my_post_book_author);

        my_post_home_images_recyclerView = itemView.findViewById(R.id.my_post_home_images_recyclerView);


    }
}
