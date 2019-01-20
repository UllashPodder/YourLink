package com.podder.ullash.yourlink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MyCommunityList extends ActionBarActivity {
//    private ListView listViewSMS;
    private StringRequest myReq;
    private RequestQueue queue;
    private SharedPreferences settings;
    TextView categoryName;
    TextView homeText;
    LinearLayout fieldContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community_list);
//        Testing Shared preference
        settings = getSharedPreferences("UserData", 0);
        homeText = (TextView)this.findViewById(R.id.textView4);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        Typeface type2 = Typeface.createFromAsset(getAssets(), "fonts/OxygenLight.ttf");
        homeText.setTypeface(type);
        categoryName = (TextView)this.findViewById(R.id.topText);
        categoryName.setTypeface(type2);
//        listViewSMS=(ListView)findViewById(R.id.communityListView);
        fieldContainer = (LinearLayout)findViewById(R.id.fieldContainer);
        Button goback = (Button)this.findViewById(R.id.cancelButton);
        goback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button proceed = (Button)this.findViewById(R.id.proceedButton);
        proceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

//        Web service call
        queue = Volley.newRequestQueue(getApplicationContext());

        String fullUrl = getResources().getString(R.string.base_url) + "getSps/" + settings.getString("user_id","")+"/" + settings.getString("postcode","");

//        categoryName.setText(fullUrl);
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
//        categoryName.setText(fullUrl);

        queue.add(myReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_community_list, menu);
        return true;
    }


    private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "categories responded", Toast.LENGTH_SHORT).show();
//              Commit the shared preference edits!
                Long tsLong = System.currentTimeMillis()/100;//time strap
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("time_strap", tsLong.toString());
                editor.commit();

                JSONArray response_Array = null;
                JSONObject responseObject;
//                categoryName.setText(response);
                try {
                    responseObject = new JSONObject(response);            //this works for
                    if(!responseObject.getBoolean("error")){
//
                        response_Array= responseObject.getJSONArray("categories");
                        //____________________//
                        for(int j=0;j<response_Array.length();j++){
                            JSONArray sps_Array = response_Array.getJSONObject(j).getJSONArray("sps");

                            TextView category_title = new TextView(getApplicationContext());
                            category_title.setTextColor(Color.parseColor("#ff341bff"));
                            category_title.setText(response_Array.getJSONObject(j).getString("category_name"));

                            ListView myListView = new ListView(getApplicationContext());
                            final String[] image_url = new String[sps_Array.length()];
                            final Integer[] already_added = new Integer[sps_Array.length()];
                            final String[] organisation_name = new String[sps_Array.length()];
                            final String[] description = new String[sps_Array.length()];
                            final String[] user_id = new String[sps_Array.length()];
                            for (int i = 0; i < sps_Array.length(); i++){
                                JSONObject history_row = sps_Array.getJSONObject(i);
                                image_url[i] = history_row.getString("image_url");
                                already_added[i] = history_row.getInt("already_added");
                                organisation_name[i] = history_row.getString("organisation_name");
                                description[i] = history_row.getString("organisation_description");
                                user_id[i] = history_row.getString("user_id");
                            }
//                            Toast.makeText(getApplicationContext(), "Msg OK", Toast.LENGTH_SHORT).show();//                        response_Array= responseObject.getJSONArray("categories");//category1.getString("category_name")

//                        // Create the Adapter
                            CustomListAdapter smsListAdapter;
                            smsListAdapter=new CustomListAdapter(getApplicationContext(),user_id,image_url,organisation_name,description);
//
//                        // Set The Adapter to ListView
                            myListView.setAdapter(smsListAdapter);
                            fieldContainer.addView(category_title);
                            fieldContainer.addView(myListView);
                        }

                    }
                    else {
                        responseObject = new JSONObject(response);            //this works
//                        Toast.makeText(getApplicationContext(), "Response recieved", Toast.LENGTH_SHORT).show();
                    }
//
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //                        response_Array= responseObject.getJSONArray("categories");//category1.getString("category_name")
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


    public class CustomListAdapter  extends BaseAdapter
    {

        private Context mContext;
        String[] user_id;
        String[] image_url;
        String[] title;
        String[] body;

        public CustomListAdapter(Context context, String[]user_id,String[]image_url,String[]title,String[]body)
        {
            super();
            mContext=context;
            this.user_id=user_id;
            this.image_url=image_url;
            this.title=title;
            this.body=body;

        }

        public int getCount()
        {
            // return the number of records in cursor
            return title.length;
        }

        // getView method is called for each item of ListView
        public View getView(int position,  View view, ViewGroup parent)
        {
            // inflate the layout for each item of listView
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.community_link_row, null);

            // move the cursor to required position
//	                    name.moveToPosition(position);

            // fetch the sender number and sms body from cursor
            String senderNumber=title[position];
            String smsBody=body[position];


            // get the reference of textViews
            TextView textTitle=(TextView)view.findViewById(R.id.title);
            TextView textBody=(TextView)view.findViewById(R.id.body);
            ImageView loadImage = (ImageView)view.findViewById(R.id.iconImage);

            //Set Flag image
            Ion.with(loadImage)
                    .error(R.drawable.dummy_image)
                    .load(image_url[position]);
            // Set the Sender number and smsBody to respective TextViews
            textTitle.setText(senderNumber);
            textBody.setText(smsBody);
            return view;
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }

}
