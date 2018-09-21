package by.paranoidandroid.dailyvisualizer.viewmodel;

import android.app.Application;
import by.paranoidandroid.dailyvisualizer.model.database.Day;

public class EditDayViewModel extends  DayViewModel {

  public EditDayViewModel(Application application) {
    super(application);
  }

  public void insertDay(Day day) {
    new DayAsyncTask(appDatabase, Task.INSERT).execute(day);
  }
}
