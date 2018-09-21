package by.paranoidandroid.dailyvisualizer.view.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import java.util.List;
import java.util.Locale;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeLineDayRecyclerAdapter extends RecyclerView.Adapter<TimeLineDayRecyclerAdapter.ViewHolder> {

  private List<Day> dayList;

  public void setDayList(List<Day> dayList){
    this.dayList = dayList;
    notifyDataSetChanged();
  }

  public List<Day> getDayList() {
    return dayList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(dayList.get(position));
  }

  @Override
  public int getItemCount() {
    if(dayList == null){
      return 0;
    }
    return dayList.size();
  }

  static class ViewHolder extends  RecyclerView.ViewHolder{
    private TextView tvTitle, tvDate, tvDatePreview;
    private ImageView ivPicture;

    ViewHolder(View view) {
      super(view);
      tvTitle = view.findViewById(R.id.timeline_item_text_view_title);
      tvDatePreview = view.findViewById(R.id.timeline_item_text_view_date_preview);
      tvDate = view.findViewById(R.id.timeline_item_text_view_date);
      ivPicture = view.findViewById(R.id.timeline_item_image_view_day_picture);
    }

    private String getPreviewDate(LocalDate date){
      LocalDate now = LocalDate.now();
      Period period = new Period(date, now);

      if(period.getYears() != 0) {
        return tvDate.getContext().getResources().getQuantityString(R.plurals.timeline_year, period.getYears(), period.getYears());
      } else if(period.getMonths() != 0){
        return tvDate.getResources().getQuantityString(R.plurals.timeline_month, period.getMonths(), period.getMonths());
      } else if(period.getWeeks() != 0){
        return tvDate.getResources().getQuantityString(R.plurals.timeline_week, period.getWeeks(), period.getWeeks());
      } else if(period.getDays() != 0){
        return tvDate.getResources().getQuantityString(R.plurals.timeline_days, period.getDays(), period.getDays());
      } else {
        return tvDate.getContext().getResources().getString(R.string.today);
      }
    }


    public void bind(Day day){
      DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy.MM.dd").withLocale(Locale.UK);
      LocalDate date = fmt.parseLocalDate(day.getDate());
      DateTimeFormatter fmtDay = DateTimeFormat.forPattern("d MMMM, yyyy").withLocale(Locale.UK);
      tvDate.setText(date.toString(fmtDay));
      tvDatePreview.setText(getPreviewDate(date));

      tvTitle.setText(day.getTitle());

      if(day.getImage() != null){
        Bitmap bitmap = BitmapFactory.decodeByteArray(day.getImage(), 0, day.getImage().length);
        ivPicture.setImageBitmap(bitmap);
      }
      //TODO: implement setting picture
    }
  }
}
