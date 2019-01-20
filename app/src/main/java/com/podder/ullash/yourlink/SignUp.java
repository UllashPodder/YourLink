package com.podder.ullash.yourlink;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


public class SignUp extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Spinner country_spinner;
    Spinner state_spinner;
    Spinner suburb_spinner;
    Spinner postcode_spinner;

    private StringRequest myReq;
    private RequestQueue queue;
    EditText fName;
    EditText lName;
    EditText contact;
    EditText residence;
    EditText email;
    EditText password;
    EditText passwordAgain;
    EditText stateText;
    EditText suburbText;
    EditText postcodeText;
    String urlPostFix = "appregistration";
    boolean checkBox = false;
    ImageButton checkButton;
    boolean gotSpinnerValues=false;
    boolean BDSelected=false;
    boolean AusSelected=false;
    //    String placesList [][][][];
    JSONArray country_Array;
    JSONArray states_Array;
    JSONArray postcodes_Array;
    JSONArray suburbs_Array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        // Set font for top label
        Typeface mFont = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");
        ViewGroup root = (ViewGroup)findViewById(R.id.rootView);
        setFont(root, mFont);
        getSupportActionBar().hide();
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        checkButton = (ImageButton) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkBox){
                    checkButton.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.check_off));
                    checkBox = false;
                }
                else {
                    checkButton.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.check_on));
                    checkBox = true;
                }
            }
        });
        spinner1 = (Spinner) findViewById(R.id.editText1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.title_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner2 = (Spinner) findViewById(R.id.editText14);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner3 = (Spinner) findViewById(R.id.editText15);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.months_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner4 = (Spinner) findViewById(R.id.editText16);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.years_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(R.layout.spinner_dropdown);

        country_spinner = (Spinner) findViewById(R.id.editText5);
        country_spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.countries_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        state_spinner = (Spinner) findViewById(R.id.editText6);
        state_spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.states_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter6.setDropDownViewResource(R.layout.spinner_dropdown);

        suburb_spinner = (Spinner) findViewById(R.id.editText8);
        suburb_spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this,
                R.array.suburb_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter7.setDropDownViewResource(R.layout.spinner_dropdown);

        postcode_spinner = (Spinner) findViewById(R.id.editText9);
        postcode_spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(this,
                R.array.postcode_array, R.layout.spinner);
// Specify the layout to use when the list of choices appears
        adapter8.setDropDownViewResource(R.layout.spinner_dropdown);

// Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);
//        country_spinner.setAdapter(adapter5);
//        state_spinner.setAdapter(adapter6);
//        suburb_spinner.setAdapter(adapter7);
//        postcode_spinner.setAdapter(adapter8);

        fName = (EditText)findViewById(R.id.editText2);
        lName = (EditText)findViewById(R.id.editText3);
        contact = (EditText)findViewById(R.id.editText4);
        residence = (EditText)findViewById(R.id.editText10);
        email = (EditText)findViewById(R.id.editText11);
        password = (EditText)findViewById(R.id.editText12);
        passwordAgain = (EditText)findViewById(R.id.editText13);

        stateText = (EditText)findViewById(R.id.stateText);
        postcodeText = (EditText)findViewById(R.id.postCodeText);
        suburbText = (EditText)findViewById(R.id.suburbText);

        ImageButton signUp = (ImageButton)this.findViewById(R.id.createProfile);


        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

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
                        params.put("first_name", fName.getText().toString());
                        params.put("last_name", lName.getText().toString());
                        params.put("phone", contact.getText().toString());
                        params.put("residence", residence.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("salutation", spinner1.getSelectedItem().toString());
                        params.put("gender", spinner2.getSelectedItem().toString());
                        params.put("date_of_birth_month", spinner3.getSelectedItem().toString());
                        params.put("date_of_birth_year", spinner4.getSelectedItem().toString());
                        params.put("country", country_spinner.getSelectedItem().toString());
                        if(BDSelected){
                            params.put("state", stateText.getText().toString());
                            params.put("suburb", suburbText.getText().toString());
                            params.put("postcode", postcodeText.getText().toString());
                        }
                        else {
                            params.put("state", state_spinner.getSelectedItem().toString());
                            params.put("suburb", suburb_spinner.getSelectedItem().toString());
                            params.put("postcode", postcode_spinner.getSelectedItem().toString());
                        }
                        return params;
                    }


                };
                if (gotSpinnerValues && checkBox){
                    String alertMsg= "";
                    if (fName.getText().length()<1) alertMsg = alertMsg + "provide First Name, ";
                    if (lName.getText().length()<1) alertMsg = alertMsg + "provide Last Name, ";
                    if (contact.getText().length()<1) alertMsg = alertMsg + "provide Phone Number, ";
                    if (email.getText().length()<1) alertMsg = alertMsg + "provide Email, ";

                    if (passwordAgain.getText().toString().equals(password.getText().toString())){
                        if (password.getText().length()<1) alertMsg = alertMsg + "provide Password, ";
                        if (passwordAgain.getText().length()<1) alertMsg = alertMsg + "Repeat Password, ";
                    }
                    else
                        alertMsg = alertMsg + "Password doesn't match, ";

                    if (spinner1.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide Title, ";
                    if (spinner2.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide Gender, ";
                    if (spinner3.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide Month, ";
                    if (spinner4.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide Year, ";
                    if (country_spinner.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide Country Name, ";
                    if(AusSelected){
                        if (state_spinner.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide State Name, ";
                        if (suburb_spinner.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide Suburb Name, ";
                        if (postcode_spinner.getSelectedItem().toString().length()<1) alertMsg = alertMsg + "provide Postcode";
                    }

                    if(alertMsg.length()>1){
                        alertMsg = "Please " + alertMsg;
                        AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this)
                                .create();
                        alertDialog.setTitle("Invalid Information");
                        alertDialog.setMessage(alertMsg);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else {
                        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);// show the loading and call for signup
                        queue.add(myReq);
                    }

                }
                else {

                    AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this)
                            .create();
                    alertDialog.setTitle("Please Complete Information");
                    alertDialog.setMessage("Complete all Fields and click the Check Box");
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
        //Fetch spinner values with: relationaldatalist
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);// show the loading

        queue = Volley.newRequestQueue(getApplicationContext());
        String fullUrl = getResources().getString(R.string.base_url) + "relationaldatalist";
        final String requestType = "0";

        myReq = new StringRequest(Request.Method.GET, fullUrl,
                createMyReqSuccessListener(requestType),
                createMyReqErrorListener()) {


        };
        queue.add(myReq);
    }


    private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);// hide the loading

                JSONObject responseObject;
                if(gotSpinnerValues){
                    try {
                        responseObject = new JSONObject(response);
                        if(!responseObject.getBoolean("error")){
                            Long tsLong = System.currentTimeMillis()/100;//time strap
                            SharedPreferences settings = getSharedPreferences("UserData", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("api_key", responseObject.getString("api_key"));
                            editor.putString("image_url", responseObject.getString("image_url"));
                            editor.putString("message", responseObject.getString("message"));
                            editor.putString("postcode", responseObject.getString("postcode"));
                            editor.putString("user_id", responseObject.getString("user_id"));
                            editor.putString("time_strap", tsLong.toString());
//                            Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();

                            // Commit the edits!
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), UpdateFnf.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Response Error:"+responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        responseObject = new JSONObject(response);            //this works for Login service.
                        if("Ok".equals(responseObject.getString("message"))){

                            country_Array = responseObject.getJSONArray("countries");
                            String country[]= new String[country_Array.length()+1];
                            country[0]="";
                            for (int i = 0; i < country_Array.length(); i++){
                                JSONObject country_row = country_Array.getJSONObject(i);
//                                states_Array= country_row.getJSONArray("states");
                                country[i+1]=country_row.getString("country_name");
                            }
                            ArrayAdapter<CharSequence> adapter1= new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner, country);
                            adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
                            country_spinner.setAdapter(adapter1);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error Loading Country list", Toast.LENGTH_SHORT).show();
                        }

                        gotSpinnerValues=true; //set spinner data value loaded.
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
//                Toast.makeText(getApplicationContext(), "Error in response", Toast.LENGTH_SHORT).show();
            }
        };
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        if (position== 0) return;

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.editText5){
//            Toast.makeText(getApplicationContext(), "Country selected:"+position , Toast.LENGTH_SHORT).show();
            state_spinner.setSelection(0);          //reset other
            postcode_spinner.setSelection(0);       //spinners to first selected position
            suburb_spinner.setSelection(0);

            if("Bangladesh".equals(country_spinner.getSelectedItem().toString())){
                Toast.makeText(getApplicationContext(), "Bangladesh selected:"+position , Toast.LENGTH_SHORT).show();
                state_spinner.setVisibility(View.INVISIBLE);
                postcode_spinner.setVisibility(View.INVISIBLE);
                suburb_spinner.setVisibility(View.INVISIBLE);
                BDSelected = true;
                AusSelected = false;
            }
            else {
                state_spinner.setVisibility(View.VISIBLE);
                postcode_spinner.setVisibility(View.VISIBLE);
                suburb_spinner.setVisibility(View.VISIBLE);
                BDSelected = false;
                AusSelected = true;

                try {
                    JSONObject country_row = country_Array.getJSONObject(position-1);
                    states_Array= country_row.getJSONArray("states");

                    String states[]= new String[states_Array.length()+1];
                    states[0]="";
                    for (int j = 0; j < states_Array.length(); j++){
                        JSONObject states_row = states_Array.getJSONObject(j);
                        states[j+1]=states_row.getString("state_name");
                    }
//                Toast.makeText(getApplicationContext(), "no. of States:"+states.length , Toast.LENGTH_SHORT).show();

                    ArrayAdapter<CharSequence> adapter1= new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner, states);
                    adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
                    state_spinner.setAdapter(adapter1);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        else if(spinner.getId() == R.id.editText6)
        {
            //State selected
//            Toast.makeText(getApplicationContext(), "State selected:"+position , Toast.LENGTH_SHORT).show();
            postcode_spinner.setSelection(0);       //set spinners to first selected position
            suburb_spinner.setSelection(0);
            try {
                JSONObject state_row = states_Array.getJSONObject(position-1);
                postcodes_Array= state_row.getJSONArray("postcodes");

                String postcodes[]= new String[postcodes_Array.length()+1];
                postcodes[0]="";
                for (int j = 0; j < postcodes_Array.length(); j++){
                    JSONObject postcode_row = postcodes_Array.getJSONObject(j);
                    postcodes[j+1]=postcode_row.getString("postcode_name");
                }
//                Toast.makeText(getApplicationContext(), "no. of postcodes:"+postcodes.length , Toast.LENGTH_SHORT).show();
                    ArrayAdapter<CharSequence> adapter1= new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner, postcodes);
                    adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
                    postcode_spinner.setAdapter(adapter1);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if(spinner.getId() == R.id.editText9)
        {
            //postcode selected
//            Toast.makeText(getApplicationContext(), "suburbs selected:"+position , Toast.LENGTH_SHORT).show();
            suburb_spinner.setSelection(0);       //spinners to first selected position

            try {
                JSONObject state_row = postcodes_Array.getJSONObject(position-1);
                suburbs_Array= state_row.getJSONArray("suburbs");

                String suburbs[]= new String[suburbs_Array.length()+1];
                suburbs[0]="";
                for (int j = 0; j < suburbs_Array.length(); j++){
                    JSONObject suburbs_row = suburbs_Array.getJSONObject(j);
                    suburbs[j+1]=suburbs_row.getString("suburb_name");
                }
//                Toast.makeText(getApplicationContext(), "no. of suburbs:"+suburbs.length , Toast.LENGTH_SHORT).show();
                    ArrayAdapter<CharSequence> adapter1= new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner, suburbs);
                    adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
                    suburb_spinner.setAdapter(adapter1);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if(spinner.getId() == R.id.editText8)
        {
            //suburb selected
//            Toast.makeText(getApplicationContext(), "Suburb selected:"+position , Toast.LENGTH_SHORT).show();
        }
//        if (position==1){
//            Toast.makeText(getApplicationContext(), "Country selected:"+position , Toast.LENGTH_SHORT).show();
//            state_spinner.setSelection(1);
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }
}
