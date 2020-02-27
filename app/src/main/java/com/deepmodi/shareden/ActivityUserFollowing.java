package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepmodi.shareden.ViewHolder.FollowingViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ActivityUserFollowing extends AppCompatActivity {

    FirebaseRecyclerAdapter<ChatRequest, FollowingViewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_following);
        recyclerView = findViewById(R.id.recyclerView_following);
        //Paper init
        Paper.init(this);
        loadData();
    }

    private void loadData()
    {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Following").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()),ChatRequest.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatRequest, FollowingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FollowingViewHolder holder, int position, @NonNull final ChatRequest model) {
                holder.id_following_request_username.setText(model.getReceiverName());
                holder.id_following_request_userlevel.setText(model.getReceiverLevel());
                Picasso.get().load(model.getReceiverImage()).error(R.drawable.usr_img).placeholder(R.drawable.usr_img).into(holder.id_following_request_userImage);
                holder.id_following_btn_unfollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUserFollowing.this);
                        builder.setCancelable(false);
                        builder.setMessage("Do you really wants to unfollow "+model.getReceiverName()+"?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("Following").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(model.getReceiverNumber()).removeValue();
                                FirebaseDatabase.getInstance().getReference("Followers").child(model.getReceiverNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).removeValue();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itm_user_following, parent, false);
                return new FollowingViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
