package com.podder.ullash.yourlink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class ProfilePhoto extends ActionBarActivity {
private ImageView ivImage;
    int REQUEST_CAMERA =0;
    int SELECT_FILE = 1;
    private StringRequest myReq;
    private RequestQueue queue;
    String tempPath;//image path
    String urlPostFix = "updateProfilePicture";
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);
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
        ImageButton backHome = (ImageButton)this.findViewById(R.id.backHome);
        backHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyHome.class);
                startActivity(intent);
            }
        });
//Add photo and stuff
        ivImage = (ImageView)this.findViewById(R.id.photoView);


        Button selectPhoto = (Button)this.findViewById(R.id.selectImage);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final CharSequence[] items = { "Take Photo", "Choose from Library",
                        "Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePhoto.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment
                                    .getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else if (items[item].equals("Choose from Library")) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    SELECT_FILE);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });
        Button rotatePhoto = (Button)this.findViewById(R.id.rotateImage);
        rotatePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        Button addName = (Button)this.findViewById(R.id.add_name);
        addName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        ImageButton addPhoto = (ImageButton)this.findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                Ion.with(getApplicationContext())
//                        .load("http://yourlink.paraexist.info/latest/v1/updateProfilePicture")
////                        .uploadProgressBar(uploadProgressBar)
//                        .setMultipartParameter("user_id", settings.getString("user_id", ""))
//                        .setMultipartFile("profile", new File(tempPath))
//                        .asJsonObject();

                // web service code
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
                        return params;
                    }

                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", settings.getString("api_key",""));

                        return params;
                    }

                };

            }
        });
    }
    private Response.Listener<String> createMyReqSuccessListener( final String requestType) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//              Commit the shared preference edits!
                Long tsLong = System.currentTimeMillis()/100;//time strap
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("time_strap", tsLong.toString());
                editor.commit();

                JSONArray response_Array = null;
                JSONObject responseObject;

//                try {
//                    responseObject = new JSONObject(response);            //this works for
//                    if(!responseObject.getBoolean("error")){
//                        response_Array= responseObject.getJSONArray("messages");
//                        final String[] image_url = new String[response_Array.length()];
//                        for (int i = 0; i < response_Array.length(); i++){
//                            JSONObject history_row = response_Array.getJSONObject(i);
//                            image_url[i] = history_row.getString("image_url");
//                            message_title[i] = history_row.getString("message_title")
//                        }
//
//
//                    }
//                    else {
//                        responseObject = new JSONObject(response);            //this works
////                        Toast.makeText(getApplicationContext(), "Response recieved", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    //                        response_Array= responseObject.getJSONArray("categories");//category1.getString("category_name")
//                    e.printStackTrace();
//                }

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
    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                    // bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    ivImage.setImageBitmap(bm);

                    tempPath = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(tempPath, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                tempPath = getPath(selectedImageUri, ProfilePhoto.this);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                ivImage.setImageBitmap(bm);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_photo, menu);
        return true;
    }

    public void profile_photo_addPhotoClicked(View v){
        Intent intent = new Intent(this, InviteFnF.class);
        startActivity(intent);
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
