package by.paranoidandroid.dailyvisualizer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import by.paranoidandroid.dailyvisualizer.model.database.AppDatabase;

public class DailyVisualizerApp extends Application {

    private static AppDatabase database;
    public static final String CHANNEL_ID = "MUSIC_CHANNEL_ID";

    public static AppDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        database = AppDatabase.getInstance(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            serviceChannel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}