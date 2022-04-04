package com.recifit.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.recifit.application.common.ProfileData;
import com.recifit.application.databinding.ActivityWriteBinding;
import com.recifit.application.ui.recipe.RecipeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity {
    public EditText recipeContent;
    public EditText recipeTitle;
    public Button back_btn;
    public Button submit_btn;
    public ImageView camera_btn;
    public ImageView camera_image;
    public Uri selectedImageUri;


    private static final String TAG = "writeActivity";

    private ActivityWriteBinding binding;
    public RecipeFragment recipeFragment;


    @Override
    public void onResume() {
        super.onResume();
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        back_btn =  binding.backBtn;
        submit_btn=  binding.submit;
        recipeContent = binding.recipeContentEdit;
        recipeTitle = binding.recipeTitleEdit;

        camera_btn = binding.cameraBtn;
        camera_image = binding.recipeImg;


        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        Log.d(TAG, "권한 설정 완료");
//                        capture();
//                    } else {
//                        Log.d(TAG, "권한 설정 요청");
//                        ActivityCompat.requestPermissions(WriteActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    }
//                }
                TedPermission.create().setPermissionListener(permissionListener)
                        .setDeniedMessage("Permissions are Denined")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();

            }
        });



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String jsonString = new JSONObject()
                            .put("userId", ProfileData.getUserId())
                            .put("title", recipeTitle.getText().toString())
                            .put("content", recipeContent.getText().toString())
                            .toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myDBHelper myHelper = new myDBHelper(WriteActivity.this);
                SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("insert into groupTBL values('"+ recipeTitle.getText().toString() + "','" + recipeContent.getText().toString() + "','"+selectedImageUri+"');");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "inserted", Toast.LENGTH_SHORT).show();
                System.out.println("aaaaa"+recipeTitle.getText().toString()+ selectedImageUri);

                Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            Toast.makeText(WriteActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();

            capture();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(WriteActivity.this, "Permission Denined\n"+ deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    public static class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context){
        super(context, "groupDB", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table groupTBL (gTitle char(30), gContent char(400), gImageUri char(200));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists groupTBL;");
        onCreate(db);
    }
}

    public void insertDB(View view){
        myDBHelper myHelper = new myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("insert into groupTBL values('"+ recipeTitle.getText().toString() + "','" + recipeContent.getText().toString() + "',"+selectedImageUri+");");
        sqlDB.close();
        Toast.makeText(getApplicationContext(), "inserted", Toast.LENGTH_SHORT).show();
    }

    public void capture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            camera_image.setImageURI(selectedImageUri);
            System.out.println("aaaaaaabbb"+selectedImageUri);

        }

    }
}