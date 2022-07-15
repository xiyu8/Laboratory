package com.jason.fileoperate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {


        ReadAndWriteAssets.copy(this,"aaa.ofyr",getExternalFilesDir("").getAbsolutePath(),"aaa.ofyr");

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        try {
            getAssets().open("aaa.ofyr");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path="a";
        File file=null;
        file=new File(getExternalFilesDir("").getAbsolutePath()+"/"+"aaa.ofyr");
        intent.setDataAndType(Uri.fromFile(file), getMIMEType(""));
//                startActivity(intent);
        Intent.createChooser(intent, "请选择对应的软件打开该附件！");

    }

    public static String getMIMEType(String filePath) {

        return "*/*";
    }


}