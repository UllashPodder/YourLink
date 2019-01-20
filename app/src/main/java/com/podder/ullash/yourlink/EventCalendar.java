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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EventCalendar extends ActionBarActivity {
    ListView listViewSMS;
    private StringRequest myReq;
    private RequestQueue queue;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_calendar);
//        Testing Shared preference
        settings = getSharedPreferences("UserData", 0);

        Button goback = (Button)this.findViewById(R.id.backButton);
        goback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

//        Web service call
        queue = Volley.newRequestQueue(getApplicationContext());

        String fullUrl = getResources().getString(R.string.base_url) + "getBroadcastForAppUser/" + settings.getString("user_id","")+"/1/" + settings.getString("time_strap","");

//        printIT.setText(fullUrl);
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
        getMenuInflater().inflate(R.menu.menu_event_calendar, menu);
        return true;
    }
    private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Calender responded", Toast.LENGTH_SHORT).show();
//              Commit the shared preference edits!
                Long tsLong = System.currentTimeMillis()/100;//time strap
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("time_strap", tsLong.toString());
                editor.commit();

                JSONArray response_Array = null;
                JSONObject responseObject;

                try {
                    responseObject = new JSONObject(response);            //this works for
                    if(!responseObject.getBoolean("error")){
                        response_Array= responseObject.getJSONArray("messages");
                        final String[] image_url = new String[response_Array.length()];
                        final String[] message_title = new String[response_Array.length()];
                        final String[] organisation_name = new String[response_Array.length()];
                        final String[] description = new String[response_Array.length()];
                        final String[] start_date = new String[response_Array.length()];
                        for (int i = 0; i < response_Array.length(); i++){
                            JSONObject history_row = response_Array.getJSONObject(i);
                            image_url[i] = history_row.getString("image_url");
                            message_title[i] = history_row.getString("message_title");
                            organisation_name[i] = history_row.getString("organisation_name");
                            description[i] = history_row.getString("description");
                            start_date[i] = history_row.getString("start_date");
                        }

//                        Toast.makeText(getApplicationContext(), "Msg OK", Toast.LENGTH_SHORT).show();//                        response_Array= responseObject.getJSONArray("categories");//category1.getString("category_name")
                        String[] day = new String[] { "Saturday", "Saturday", "Saturday",
                                "Sunday", "Saturday", "Friday", "Monday", "Saturday",
                                "Saturday", "Saturday" };
                        String[] date = new String[] { "Saturday", "Saturday", "Saturday",
                                "Sunday", "Saturday", "Friday", "Monday", "Saturday",
                                "Saturday", "Saturday" };
                        String[] titles = new String[] { "title1", "title2", "title3",
                                "title4", "title5", "title1", "title2", "title3",
                                "title4", "title5" };
                        String[] bodies = new String[] { "body1", "body2", "body3",
                                "body4", "body5","body1", "body2", "body3",
                                "body4", "body5" };
                        listViewSMS=(ListView)findViewById(R.id.eventListView);
//                        // Create the Adapter
                        CustomListAdapter smsListAdapter;
                        smsListAdapter=new CustomListAdapter(getApplicationContext(),start_date,message_title,message_title,organisation_name);
//
//                        // Set The Adapter to ListView
                        listViewSMS.setAdapter(smsListAdapter);

                    }
                    else {
                        responseObject = new JSONObject(response);            //this works
//                        Toast.makeText(getApplicationContext(), "Response recieved", Toast.LENGTH_SHORT).show();
                    }

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
//                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
        String[] dateTime;
        String[] time;
        String[] title;
        String[] body;

        public CustomListAdapter(Context context, String[]dateTime,String[]time,String[]title,String[]body)
        {
            super();
            mContext=context;
            this.dateTime=dateTime;
            this.time=time;
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
            view = inflater.inflate(R.layout.event_list_row, null);

            // move the cursor to required position
//	                    name.moveToPosition(position);

            // fetch the sender number and sms body from cursor
            String senderNumber=title[position];
            String smsBody=body[position];

            Typeface typeTitle = Typeface.createFromAsset(getAssets(), "fonts/OxygenBold.ttf");
            Typeface typeBody = Typeface.createFromAsset(getAssets(), "fonts/OxygenRegular.ttf");
            // get the reference of textViews
            TextView textDay=(TextView)view.findViewById(R.id.day);
            TextView textDate=(TextView)view.findViewById(R.id.date);
            TextView textTime=(TextView)view.findViewById(R.id.monthYear);
            TextView textTitle=(TextView)view.findViewById(R.id.title);
            textTitle.setTypeface(typeTitle);
            TextView textBody=(TextView)view.findViewById(R.id.body);
            textBody.setTypeface(typeBody);

            // Set the Sender number and smsBody to respective TextViews
            textTitle.setText(senderNumber);
            textBody.setText(smsBody);
            String dtStart = dateTime[position];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                DateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1 = inputDF.parse(dtStart);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);

                int month = cal.get(Calendar.MONTH);
                int date = cal.get(Calendar.DAY_OF_MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);

                int year = cal.get(Calendar.YEAR);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
//                textDate.setText(minute+" - "+day+" - "+hour);
                textDate.setText(String.valueOf(date));
                textDay.setText(new DateFormatSymbols().getWeekdays()[day]);
                textTime.setText(new DateFormatSymbols().getMonths()[month] +" "+ String.valueOf(year));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Set Flag image
            //for flag image
//            ImageView flag1;
//            ImageView flag2;
//            flag1 = (ImageView)view.findViewById(R.id.flag1);
//            flag2 = (ImageView)view.findViewById(R.id.flag2);
//            //set flags
//            String mDrawableName = team1[position].toLowerCase();
//            mDrawableName = mDrawableName.replace(" ", "");
//            int resID = getResources().getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());
//
//            flag1.setImageResource(resID);
//            mDrawableName = team2[position].toLowerCase();
//            mDrawableName = mDrawableName.replace(" ", "");
//            resID = getResources().getIdentifier(mDrawableName , "drawable", getActivity().getPackageName());
//
//            flag2.setImageResource(resID);
            return view;
        }
        public String getMonth(int month) {
            return new DateFormatSymbols().getMonths()[month-1];
        }
        public String getWeekDay(int month) {
            return new DateFormatSymbols().getWeekdays()[month-1];
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
