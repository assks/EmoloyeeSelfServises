package in.technitab.ess.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import in.technitab.ess.R;

public class SetTime implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private EditText editText;
    private Calendar myCalendar;
    private Context context;

    public SetTime(EditText editText, Context ctx){
        this.editText = editText;
        this.context = ctx;
        this.editText.setOnClickListener(this);
        this.myCalendar = Calendar.getInstance();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.editText.setText(context.getResources().getString(R.string.min_hours_value,hourOfDay,minute));
    }


    @Override
    public void onClick(View view) {
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        new TimePickerDialog(context, this, hour, minute, true).show();
    }

}
