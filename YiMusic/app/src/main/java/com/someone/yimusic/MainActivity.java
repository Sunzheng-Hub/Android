package com.someone.yimusic;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private Button mPlayBtn;
    private Button mPauseBtn;
    private Button mStopBtn;
    private SeekBar mSeekBar;

    private MediaPlayer mMediaPlayer;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayBtn = (Button) findViewById(R.id.id_play);
        mPauseBtn = (Button) findViewById(R.id.id_pause);
        mStopBtn = (Button) findViewById(R.id.id_stop);
        mSeekBar = (SeekBar) findViewById(R.id.id_seekbar);

        mMediaPlayer = MediaPlayer.create(this,R.raw.actor);
        mMediaPlayer.setLooping(true);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (mSeekBar.isPressed()){
                    // SeekBar被拖动，不进行设置
                } else {
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                }

            }
        };
        final DelayThread delayThread = new DelayThread(100);


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                mMediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Anonymous","progress is: " + seekBar.getProgress());
                mMediaPlayer.seekTo(seekBar.getProgress());
                mMediaPlayer.start();
                seekBar.setProgress(seekBar.getProgress());
                // TODO 退出应用时歌曲没有停止，再次进入应用时seekbar不能拖动
            }
        });

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMediaPlayer.isPlaying()){
                    mMediaPlayer.start();
                    if (delayThread.getState() == Thread.State.NEW){
                        delayThread.start();
                    } else if (delayThread.isInterrupted()){
                        delayThread.run();
                    }
                    mSeekBar.setMax(mMediaPlayer.getDuration());
                    Log.d("Anonymous","getDuration is: " + mMediaPlayer.getDuration());
                }
            }
        });

        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                }
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMediaPlayer.stop();
//                mMediaPlayer.prepareAsync();
                mSeekBar.setProgress(0);
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
//                try {
//                    mMediaPlayer.stop();
////                    mMediaPlayer.prepare();
//                    mMediaPlayer.prepareAsync();
//                    mSeekBar.setProgress(0);
//                    delayThread.keepRunning = false;
//                    delayThread.interrupt();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });





    }

    class DelayThread extends Thread{

        int delayTime;
        volatile boolean keepRunning = true;

        public DelayThread(int time) {
            delayTime = time;
        }

        @Override
        public void run() {
            while (keepRunning){
                try {
                    sleep(delayTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.this.finish();
    }


}
