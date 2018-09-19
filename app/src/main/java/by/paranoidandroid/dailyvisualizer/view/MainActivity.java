package by.paranoidandroid.dailyvisualizer.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.utils.CalendarDate;
import by.paranoidandroid.dailyvisualizer.view.fragments.CalendarFragment;
import by.paranoidandroid.dailyvisualizer.view.fragments.DayFragment;
import by.paranoidandroid.dailyvisualizer.view.fragments.SearchFragment;
import by.paranoidandroid.dailyvisualizer.view.fragments.SettingsFragment;

import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_ACTIVE_FRAGMENT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_TAG_1;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_TAG_2;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_TAG_3;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_TAG_4;

public class MainActivity extends AppCompatActivity implements CalendarFragment.CalendarListener {
    private Fragment active;
    private FragmentManager fm;
    private Fragment searchFragment, calendarFragment, dayFragment, settingsFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            searchFragment = new SearchFragment();
            calendarFragment = new CalendarFragment();

            Calendar calendar = Calendar.getInstance();
            dayFragment = DayFragment.newInstance(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.DAY_OF_WEEK));

            settingsFragment = new SettingsFragment();

            fm.beginTransaction()
                    .add(R.id.main_container, searchFragment, FRAGMENT_TAG_1)
                    .hide(searchFragment)
                    .commit();
            fm.beginTransaction()
                    .add(R.id.main_container, calendarFragment, FRAGMENT_TAG_2)
                    .hide(calendarFragment)
                    .commit();
            fm.beginTransaction()
                    .add(R.id.main_container, dayFragment, FRAGMENT_TAG_3)
                    .commit();
            fm.beginTransaction()
                    .add(R.id.main_container, settingsFragment, FRAGMENT_TAG_4)
                    .hide(settingsFragment)
                    .commit();

            active = dayFragment;
            bottomNavigationView.setSelectedItemId(R.id.action_day);
        } else {
            final String ACTIVE_FRAGMENT_TAG = savedInstanceState.getString(ARGS_ACTIVE_FRAGMENT);
            if (!TextUtils.isEmpty(ACTIVE_FRAGMENT_TAG)) {
                searchFragment = fm.findFragmentByTag(FRAGMENT_TAG_1);
                calendarFragment = fm.findFragmentByTag(FRAGMENT_TAG_2);
                dayFragment = fm.findFragmentByTag(FRAGMENT_TAG_3);
                settingsFragment = fm.findFragmentByTag(FRAGMENT_TAG_4);
                active = fm.findFragmentByTag(ACTIVE_FRAGMENT_TAG);
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_search:
                            resetActive(searchFragment);
                            return true;
                        case R.id.action_calendar:
                            resetActive(calendarFragment);
                            return true;
                        case R.id.action_day:
                            resetActive(dayFragment);
                            return true;
                        case R.id.action_settings:
                            resetActive(settingsFragment);
                            return true;
                    }
                    return true;
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGS_ACTIVE_FRAGMENT, bottomNavigationView.getSelectedItemId());

        if (active instanceof SearchFragment) {
            outState.putString(ARGS_ACTIVE_FRAGMENT, FRAGMENT_TAG_1);
        } else if (active instanceof CalendarFragment) {
            outState.putString(ARGS_ACTIVE_FRAGMENT, FRAGMENT_TAG_2);
        } else if (active instanceof DayFragment) {
            outState.putString(ARGS_ACTIVE_FRAGMENT, FRAGMENT_TAG_3);
        } else if (active instanceof SettingsFragment) {
            outState.putString(ARGS_ACTIVE_FRAGMENT, FRAGMENT_TAG_4);
        }
    }

    @Override
    public void onDaySelected(CalendarDate selectedDate, int dayOfWeek) {
        dayFragment = fm.findFragmentByTag(FRAGMENT_TAG_3);
        if (dayFragment != null) {
            ((DayFragment) dayFragment).updateTitle(selectedDate.year,
                    selectedDate.month,
                    selectedDate.dayOfMonth,
                    dayOfWeek);
            fm.beginTransaction().hide(active).show(dayFragment).commit();
            active = dayFragment;
        }
        active = dayFragment;
        bottomNavigationView.setSelectedItemId(R.id.action_day);
    }

    private void resetActive(Fragment newActiveFragment) {
        fm.beginTransaction().hide(active).show(newActiveFragment).commit();
        active = newActiveFragment;
    }
}
