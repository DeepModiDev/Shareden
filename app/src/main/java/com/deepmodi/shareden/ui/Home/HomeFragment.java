package com.deepmodi.shareden.ui.Home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.deepmodi.shareden.Adapter.HomePostImageViewAdapter;
import com.deepmodi.shareden.ChatActivity;
import com.deepmodi.shareden.FullScreenImageView;
import com.deepmodi.shareden.HomeActivity;
import com.deepmodi.shareden.Notifications.NotificationHelper;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.UploadActivity;
import com.deepmodi.shareden.ViewHolder.LatestBookViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.deepmodi.shareden.model.UserPost;
import com.deepmodi.shareden.model.UserRegister;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class HomeFragment extends Fragment implements LifecycleOwner {

    private RecyclerView recyclerView;
    private DatabaseReference reference, referenceRequest, referenceWhoami, referenceRequestStatus;
    private HomePostImageViewAdapter homePostImageViewAdapter;
    FirebaseDatabase database, databaseRequest, databaseWhoami, databaseRequestStatus;
    HomeViewModel viewModel;
    private ChatRequest request, requestStatus;
    private UserRegister userRegister;
    private ImageButton btn_chat;
    private FirebaseRecyclerAdapter<UserPost, LatestBookViewHolder> adapterData;

    private NotificationHelper notificationHelper;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);

        //Paper init
        Paper.init(v.getContext());

        //init Notification helper
        notificationHelper = new NotificationHelper(getActivity());
        //firebase init
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("UserPost");

        databaseRequest = FirebaseDatabase.getInstance();
        referenceRequest = databaseRequest.getReference("UserRequests");

        databaseWhoami = FirebaseDatabase.getInstance();
        referenceWhoami = databaseWhoami.getReference("Users");

        databaseRequestStatus = FirebaseDatabase.getInstance();
        referenceRequestStatus = databaseRequestStatus.getReference("UserRequests");

        userRegister = new UserRegister();
        requestStatus = new ChatRequest();

        final ImageButton float_create_post = v.findViewById(R.id.create_post);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(v.getContext());
        float_create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), UploadActivity.class));
                //UserPost userPost = new UserPost("Deep Modi","Teacher","img",getResources().getString(R.string.edit_user_desc_hint),"follow");
                //reference.child(String.valueOf(System.currentTimeMillis())).setValue(userPost);
            }
        });
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        btn_chat = v.findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(v.getContext(), ChatActivity.class));
                NotificationCompat.Builder nb = notificationHelper.getChannel1Builder("Channel 1 is on the way.","This is a channel 1");
                notificationHelper.getManager().notify(1,nb.build());

            }
        });

        loadList();
        loadDetails();
        return v;

    }

    private void loadList() {

        try {
            FirebaseRecyclerOptions<UserPost> options = new FirebaseRecyclerOptions.Builder<UserPost>()
                    .setQuery(reference, UserPost.class)
                    .build();

            final FirebaseRecyclerAdapter<UserPost, LatestBookViewHolder> adapter
                    = new FirebaseRecyclerAdapter<UserPost, LatestBookViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final LatestBookViewHolder holder, int position, @NonNull final UserPost model) {
                    holder.user_name.setText(model.getUserFullName());
                    holder.user_level.setText(model.getUserLevel());
                    holder.user_book_description.setText(model.getUserBookDescription());
                    holder.user_book_name.setText(String.format("Book Name : %s", model.getUserBookName()));
                    holder.user_book_author.setText(String.format("Book Author : %s", model.getUserBookAuthor()));
                    Picasso.get().load(model.getUserImg()).into(holder.profile_img);
                    holder.post_time.setText(model.getPostTime());
                    holder.profile_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), FullScreenImageView.class);
                            intent.putExtra("fullScreenImageUrl", model.getUserImg());
                            startActivity(intent);
                        }
                    });
                    referenceRequestStatus.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(model.getUserPhoneNumber()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                requestStatus = dataSnapshot.getValue(ChatRequest.class);
                                /*
                                    if (requestStatus.getSenderRequestStatus().equals("false")) {
                                        holder.user_follow_btn.setText("Requested");

                                    } else if (requestStatus.getSenderRequestStatus().equals("true")) {
                                        holder.user_follow_btn.setText("Following");
                                    } else if (requestStatus.getSenderRequestStatus().equals("follow")) {
                                        holder.user_follow_btn.setText("Follow");
                                    }
                                 */
                                }
                            catch (Exception e)
                            {
                                Log.e("HomeActivity",e.getMessage());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // Log.d("HomeFragment", String.valueOf(model.getUserUploadBookList()));
                    homePostImageViewAdapter = new HomePostImageViewAdapter(getContext(), model.getUserUploadBookList());
                    //homePostImageViewAdapter.setListImages(model.getUserUploadBookList());
                    holder.recyclerView.setAdapter(homePostImageViewAdapter);
                    holder.recyclerView.setHasFixedSize(true);
                    holder.user_follow_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(), "" + model.getUserFullName(), Toast.LENGTH_SHORT).show();
                            request = new ChatRequest(Paper.book().read(Common.USER_FINAL_NUMBER).toString(),
                                    userRegister.getUserName(),
                                    userRegister.getUserLevel(),
                                    userRegister.getUserImg(),
                                    model.getUserPhoneNumber(),
                                    model.getUserFullName(),
                                    model.getUserLevel(),
                                    model.getUserImg(),
                                    "false");
                            referenceRequest.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(model.getUserPhoneNumber()).setValue(request);
                            referenceRequest.child(model.getUserPhoneNumber()).child("MyRequests").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(request);

                        }
                    });

                    if (model.getUserPhoneNumber().equals(Paper.book().read(Common.USER_FINAL_NUMBER).toString())) {
                        holder.user_follow_btn.setVisibility(View.GONE);
                    }
                }

                @NonNull
                @Override
                public LatestBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_home_recycler_container, parent, false);
                    return new LatestBookViewHolder(view);
                }
            };
            adapter.startListening();
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDetails() {
        try {
            referenceWhoami.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userRegister = dataSnapshot.getValue(UserRegister.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
