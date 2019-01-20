package com.podder.ullash.yourlink;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class UpdateFnf extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        // Set font for top label
        TextView homeText = (TextView)this.findViewById(R.id.textView4);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        homeText.setTypeface(type);

        TextView upText = (TextView)this.findViewById(R.id.textView1);
        Typeface type2 = Typeface.createFromAsset(getAssets(), "fonts/OxygenLight.ttf");
        upText.setTypeface(type2);
        TextView downText = (TextView)this.findViewById(R.id.textView2);
        downText.setTypeface(type2);

        //HOME & help
        ImageView home = (ImageView)this.findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyHome.class);
                startActivity(intent);
            }
        });
        ImageView help = (ImageView)this.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Help.class);
                startActivity(intent);
            }
        });

        Button addFriend = (Button)this.findViewById(R.id.addFnF);
        addFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteFnF.class);
                startActivity(intent);
            }
        });
        Button addCommunity = (Button)this.findViewById(R.id.addCommunity);
        addCommunity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteFnF.class);
                startActivity(intent);
            }
        });
        Button addPic = (Button)this.findViewById(R.id.addProPic);
        addPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteFnF.class);
                startActivity(intent);
            }
        });
    }

    public void proceedClicked(View v){
        Intent intent = new Intent(this, ProfilePhoto.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_profile, menu);
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
}
