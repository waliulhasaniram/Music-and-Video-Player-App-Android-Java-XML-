package com.example.musicandvideoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayer extends AppCompatActivity {

  private Button  musicList, videoList;
  private SurfaceView videoplayer;
  private SeekBar seekBar;
  private ImageView previous;
  private ImageView pause;
  private ImageView next;
  private TextView VideoName;

  ArrayList<File> vidarry;
  String currentposition;
  int position;
  MediaPlayer mediaPlayer;
  Thread UpdateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        musicList = (Button) findViewById(R.id.musicList);
        videoList = (Button) findViewById(R.id.videoList);
        videoplayer = (SurfaceView) findViewById(R.id.videoplayer);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        previous = (ImageView) findViewById(R.id.previous);
        pause = (ImageView) findViewById(R.id.pause);
        next = (ImageView) findViewById(R.id.next);
        VideoName = (TextView) findViewById(R.id.VideoName);
////////////////////////////////////////////////////////////////////////// going to music list
        musicList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mediaPlayer.pause();
               mediaPlayer.release();

                Intent V_int = new Intent(VideoPlayer.this, MainActivity.class);
                V_int.putExtra("From_VPly","VPly");
                startActivity(V_int);
            }
        });
 /////////////////////////////////////////////////////////////////////////// going to video list
       videoList.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             mediaPlayer.pause();
             mediaPlayer.release();

               Intent V_int2 = new Intent(VideoPlayer.this, Video_LIST.class);
               V_int2.putExtra("From_VPly2","VPly2");
               startActivity(V_int2);
           }
       });
 //////////////////////////////////////////////////////////////////////////////////////
       Intent intent = getIntent();
       Bundle bundle = intent.getExtras();

        vidarry = (ArrayList) bundle.getParcelableArrayList("videos");
        currentposition = intent.getStringExtra("currentposition");
        VideoName.setText(currentposition);

        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(vidarry.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
////////////////////////////////////////////////////////////////////////////////////seekBar update
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        UpdateSeekBar = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        UpdateSeekBar.start();
 ///////////////////////////////////////////////////////////////////////////////////////
          if(mediaPlayer.isPlaying()){
              videoplayer.setKeepScreenOn(true);
          }

        SurfaceHolder surfaceHolder = videoplayer.getHolder();
          surfaceHolder.addCallback(new SurfaceHolder.Callback() {
              @Override
              public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                  mediaPlayer.setDisplay(surfaceHolder);
                  mediaPlayer.start();
              }

              @Override
              public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

              }

              @Override
              public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

              }
          });
///////////////////////////////////////////////////////////////////////////////////////// Buttons
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    pause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                }
                else{
                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.ic_baseline_pause_presentation_24);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position !=  vidarry.size()-1){
                    position = position +1;
                }
                else {
                    position = 0;
                }

                Uri uri = Uri.parse(vidarry.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());

                currentposition = vidarry.get(position).getName().toString();
                VideoName.setText(currentposition);

                pause.setImageResource(R.drawable.ic_baseline_pause_presentation_24);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();

                if(position!=0){
                    position = position - 1;
                }
                else {
                    position = vidarry.size() -1;
                }

                Uri uri = Uri.parse(vidarry.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());

                currentposition = vidarry.get(position).getName().toString();
                VideoName.setText(currentposition);

                pause.setImageResource(R.drawable.ic_baseline_pause_presentation_24);
            }
        });
//////////////////////////////////////////////////////////////////////////////

    }
}