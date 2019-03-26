package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;

public class NewTecMainHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.rootLayout)
    public RelativeLayout rootLayout;
    @BindView(R.id.dateLayout)
    public LinearLayout dateLayout;
    @BindView(R.id.date)
    public TextView date;
    @BindView(R.id.from_to)
    public TextView fromTo;
    @BindView(R.id.status)
    public ImageView status;
    @BindView(R.id.user_note)
    public TextView userNotes;
    @BindView(R.id.month)
    public TextView month;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.type)
    public TextView type;
    @BindView(R.id.remark)
    public TextView remark;
    @BindView(R.id.descriptionLayout)
    public LinearLayout descriptionLayout;


    public NewTecMainHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }


    @OnClick(R.id.status)
    public void onStatusClick(){
        if (descriptionLayout.getVisibility() == View.GONE){
            descriptionLayout.setVisibility(View.VISIBLE);
            status.animate().rotation(180).start();
        }else{
            descriptionLayout.setVisibility(View.GONE);
            status.animate().rotation(0).start();
        }
    }
}
