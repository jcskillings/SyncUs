package com.parse.starter;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Owner on 5/1/2015.
 */
public class FriendActivity extends Activity{
    protected static final String TAG = null;

    ParseObject po;

    @Override
    public void onStart() {
        super.onStart();
        //UAirship.shared().getAnalytics();

    }
    String username;
    String name;
    String friendId;
    Boolean work;
    Boolean school;
    Boolean all;
    Boolean personal;
    Boolean family;
    Boolean friend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        final EditText inviteCode = (EditText)findViewById(R.id.editText);
        final EditText nicknameEdit = (EditText)findViewById(R.id.nickname);
        final CheckBox allBox = (CheckBox)findViewById(R.id.all);
        final CheckBox friendBox = (CheckBox)findViewById(R.id.friend);
        final CheckBox familyBox = (CheckBox)findViewById(R.id.family);
        final CheckBox workBox = (CheckBox)findViewById(R.id.work);
        final CheckBox schoolBox = (CheckBox)findViewById(R.id.school);
        final CheckBox personalBox = (CheckBox)findViewById(R.id.personal);
        Button view = (Button)findViewById(R.id.view);
        Button search = (Button)findViewById(R.id.find);
        Button Add = (Button)findViewById(R.id.Add);
        Button pending = (Button)findViewById(R.id.pending);
        final TextView ResultText = (TextView)findViewById(R.id.ResultTextView);
        final TextView ResultText2 = (TextView)findViewById(R.id.ResultTextView2);
        final TextView ResultText3 = (TextView)findViewById(R.id.ResultTextView3);
        final FrameLayout ResultFrame = (FrameLayout)findViewById(R.id.ResultFrameLayout);

        //ResultFrame.setVisibility(View.GONE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FriendActivity.this, ViewFriends.class);
                startActivity(intent);
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FriendActivity.this, PendingFriends.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String code = inviteCode.getText().toString();

                final ParseQuery query = ParseUser.getQuery();
                query.whereEqualTo("invite_code", code);
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        // TODO Auto-generated method stub
                        try {
                            ParseObject userObject = objects.get(0);
                            username = userObject.getString("username");
                            name = userObject.getString("name");
                            friendId = userObject.getObjectId();
                            ResultText.setText(username);
                            ResultText2.setText(name);
                            ResultText3.setText(friendId);
                            ResultFrame.setVisibility(View.VISIBLE);
                            nicknameEdit.setVisibility(View.VISIBLE);

                            Toast.makeText(getApplicationContext(), "Player Found",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Username Not Found",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });

        Add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //String Friends = username.getText().toString();
                final ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    {

                        final ParseObject friend = new ParseObject("Friends");
                        friend.put("username", username);
                        friend.put("nickname", nicknameEdit.getText().toString());
                        friend.put("all", allBox.isChecked());
                        friend.put("friend", friendBox.isChecked());
                        friend.put("family", familyBox.isChecked());
                        friend.put("work", workBox.isChecked());
                        friend.put("school", schoolBox.isChecked());
                        friend.put("personal", personalBox.isChecked());
                        friend.put("friend_id", friendId);

                        friend.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                // TODO Auto-generated method stub
                                ParseRelation relation = currentUser.getRelation("Friends");
                                relation.add(friend);
                                currentUser.saveInBackground();
                                ResultFrame.setVisibility(View.INVISIBLE);
                                nicknameEdit.setVisibility(View.INVISIBLE);
                            }

                        });

                        Toast.makeText(getApplicationContext(), "Player Has Been Added",
                                Toast.LENGTH_LONG).show();

                    }
                }

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
