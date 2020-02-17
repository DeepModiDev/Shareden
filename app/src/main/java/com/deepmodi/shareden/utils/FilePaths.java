package com.deepmodi.shareden.utils;

import android.os.Environment;

public class FilePaths {
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";
    public String STORIES = ROOT_DIR + "/Stories";
    public String FIREBASE_PHOTOS_STORAGE = "photos/users/";
}
