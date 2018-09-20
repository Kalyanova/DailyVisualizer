package by.paranoidandroid.dailyvisualizer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import by.paranoidandroid.dailyvisualizer.DailyVisualizerApp;
import by.paranoidandroid.dailyvisualizer.model.database.AppDatabase;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
  private AppDatabase appDatabase;
  private LiveData<List<Day>> searchData;
  private MutableLiveData<String> filterLiveData = new MutableLiveData<>();

  public LiveData<List<Day>> getSearchData() {
    return searchData;
  }

  public SearchViewModel(@NonNull Application application) {
    super(application);
    appDatabase = DailyVisualizerApp.getDatabase();
    searchData = Transformations.switchMap(filterLiveData,
        v -> appDatabase.dayDao().getDays("%" + v + "%"));
  }

  public void setSearchQuery(String query){
    filterLiveData.setValue(query);
  }
}
