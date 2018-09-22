package by.paranoidandroid.dailyvisualizer.view.utils;

import static by.paranoidandroid.dailyvisualizer.model.utils.Constants.GOOGLE_MAP_INTENT;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import by.paranoidandroid.dailyvisualizer.R;

public class LocationMapManager {
    public static void showLocation(Context context, String lattitude, String longtitude){
      Uri gmmIntentUri = Uri.parse("geo:" + lattitude + "," + longtitude);
      Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
      mapIntent.setPackage(GOOGLE_MAP_INTENT);
      if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
        context.startActivity(mapIntent);
      } else {
        Toast.makeText(context, R.string.install_maps, Toast.LENGTH_SHORT).show();
      }
    }
}
