package com.deepmodi.shareden.services;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.deepmodi.shareden.HomeActivity;
import com.deepmodi.shareden.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploadService extends BaseTaskService {

    private static final String TAG = "ImageUploadService";

    //Intent Action
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETE = "upload_complete";
    public static final String UPLOAD_ERROR = "upload_error";


    //Intent Extras
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    // [START declare_ref]
    private StorageReference mStorageRef;


    @Override
    public void onCreate() {
        super.onCreate();
        // [START get_storage_ref]
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // [END get_storage_ref]
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand:"+intent+":"+startId);
        if(ACTION_UPLOAD.equals(intent.getAction()))
        {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                getContentResolver().takePersistableUriPermission(Uri.parse("file://"+fileUri),Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            uploadFromUri(Uri.parse("file://"+fileUri));
        }
      return START_REDELIVER_INTENT;
    }

    private void uploadFromUri(final Uri fileUri)
    {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        taskStarted();
        showProgressNotification("Uploading",0,0);

        final StorageReference photoRef = mStorageRef.child("Demo Photos")
                .child(fileUri.getLastPathSegment());


        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(fileUri)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            showProgressNotification("Uploading...",
                            taskSnapshot.getBytesTransferred(),
                            taskSnapshot.getTotalByteCount());
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return photoRef.getDownloadUrl();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri){
                        broadcastUploadFinished(uri,fileUri);
                        taskCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                        broadcastUploadFinished(null,fileUri);
                        showUploadFinishedNotification(null,fileUri);
                        taskCompleted();
                    }
                });
    }

    private boolean broadcastUploadFinished(Uri downloadUrl,Uri fileUri)
    {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETE : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        // Hide the progress notification
        dismissProgressNotification();

        // Make Intent to MainActivity
        Intent intent = new Intent(this, HomeActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? "Successfully Uploaded" : "Fail to upload";
        showFinishedNotification(caption, intent, success);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETE);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }
}

