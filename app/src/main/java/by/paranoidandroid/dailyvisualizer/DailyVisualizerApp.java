package by.paranoidandroid.dailyvisualizer;

import android.app.Application;
import by.paranoidandroid.dailyvisualizer.model.database.AppDatabase;

public class DailyVisualizerApp extends Application {

  private static AppDatabase database;

  public static AppDatabase getDatabase() {
    return database;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    database = AppDatabase.getInstance(this);
  }


}
