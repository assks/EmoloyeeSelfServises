package in.technitab.ess.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

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
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminTecEntryActivity extends AppCompatActivity implements ViewHeaderListener, TecTripBookingAdapter.TecTripBookingListener {

    @BindView(R.id.tecRecyclerView)
    RecyclerView tecRecyclerView;
    @BindView(R.id.draft)
    Button draft;
    @BindView(R.id.approve)
    Button approve;
    @BindView(R.id.paid)
    Button paid;
    @BindView(R.id.employee_amount)
    TextView employeeAmount;
    @BindView(R.id.account_amount)
    TextView accountAmount;
    @BindView(R.id.userLayout)
    RelativeLayout userLayout;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.bookingRecyclerView)
    RecyclerView bookingRecyclerView;
    @BindView(R.id.uploadBookingOnTec)
    Button uploadBookingOnTec;
    @BindView(R.id.uploadBookingOnTecLaoyut)
    RelativeLayout uploadBookingOnTecLaoyut;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;

    private Resources resources;
    private ArrayList<TecEntryResponse> mTecEntryArrayList;
    private int selectedBookingCount = 0;
    private TecEntryHeaderAdapter adapter;
    private ArrayList<TecTripBooking> mBookings;
    private TecTripBookingAdapter bookingAdapter;
    private String status = "";
    private NewTec newTec;
    private int RC_ADD = 1, RC_EDIT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tec_entry);
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
    }

    private void init() {
        connection = new NetConnection();
        resources = getResources();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        mTecEntryArrayList = new ArrayList<>();
        mBookings = new ArrayList<>();
    }


    private void showHide() {
        if (mTecEntryArrayList.isEmpty()) {
            userLayout.setVisibility(View.GONE);
        } else {
            userLayout.setVisibility(View.VISIBLE);
            if (status.equalsIgnoreCase(resources.getString(R.string.submit))) {
                draft.setVisibility(View.VISIBLE);
                approve.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                paid.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
            } else if (status.equalsIgnoreCase(resources.getString(R.string.open))) {
                draft.setVisibility(View.GONE);
                approve.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                paid.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
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
        tecRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            Call<TecTripResponse> call = api.fetchTECEntryList(tecId, newTec.getTripId(), "admin_tec_entry");
            call.enqueue(new Callback<TecTripResponse>() {
                @Override
                public void onResponse(@NonNull Call<TecTripResponse> call, @NonNull Response<TecTripResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        TecTripResponse stringResponse = response.body();

                        if (stringResponse != null) {

                            if (!mTecEntryArrayList.isEmpty()) {
                                mTecEntryArrayList.clear();
                                mBookings.clear();
                                selectedBookingCount = 0;

                            }
                            mTecEntryArrayList.addAll(stringResponse.getTecEntrys());
                            adapter.notifyDataSetChanged();
                            mBookings.addAll(stringResponse.getTripBookings());

                            uploadBookingOnTecLaoyut.setVisibility(mBookings.isEmpty()?View.GONE:View.VISIBLE);

                            bookingAdapter.notifyDataSetChanged();
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


    @OnClick({R.id.approve, R.id.paid})
    protected void onSubmit(View view) {

        String message = "";
        switch (view.getId()) {
            case R.id.approve:
                status = "open";
                message = resources.getString(R.string.approve_tec_message);
                break;

            case R.id.paid:
                status = "paid";
                message = resources.getString(R.string.paid_tec_message);
                break;

        }

        showWarning(message);
    }


    private void showWarning(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(message);
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
                onProcced("", status);
            }
        });
        builder.show();
    }


    @OnClick({R.id.draft, R.id.submit})
    protected void onPrevious(final View mainView) {

        String strNotes = remark.getText().toString().trim();
        if (strNotes.isEmpty()) {
            showMessage("Remark is required");
        } else {

            if (mainView.getId() == R.id.submit) {
                status = "submit";
            } else {
                status = "draft";
            }
            onProcced(strNotes, status);
        }
    }

    private void onProcced(final String strNotes, final String status) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            Call<StringResponse> call = api.submitTEC(userPref.getUserId(), newTec.getTecId(), newTec.getTripId(), newTec.getCreatedById(), status, newTec.getProjectName(), strNotes,"admin_update_tec");
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!stringResponse.isError()) {
                                showMessage("Status Change");

                                showHide();
                                if (resources.getString(R.string.draft).equalsIgnoreCase(status) || resources.getString(R.string.submit).equalsIgnoreCase(status) || resources.getString(R.string.paid).equalsIgnoreCase(status)) {
                                    startHomeActivity();
                                } else if (resources.getString(R.string.open).equalsIgnoreCase(status)) {
                                    paid.setVisibility(View.VISIBLE);
                                    submit.setVisibility(View.VISIBLE);
                                    draft.setVisibility(View.GONE);
                                    approve.setVisibility(View.GONE);
                                }
                            } else {
                                showMessage(stringResponse.getMessage());
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

    private void startHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }, 500);
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
        menuItem.setVisible(status.equalsIgnoreCase(resources.getString(R.string.submit)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            Intent intent = new Intent(this, AdminTecClaimActivity.class);
            intent.putExtra(resources.getString(R.string.tec), newTec);
            startActivityForResult(intent, RC_ADD);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {
        if (status.equalsIgnoreCase(resources.getString(R.string.submit))) {
            Intent intent = new Intent(this, EditTecClaimActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.HEADER_POSITION, headerPosition);
            intent.putExtra(ConstantVariable.MIX_ID.ID, position);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, ConstantVariable.MIX_ID.APPROVE);
            intent.putExtra(resources.getString(R.string.tec), newTec);
            intent.putExtra(ConstantVariable.Tec.TEC_ID, newTec.getTecId());
            intent.putExtra(ConstantVariable.Tec.BASE_LOCATION, newTec.getBaseLocation());
            intent.putExtra(ConstantVariable.Tec.SITE_LOCATION, newTec.getSiteLocation());
            intent.putExtra(ConstantVariable.Tec.PROJECT_NAME, newTec.getProjectName());
            intent.putExtra(ConstantVariable.MIX_ID.PROJECT_ID, newTec.getProjectId());
            intent.putExtra(ConstantVariable.Tec.CREATED_BY_ID, newTec.getCreatedById());
            intent.putExtra(resources.getString(R.string.tec_entry), mTecEntryArrayList.get(headerPosition).getTecArrayList().get(position));
            startActivityForResult(intent, RC_EDIT);
        } else {
            Intent intent = new Intent(this, ViewTecEntry.class);
            intent.putExtra(resources.getString(R.string.tec_entry), mTecEntryArrayList.get(headerPosition).getTecArrayList().get(position));
            intent.putExtra(ConstantVariable.Tec.PROJECT_NAME, newTec.getProjectName());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EDIT && resultCode == Activity.RESULT_OK && data != null) {
            NewTecEntry tecEntry = data.getParcelableExtra(resources.getString(R.string.tec_entry));
            int headerPosition = data.getIntExtra(ConstantVariable.MIX_ID.HEADER_POSITION, -1);
            int position = data.getIntExtra(ConstantVariable.MIX_ID.ID, -1);

            if (headerPosition > -1 && position > -1) {
                double prevAmount = mTecEntryArrayList.get(headerPosition).getTecArrayList().get(position).getBillAmount();
                double newAmount = tecEntry.getBillAmount();
                double totalAmount = mTecEntryArrayList.get(headerPosition).getTotalAmount();

                double diffAmount;
                if (prevAmount > newAmount) {
                    diffAmount = prevAmount - newAmount;
                    totalAmount = totalAmount - diffAmount;
                } else {
                    diffAmount = newAmount - prevAmount;
                    totalAmount = totalAmount + diffAmount;
                }


                mTecEntryArrayList.get(headerPosition).setTotalAmount(totalAmount);

                mTecEntryArrayList.get(headerPosition).getTecArrayList().set(position, tecEntry);
                calculateAmount(mTecEntryArrayList);
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == RC_ADD && resultCode == Activity.RESULT_OK && data != null) {
            int tecId = data.getIntExtra(ConstantVariable.Tec.TEC_ID, 0);
            if (tecId > 0)
                fetchTecEntry(tecId);

        }
    }

    @Override
    public void onRootClick(RecyclerView.ViewHolder viewHolder, int position) {
        TecTripBooking tripBooking = mBookings.get(position);
        if (tripBooking.getBookingAttachment().isEmpty()) {
            showMessage("Bill has not uploaded on this booking.");
        } else {
            mBookings.get(position).setSelected(!tripBooking.isSelected());
            bookingAdapter.notifyItemChanged(position);

            if (mBookings.get(position).isSelected()) {
                selectedBookingCount++;
            } else {
                selectedBookingCount--;
            }

            onbookingSelection();
        }

    }

    private void onbookingSelection() {
        uploadBookingOnTec.setVisibility(selectedBookingCount > 0 ? View.VISIBLE : View.GONE);
    }


    @OnClick(R.id.uploadBookingOnTec)
    protected void onUploadBookingOnTec() {
        ArrayList<TecTripBooking> tripBookings = new ArrayList<>();
        for (TecTripBooking tripBooking : mBookings) {
            if (tripBooking.isSelected()) {
                tripBookings.add(tripBooking);
            }
        }

        Gson gson = new Gson();
        String bookingJson = gson.toJson(tripBookings);
        Log.d("Admin TEc ",bookingJson);

        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            Call<StringResponse> call = api.linkBookingWithTec(userPref.getUserId(), newTec.getTecId(), bookingJson, "link_booking_tec");
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!stringResponse.isError()) {
                                fetchTecEntry(newTec.getTecId());
                            } else {
                                dialog.dismissDialog();
                                showMessage(stringResponse.getMessage());
                            }
                        }
                    } else {
                        dialog.dismissDialog();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
