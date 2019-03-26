package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class FixAssetHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.action)
    public ImageView action;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.asset_image)
    public ImageView assetImage;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.rootLayout)
    public RelativeLayout rootLayout;

    public FixAssetHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
