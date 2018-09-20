package by.paranoidandroid.dailyvisualizer.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import by.paranoidandroid.dailyvisualizer.DailyVisualizerApp;
import by.paranoidandroid.dailyvisualizer.model.database.AppDatabase;
import by.paranoidandroid.dailyvisualizer.model.database.Day;

public class DayViewModel extends AndroidViewModel {
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
        new InsertAsyncTask(appDatabase).execute(day);
    }

    private static class InsertAsyncTask extends AsyncTask<Day, Void, Void> {

        private AppDatabase db;

        InsertAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Day... days) {
            db.dayDao().insert(days[0]);
            return null;
        }
    }
}
