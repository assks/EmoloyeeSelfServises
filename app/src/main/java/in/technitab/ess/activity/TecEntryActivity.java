package in.technitab.ess.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.TecEntryHeaderAdapter;
import in.technitab.ess.adapter.TecTripBookingAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.NewTec;
import in.technitab.ess.model.NewTecEntry;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.TecEntryResponse;
import in.technitab.ess.model.TecTripBooking;
import in.technitab.ess.model.TecTripResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.DateCal;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TecEntryActivity extends AppCompatActivity implements ViewHeaderListener,TecTripBookingAdapter.TecTripBookingListener {

    @BindView(R.id.tecRecyclerView)
    RecyclerView tecRecyclerView;

    @BindView(R.id.employee_amount)
    TextView employeeAmount;
    @BindView(R.id.account_amount)
    TextView accountAmount;
    @BindView(R.id.travel_end_date)
    EditText travelEndDate;
    @BindView(R.id.user_note)
    EditText userNote;
    @BindView(R.id.userLayout)
    LinearLayout userLayout;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.bookingRecyclerView)
    RecyclerView bookingRecyclerView;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;

    private Resources resources;
    private ArrayList<TecEntryResponse> mTecEntryArrayList;
    private TecEntryHeaderAdapter adapter;
    private ArrayList<TecTripBooking> mBookings;
    private TecTripBookingAdapter bookingAdapter;
    private String status = "";
    private NewTec newTec;
    private int RC_ADD = 1, RC_EDIT = 2;
    private List<String> mTecArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tec_entry);
        ButterKnife.bind(this);

        init();

        initRecyclerView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newTec = bundle.getParcelable(ConstantVariable.Tec.ID);
            if (newTec != null) {
                status = newTec.getStatus();
                showHide();
                fetchTecEntry(newTec.getTecId());

            }

            setToolbar();
        }

        new CustomDate(travelEndDate, this, null, DateCal.getDate(System.currentTimeMillis()));
    }


    private void init() {
        connection = new NetConnection();
        resources = getResources();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        mTecEntryArrayList = new ArrayList<>();
        mTecArrayList = new ArrayList<>();
        mBookings = new ArrayList<>();
        mTecArrayList = Arrays.asList(resources.getStringArray(R.array.tecAttachmentArray));
    }

    private void showHide() {
        if (status.equalsIgnoreCase(resources.getString(R.string.draft))) {
            userLayout.setVisibility(View.VISIBLE);
        } else {
            userLayout.setVisibility(View.GONE);
        }
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.filled_tec, newTec.getTecId()));
            actionBar.setSubtitle(newTec.getProjectName());
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void initRecyclerView() {
        tecRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tecRecyclerView.setHasFixedSize(false);
        tecRecyclerView.setNestedScrollingEnabled(false);
        adapter = new TecEntryHeaderAdapter(mTecEntryArrayList);
        tecRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(this);


        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bookingRecyclerView.setHasFixedSize(false);
        bookingRecyclerView.setNestedScrollingEnabled(false);
        bookingAdapter = new TecTripBookingAdapter(mBookings);
        bookingRecyclerView.setAdapter(bookingAdapter);
        bookingAdapter.setListener(this);
    }

    private void fetchTecEntry(int tecId) {
        mBookings.clear();
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            Call<TecTripResponse> call = api.fetchTECEntryList(tecId,newTec.getTripId(),"tec entry");
            call.enqueue(new Callback<TecTripResponse>() {
                @Override
                public void onResponse(@NonNull Call<TecTripResponse> call, @NonNull Response<TecTripResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        TecTripResponse stringResponse = response.body();

                        if (stringResponse != null) {

                            if (!mTecEntryArrayList.isEmpty()) {
                                mTecEntryArrayList.clear();

                            }
                            mTecEntryArrayList.addAll(stringResponse.getTecEntrys());
                            mBookings.addAll(stringResponse.getTripBookings());
                            bookingAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                            showHide();
                            calculateAmount(mTecEntryArrayList);

                        }
                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TecTripResponse> call, @NonNull Throwable t) {
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

    private void calculateAmount(ArrayList<TecEntryResponse> mTecEntryArrayList) {
        double empAmount = 0, account = 0;
        for (TecEntryResponse tecResponse : mTecEntryArrayList) {
            for (NewTecEntry newTecEntry : tecResponse.getTecArrayList()) {
                if (newTecEntry.getPaidBy().equalsIgnoreCase(resources.getString(R.string.employee)))
                    empAmount = empAmount + newTecEntry.getBillAmount();
                else
                    account = account + newTecEntry.getBillAmount();
            }
        }
        employeeAmount.setText(resources.getString(R.string.emp_bill, empAmount));
        accountAmount.setText(resources.getString(R.string.acc_bill, account));
        totalAmount.setText(resources.getString(R.string.tec_total_amount, empAmount + account));
    }


    private Double getTotalAmount() {
        double empAmount = 0;
        for (TecEntryResponse tecResponse : mTecEntryArrayList) {
            for (NewTecEntry newTecEntry : tecResponse.getTecArrayList()) {
                empAmount = empAmount + newTecEntry.getBillAmount();
            }
        }
        return empAmount;
    }

    @OnClick(R.id.user_submit)
    protected void onSubmit() {

        String travelDate = travelEndDate.getText().toString().trim();
        double totalAmount = getTotalAmount();

        if (travelDate.isEmpty()) {
            showMessage("Claim end date is required");
        } else if (totalAmount < 1) {
            showMessage("Total amount can't zero");
        } else if (totalAmount < 1) {
            showMessage("You can't claim for this amount. Take suggestion from HR");
        } else {
            if (isAllFileAttached()) {
                proceed(travelDate);
            } else {
                showWarning(travelDate);
            }
        }
    }

    private void showWarning(final String travelDate) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(Html.fromHtml(resources.getString(R.string.submit_warning)));
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.proceed), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                proceed(travelDate);
            }
        });
        builder.show();
    }


    private boolean isAllFileAttached() {
        int requiredCategoryCount = 0;
        for (TecEntryResponse t : mTecEntryArrayList) {
            if (mTecArrayList.contains(t.getCategory())) {
                requiredCategoryCount++;
            }
        }
        return requiredCategoryCount == 5;
    }


    private void proceed(String tecEndDate) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            ArrayList<TecTripBooking> bookings = new ArrayList<>();
            for (TecTripBooking tripBooking : mBookings){
                if (tripBooking.isSelected()){
                    bookings.add(tripBooking);
                }
            }

            Gson gson = new Gson();
            String bookingJson = gson.toJson(bookings);

            Call<StringResponse> call = api.userSubmitTEC( userPref.getUserId(), newTec.getTripId(),newTec.getTecId(), newTec.getProjectName(), userNote.getText().toString().trim(), tecEndDate,bookingJson,"submit tec");
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!stringResponse.isError()) {
                                showMessage("Status Change");
                                newTec.setStatus(resources.getString(R.string.submit));
                                status = "submit";
                                showHide();
                                invalidateOptionsMenu();
                            } else {
                                showMessage("Status not change");
                            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance_leave, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setVisible(status.equalsIgnoreCase(resources.getString(R.string.draft)));
        menuItem.setTitle(getResources().getString(R.string.fill));
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setVisible(status.equalsIgnoreCase(resources.getString(R.string.draft)));
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            Intent intent = new Intent(this, TecClaimActivity.class);
            intent.putExtra(resources.getString(R.string.tec), newTec);
            startActivityForResult(intent, RC_ADD);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ADD && resultCode == Activity.RESULT_OK && data != null) {
            int tecId = data.getIntExtra(ConstantVariable.Tec.TEC_ID, 0);
            if (tecId > 0) {
                fetchTecEntry(tecId);
            }
        } else if (requestCode == RC_EDIT && resultCode == Activity.RESULT_OK && data != null) {
            NewTecEntry tecEntry = data.getParcelableExtra(resources.getString(R.string.tec_entry));
            int headerPosition = data.getIntExtra(ConstantVariable.MIX_ID.HEADER_POSITION, -1);
            int position = data.getIntExtra(ConstantVariable.MIX_ID.ID, -1);
            String action = data.getStringExtra(ConstantVariable.MIX_ID.ACTION);

            double prevAmount = mTecEntryArrayList.get(headerPosition).getTecArrayList().get(position).getBillAmount();
            double totalAmount = mTecEntryArrayList.get(headerPosition).getTotalAmount();

            if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE)) {
                double newAmount = tecEntry.getBillAmount();
                updateAmount(totalAmount, prevAmount, newAmount, headerPosition);
                mTecEntryArrayList.get(headerPosition).getTecArrayList().set(position, tecEntry);
                calculateAmount(mTecEntryArrayList);
                adapter.notifyDataSetChanged();
            } else if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.DELETE)) {
                fetchTecEntry(tecEntry.getTecId());
            }
        }
    }

    private void updateAmount(double totalAmount, double prevAmount, double newAmount, int headerPosition) {
        double diffAmount;
        if (prevAmount > newAmount) {
            diffAmount = prevAmount - newAmount;
            totalAmount = totalAmount - diffAmount;
        } else {
            diffAmount = newAmount - prevAmount;
            totalAmount = totalAmount + diffAmount;
        }

        mTecEntryArrayList.get(headerPosition).setTotalAmount(totalAmount);
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {

        if (status.equalsIgnoreCase(resources.getString(R.string.draft))) {
            if (mTecEntryArrayList.get(headerPosition).getTecArrayList().get(position).getCreatedById() == Integer.parseInt(userPref.getUserId())) {
                Intent intent = new Intent(this, EditTecClaimActivity.class);
                intent.putExtra(ConstantVariable.MIX_ID.HEADER_POSITION, headerPosition);
                intent.putExtra(resources.getString(R.string.tec), newTec);
                intent.putExtra(ConstantVariable.MIX_ID.ID, position);
                intent.putExtra(ConstantVariable.MIX_ID.ACTION, ConstantVariable.MIX_ID.UPDATE);
                intent.putExtra(ConstantVariable.Tec.TEC_ID, newTec.getTecId());
                intent.putExtra(ConstantVariable.Tec.BASE_LOCATION, newTec.getBaseLocation());
                intent.putExtra(ConstantVariable.Tec.SITE_LOCATION, newTec.getSiteLocation());
                intent.putExtra(ConstantVariable.Tec.PROJECT_NAME, newTec.getProjectName());
                intent.putExtra(ConstantVariable.MIX_ID.PROJECT_ID, newTec.getProjectId());
                intent.putExtra(ConstantVariable.Tec.CREATED_BY_ID, newTec.getCreatedById());
                intent.putExtra(resources.getString(R.string.tec_entry), mTecEntryArrayList.get(headerPosition).getTecArrayList().get(position));
                startActivityForResult(intent, RC_EDIT);
            } else {
                showMessage("You can't edit this entry because entry filled by admin.");
            }
        } else {
            showMessage("You can't edit Tec because tec is submitted");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRootClick(RecyclerView.ViewHolder viewHolder, int position) {
        TecTripBooking tripBooking = mBookings.get(position);
        if (tripBooking.getBookingAttachment().isEmpty()){
            showMessage("Bill has not uploaded on this booking.");
        }else{
            mBookings.get(position).setSelected(!tripBooking.isSelected());
            bookingAdapter.notifyItemChanged(position);
        }
    }
}
