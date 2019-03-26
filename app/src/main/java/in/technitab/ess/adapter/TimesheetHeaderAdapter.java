package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.model.TimeSheetDate;


public class TimesheetHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TimeSheetDate> mEventArrayList;
    private SparseIntArray listPosition = new SparseIntArray();
    private String selectedViewType;


    public TimesheetHeaderAdapter(String selectedViewType, ArrayList<TimeSheetDate> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.selectedViewType = selectedViewType;
    }


    public class TimesheetHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        public TextView date;
        @BindView(R.id.timeSheetRecyclerView)
        public RecyclerView timeSheetRecyclerView;

        public TimesheetHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_timesheet_header, parent, false);
        return new TimesheetHeaderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TimesheetHeaderHolder cellViewHolder = (TimesheetHeaderHolder) holder;
        final TimeSheetDate timeSheetDate = mEventArrayList.get(position);

        String projectOrDate = timeSheetDate.getDate();

        if (selectedViewType.equalsIgnoreCase(mContext.getResources().getString(R.string.date)))
            projectOrDate = getDate(projectOrDate);

        cellViewHolder.date.setText(projectOrDate);

        cellViewHolder.timeSheetRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cellViewHolder.timeSheetRecyclerView.setLayoutManager(layoutManager);
        TimesheetAdapter adapter = new TimesheetAdapter(selectedViewType, mEventArrayList.get(position).getResponses());
        cellViewHolder.timeSheetRecyclerView.setAdapter(adapter);

        int lastSeenFirstPosition = listPosition.get(position, 0);
        if (lastSeenFirstPosition >= 0) {
            cellViewHolder.timeSheetRecyclerView.scrollToPosition(lastSeenFirstPosition);
        }

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

    @Override
    public int getItemCount() {
        return mEventArrayList.size();
    }
}
