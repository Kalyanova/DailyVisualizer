package by.paranoidandroid.dailyvisualizer.model.utils;

public class CalendarDate {
    public int year, month, dayOfMonth;

    public CalendarDate(int y, int m, int d) {
        year = y;
        month = m;
        dayOfMonth = d;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CalendarDate other = (CalendarDate) obj;
        return year == other.year && month == other.month && dayOfMonth == other.dayOfMonth;
    }
}