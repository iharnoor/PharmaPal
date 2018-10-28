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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    //        private String url = "127.0.0.1:80/getNoteText/image_Test.JPG";
    private String url2 = "http://00455e37.ngrok.io/image";
    private String url = "https://00455e37.ngrok.io/api/postData/" + Utils.LANGUAGE;
    //    private String img_path = "/storage/emulated/0/Pictures/image_Test.JPG";
    public static int count = 0;
    int TAKE_PHOTO_CODE = 0;
    private String img_path;
    String outputFromTheServer = "";

    public static TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
//                    Locale locSpanish = new Locale("spa", "MEX");
//                    t1.setLanguage(locSpanish);
//                    t1.setLanguage(Locale.CHINA);
//                    Hindi
                    t1.setLanguage(Locale.forLanguageTag("hin"));
                }
            }
        });

        Button _btnTranslate = findViewById(R.id.btnTranslate);
        Button _btnPost = findViewById(R.id.btnPost);
        Button _btnCapture = findViewById(R.id.btnCapture);

        _btnTranslate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Translate.class);
                        startActivity(intent);
                    }
                }
        );

        if (checkPermissions()) {
            //  permissions  granted.
        }
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        if (!newdir.exists()) {
            newdir.mkdir();
        }

        _btnCapture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        dispatchTakePictureIntent();
                        // and likewise.
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
                    }
                }
        );
        _btnPost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Method 1
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
                        outputFromTheServer = push3(url, jsonObject.toString());

                        while (true) {
                            if (Utils.isOutputReady) {
                                break;
                            }
                        }

                        Intent intent = new Intent(getApplicationContext(), AudioActivity.class);
                        intent.putExtra("output", outputFromTheServer);
                        startActivity(intent);
//                        }
                    }
                }
        );

        Toast.makeText(this, "output: " + outputFromTheServer, Toast.LENGTH_SHORT).show();
    }

    public String push3(final String url, final String json) {
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


//
//    public String pushToServer(String imagePath) throws IOException {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);
//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
//        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"pic\"; filename=" + imagePath + "\r\nContent-Type: image/jpeg\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
//        Request request = new Request.Builder()
//                .url(url2)
//                .post(body)
//                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("Postman-Token", "cfafb477-3e68-477d-8de2-e614f8e7f727")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        Toast.makeText(this, "WORKS:  " + response.body().string(), Toast.LENGTH_LONG).show();
//        return "";
//    }


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

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                "example",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

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
//        Uri contentUri = Uri.fromFile(f);
//        Uri contentUri = Uri.fromFile(f);
        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, f);

        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
