package com.codepath.android.instudy.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.MainActivity;
import com.codepath.android.instudy.models.Message;
import com.codepath.android.instudy.models.StudentGroups;
import com.codepath.android.instudy.models.TeacherGroups;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStorageDirectory;
import static com.codepath.android.instudy.R.drawable.studyowl;
import static com.codepath.android.instudy.R.drawable.theaderowl;

@RuntimePermissions
public class MyProfile extends Fragment implements View.OnClickListener {

    static final int MAX_TEACHERS_GROUPS_TO_SHOW = 2;
    static final int MAX_STUDENT_GROUPS_TO_SHOW = 2;
    EditText etUserName;
    EditText etTagline;
    EditText etLocation;
    EditText etShareNotes;
    Button btSnapPic;
    Button btSave;
    Button btGalPic;
    public String TagLine = "Tag Line";
    public String Location = "New York City";
    public String ShareNotes = " I may not be able to attend";

    ListView lvStudentsGroup;
    ArrayList<StudentGroups> mStudentGroups;
    ListView lvTeacherGroup;
    ArrayList<TeacherGroups> mTeacherGroups;
    ListView lvChat;
    ArrayList<Message> mMessages;
    private ParseUser me;
    ImageView profileImg;
    public String photoFileName = "myProfilePic";
    public String parseProfileImageName = "myParseImagePic";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final int SELECT_PHOTO = 100;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_myprofile, container, false);
        lvChat = (ListView) v.findViewById(R.id.lvChat);
        etUserName = (EditText) v.findViewById(R.id.etUserName);
        etTagline = (EditText) v.findViewById(R.id.etTagLine);
        etLocation = (EditText) v.findViewById(R.id.etLocation);
        etShareNotes = (EditText) v.findViewById(R.id.etShareNotes);
        btSnapPic = (Button) v.findViewById(R.id.btSnapPic);
        btGalPic = (Button) v.findViewById(R.id.btGalPic);
        btSave = (Button) v.findViewById(R.id.btSave);
        lvStudentsGroup = (ListView) v.findViewById(R.id.lvStudentGroup);
        lvTeacherGroup = (ListView) v.findViewById(R.id.lvTeacherGroup);
        profileImg = (ImageView) v.findViewById(R.id.ivProfileImage);
        me =  ParseUser.getCurrentUser();
        mMessages = new ArrayList<Message>();
        btSnapPic.setOnClickListener(this);
        btSave.setOnClickListener(this);
        btGalPic.setOnClickListener(this);
        // Set profile values
        SetProfile(v);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btSnapPic:
                takePhoto();
                break;
            case R.id.btGalPic:
                galleryPicker(v);
                break;
            case R.id.btSave:
                UserProfileUpdate();
                DoneSaveBackToActivity();
                break;
        }
    }
    // All data items saved on line in Parse
    // No need to pass any stuff back to calling activity
    // So get the main activity as an intent and return to it.
    public void DoneSaveBackToActivity() {
        Intent i = null;
        i = new Intent(getActivity(), MainActivity.class);
        if(i!=null){
            getActivity().finish();
            startActivity(i);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ImageView imageView = (ImageView) getActivity().findViewById(R.id.ivProfileImage);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        // Scale to show on local load
                        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imageView.setImageBitmap(bitmapScaled);
                        //Toast.makeText(getContext(), selectedImage.toString(),Toast.LENGTH_LONG).show();
                        // Configure byte output stream
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        // Rescale larger to store on device
                        bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                        // Compress the image further
                        bitmapScaled.compress(Bitmap.CompressFormat.PNG, 40, bytes);
                        // Create a new file for the resized bitmap
                        String pPName = String.valueOf(selectedImage).replace("jpg","png");
                        Uri resizedUri = Uri.parse(pPName);
                        File resizedFile = new File(resizedUri.getPath());
                        resizedFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(resizedFile);
                        // Write the bytes of the bitmap to file
                        fos.write(bytes.toByteArray());
                        fos.close();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
                break;
            //from the gallery
            case SELECT_PHOTO:
                if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && null!= data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    profileImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;

        }
    }

    public void SetProfile(View v) {
        final String myName = me.getString("FullName");
        etUserName.setText(myName); // is known at user login creation
        Log.d("DEBUG", "Found this user: "+myName);
        String myProfile = me.getString("Profile");
        if(myProfile != "None") {
            getUserProfileFromParseAndUpdate();
        }
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void takePhoto() {
        Log.d("DEBUG", " entering take photo ");
        boolean this_is_android_v7 = false;
        if (!checkCameraHardware(getContext())) {
            Toast.makeText(getContext(), "No Camera on this device", Toast.LENGTH_LONG).show();
            Log.d("DEBUG", "No Camera on this device");
            return;
        }
        Log.d("DEBUG", " Past camera hardware check ");
        //String photoFileLocation = "android.resource://com.codepath.android.instudy/files/Pictures/InStudy";
        //Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", createImageFile());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (this_is_android_v7) {
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                File path = new File(Environment.getExternalStorageDirectory(),"../../../SDCARD/DCIM");
                Log.d("DEBUG"," photo path is "+path.toString());
                File photo = new File( path ,photoFileName+".png"); //+System.currentTimeMillis()
                try {
                    photo.createNewFile();
                    Log.d("DEBUG","Attempted to create photo file");
                } catch (IOException e) {
                    Log.d("DEBUG","Failed to create photo file");
                    e.printStackTrace();
                }
                Log.d("DEBUG"," photo file is "+photo.toString());
                imageUri = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photo);
                //imageUri = Uri.fromFile(photo);
                Log.d("DEBUG","imageUri is "+imageUri.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            } else {
                Log.d("DEBUG","Resolve Activity returned null");
            }
        } else {
            File photo = new File(getExternalStorageDirectory(), photoFileName+".png");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            imageUri = Uri.fromFile(photo);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    private void getUserProfileFromParseAndUpdate() {
        String value = (String) me.getString("FullName");
        if (value != null ) { etUserName.setText(value); } else {etUserName.setText("None");}
        value = (String) me.getString("TagLine");
        if (value != null ) { etTagline.setText(value);} else {etTagline.setText("None");}
        value = (String) me.getString("Location");
        if (value != null ) {etLocation.setText(value);} else {etLocation.setText("None");}
        value = (String) me.getString("ShareNotes");
        if (value != null ) {etShareNotes.setText(value);} else {etShareNotes.setText("None");}
        ParseFile phFile = (ParseFile) me.get("ImageFile");
        phFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if( e == null ) {
                    // Photo picture avail
                    File photo = new File(getExternalStorageDirectory(), parseProfileImageName+".png");
                    FileOutputStream ph = null;
                    try {
                        ph = new FileOutputStream(photo);
                        ph.write(data);
                        ph.close();
                    } catch (IOException e1) {
                        Log.d("DEBUG","Failed to get ph stream photo file");
                        e1.printStackTrace();
                    }
                } else {
                    Log.d("DEBUG","Failed to get photo file");
                }
                File parsephoto = new File(getExternalStorageDirectory(),  parseProfileImageName+".png");
                Uri uri;
                uri = Uri.fromFile(parsephoto); // photo
                if ( uri == null ) { // set default picture
                    uri = Uri.parse("android.resource://com.codepath.android.instudy/drawable/theaderowl");
                }
                Picasso.with(getContext())
                        .load(uri)
                        .fit()
                        .centerInside()
                        .placeholder(studyowl)
                        .error(theaderowl)
                        .transform(new RoundedCornersTransformation(30, 30))
                        .into(profileImg);
            }
        });

    }

    public void UserProfileUpdate() {
        TagLine = etTagline.getText().toString();
        Location = etLocation.getText().toString();
        ShareNotes = etShareNotes.getText().toString();
         // Before exiting, push profile back to Parse DB
        if(TagLine == null) { TagLine = "None";}
        if(Location == null) { Location = "None";}
        if(ShareNotes == null) { ShareNotes = "None";}
        me.put("TagLine",TagLine);
        me.put("Location",Location);
        me.put("ShareNotes",ShareNotes);
        me.put("Profile","Updated"); // set flag so next time we do not save unless specifically save is clicked
        File photo = new File(getExternalStorageDirectory(),  photoFileName+".png");
        if ( !photo.exists() ) { // if no photo exists then set default picture
            photo = new File("android.resource://com.codepath.android.instudy/drawable/theaderowl");
        }
        String fPath4BMap = String.format("%s/%s.png", getExternalStorageDirectory(), photoFileName);
        Bitmap bitmap = BitmapFactory.decodeFile(fPath4BMap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] image = stream.toByteArray();
        Uri cUri = Uri.fromFile(photo);
        Picasso.with(getContext())
                .load(cUri)
                .fit()
                .centerInside()
                .placeholder(studyowl)
                .error(theaderowl)
                .transform(new RoundedCornersTransformation(30, 30))
                .into(profileImg);
        ParseFile pfile  = new ParseFile("myprofilepic.png",image);
        pfile.saveInBackground();
        me.put("ImageName","myprofilepic.png");
        me.put("ImageFile",pfile);
        me.saveInBackground();

    }

    public void galleryPicker(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
}
