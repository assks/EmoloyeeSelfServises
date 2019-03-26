package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder>  implements Filterable {
    private ArrayList<Vendor> mProjectTaskArrayList;
    private ArrayList<Vendor> mProjectTaskFilterList;
    private Context context;
    private RecyclerViewItemClickListener listener;
    private String view;

    public VendorAdapter(Context context,String view, ArrayList<Vendor> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        mProjectTaskFilterList = new ArrayList<>();
        this.mProjectTaskFilterList = mProjectTaskArrayList;
        this.view =view;
        this.context = context;
    }

    public void setListener(RecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public VendorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vendor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VendorAdapter.ViewHolder holder, final int position) {
        final Vendor vendor = mProjectTaskFilterList.get(position);
        holder.vendorName.setText(vendor.getContactName());
        if (view.equalsIgnoreCase(ConstantVariable.MIX_ID.VIEW))
        holder.actionButton.setBackground(context.getResources().getDrawable(R.drawable.ic_more));
        else
            holder.actionButton.setBackground(context.getResources().getDrawable(R.drawable.ic_chevron));
    }

    @Override
    public int getItemCount() {
        return mProjectTaskFilterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.vendor_name)
        public TextView vendorName;
        @BindView(R.id.action)
        public ImageButton actionButton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.action,R.id.rootLayout})
        public void onClick(){
            listener.onClickListener(this,getAdapterPosition());
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mProjectTaskFilterList = mProjectTaskArrayList;
                } else {
                    ArrayList<Vendor> filteredList = new ArrayList<>();
                    for (Vendor row : mProjectTaskArrayList) {
                        if (row.getDistrict().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mProjectTaskFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mProjectTaskFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mProjectTaskFilterList = (ArrayList<Vendor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}