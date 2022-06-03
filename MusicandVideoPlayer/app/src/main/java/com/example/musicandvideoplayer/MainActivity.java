package com.example.musicandvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button See_VL;
    private ListView MListview;
    static MediaPlayer mediaPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        See_VL =(Button) findViewById(R.id.See_VL);
        MListview = (ListView) findViewById(R.id.MListview);
///////////////////////////////////////////////////////////////////// Goning to video list
        See_VL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Music_intent = new Intent(MainActivity.this, Video_LIST.class);
                Music_intent.putExtra("From_ML","ML");
                startActivity(Music_intent);
            }
        });
/////////////////////////////////////////////////////////////////////////////// getting the video list intent
        Intent getVL = getIntent();
        Bundle bundleM = getVL.getExtras();
        getVL.getStringExtra("From_VL");
////////////////////////////////////////////////////////////////////////////// getting the mucis player intent
        Intent getMP = getIntent();
        Bundle bundleMP = getMP.getExtras();
        getMP.getStringExtra("From_MP");
///////////////////////////////////////////////////////////////////////////// getting the video player intent
        Intent getVP = getIntent();
        Bundle bundleVP = getVP.getExtras();
        getVP.getStringExtra("From_VPly");
//////////////////////////////////////////////////////////////////////////////////////
        Dexter.withContext(this)
              .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
              .withListener(new PermissionListener() {
                  @Override
                  public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                      ArrayList<File> SarrayList = FetchSongs(Environment.getExternalStorageDirectory());
                      String[] songsFiles = new String[SarrayList.size()];
                        for(int i=0; i<SarrayList.size(); i++){
                            songsFiles[i] = SarrayList.get(i).getName().replace(".mp3","");
                        }

                      ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_activated_1 ,songsFiles);
                      MListview.setAdapter(arrayAdapter);
                      MListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                          @Override
                          public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {

                              Intent sendMusic = new Intent(MainActivity.this,Music_Player.class);
                              String currentposition = MListview.getItemAtPosition(p).toString();
                              sendMusic.putExtra("songs",SarrayList);
                              sendMusic.putExtra("currentposition",currentposition);
                              sendMusic.putExtra("position",p);
                              startActivity(sendMusic);

                              try {
                                  if(Music_Player.mediaPlayer.isPlaying())
                                  {
                                      Music_Player.mediaPlayer.pause();
                                      Music_Player.mediaPlayer.stop();
                                      Music_Player.mediaPlayer.release();

                                  }
                              }catch (Exception e){
                                  Toast.makeText(MainActivity.this, "Starting music", Toast.LENGTH_SHORT).show();
                              }

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

   public ArrayList<File> FetchSongs(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] songs = file.listFiles();
        if(songs != null){
            for(File mysongs: songs){
                if(mysongs.isDirectory() && !mysongs.isHidden()){
                    arrayList.addAll(FetchSongs(mysongs));
                }
                else {
                    if(mysongs.getName().endsWith(".mp3") && !mysongs.getName().startsWith(".")){
                        arrayList.add(mysongs);
                    }
                    if(mysongs.getName().endsWith(".wav") && !mysongs.getName().startsWith(".")){
                        arrayList.add(mysongs);
                    }
                }
            }
        }
        return  arrayList;
   }
}