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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.technitab.ess.R;
import in.technitab.ess.model.ApproveTimeSheet;

import in.technitab.ess.viewholder.ApproveTimeSheetHolder;

public class ApproveTimeSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ApproveTimeSheet> mProjectTaskArrayList;
    private ProjectTaskListener listener;
    private ApproveTimeSheetHolder approveTimeSheetHolder;
    private String prevUserName = "";
    private Context context;
    private int selectedHour = 0, selectedMinute = 0;


    public ApproveTimeSheetAdapter(Context context, ArrayList<ApproveTimeSheet> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        this.context = context;
    }

    public void setTaskListener(ProjectTaskListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_approve_timesheet, parent, false);
        return new ApproveTimeSheetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        approveTimeSheetHolder = (ApproveTimeSheetHolder) holder;
        ApproveTimeSheet approveTimeSheet = mProjectTaskArrayList.get(position);
        String name = approveTimeSheet.getName();

        if (prevUserName.equalsIgnoreCase(name)) {
            approveTimeSheetHolder.name.setVisibility(View.GONE);
        } else {
            approveTimeSheetHolder.name.setVisibility(View.VISIBLE);
            prevUserName = name;
        }

        approveTimeSheetHolder.name.setText(approveTimeSheet.getName());
        approveTimeSheetHolder.date.setText(getDate(approveTimeSheet.getDate()));
        approveTimeSheetHolder.timeSpent.setText(mProjectTaskArrayList.get(position).getSpentHours());
        approveTimeSheetHolder.taskName.setText(approveTimeSheet.getActivity());
        approveTimeSheetHolder.isBillable.setChecked(approveTimeSheet.getIsBillable() != 0);
        approveTimeSheetHolder.timeSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDuration(approveTimeSheetHolder.getAdapterPosition());
            }
        });

        approveTimeSheetHolder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTextChange(approveTimeSheetHolder, approveTimeSheetHolder.getAdapterPosition());
            }
        });

        approveTimeSheetHolder.isBillable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int isBillable = mProjectTaskArrayList.get(approveTimeSheetHolder.getAdapterPosition()).getIsBillable();
                mProjectTaskArrayList.get(approveTimeSheetHolder.getAdapterPosition()).setIsBillable(isBillable == 0 ? 1 : 0);
            }
        });

    }

    private String getDate(String strDate) {
        String d = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat strFormat = new SimpleDateFormat("MMM dd,yy", Locale.getDefault());
            d = strFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
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
        Button cancel = view.findViewById(R.id.cancel);
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
                if (/*mProjectTaskArrayList.get(position).getSpentHours().isEmpty() ||*/ !timeString.equalsIgnoreCase(context.getResources().getString(R.string.blank_duration))) {
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
