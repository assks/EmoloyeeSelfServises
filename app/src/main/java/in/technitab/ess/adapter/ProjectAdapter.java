package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.technitab.ess.R;
import in.technitab.ess.model.TimesheetProject;
import in.technitab.ess.viewholder.TimeSheetProjectHolder;

public class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TimesheetProject> mTimesheetProjects;
    private Context mContext;
    private String prevProject = "";

    public ProjectAdapter(ArrayList<TimesheetProject> mTimesheetProjects) {
        this.mTimesheetProjects = mTimesheetProjects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_timesheet, parent, false);
        return new TimeSheetProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimeSheetProjectHolder mHolder = (TimeSheetProjectHolder) holder;
        TimesheetProject projectRow = mTimesheetProjects.get(position);
        prevProject = position == 0 ? "" : prevProject;

        String project = projectRow.getProject();
        if (!prevProject.equalsIgnoreCase(project)) {
            prevProject = project;
        } else {
            mHolder.project.setVisibility(View.GONE);
        }
        mHolder.project.setText(project);
        mHolder.activityName.setText(projectRow.getActivityName());
        String timesheetHours = projectRow.getSpentTime();
        mHolder.timeSpent.setText(timesheetHours);

    }
    @Override
    public int getItemCount() {
        return mTimesheetProjects.size();
    }
}
