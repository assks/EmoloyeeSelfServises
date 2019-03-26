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
import in.technitab.ess.model.ApproveLeave;

public class ApproveLeaveHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ApproveLeave> mEventArrayList;
    private SparseIntArray listPosition = new SparseIntArray();
    private ApproveLeaveAdapter.LeaveListener mItemClickListener;

    public ApproveLeaveHeaderAdapter(ArrayList<ApproveLeave> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
    }


    public class LeaveHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_name)
        public TextView userName;
        @BindView(R.id.userLeaveRecyclerView)
        public RecyclerView userLeaveRecyclerView;

        public LeaveHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_leave_header, parent, false);
        return new LeaveHeaderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final LeaveHeaderHolder cellViewHolder = (LeaveHeaderHolder) holder;
        final ApproveLeave approveLeave = mEventArrayList.get(position);


       /* if (selectedViewType.equalsIgnoreCase(mContext.getResources().getString(R.string.date)))
            projectOrDate = getDate(projectOrDate);*/

        cellViewHolder.userName.setText(approveLeave.getName());

        cellViewHolder.userLeaveRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cellViewHolder.userLeaveRecyclerView.setLayoutManager(layoutManager);
        ApproveLeaveAdapter adapter = new ApproveLeaveAdapter(/*selectedViewType, */mContext,position,approveLeave.getMyLeavesArrayList());
        cellViewHolder.userLeaveRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(mItemClickListener);


        int lastSeenFirstPosition = listPosition.get(position, 0);
        if (lastSeenFirstPosition >= 0) {
            cellViewHolder.userLeaveRecyclerView.scrollToPosition(lastSeenFirstPosition);
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

    public void SetOnItemClickListener(ApproveLeaveAdapter.LeaveListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
