package by.paranoidandroid.dailyvisualizer.view.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_WEEK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_YEAR;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.DATE_FORMAT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_CODE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.FRAGMENT_TAG_5;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.SONG_DATE_NOTIFICATION_ID;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import by.paranoidandroid.dailyvisualizer.view.utils.LocationMapManager;
import by.paranoidandroid.dailyvisualizer.view.utils.MusicService;
import by.paranoidandroid.dailyvisualizer.viewmodel.DayViewModel;
import java.util.Locale;

public class DayFragment extends DayParentFragment implements DialogDeleteDayFragment.OnDismissDialogListener {
    OnDayEditModeListener onDayEditModeListener;
    TextView tvDate, tvTitle;
    TextView tvDescription;
    ImageView ivDay;
    DayViewModel model;
    LiveData<Day> dayLiveData;
    ImageView btShowLocation;
    Day selectedDay;
    Integer music = -1;
    ImageButton muteButton;
    Boolean isMuted = false;
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
        tvDate = view.findViewById(R.id.tv_preview_day);
        tvDate.setText(getDayTitle(year, month, dayOfMonth));
        muteButton = view.findViewById(R.id.mute_music_button);

        tvDayOfTheWeek = view.findViewById(R.id.tv_day_of_the_week);
        tvDayOfTheWeek.setText(getDayOfWeekName(dayOfWeek));

        tvTitle = view.findViewById(R.id.tv_note_title);
        tvDescription = view.findViewById(R.id.tv_desctription);
        ivDay = view.findViewById(R.id.iv_day_picture);
        btShowLocation = view.findViewById(R.id.btn_show_location);

        dialogFragment = new DialogDeleteDayFragment();
        dialogFragment.setListenerDissmis(this);

        model = ViewModelProviders.of(getActivity()).get(DayViewModel.class);
        String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);
        model.setFilter(date);
        dayLiveData = model.getSearchBy();
        dayLiveData.observe(this, day -> {
            // Update the UI.
            // TODO: change it, retrieve other stuff from database
            if (day != null) {
                selectedDay = day;

                if (!TextUtils.isEmpty(day.getTitle())) {
                    tvTitle.setText(day.getTitle());
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(day.getDescription())) {
                    tvDescription.setText(day.getDescription());
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    tvDescription.setVisibility(View.GONE);
                }

                if(day.getImage() != null){
                    ivDay.setImageBitmap(BitmapFactory.decodeByteArray(day.getImage(), 0, day.getImage().length));
                } else {
                    ivDay.setImageDrawable(null);
                }
                if (day.getLatitude() != null) {
                    btShowLocation.setVisibility(VISIBLE);

                    btShowLocation.setOnClickListener(v->{
                        LocationMapManager.showLocation(getActivity(), day.getLatitude(), day.getLongitude());
                    });
                } else {
                    btShowLocation.setVisibility(GONE);
                }
                if (day.getMusic() != -1) {
                    muteButton.setClickable(true);
                    music = day.getMusic();
                    muteButton.setVisibility(VISIBLE);
                } else {
                    muteButton.setVisibility(GONE);
                }
            } else {
                muteButton.setVisibility(GONE);
                tvTitle.setVisibility(View.GONE);
                tvDescription.setText(getString(R.string.label_empty_day));
                ivDay.setImageDrawable(null);
                btShowLocation.setVisibility(GONE);
                muteButton.setClickable(false);
                isMuted = true;
            }
        });

        muteButton.setOnClickListener(view1 -> {
            if (!isMuted) {
                isMuted = true;
                muteButton.setImageResource(R.drawable.ic_volume_off);
                stopMusicService();
            } else {
                isMuted = false;
                muteButton.setImageResource(R.drawable.ic_volume_on);
                startMusicService(music);
            }
        });

        if (!isMuted && music != -1)
            startMusicService(music);

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
        if (tvDate != null) {
            tvDate.setText(getDayTitle(year, month, dayOfMonth));
        }
        if (tvDayOfTheWeek != null) {
            tvDayOfTheWeek.setText(getDayOfWeekName(dayOfWeek));
        }
        String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);
        model.setFilter(date);
        dayLiveData = model.getSearchBy();
    }

    private void stopMusicService() {
        Intent serviceIntent = new Intent(getContext(), MusicService.class);
        getActivity().stopService(serviceIntent);
    }

    private void startMusicService(int songToPlay) {
        Intent serviceIntent = new Intent(getContext(), MusicService.class);
        serviceIntent.putExtra(SONG_DATE_NOTIFICATION_ID,
                getDayTitle(year, month, dayOfMonth));
        serviceIntent.putExtra("SELECTED_YEAR_TIME", songToPlay);
        getActivity().startService(serviceIntent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            stopMusicService();
        } else if (!isMuted){
            startMusicService(music);
        }
    }

    @Override
    public void onStop() {
        isMuted = true;
        stopMusicService();
        super.onStop();
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
        fabAdd.setVisibility(GONE);
        fabAddImage.setVisibility(GONE);
        fabAddSnapshot.setVisibility(GONE);
        fabAddMusic.setVisibility(GONE);
        fabAddLocation.setVisibility(GONE);
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
                stateDialogButton = 0;
                break;
            default:
                break;
        }
    }
}