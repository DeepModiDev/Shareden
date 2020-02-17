package com.deepmodi.shareden.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.deepmodi.shareden.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class GridViewImageAdapter extends ArrayAdapter<String> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private ArrayList<String> imageUrls;

    public GridViewImageAdapter(@NonNull Context context,int resource,ArrayList<String> imageUrls) {
        super(context, resource, imageUrls);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.imageUrls = imageUrls;
    }

    private static class ViewHolder
    {
        ImageView galleryImageViewholder;
        ProgressBar galleryProgressViewHolder;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = inflater.inflate(resource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.galleryImageViewholder = convertView.findViewById(R.id.gridImageView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String imageURL = getItem(position);
        Picasso.get()
                .load("file:///"+imageURL)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.galleryImageViewholder);
        return convertView;
    }
}
