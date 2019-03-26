package in.technitab.ess.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import in.technitab.ess.R;

public class CustomTime implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private EditText editText;
    private Context context;
    private TimePickerDialog timePickerDialog;

    public CustomTime(EditText editText, Context ctx){
        this.editText = editText;
        this.context = ctx;
        this.editText.setOnClickListener(this);
        Calendar myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(context, this, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.editText.setText(context.getResources().getString(R.string.min_hours_value,hourOfDay,minute));
    }

    @Override
    public void onClick(View view) {
        timePickerDialog.show();
    }

}
