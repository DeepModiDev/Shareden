package com.deepmodi.shareden.ui.Search;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deepmodi.shareden.Adapter.HomePostImageViewAdapter;
import com.deepmodi.shareden.FullScreenImageView;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.ViewHolder.LatestBookViewHolder;
import com.deepmodi.shareden.model.MyPostView;
import com.deepmodi.shareden.model.UserPost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private RecyclerView recyclerView_search;
    private FirebaseDatabase databaseSearch;
    private DatabaseReference referenceSearch;
    private EditText edit_text_search;
    private AppCompatImageButton search_btn;
    private String searchData;
    private HomePostImageViewAdapter homePostImageViewAdapter;
    private FirebaseRecyclerAdapter<UserPost, LatestBookViewHolder> adapterDefault;
    private FirebaseRecyclerAdapter<UserPost,LatestBookViewHolder> adapterSearch;
    private SwipeRefreshLayout swipe_referesh_search;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);
        recyclerView_search = v.findViewById(R.id.search_book);
        edit_text_search = v.findViewById(R.id.edit_text_search);
        search_btn = v.findViewById(R.id.btn_click_search);
        swipe_referesh_search = v.findViewById(R.id.swipe_referesh_search);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData = edit_text_search.getText().toString();
                loadData(searchData);
            }
        });

        edit_text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    searchData = edit_text_search.getText().toString();
                    loadData(searchData);
                    return true;
                }
                return false;
            }

        });
        databaseSearch = FirebaseDatabase.getInstance();
        referenceSearch  = databaseSearch.getReference("UserPost");

        loadDefaultData();
        swipe_referesh_search.setColorSchemeResources(R.color.colorAccent);
        swipe_referesh_search.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_referesh_search.setRefreshing(false);
                loadDefaultData();
            }
        });

        return v;
    }

    private void loadDefaultData()
    {
        FirebaseRecyclerOptions optionsDefaut = new FirebaseRecyclerOptions.Builder<UserPost>()
              .setQuery(referenceSearch,UserPost.class)
               .build();

        adapterDefault = new FirebaseRecyclerAdapter<UserPost, LatestBookViewHolder>(optionsDefaut) {
            @Override
            protected void onBindViewHolder(@NonNull LatestBookViewHolder holder, int position, @NonNull final UserPost model) {
                holder.user_name.setText(model.getUserFullName());
                holder.user_follow_btn.setText(model.getUserFollowStatus());
                holder.user_level.setText(model.getUserLevel());
                holder.user_book_description.setText(model.getUserBookDescription());
                holder.user_follow_btn.setVisibility(View.GONE);
                holder.user_book_name.setText(String.format("Book Name : %s", model.getUserBookName()));
                holder.user_book_author.setText(String.format("Book Author : %s", model.getUserBookAuthor()));
                Picasso.get().load(model.getUserImg()).into(holder.profile_img);
                holder.post_time.setText(model.getPostTime());
                holder.profile_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FullScreenImageView.class);
                        intent.putExtra("fullScreenImageUrl", model.getUserImg());
                        startActivity(intent);
                    }
                });
               // Log.d("HomeFragment", String.valueOf(model.getUserUploadBookList()));
                homePostImageViewAdapter = new HomePostImageViewAdapter(getContext(), model.getUserUploadBookList());
                holder.recyclerView.setAdapter(homePostImageViewAdapter);
                holder.recyclerView.setHasFixedSize(true);
            }

            @NonNull
            @Override
            public LatestBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_home_recycler_container,parent,false);
                return new LatestBookViewHolder(view);
            }
        };
        adapterDefault.startListening();
        adapterDefault.notifyDataSetChanged();
        recyclerView_search.setAdapter(adapterDefault);

    }

    private void loadData(String search)
    {
        FirebaseRecyclerOptions optionSearch = new FirebaseRecyclerOptions.Builder<UserPost>()
                .setQuery(referenceSearch.orderByChild("userBookName").startAt(search).endAt(search+"\uf8ff"),UserPost.class)
                .build();

        adapterSearch = new FirebaseRecyclerAdapter<UserPost, LatestBookViewHolder>(optionSearch) {
            @Override
            protected void onBindViewHolder(@NonNull LatestBookViewHolder holder, int position, @NonNull final UserPost model) {
                holder.user_name.setText(model.getUserFullName());
                holder.user_follow_btn.setText(model.getUserFollowStatus());
                holder.user_level.setText(model.getUserLevel());
                holder.user_book_description.setText(model.getUserBookDescription());
                holder.user_follow_btn.setVisibility(View.GONE);
                holder.user_book_name.setText(String.format("Book Name : %s", model.getUserBookName()));
                holder.user_book_author.setText(String.format("Book Author : %s", model.getUserBookAuthor()));
                Picasso.get().load(model.getUserImg()).into(holder.profile_img);
                holder.post_time.setText(model.getPostTime());
                holder.profile_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FullScreenImageView.class);
                        intent.putExtra("fullScreenImageUrl", model.getUserImg());
                        startActivity(intent);
                    }
                });
                // Log.d("HomeFragment", String.valueOf(model.getUserUploadBookList()));
                homePostImageViewAdapter = new HomePostImageViewAdapter(getContext(), model.getUserUploadBookList());
                holder.recyclerView.setAdapter(homePostImageViewAdapter);
                holder.recyclerView.setHasFixedSize(true);
            }

            @NonNull
            @Override
            public LatestBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_home_recycler_container,parent,false);
                return new LatestBookViewHolder(view);
            }
        };
        adapterSearch.startListening();
        adapterSearch.notifyDataSetChanged();
        recyclerView_search.setAdapter(adapterSearch);

    }
}
