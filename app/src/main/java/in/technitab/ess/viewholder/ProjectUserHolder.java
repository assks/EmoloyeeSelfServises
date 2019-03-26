package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class ProjectUserHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.projectName)
    public TextView projectName;
    @BindView(R.id.projectType)
    public TextView projectType;
    @BindView(R.id.add_tag)
    public ImageButton addTag;

    public ProjectUserHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
