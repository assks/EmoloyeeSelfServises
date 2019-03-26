package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.NewTecEntry;

public class NewTecEntryAdapter extends RecyclerView.Adapter<NewTecEntryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<NewTecEntry> mEventArrayList;
    private List<String> mCategoryList;
    private int headerPosition;
    private ViewHeaderListener mItemClickListener;

    NewTecEntryAdapter(Context mContext, int headerPosition, ArrayList<NewTecEntry> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        mCategoryList = new ArrayList<>();
        this.mContext = mContext;
        this.headerPosition = headerPosition;
        mCategoryList =  Arrays.asList(mContext.getResources().getStringArray(R.array.tecCategoryArray));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootLayout)
        public RelativeLayout rootLayout;
        @BindView(R.id.status)
        public ImageView status;
        @BindView(R.id.paid_by)
        public ImageView paidBy;
        @BindView(R.id.is_attachment)
        public ImageView isAttachment;
        @BindView(R.id.description)
        public TextView description;
        @BindView(R.id.type)
        public TextView type;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tec_entry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewTecEntryAdapter.ViewHolder holder, int position) {
        NewTecEntry tecEntry = mEventArrayList.get(position);
        String type = tecEntry.getEntryCategory();
        holder.type.setText(type);
        holder.type.setVisibility(View.GONE);
        String description = "";


        if (type.equalsIgnoreCase(mCategoryList.get(0))){
            description = "Bid - " + tecEntry.getBookingId() + " | Pid- " + tecEntry.getPaymentId() + " | " +
                    tecEntry.getDepartureDate() + " | " + tecEntry.getFromLocation() + " To " + tecEntry.getToLocation() + " | " +
                    tecEntry.getTravelMode() + " | " + mContext.getResources().getString(R.string.double_value, tecEntry.getBillAmount());
        }else if (type.equalsIgnoreCase(mCategoryList.get(1))){
            description = mContext.getResources().getString(R.string.food_description,mEventArrayList.get(position).getLocation(),mEventArrayList.get(position).getTotalQuantitty(),mEventArrayList.get(position).getUnitPrice(),mEventArrayList.get(position).getBillAmount());
        }else if (type.equalsIgnoreCase(mCategoryList.get(2))){
            description = mContext.getResources().getString(R.string.food_description,tecEntry.getDescription(),tecEntry.getTotalQuantitty(),tecEntry.getUnitPrice(),tecEntry.getBillAmount());
        }else if (type.equalsIgnoreCase(mCategoryList.get(3)) || type.equalsIgnoreCase(mCategoryList.get(4))|| type.equalsIgnoreCase(mCategoryList.get(5)) || type.equalsIgnoreCase(mCategoryList.get(8))){
            description = mContext.getResources().getString(R.string.misc_description,tecEntry.getDate(),tecEntry.getDescription(),tecEntry.getBillAmount());
        }else if (type.equalsIgnoreCase(mCategoryList.get(6))){
            description = mContext.getResources().getString(R.string.food_description,tecEntry.getDate(),tecEntry.getKiloMeter(),tecEntry.getMileage(),tecEntry.getBillAmount());
        }else if (type.equalsIgnoreCase(mCategoryList.get(7))){
            String vendor = tecEntry.getPaidTo().length() >15? tecEntry.getPaidTo().substring(0,15):tecEntry.getPaidTo();
            description = "Bid - " + tecEntry.getBookingId() + " | Pid- " + tecEntry.getPaymentId() + " | " +
                    vendor + " | " + tecEntry.getDepartureDate() + " To " + tecEntry.getArrivalDate() + " | " +
                    mContext.getResources().getString(R.string.double_value, tecEntry.getBillAmount());
        }else {
            String fileName = tecEntry.getAttachmentPath();
            if (!fileName.isEmpty())
                description = fileName.substring(fileName.lastIndexOf("/") + 1);
        }

        holder.paidBy.setBackground(mContext.getResources().getDrawable(tecEntry.getPaidBy().equalsIgnoreCase(mContext.getResources().getString(R.string.employee))?R.drawable.ic_emp_amount:R.drawable.ic_account_amount));
        holder.description.setText(description);
        holder.isAttachment.setVisibility(tecEntry.getAttachmentPath().isEmpty()?View.GONE:View.VISIBLE);

        if (mItemClickListener != null){
            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemSelected(holder,headerPosition,holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mEventArrayList.size();
    }

    public void SetOnItemClickListener(ViewHeaderListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}