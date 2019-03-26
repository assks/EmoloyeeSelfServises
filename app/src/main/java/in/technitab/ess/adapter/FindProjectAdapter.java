package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Project;

public class FindProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = FindProjectAdapter.class.getSimpleName();
    private ArrayList<Object> mProjectTaskArrayList;
    private RecyclerViewItemClickListener listener;
    private int NEW_PROJECT = 1, EXISTING_PROJECT = 2;
    private Context context;

    public FindProjectAdapter(Context context, ArrayList<Object> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        this.context = context;
    }

    public void setListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == NEW_PROJECT){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_new_project, parent, false);
            return new NewProjectHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_request_project_item, parent, false);
            return new CustomerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == NEW_PROJECT) {
            NewProjectHolder newProjectHolder = (NewProjectHolder) holder;
            newProjectHolder.createNewProject.setText(context.getResources().getString(R.string.create_new_project));
        } else if (holder.getItemViewType() == EXISTING_PROJECT) {
            CustomerViewHolder customerViewHolder = (CustomerViewHolder) holder;
            Project assignProject = (Project) mProjectTaskArrayList.get(position);
            customerViewHolder.projectName.setText(assignProject.getProjectName());
            customerViewHolder.customerName.setText(assignProject.getProjectType());
        }
    }

    @Override
    public int getItemCount() {
        return mProjectTaskArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mProjectTaskArrayList.get(position) instanceof String){
            return NEW_PROJECT;
        }else{
            return EXISTING_PROJECT;
        }
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.action_button)
        public ImageButton actionButton;
        @BindView(R.id.project_name)
        public TextView projectName;
        @BindView(R.id.customer_name)
        public TextView customerName;

        CustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.action_button)
        public void onClick() {
            listener.onClickListener(this, getAdapterPosition());
        }
    }

    public class NewProjectHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.createNewProject)
        public TextView createNewProject;

         NewProjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.createNewProject)
        protected void onNew(){
            listener.onClickListener(this, getAdapterPosition());
        }
    }
}