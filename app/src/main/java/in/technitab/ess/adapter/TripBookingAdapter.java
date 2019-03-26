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

import in.technitab.ess.model.TripBooking;
import in.technitab.ess.viewholder.TripBookingMainHolder;
import in.technitab.ess.util.UserPref;



public class TripBookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TripBooking> mEventArrayList;
    private int headerAdapterPosition, viewUser;
    private TripBookingListener listener;
    private UserPref userPref;
    private Resources resources;

    TripBookingAdapter(int headerAdapterPosition, int viewUser, ArrayList<TripBooking> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.viewUser = viewUser;
        this.headerAdapterPosition = headerAdapterPosition;
    }

    public void setListener(TripBookingListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        resources = mContext.getResources();
        userPref = new UserPref(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_trip_booking_item, parent, false);
        return new TripBookingMainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final TripBookingMainHolder mainHolder = (TripBookingMainHolder) holder;
        TripBooking tripBooking = mEventArrayList.get(position);
        mainHolder.travelType.setText(tripBooking.getTravel_type());
        mainHolder.dateLayout.setBackground(getBackground(tripBooking.getTravel_type()));

        hideActionButton(mainHolder,tripBooking);

        if (tripBooking.getTrip_status().equalsIgnoreCase(resources.getString(R.string.trip_booking_requested))) {
            mainHolder.bookingMode.setText(tripBooking.getUser_booking_mode());
            String strCityArea;
            String description;
            if (tripBooking.getUser_booking_mode().equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode)) || tripBooking.getUser_booking_mode().equalsIgnoreCase(resources.getString(R.string.booking_guesthouse))) {
                strCityArea = tripBooking.getUser_city_area();
                description = resources.getString(R.string.user_trip_description, tripBooking.getId(), getYMDDate(tripBooking.getUser_check_in()), getYMDDate(tripBooking.getUser_check_out()));
            } else {
                strCityArea = tripBooking.getUser_destination();
                description =  "Bid - "+ tripBooking.getId()+" | "+ tripBooking.getUser_source()+ " | "+ tripBooking.getUser_travel_date();
            }
            mainHolder.cityArea.setText(strCityArea);
            mainHolder.description.setText(description);
            mainHolder.user.setText(String.valueOf(tripBooking.getBookingMembers().size()));

        }else {
            mainHolder.bookingMode.setText(tripBooking.getAdminBookingMode());
            String strCityArea;
            String description;
            if (tripBooking.getAdminBookingMode().equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode)) || tripBooking.getUser_booking_mode().equalsIgnoreCase(resources.getString(R.string.booking_guesthouse))) {
                strCityArea = tripBooking.getAdminCityArea();
                description = resources.getString(R.string.user_trip_description, tripBooking.getId(), getYMDDate(tripBooking.getAdminCheckIn()),getYMDDate(tripBooking.getAdminCheckOut()) );
            } else {
                strCityArea = tripBooking.getAdminDestination();
                description = resources.getString(R.string.user_trip_description, tripBooking.getId(), getYMDDateForTravel(tripBooking.getAdminDeparture()),getYMDDateForTravel(tripBooking.getAdminArrival()) );
            }
            mainHolder.cityArea.setText(strCityArea);
            mainHolder.description.setText(description);
            mainHolder.user.setText(String.valueOf(tripBooking.getBookingMembers().size()));
        }

        if (!tripBooking.getBookingAttachment().isEmpty()){
            mainHolder.billAttachment.setVisibility(View.VISIBLE);
        }

        mainHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRootClick(mainHolder,headerAdapterPosition,mainHolder.getAdapterPosition());
            }
        });


        mainHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditClick(mainHolder,headerAdapterPosition,mainHolder.getAdapterPosition());
            }
        });


        mainHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClick(mainHolder,headerAdapterPosition,mainHolder.getAdapterPosition());
            }
        });
    }

    private void hideActionButton(TripBookingMainHolder mainHolder, TripBooking tripBooking) {
        if (tripBooking.getTrip_status().equalsIgnoreCase(resources.getString(R.string.trip_booking_requested)) && tripBooking.getCreated_by_id() == Integer.parseInt(userPref.getUserId())){
            mainHolder.edit.setVisibility(View.VISIBLE);
            mainHolder.delete.setVisibility(View.VISIBLE);
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

    private String getYMDDateForTravel(String claimStartDate) {
        String strClaimStart;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
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

    public interface TripBookingListener {
        void onRootClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position);

        void onEditClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position);
        void onDeleteClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position);
    }
}