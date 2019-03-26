package in.technitab.ess.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;

public class TecTripBookingMainHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.dateLayout)
    public LinearLayout dateLayout;
    @BindView(R.id.travelType)
    public TextView travelType;
    @BindView(R.id.bookingMode)
    public TextView bookingMode;
    @BindView(R.id.cityArea)
    public TextView cityArea;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.action)
    public ImageView action;
    @BindView(R.id.billAttachment)
    public ImageView billAttachment;
    @BindView(R.id.rootLayout)
    public RelativeLayout rootLayout;

    public TecTripBookingMainHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
