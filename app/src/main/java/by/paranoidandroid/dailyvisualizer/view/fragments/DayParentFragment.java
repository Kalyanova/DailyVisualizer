package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.utils.Month;

public class DayParentFragment extends Fragment {
    protected FloatingActionButton fabAdd, fabAddImage, fabAddSnapshot, fabAddMusic, fabAddLocation;
    protected TextView tvTitle, tvDayOfTheWeek;
    protected int year, month, dayOfMonth, dayOfWeek;

    protected String getDayOfWeekName(int dayOfWeek) {
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

    protected String getDayTitle(int year, int month, int dayOfMonth) {
        return Month.of(month + 1).name() + " " + dayOfMonth + ", " + year;
    }

    protected void findFABs() {
        fabAdd = getActivity().findViewById(R.id.fab_add);
        fabAddImage = getActivity().findViewById(R.id.fab_add_image);
        fabAddSnapshot = getActivity().findViewById(R.id.fab_add_snapshot);
        fabAddMusic = getActivity().findViewById(R.id.fab_add_music);
        fabAddLocation = getActivity().findViewById(R.id.fab_add_location);
    }
}
