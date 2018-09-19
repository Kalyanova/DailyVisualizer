package by.paranoidandroid.dailyvisualizer.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.model.database.Day;
import java.util.List;

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

    ViewHolder(View view) {
      super(view);
      tvTitle = view.findViewById(R.id.timeline_item_text_view_title);
      tvDatePreview = view.findViewById(R.id.timeline_item_text_view_date_preview);
      tvDate = view.findViewById(R.id.timeline_item_text_view_date);
    }

    //TODO: implement this method
    private String getPreviewDate(String date){
      return "1 week ago";
    }

    public void bind(Day day){
      tvDatePreview.setText(getPreviewDate(day.getDate()));
      tvDate.setText(day.getDate());
      tvTitle.setText(day.getTitle());

      //TODO: implement setting picture
    }
  }
}
