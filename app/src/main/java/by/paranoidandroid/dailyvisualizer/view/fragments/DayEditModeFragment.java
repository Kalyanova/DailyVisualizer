package by.paranoidandroid.dailyvisualizer.view.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_WEEK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DESCRIPTION;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_IMAGE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_LOCATION;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_TITLE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_YEAR;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.DATE_FORMAT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.IMAGE_MIME_TYPE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_IMAGE_SHAPSHOT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_OPEN_IMAGE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_PERMISSION_FOR_LOCATION;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_PERMISSION_FOR_SNAPSHOT;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import by.paranoidandroid.dailyvisualizer.view.utils.BitmapManager;
import by.paranoidandroid.dailyvisualizer.viewmodel.EditDayViewModel;

import static android.app.Activity.RESULT_OK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_DAY_OF_WEEK;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_MONTH;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.ARGS_YEAR;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.DATE_FORMAT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.IMAGE_MIME_TYPE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_IMAGE_SHAPSHOT;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_OPEN_IMAGE;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_PERMISSION_FOR_LOCATION;
import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.REQUEST_PERMISSION_FOR_SNAPSHOT;

public class DayEditModeFragment extends DayParentFragment {
    private boolean isFABOpened;
    private Button btnSave;
    private EditText etTitle, etDescription;
    private FloatingActionButton fabAdd, fabAddImage, fabAddSnapshot, fabAddMusic, fabAddLocation;
    private EditDayViewModel viewModel;
    private LinearLayout container;
    private Location location;
    private ProgressBar progressBar;
    private String mCurrentPhotoPath;

    private OnClickListener deleteImageLisnener = view -> {
        img = null;
        fabAddImage.setClickable(true);
        fabAddSnapshot.setClickable(true);
        ((ViewGroup) view.getParent().getParent()).removeView((ViewGroup) view.getParent());
    };

    private OnClickListener deleteMapListener = view -> {
        location = null;
        fabAddLocation.setClickable(true);
        ((ViewGroup) view.getParent().getParent()).removeView((ViewGroup) view.getParent());
    };

    //JUST FOR TESTING
    //Adding latest picture in database (if not null)
    private ImageView img;

    public static DayEditModeFragment newInstance(int year, int month, int dayOfMonth,
        int dayOfWeek) {
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
        setRetainInstance(true);
        viewModel = ViewModelProviders.of(this).get(EditDayViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
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

        this.container = view.findViewById(R.id.ll_container);
        etTitle = view.findViewById(R.id.et_title);
        etDescription = view.findViewById(R.id.et_description);

        btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            // TODO: change it - add saving other stuff (image, location and etc.) to database
            String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);
            Day day = new Day(date,
                etTitle.getText().toString().trim(),
                etDescription.getText().toString().trim());
            if (img != null) {
                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                day.setImage(getByteArray(bitmap));
            }
            if(location != null){
                day.setLongitude(String.valueOf(location.getLongitude()));
                day.setLatitude(String.valueOf(location.getLatitude()));
            }
            viewModel.insertDay(day);
            // Here we try to close edit mode fragment like Activity with finish()
            getActivity().getSupportFragmentManager()
                .popBackStack();
        });

        showFABs();
        setupFabs();
        progressBar = view.findViewById(R.id.pb_detail_mode);

        String date = String.format(Locale.ENGLISH, DATE_FORMAT, year, month + 1, dayOfMonth);

        if(bundle.getString(ARGS_TITLE) != null){
            changeUIVisibility();
            etTitle.setText(bundle.getString(ARGS_TITLE));
            etDescription.setText(bundle.getString(ARGS_DESCRIPTION));
            if (bundle.getByteArray(ARGS_IMAGE) != null){
                fabAddSnapshot.setClickable(false);
                fabAddImage.setClickable(false);
                byte arr [] = bundle.getByteArray(ARGS_IMAGE);
                img = createImageView(deleteImageLisnener);
                Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
                img.setImageBitmap(bitmap);
            }
            if(bundle.getParcelable(ARGS_LOCATION) != null){
                location = bundle.getParcelable(ARGS_LOCATION);
                showLocationButton();
            }
        } else {
            viewModel.setFilter(date);
            viewModel.getSearchBy().observe(this, day -> {
                changeUIVisibility();
                if(day != null){
                    etTitle.setText(day.getTitle());
                    etDescription.setText(day.getDescription());
                    if(day.getLatitude() != null){
                        location = new Location("");
                        location.setLatitude(Double.valueOf(day.getLatitude()));
                        location.setLongitude(Double.valueOf(day.getLongitude()));
                        showLocationButton();
                    }
                    if(day.getImage() != null){
                        fabAddSnapshot.setClickable(false);
                        fabAddImage.setClickable(false);
                        img = createImageView(deleteImageLisnener);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(day.getImage(), 0, day.getImage().length);
                        img.setImageBitmap(bitmap);
                    }
                }
            });
        }

        return view;
    }

    private void changeUIVisibility(){
        progressBar.setVisibility(GONE);
        etDescription.setVisibility(View.VISIBLE);
        etTitle.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
    }

    private byte[] getByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARGS_YEAR, year);
        outState.putInt(ARGS_MONTH, month);
        outState.putInt(ARGS_DAY_OF_MONTH, dayOfMonth);
        outState.putInt(ARGS_DAY_OF_WEEK, dayOfWeek);

        if(img != null){
            outState.putByteArray(ARGS_IMAGE,
                getByteArray(((BitmapDrawable) img.getDrawable()).getBitmap()));
        }
        outState.putString(ARGS_TITLE, etTitle.getText().toString());
        outState.putString(ARGS_DESCRIPTION, etDescription.getText().toString());
        outState.putParcelable(ARGS_LOCATION, location);
        super.onSaveInstanceState(outState);
    }

    private void setupFabs(){
        fabAdd.setOnClickListener(v -> {
                Toast.makeText(getActivity(), "Add click", Toast.LENGTH_SHORT).show();
                if (!isFABOpened) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        );

        fabAddImage.setOnClickListener(v -> {
            closeFABMenu();
            fabAddSnapshot.setClickable(false);
            fabAddImage.setClickable(false);
            performImageFileSearch();
        });


        fabAddLocation.setOnClickListener(v -> {
            if (!checkPermission(permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_FOR_LOCATION);
            } else {
                addLocation();
            }

        });

        fabAddSnapshot.setOnClickListener(v -> {
            closeFABMenu();
            fabAddImage.setClickable(false);
            fabAddSnapshot.setClickable(false);
            if (!checkPermission(permission.WRITE_EXTERNAL_STORAGE,
                permission.READ_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{permission.WRITE_EXTERNAL_STORAGE,
                        permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_FOR_SNAPSHOT);
            } else {
                addSnapshot();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_FOR_SNAPSHOT) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addSnapshot();
            } else {
                //TODO: implement this sutuation
            }
        } else if (requestCode == REQUEST_PERMISSION_FOR_LOCATION) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addLocation();
            } else {
                //TODO: implement this sutuation
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void addSnapshot() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "by.paranoidandroid.dailyvisualizer.provider",
                    photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_SHAPSHOT);
            }
        }
    }

    private boolean checkPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) !=
                PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_SHAPSHOT) {
                Bitmap myBitmap = BitmapManager
                        .getBitmapForImageView(mCurrentPhotoPath, container.getWidth());
                ImageView iv = createImageView(deleteImageLisnener);
                iv.setImageBitmap(myBitmap);
                img = iv;
            } else if (requestCode == REQUEST_OPEN_IMAGE) {
                // The document selected by the user won't be returned in the intent.
                // Instead, a URI to that document will be contained in the return intent
                // provided to this method as a parameter.
                // Pull that URI using resultData.getData().
                if (data != null) {
                    Uri uri = data.getData();
                    ImageView iv = createImageView(deleteImageLisnener);
                    iv.setImageURI(uri);
                    img = iv;
                }
            }
        }
    }


    private void showFABMenu() {
        int orientation = this.getResources().getConfiguration().orientation;
        isFABOpened = true;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fabAddLocation.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
            fabAddMusic.animate().translationY(-getResources().getDimension(R.dimen.standard_130));
            fabAddSnapshot.animate().translationY(-getResources().getDimension(R.dimen.standard_185));
            fabAddImage.animate().translationY(-getResources().getDimension(R.dimen.standard_240));
        } else {
            fabAddLocation.animate().translationX(-getResources().getDimension(R.dimen.standard_75));
            fabAddMusic.animate().translationX(-getResources().getDimension(R.dimen.standard_130));
            fabAddSnapshot.animate().translationX(-getResources().getDimension(R.dimen.standard_185));
            fabAddImage.animate().translationX(-getResources().getDimension(R.dimen.standard_240));
        }
    }

    private void closeFABMenu() {
        int orientation = this.getResources().getConfiguration().orientation;
        isFABOpened = false;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fabAdd.animate().translationY(0);
            fabAddImage.animate().translationY(0);
            fabAddSnapshot.animate().translationY(0);
            fabAddMusic.animate().translationY(0);
            fabAddLocation.animate().translationY(0);
        } else {
            fabAdd.animate().translationX(0);
            fabAddImage.animate().translationX(0);
            fabAddSnapshot.animate().translationX(0);
            fabAddMusic.animate().translationX(0);
            fabAddLocation.animate().translationX(0);
        }
    }

    private void showLocationButton(){
        ImageView iv = createImageView(deleteMapListener);
        iv.getLayoutParams().height = ((int)getResources().getDimension(R.dimen.map_height));
        iv.setImageResource(R.drawable.map);

        int padding = getResources().getDimensionPixelSize(R.dimen.map_padding);
        ((View)iv.getParent()).setPadding(padding, padding, padding, padding);
        fabAddLocation.setClickable(false);
    }

    //permission already checked
    @SuppressLint("MissingPermission")
    private void addLocation() {
        LocationManager locationManager = (LocationManager) getActivity()
            .getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        location = locationManager.getLastKnownLocation(locationProvider);
        showLocationButton();
    }

    private void performImageFileSearch() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Filter to show only images, using the image MIME data type.
            intent.setType(IMAGE_MIME_TYPE);
        } else {
            intent = new Intent(Intent.ACTION_PICK);
        }
        intent.setType(IMAGE_MIME_TYPE);
        openActivityForResult(intent, REQUEST_OPEN_IMAGE);
    }

    private ImageView createImageView(OnClickListener listenerDelete) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view = li.inflate(R.layout.image_view_delete_button, null);
        container.addView(view, container.getChildCount()-1);
        ImageView iv  = view.findViewById(R.id.iv_picture);
        view.findViewById(R.id.ib_delete_image).setOnClickListener(listenerDelete);
        return iv;
    }

    private void openActivityForResult(Intent intent, int requestCode) {
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(getContext(), R.string.no_appropriate_apps, Toast.LENGTH_SHORT).show();
        }
    }

    private void showFABs() {
        findFABs();
        fabAdd.setVisibility(View.VISIBLE);
        fabAddImage.setVisibility(View.VISIBLE);
        fabAddSnapshot.setVisibility(View.VISIBLE);
        fabAddMusic.setVisibility(View.VISIBLE);
        fabAddLocation.setVisibility(View.VISIBLE);
    }

    protected void findFABs() {
        fabAdd = getActivity().findViewById(R.id.fab_add);
        fabAddImage = getActivity().findViewById(R.id.fab_add_image);
        fabAddSnapshot = getActivity().findViewById(R.id.fab_add_snapshot);
        fabAddMusic = getActivity().findViewById(R.id.fab_add_music);
        fabAddLocation = getActivity().findViewById(R.id.fab_add_location);
    }
}