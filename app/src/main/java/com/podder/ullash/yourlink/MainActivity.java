package com.podder.ullash.yourlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class MainActivity extends ActionBarActivity {
    private StringRequest myReq;
    private RequestQueue queue;
    private TextView result;
    EditText email;
    EditText password;
    //	private TextView labelView;
//	Button changeButton;
    String urlPostFix = "applogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        result = (TextView)this.findViewById(R.id.forgotPW);
        ImageButton login = (ImageButton)this.findViewById(R.id.loginButton);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        getSupportActionBar().hide();
//        getActionBar().setDisplayShowHomeEnabled(false);
        email = (EditText)findViewById(R.id.emailText);
        password = (EditText)findViewById(R.id.password);
//        TextView myTextView = (TextView) findViewById(R.id.textView2);
//        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/OxygenLight.ttf");
//        myTextView.setTypeface(type);
//        result.setText(email.getText().toString());
        Typeface mFont = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        ViewGroup root = (ViewGroup)findViewById(R.id.rootView);
        setFont(root, mFont);


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);// show the loading
//                // web service code
                queue = Volley.newRequestQueue(getApplicationContext());
                String fullUrl = getResources().getString(R.string.base_url) + urlPostFix;

                final String requestType = "0";

                myReq = new StringRequest(Request.Method.POST, fullUrl,
                        createMyReqSuccessListener(requestType),
                        createMyReqErrorListener()) {

                    protected Map<String, String> getParams()
                            throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());

                        return params;
                    }


                };
//                queue.add(myReq);

////                // web service code ends
                Intent intent = new Intent(getApplicationContext(), ProfilePhoto.class);
//                intent.putExtra("fnf_id","25");
                startActivity(intent);
            }
        });

    }

    /*
     * Sets the font on all TextViews in the ViewGroup. Searches
     * recursively for all inner ViewGroups as well. Just add a
     * check for any other views you want to set as well (EditText,
     * etc.)
     */
    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView ) /* || v instanceof Button */
                ((TextView)v).setTypeface(font);
            else if(v instanceof ViewGroup)
                setFont((ViewGroup)v, font);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void newAccountClicked(View v){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
    return new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);// hide the loading

            JSONObject responseObject;
                try {
            responseObject = new JSONObject(response);            //this works for Login service.
                    if(!responseObject.getBoolean("error")){
                        Long tsLong = System.currentTimeMillis()/100;//time strap
                        SharedPreferences settings = getSharedPreferences("UserData", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("api_key", responseObject.getString("api_key"));
                        editor.putString("email", responseObject.getString("email"));
                        editor.putString("image_url", responseObject.getString("image_url"));
                        editor.putString("message", responseObject.getString("message"));
                        editor.putString("postcode", responseObject.getString("postcode"));
                        editor.putString("user_id", responseObject.getString("user_id"));
                        editor.putString("user_name", responseObject.getString("user_name"));
                        editor.putString("user_role", responseObject.getString("user_role"));
                        editor.putString("time_strap", tsLong.toString());

                        // Commit the edits!
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), MyHome.class);
                        startActivity(intent);
//                        Intent intent = new Intent(getApplicationContext(), MessageFnF.class);
//                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Response Error", Toast.LENGTH_SHORT).show();
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
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error in response", Toast.LENGTH_SHORT).show();
            }
        };
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
