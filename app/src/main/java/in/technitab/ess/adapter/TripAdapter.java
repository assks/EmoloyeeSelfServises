package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.technitab.ess.R;
import in.technitab.ess.model.Trip;
import in.technitab.ess.util.UserPref;
import in.technitab.ess.viewholder.TripMainHolder;


public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Trip> mEventArrayList;
    private int headerAdapterPosition, viewUser;
    private TripListener listener;
    private UserPref userPref;

    TripAdapter(int headerAdapterPosition, int viewUser, ArrayList<Trip> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.viewUser = viewUser;
        this.headerAdapterPosition = headerAdapterPosition;
    }

    public void setListener(TripListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        userPref = new UserPref(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_trip, parent, false);
        return new TripMainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final TripMainHolder mainHolder = (TripMainHolder) holder;
        Trip tec = mEventArrayList.get(position);

        mainHolder.fromTo.setText(getYMDDate(tec.getStartDate(), tec.getEndDate()));
        mainHolder.userNotes.setText(mContext.getResources().getString(R.string.user_note_value, tec.getRemark()));
        mainHolder.remark.setText(mContext.getResources().getString(R.string.admin_note, tec.getComment()));
        mainHolder.remark.setVisibility(tec.getComment().isEmpty() ? View.GONE : View.VISIBLE);
        mainHolder.userNotes.setVisibility(tec.getRemark().isEmpty() ? View.GONE : View.VISIBLE);
        mainHolder.status.setVisibility(tec.getComment().isEmpty() && tec.getRemark().isEmpty() ? View.GONE : View.VISIBLE);
        mainHolder.date.setText(getDate(tec.getCreatedDate()));
        mainHolder.month.setText(getMonth(tec.getCreatedDate()));
        mainHolder.type.setText(tec.getProjectName());
        String description;

        if (viewUser == mContext.getResources().getInteger(R.integer.admin)) {
            mainHolder.action.setVisibility(View.GONE);
            description = mContext.getResources().getString(R.string.trip_admin_description, tec.getId(), tec.getCreatedBy());
        } else if (Integer.parseInt(userPref.getUserId()) == tec.getCreatedById() && tec.getStatus().equalsIgnoreCase(mContext.getResources().getString(R.string.initiated))) {
            mainHolder.action.setVisibility(View.VISIBLE);
            description = mContext.getResources().getString(R.string.trip_user_description, tec.getId());
        } else {
            mainHolder.action.setVisibility(View.GONE);
            description = mContext.getResources().getString(R.string.trip_user_description, tec.getId());
        }

        mainHolder.description.setText(description);
        mainHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRootClick(holder, headerAdapterPosition, holder.getAdapterPosition());
                }
            }
        });

        mainHolder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onActionClick(holder, headerAdapterPosition, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventArrayList.size();
    }

    private String getDate(String startDate) {
        SimpleDateFormat stringFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
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
        SimpleDateFormat stringFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
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

    private String getYMDDate(String claimStartDate, String claimEndDate) {
        String strClaimStart, strClaimEnd;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        try {
            Date d1 = simpleDateFormat.parse(claimStartDate);
            if (claimEndDate.equalsIgnoreCase(mContext.getResources().getString(R.string.default_date)) || TextUtils.isEmpty(claimEndDate)) {
                strClaimEnd = "-";
            } else {
                Date d2 = simpleDateFormat.parse(claimEndDate);
                strClaimEnd = dateFormat.format(d2);
            }
            strClaimStart = dateFormat.format(d1);

        } catch (ParseException e) {
            e.printStackTrace();
            strClaimStart = "-";
            strClaimEnd = "-";
        }
        return mContext.getResources().getString(R.string.tec_claim_start_end, strClaimStart, strClaimEnd);
    }

    public interface TripListener {
        void onRootClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position);

        void onActionClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position);
    }
}