package in.technitab.ess.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.technitab.ess.R;
import in.technitab.ess.model.TecTripBooking;
import in.technitab.ess.viewholder.TecTripBookingMainHolder;


public class TecTripBookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TecTripBooking> mEventArrayList;
    private TecTripBookingListener listener;
    private Resources resources;

    public TecTripBookingAdapter( ArrayList<TecTripBooking> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;

    }

    public void setListener(TecTripBookingListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        resources = mContext.getResources();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tec_trip_booking, parent, false);
        return new TecTripBookingMainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final TecTripBookingMainHolder mainHolder = (TecTripBookingMainHolder) holder;
        final TecTripBooking tripBooking = mEventArrayList.get(position);
        mainHolder.travelType.setText(tripBooking.getTravelType());
        mainHolder.dateLayout.setBackground(getBackground(tripBooking.getTravelType()));

            mainHolder.bookingMode.setText(tripBooking.getAdminBookingMode());
            String strCityArea;
            String description;
            if (tripBooking.getAdminBookingMode().equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode)) || tripBooking.getAdminBookingMode().equalsIgnoreCase(resources.getString(R.string.booking_guesthouse))) {
                strCityArea = tripBooking.getAdminCityArea();
                description = "Bid: "+ tripBooking.getId()+" | Pid: "+tripBooking.getPaymentId()+" | "+ getYMDDate(tripBooking.getAdminCheckIn())+ " | "+ getYMDDate(tripBooking.getAdminCheckOut());
            } else {
                strCityArea = tripBooking.getAdminDestination();
                description =  "Bid: "+ tripBooking.getId()+" | Pid: "+tripBooking.getPaymentId()+" | "+ tripBooking.getAdminSource()+ " | "+ tripBooking.getAdminDeparture();
            }
            mainHolder.cityArea.setText(strCityArea);
            mainHolder.description.setText(description);
            mainHolder.action.setVisibility(tripBooking.isSelected()?View.VISIBLE:View.GONE);
            mainHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRootClick(mainHolder,mainHolder.getAdapterPosition());
                }
            });

            if (!tripBooking.getBookingAttachment().isEmpty()){
                mainHolder.billAttachment.setVisibility(View.VISIBLE);
            }
    }



    @Override
    public int getItemCount() {
        return mEventArrayList.size();
    }

    private Drawable getBackground(String travelType){
        int color;
        if (travelType.equalsIgnoreCase(resources.getString(R.string.onward_journey))){
            color = mContext.getResources().getColor(R.color.onward_journey);
        }else if (travelType.equalsIgnoreCase(resources.getString(R.string.intermediate_journey))){
            color = mContext.getResources().getColor(R.color.intermediate_journey);
        }else {
            color = mContext.getResources().getColor(R.color.return_journey);
        }

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.circluler_bg);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    private String getYMDDate(String claimStartDate) {
        String strClaimStart;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        try {
            Date d1 = simpleDateFormat.parse(claimStartDate);
            strClaimStart = dateFormat.format(d1);

        } catch (ParseException e) {
            e.printStackTrace();
            strClaimStart = "-";
        }
        return strClaimStart;
    }

    public interface TecTripBookingListener {
        void onRootClick(RecyclerView.ViewHolder viewHolder, int position);

    }
}