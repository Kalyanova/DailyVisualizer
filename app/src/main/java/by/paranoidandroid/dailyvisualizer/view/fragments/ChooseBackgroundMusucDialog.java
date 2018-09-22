package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.view.MainActivity;

public class ChooseBackgroundMusucDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose music: ")
                .setItems(R.array.music_year_time, (dialogInterface, i) -> {
                    Fragment fr = ((MainActivity)getActivity()).getCurrentFragment();
                    if (fr.getClass() == DayEditModeFragment.class){
                        ((DayEditModeFragment)fr).addMusic(i);
                    }
                })
                .create();
    }
}
