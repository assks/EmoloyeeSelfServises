package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.TripBookingAdapter;
import in.technitab.ess.adapter.TripBookingHeaderAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripBooking;
import in.technitab.ess.model.TripBookingResponse;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTravelBookingActivity extends AppCompatActivity implements TripBookingAdapter.TripBookingListener{

    private static final int RC_NEW = 1;
    @BindView(R.id.bookingRecyclerView)
    RecyclerView bookingRecyclerView;
    private Trip trip;
    private ArrayList<TripBookingResponse> mTripResponseArrayList;
    private TripBookingHeaderAdapter adapter;
    private ArrayList<TripMember> memberArrayList;
    private Resources resources;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            trip = bundle.getParcelable(resources.getString(R.string.trip));
            memberArrayList = bundle.getParcelableArrayList(ConstantVariable.LIST);
            setToolbar();
        }

        setRecyclerView();
        getTripBookingList();
    }

    private void getTripBookingList() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<TripBookingResponse>> call = api.fetchTripBooking(trip.getId());
            call.enqueue(new Callback<ArrayList<TripBookingResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TripBookingResponse>> call, @NonNull Response<ArrayList<TripBookingResponse>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<TripBookingResponse> assignProject = response.body();
                        if (assignProject != null) {

                            if (!mTripResponseArrayList.isEmpty()) {
                                mTripResponseArrayList.clear();
                            }

                            mTripResponseArrayList.addAll(assignProject);
                            adapter.notifyDataSetChanged();
                            if (mTripResponseArrayList.isEmpty()) {
                                showMessage(resources.getString(R.string.no_booking_history_found));
                            }
                        }
                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TripBookingResponse>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showMessage(resources.getString(R.string.slow_internet_connection));
                    }

                }
            });
        } else {
            showMessage(resources.getString(R.string.internet_not_available));
        }
    }

    private void init() {
        resources = getResources();
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        resources = getResources();
        memberArrayList = new ArrayList<>();
        mTripResponseArrayList = new ArrayList<>();
        trip = new Trip();
    }


    private void setRecyclerView() {
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingRecyclerView.setHasFixedSize(false);
        bookingRecyclerView.setNestedScrollingEnabled(false);
        adapter = new TripBookingHeaderAdapter(resources.getInteger(R.integer.user), mTripResponseArrayList);
        bookingRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(this);
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.table_trip_bookings, trip.getId()));
            actionBar.setSubtitle(trip.getProjectName());
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.travel_booking, menu);
        MenuItem deleteBooking = menu.findItem(R.id.delete);
        MenuItem requestBooking = menu.findItem(R.id.booking_request);
        deleteBooking.setVisible(false);
        requestBooking.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_booking) {
            Intent intent = new Intent(this, TravelBookingActivity.class);
            intent.putExtra(resources.getString(R.string.trip), trip);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION,resources.getString(R.string.admin));
            intent.putParcelableArrayListExtra(ConstantVariable.LIST, memberArrayList);
            startActivityForResult(intent, RC_NEW);
            return true;

        }  else
            return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getTripBookingList();
        }
    }


    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }



    @Override
    public void onRootClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {
        TripBooking tripBooking = mTripResponseArrayList.get(headerPosition).getTripBookings().get(position);
        if(tripBooking.getTrip_status().equalsIgnoreCase(ConstantVariable.TripBooking.BOOKING_REQUESTED_STATUS)){
            startActivityForBooking(tripBooking);

        }else if (tripBooking.getTrip_status().equalsIgnoreCase(ConstantVariable.TripBooking.PAYMENT_REQUESTED_STATUS) || (tripBooking.getTrip_status().equalsIgnoreCase(ConstantVariable.TripBooking.BOOKING_DONE_STATUS) && tripBooking.getCreatedBy().equalsIgnoreCase(resources.getString(R.string.admin)))){
            startActivityForPayment(tripBooking);
        }else if (tripBooking.getTrip_status().equalsIgnoreCase(ConstantVariable.TripBooking.PAYMENT_DONE_STATUS)){
            Intent intent = new Intent(this, AttachBillActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION,resources.getString(R.string.user));
            intent.putExtra(ConstantVariable.TRIP_BOOKINGS, tripBooking);
            startActivityForResult(intent,1);
        }else{
            showMessage("You can't perfom action");
        }
    }

    private void startActivityForPayment(TripBooking tripBooking) {
        Intent intent = new Intent(this,TripBookingPaymentActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,resources.getString(R.string.admin));
        intent.putExtra(ConstantVariable.TRIP_BOOKINGS,tripBooking);
        startActivityForResult(intent,1);
    }

    private void startActivityForBooking(TripBooking tripBooking) {
        Intent intent = new Intent(this,TripRequestBookingActivity.class);
        ArrayList<TripMember> tripMembers = tripBooking.getBookingMembers();
        intent.putExtra(resources.getString(R.string.trip),trip);
        intent.putExtra(ConstantVariable.TRIP_BOOKINGS,tripBooking);
        intent.putParcelableArrayListExtra(ConstantVariable.LIST,tripMembers);
        startActivityForResult(intent,1);
    }


    @Override
    public void onEditClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {

    }

    @Override
    public void onDeleteClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}