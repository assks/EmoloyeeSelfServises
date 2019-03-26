package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.AssignProject;


public class AssignProjectAdapter extends RecyclerView.Adapter<AssignProjectAdapter.ViewHolder> {

    private ArrayList<AssignProject> mProjectTaskArrayList;
    private RecyclerViewItemClickListener listener;
    private Context context;


    public AssignProjectAdapter(Context context, ArrayList<AssignProject> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        this.context = context;
    }

    public void setTaskListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AssignProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_project_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,int position) {
        AssignProject assignProject = mProjectTaskArrayList.get(position);
        holder.projectName.setText(assignProject.getProjectName());
        holder.customerName.setText(assignProject.getCustomerName());
        holder.billableDuration.setText(assignProject.getTotalBillableDuration());
        holder.totalTimesheetDuration.setText(assignProject.getTotalTimesheetDuration());
        holder.projectProfit.setText(context.getResources().getString(R.string.project_profitability,assignProject.getProjectProfit()));
        holder.tecClaim.setText(context.getResources().getString(R.string.remove_decimal,assignProject.getTecClaimExpense()));
        holder.bookingClaim.setText(context.getResources().getString(R.string.remove_decimal,assignProject.getBookingExpense()));
        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(holder,holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mProjectTaskArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.project_name)
        public TextView projectName;
        @BindView(R.id.customer_name)
        public TextView customerName;
        @BindView(R.id.total_timesheet_duration)
        public TextView totalTimesheetDuration;
        @BindView(R.id.billable_duration)
        public TextView billableDuration;
        @BindView(R.id.tec_claim)
        public TextView tecClaim;
        @BindView(R.id.booking_claim)
        public TextView bookingClaim;
        @BindView(R.id.project_profit)
        public TextView projectProfit;
        @BindView(R.id.action_button)
        public ImageButton actionButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}