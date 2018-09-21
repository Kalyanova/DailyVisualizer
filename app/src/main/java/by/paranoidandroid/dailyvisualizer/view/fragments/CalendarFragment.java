package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;

import androidx.fragment.app.Fragment;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.utils.CalendarDate;

import static java.util.Calendar.DAY_OF_WEEK;

public class CalendarFragment extends Fragment {
    private CalendarDate selectedDate;
    private CalendarListener calendarListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            calendarListener = (CalendarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CalendarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        Calendar calendar = Calendar.getInstance();
        CalendarView calendarView = view.findViewById(R.id.calendar_view);
        // We open DayFragment only after the second click on selected date.
        calendarView.setOnDateChangeListener((cv, year, month, dayOfMonth) -> {
                    CalendarDate newSelectedDate = new CalendarDate(year, month, dayOfMonth);
                    if (selectedDate != null && selectedDate.equals(newSelectedDate)) {
                        calendar.set(year, month, dayOfMonth);
                        int dayOfWeek = calendar.get(DAY_OF_WEEK);
                        calendarListener.onDaySelected(selectedDate, dayOfWeek);
                    }
                    selectedDate = newSelectedDate;
                }
        );

        if (selectedDate != null) {
            calendar.set(selectedDate.year,
                    selectedDate.month,
                    selectedDate.dayOfMonth);
            calendarView.setDate(calendar.getTimeInMillis());
        }

        return view;
    }

    public interface CalendarListener {
        void onDaySelected(CalendarDate selectedDate, int dayOfWeek);
    }
}