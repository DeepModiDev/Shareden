package com.deepmodi.shareden.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class FileSearch {

    /**
     * search for the directories and return all the path of the directories
     * @param directory
     * @return
     */

    public static ArrayList<String> getDirectoryPaths(String directory)
    {
        ArrayList<String> pathArray = new ArrayList<>();
        try
        {
            File file = new File(directory);
            File[] listFile = file.listFiles();
            assert listFile != null;
            for (File value : listFile) {
                if (value.isDirectory()) {
                    pathArray.add(value.getAbsolutePath());
                }
            }
            return pathArray;
        }catch (Exception e)
        {
            Log.e("FileSearch",e.getMessage());
        }
        return pathArray;
    }


    /**
     * this function is used for searching the files in the given directory
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory, Context context)
    {
        ArrayList<String> filePaths = new ArrayList<>();
        File files = new File(directory);
        File[] listFiles = files.listFiles();

        try {
            for (File listFile : listFiles) {
                if (listFile.isFile()) {
                    filePaths.add(listFile.getAbsolutePath());
                }
            }
        }catch (NullPointerException e)
        {
            Log.e("FileSearch :",e.getMessage());
            Toast.makeText(context, "No files found.", Toast.LENGTH_SHORT).show();
        }


        return filePaths;
    }
}
