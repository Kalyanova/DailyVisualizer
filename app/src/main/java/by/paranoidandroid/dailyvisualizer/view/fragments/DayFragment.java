package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import by.paranoidandroid.dailyvisualizer.viewmodel.DayViewModel;

import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_WEEK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_YEAR;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.DATE_FORMAT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_TAG_5;

public class DayFragment extends DayParentFragment {
    OnDayEditModeListener onDayEditModeListener;
    TextView tvDescription;
    ImageView ivDay;
    DayViewModel model;
    LiveData<Day> dayLiveData;

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
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onDayEditModeListener = (OnDayEditModeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDayEditModeListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
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

        tvDescription = view.findViewById(R.id.tv_desctription);
        ivDay = view.findViewById(R.id.iv_day_picture);

        model = ViewModelProviders.of(getActivity()).get(DayViewModel.class);
        String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);
        model.setFilter(date);
        dayLiveData = model.getSearchBy();
        dayLiveData.observe(this, day -> {
            // Update the UI.
            // TODO: change it, etrieve other stuff from database
            if (day != null) {
                tvDescription.setText(day.getDate() + "\n"
                        + day.getTitle() + "\n"
                        + day.getDescription());
                if(day.getImage() != null){
                    ivDay.setImageBitmap(BitmapFactory.decodeByteArray(day.getImage(), 0, day.getImage().length));
                }
            } else {
                tvDescription.setText(getString(R.string.label_empty_day));
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_day, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                item.setEnabled(false); // TODO: disable delete button too
                onDayEditModeListener.onDayEditModeOpened();
                Toast.makeText(getActivity(), "Edit click", Toast.LENGTH_SHORT).show();
                DayEditModeFragment editModeFragment = DayEditModeFragment.newInstance(year,
                        month,
                        dayOfMonth,
                        dayOfWeek);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, editModeFragment, FRAGMENT_TAG_5)
                        .addToBackStack(FRAGMENT_TAG_5)
                        .commit();
                return true;
            case R.id.action_delete:
                Toast.makeText(getActivity(), "Delete click", Toast.LENGTH_SHORT).show();
                // TODO: delete day from database
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);
        model.setFilter(date);
        dayLiveData = model.getSearchBy();
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

    public interface OnDayEditModeListener {
        void onDayEditModeOpened();
    }
}