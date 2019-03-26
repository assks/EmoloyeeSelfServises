package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class AddViewHolder extends RecyclerView.ViewHolder {

    public View view;
    @BindView(R.id.newEntry)
    public TextView newEntry;

    public AddViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        ButterKnife.bind(this,itemView);
    }
}
