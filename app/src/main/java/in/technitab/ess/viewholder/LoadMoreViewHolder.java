package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    public LoadMoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
