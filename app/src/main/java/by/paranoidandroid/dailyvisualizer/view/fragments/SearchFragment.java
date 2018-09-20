package by.paranoidandroid.dailyvisualizer.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import by.paranoidandroid.dailyvisualizer.R;
import by.paranoidandroid.dailyvisualizer.view.adapters.TimeLineDayRecyclerAdapter;
import by.paranoidandroid.dailyvisualizer.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel model;
    private SearchView searchView;
    private TextView tvNothingToShow;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TimeLineDayRecyclerAdapter timeLineDayRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        model = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);

        tvNothingToShow = view.findViewById(R.id.tv_nothing_to_show);
        recyclerView = view.findViewById(R.id.rv_search);
        timeLineDayRecyclerAdapter = new TimeLineDayRecyclerAdapter();
        recyclerView.setAdapter(timeLineDayRecyclerAdapter);
        progressBar = view.findViewById(R.id.pb_search);

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                tvNothingToShow.setVisibility(View.GONE);
                model.setSearchQuery(s.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        model.getSearchData().observe(this, days -> {
            progressBar.setVisibility(View.GONE);
            if(days == null || days.size() == 0){
                tvNothingToShow.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvNothingToShow.setVisibility(View.GONE);
            }
            timeLineDayRecyclerAdapter.setDayList(days);
        });
        return view;
    }
}
