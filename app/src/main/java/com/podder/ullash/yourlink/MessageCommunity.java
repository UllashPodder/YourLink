package com.podder.ullash.yourlink;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MessageCommunity extends ActionBarActivity {
    ListView listViewSMS;
    private StringRequest myReq;
    private RequestQueue queue;
    private SharedPreferences settings;
    TextView printIT;
    ImageButton sendMSG;
    EditText msgText;
    String  fnf_id;

    String[] message_id;
    String[] receiver_id;
    String[] description;
    String[] send_date;

    boolean deleteMsg = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_community);
//        Testing Shared preference
        settings = getSharedPreferences("UserData", 0);
        // Set font for top label
        printIT = (TextView)findViewById(R.id.textView4);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        printIT.setTypeface(type);
        Typeface chat = Typeface.createFromAsset(getAssets(), "fonts/OxygenBold.ttf");
        TextView chatName = (TextView)this.findViewById(R.id.friendName);
        chatName.setTypeface(chat);
        Bundle extras = getIntent().getExtras();
        fnf_id= extras.getString("fnf_id");

//        Load Friend's Image'
        String image_url = extras.getString("image_url");
        ImageView friendImage =(ImageView)findViewById(R.id.friendImage);
        Ion.with(friendImage)
                .error(R.drawable.blue_button)
                .load(image_url);

        RelativeLayout goback = (RelativeLayout)this.findViewById(R.id.backToFnf);
        goback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        listViewSMS=(ListView)findViewById(R.id.msgList);
        msgText = (EditText)findViewById(R.id.msgText);
        sendMSG=(ImageButton)findViewById(R.id.msg_send);
        ImageButton msgClear = (ImageButton)findViewById(R.id.msg_cancel);

        sendMSG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (msgText.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Empty Message Field", Toast.LENGTH_SHORT).show();
                }
                else {
//                Remove all items
                    listViewSMS.setAdapter(null);
//                Reload from service
                    sendChat();
                }
            }
        });
        msgClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msgText.setText("");
            }
        });
        loadMessages();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_fn, menu);
        return true;
    }
    private void loadMessages(){
        //        Web service call
        queue = Volley.newRequestQueue(getApplicationContext());

        String fullUrl = getResources().getString(R.string.base_url) + "getSpsMessage/" + fnf_id + "/" + settings.getString("user_id","");
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

    private void sendChat(){
//        msgText = (EditText)findViewById(R.id.msgText);
        if (msgText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Empty Message Field", Toast.LENGTH_SHORT).show();
        }
        else {
            String str = "sendIndividualMessage";

            queue = Volley.newRequestQueue(getApplicationContext());
            String fullUrl = getResources().getString(R.string.base_url) + str;

            final String requestType = "0";

            myReq = new StringRequest(Request.Method.POST, fullUrl,
                    createMyReqSuccessListener(requestType),
                    createMyReqErrorListener()) {

                protected Map<String, String> getParams()
                        throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sender_id", settings.getString("user_id",""));
                    params.put("sender_role", settings.getString("user_role",""));
                    params.put("receiver_id", fnf_id);
                    params.put("message_title", "Hello from Mobile");
                    params.put("description", msgText.getText().toString());
                    params.put("status", "sent");

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

    }

    private void dltMsg(String msgID){
//        msgText = (EditText)findViewById(R.id.msgText);
        String str = "deleteIndividualMessage";
        deleteMsg = true;
        queue = Volley.newRequestQueue(getApplicationContext());
        String fullUrl = getResources().getString(R.string.base_url) + str +"/"+ msgID +"/"+ settings.getString("user_id","");

        final String requestType = "0";

        myReq = new StringRequest(Request.Method.POST, fullUrl,
                createMyReqSuccessListener(requestType),
                createMyReqErrorListener()) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", settings.getString("api_key",""));

                return params;
            }
        };
        queue.add(myReq);

//        listViewSMS.setAdapter(null);
//        loadMessages();
//        Toast.makeText(getApplicationContext(), fullUrl, Toast.LENGTH_SHORT).show();

    }

    private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//              Commit the shared preference edits!
                Long tsLong = System.currentTimeMillis()/100;//time strap
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("time_strap", tsLong.toString());
                editor.commit();
                JSONArray response_Array = null;
                JSONObject responseObject;
//                if (deleteMsg){
//                    Toast.makeText(getApplicationContext(), "response:"+response, Toast.LENGTH_SHORT).show();
//                    listViewSMS.setAdapter(null);
//
//                }

                if(deleteMsg){
                    deleteMsg = false;
                    try {
                        responseObject = new JSONObject(response);            //this works for
                        if(!responseObject.getBoolean("error")){
                            Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_LONG).show();
                            //                Remove msg from list
                            listViewSMS.setAdapter(null);
//                            load Messages;
                            loadMessages();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Response Error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        responseObject = new JSONObject(response);            //this works for
                        if(!responseObject.getBoolean("error")){
                            response_Array= responseObject.getJSONArray("messages");
//                        printIT.setText(response_Array.toString());

                            message_id = new String[response_Array.length()];
                            receiver_id = new String[response_Array.length()];
                            description = new String[response_Array.length()];
                            send_date = new String[response_Array.length()];

                            for (int i = 0; i < response_Array.length(); i++){
                                JSONObject history_row = response_Array.getJSONObject(i);
                                message_id[i] = String.valueOf(history_row.getInt("message_id"));
                                receiver_id[i] = String.valueOf(history_row.getInt("receiver_id"));
                                description[i] = history_row.getString("description");
                                send_date[i] = history_row.getString("send_date");
                            }

//                        // Create the Adapter
                            CustomListAdapter smsListAdapter;
                            smsListAdapter=new CustomListAdapter(getApplicationContext(),receiver_id,description,send_date,message_id );
//
//                        // Set The Adapter to ListView
                            listViewSMS.setDivider(null);
                            listViewSMS.setAdapter(smsListAdapter);

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Response Error", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
        String[] name;
        String[] msgBody;
        String[] time;
        String[] person;
        String[] msgID;


        public CustomListAdapter(Context context, String[]person,String[]msgBody,String[]time, String[]msgID)
        {
            super();
            mContext=context;
            this.person=person;
            this.msgBody=msgBody;
            this.time=time;
            this.msgID = msgID;
        }

        public int getCount()
        {
            // return the number of records in cursor
            return msgBody.length;
        }

        // getView method is called for each item of ListView
        public View getView(final int position,  View view, ViewGroup parent)
        {
            // inflate the layout for each item of listView
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.message_row, null);


            // move the cursor to required position
//	                    name.moveToPosition(position);

            // fetch the sender number and sms body from cursor
            String dtStart = time[position];
//            String senderNumber=time[position];
            String smsBody=msgBody[position];
            // get the reference of textViews
            Typeface typeBody = Typeface.createFromAsset(getAssets(), "fonts/OxygenRegular.ttf");
            TextView msgBody=(TextView)view.findViewById(R.id.msgTextMe);
            msgBody.setTypeface(typeBody);
            Typeface typeDate = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");

            TextView dateMe=(TextView)view.findViewById(R.id.dateMe);
            TextView dateYou=(TextView)view.findViewById(R.id.dateYou);
            dateMe.setTypeface(typeBody);
            dateYou.setTypeface(typeBody);

            ImageView dltMsgMe = (ImageView)view.findViewById(R.id.dltMsgMe);
            ImageView dltMsgYou = (ImageView)view.findViewById(R.id.dltMsgYou);


            // Set the Sender number and smsBody to respective TextViews
            try {
                DateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1 = inputDF.parse(dtStart);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);

                int month = cal.get(Calendar.MONTH);
                int date = cal.get(Calendar.DAY_OF_MONTH);
//                int day = cal.get(Calendar.DAY_OF_WEEK);

                int year = cal.get(Calendar.YEAR);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                dateYou.setText(minute +":"+hour+" " + String.valueOf(date)+" " + new DateFormatSymbols().getMonths()[month] +" "+ String.valueOf(year));
                dateYou.setText(minute +":"+hour+" " + String.valueOf(date)+" " + new DateFormatSymbols().getMonths()[month] +" "+ String.valueOf(year));

//                dateMe.setText(senderNumber);
//                dateYou.setText(senderNumber);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            msgBody.setText(smsBody);
            if (person[position].equals(settings.getString("user_id",""))){
                dateYou.setVisibility(view.INVISIBLE);
                dltMsgYou.setVisibility(view.INVISIBLE);

                dltMsgMe.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dltMsg(msgID[position]);
                    }
                });
                int resID = getResources().getIdentifier("msg_box_you" , "drawable", getApplicationContext().getPackageName());
                msgBody.setBackgroundResource(resID);
            }
            else {
                dateMe.setVisibility(view.INVISIBLE);
                dltMsgMe.setVisibility(view.INVISIBLE);
                dltMsgYou.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "Msg dlt: "+position, Toast.LENGTH_SHORT).show();
                        dltMsg(msgID[position]);
                    }
                });
            }


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
