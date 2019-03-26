package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.VendorAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingVendorActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.vendorRecyclerView)
    RecyclerView vendorRecyclerView;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private Resources resources;
    private VendorAdapter adapter;
    private ArrayList<Vendor> mVendorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_vendor);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initRecyclerView();
            String title = bundle.getString(ConstantVariable.MIX_ID.TITLE);
            setToolbar(title);
            int projectId = bundle.getInt(ConstantVariable.Project.ID);
            String bookingMode = bundle.getString(ConstantVariable.Booking.BOOKING_MODE);
            String travelType = bundle.getString(ConstantVariable.Booking.TRAVEL_TYPE);
            fetVendorList(projectId, bookingMode, travelType);
        }
    }

    private void setToolbar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void init() {
        resources = getResources();
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
    }

    private void initRecyclerView() {
        vendorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vendorRecyclerView.setHasFixedSize(true);
        vendorRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mVendorList = new ArrayList<>();
        adapter = new VendorAdapter(this, ConstantVariable.MIX_ID.ACTION, mVendorList);
        vendorRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }

    private void fetVendorList(int projectId, String bookingMode, String travelType) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Vendor>> call = api.fetchTripVendorList(projectId, bookingMode, travelType);
            call.enqueue(new Callback<ArrayList<Vendor>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Vendor>> call, @NonNull Response<ArrayList<Vendor>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<Vendor> list = response.body();
                        if (list != null) {
                            mVendorList.addAll(list);
                            adapter.notifyDataSetChanged();
                            if (mVendorList.isEmpty()) {
                                showToast("No vendor created yet");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Vendor>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {

                        showToast(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(resources.getString(R.string.internet_not_available));
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VendorAdapter.ViewHolder) {
            Intent intent = new Intent();
            intent.putExtra(ConstantVariable.Vendor.DISPLAY_NAME, mVendorList.get(position).getContactName());
            intent.putExtra(ConstantVariable.Vendor.DISTRICT, mVendorList.get(position).getPlaceOfSupply());
            intent.putExtra(ConstantVariable.Vendor.RATE, mVendorList.get(position).getRate());
            intent.putExtra(ConstantVariable.MIX_ID.ID, mVendorList.get(position).getId());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
