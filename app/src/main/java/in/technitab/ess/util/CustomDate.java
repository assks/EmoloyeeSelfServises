package in.technitab.ess.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import in.technitab.ess.R;

public class CustomDate implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText editText;
    private Calendar myCalendar;
    private Context context;
    private String minDate,maxDate;
    private CustomDateListener listener;

    public CustomDate(EditText editText, Context ctx,String minDate,String maxDate){
        this.editText = editText;
        this.context = ctx;
        this.editText.setOnClickListener(this);
        this.myCalendar = Calendar.getInstance();
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    public void setDateSetListener(CustomDateListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int date = myCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog =  new DatePickerDialog(context, this, year, month, date);
        if (minDate != null) {
            datePickerDialog.getDatePicker().setMinDate(DateCal.getLongDate(minDate));
        }

        if (maxDate != null){
            datePickerDialog.getDatePicker().setMaxDate(DateCal.getLongDate(maxDate));
        }
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        editText.setText(context.getResources().getString(R.string.date_text, year, (month + 1), date));

        if (listener != null){
            listener.onDateSetListener(editText.getText().toString().trim());
        }
    }


    public interface CustomDateListener {
        void onDateSetListener(String date);
    }
}
