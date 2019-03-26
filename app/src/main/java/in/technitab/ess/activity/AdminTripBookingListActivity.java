package in.technitab.ess.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;

public class AdminTripBookingListActivity extends AppCompatActivity {

    private static final int RC_NEW_BOOKING = 1;
    private static final String TAG = AdminTripBookingListActivity.class.getSimpleName();
    private Trip trip;
    private ArrayList<TripMember> memberArrayList;
    private Resources resources;
    private NetConnection connection;
    private Dialog dialog;
    private UserPref userPref;
    private RestApi api;
    private int headerPosition = -1, position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_trip_booking_list);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            trip = bundle.getParcelable(resources.getString(R.string.trip));
            memberArrayList = bundle.getParcelableArrayList(ConstantVariable.LIST);
            Log.d(TAG,"size "+memberArrayList.size());
            setToolbar();
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
        trip = new Trip();
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.trip_detail));
            actionBar.setSubtitle(trip.getProjectName() + "-" + trip.getId());
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.travel_booking, menu);
        MenuItem menuItem = menu.findItem(R.id.delete);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean valid;
        if (item.getItemId() == R.id.new_booking) {
            Intent intent = new Intent(this, AdminTravelBooking.class);
            intent.putExtra(resources.getString(R.string.trip),trip);
            intent.putExtra(ConstantVariable.LIST,memberArrayList);
            startActivityForResult(intent,RC_NEW_BOOKING);
            valid = true;

        } else
            valid = super.onOptionsItemSelected(item);

        return valid;
    }



    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
