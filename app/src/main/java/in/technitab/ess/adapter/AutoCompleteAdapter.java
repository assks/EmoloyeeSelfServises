package in.technitab.ess.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Customer;
import in.technitab.ess.model.Project;
import in.technitab.ess.model.ProjectActivity;
import in.technitab.ess.model.User;

public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder>  {

    private ArrayList<Object> mProjectTaskFilterList;
    private Context mContext;
    private String actionViewType;
    private RecyclerViewItemClickListener listener;

    public AutoCompleteAdapter(String actionViewType, @NonNull ArrayList<Object> mOriginalValues) {
        this.mProjectTaskFilterList = mOriginalValues;
        this.actionViewType = actionViewType;
    }

    public void setonItemClickListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AutoCompleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AutoCompleteAdapter.ViewHolder holder, int position) {
        if (actionViewType.equalsIgnoreCase(mContext.getResources().getString(R.string.customer_name))) {

            Customer customer = (Customer) mProjectTaskFilterList.get(position);
            holder.customerName.setText(customer.getCustomerName());
        } else if (actionViewType.equalsIgnoreCase(mContext.getResources().getString(R.string.project_name))) {
            Project project = (Project) mProjectTaskFilterList.get(position);
            holder.customerName.setText(project.getProjectName());
        } else if (actionViewType.equalsIgnoreCase(mContext.getResources().getString(R.string.activity))) {
            ProjectActivity projectActivity = (ProjectActivity) mProjectTaskFilterList.get(position);
            holder.customerName.setText(projectActivity.getActivityType());
        } else if (actionViewType.equalsIgnoreCase(mContext.getResources().getString(R.string.user_name))) {
            User projectActivity = (User) mProjectTaskFilterList.get(position);
            holder.customerName.setText(projectActivity.getName());
        } else if (actionViewType.equalsIgnoreCase(mContext.getResources().getString(R.string.add_timesheet))) {
            User projectActivity = (User) mProjectTaskFilterList.get(position);
            holder.customerName.setText(projectActivity.getName());
        } else {
            Project project = (Project) mProjectTaskFilterList.get(position);
            holder.customerName.setText(project.getProjectName());
        }

        holder.customerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(holder,holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjectTaskFilterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.customerName)
        public TextView customerName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
