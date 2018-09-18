package by.paranoidandroid.dailyvisualizer.model.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Day.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

  private static final String DATABASE_NAME = "DayDB.db";
  private static volatile AppDatabase instance;

  public static AppDatabase getInstance(Context context) {
    if(instance == null){
      synchronized (AppDatabase.class){
        instance = Room.databaseBuilder(context,
            AppDatabase.class,
            DATABASE_NAME)
            .build();
      }
    }
    return instance;
  }

  public abstract DayDao dayDao();
}
