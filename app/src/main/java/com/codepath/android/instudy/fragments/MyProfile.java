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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Message;
import com.parse.GetDataCallback;
import com.parse.Parse;
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
import static com.codepath.android.instudy.R.id.ivUser;
import static com.codepath.android.instudy.R.id.myProfile;

@RuntimePermissions
public class MyProfile extends Fragment implements View.OnClickListener,
        EditStatusFragment.EditNameDialogListener,AddEventFragment.AddEventDialogListener {

    TextView etUserName;
    TextView tvStatusLine;
    Button btSave;
    ImageView ivGallery;
    ImageView ivCamera;
    LinearLayout llStatusLine;
    RelativeLayout rlAddNewEvent;

    ArrayList<Message> mMessages;
    private ParseUser me;
    ImageView profileImg;
    public String myParseGetPic = "myParseGetPic";
    public String parseProfileImageName = "myParseImagePic";
    public String myGalPic = "myGalPic";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final int SELECT_PHOTO = 100;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_myprofile, container, false);
        etUserName = (TextView) v.findViewById(R.id.tvFullName);
        tvStatusLine = (TextView) v.findViewById(R.id.tvStatusLine);
        ivGallery=(ImageView)v.findViewById(R.id.ivGallery);
        ivCamera=(ImageView)v.findViewById(R.id.ivCamera);
        btSave = (Button) v.findViewById(R.id.btSave);
        llStatusLine=(LinearLayout) v.findViewById(R.id.llStatusLine);
        profileImg = (ImageView) v.findViewById(R.id.ivProfileImage);
        rlAddNewEvent = (RelativeLayout)v.findViewById(R.id.rlAddNewEvent);
        me =  ParseUser.getCurrentUser();
        ivCamera.setOnClickListener(this);

        ivGallery.setOnClickListener(this);
        rlAddNewEvent.setOnClickListener(this);
        llStatusLine.setOnClickListener(this);
        // Set profile values
        SetProfile(v);
        //fill timeline

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        insertNestedFragment();
    }

    // Embeds the child fragment dynamically
    private void insertNestedFragment() {
        TimelineFragment childFragment = new TimelineFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.timeline_container, childFragment,"timelinefrag").commit();
        childFragment.populateTimeline(ParseUser.getCurrentUser().getObjectId());
    }





    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ivCamera:
                takePhoto();
                break;
            case R.id.ivGallery:
                galleryPicker(v);
                break;
            case R.id.btSave:
                UserProfileUpdate();
                getActivity().finish();
                break;
            case R.id.llStatusLine:
                showEditDialog();
                break;
            case R.id.rlAddNewEvent:
                showAddNewEventDialog();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ImageView imageView = (ImageView) getActivity().findViewById(R.id.ivProfileImage);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    String pPName;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        // Scale to show on local load
                        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imageView.setImageBitmap(bitmapScaled);

                        // Configure byte output stream
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        // Rescale larger to store on device
                        bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                        // Compress the image further
                        bitmapScaled.compress(Bitmap.CompressFormat.PNG, 40, bytes);

                        // Create a new file for the resized bitmap
                        pPName = String.valueOf(selectedImage).replace("jpg","png");
                        //Log.d("DEBUG","Photo cpature saved file "+pPName);
                        Uri resizedUri = Uri.parse(pPName);
                        File resizedFile = new File(resizedUri.getPath());
                        resizedFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(resizedFile);
                        // Write the bytes of the bitmap to file
                        fos.write(bytes.toByteArray());
                        fos.close();
                        fos.flush();
                        Glide.with(getContext()).load(resizedUri).asBitmap().centerCrop().into(new BitmapImageViewTarget(profileImg) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                profileImg.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        byte[] image = bytes.toByteArray();
                        ParseFile pfile = new ParseFile("myprofilepic.png", image);
                        pfile.saveInBackground();
                        me.put("ImageName", "myprofilepic.png");
                        me.put("ImageFile", pfile);
                        me.saveInBackground();
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
                    Bitmap bitmap;
                    String pPName;
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmap = BitmapFactory.decodeFile(picturePath);
                    profileImg.setImageBitmap(bitmap);
                    Bitmap bitmapScaled;
                    bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmapScaled.compress(Bitmap.CompressFormat.PNG, 40, bytes);
                    String fP4BMap = String.format("file://%s/%s.png", getExternalStorageDirectory(), myGalPic);
                    //Log.d("DEBUG","Photo from gallery saved in file "+fP4BMap);
                    File galFile = new File(getExternalStorageDirectory(), myGalPic+".png");
                    Uri galUri = Uri.parse(fP4BMap);
                    byte[] selpic = bytes.toByteArray();
                    try {
                        //galFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(galFile);
                        // Write the bytes of the bitmap to file
                        fos.write(selpic);
                        fos.close();
                        fos.flush();
                        Picasso.with(getContext())
                                .load(galUri)
                                .fit()
                                .centerInside()
                                .placeholder(studyowl)
                                .error(theaderowl)
                                .transform(new RoundedCornersTransformation(30, 30))
                                .into(profileImg);
                        ParseFile pfile = new ParseFile("myprofilepic.png", selpic);
                        pfile.saveInBackground();
                        me.put("ImageName", "myprofilepic.png");
                        me.put("ImageFile", pfile);
                        me.saveInBackground();
                    } catch (IOException e) {
                        Log.d("DEBUG","Caught IO exception");
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    public void SetProfile(View v) {
        String profileImageUrl;

        if(me.getParseFile("ImageFile")!=null){
            ParseFile imageFile = me.getParseFile("ImageFile");
                //profileImg
            profileImageUrl = imageFile.getUrl();
        }else
        {
            profileImageUrl = me.getString("ProfileImage");
        }

        Glide.with(this).load(profileImageUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(profileImg) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                profileImg.setImageDrawable(circularBitmapDrawable);
            }
        });

        String value =  me.getString("FullName");
        if (value != null ) { etUserName.setText(value); }

        value =  me.getString("statusLine");
        if (value != null ) {tvStatusLine.setText(value);}



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
        //Log.d("DEBUG", " entering take photo ");
        boolean this_is_android_v7 = true;
        if (!checkCameraHardware(getContext())) {
            Toast.makeText(getContext(), "No Camera on this device", Toast.LENGTH_LONG).show();
            Log.d("DEBUG", "No Camera on this device");
            return;
        }
        //Log.d("DEBUG", " Past camera hardware check ");
        //String photoFileLocation = "android.resource://com.codepath.android.instudy/files/Pictures/InStudy";
        //Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", createImageFile());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (this_is_android_v7) {
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                File path = new File(Environment.getExternalStorageDirectory(),"");
                //Log.d("DEBUG"," photo path is "+path.toString());
                File photo = new File( path ,parseProfileImageName+".png"); //+System.currentTimeMillis()
                try {
                    photo.createNewFile();
                    //Log.d("DEBUG","Attempted to create photo file");
                } catch (IOException e) {
                    Log.d("DEBUG","Failed to create photo file");
                    e.printStackTrace();
                }
                //Log.d("DEBUG"," photo file is "+photo.toString());
                //imageUri = FileProvider.getUriForFile(this.getContext(), this.getContext().getPackageName(), photo);
                imageUri = Uri.fromFile(photo);
                //Log.d("DEBUG","imageUri is "+imageUri.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            } else {
                Log.d("DEBUG","Resolve Activity returned null");
            }
        } else {
            File photo = new File(getExternalStorageDirectory(), parseProfileImageName+".png");
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
        String value =  me.getString("FullName");
        if (value != null ) { etUserName.setText(value); }

        value = (String) me.getString("statusLine");
        if (value != null ) {tvStatusLine.setText(value);}


        ParseFile phFile = (ParseFile) me.get("ImageFile");
        if(phFile != null ) {
            phFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        // Photo picture avail
                        File photo = new File(getExternalStorageDirectory(), myParseGetPic + ".png");
                        FileOutputStream ph = null;
                        try {
                            ph = new FileOutputStream(photo);
                            ph.write(data);
                            ph.close();
                            ph.flush();
                        } catch (IOException e1) {
                            Log.d("DEBUG", "Failed to get ph stream photo file");
                            e1.printStackTrace();
                        }
                    } else {
                        Log.d("DEBUG", "Failed to get photo file");
                    }
                    File parsephoto = new File(getExternalStorageDirectory(), myParseGetPic + ".png");
                    Uri uri;
                    uri = Uri.fromFile(parsephoto); // photo
                    if (uri == null) { // set default picture
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
        } else {
            Toast.makeText(getContext(),"No Profile Picture stored ", Toast.LENGTH_SHORT).show();
            //Log.d("DEBUG","Getting dummy file");
            // Make dummy profile picture and store in on disk
            File photo = new File(getExternalStorageDirectory(), myParseGetPic + ".png");
            File dummyProfile = new File("android.resource://com.codepath.android.instudy/drawable/theaderowl.png");
            byte[] dummydata = dummyProfile.toString().getBytes();
            FileOutputStream ph = null;
            try {
                ph = new FileOutputStream(photo);
                ph.write(dummydata);
                ph.close();
                ph.flush();
            } catch (IOException e1) {
                Log.d("DEBUG", "Failed to store dummy profile pics photo file");
                e1.printStackTrace();
            }
        }
    }

    public void UserProfileUpdate() {
       /* TagLine = etTagline.getText().toString();
        Location = etLocation.getText().toString();
        ShareNotes = etShareNotes.getText().toString();
        // Before exiting, push profile back to Parse DB
        if (TagLine == null) {
            TagLine = "None";
        }
        if (Location == null) {
            Location = "None";
        }
        if (ShareNotes == null) {
            ShareNotes = "None";
        }
        me.put("TagLine", TagLine);
        me.put("Location", Location);
        me.put("ShareNotes", ShareNotes);
        me.put("Profile", "Updated"); // set flag so next time we do not save unless specifically save is clicked
        me.saveInBackground();*/
        File photo = new File(getExternalStorageDirectory(), myParseGetPic + ".png");
        if(photo.exists()) {
            boolean delFile = photo.delete();
        }
        photo = new File(getExternalStorageDirectory(), parseProfileImageName + ".png");
        if(photo.exists()) {
            boolean delFile = photo.delete();
        }
        photo = new File(getExternalStorageDirectory(), myGalPic + ".png");
        if(photo.exists()) {
            boolean delFile = photo.delete();
        }
    }

    public void galleryPicker(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        EditStatusFragment editNameDialogFragment = EditStatusFragment.newInstance();
        editNameDialogFragment.setTargetFragment(MyProfile.this, 300);
        editNameDialogFragment.show(fm, "fragment_edit_status");
    }


    private void showAddNewEventDialog() {
        FragmentManager fm = getFragmentManager();
        AddEventFragment frg = AddEventFragment.newInstance();
        frg.setTargetFragment(MyProfile.this, 300);
        frg.show(fm, "fragment_add_new_event");
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        tvStatusLine.setText(inputText);
    }

    @Override
    public void onCloseDialog() {
        /*TimelineFragment childFragment = new TimelineFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.timeline_container, childFragment).commit();
        childFragment.populateTimeline(ParseUser.getCurrentUser().getObjectId());
*/
        TimelineFragment f= (TimelineFragment)
                getChildFragmentManager().findFragmentByTag("timelinefrag");
        if(f!=null){
            f.populateTimeline(ParseUser.getCurrentUser().getObjectId());
        }
    }
}
