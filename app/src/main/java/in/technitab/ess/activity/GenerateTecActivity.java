package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.TecTripBookingAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.AddResponse;
import in.technitab.ess.model.TecTripBooking;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateTecActivity extends AppCompatActivity implements TecTripBookingAdapter.TecTripBookingListener {

    @BindView(R.id.projectName)
    TextView projectName;
    @BindView(R.id.base_location)
    EditText baseLocation;
    @BindView(R.id.travel_date)
    EditText travelDate;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.site_location)
    EditText siteLocation;
    @BindView(R.id.tripBookingRecyclerView)
    RecyclerView tripBookingRecyclerView;
    private int projectId = 0, tripId = 0;
    private String strProjectName = "";
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private Resources resources;
    private ArrayList<TecTripBooking> mBookings;
    private TecTripBookingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_tec);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tripId = bundle.getInt(ConstantVariable.Tec.ID);
            projectId = bundle.getInt(ConstantVariable.Tec.PROJECT_ID);
            strProjectName = bundle.getString(getResources().getString(R.string.project));
            baseLocation.setText(userPref.getBaseLocation());
            siteLocation.setText(bundle.getString(ConstantVariable.Project.SITE_LOCATION));
            projectName.setText(strProjectName);
            travelDate.setText(bundle.getString(ConstantVariable.Trip.START_DATE));
        }
        setToolbar();
        CustomDate customDate = new CustomDate(travelDate, this, null, null);
        setRecyclerView();
        getTecTripBooking(tripId);

    }

    private void init() {
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        resources = getResources();
        api = APIClient.getClient().create(RestApi.class);
        mBookings = new ArrayList<>();
    }

    private void setRecyclerView() {
        tripBookingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tripBookingRecyclerView.setHasFixedSize(false);
        tripBookingRecyclerView.setNestedScrollingEnabled(false);
        adapter = new TecTripBookingAdapter(mBookings);
        tripBookingRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.create_tec));
            actionBar.setSubtitle(strProjectName);
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void getTecTripBooking(int tripId) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<TecTripBooking>> call = api.fetchTripBookingForTec("trip bookings",tripId);
            call.enqueue(new Callback<ArrayList<TecTripBooking>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TecTripBooking>> call, @NonNull Response<ArrayList<TecTripBooking>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<TecTripBooking> assignProject = response.body();
                        if (assignProject != null) {
                            mBookings.addAll(assignProject);
                            adapter.notifyDataSetChanged();
                        }
                    }else{
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TecTripBooking>> call, @NonNull Throwable t) {
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


    @OnClick(R.id.submit)
    protected void onSubmit() {
        final String strTravelDate = travelDate.getText().toString().trim();
        final String strBaseLocation = baseLocation.getText().toString().trim();
        final String strSitLocation = siteLocation.getText().toString().trim();

        ArrayList<TecTripBooking> bookings = new ArrayList<>();
        for (TecTripBooking tripBooking : mBookings){
            if (tripBooking.isSelected()){
                bookings.add(tripBooking);
            }
        }

        Gson gson = new Gson();
        String bookingJson = gson.toJson(bookings);

        if (invalidate(strTravelDate, strBaseLocation, strSitLocation)) {
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();
                Call<AddResponse> call = api.generateTecId("insert tec",userPref.getRoleId(), userPref.getUserId(), tripId, projectId, strTravelDate, strBaseLocation, strSitLocation,bookingJson);
                call.enqueue(new Callback<AddResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AddResponse> call, @NonNull Response<AddResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            AddResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                if (!stringResponse.isError()) {
                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                                showMessage(stringResponse.getMessage());
                            }
                        } else {
                            showMessage(resources.getString(R.string.problem_to_connect));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddResponse> call, @NonNull Throwable t) {
                        dialog.dismissDialog();
                        if (t instanceof SocketTimeoutException) {
                            showMessage(getResources().getString(R.string.slow_internet_connection));
                        }
                    }
                });
            } else {
                showMessage(getResources().getString(R.string.internet_not_available));
            }
        }
    }

    private boolean invalidate(String strTravelDate, String strBaseLocation, String strSitLocation) {
        boolean valid = true;
        if (strTravelDate.isEmpty()) {
            showMessage("Claim start date is required");
            valid = false;
        } else if (strBaseLocation.isEmpty()) {
            showMessage("Base Location is required");
            valid = false;
        } else if (strSitLocation.isEmpty()) {
            showMessage("Site Location");
            valid = false;
        }
        return valid;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRootClick(RecyclerView.ViewHolder viewHolder, int position) {
        TecTripBooking tripBooking = mBookings.get(position);
        if (tripBooking.getBookingAttachment().isEmpty()){
            showMessage("Bill hasn't uploaded on this booking");
        }else{
            mBookings.get(position).setSelected(!tripBooking.isSelected());
            adapter.notifyItemChanged(position);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
