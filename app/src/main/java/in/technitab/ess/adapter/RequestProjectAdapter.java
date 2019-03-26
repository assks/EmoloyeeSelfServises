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
import in.technitab.ess.model.RequestedProject;


public class RequestProjectAdapter extends RecyclerView.Adapter<RequestProjectAdapter.ViewHolder> {

    private ArrayList<RequestedProject> mProjectTaskArrayList;
    private RecyclerViewItemClickListener listener;
    private Context context;

    /*
     viewFrom 1 chooseProject 2 UnassignedProject
     */
    public RequestProjectAdapter(Context context, ArrayList<RequestedProject> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        this.context = context;
    }

    public void setTaskListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_expense_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        RequestedProject assignProject = mProjectTaskArrayList.get(position);
        holder.projectName.setText(assignProject.getProjectName());
        holder.customerName.setText(assignProject.getCreatedBy());
        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(holder, holder.getAdapterPosition());
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
        @BindView(R.id.action_button)
        public ImageButton actionButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}