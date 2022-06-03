package com.example.musicandvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class Video_LIST extends AppCompatActivity {

    private Button See_ML;
    private ListView Vlistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        See_ML = (Button) findViewById(R.id.See_ML);
        Vlistview = (ListView) findViewById(R.id.Vlistview);
///////////////////////////////////////////////////////// getting the Music list intent

        Intent getML = getIntent();
        Bundle bundleM = getML.getExtras();
        getML.getStringExtra("From_ML");
//############################################################/
                 /*Going to music list*/
        See_ML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Video_intent = new Intent(Video_LIST.this, MainActivity.class);
                Video_intent.putExtra("From_VL","VL");
                startActivity(Video_intent);
            }
        });
/////////////////////////////////////////////////////////////////////////// getting the Music player intent
        Intent getMPlayer = getIntent();
        Bundle bundleVlist = getMPlayer.getExtras();
        getMPlayer.getStringExtra("From_MP2");
///////////////////////////////////////////////////////////////////////// getting the video player intent
        Intent getVPlayer = getIntent();
        Bundle bundleVPlayer = getVPlayer.getExtras();
        getVPlayer.getStringExtra("From_VPly2");
/////////////////////////////////////////////////////////////////////////
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> SarrayList = FetchVideos(Environment.getExternalStorageDirectory());
                        String[] videoFiles = new String[SarrayList.size()];
                        for(int i=0; i<SarrayList.size(); i++){
                            videoFiles[i] = SarrayList.get(i).getName().replace(".mp3","");
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(Video_LIST.this, android.R.layout.simple_list_item_activated_1 ,videoFiles);
                        Vlistview.setAdapter(arrayAdapter);
                        Vlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                                Intent sendMusic = new Intent(Video_LIST.this,VideoPlayer.class);
                                String currentposition = Vlistview.getItemAtPosition(p).toString();
                                sendMusic.putExtra("videos",SarrayList);
                                sendMusic.putExtra("currentposition",currentposition);
                                sendMusic.putExtra("position",p);
                                startActivity(sendMusic);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }
    public static ArrayList<File> FetchVideos(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] videos = file.listFiles();
        if(videos != null){
            for(File myvideos: videos){
                if(myvideos.isDirectory() && !myvideos.isHidden()){
                    arrayList.addAll(FetchVideos(myvideos));
                }
                else {
                    if(myvideos.getName().endsWith(".mp4") && !myvideos.getName().startsWith(".")){
                        arrayList.add(myvideos);
                    }
                    if(myvideos.getName().endsWith(".mkv") && !myvideos.getName().startsWith(".")){
                        arrayList.add(myvideos);
                    }
                }
            }
        }
        return  arrayList;
    }
}