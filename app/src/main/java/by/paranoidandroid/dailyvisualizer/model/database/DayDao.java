package by.paranoidandroid.dailyvisualizer.model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DayDao {
  @Query("SELECT * FROM DAY")
  List<Day> getAll();

  @Query("SELECT * FROM DAY WHERE date LIKE :date")
  LiveData<Day> getDay(String date);

  @Query("SELECT * FROM DAY WHERE title LIKE :title ORDER BY date DESC")
  LiveData<List<Day>> getDays(String title);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(Day... days);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(Day day);

  @Delete
  void delete(Day day);
}
