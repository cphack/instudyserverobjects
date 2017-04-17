package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.ChatMessageAdapter;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Message;
import com.parse.GetCallback;
import com.parse.ParseUser;


import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    static final String TAG = ChatActivity.class.getSimpleName();
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    String chatId = "";
    String userId = "";
    Message message;
    Chat chat;
    EditText etMessage;
    Button btSend;
    ListView lvChat;
    ArrayList<Message> mMessages;
    ChatMessageAdapter mAdapter;
    //track first load
    boolean mFirstLoad;
    Toolbar toolbar;


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check if this belongs to existing chatid
        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra("chatid")) {
            chatId = parentIntent.getStringExtra("chatid");
            populateData(chatId);
        } else {

            userId = parentIntent.getStringExtra("userid");
        }
        setupMessagePosting();
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }


    private void populateData(String chatId){
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        // Specify the object id
        query.getInBackground(chatId, new GetCallback<Chat>() {
            public void done(Chat chat, ParseException e) {
                if (e == null) {
                   if(chat.getChatName()!=null){
                       toolbar.setTitle(chat.getChatName());
                   }else if(chat.getRecipients().size()==2){
                       //TODO get 2nd user and get his fullname to display on toolbar
                   }
                }
            }
        });
    }

    void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etText);
        btSend = (Button) findViewById(R.id.btnSend);
        lvChat = (ListView) findViewById(R.id.lvMessages);

        mMessages = new ArrayList<>();
        lvChat.setTranscriptMode(1); // scroll to the bottom when a new data shows up
        mFirstLoad = true;
        mAdapter = new ChatMessageAdapter(ChatActivity.this, ParseUser.getCurrentUser().getObjectId(), mMessages);
        lvChat.setAdapter(mAdapter);
        // When send button is clicked, create message object on Parse


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                chatId=chat.getObjectId();
                                message.saveInBackground();
                            }
                        }
                    });
                } else {
                    saveMessageWithChat(message);
                }
                etMessage.setText(null);
            }
        });
    }

    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.whereEqualTo(Message.CHAT_KEY,this.chatId);
        // get the latest 500 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    void saveMessageWithChat(Message message) {
        message.setChatId(chatId);
        message.saveInBackground();
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        // Specify the object id
        query.getInBackground(chatId, new GetCallback<Chat>() {
            public void done(Chat chat, ParseException e) {
                if (e == null) {
                    chat.setLastDate(new Date());
                    ArrayList<String> recipients = chat.getRecipients();
                    if(!recipients.contains(ParseUser.getCurrentUser().getObjectId())){
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
