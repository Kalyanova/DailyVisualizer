package by.paranoidandroid.dailyvisualizer.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import by.paranoidandroid.dailyvisualizer.DailyVisualizerApp;
import by.paranoidandroid.dailyvisualizer.model.database.AppDatabase;
import by.paranoidandroid.dailyvisualizer.model.database.Day;

public class DayViewModel extends AndroidViewModel {
    @IntDef({Task.INSERT, Task.DELETE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Task {
        int INSERT = 0;
        int DELETE = 1;
    }
    private AppDatabase appDatabase;
    private LiveData<Day> searchByLiveData;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>();

    public DayViewModel(Application application) {
        super(application);
        appDatabase = DailyVisualizerApp.getDatabase();
        searchByLiveData = Transformations.switchMap(filterLiveData,
                v -> appDatabase.dayDao().getDay(v));
    }

    public LiveData<Day> getSearchBy() { return searchByLiveData; }
    public void setFilter(String filter) { filterLiveData.setValue(filter); }

    public void insertDay(Day day) {
        new DayAsyncTask(appDatabase, Task.INSERT).execute(day);
    }

    public void deleteDay(Day day) {
        new DayAsyncTask(appDatabase, Task.DELETE).execute(day);
    }

    private static class DayAsyncTask extends AsyncTask<Day, Void, Void> {
        private AppDatabase db;
        int task;

        public DayAsyncTask(AppDatabase appDatabase, @Task int tsk) {
            db = appDatabase;
            task = tsk;
        }

        @Override
        protected Void doInBackground(final Day... days) {
            switch (task) {
                case Task.INSERT:
                    db.dayDao().insert(days[0]);
                    return null;
                case Task.DELETE:
                    db.dayDao().delete(days[0]);
                    return null;
            }
            return null;
        }
    }
}
