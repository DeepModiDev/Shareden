package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.R;

public class RecommendationViewHolder extends RecyclerView.ViewHolder {

    public Button btn_suggestion;

    public RecommendationViewHolder(@NonNull View itemView) {
        super(itemView);
        btn_suggestion = itemView.findViewById(R.id.btn_suggestion);
    }

}
