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
import in.technitab.ess.model.User;

public class UserNameAdapter extends RecyclerView.Adapter<UserNameAdapter.ViewHolder> implements Filterable {
    private ArrayList<User> mProjectTaskArrayList;
    private ArrayList<User> mProjectTaskFilterList;
    private Context context;
    private RecyclerViewItemClickListener listener;

    public UserNameAdapter(Context context, String view, ArrayList<User> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        mProjectTaskFilterList = new ArrayList<>();
        this.mProjectTaskFilterList = mProjectTaskArrayList;
        this.context = context;
    }

    public void setListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vendor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserNameAdapter.ViewHolder holder, final int position) {
        final User vendor = mProjectTaskFilterList.get(position);
        holder.vendorName.setText(vendor.getName());
        holder.actionButton.setBackground(context.getResources().getDrawable(R.drawable.ic_chevron));
    }


    @Override
    public int getItemCount() {
        return mProjectTaskFilterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vendor_name)
        public TextView vendorName;
        @BindView(R.id.action)
        public ImageButton actionButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.action, R.id.rootLayout})
        public void onClick() {
            listener.onClickListener(this, getAdapterPosition());
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
                    ArrayList<User> filteredList = new ArrayList<>();
                    for (User row : mProjectTaskArrayList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                mProjectTaskFilterList = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}