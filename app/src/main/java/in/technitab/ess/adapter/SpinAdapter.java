package in.technitab.ess.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import in.technitab.ess.model.BookingMode;
import in.technitab.ess.model.Customer;

public class SpinAdapter extends ArrayAdapter<Object> {
    private List<Object> values;

    public SpinAdapter(Context context, int textViewResourceId,
                       List<Object> values) {
        super(context, textViewResourceId, values);
        this.values = values;
    }


    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setPadding(2, label.getPaddingTop(), label.getPaddingRight(), label.getPaddingBottom());

        label.setTextColor(Color.BLACK);
        if (values.get(position) instanceof String){
            String value = (String) values.get(position);
            label.setText(value);
        }else if (values.get(position) instanceof Customer){
            Customer customer = ((Customer) values.get(position));
            label.setText(customer.getCustomerName());
        }else if (values.get(position) instanceof BookingMode){
            BookingMode bookingMode = (BookingMode) values.get(position);
            label.setText(bookingMode.getTitle());
        }
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        if (values.get(position) instanceof String){
            String value = (String) values.get(position);
            label.setText(value);
        }else if (values.get(position) instanceof Customer){
            Customer customer = ((Customer) values.get(position));
            label.setText(customer.getCustomerName());
        }else if (values.get(position) instanceof BookingMode){
            BookingMode bookingMode = (BookingMode) values.get(position);
            label.setText(bookingMode.getTitle());
        }

        return label;
    }
}

