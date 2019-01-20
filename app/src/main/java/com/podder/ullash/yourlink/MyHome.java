package com.podder.ullash.yourlink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
//import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyHome extends ActionBarActivity {
    private StringRequest myReq;
    private RequestQueue queue;
    private SharedPreferences settings;
    String category_id;
    TextView homeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);
//        Testing Shared preference
        settings = getSharedPreferences("UserData", 0);
        homeText = (TextView)this.findViewById(R.id.textView4);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        homeText.setTypeface(type);
//        result.setText(email.getText().toString());

        //HOME & HELP button
        ImageView home = (ImageView)this.findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MyHome.class);
//                startActivity(intent);
            }
        });
        ImageView help = (ImageView)this.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Help.class);
                startActivity(intent);
            }
        });

//Tab actions
        RelativeLayout tab2 = (RelativeLayout)this.findViewById(R.id.tab2);
        tab2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Work in Progress", Toast.LENGTH_SHORT).show();
            }
        });
        RelativeLayout tab3 = (RelativeLayout)this.findViewById(R.id.tabCommunityLink);
        tab3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyCommunityList.class);
                startActivity(intent);
            }
        });
        RelativeLayout tab4 = (RelativeLayout)this.findViewById(R.id.tab4);
        tab4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Work in Progress", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CommunityNoticeboard.class);
                startActivity(intent);
            }
        });
        RelativeLayout tab5 = (RelativeLayout)this.findViewById(R.id.tabEvents);
        tab5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventCalendar.class);
                startActivity(intent);
            }
        });

//        Load home buttons
        // web service code

        queue = Volley.newRequestQueue(getApplicationContext());

        String fullUrl = getResources().getString(R.string.base_url) + "getLevelOneWithUnreadMsg/"+settings.getString("user_id","")+"/" + settings.getString("time_strap","");


        final String requestType = "0";
        myReq = new StringRequest(Request.Method.GET, fullUrl,
                createMyReqSuccessListener(requestType),
                createMyReqErrorListener()) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", settings.getString("api_key",""));

                return params;
            }


        };

        queue.add(myReq);

    }
    public void home_FnF(View v){
        Intent intent = new Intent(this, UpdateFnf.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_home, menu);
        return true;
    }
    private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "categories responded", Toast.LENGTH_SHORT).show();
//              Commit the shared preference edits!
                Long tsLong = System.currentTimeMillis()/100;//time strap
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("time_strap", tsLong.toString());
                editor.commit();
//                homeText.setText(response);

                JSONArray response_Array = null;
                JSONObject responseObject;

                try {
                    responseObject = new JSONObject(response);            //this works for
                    if("Ok".equals(responseObject.getString("message")))
                    {
                        response_Array= responseObject.getJSONArray("categories");
//                    Load Home buttons
                        LinearLayout buttonContainer = (LinearLayout)findViewById(R.id.home_button_Container);

                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                        for (int i=0;i<response_Array.length();i+=3){

                            View view1 = inflater.inflate(R.layout.home_buttons, null);
                            TextView textTitle1=(TextView)view1.findViewById(R.id.textView1);
                            TextView count1=(TextView)view1.findViewById(R.id.count1);
                            ImageView red1= (ImageView)view1.findViewById(R.id.red1);
                            ImageButton button1= (ImageButton)view1.findViewById(R.id.button1);
                            TextView textTitle2=(TextView)view1.findViewById(R.id.textView2);
                            TextView count2=(TextView)view1.findViewById(R.id.count2);
                            ImageView red2= (ImageView)view1.findViewById(R.id.red2);
                            ImageButton button2= (ImageButton)view1.findViewById(R.id.button2);
                            TextView textTitle3=(TextView)view1.findViewById(R.id.textView3);
                            TextView count3=(TextView)view1.findViewById(R.id.count3);
                            ImageView red3= (ImageView)view1.findViewById(R.id.red3);
                            ImageButton button3= (ImageButton)view1.findViewById(R.id.button3);
                            JSONObject category1 = response_Array.getJSONObject(i);
                            textTitle1.setText(category1.getString("category_name"));
                            category_id = category1.getString("category_id");
                            if("1".equals(category1.getString("category_id"))){
//                                homeText.setText(category1.getString("category_id"));
                                button1.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), FamilyAndFriends.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            else {
                                button1.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), CommunityLists.class);
                                        intent.putExtra("category_id",category_id);
                                        startActivity(intent);
                                    }
                                });
                            }
                            if (category1.getInt("no_of_msg")>0){
                                count1.setText(String.valueOf(category1.getInt("no_of_msg")));
                                red1.setVisibility(view1.VISIBLE);
                            }


                            if ((i+1)<response_Array.length()){

                                JSONObject category2 = response_Array.getJSONObject(i+1);
                                textTitle2.setText(category2.getString("category_name"));

                                category_id = category2.getString("category_id");
                                if("1".equals(category2.getString("category_id"))){
                                    button2.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), FamilyAndFriends.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                else {
                                    button2.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), CommunityLists.class);
                                            intent.putExtra("category_id",category_id);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                if (category2.getInt("no_of_msg")>0){
                                    count2.setText(String.valueOf(category2.getInt("no_of_msg")));
                                    red2.setVisibility(view1.VISIBLE);
                                }
                            }
                            else {
                                button2.setVisibility(view1.INVISIBLE);
                            }

                            if ((i+2)<response_Array.length()){

                                JSONObject category3 = response_Array.getJSONObject(i+2);
                                textTitle3.setText(category3.getString("category_name"));

                                category_id = category3.getString("category_id");
                                if("1".equals(category3.getString("category_id"))){
                                    button3.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), FamilyAndFriends.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                else {
                                    button3.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), CommunityLists.class);
                                            intent.putExtra("category_id",category_id);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                if (category3.getInt("no_of_msg")>0){
                                    count3.setText(String.valueOf(category3.getInt("no_of_msg")));
                                    red3.setVisibility(view1.VISIBLE);
                                }
                            }
                            else {
                                button3.setVisibility(view1.INVISIBLE);
                            }

                            buttonContainer.addView(view1);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error in response", Toast.LENGTH_SHORT).show();
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
