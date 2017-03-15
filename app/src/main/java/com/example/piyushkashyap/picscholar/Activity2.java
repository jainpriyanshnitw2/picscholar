package com.example.piyushkashyap.picscholar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.String;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;


public class Activity2 extends AppCompatActivity {

    Button b2,b3;
    ImageView v2;
    //private static final String DEBUGTAG = "JWP";
    Bitmap bitmap;
    static final int CAM_REQUEST=1;
    File file;
    File Root = Environment.getExternalStorageDirectory();
    File Dir = new File(Root.getAbsolutePath() + "/MyAppFile");
    String path=Dir.getPath();
    String coords;
    String respon="";
    boolean postflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        addTouchListener();
        b2 = (Button) findViewById(R.id.button5);
        b3 = (Button) findViewById(R.id.button6);
        v2 = (ImageView) findViewById(R.id.imageView);

        coords = "";

        String state = Environment.getExternalStorageState();
        if (!Dir.exists()) {
            Dir.mkdir();
        }
        file = new File(Dir, "MyMessage.txt");
        try {
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(("").getBytes());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getfile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);

            }
        });


        b3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                    Log.d("debug", "b3");
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(getfile()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String image = getStringImage(bitmap);
                    Log.d("bitmapStirng", "dddddd");
                    sendSampleRequest(image, coords);
                SystemClock.sleep(10000);
                    getdatafromserver(new File(Dir, "Output.txt"));
//                Intent intent = new Intent(Activity2.this, Activity3.class);
//                intent.putExtra("message", respon);
//                startActivity(intent);
            }
        });


    }




    private void addTouchListener(){
        ImageView image = (ImageView) findViewById(R.id.imageView);
        assert image != null;
        image.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String Message1, Message2;
                    try {
                        FileOutputStream fileoutputstream = new FileOutputStream(file, true);
                        Message1 = String.valueOf(event.getX());
                        Message2 = String.valueOf(event.getY());
                        coords = coords+Message1+','+Message2+',';

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    private File getfile()
    {
        File Dir = new File(Root.getAbsolutePath() + "/MyAppFile");
        if (!Dir.exists()) {
            Dir.mkdir();
        }
        File image_file = new File(Dir, "cam_image.jpg");
        return image_file;
    }
@Override

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
    String path = Dir+"/cam_image.jpg";
    v2.setImageDrawable(Drawable.createFromPath(path));
}

    public void getdatafromserver(final File filename)
    {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                "http://192.168.43.133:8000/main/data/", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag_event", "Register Response: " + response.toString());
                respon = response.toString();
                try {
                    FileOutputStream stream = new FileOutputStream(filename);
                    Log.d("cdx",respon);
                    stream.write(respon.getBytes());
                    stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Registration Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, "tag");
    }


    public void sendSampleRequest(final String image, final String coords)
    {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://192.168.43.133:8000/main/show_image/", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag_event", "Register Response: " + response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Registration Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("image", image);
                params.put("coords", coords);
                return params;
            }

        };

        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         AppController.getInstance().addToRequestQueue(strReq, "tag");
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




}
