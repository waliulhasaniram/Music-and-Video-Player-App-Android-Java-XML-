package com.example.musicandvideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Music_Player extends AppCompatActivity {
/*   @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }*/

    private Button musicList1, videoList1;
    private TextView songname;
    private SeekBar seekBar;
    private ImageView previous, pause, next;

    String currentposition;
    ArrayList<File> Song_arrLt;
    static MediaPlayer mediaPlayer;
    int position;
    Thread UpdateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        musicList1 = (Button) findViewById(R.id.musicList1);
        videoList1 = (Button) findViewById(R.id.videoList1);
        songname = (TextView) findViewById(R.id.songname);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        pause = (ImageView) findViewById(R.id.pause);
        previous = (ImageView) findViewById(R.id.previous);
        next = (ImageView) findViewById(R.id.next);

         Intent intent1 = getIntent();
         Bundle bundle1 = intent1.getExtras();

        Song_arrLt = (ArrayList) bundle1.getParcelableArrayList("songs");
        currentposition = intent1.getStringExtra("currentposition");
        songname.setText(currentposition);

        position = intent1.getIntExtra("position",0);
        Uri uri = Uri.parse(Song_arrLt.get(position).toString());
        mediaPlayer = MediaPlayer.create(Music_Player.this,uri);

        mediaPlayer.start();


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
                        sleep(950);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        UpdateSeekBar.start();

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
                if(position != Song_arrLt.size()-1){
                    position = position +1;
                }
                else {
                    position = 0;
                }

                Uri uri = Uri.parse(Song_arrLt.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());

                currentposition = Song_arrLt.get(position).getName().toString();
                songname.setText(currentposition);

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
                    position = Song_arrLt.size() -1;
                }

                Uri uri = Uri.parse(Song_arrLt.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());

                currentposition = Song_arrLt.get(position).getName().toString();
                songname.setText(currentposition);

                pause.setImageResource(R.drawable.ic_baseline_pause_presentation_24);
            }
        });
////////////////////////////////////////////////////////////////////////////// Going to music list
        musicList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* mediaPlayer.stop();
                mediaPlayer.release();*/

                Intent ML1 = new Intent(Music_Player.this, MainActivity.class);
                ML1.putExtra("From_MP","MP");
                startActivity(ML1);
            }
        });
////////////////////////////////////////////////////////////////////////////// Goning to video list
        videoList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                Intent ML2 = new Intent(Music_Player.this, Video_LIST.class);
                ML2.putExtra("From_MP2","MP2");
                startActivity(ML2);
            }
        });
/////////////////////////////////////////////////////////////////////////////////

    }

}