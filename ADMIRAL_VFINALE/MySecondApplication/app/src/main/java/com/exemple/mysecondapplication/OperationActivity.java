package com.exemple.mysecondapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class OperationActivity extends AppCompatActivity {
    Button btnTakePhoto;
    Button btnList;
    public static final int REQUEST_PREM_WRITE_STORAGE = 102;
    private final int CAPTURE_PHOTO = 104;
    ImageView image;
    private final int VIDEO_REQUEST_CODE = 100;
    private Uri videoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        initializeUI();

        Intent i1 = getIntent();
        Bundle b1 = i1.getBundleExtra("timeb");
        Intent i2 = getIntent();
        Bundle b2 = i1.getBundleExtra("time");
        final String time = b1.getString("string");
        final String time1 = b2.getString("string");

        Toast.makeText(OperationActivity.this, time + " " + time1, Toast.LENGTH_SHORT).show();

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(OperationActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(OperationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PREM_WRITE_STORAGE);

                }else{
                    TakePhoto();
                }
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OperationActivity.this, ListeActivity.class);
                Bundle b = new Bundle();
                b.putString("string", time);

                Bundle p = new Bundle();
                p.putString("string", time1);

                intent.putExtra("timeb", b);
                intent.putExtra("time", p);
                startActivity(intent);
            }
        });
    }

    public void initializeUI(){
        btnTakePhoto = (Button)findViewById(R.id.btnTakePhoto);
        image = (ImageView)findViewById(R.id.image);
        btnList = (Button)findViewById(R.id.btnList);

    }

    public void TakePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAPTURE_PHOTO);

    }

    public void captureVideo(View view) {

        Intent video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(video_intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(video_intent, VIDEO_REQUEST_CODE);
        }
        File video_file = getFilePath();
        Uri video_uri = Uri.fromFile(video_file);
        video_intent.putExtra(MediaStore.EXTRA_OUTPUT, video_file);
        video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent){
        super.onActivityResult(requestCode, resultCode, returnIntent);

        if(requestCode == VIDEO_REQUEST_CODE && resultCode == RESULT_OK){
            videoUri = returnIntent.getData();
        }

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CAPTURE_PHOTO:
                    Bitmap capturedCoolerBitmap = (Bitmap) returnIntent.getExtras().get("data");
                    image.setImageBitmap(capturedCoolerBitmap);
                    saveImage(capturedCoolerBitmap);

                    break;

                default:
                    break;
            }
        }
    }

    private void saveImage(Bitmap finalBitmap){

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saveImage");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt();
        String imageName = "Image-" + n + ".jpg";
        File file = new File(myDir, imageName);
        if(file.exists()){
            file.delete();
        }
        try{
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            String resizeImagePath = file.getAbsolutePath();
            out.flush();
            out.close();

            Toast.makeText(OperationActivity.this, "Your Photo Save", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(OperationActivity.this, "Exception Throw", Toast.LENGTH_SHORT).show();
        }
    }

    public File getFilePath(){
        File folder = new File("sdcard/video_app");
        if(folder.exists()){
            folder.mkdir();
        }
        File video_file = new File(folder, "sample_video.mp4");

        return video_file;
    }
}
