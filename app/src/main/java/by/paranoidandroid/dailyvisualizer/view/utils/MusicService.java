package by.paranoidandroid.dailyvisualizer.view.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import java.io.IOException;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.view.MainActivity;
import static by.paranoidandroid.dailyvisualizer.DailyVisualizerApp.CHANNEL_ID;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.SONG_DATE_NOTIFICATION_ID;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.SONG_MONTH_ID;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {
    MediaPlayer musicPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String dateToShow = intent.getStringExtra(SONG_DATE_NOTIFICATION_ID);
        Integer monthNumber = intent.getIntExtra(SONG_MONTH_ID, 0);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("DailyVisualizer")
                .setContentText(dateToShow)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentIntent(pendingIntent)
                .build();

        Uri songPath;
        if (monthNumber > 1 && monthNumber < 5)
            songPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.spring_mp3);
        else if (monthNumber > 4 && monthNumber < 8)
            songPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.summer_mp3);
        else if (monthNumber > 7 && monthNumber < 11)
            songPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.autumn_mp3);
        else
            songPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.winter_mp3);

        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(getApplicationContext(), songPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setOnPreparedListener(this);
        musicPlayer.prepareAsync();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
    }
}