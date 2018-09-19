package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.utils.Month;

import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_WEEK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_YEAR;

public class DayFragment extends Fragment {
    private TextView tvTitle, tvDayOfTheWeek;
    private int year, month, dayOfMonth, dayOfWeek;

    public static DayFragment newInstance(int year, int month, int dayOfMonth, int dayOfWeek) {
        DayFragment fragment = new DayFragment();
        Bundle bundle = new Bundle(4);
        bundle.putInt(ARGS_YEAR, year);
        bundle.putInt(ARGS_MONTH, month);
        bundle.putInt(ARGS_DAY_OF_MONTH, dayOfMonth);
        bundle.putInt(ARGS_DAY_OF_WEEK, dayOfWeek);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        Bundle bundle = (savedInstanceState == null)
                ? getArguments()
                : savedInstanceState;
        year = bundle.getInt(ARGS_YEAR);
        month = bundle.getInt(ARGS_MONTH);
        dayOfMonth = bundle.getInt(ARGS_DAY_OF_MONTH);
        dayOfWeek = bundle.getInt(ARGS_DAY_OF_WEEK);
        tvTitle = view.findViewById(R.id.tv_preview_day);
        tvTitle.setText(getDayTitle(year, month, dayOfMonth));
        tvDayOfTheWeek = view.findViewById(R.id.tv_day_of_the_week);
        tvDayOfTheWeek.setText(getDayOfWeekName(dayOfWeek));
        return view;
    }

    public void updateTitle(int year, int month, int dayOfMonth, int dayOfWeek) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        if (tvTitle != null) {
            tvTitle.setText(getDayTitle(year, month, dayOfMonth));
        }
        if (tvDayOfTheWeek != null) {
            tvDayOfTheWeek.setText(getDayOfWeekName(dayOfWeek));
        }
        // TODO: Retrieve other stuff from database.
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARGS_YEAR, year);
        outState.putInt(ARGS_MONTH, month);
        outState.putInt(ARGS_DAY_OF_MONTH, dayOfMonth);
        outState.putInt(ARGS_DAY_OF_WEEK, dayOfWeek);
        super.onSaveInstanceState(outState);
    }

    private String getDayOfWeekName(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }
    }

    private String getDayTitle(int year, int month, int dayOfMonth) {
        return Month.of(month + 1).name() + " " + dayOfMonth + ", " + year;
    }
}