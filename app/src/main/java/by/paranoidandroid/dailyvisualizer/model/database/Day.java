package by.paranoidandroid.dailyvisualizer.model.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DAY")
public class Day {
  @NonNull
  @PrimaryKey
  private String date;
  private String title;
  private String description;
  private String latitude;
  private String longitude;

  private int music = -1;

  public Day(String date, String title, String description) {
    this.date = date;
    this.title = title;
    this.description = description;
  }

  @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
  private byte[] image;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = String.valueOf(longitude);
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public int getMusic() {
    return music;
  }

  public void setMusic(int music) {
    this.music = music;
  }
}
