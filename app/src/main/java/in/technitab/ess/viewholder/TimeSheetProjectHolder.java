package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class TimeSheetProjectHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.activity_name)
    public TextView activityName;

    @BindView(R.id.time_spent)
    public TextView timeSpent;

    @BindView(R.id.project)
    public TextView project;

    public TimeSheetProjectHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
