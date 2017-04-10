package com.codepath.android.instudy.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import com.codepath.android.instudy.models.Message;
import com.codepath.android.instudy.models.Profile;
import com.codepath.android.instudy.models.StudentGroups;
import com.codepath.android.instudy.models.TeacherGroups;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.codepath.android.instudy.R.drawable.studyowl;


public class MyProfile extends Fragment implements View.OnClickListener {

    static final int MAX_TEACHERS_GROUPS_TO_SHOW = 2;
    static final int MAX_STUDENT_GROUPS_TO_SHOW = 2;
    EditText etUserName;
    EditText etTagline;
    EditText etLocation;
    EditText etShareNotes;
    Button btSnapPic;
    Button btSend;
    ListView lvStudentsGroup;
    ArrayList<StudentGroups> mStudentGroups;
    ListView lvTeacherGroup;
    ArrayList<TeacherGroups> mTeacherGroups;
    ListView lvChat;
    ArrayList<Message> mMessages;
    private Profile mProfile;
    public String photoFileName = "myProfilePic.jpg";
    public String profilePicFileName = "myProfilePic.png";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private Uri imageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_myprofile, container, false);
        lvChat = (ListView) v.findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        // Set profile values
        SetProfile(v);
        btSnapPic.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btSnapPic:
                takePhoto(v);
                break;
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


    public void takePhoto(View view) {
        if(!checkCameraHardware(getContext())) {
            Toast.makeText(getContext(), "No Camera on this device",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //String photoFileLocation = "android.resource://com.codepath.android.instudy/files/Pictures/InStudy";
        File photo = new File(Environment.getExternalStorageDirectory(),  photoFileName);
        //File photo = new File(DIR_profPic+File.separator+photoFileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
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
        }
    }

    public void SetProfile(View v) {
        // Create a profile
        mProfile = new Profile();
        // For testing force the values
        final String userId = ParseUser.getCurrentUser().getObjectId();
        if ((mProfile.getUserIdKey() != null) && (mProfile.getUserIdKey().equals(userId))) {
           Log.d("DEBUG", "Found this user: "+mProfile.getUserIdKey().toString()+
                   " and parse user are equal "+ userId);
        } else {
            if(userId == null) { Log.d("DEBUG","and parse uId is Null");}
            else {Log.d("DEBUG", "Parse uId: "+userId+" Found this user: id is "+mProfile.getUserIdKey());}
        }
        mProfile.setUSER_ID_KEY (userId);
        mProfile.setUserNameKey("John Student");
        mProfile.setUserTaglineKey("Seriously? study?");
        mProfile.setUserLocationKey("Galveston, Texas");
        mProfile.setUserNotesKey("I'm out for the evening");
        // Get this App users Id. here if null create a new user

        ParseQuery<Profile> mProfilequery = ParseQuery.getQuery(Profile.class);
        mProfilequery.getInBackground(userId, new GetCallback<Profile>() {
            @Override
            public void done(Profile object, ParseException e) {
                if( object != null) { // note test is for Parse exception not for object
                    String s = object.getUserNameKey();
                    mProfile.setUserNameKey(s);
                    s = object.getUserTaglineKey();
                    mProfile.setUserTaglineKey(s);
                    s = object.getUserLocationKey();
                    mProfile.setUserLocationKey(s);
                    s = object.getUserNotesKey();
                    mProfile.setUserNotesKey(s);
                    Log.d("DEBUG","Got Profile from parse ");
                } else {
                    // no access or something went wrong
                    Log.d("DEBUG","Empty Profile from parse ");
                }
            }
        });

        // Obtain values from Parse or Local dB
        File photo = new File(Environment.getExternalStorageDirectory(),  profilePicFileName);
        Uri uri;
        uri = Uri.fromFile(photo);
        if ( uri == null ) { // set default picture
            uri = Uri.parse("android.resource://com.codepath.android.instudy/drawable/theaderowl");
        }
        mProfile.setUserProfileImageKey(uri);
        // Find the resource id in the view
        etUserName = (EditText) v.findViewById(R.id.etUserName);
        etTagline = (EditText) v.findViewById(R.id.etTagLine);
        etLocation = (EditText) v.findViewById(R.id.etLocation);
        etShareNotes = (EditText) v.findViewById(R.id.etShareNotes);
        btSnapPic = (Button) v.findViewById(R.id.btSnapPic);
        btSend = (Button) v.findViewById(R.id.btSend);
        lvStudentsGroup = (ListView) v.findViewById(R.id.lvStudentGroup);
        lvTeacherGroup = (ListView) v.findViewById(R.id.lvTeacherGroup);
        final ImageView profileImg = (ImageView) v.findViewById(R.id.ivProfileImage);
        // Set the views
        etUserName.setText((CharSequence) mProfile.getUserNameKey());
        etTagline.setText((CharSequence) mProfile.getUserTaglineKey());
        etLocation.setText((CharSequence) mProfile.getUserLocationKey());
        etShareNotes.setText((CharSequence) mProfile.getUserNotesKey());
        Picasso.with(getContext())
                .load((mProfile.getUserProfileImageKey()))
                .fit()
                .centerInside()
                .placeholder(studyowl)
                .error(studyowl)
                .transform(new RoundedCornersTransformation(30, 30))
                .into(profileImg);
        // Before exiting, push profile back to Parse DB
        mProfile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(getContext(), "Stored my profile on Parse",Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG", "Stored my profile on Parse");
                } else {
                    Log.e("DEBUG", "Failed to save message", e);
                }
            }
        });
    }
}
