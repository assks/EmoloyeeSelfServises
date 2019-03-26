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
import in.technitab.ess.viewholder.SimpleViewHolder;


public class TripTecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<NewTec> mEventArrayList;
    private ViewHeaderListener listener;
    private int headerAdapterPosition;

    TripTecAdapter(int headerAdapterPosition, ArrayList<NewTec> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.headerAdapterPosition = headerAdapterPosition;
    }

    public void setListener(ViewHeaderListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_simple, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;
        NewTec tecUser = mEventArrayList.get(position);
        viewHolder.textView.setText(mContext.getResources().getString(R.string.trip_reference,tecUser.getTecId(),getDate(tecUser.getClaimStartDate()),getDate(tecUser.getClaimEndDate())));
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(viewHolder,headerAdapterPosition,viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventArrayList.size();
    }

    private String getDate(String startDate) {
        SimpleDateFormat stringFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMdd", Locale.getDefault());
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