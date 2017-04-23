package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.MessageAdapter;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Message;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codepath.android.instudy.R.drawable.theaderowl;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

//TODO: implement differnet toolbar if group or single chat


    public String photoFileName = "instudy_attach.jpg";
    static final String TAG = ChatActivity.class.getSimpleName();
    Bitmap bmp;
    Uri BmpFileName;
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    public final String APP_TAG = "InStudy";
    //File
    private File filePathImageCamera;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private LinearLayoutManager linearLayoutManager;
    String chatId = "";
    String userId = "";
    Message message;
    Chat chat;
    EditText etMessage;
    ImageView btnSend, btnGallery, btnPhoto, btnLocation;
    RecyclerView lvChat;
    ArrayList<Message> mMessages;

    MessageAdapter adapter;

    boolean mFirstLoad;
    Toolbar toolbar;
    LinearLayout layoutToolbar;


    // Create a handler which can run code periodically
    static final int POLL_INTERVAL = 1000; // milliseconds
    Handler myHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        layoutToolbar = (LinearLayout)toolbar.findViewById(R.id.toolbar_item_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupMessagePosting();
        //check if this belongs to existing chatid
        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("chatid")) {
            chatId = parentIntent.getStringExtra("chatid");
            populateData(chatId);
        } else {

            userId = parentIntent.getStringExtra("userid");
        }

        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    private void populateData(String chatId) {

        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        // Specify the object id
        query.getInBackground(chatId, new GetCallback<Chat>() {
            public void done(final Chat chat, final ParseException e) {
                ArrayList<String> chattrs = chat.getRecipients();
                if (e == null) {
                    if (chat.getChatName() != null) {
                        //toolbar.setTitle(chat.getChatName());
                        toolbar.setTitle("");
                        final int NumChatters = chat.getRecipients().size();
                        if(NumChatters < 2) {
                            Toast.makeText(getBaseContext(), "No chatters in this group", Toast.LENGTH_SHORT).show();
                            return;
                        } // temp check for num of chatters less than 2
                        String otherChatterId = chattrs.get(0);
                        final String finalOtherChatterId = otherChatterId;
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                        query.whereEqualTo("objectId",otherChatterId);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    final ImageView image1 = (ImageView) toolbar.findViewById(R.id.ivChatter1);
                                    try {
                                        String profImg = objects.get(0).getString("ProfileImage");
                                        Log.d("DEBUG"," Found profile image for userId0: "+ finalOtherChatterId);
                                        Glide.with(getBaseContext())
                                                .load(profImg).asBitmap().override(50, 50).centerCrop().into(new BitmapImageViewTarget(image1) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                image1.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                                    } catch (Exception e0) {
                                        Log.d("DEBUG"," Could not find profile image for userId0: "+ finalOtherChatterId);
                                        Glide.with(getBaseContext())
                                                .load(theaderowl).asBitmap().override(50, 50).centerCrop().into(new BitmapImageViewTarget(image1) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                image1.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                                    }
                                } else {
                                    // error
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("DEBUG"," Got parse error for this user");
                                }
                            }
                        });
                        otherChatterId = chattrs.get(1);
                        query = ParseQuery.getQuery("_User");
                        final String finalOtherChatterId1 = otherChatterId;
                        query.whereEqualTo("objectId",otherChatterId);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    final ImageView image2 = (ImageView) toolbar.findViewById(R.id.ivChatter2);
                                    try {
                                        String profImg = objects.get(1).getString("ProfileImage");
                                        Log.d("DEBUG"," Found profile image for userId1: "+ finalOtherChatterId1);
                                        Glide.with(getBaseContext())
                                                .load(profImg).asBitmap().override(50, 50).centerCrop().into(new BitmapImageViewTarget(image2) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                image2.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                                    } catch (Exception e1) {
                                        Log.d("DEBUG"," Could not find profile image for userId1: "+ finalOtherChatterId1);
                                        Glide.with(getBaseContext())
                                            .load(theaderowl).asBitmap().override(50,50).centerCrop().into(new BitmapImageViewTarget(image2) {
                                                @Override
                                                protected void setResource(Bitmap resource) {
                                                    RoundedBitmapDrawable circularBitmapDrawable =
                                                            RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                                                    circularBitmapDrawable.setCircular(true);
                                                    image2.setImageDrawable(circularBitmapDrawable);
                                                }
                                            });
                                    }
                                } else {
                                    // error
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("DEBUG"," Got parse error for this user");
                                }
                            }
                        });
                        otherChatterId = chattrs.get(2);
                        query = ParseQuery.getQuery("_User");
                        final String finalOtherChatterId2 = otherChatterId;
                        query.whereEqualTo("objectId",otherChatterId);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    final ImageView image3 = (ImageView) toolbar.findViewById(R.id.ivChatter3);
                                    try {
                                        String profImg = objects.get(2).getString("ProfileImage");
                                        Log.d("DEBUG"," Found profile image for userId2: "+ finalOtherChatterId2);
                                        Glide.with(getBaseContext())
                                                .load(profImg).asBitmap().override(50,50).centerCrop().into(new BitmapImageViewTarget(image3) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                image3.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                                    } catch (Exception e2) {
                                        Log.d("DEBUG"," Could not find profile image for userId2: "+ finalOtherChatterId2);
                                        Glide.with(getBaseContext())
                                                .load(theaderowl).asBitmap().override(50,50).centerCrop().into(new BitmapImageViewTarget(image3) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                image3.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                                    }
                                } else {
                                    // error
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("DEBUG"," Got parse error for this user");
                                }
                            }
                        });
                        String dNumGroupCh = " + "+String.valueOf(NumChatters-3)+ " Other/s";
                        TextView etFullName = (TextView) toolbar.findViewById(R.id.etFullName);
                        etFullName.setText(dNumGroupCh);
                        etFullName.setTextSize(18);
                        TextView etOnline = (TextView) toolbar.findViewById(R.id.etOnLine);
                        etOnline.setText("");
                    } else if (chat.getRecipients().size() == 2) {
                        // get 2nd user and get their full name to display on toolbar
                        String otherChatterId = chattrs.get(0);
                        final ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                        query.whereEqualTo("objectId",otherChatterId);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    toolbar.setTitle("");
                                    final ImageView image1 = (ImageView) toolbar.findViewById(R.id.ivChatter1);
                                    String profImg = objects.get(0).getString("ProfileImage");;
                                    Glide.with(getBaseContext())
                                            .load(profImg).asBitmap().override(50,50).centerCrop().into(new BitmapImageViewTarget(image1) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            image1.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                    TextView etFullName = (TextView) toolbar.findViewById(R.id.etFullName);
                                    // need the otherChattername as a separate variable to avoid null exception
                                    // as otherChatterName is initialized to no-one
                                    String otherChatterName = "No-One";
                                    otherChatterName = objects.get(0).getString("FullName");
                                    etFullName.setText(otherChatterName);
                                    TextView etOnline = (TextView) toolbar.findViewById(R.id.etOnLine);
                                    //long userLast = objects.get(0).getInt("updatedAt");
//                                    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'LLL'Z'");
//                                    Date currentDate=new Date();
//                                    String parseTime = objects.get(0).getString("_updated_at");
//                                    Date parseDate = null;
//                                    try {
//                                        parseDate = formatter.parse(parseTime);
//                                    } catch(Exception et) {
//                                        Toast.makeText(getBaseContext(), et.getMessage(), Toast.LENGTH_SHORT).show();
//                                        Log.d("DEBUG"," Ex "+et.getMessage());
//                                    }
//                                    long difference = currentDate.getTime() - parseDate.getTime();
//                                    Log.d("DEBUG"," difference is "+difference);
                                    // Use the difference in time to set on or off line
                                    long difference = 0;
                                    if (( difference > 0) && (difference < 1)) {
                                        etOnline.setText("Online");
                                    } else {
                                        etOnline.setText("OffLine");
                                    }

                                } else {
                                    // error
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("DEBUG"," Got parse error for this user");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etText);
        btnSend = (ImageView) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnPhoto = (ImageView) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(this);
        btnGallery = (ImageView) findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);
        //btnLocation= (ImageView) findViewById(R.id.btnLocation);
        //btnLocation.setOnClickListener(this);
        lvChat = (RecyclerView) findViewById(R.id.lvMessages);

        mMessages = new ArrayList<>();
        //lvChat.setTranscriptMode(1); // scroll to the bottom when a new data shows up

        //mAdapter = new ChatMessageAdapter(ChatActivity.this, ParseUser.getCurrentUser().getObjectId(), mMessages);
        adapter = new MessageAdapter(ChatActivity.this, mMessages);
        lvChat.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        lvChat.setLayoutManager(linearLayoutManager);
        // When send button is clicked, create message object on Parse


    }

    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        //query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.whereEqualTo(Message.CHAT_KEY, this.chatId);
        // get the latest 500 messages, order will show up newest to oldest of this group
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    lvChat.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                   /* if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }*/
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                sendMessage();
                break;
            case R.id.btnPhoto:
                verifyStoragePermissions();
                break;
            case R.id.btnGallery:
                photoGalleryIntent();
                break;
          /*  case R.id.btnLocation:
                //locationPlacesIntent();
                break;*/
        }
    }

    private void sendMessage() {

        String data = etMessage.getText().toString();
        message = new Message();
        message.setBody(data);
        message.setUserId(ParseUser.getCurrentUser().getObjectId());

        if (TextUtils.isEmpty(chatId)) {
            chat = new Chat();
            ArrayList<String> recipients = new ArrayList<String>();
            recipients.add(ParseUser.getCurrentUser().getObjectId());
            recipients.add(userId);
            chat.setRecipients(recipients);
            chat.setLastDate(new Date());

            chat.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        message.setChatId(chat.getObjectId());
                        chatId = chat.getObjectId();
                        message.saveInBackground();
                    }
                }
            });
        } else {
            message.setChatId(chatId);
            message.saveInBackground();
            ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
            // Specify the object id
            query.getInBackground(chatId, new GetCallback<Chat>() {
                public void done(Chat chat, ParseException e) {
                    if (e == null) {
                        chat.setLastDate(new Date());
                        ArrayList<String> recipients = chat.getRecipients();
                        if (!recipients.contains(ParseUser.getCurrentUser().getObjectId())) {
                            recipients.add(ParseUser.getCurrentUser().getObjectId());
                            chat.setRecipients(recipients);
                        }
                        chat.saveInBackground();
                    }
                }
            });
            message.setUserId(ParseUser.getCurrentUser().getObjectId());
        }
        etMessage.setText(null);

    }

    private void sendFileMessage(Bitmap bmp) throws ParseException {

        String data = etMessage.getText().toString();
        message = new Message();

        message.setBody(data);
        message.setUserId(ParseUser.getCurrentUser().getObjectId());
        if (bmp == null) {
            Toast.makeText(ChatActivity.this,"Problem with image",Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 0, stream);
        final ParseFile pFile = new ParseFile("image.jpg", stream.toByteArray());
        pFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    message.put("fileName", pFile);
                    if (TextUtils.isEmpty(chatId)) {
                        chat = new Chat();
                        ArrayList<String> recipients = new ArrayList<String>();
                        recipients.add(ParseUser.getCurrentUser().getObjectId());
                        recipients.add(userId);
                        chat.setRecipients(recipients);
                        chat.setLastDate(new Date());
                        chat.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    message.setChatId(chat.getObjectId());
                                    chatId = chat.getObjectId();
                                    message.saveInBackground();
                                }
                            }
                        });
                    } else {
                        message.setChatId(chatId);
                        message.saveInBackground();
                        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
                        // Specify the object id
                        query.getInBackground(chatId, new GetCallback<Chat>() {
                            public void done(Chat chat, ParseException e) {
                                if (e == null) {
                                    chat.setLastDate(new Date());
                                    ArrayList<String> recipients = chat.getRecipients();
                                    if (!recipients.contains(ParseUser.getCurrentUser().getObjectId())) {
                                        recipients.add(ParseUser.getCurrentUser().getObjectId());
                                        chat.setRecipients(recipients);
                                    }
                                    chat.saveInBackground();
                                }
                            }
                        });
                        message.setUserId(ParseUser.getCurrentUser().getObjectId());
                    }
                }
            }
        });
       // addMessage(message);
        etMessage.setText(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    sendFileMessage(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == IMAGE_CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {

                    Uri photoUri = getPhotoFileUri(photoFileName);
                    if(photoUri==null) return;
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    sendFileMessage(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Place place = PlacePicker.getPlace(this, data);
               /* if (place!=null){
                    LatLng latLng = place.getLatLng();
                    MapModel mapModel = new MapModel(latLng.latitude+"",latLng.longitude+"");
                    ChatModel chatModel = new ChatModel(userModel, Calendar.getInstance().getTime().getTime()+"",mapModel);
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(chatModel);
                }else{
                    //PLACE IS NULL
                }*/
            }
        }

    }

    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(ChatActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    ChatActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            // we already have permission, lets go ahead and call camera intent
            photoCameraIntent();
        }
    }

    private void photoCameraIntent() {
        Uri photoUri = getPhotoFileUri(photoFileName);
       if(photoUri!=null){
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);;
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);;
            startActivityForResult(i, IMAGE_CAMERA_REQUEST);
        }
    }

    private Uri getPhotoFileUri(String fileName){
      /*  String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            File file = Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            return FileProvider.getUriForFile(MyActivity.this, "com.codepath.fileprovider", file);
        }*/
        Uri photoUri= null;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            File photoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
            try {
                if (photoFile.exists() == false) {
                    photoFile.getParentFile().mkdirs();
                    photoFile.createNewFile();
                }
            } catch (IOException e) {
                Log.e("DocumentActivity", "Could not create file.", e);
            }

            photoUri = Uri.fromFile(photoFile);
        }
        return  photoUri;
    }

    private void photoGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Get Photo From"), IMAGE_GALLERY_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    photoCameraIntent();
                }
                break;
        }
    }


    public void addMessage(Message msg){
        mMessages.add(0,msg);
        adapter.notifyDataSetChanged();
    }


     /*private void locationPlacesIntent(){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }*/


}
