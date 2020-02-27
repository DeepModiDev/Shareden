package com.deepmodi.shareden.ui.ToRequest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepmodi.shareden.R;
import com.deepmodi.shareden.ViewHolder.RequestActivityViewHolder;
import com.deepmodi.shareden.ViewHolder.ToRequestViewHolder;
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

public class ToRequest extends Fragment {

    public ToRequest() {}
    protected FirebaseDatabase databaseToRequest;
    private DatabaseReference referenceToRequest;
    private ChatRequest request;
    private FirebaseRecyclerAdapter<ChatRequest, ToRequestViewHolder> adapterToRequest;
    private RecyclerView recyclerView_to_request;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {  request = new ChatRequest();

        //firebase and paper init
        View view = inflater.inflate(R.layout.fragment_to_request, container, false);

        //init Paper
        Paper.init(getActivity());

        databaseToRequest = FirebaseDatabase.getInstance();
        referenceToRequest = databaseToRequest.getReference("UserRequests");
        recyclerView_to_request = view.findViewById(R.id.to_request_recyclerview);

        loadData();

        return view;
    }

    private void loadData() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                .setQuery(referenceToRequest.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("ToRequests"),ChatRequest.class)
                .build();

        adapterToRequest = new FirebaseRecyclerAdapter<ChatRequest, ToRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ToRequestViewHolder holder, int position, @NonNull final ChatRequest model) {
                Picasso.get().load(model.getReceiverImage()).error(R.drawable.usr_img).into(holder.id_to_requested_user_image);
                holder.id_to_requested_user_name.setText(model.getReceiverName());
                holder.id_to_requested_user_level.setText(model.getReceiverLevel());
                holder.id_to_reject_user_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //FirebaseDatabase.getInstance().getReference("UserRequests").child(model.getReceiverName()).child("MyRequests").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).removeValue();
                    // FirebaseDatabase.getInstance().getReference("UserRequests").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("ToRequests").child(model.getSenderNumber()).removeValue();
                    FirebaseDatabase.getInstance().getReference("UserRequests").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("ToRequests").child(model.getReceiverNumber()).removeValue();
                    FirebaseDatabase.getInstance().getReference("UserRequests").child(model.getReceiverNumber()).child("MyRequests").child(model.getSenderNumber()).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public ToRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_view_to_request,parent,false);
                return new ToRequestViewHolder(view);
            }
        };
        adapterToRequest.startListening();
        adapterToRequest.notifyDataSetChanged();
        recyclerView_to_request.setAdapter(adapterToRequest);
    }
}
