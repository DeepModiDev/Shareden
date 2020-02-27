package com.deepmodi.shareden;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.deepmodi.shareden.Adapter.CreatePostRecyclerViewAdapter;
import com.deepmodi.shareden.Adapter.UploadImageRecyclerViewAdapter;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ImagesList;
import com.deepmodi.shareden.model.MyPostView;
import com.deepmodi.shareden.model.UIElement;
import com.deepmodi.shareden.model.User;
import com.deepmodi.shareden.model.UserPost;
import com.deepmodi.shareden.model.UserRegisterClass;
import com.deepmodi.shareden.utils.FilePaths;
import com.deepmodi.shareden.utils.FileSearch;
import com.deepmodi.shareden.utils.FileUtil;
import com.google.android.gms.common.data.EntityBuffer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.Logger;
import id.zelory.compressor.Compressor;
import io.paperdb.Paper;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = "UploadActivity";
    Button btn_upload_book, btn_send_img;
    RoundedImageView profile_img_upload;
    EditText id_user_upload_book_name;
    EditText id_user_upload_book_author;
    EditText id_user_upload_book_description;
    FloatingActionButton fab_attach;
    BottomSheetBehavior behavior;
    ImageButton img_close;
    Spinner select_path_spinner;

    private ImagesList imagesList, imagesUpdatedList;

    private ArrayList<String> directories;
    private String appends = "file:/";
    private int STORAGE_PERMISSION_CODE = 123;
    private static final int NUM_GRID_COLUMN = 3;

    ProgressBar progressBar;
    ImageView galleryImageView;
    GridView gridView;
    String mSelectedImage;

    private String[] select_book_type ={"Select your book type","Novel","Engineering","Medical","Commerce","Arts","11th Std","12th Std","Other"};

    List<String> selectedUrls = new ArrayList<>();
    List<String> UpdateSelectedUrl = new ArrayList<>();
    List<UIElement> uiElements;
    List<String> finalSendList = new ArrayList<>();
    private List<String> finalUploadImageList = new ArrayList<>();
    private List<String> finalUpdateUploadList = new ArrayList<>();
    private List<String> superUploadList = new ArrayList<>();

    RecyclerView recyclerView_selected_img;
    RecyclerView.LayoutManager manager;
    UploadImageRecyclerViewAdapter UploadAdapter, UpdateImageAdapter;

    RecyclerView create_post_grid_recyclerView;
    RecyclerView.LayoutManager createPost_grid_manager;
    CreatePostRecyclerViewAdapter createPostRecyclerViewAdapter;


    protected FirebaseDatabase databaseUploadBook;
    protected DatabaseReference referenceUploadBook;

    protected FirebaseStorage storage;
    private StorageReference referenceStorage;

    protected FirebaseDatabase databaseUserCall, databaseMyPost;
    protected DatabaseReference referenceUserCall, referenceMyPost;
    private UserRegisterClass register;

    private Date date = new Date();
    Intent intentGetInfo;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String bookId;
    private int p;
    private UserPost post;
    private MyPostView postView;
    private String user_selected_book_type;
    private BottomSheetDialog bottomSheetDialog;
    private ConstraintLayout parentLayout;

    File photoFile = null;
    private String capturedImagePath;
    public static final int CAMERA_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar_upload = findViewById(R.id.toolbar_upload);
        setSupportActionBar(toolbar_upload);

        intentGetInfo = getIntent();
        //postView = intentGetInfo.getParcelableExtra("EditBookData");
        //request storage permission
        requestStoragePermission();
        Paper.init(this);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE , dd-MM-yyyy , hh:mm");

        //recyclerView setup
        databaseUploadBook = FirebaseDatabase.getInstance();
        referenceUploadBook = databaseUploadBook.getReference("UserPost");

        storage = FirebaseStorage.getInstance();
        referenceStorage = storage.getReference("UsersPostImg");

        databaseUserCall = FirebaseDatabase.getInstance();
        referenceUserCall = databaseUserCall.getReference("Users");

        databaseMyPost = FirebaseDatabase.getInstance();
        referenceMyPost = databaseMyPost.getReference("MyPost");
        finalUpdateUploadList = new ArrayList<>();

        createPost_grid_manager = new GridLayoutManager(this, 3);
        create_post_grid_recyclerView = findViewById(R.id.gridView);
        create_post_grid_recyclerView.setHasFixedSize(true);
        create_post_grid_recyclerView.setLayoutManager(createPost_grid_manager);

        manager = new LinearLayoutManager(this);
        recyclerView_selected_img = findViewById(R.id.recycler_View_selected_img);
        recyclerView_selected_img.setLayoutManager(manager);
        recyclerView_selected_img.setHasFixedSize(true);

        parentLayout = findViewById(R.id.constraint_parent_1);

        id_user_upload_book_name = findViewById(R.id.id_user_upload_book_name);
        id_user_upload_book_author = findViewById(R.id.id_user_upload_book_author);
        id_user_upload_book_description = findViewById(R.id.id_user_upload_book_description);
        AppCompatSpinner spinner_select_book_level = findViewById(R.id.appCompatSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,select_book_type);
        spinner_select_book_level.setAdapter(adapter);

        spinner_select_book_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UploadActivity.this, ""+select_book_type[position], Toast.LENGTH_SHORT).show();
                user_selected_book_type = select_book_type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fab_attach = findViewById(R.id.fab_attach);
        //gridView = findViewById(R.id.gridView);
        progressBar = findViewById(R.id.progressBar);
        galleryImageView = findViewById(R.id.galleryImageView);

        imagesList = new ImagesList();
        imagesUpdatedList = new ImagesList();

        if (intentGetInfo != null) {
            id_user_upload_book_name.setText(intentGetInfo.getStringExtra("BookUpdateName"));
            id_user_upload_book_author.setText(intentGetInfo.getStringExtra("BookUpdateBookAuthor"));
            id_user_upload_book_description.setText(intentGetInfo.getStringExtra("BookUpdateDescription"));
            UpdateSelectedUrl = intentGetInfo.getStringArrayListExtra("BookImageList");
            superUploadList = intentGetInfo.getStringArrayListExtra("superImageList");
            bookId = intentGetInfo.getStringExtra("BookId");
        }

        /*
        if(postView != null)
        {
            id_user_upload_book_name.setText(postView.getUserBookName());
            id_user_upload_book_author.setText(postView.getUserBookAuthor());
            id_user_upload_book_description.setText(postView.getUserBookDescription());
            UpdateSelectedUrl = postView.getUserUploadBookList();
            superUploadList = postView.getUserUploadBookList();
            bookId = postView.getPostId();
        }
         */

        //bottom sheet view
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
        View constraintLayout = coordinatorLayout.findViewById(R.id.bottom_sheet);
        img_close = findViewById(R.id.close_btn);
        select_path_spinner = findViewById(R.id.spinner_select_path);

        behavior = BottomSheetBehavior.from(constraintLayout);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }


        });
        //init all ui components
        btn_send_img = findViewById(R.id.btn_send_img);
        btn_upload_book = findViewById(R.id.btn_upload_book);
        profile_img_upload = findViewById(R.id.profile_img_upload);
        try {
            Picasso.get().load(Paper.book().read(Common.USER_IMAGE_LINK).toString()).into(profile_img_upload);
        } catch (Exception e) {
            Log.e(TAG,Objects.requireNonNull(e.getMessage()));
        }

        //This will be display bottom sheet which contains 2 options like select camera or select image from gallery.

        fab_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog = new BottomSheetDialog(UploadActivity.this,R.style.BottomSheetDialogTheme);
                View view = LayoutInflater.from(UploadActivity.this).inflate(R.layout.bottom_sheet_select_item,parentLayout,false);
                bottomSheetDialog.setContentView(view);
                view.findViewById(R.id.btn_start_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCamera();
                        bottomSheetDialog.dismiss();
                    }
                });

                view.findViewById(R.id.btn_select_from_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        btn_send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                /**
                 * Very Important function is below
                 */
                setSelectedImagesIntoList();
                /**
                 * Very Important function is above
                 */

                if (intentGetInfo.getBooleanExtra("BookEnableId", false)) {
                    UpdateUploadedBook();
                } else {
                    loadSelectedImages();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadSpinner();
        }

        btn_upload_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!id_user_upload_book_name.getText().toString().isEmpty()) {
                    if (!id_user_upload_book_author.getText().toString().isEmpty()) {

                        if (!id_user_upload_book_description.getText().toString().isEmpty()) {
                            if (intentGetInfo.getBooleanExtra("BookEnableId", false)) {
                                //UpdateUploadedBook();
                                UploadUpdatedBook();
                            } else {
                                uploadBook();
                            }
                        } else {
                            id_user_upload_book_description.requestFocus();
                            id_user_upload_book_description.setError("Please enter book description.");
                        }
                    } else {
                        id_user_upload_book_author.requestFocus();
                        id_user_upload_book_author.setError("Please enter book author or publication name.");
                    }
                } else {
                    id_user_upload_book_name.requestFocus();
                    id_user_upload_book_name.setError("Please enter book name.");
                }
            }
        });

        loadUserInfo();

        try {
            if (intentGetInfo.getBooleanExtra("BookEnableId", false)) {
                UpdateUploadedBook();
            } else {
                loadSelectedImages();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //Methods for capture image and save images

    private void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { captureImage(); } else { captureImage2(); }
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.deepmodi.shareden.UploadActivity.fileprovider"
                ,photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent,CAMERA_REQUEST_CODE);
            }
        }catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
    }

    private void captureImage2() {
        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile4();
            if(photoFile!=null)
            {
                Log.i(TAG,photoFile.getAbsolutePath());
                Uri photoURI  = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        } catch (Exception e) { Log.e(TAG,e.getMessage()); }
    }

    private File createImageFile4() throws IOException{
        File mediaStoreDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Shareden");
        if(!mediaStoreDir.exists())
        {
            if(!mediaStoreDir.mkdirs())
            {
                Toast.makeText(this, "Unable to create dir.", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStoreDir.getPath()+File.separator + "IMG_"+timeStamp+".jpg");
        return mediaFile;
    }

    private File createImageFile() throws IOException {
        String currentTimeStamp  = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + currentTimeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, //prefix
                ".jpg", //suffix
                storageDir);
        capturedImagePath = image.getAbsolutePath();
        return image;
    }

    //Methods for capture image and save images

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for load the images from your phone.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void loadSpinner() {
        FilePaths filePaths = new FilePaths();
        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        directories.add(filePaths.CAMERA);

        ArrayList<String> directoriesName = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            int index = directories.get(i).lastIndexOf("/") + 1;
            String string = directories.get(i).substring(index);
            directoriesName.add(string);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UploadActivity.this, android.R.layout.simple_spinner_item, directoriesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_path_spinner.setAdapter(adapter);

        select_path_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadGridView(String selectedDirectory) {
        final ArrayList<String> imageUrls = FileSearch.getFilePaths(selectedDirectory, this);
        createPostRecyclerViewAdapter = new CreatePostRecyclerViewAdapter(imageUrls, appends, getSelectedListData(imageUrls.size()), galleryImageView, UploadActivity.this, progressBar, finalSendList);
        create_post_grid_recyclerView.setAdapter(createPostRecyclerViewAdapter);
        try {
            if (imageUrls.size() > 0) {
                setImage(imageUrls.get(0), galleryImageView, "file://");
            } else {
                setImage(String.valueOf(R.mipmap.ic_launcher), galleryImageView, "file://");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " + e.getMessage());
        }
    }

    private List<UIElement> getSelectedListData(int size) {
        uiElements = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            uiElements.add(new UIElement(String.valueOf(i)));
        }
        return uiElements;
    }

    private void setImage(String imgURL, ImageView image, String append) {
        Log.d(TAG, "setImage: setting image");
        Picasso.get().load(append + imgURL).into(image, new Callback() {
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

    private void loadSelectedImages() {
        UploadAdapter = new UploadImageRecyclerViewAdapter(selectedUrls, UploadActivity.this);
        recyclerView_selected_img.setAdapter(UploadAdapter);
        UploadAdapter.notifyDataSetChanged();
    }

    private void UpdateUploadedBook() {
        try {
            if (UpdateSelectedUrl.size() > 0) {
                UpdateImageAdapter = new UploadImageRecyclerViewAdapter(UpdateSelectedUrl, this, intentGetInfo.getBooleanExtra("BookEnableId", false), finalSendList, superUploadList);
                recyclerView_selected_img.setAdapter(UpdateImageAdapter);
                UpdateImageAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectedImagesIntoList() {

        if (intentGetInfo.getBooleanExtra("BookEnableId", false)) {
            try {
                for (int i = 0; i < finalSendList.size(); i++) {
                    //Log.d(TAG,"outside if :"+String.valueOf(finalSendList.get(i)));
                    if (!UpdateSelectedUrl.contains("file://" + finalSendList.get(i))) {
                        UpdateSelectedUrl.add("file://" + finalSendList.get(i));
                        //Log.d(TAG,"inside if :"+String.valueOf(UpdateSelectedUrl));
                    }
                    //Log.d(TAG,"This is a main function :"+String.valueOf(UpdateSelectedUrl));
                }

                if (UpdateSelectedUrl.size() > 0)
                {
                    try {

                        //Firebase vision for recognizing text from the image.
                        FirebaseVisionImage visionImage = FirebaseVisionImage.fromFilePath(this,Uri.parse("file://"+UpdateSelectedUrl.get(0)));
                        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                        detector.processImage(visionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                firebaseVisionText.getText();
                                if(firebaseVisionText.getTextBlocks().size() > 1)
                                {
                                    try {
                                        String bookName = firebaseVisionText.getTextBlocks().get(0).getText() +" "+ firebaseVisionText.getTextBlocks().get(1).getText();
                                        id_user_upload_book_name.setText(bookName);

                                        int bookSecondLastPosition = firebaseVisionText.getTextBlocks().size() - 1;
                                        String bookDescription = firebaseVisionText.getTextBlocks().get(bookSecondLastPosition - 1).getText();
                                        id_user_upload_book_description.setText(bookDescription);

                                    }catch (Exception e)
                                    {
                                        Log.e("UploadActivity", Objects.requireNonNull(e.getMessage()));
                                    }
                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("UploadActivity",Objects.requireNonNull(e.getMessage()));
                                    }
                                });
                    }catch(Exception e)
                    {
                        Log.e("UploadActivity.class",Objects.requireNonNull(e.getMessage()));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < finalSendList.size(); i++) {
                if (!selectedUrls.contains(finalSendList.get(i))) {
                    selectedUrls.add(finalSendList.get(i));
                }
            }

            if (selectedUrls.size() > 0)
            {
                try {
                    FirebaseVisionImage visionImage = FirebaseVisionImage.fromFilePath(this,Uri.parse("file://"+selectedUrls.get(0)));
                    FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                    detector.processImage(visionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            firebaseVisionText.getText();
                            if(firebaseVisionText.getTextBlocks().size() > 1)
                            {
                                try {
                                    String bookName = firebaseVisionText.getTextBlocks().get(0).getText() +" "+ firebaseVisionText.getTextBlocks().get(1).getText();
                                    id_user_upload_book_name.setText(bookName);

                                    int bookSecondLastPosition = firebaseVisionText.getTextBlocks().size() - 1;
                                    String bookDescription = firebaseVisionText.getTextBlocks().get(bookSecondLastPosition - 1).getText();
                                    id_user_upload_book_description.setText(bookDescription);

                                }catch (Exception e)
                                {
                                    Log.e("UploadActivity", Objects.requireNonNull(e.getMessage()));
                                }
                            }
                        }
                    })
                   .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("UploadActivity",Objects.requireNonNull(e.getMessage()));
                                }
                            });
                }catch(Exception e)
                {
                    Log.e("UploadActivity.class",Objects.requireNonNull(e.getMessage()));
                }
            }
            }
    }

    private void uploadBook() {
        final ProgressDialog dialog = new ProgressDialog(this);
        if (selectedUrls.size() > 0) {
            final int time = (int) System.currentTimeMillis();
            //Log.d(TAG, "INSIDE THE IF FUNCTION");
            for (int i = 0; i < selectedUrls.size(); i++) {
                try {
                    //Log.d(TAG, "INSIDE THE FOR LOOP");
                    final File finalImage = FileUtil.from(this, Uri.parse("file://" + selectedUrls.get(i)));
                    Log.d(TAG, "selected images final list " + selectedUrls.get(i));
                    File compressedImage = new Compressor(this)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(finalImage);

                    final StorageReference ref = referenceStorage.child("images/" + Paper.book().read(Common.USER_FINAL_NUMBER)).child(String.valueOf(System.currentTimeMillis()));
                    ref.putFile(Uri.fromFile(compressedImage))
                            .addOnProgressListener(
                                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                            try {
                                                double progress = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                dialog.setMessage("Loading : " + (float) progress + "%");
                                                dialog.show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            //Log.d(TAG, "INSIDE THE ON PREGRESS LISTNER");
                                        }
                                    })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    dialog.dismiss();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            finalUploadImageList.add(uri.toString());
                                            // Log.d(TAG, "INSIDE THE ON SUCESS LISTNER" + finalUploadImageList);
                                            try {
                                                imagesList.setImagesList(finalUploadImageList);
                                                post = new UserPost(
                                                        register.getUserName(),
                                                        register.getUserLevel(),
                                                        Paper.book().read(Common.USER_IMAGE_LINK).toString(),
                                                        id_user_upload_book_description.getText().toString(),
                                                        "temp",
                                                        String.valueOf(time),
                                                        simpleDateFormat.format(calendar.getTime()),
                                                        imagesList.getImagesList(),
                                                        id_user_upload_book_name.getText().toString(),
                                                        id_user_upload_book_author.getText().toString(),
                                                        Paper.book().read(Common.USER_FINAL_NUMBER).toString());
                                            } catch (Exception e) {
                                                imagesList.setImagesList(finalUploadImageList);
                                                post = new UserPost(
                                                        register.getUserName(),
                                                        register.getUserLevel(),
                                                        "https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png",
                                                        id_user_upload_book_description.getText().toString(),
                                                        "temp",
                                                        String.valueOf(time),
                                                        simpleDateFormat.format(calendar.getTime()),
                                                        imagesList.getImagesList(),
                                                        id_user_upload_book_name.getText().toString(),
                                                        id_user_upload_book_author.getText().toString(),
                                                        Paper.book().read(Common.USER_FINAL_NUMBER).toString());

                                            }

                                            referenceUploadBook.child(String.valueOf(time)).setValue(post);
                                            referenceMyPost.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(String.valueOf(time)).setValue(post);
                                            Toast.makeText(UploadActivity.this, "Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                                            btn_upload_book.setClickable(false);
                                            btn_upload_book.setText("Done");
                                            btn_upload_book.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    // Log.d(TAG, "INSIDE THE FAILER LISTNER");
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG,Objects.requireNonNull(e.getMessage()));
                }
            }
            dialog.show();
        } else {
            Toast.makeText(this, "You must need to upload at least 1 image.", Toast.LENGTH_LONG).show();
        }
    }

    private void loadUserInfo() {

        referenceUserCall.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                register = dataSnapshot.getValue(UserRegisterClass.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UploadUpdatedBook() {
        //UpdateUploadedBook();
        final ProgressDialog dialog = new ProgressDialog(this);
        final int time = (int) System.currentTimeMillis();
        if (finalSendList.size() > 0) {
            // finalUpdateUploadList.addAll(UpdateSelectedUrl);
            for (p = 0; p < finalSendList.size(); p++) {
               //Log.d(TAG, "Contains Sub link : " + finalSendList.get(p));
                try {
                    //Log.d(TAG, "INSIDE THE FOR LOOP");
                    final File finalImage = FileUtil.from(this, Uri.parse("file://" + finalSendList.get(p)));
                    //Log.d(TAG,"selected images final list "+selectedUrls.get(i));
                    File compressedImage = new Compressor(this)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(finalImage);

                    final StorageReference ref = referenceStorage.child("images/" + Paper.book().read(Common.USER_FINAL_NUMBER)).child(String.valueOf(System.currentTimeMillis()));
                    ref.putFile(Uri.fromFile(compressedImage))
                            .addOnProgressListener(
                                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                            dialog.setMessage("Loading : " + (float) progress + "%");
                                            dialog.setCancelable(false);
                                            dialog.show();
                                            //Log.d(TAG, "INSIDE THE ON PREGRESS LISTNER");
                                        }
                                    })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    dialog.dismiss();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            finalUpdateUploadList.add(uri.toString());
                                            for (int i = 0; i < finalUpdateUploadList.size(); i++) {
                                                if (!superUploadList.contains(finalUpdateUploadList.get(i))) {
                                                    superUploadList.add(finalUpdateUploadList.get(i));
                                                    Log.d(TAG, "Contains Link : " + superUploadList);
                                                    imagesList.setImagesList(superUploadList);
                                                    if(register.getUserImg()==null)
                                                    {
                                                        register.setUserImg("https://firebasestorage.googleapis.com/v0/b/shareden.appspot.com/o/usr_img.png?alt=media&token=d9e48a3e-dd3d-4383-957d-5bbc98597972");
                                                    }
                                                    UserPost post = new UserPost(
                                                            register.getUserName(),
                                                            register.getUserLevel(),
                                                            register.getUserImg(),
                                                            id_user_upload_book_description.getText().toString(),
                                                            "temp",
                                                            bookId,
                                                            simpleDateFormat.format(calendar.getTime()),
                                                            imagesList.getImagesList(),
                                                            id_user_upload_book_name.getText().toString(),
                                                            id_user_upload_book_author.getText().toString(),
                                                            Paper.book().read(Common.USER_FINAL_NUMBER).toString(),
                                                            user_selected_book_type);

                                                    referenceUploadBook.child(bookId).setValue(post);
                                                    referenceMyPost.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(bookId).setValue(post);
                                                    btn_upload_book.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    // Log.d(TAG, "INSIDE THE FAILER LISTNER");
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (UpdateSelectedUrl.size() > 0) {
                if(register.getUserImg() == null)
                {
                    register.setUserImg("https://firebasestorage.googleapis.com/v0/b/shareden.appspot.com/o/usr_img.png?alt=media&token=d9e48a3e-dd3d-4383-957d-5bbc98597972");
                }
                imagesList.setImagesList(UpdateSelectedUrl);
               /* UserPost post = new UserPost(
                        register.getUserName(),
                        register.getUserLevel(),
                        register.getUserImg(),
                        id_user_upload_book_description.getText().toString(),
                        "temp",
                        bookId,
                        simpleDateFormat.format(calendar.getTime()),
                        imagesList.getImagesList(),
                        id_user_upload_book_name.getText().toString(),
                        id_user_upload_book_author.getText().toString(),
                        Paper.book().read(Common.USER_FINAL_NUMBER).toString());
                */

                UserPost post = new UserPost(
                        register.getUserName(),
                        register.getUserLevel(),
                        register.getUserImg(),
                        id_user_upload_book_description.getText().toString(),
                        "temp",
                        bookId,
                        simpleDateFormat.format(calendar.getTime()),
                        imagesList.getImagesList(),
                        id_user_upload_book_name.getText().toString(),
                        id_user_upload_book_author.getText().toString(),
                        Paper.book().read(Common.USER_FINAL_NUMBER).toString(),
                        user_selected_book_type);

                referenceUploadBook.child(bookId).setValue(post);
                referenceMyPost.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(bookId).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadActivity.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "You must need to select at least one image.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            if (intentGetInfo.getBooleanExtra("BookEnableId", false)) {
                //UpdateUploadedBook();
                UpdateSelectedUrl.add("file://"+photoFile.getAbsolutePath());
                UpdateImageAdapter.notifyDataSetChanged();
            } else {
                selectedUrls.add(photoFile.getAbsolutePath());
                UploadAdapter.notifyDataSetChanged();
                //Bundle bundle = data.getExtras();
                //Log.d(TAG, String.valueOf(Uri.parse(String.valueOf(bundle.get("data")))));
            }
        } else {
            Log.e(TAG, "Camera data is null");
        }
    }

    @Override
    public void onBackPressed() {
        if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else
        {
            if(UploadAdapter!=null || UpdateImageAdapter.getItemCount() > 0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit");
                builder.setMessage("Do you really wants to go back ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UploadActivity.super.onBackPressed();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
            else
            {
                super.onBackPressed();
            }
        }
    }
}


/*
        Log.d("UploadActivity ","Selected Directory" + selectedDirectory);
        final ArrayList<String> imageUrls = FileSearch.getFilePaths(selectedDirectory);
        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        final int imageWidth = gridWidth / NUM_GRID_COLUMN;
        gridView.setColumnWidth(imageWidth);
        //use the grid adapter to adapter the images to gridview
        final GridImageAdapter adapter = new GridImageAdapter(this, R.layout.gridlayout, "file://", imageUrls);
        gridView.setAdapter(adapter);
        try{
            if(imageUrls.size() > 0)
            {
                setImage(imageUrls.get(0), galleryImageView,"file://");
                mSelectedImage = imageUrls.get(0);
            }else
            {
                setImage(String.valueOf(R.mipmap.ic_launcher),galleryImageView,"file://");
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " +e.getMessage());
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imageUrls.get(position));
                setImage(imageUrls.get(position), galleryImageView, "file://");
                mSelectedImage = imageUrls.get(position);
                Log.d(TAG,"ArrayList of urls :"+ selectedUrls);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemLongClick: selected an image: " + imageUrls.get(position));
                selectedUrls.add(imageUrls.get(position));
                Toast.makeText(UploadActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
         */