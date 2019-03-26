package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.technitab.ess.R;
import in.technitab.ess.model.TimesheetDataResponse;
import in.technitab.ess.viewholder.TimeSheetHolder;


public class TimesheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TimesheetDataResponse> mTaskArrayList;
    private String selectedView;

    public TimesheetAdapter(String selectedView, ArrayList<TimesheetDataResponse> mTaskArrayList) {
        this.mTaskArrayList = mTaskArrayList;
        this.selectedView = selectedView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_timesheet_item, parent, false);
        return new TimeSheetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimeSheetHolder mHolder = (TimeSheetHolder) holder;
        TimesheetDataResponse row = mTaskArrayList.get(mHolder.getAdapterPosition());

        String projectOrDate = row.getName();
        if (selectedView.equalsIgnoreCase(mContext.getResources().getString(R.string.project)))
            projectOrDate = getDate(projectOrDate);
        mHolder.activityName.setText(projectOrDate);
        mHolder.time.setText(row.getSpentTime());
    }

    @Override
    public int getItemCount() {
        return mTaskArrayList.size();
    }


    private String getDate(String projectOrDate) {
        String mDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = format.parse(projectOrDate);

            SimpleDateFormat stringFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            mDate = stringFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }
}
