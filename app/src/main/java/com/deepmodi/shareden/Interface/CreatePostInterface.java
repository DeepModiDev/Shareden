package com.deepmodi.shareden.Interface;

import android.view.View;

public interface CreatePostInterface {
     boolean onLongClick(View v,int position,boolean isLongClicked);
     void onClick(View v,int position,boolean isLongClicked);
}
