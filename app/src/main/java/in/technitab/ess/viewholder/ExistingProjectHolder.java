package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class ExistingProjectHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rootLayout)
    public RelativeLayout rootLayout;
    @BindView(R.id.status)
    public ImageView status;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.type)
    public TextView type;
    public ExistingProjectHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
