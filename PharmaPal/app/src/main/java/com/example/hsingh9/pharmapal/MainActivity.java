package com.example.hsingh9.pharmapal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    public static TextToSpeech t1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int count = 0;
    private int TAKE_PHOTO_CODE = 0;
    private String img_path;
    private String outputFromTheServer = "";
    private String mCurrentPhotoPath;
    private static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    // Change the URL here
    private String url = "http://2ce4e4c2.ngrok.io/api/postData/" + Utils.LANGUAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Activate Text to Speech
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Utils.LOCALE);
                }
            }
        });

        if (checkPermissions()) {
            //  permissions  granted.
        }

        // Create Folder in the device to store the image
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        if (!newdir.exists()) {
            newdir.mkdir();
        }


        // Store the image in the device after taking the image
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    count++;
                    String file = dir + count + ".jpg";
                    final File newfile = new File(file);
                    newfile.createNewFile();

                    //     Uri outputFileUri = Uri.fromFile(newfile);
                    Uri outputFileUri = FileProvider.getUriForFile(MainActivity.this, "com.example.hsingh9.pharmapal.provider", newfile);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

                } catch (IOException ignored) {
                }
            }
        }).start();

        // Store the image as a bitmap
        JSONObject jsonObject = new JSONObject();
        img_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/" + count + ".jpg";
        Bitmap bm = BitmapFactory.decodeFile(img_path);
        String base64Img = encode(bm);
        try {
            jsonObject.put("text", base64Img);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Carls json", jsonObject.toString());
        outputFromTheServer = pushToServer(url, jsonObject.toString());


        // Wait for camera to take the picture before going to the next activity
        while (true) {
            if (Utils.isOutputReady) {
                break;
            }
        }

        Intent intent = new Intent(getApplicationContext(), AudioActivity.class);
        intent.putExtra("output", outputFromTheServer);
        startActivity(intent);
        Toast.makeText(this, "output: " + outputFromTheServer, Toast.LENGTH_SHORT).show();
    }

    public String pushToServer(final String url, final String json) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final String[] jsonObjectResp = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("WORKING,", "WORKS");
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();

                    okhttp3.RequestBody body = RequestBody.create(JSON, json);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    okhttp3.Response response = client.newCall(request).execute();

                    final String networkResp = response.body().string();

                    Log.d("CARL's output correct", networkResp);
                    if (!networkResp.isEmpty()) {
                        jsonObjectResp[0] = networkResp;
                    }
                } catch (Exception ex) {
                    String err = String.format("{\"result\":\"false\",\"error\":\"%s\"}", ex.getMessage());
                    jsonObjectResp[0] = err;
                    Log.d("CARL ERROR", jsonObjectResp[0]);
                }
                Utils.isOutputReady = true;
                outputFromTheServer = jsonObjectResp[0];
            }
        });
        thread.start();
        return jsonObjectResp[0];
    }

    public String encode(final Bitmap image) {
        String encodedImage = "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();

        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            Log.d("CameraDemo", "Pic saved");
            galleryAddPic();
        }
    }

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
//                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
//                            .show();
                }
                // permissions list of don't granted permission
            }
            return;
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
