package com.podder.ullash.yourlink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InviteFnF extends ActionBarActivity {
    private StringRequest myReq;
    private RequestQueue queue;
    String urlPostFix = "addfnf";
    String emailIDs;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_fn_f);
//        Testing Shared preference
        settings = getSharedPreferences("UserData", 0);

        // Set font for top label
        TextView homeText = (TextView)this.findViewById(R.id.textView4);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        homeText.setTypeface(type);
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
        Button invite = (Button)this.findViewById(R.id.invite);

        final EditText email1 = (EditText)this.findViewById(R.id.editText1);
        final EditText email2 = (EditText)this.findViewById(R.id.editText2);
        final EditText email3 = (EditText)this.findViewById(R.id.editText3);
        final EditText email4 = (EditText)this.findViewById(R.id.editText4);
        final EditText email5 = (EditText)this.findViewById(R.id.editText5);
        final EditText email6 = (EditText)this.findViewById(R.id.editText6);
        final EditText email7 = (EditText)this.findViewById(R.id.editText7);
        final EditText email8 = (EditText)this.findViewById(R.id.editText8);
        final EditText email9 = (EditText)this.findViewById(R.id.editText9);
        final EditText email10 = (EditText)this.findViewById(R.id.editText10);
        invite.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

//                // web service code
                emailIDs = "";
                if (email1.getText().length()>3) emailIDs = emailIDs + email1.getText().toString() +",";
                if (email2.getText().length()>3) emailIDs = emailIDs + email2.getText().toString() +",";
                if (email3.getText().length()>3) emailIDs = emailIDs + email3.getText().toString() +",";
                if (email4.getText().length()>3) emailIDs = emailIDs + email4.getText().toString() +",";
                if (email5.getText().length()>3) emailIDs = emailIDs + email5.getText().toString() +",";
                if (email6.getText().length()>3) emailIDs = emailIDs + email6.getText().toString() +",";
                if (email7.getText().length()>3) emailIDs = emailIDs + email7.getText().toString() +",";
                if (email8.getText().length()>3) emailIDs = emailIDs + email8.getText().toString() +",";
                if (email9.getText().length()>3) emailIDs = emailIDs + email9.getText().toString() +",";
                if (email10.getText().length()>3) emailIDs = emailIDs + email10.getText().toString() +",";

                if (emailIDs.length() > 0 && emailIDs.charAt(emailIDs.length()-1)==',') {
                    emailIDs = emailIDs.substring(0, emailIDs.length()-1);

                    queue = Volley.newRequestQueue(getApplicationContext());
                    String fullUrl = getResources().getString(R.string.base_url) + urlPostFix;
                    final String requestType = "0";
                    myReq = new StringRequest(Request.Method.POST, fullUrl,
                            createMyReqSuccessListener(requestType),
                            createMyReqErrorListener()) {

                        protected Map<String, String> getParams()
                                throws com.android.volley.AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_id", settings.getString("user_id", ""));
                            params.put("user_emails", emailIDs);
                            return params;
                        }

                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", settings.getString("api_key",""));

                            return params;
                        }

                    };
                    queue.add(myReq);
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(InviteFnF.this)
                            .create();
                    alertDialog.setTitle("Provide Email IDs");
                    alertDialog.setMessage("Email fields are blank");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }



            }
        });
    }


    private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);// hide the loading

                JSONObject responseObject;
//                AlertDialog alertDialog = new AlertDialog.Builder(InviteFnF.this)
//                        .create();
//                alertDialog.setTitle("Response");
//                alertDialog.setMessage(response);
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
                try {
                    responseObject = new JSONObject(response);
                    if(!responseObject.getBoolean("error")){
                        Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Response Error:" + responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener( ) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error in response", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void invite_fnf_backHomeClicked(View v){
        Intent intent = new Intent(this, MyHome.class);
        startActivity(intent);
    }
    public void invite_fnf_help(View v){
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite_fn, menu);
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
