package com.deepmodi.shareden.ui.FromRequest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepmodi.shareden.ActivityUserRequest;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.ViewHolder.RequestActivityViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FromRequest extends Fragment {

    public FromRequest() {
        // Required empty public constructor
    }

    protected FirebaseDatabase databaseRequest,databaseAcceptRequest,databaseFollower,databaseFollowing;
    private DatabaseReference referenceRequest;
    private ChatRequest request;
    private FirebaseRecyclerAdapter<ChatRequest, RequestActivityViewHolder> adapter;
    private RecyclerView recyclerView_request;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_from_request, container, false);
        request  = new ChatRequest();

        //firebase and paper init
        Paper.init(getActivity());
        databaseRequest = FirebaseDatabase.getInstance();
        referenceRequest = databaseRequest.getReference("UserRequests");
        recyclerView_request = view.findViewById(R.id.request_recyclerview);

        referenceRequest.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("MyRequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(ChatRequest.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loadData();

        return view;


    }

    private void loadData() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                .setQuery(referenceRequest.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("MyRequests"),ChatRequest.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatRequest, RequestActivityViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestActivityViewHolder holder, int position, @NonNull final ChatRequest model) {
                Picasso.get().load(model.getSenderImage()).into(holder.id_requested_user_image);
                holder.id_requested_user_name.setText(model.getSenderName());
                holder.id_requested_user_level.setText(model.getSenderLevel());
                holder.id_requested_user_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                //FirebaseDatabase.getInstance().getReference("UserRequests").child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(request);
                                //FirebaseDatabase.getInstance().getReference("Followers").child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(request);
                                //FirebaseDatabase.getInstance().getReference("Following").child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(request);
                                //FirebaseDatabase.getInstance().getReference("UserRequests").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("MyRequests").child(model.getSenderNumber()).removeValue();
                                FirebaseDatabase.getInstance().getReference("Followers").child(model.getReceiverNumber()).child(model.getSenderNumber()).setValue(model);
                                FirebaseDatabase.getInstance().getReference("Following").child(model.getSenderNumber()).child(model.getReceiverNumber()).setValue(model);
                                FirebaseDatabase.getInstance().getReference("UserRequests").child(model.getSenderNumber()).child("ToRequests").child(model.getReceiverNumber()).removeValue();
                                FirebaseDatabase.getInstance().getReference("UserRequests").child(model.getReceiverNumber()).child("MyRequests").child(model.getSenderNumber()).removeValue();
                    }
                });

                holder.id_reject_user_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("UserRequests").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("MyRequests").child(model.getSenderNumber()).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public RequestActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_item_view_holder,parent,false);
                return new RequestActivityViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView_request.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        DatabaseReference.goOffline();
        super.onStop();
    }

    @Override
    public void onPause() {
        DatabaseReference.goOffline();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        DatabaseReference.goOffline();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        DatabaseReference.goOnline();
        super.onStart();
    }

    @Override
    public void onResume() {
        DatabaseReference.goOnline();
        super.onResume();
    }
}
