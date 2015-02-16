package Otros;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import natour.issam.proyecto.es.proyecto_qiz.R;


public class ChatAplication extends ActionBarActivity {
    public static final String USER_ID_KEY = "userId";

    private EditText etMessage;
    private Button btSend;
    String sUserId;
    private ListView lvChat;
    private ArrayList<Messages> mMessages;
    private ChatListAdapter mAdapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_aplication);

        ParseObject.registerSubclass(Messages.class);
        // Existing initialization happens after all classes are registered
        Parse.initialize(this,"LPLJJbBhObox41EBfcjYi84il4ywv3DiXCQY0wiK","jwfLQI2wuqeYoH4ZCAEYXBmGsltciSHTogQ7KnrI");
        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser();
        } else {
            login();
        }
        // Run the runnable object defined every 100ms
        handler.postDelayed(runnable, 5000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 5000);
        }
    };

    private void refreshMessages() {
        receiveMessage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_aplication, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    private void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d("TAG", "Anonymous login failed.");
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        setupMessagePosting();
    }

    // Setup button event handler which posts the entered message to Parse

    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<Messages>();
        mAdapter = new ChatListAdapter(ChatAplication.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                // Use Message model to create new messages now
                Messages message = new Messages();
                message.setUserId(sUserId);
                message.setBody(body);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        receiveMessage();
                    }
                });
                etMessage.setText("");
            }
        });
    }

    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<Messages> query = ParseQuery.getQuery(Messages.class);
        query.setLimit(50);
        query.orderByAscending("createdAt");
        // Execute query for messages asynchronously
        query.findInBackground(new FindCallback<Messages>() {

            @Override
            public void done(List<Messages> messageses, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messageses);
                    mAdapter.notifyDataSetChanged();
                    lvChat.invalidate();
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }
}
