package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import androidx.lifecycle.ViewModelProviders;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import by.paranoidandroid.dailyvisualizer.viewmodel.DayViewModel;

import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_WEEK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_YEAR;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.DATE_FORMAT;

public class DayEditModeFragment extends DayParentFragment {
    private boolean isFABOpened;
    private FloatingActionButton fabAdd, fabAddImage, fabAddSnapshot, fabAddMusic, fabAddLocation;
    private DayViewModel viewModel;

    public static DayEditModeFragment newInstance(int year, int month, int dayOfMonth, int dayOfWeek) {
        DayEditModeFragment fragment = new DayEditModeFragment();
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
        setHasOptionsMenu(false);
        viewModel = ViewModelProviders.of(getActivity()).get(DayViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_mode_day, container, false);

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

        EditText etTitle = view.findViewById(R.id.et_title);
        EditText etDescription = view.findViewById(R.id.et_description);

        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            // TODO: change it - add saving other stuff (image, location and etc.) to database
            String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);
            Day day = new Day(date,
                    etTitle.getText().toString(),
                    etDescription.getText().toString());
            viewModel.insertDay(day);
            // Here we try to close edit mode fragment like Activity with finish()
            getActivity().getSupportFragmentManager()
                    .popBackStack();
        });

        fabAdd = view.findViewById(R.id.fab_add);
        fabAddImage = view.findViewById(R.id.fab_add_image);
        fabAddSnapshot = view.findViewById(R.id.fab_add_snapshot);
        fabAddMusic = view.findViewById(R.id.fab_add_music);
        fabAddLocation = view.findViewById(R.id.fab_add_location);
        fabAdd.setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Add click", Toast.LENGTH_SHORT).show();
                    if (!isFABOpened) {
                        showFABMenu();
                    } else {
                        closeFABMenu();
                    }
                }
        );

        // TODO: implement fab clicks
        fabAddLocation.setOnClickListener(v -> {
        });

        return view;
    }

    private void showFABMenu() {
        isFABOpened = true;
        fabAddLocation.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        fabAddMusic.animate().translationY(-getResources().getDimension(R.dimen.standard_130));
        fabAddSnapshot.animate().translationY(-getResources().getDimension(R.dimen.standard_185));
        fabAddImage.animate().translationY(-getResources().getDimension(R.dimen.standard_240));
    }

    private void closeFABMenu() {
        isFABOpened = false;
        fabAdd.animate().translationY(0);
        fabAddImage.animate().translationY(0);
        fabAddSnapshot.animate().translationY(0);
        fabAddMusic.animate().translationY(0);
        fabAddLocation.animate().translationY(0);
    }
}
