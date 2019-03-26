package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Customer;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>  implements Filterable {
    private ArrayList<Customer> mCustomerArrayList;
    private ArrayList<Customer> mCustomerFilterList;
    private Context context;
    private RecyclerViewItemClickListener listener;

    public CustomerAdapter(Context context, ArrayList<Customer> mCustomerArrayList) {
        this.mCustomerArrayList = mCustomerArrayList;
        mCustomerFilterList = new ArrayList<>();
        this.mCustomerFilterList = mCustomerArrayList;
        this.context = context;
    }

    public void setListener(RecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerAdapter.CustomerViewHolder holder, final int position) {
        final Customer customer = mCustomerFilterList.get(position);
        holder.clientName.setText(customer.getCustomerName());
       // holder.clientLocation.setText(context.getResources().getString(R.string.client_location,customer.getCountry(),customer.getState(),customer.getDistrict()));
    }

    @Override
    public int getItemCount() {
        return mCustomerFilterList.size();
    }


    public class CustomerViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.clientName)
        public TextView clientName;
        @BindView(R.id.clientLocation)
        public TextView clientLocation;
        @BindView(R.id.rootLayout)
        public LinearLayout rootLayout;
        CustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.rootLayout)
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
                    mCustomerFilterList = mCustomerArrayList;
                } else {
                    ArrayList<Customer> filteredList = new ArrayList<>();
                    for (Customer row : mCustomerArrayList) {
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mCustomerFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mCustomerFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mCustomerFilterList = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}