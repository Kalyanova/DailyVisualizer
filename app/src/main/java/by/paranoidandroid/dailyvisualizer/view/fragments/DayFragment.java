package by.paranoidandroid.dailyvisualizer.view.fragments;

import static android.app.Activity.RESULT_OK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_WEEK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_YEAR;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.DATE_FORMAT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_CODE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_TAG_5;

import android.content.Context;
import android.content.Intent;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import by.paranoidandroid.dailyvisualizer.view.utils.LocationMapManager;
import by.paranoidandroid.dailyvisualizer.viewmodel.DayViewModel;
import java.util.Locale;

public class DayFragment extends DayParentFragment implements DialogDeleteDayFragment.OnDismissDialogListener {
    OnDayEditModeListener onDayEditModeListener;
    TextView tvDescription;
    ImageView ivDay;
    DayViewModel model;
    LiveData<Day> dayLiveData;
    ImageView btShowLocation;
    Day selectedDay;
    DialogDeleteDayFragment dialogFragment;
    int stateDialogButton;
    boolean dayIsAdded;

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
        dialogFragment = new DialogDeleteDayFragment();
        dialogFragment.setListenerDissmis(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        Bundle bundle = (savedInstanceState == null)
                ? getArguments()
                : savedInstanceState;
        if(!dayIsAdded){
            year = bundle.getInt(ARGS_YEAR);
            month = bundle.getInt(ARGS_MONTH);
            dayOfMonth = bundle.getInt(ARGS_DAY_OF_MONTH);
            dayOfWeek = bundle.getInt(ARGS_DAY_OF_WEEK);
        }
        tvTitle = view.findViewById(R.id.tv_preview_day);
        tvTitle.setText(getDayTitle(year, month, dayOfMonth));
        tvDayOfTheWeek = view.findViewById(R.id.tv_day_of_the_week);
        tvDayOfTheWeek.setText(getDayOfWeekName(dayOfWeek));

        tvDescription = view.findViewById(R.id.tv_desctription);
        ivDay = view.findViewById(R.id.iv_day_picture);
        btShowLocation = view.findViewById(R.id.btn_show_location);

        model = ViewModelProviders.of(getActivity()).get(DayViewModel.class);
        String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);
        model.setFilter(date);
        dayLiveData = model.getSearchBy();
        dayLiveData.observe(this, day -> {
            // Update the UI.
            // TODO: change it, etrieve other stuff from database
            if (day != null) {
                selectedDay = day;                tvDescription.setText(day.getDate() + "\n"
                        + day.getTitle() + "\n"
                        + day.getDescription());
                if(day.getImage() != null){
                    ivDay.setImageBitmap(BitmapFactory.decodeByteArray(day.getImage(), 0, day.getImage().length));
                } else {
                    ivDay.setImageDrawable(null);
                }
                if(day.getLatitude() != null){
                    btShowLocation.setVisibility(View.VISIBLE);
                    btShowLocation.setOnClickListener(v->{
                      LocationMapManager.showLocation(getActivity(), day.getLatitude(), day.getLongitude());
                    });
                } else {
                   btShowLocation.setVisibility(View.GONE);
                }
            } else {
                tvDescription.setText(getString(R.string.label_empty_day));
                ivDay.setImageDrawable(null);
                btShowLocation.setVisibility(View.GONE);
            }
        });
        hideFABs();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_day, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == FRAGMENT_CODE){
                dayIsAdded = true;
                Log.d("UPDATEEE", "UPDATE");
                year = data.getIntExtra(ARGS_YEAR,0);
                month = data.getIntExtra(ARGS_MONTH,0);
                dayOfMonth = data.getIntExtra(ARGS_DAY_OF_MONTH,0);
                dayOfWeek = data.getIntExtra(ARGS_DAY_OF_WEEK,0);
            }
        }
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
                editModeFragment.setTargetFragment(this,  FRAGMENT_CODE);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, editModeFragment, FRAGMENT_TAG_5)
                        .addToBackStack(FRAGMENT_TAG_5)
                        .commit();
                return true;
            case R.id.action_delete:
                // TODO: delete day from database
                if (selectedDay != null) {
                    dialogFragment.show(getFragmentManager(), "a");
                    onDismissDialog();
                } else {
                    Toast.makeText(getActivity(), "Nothing to delete!", Toast.LENGTH_SHORT).show();
                }

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
        void onDayEditModeClosed();
    }

    private void hideFABs() {
        findFABs();
        fabAdd.setVisibility(View.GONE);
        fabAddImage.setVisibility(View.GONE);
        fabAddSnapshot.setVisibility(View.GONE);
        fabAddMusic.setVisibility(View.GONE);
        fabAddLocation.setVisibility(View.GONE);
    }

    protected void findFABs() {
        fabAdd = getActivity().findViewById(R.id.fab_add);
        fabAddImage = getActivity().findViewById(R.id.fab_add_image);
        fabAddSnapshot = getActivity().findViewById(R.id.fab_add_snapshot);
        fabAddMusic = getActivity().findViewById(R.id.fab_add_music);
        fabAddLocation = getActivity().findViewById(R.id.fab_add_location);
    }

    public void onDialogButtonClick(int state) {
        if (state == 1) {
            stateDialogButton = 1;
        }
    }

    @Override
    public void onDismissDialog() {
        switch (stateDialogButton) {

            case 1:
                model.deleteDay(selectedDay);
                selectedDay = null;
                Toast.makeText(getActivity(), "Note deleted!", Toast.LENGTH_LONG).show();
                stateDialogButton = 0;
                break;
            case 2:
                break;
            default:
                break;
        }
    }
}