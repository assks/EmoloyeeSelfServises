package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.technitab.ess.R;
import in.technitab.ess.model.User;

public class UserAutoCompleteAdapter extends ArrayAdapter<User> implements Filterable {

    private ArrayList<User> fullList;
    private ArrayList<User> mOriginalValues;
    private ArrayFilter mFilter;

    public UserAutoCompleteAdapter(Context context, List<User> objects) {

        super(context, 0,objects);
        fullList = (ArrayList<User>) objects;
        mOriginalValues = new ArrayList<>(fullList);

    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public User getItem(int position) {
        return fullList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customer,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.customerName);
        textView.setText(mOriginalValues.get(position).getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<User> list = new ArrayList<>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<User> values = mOriginalValues;
                int count = values.size();

                ArrayList<User> newValues = new ArrayList<>(count);

                for (int i = 0; i < count; i++) {
                    User item = values.get(i);
                    if (item.getName().toLowerCase().contains(prefixString)) {
                        newValues.add(item);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.values!=null){
                fullList = (ArrayList<User>) results.values;
            }else{
                fullList = new ArrayList<>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}