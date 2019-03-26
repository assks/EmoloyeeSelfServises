package in.technitab.ess.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripBooking;
import in.technitab.ess.viewholder.TripBookingMainHolder;
import in.technitab.ess.model.TripBookingResponse;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBookingActivity extends AppCompatActivity implements TripBookingAdapter.TripBookingListener {


    private static final int RC_NEW = 1, RC_REQUEST = 2, RC_PAYMENT = 3, RC_ENTRY = 4;
    private static final String TAG = UserBookingActivity.class.getSimpleName();
    @BindView(R.id.bookingRecyclerView)
    RecyclerView bookingRecyclerView;
    private Trip trip;
    private ArrayList<TripBookingResponse> mTripResponseArrayList;
    private TripBookingHeaderAdapter adapter;
    private ArrayList<TripMember> memberArrayList;
    private Resources resources;
    private NetConnection connection;
    private Dialog dialog;
    private UserPref userPref;
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
            memberArrayList = bundle.getParcelableArrayList(resources.getString(R.string.trip_member));
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
        userPref = new UserPref(this);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean valid;
        if (item.getItemId() == R.id.delete) {
            showDeleteDialog(trip.getId());
            valid = true;

        } else if (item.getItemId() == R.id.new_booking) {
            Intent intent = new Intent(this, TravelBookingActivity.class);
            intent.putExtra(resources.getString(R.string.trip), trip);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION,resources.getString(R.string.user));
            intent.putExtra(ConstantVariable.LIST, memberArrayList);
            startActivityForResult(intent, RC_NEW);
            valid = true;

        } else if (item.getItemId() == R.id.booking_request) {
            Intent intent = new Intent(this, TripRequestActivity.class);
            intent.putExtra(resources.getString(R.string.trip), trip);
            intent.putExtra(ConstantVariable.LIST, memberArrayList);
            startActivityForResult(intent, RC_REQUEST);
            valid = true;

        } else
            valid = super.onOptionsItemSelected(item);

        return valid;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK) {
            getTripBookingList();
        }
    }

    private void showDeleteDialog(final int tripId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle(resources.getString(R.string.delete));
        builder.setMessage(R.string.delete_message);
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.timesheet_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.timesheet_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onProceed(tripId);

            }
        });

        builder.show();

    }

    private void onProceed(int tripId) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> call = api.deleteTrip(tripId, userPref.getUserId());
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse assignProject = response.body();
                        if (assignProject != null) {
                            showMessage(assignProject.getMessage());
                        }
                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
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


    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onRootClick(RecyclerView.ViewHolder viewHolder, final int headerPosition, final int position) {

        final TripBooking tripBooking = mTripResponseArrayList.get(headerPosition).getTripBookings().get(position);
        if (tripBooking.getTrip_status().equalsIgnoreCase(ConstantVariable.TripBooking.BOOKING_DONE_STATUS)) {
            TripBookingMainHolder mainHolder = (TripBookingMainHolder) viewHolder;
            final PopupMenu popup = new PopupMenu(this, mainHolder.rootLayout);
            popup.getMenuInflater().inflate(R.menu.menu_trip, popup.getMenu());
            MenuItem tripBookingItem = popup.getMenu().findItem(R.id.trip_booking);
            tripBookingItem.setTitle(resources.getString(R.string.payment_request));

            MenuItem tripTecItem = popup.getMenu().findItem(R.id.trip_tec);
            tripTecItem.setTitle(resources.getString(R.string.payment_entry));
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.trip_booking){
                        sendPaymentRequest(tripBooking,headerPosition,position);
                    }else if (item.getItemId() == R.id.trip_tec){

                        startActivityForPayment(tripBooking);
                    }
                    return true;
                }
            });

            popup.show();
        }else if (tripBooking.getTrip_status().equalsIgnoreCase(ConstantVariable.TripBooking.PAYMENT_DONE_STATUS)){
            Intent intent = new Intent(this, AttachBillActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION,resources.getString(R.string.user));
            intent.putExtra(ConstantVariable.TRIP_BOOKINGS, tripBooking);
            startActivityForResult(intent, RC_PAYMENT);
        }
    }

    private void sendPaymentRequest(TripBooking tripBooking, final int headerPosition, final int position) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> bookingRequestApi = api.paymentRequest("payment request",tripBooking.getId(),userPref.getUserId());
            bookingRequestApi.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    //dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            showMessage(stringResponse.getMessage());
                            getTripBookingList();

                        }
                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showMessage(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });
        }else{
            showMessage(resources.getString(R.string.internet_not_available));
        }
    }

    private void startActivityForPayment(TripBooking tripBooking) {
        Intent intent = new Intent(this, TripBookingPaymentActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,resources.getString(R.string.user));
        intent.putExtra(ConstantVariable.TRIP_BOOKINGS, tripBooking);
        startActivityForResult(intent, RC_PAYMENT);
    }

    private void startActivityForBooking(TripBooking tripBooking) {
        Intent intent = new Intent(this, TripRequestBookingActivity.class);
        ArrayList<TripMember> tripMembers = tripBooking.getBookingMembers();
        intent.putExtra(resources.getString(R.string.trip), trip);
        intent.putExtra(ConstantVariable.TRIP_BOOKINGS, tripBooking);
        intent.putParcelableArrayListExtra(ConstantVariable.LIST, tripMembers);
        startActivityForResult(intent, RC_ENTRY);
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
