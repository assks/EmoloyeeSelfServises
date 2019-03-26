package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;


public class TimeSheetHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.activity_name)
    public TextView activityName;
    @BindView(R.id.time)
    public TextView time;

    public TimeSheetHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
