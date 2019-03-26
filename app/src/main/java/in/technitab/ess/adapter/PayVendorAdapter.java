package in.technitab.ess.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.PayVendor;
import in.technitab.ess.viewholder.TecMainHolder;

public class PayVendorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PayVendor> mEventArrayList;
    private Resources resources;
    private RecyclerViewItemClickListener listener;
    private String action;

    public PayVendorAdapter(String action,ArrayList<PayVendor> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.action = action;
    }

    public void setListener(RecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        resources = mContext.getResources();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tec, parent, false);
        return new TecMainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final TecMainHolder mainHolder = (TecMainHolder) holder;
        PayVendor payVendor = mEventArrayList.get(position);

        final String requestDate = payVendor.getCreatedDate();
        mainHolder.date.setText(getDate(requestDate));
        mainHolder.month.setText(getMonth(requestDate));
        mainHolder.type.setText(payVendor.getVendorName());

        String description;
        if (action.equalsIgnoreCase(resources.getString(R.string.approve))){
            description = resources.getString(R.string.pay_vendor_description,payVendor.getUserName(),payVendor.getStatus(),payVendor.getTotalAmount());
        }else {
            description = resources.getString(R.string.pay_vendor_user_description,payVendor.getStatus(),payVendor.getTotalAmount());
        }
        mainHolder.description.setText(description);
        mainHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClickListener(holder,holder.getAdapterPosition());
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mEventArrayList.size();
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

}