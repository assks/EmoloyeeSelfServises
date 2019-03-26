package in.technitab.ess.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import in.technitab.ess.R;
import in.technitab.ess.adapter.LeaveAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.MyLeaves;
import in.technitab.ess.model.ParticularLeave;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveListActivity extends AppCompatActivity implements LeaveAdapter.LeaveListener {

    @BindView(R.id.leave_recycler_view)
    RecyclerView leaveRecyclerView;
    private LeaveAdapter adapter;
    private List<MyLeaves> myLeavesArrayList;
    private UserPref pref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private int LEAVE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        ButterKnife.bind(this);

        setToolbar();
        initialise();
        getLeaveList();

    }

    private void getLeaveList() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<List<MyLeaves>> call = api.getLeaveList(pref.getUserId());
            call.enqueue(new Callback<List<MyLeaves>>() {
                @Override
                public void onResponse(@NonNull Call<List<MyLeaves>> call, @NonNull Response<List<MyLeaves>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        myLeavesArrayList = response.body();
                        if (myLeavesArrayList != null) {
                            adapter = new LeaveAdapter(LeaveListActivity.this, myLeavesArrayList);
                            leaveRecyclerView.setAdapter(adapter);
                            adapter.setItemSelectedListener(LeaveListActivity.this);
                        } else {
                            showMessage("No leave history");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<MyLeaves>> call, @NonNull Throwable t) {
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

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void initialise() {
        pref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);

        leaveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaveRecyclerView.setHasFixedSize(true);
        myLeavesArrayList = new ArrayList<>();
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LEAVE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            myLeavesArrayList.clear();
            getLeaveList();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request_leave, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_request) {
            startActivityForResult(new Intent(this, LeaveActivity.class), LEAVE_REQUEST);
            return true;
        } else
            return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel leave request");
        builder.setMessage("Are you wish to cancel leave");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onCancelLeave(position);
            }
        });
        builder.setNegativeButton("Don't Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void onCancelLeave(final int position) {

        ArrayList<ParticularLeave> mLeaves = myLeavesArrayList.get(position).getLeaveArrayList();
        if (mLeaves.size() > 0) {

            String startDate = mLeaves.get(0).getDate();
            Date appliedDate = getDate(startDate);

            int cancelStatus = getResources().getInteger(R.integer.cancel);
            if (new Date().compareTo(appliedDate) <= 0) {
                if (connection.isNetworkAvailable(this)) {
                    Call<StringResponse> call = api.cancelRequest(pref.getUserId(),pref.getUserId(),cancelStatus, myLeavesArrayList.get(position).getId());
                    call.enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                            if (response.isSuccessful()) {
                                StringResponse stringResponse = response.body();
                                if (stringResponse != null) {
                                    if (!stringResponse.isError()) {
                                        myLeavesArrayList.get(position).setStatus(getResources().getString(R.string.canceled));
                                        adapter.notifyItemChanged(position);
                                    }
                                    showMessage(stringResponse.getMessage());
                                }
                            } else if (response.code() == getResources().getInteger(R.integer.not_found)) {
                                showMessage(getResources().getString(R.string.server_not_connected));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                            if (t instanceof SocketTimeoutException) {
                                showMessage(getResources().getString(R.string.server_not_connected));
                            }
                        }
                    });
                } else {
                    showMessage(getResources().getString(R.string.internet_not_available));
                }
            }else{
                showMessage("Please drop mail. You can't cancel leave");
            }
        }
    }

    private Date getDate(String strDate){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
