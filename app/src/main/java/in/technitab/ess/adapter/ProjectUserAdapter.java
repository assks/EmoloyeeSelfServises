package in.technitab.ess.adapter;

import android.content.Context;
import android.content.res.Resources;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import in.technitab.ess.R;

import in.technitab.ess.model.ProjectUser;
import in.technitab.ess.viewholder.AddViewHolder;
import in.technitab.ess.viewholder.ProjectUserHolder;

public class ProjectUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NEW_PROJECT = 1, PROJECT_ITEM = 2;
    private List<Object> mObjectList;
    private Context mContext;
    private LeaveAdapter.LeaveListener listener;

    private Resources resources;

    public ProjectUserAdapter(Context context, List<Object> mObjectList) {
        this.mObjectList = mObjectList;
        this.mContext = context;
        resources = mContext.getResources();
    }

    public void setItemSelectedListener(LeaveAdapter.LeaveListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == NEW_PROJECT) {
            View view = inflater.inflate(R.layout.layout_new_entry, parent, false);
            return new AddViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.layout_project_user, parent, false);
            return new ProjectUserHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == NEW_PROJECT) {
            AddViewHolder addViewHolder = (AddViewHolder) holder;
            addViewHolder.newEntry.setText(resources.getString(R.string.add_project));

        } else if (holder.getItemViewType() == PROJECT_ITEM) {
            ProjectUser projectUser = (ProjectUser) mObjectList.get(position);
            ProjectUserHolder userHolder = (ProjectUserHolder) holder;

            userHolder.projectName.setText(projectUser.getProjectName());
            userHolder.projectType.setText(projectUser.getProjectType());
        }
    }


    @Override
    public int getItemCount() {
        return mObjectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectList.get(position) instanceof String)
            return NEW_PROJECT;
        else
            return PROJECT_ITEM;
    }

}