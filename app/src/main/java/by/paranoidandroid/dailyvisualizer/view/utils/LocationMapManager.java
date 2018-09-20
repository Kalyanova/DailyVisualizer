package by.paranoidandroid.dailyvisualizer.view.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import by.paranoidandroid.dailyvisualizer.R;

public class LocationMapManager {
    public static void showLocation(Context context, String lattitude, String longtitude){
      Uri gmmIntentUri = Uri.parse("geo:" + lattitude + "," + longtitude);
      Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
      mapIntent.setPackage("com.google.android.apps.maps");
      if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
        context.startActivity(mapIntent);
      } else {
        Toast.makeText(context, R.string.install_maps, Toast.LENGTH_SHORT);
      }
    }
}
