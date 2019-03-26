package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;

import java.util.ArrayList;

import in.technitab.ess.R;
import in.technitab.ess.model.ProjectTask;
import in.technitab.ess.viewholder.ProjectTaskHolder;

public class ProjectTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ProjectTask> mProjectTaskArrayList;
    private ProjectTaskListener listener;
    private String prevProject = "";
    private ProjectTaskHolder projectTaskHolder;
    private Context context;
    private int selectedHour = 0, selectedMinute = 0;


    public ProjectTaskAdapter(Context context, ArrayList<ProjectTask> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        this.context = context;

    }

    public void setTaskListener(ProjectTaskListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task, parent, false);
        return new ProjectTaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        projectTaskHolder = (ProjectTaskHolder) holder;

        String projectName = mProjectTaskArrayList.get(position).getProject();
        projectTaskHolder.project.setVisibility(prevProject.equalsIgnoreCase(projectName) ? View.GONE : View.VISIBLE);
        prevProject = projectName;
        projectTaskHolder.project.setText(projectName);

        projectTaskHolder.timeSpent.setText(mProjectTaskArrayList.get(position).getSpentHours());

        projectTaskHolder.taskName.setText(mProjectTaskArrayList.get(position).getActivity());
        projectTaskHolder.isBillable.setChecked(mProjectTaskArrayList.get(position).getIsBillable() != 0);
        projectTaskHolder.timeSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDuration(position);
            }
        });

        projectTaskHolder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTextChange(projectTaskHolder, position);
            }
        });

        projectTaskHolder.isBillable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int isBillable = mProjectTaskArrayList.get(position).getIsBillable();
                mProjectTaskArrayList.get(position).setIsBillable(isBillable == 0 ? 1 : 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProjectTaskArrayList.size();
    }

    private void onDuration(final int position) {
        selectedHour = selectedMinute = 0;
        final AlertDialog builder = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_spent_hour, null);
        builder.setView(view);

        final NumberPicker hour = view.findViewById(R.id.hour);
        NumberPicker minute = view.findViewById(R.id.minute);
        Button done = view.findViewById(R.id.done);
        final Button cancel = view.findViewById(R.id.cancel);
        hour.setMinValue(0);
        hour.setMaxValue(23);
        minute.setMinValue(0);
        minute.setMaxValue(59);

        hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedHour = newVal;
            }
        });


        minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedMinute = newVal;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                String timeString = context.getResources().getString(R.string.min_hours_value, selectedHour, selectedMinute);
                if (!timeString.equalsIgnoreCase(context.getResources().getString(R.string.blank_duration))) {
                    mProjectTaskArrayList.get(position).setSpentHours(timeString);
                    notifyDataSetChanged();
                } else {
                    mProjectTaskArrayList.get(position).setSpentHours("");
                    notifyDataSetChanged();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        builder.show();
    }

    public interface ProjectTaskListener {
        void onTextChange(RecyclerView.ViewHolder viewHolder, int position);
    }
}
