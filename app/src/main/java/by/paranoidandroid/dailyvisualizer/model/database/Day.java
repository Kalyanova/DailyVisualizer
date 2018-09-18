package by.paranoidandroid.dailyvisualizer.model.database;

import androidx.annotation.NonNull;
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
  private String music;
  private float lattitude;
  private float longtitude;

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

  public String getMusic() {
    return music;
  }

  public void setMusic(String music) {
    this.music = music;
  }

  public float getLattitude() {
    return lattitude;
  }

  public void setLattitude(float lattitude) {
    this.lattitude = lattitude;
  }

  public float getLongtitude() {
    return longtitude;
  }

  public void setLongtitude(float longtitude) {
    this.longtitude = longtitude;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }
}
