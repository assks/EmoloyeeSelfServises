package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class ProjectTaskHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.project)
    public TextView project;
    @BindView(R.id.task_name)
    public TextView taskName;
    @BindView(R.id.time_spent)
    public EditText timeSpent;
    @BindView(R.id.description)
    public ImageView description;
    @BindView(R.id.is_billable)
    public CheckBox isBillable;

    public ProjectTaskHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
