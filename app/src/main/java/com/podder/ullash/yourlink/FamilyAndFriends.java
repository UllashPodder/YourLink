package com.podder.ullash.yourlink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FamilyAndFriends extends ActionBarActivity {
    private StringRequest myReq;
    private RequestQueue queue;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_and_friends);
        settings = getSharedPreferences("UserData", 0);
        TextView homeText = (TextView)this.findViewById(R.id.textView4);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        homeText.setTypeface(type);

//HOME & help
        ImageView home = (ImageView)this.findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
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
        RelativeLayout tab1 = (RelativeLayout)this.findViewById(R.id.tabFnfAdd);
        tab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InviteFnF.class);
                startActivity(intent);
                //
            }
        });
        //Tab actions
        RelativeLayout tab2 = (RelativeLayout)this.findViewById(R.id.tabProfile);
        tab2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePhoto.class);
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
        RelativeLayout tab4 = (RelativeLayout)this.findViewById(R.id.tabCommunityNotice);
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

        // web service code

        queue = Volley.newRequestQueue(getApplicationContext());

        String fullUrl = getResources().getString(R.string.base_url) + "getFnfWithUnreadMsgForApp/"+settings.getString("user_id","")+"/" + settings.getString("time_strap","");


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_family_and_friends, menu);
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
                        LinearLayout buttonContainer = (LinearLayout)findViewById(R.id.fnf_button_Container);

                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                        for (int i=0;i<response_Array.length();i+=3){

                            View view1 = inflater.inflate(R.layout.fnf_buttons, null);
                            TextView textTitle1=(TextView)view1.findViewById(R.id.textView1);
                            TextView count1=(TextView)view1.findViewById(R.id.count1);
                            ImageView red1= (ImageView)view1.findViewById(R.id.red1);
                            ImageButton button1= (ImageButton)view1.findViewById(R.id.button1);

                            TextView textTitle2=(TextView)view1.findViewById(R.id.textView2);
                            TextView count2=(TextView)view1.findViewById(R.id.count2);
                            ImageView red2= (ImageView)view1.findViewById(R.id.red2);
                            ImageView dummy2= (ImageView)view1.findViewById(R.id.dummyImage2);
                            ImageButton button2= (ImageButton)view1.findViewById(R.id.button2);

                            TextView textTitle3=(TextView)view1.findViewById(R.id.textView3);
                            TextView count3=(TextView)view1.findViewById(R.id.count3);
                            ImageView red3= (ImageView)view1.findViewById(R.id.red3);
                            ImageView dummy3= (ImageView)view1.findViewById(R.id.dummyImage3);
                            ImageButton button3= (ImageButton)view1.findViewById(R.id.button3);


                            JSONObject category1 = response_Array.getJSONObject(i);
                            textTitle1.setText(category1.getString("first_name")+category1.getString("last_name"));
                            final String fnf_id1 = category1.getString("user_id");
                            final String image_url1 = category1.getString("image_url");
                            Ion.with(button1)
                            .error(R.drawable.blue_button)
                            .load(category1.getString("image_url"));

                            button1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), MessageFnF.class);
                                    intent.putExtra("fnf_id",fnf_id1);
                                    intent.putExtra("image_url",image_url1);
                                    startActivity(intent);
                                }
                            });

                            if (category1.getInt("no_of_msg")>0){
                                count1.setText(String.valueOf(category1.getInt("no_of_msg")));
                                red1.setVisibility(view1.VISIBLE);
                            }

                            if ((i+1)<response_Array.length()){

                                JSONObject category2 = response_Array.getJSONObject(i+1);
                                textTitle2.setText(category2.getString("first_name")+category2.getString("last_name"));
                                final String fnf_id2 = category2.getString("user_id");
                                final String image_url2 = category2.getString("image_url");
                                Ion.with(button2)
                                        .error(R.drawable.blue_button)
                                        .load(category2.getString("image_url"));

                                button2.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), MessageFnF.class);
                                        intent.putExtra("fnf_id",fnf_id2);
                                        intent.putExtra("image_url",image_url2);
                                        startActivity(intent);
                                    }
                                });

                                if (category2.getInt("no_of_msg")>0){
                                    count2.setText(String.valueOf(category2.getInt("no_of_msg")));
                                    red2.setVisibility(view1.VISIBLE);
                                }
                            }
                            else {
                                dummy2.setVisibility(view1.INVISIBLE);
                                button2.setVisibility(view1.INVISIBLE);
                            }

                            if ((i+2)<response_Array.length()){

                                JSONObject category3 = response_Array.getJSONObject(i+2);
                                textTitle3.setText(category3.getString("first_name")+category3.getString("last_name"));
                                final String fnf_id3 = category3.getString("user_id");
                                final String image_url3 = category3.getString("image_url");
                                Ion.with(button3)
                                        .error(R.drawable.blue_button)
                                        .load(category3.getString("image_url"));

                                button3.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), MessageFnF.class);
                                        intent.putExtra("fnf_id",fnf_id3);
                                        intent.putExtra("image_url",image_url3);
                                        startActivity(intent);
                                    }
                                });
                                if (category3.getInt("no_of_msg")>0){
                                    count3.setText(String.valueOf(category3.getInt("no_of_msg")));
                                    red3.setVisibility(view1.VISIBLE);
                                }
                            }
                            else {
                                dummy3.setVisibility(view1.INVISIBLE);
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
