package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import by.paranoidandroid.dailyvisualizer.R;

public class DialogDeleteDayFragment extends DialogFragment {

    private int dialogState;
    private OnDismissDialogListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_delete_day, null);
        view.findViewById(R.id.button_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogState = 2;
                OnDialogButtonClickListener onDialogButtonClick = (OnDialogButtonClickListener) getActivity();
                onDialogButtonClick.dialogButtonClick(dialogState);
                dismiss();
            }
        });
        view.findViewById(R.id.button_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogState = 1;
                OnDialogButtonClickListener onDialogButtonClick = (OnDialogButtonClickListener) getActivity();
                onDialogButtonClick.dialogButtonClick(dialogState);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDismissDialog();
    }

    public interface OnDialogButtonClickListener {
        void dialogButtonClick(int dialogState);
    }

    public interface OnDismissDialogListener {
        void onDismissDialog();
    }

    public void setListenerDissmis(OnDismissDialogListener listener) {
        this.listener = listener;
    }
}
