package com.deepmodi.shareden.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.Interface.CreatePostInterface;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.ViewHolder.CreatePostGridViewHolder;
import com.deepmodi.shareden.model.UIElement;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CreatePostRecyclerViewAdapter extends RecyclerView.Adapter<CreatePostGridViewHolder> {

    private ArrayList<String> URIs;
    private String append;
    private HashMap<Integer,String> selectedPath;
    private int temp=0;
    private List<UIElement> uiElements;
    private ImageView imageView;
    private Context context;
    private ProgressBar progressBar;
    private List<String> sendImages;

    public CreatePostRecyclerViewAdapter(ArrayList<String> URIs, String append,HashMap<Integer,String> selectedPath) {
        this.URIs = URIs;
        this.append = append;
        this.selectedPath = selectedPath;
    }

    public CreatePostRecyclerViewAdapter(ArrayList<String> URIs, String append, List<UIElement> uiElements) {
        this.URIs = URIs;
        this.append = append;
        this.uiElements = uiElements;
    }

    public CreatePostRecyclerViewAdapter(ArrayList<String> URIs, String append, List<UIElement> uiElements, ImageView imageView, Context context,ProgressBar progressBar,List<String> sendImages) {
        this.URIs = URIs;
        this.append = append;
        this.uiElements = uiElements;
        this.imageView = imageView;
        this.context = context;
        this.progressBar = progressBar;
        this.sendImages = sendImages;
    }

    @NonNull
    @Override
    public CreatePostGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridlayout,parent,false);
        return new CreatePostGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreatePostGridViewHolder holder, int position) {
        final UIElement model = uiElements.get(position);
        //holder.setIsRecyclable(false);
        Picasso.get().load("file://"+URIs.get(position)).resize(100,100).centerCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.grid_images);
        holder.setCreatePostInterface(new CreatePostInterface() {
            @Override
            public boolean onLongClick(View v, int position, boolean isLongClicked) {
                return false;
            }
            @Override
            public void onClick(View v, int position, boolean isLongClicked) {
                model.setSelected(!model.isSelected());
                holder.itemView.setBackgroundColor(model.isSelected() ? Color.RED : Color.WHITE);
                Toast.makeText(v.getContext(), ""+uiElements.get(position).isSelected(), Toast.LENGTH_SHORT).show();
                if(uiElements.get(position).isSelected())
                {
                    if(!sendImages.contains(URIs.get(position)))
                    {
                        sendImages.add(URIs.get(position));
                    }
                }
                else
                {
                   if(sendImages.size() > 0 )
                   {
                       sendImages.remove(URIs.get(position));
                   }
                }
                setImage(URIs.get(position),imageView,"file://");
            }
        });
    }

    private void setImage(String imgURL, ImageView image, String append) {
        Log.d(TAG, "setImage: setting image");
        Picasso.get().load(append+imgURL).into(image, new Callback() {
            @Override
            public void onSuccess() {
               progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return URIs.size();
    }

    public int selectedItemCount()
    {
       return uiElements == null ? 0 : uiElements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<String> getSelectedImagesURIList()
    {
        return sendImages;
    }
}
