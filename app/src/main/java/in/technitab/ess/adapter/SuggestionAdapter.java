package in.technitab.ess.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.technitab.ess.model.Customer;
import in.technitab.ess.model.State;

public class SuggestionAdapter extends ArrayAdapter<Object> implements Filterable {
    private List<Object> mlistData;

    public SuggestionAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mlistData = new ArrayList<>();
    }

    public void setData(List<Object> list) {
        mlistData.clear();
        mlistData.addAll(list);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        if (mlistData.get(position) instanceof Customer) {
            Customer spinnerModel = (Customer) mlistData.get(position);
            label.setText(spinnerModel.getCustomerName());
        }else if(mlistData.get(position) instanceof State){
            State state = (State) mlistData.get(position);
            label.setText(state.getName());
        }else if (mlistData.get(position) instanceof String) {
            String district = String.valueOf(mlistData.get(position));
            label.setText(district);
        }
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        if (mlistData.get(position) instanceof Customer) {
            Customer spinnerModel = (Customer) mlistData.get(position);
            label.setText(spinnerModel.getCustomerName());
        }else if(mlistData.get(position) instanceof State){
            State state = (State) mlistData.get(position);
            label.setText(state.getName());
        }else if (mlistData.get(position) instanceof String) {
            String district = String.valueOf(mlistData.get(position));
            label.setText(district);
        }
        return label;
    }


    @Override
    public int getCount() {
        return mlistData.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mlistData.get(position);
    }

    /**
     * Used to Return the full object directly from adapter.
     *
     * @param position
     * @return
     */
    public Object getObject(int position) {
        return mlistData.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mlistData;
                    filterResults.count = mlistData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return dataFilter;
    }
}

