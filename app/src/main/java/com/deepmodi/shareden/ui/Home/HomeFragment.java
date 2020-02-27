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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.deepmodi.shareden.ShowUserFullDetails;
import com.deepmodi.shareden.UploadActivity;
import com.deepmodi.shareden.ViewHolder.LatestBookViewHolder;
import com.deepmodi.shareden.ViewHolder.RecommendationViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.deepmodi.shareden.model.UserPost;
import com.deepmodi.shareden.model.UserRegisterClass;
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
    private DatabaseReference reference, referenceRequest, referenceWhoami,referenceRequestStatus,referenceUserRequest;
    private HomePostImageViewAdapter homePostImageViewAdapter;
    FirebaseDatabase database, databaseRequest, databaseWhoami, databaseRequestStatus,databaseUserRequest;
    HomeViewModel viewModel;
    private ChatRequest request, requestStatus;
    private UserRegisterClass userRegister;
    private ImageButton btn_chat;
    private FirebaseRecyclerAdapter<UserPost, LatestBookViewHolder> adapterData;
    private NotificationHelper notificationHelper;
    private SwipeRefreshLayout swipeRefreshLayout;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);

        //Paper initz
        Paper.init(v.getContext());

        //init Notificati on helper
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

        databaseUserRequest = FirebaseDatabase.getInstance();
        referenceUserRequest = databaseUserRequest.getReference("Users");


        //userRegister = new UserRegister();
        //requestStatus = new ChatRequest();

        RecyclerView suggestion_recyclerView = v.findViewById(R.id.suggestion_recyclerView);
        swipeRefreshLayout = v.findViewById(R.id.swipe_referesh_home);

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
                startActivity(new Intent(v.getContext(), ChatActivity.class));
                //NotificationCompat.Builder nb = notificationHelper.getChannel1Builder("Channel 1 is on the way.","This is a channel 1");
                //notificationHelper.getManager().notify(1,nb.build());
            }
        });

        loadList();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                loadList();
            }
        });
        loadDetails();
        return v;
    }

    private void loadList() {

        try {
            final FirebaseRecyclerOptions<UserPost> options = new FirebaseRecyclerOptions.Builder<UserPost>()
                    .setQuery(reference.orderByChild("postTime"), UserPost.class)
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
                    //holder.setIsRecyclable(false);
                    /*
                    if(model.getUserFollowStatus().equals("false"))
                    {
                        holder.user_follow_btn.setText("Requested");
                    }*/
                    holder.post_time.setText(model.getPostTime());
                    if (!model.getUserPhoneNumber().equals(Paper.book().read(Common.USER_FINAL_NUMBER).toString()))
                    {
                        holder.profile_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), ShowUserFullDetails.class);
                                intent.putExtra("userName",model.getUserFullName());
                                intent.putExtra("userNumber",model.getUserPhoneNumber());
                                intent.putExtra("userImage",model.getUserImg());
                                intent.putExtra("userLevel",model.getUserLevel());
                                startActivity(intent);
                            }
                        });
                    }

                    /*
                    referenceRequestStatus.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(model.getUserPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                requestStatus = dataSnapshot.getValue(ChatRequest.class);
                                    if (requestStatus.getSenderRequestStatus().equals("false")) {
                                        holder.user_follow_btn.setText("Requested");
                                    } else if (requestStatus.getSenderRequestStatus().equals("true")) {
                                        holder.user_follow_btn.setText("Following");
                                    } else if (requestStatus.getSenderRequestStatus().equals("follow")) {
                                        holder.user_follow_btn.setText("Follow");
                                    }
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

                     */
                    // Log.d("HomeFragment", String.valueOf(model.getUserUploadBookList()));
                    homePostImageViewAdapter = new HomePostImageViewAdapter(getContext(), model.getUserUploadBookList());
                    //homePostImageViewAdapter.setListImages(model.getUserUploadBookList());
                    holder.recyclerView.setAdapter(homePostImageViewAdapter);
                    holder.recyclerView.setHasFixedSize(true);
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

            Log.d("HomeFragment",String.valueOf(adapter.getItemCount()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDetails() {
        try {
            referenceWhoami.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userRegister = dataSnapshot.getValue(UserRegisterClass.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNotifications() {

    }

    private void loadSuggestions() {
        FirebaseDatabase databaseQuicAction = FirebaseDatabase.getInstance();
        DatabaseReference refQuick = databaseQuicAction.getReference("QuickAccess");

        FirebaseRecyclerOptions optionsQuick = new FirebaseRecyclerOptions.Builder<UserPost>()
                .setQuery(refQuick,UserPost.class)
                .build();


        final FirebaseRecyclerAdapter<UserPost, RecommendationViewHolder> adapter
                = new FirebaseRecyclerAdapter<UserPost, RecommendationViewHolder>(optionsQuick) {
            @Override
            protected void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position, @NonNull UserPost model) {

            }

            @NonNull
            @Override
            public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
    }
}
