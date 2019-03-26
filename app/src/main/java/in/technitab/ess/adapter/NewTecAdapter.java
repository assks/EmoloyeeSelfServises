package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.technitab.ess.R;
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.NewTec;
import in.technitab.ess.viewholder.NewTecMainHolder;


public class NewTecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<NewTec> mEventArrayList;
    private int headerAdapterPosition,viewUser;
    private ViewHeaderListener listener;

    NewTecAdapter(int headerAdapterPosition,int viewUser, ArrayList<NewTec> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.viewUser = viewUser;
        this.headerAdapterPosition = headerAdapterPosition;
    }

    public void setListener(ViewHeaderListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_new_tec, parent, false);
        return new NewTecMainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final NewTecMainHolder mainHolder = (NewTecMainHolder) holder;
        NewTec tec = mEventArrayList.get(position);

        mainHolder.fromTo.setText(getYMDDate(tec.getClaimStartDate(), tec.getClaimEndDate()));
        mainHolder.userNotes.setText(mContext.getResources().getString(R.string.user_note_value,tec.getUserNote()));
        mainHolder.remark.setText(mContext.getResources().getString(R.string.admin_note,tec.getRemark()));
        mainHolder.remark.setVisibility(tec.getRemark().isEmpty()?View.GONE:View.VISIBLE);
        mainHolder.userNotes.setVisibility(tec.getUserNote().isEmpty()?View.GONE:View.VISIBLE);

        mainHolder.status.setVisibility(tec.getUserNote().isEmpty() && tec.getRemark().isEmpty()? View.GONE:View.VISIBLE);

        if (!tec.getStatus().equalsIgnoreCase(mContext.getResources().getString(R.string.draft))) {
            mainHolder.date.setText(getDate(tec.getSubmitDate()));
            mainHolder.month.setText(getMonth(tec.getSubmitDate()));
        } else {
            mainHolder.date.setText("-");
            mainHolder.month.setText("");
        }
            mainHolder.type.setText(tec.getProjectName());
            String description;

            if (viewUser == mContext.getResources().getInteger(R.integer.admin)) {
                description = "Trip: "+tec.getTripId()+" | Tec "+tec.getTecId()+" | "+tec.getUserName()+" | "+mContext.getResources().getString(R.string.double_value,tec.getTotalAmount());
            } else {
                description = "Trip: "+tec.getTripId()+" | Tec "+tec.getTecId()+" | "+mContext.getResources().getString(R.string.double_value,tec.getTotalAmount());
            }

            mainHolder.description.setText(description);

            mainHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemSelected(holder, headerAdapterPosition, holder.getAdapterPosition());
                    }
                }
            });

        }


        @Override
        public int getItemCount () {
            return mEventArrayList.size();
        }

        private String getDate (String startDate){
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


        private String getMonth (String startDate){
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

        private String getYMDDate (String claimStartDate, String claimEndDate){
            String strClaimStart, strClaimEnd;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM", Locale.getDefault());
            try {
                Date d1 = simpleDateFormat.parse(claimStartDate);
                if (claimEndDate.equalsIgnoreCase("0000-00-00")) {
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
    }