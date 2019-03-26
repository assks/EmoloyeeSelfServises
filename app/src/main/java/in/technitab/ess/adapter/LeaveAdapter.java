package in.technitab.ess.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import in.technitab.ess.R;
import in.technitab.ess.model.MyLeaves;
import in.technitab.ess.model.ParticularLeave;
import in.technitab.ess.viewholder.LeaveViewHolder;


public class LeaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyLeaves> mLeaveLists;
    private Context mContext;
    private LeaveListener listener;
    private List<String> defaultLeaves;
    private Resources resources;

    public LeaveAdapter(Context context, List<MyLeaves> mLeaveLists) {
        this.mLeaveLists = mLeaveLists;
        this.mContext = context;
        resources = mContext.getResources();
        defaultLeaves = Arrays.asList(resources.getStringArray(R.array.leaveReasonArray));
    }

    public void setItemSelectedListener(LeaveListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_leave_item, parent, false);
        return new LeaveViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final LeaveViewHolder mHolder = (LeaveViewHolder) holder;
        MyLeaves leaveItem = mLeaveLists.get(position);
        String leaveReason = leaveItem.getReason();
        mHolder.type.setText(leaveReason);
        mHolder.description.setText(leaveItem.getDescription());

        ArrayList<ParticularLeave> particularLeave = leaveItem.getLeaveArrayList();
        if (!particularLeave.isEmpty()) {
            String startDate = particularLeave.get(0).getDate();
            String startMonth = getMonth(startDate);
            if (particularLeave.size() > 1) {

                String endDate = particularLeave.get(particularLeave.size() - 1).getDate();
                String endMonth = getMonth(endDate);
                if (!startMonth.equalsIgnoreCase(endMonth)) {
                    startMonth = startMonth + "-" + endMonth;
                }
                mHolder.date.setText(getDate(startDate) + "-" + getDate(endDate));
                mHolder.month.setText(startMonth);
            } else {
                mHolder.date.setText(getDate(startDate));
                mHolder.month.setText(startMonth);
            }
        }

        mHolder.dateLayout.setBackground(getLeaveStatus(leaveReason));
        mHolder.status.setBackground(getLeaveStatusImage(leaveItem.getStatus()));
        mHolder.rootLayout.setBackground(getLayoutBackground(leaveReason));

        mHolder.option.setVisibility(leaveItem.getStatus().equalsIgnoreCase(resources.getString(R.string.canceled)) || leaveItem.getStatus().equalsIgnoreCase(resources.getString(R.string.approved)) || leaveItem.getStatus().equalsIgnoreCase(resources.getString(R.string.rejected)) ? View.GONE : View.VISIBLE);

        mHolder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(mHolder, mHolder.getAdapterPosition());
            }
        });

    }

    private Drawable getLeaveStatusImage(String status) {
        Drawable drawable = null;
        if (resources.getString(R.string.pending).equalsIgnoreCase(status)) {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_question);
        } else if (resources.getString(R.string.approved).equalsIgnoreCase(status)) {
            drawable = mContext.getResources().getDrawable(R.drawable.attendance_hrs);
        } else if (resources.getString(R.string.rejected).equalsIgnoreCase(status)) {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_close);
        }else if (resources.getString(R.string.canceled).equalsIgnoreCase(status)){
            drawable = resources.getDrawable(R.drawable.ic_delete_forever);
        }
        return drawable;
    }

    private String getDate(String startDate) {
        SimpleDateFormat stringFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String d = "";
        try {
            Date date = stringFormat.parse(startDate);
            d = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }


    private String getMonth(String startDate) {
        SimpleDateFormat stringFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        String d = "";
        try {
            Date date = stringFormat.parse(startDate);
            d = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }


    private Drawable getLayoutBackground(String leaveReason) {
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.leave_type_background);
        if (defaultLeaves.get(0).equals(leaveReason)) {
            drawable = mContext.getResources().getDrawable(R.drawable.medical_leave_bg);

        } else if (defaultLeaves.get(1).equals(leaveReason)) {
            drawable = mContext.getResources().getDrawable(R.drawable.personal_leave_bg);

        } else if (defaultLeaves.get(2).equals(leaveReason)) {
            drawable = mContext.getResources().getDrawable(R.drawable.leave_background);

        } else if (defaultLeaves.get(3).equals(leaveReason)) {
            drawable = mContext.getResources().getDrawable(R.drawable.exam_leave_background);
        } else if (defaultLeaves.get(4).equals(leaveReason)) {
            drawable = mContext.getResources().getDrawable(R.drawable.exam_leave_background);
        } else if (defaultLeaves.get(5).equals(leaveReason)) {
            drawable = mContext.getResources().getDrawable(R.drawable.exam_leave_background);
        }

        return drawable;
    }

    private Drawable getLeaveStatus(String leaveReason) {
        int color = 0;
        if (defaultLeaves.get(0).equals(leaveReason)) {
            color = mContext.getResources().getColor(R.color.colorMedical);

        } else if (defaultLeaves.get(1).equals(leaveReason)) {
            color = mContext.getResources().getColor(R.color.colorPersonalEmergency);

        } else if (defaultLeaves.get(2).equals(leaveReason)) {
            color = mContext.getResources().getColor(R.color.colorAnnualHoliday);

        } else if (defaultLeaves.get(3).equals(leaveReason)) {
            color = mContext.getResources().getColor(R.color.colorExam);

        } else if (defaultLeaves.get(4).equals(leaveReason)) {
            color = mContext.getResources().getColor(R.color.colorExam);

        } else if (defaultLeaves.get(5).equals(leaveReason)) {
            color = mContext.getResources().getColor(R.color.colorExam);

        }

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.leave_type_background);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }


    @Override
    public int getItemCount() {
        return mLeaveLists.size();
    }

    public interface LeaveListener {

        void onItemSelected(RecyclerView.ViewHolder viewHolder, int position);
    }
}
