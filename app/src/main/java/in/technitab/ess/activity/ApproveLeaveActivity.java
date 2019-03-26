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
import android.util.Log;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.ApproveLeaveAdapter;
import in.technitab.ess.adapter.ApproveLeaveHeaderAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.ApproveLeave;
import in.technitab.ess.model.MyLeaves;
import in.technitab.ess.model.ParticularLeave;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveLeaveActivity extends AppCompatActivity implements ApproveLeaveAdapter.LeaveListener {

    private static final String TAG = ApproveLeaveActivity.class.getSimpleName();
    @BindView(R.id.leaveRecyclerView)
    RecyclerView leaveRecyclerView;
    @BindView(R.id.empty_leave)
    TextView emptyLeave;

    private ArrayList<ApproveLeave> mApproveLeaveArrayList;
    private ApproveLeaveHeaderAdapter mAdapter;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private Resources resources;
    private int headerPosition, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);
        ButterKnife.bind(this);

        init();
        setToolbar();
        initRecylcerView();
        getLeaveList();
    }

    private void getLeaveList() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<ApproveLeave>> call = api.fetchApproveLeaves(userPref.getUserId());
            call.enqueue(new Callback<ArrayList<ApproveLeave>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ApproveLeave>> call, @NonNull Response<ArrayList<ApproveLeave>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "response " + response.body());
                        ArrayList<ApproveLeave> list = response.body();
                        if (list != null) {
                            mApproveLeaveArrayList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ApproveLeave>> call, @NonNull Throwable t) {
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

    private void init() {
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        mApproveLeaveArrayList = new ArrayList<>();
        resources = getResources();
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    private void initRecylcerView() {
        leaveRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        leaveRecyclerView.setHasFixedSize(false);
        mAdapter = new ApproveLeaveHeaderAdapter(mApproveLeaveArrayList);
        mAdapter.SetOnItemClickListener(this);
        leaveRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {
        this.headerPosition = headerPosition;
        this.position = position;
        String leaveStatus = mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).getStatus();
        int id = mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).getId();
        String appliedById = mApproveLeaveArrayList.get(headerPosition).getUserId();


        ArrayList<MyLeaves> myLeavesArrayList = mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList();

        ArrayList<ParticularLeave> particularLeave = myLeavesArrayList.get(position).getLeaveArrayList();

        String startDate="",endDate="";
        String description = "";
        if (!particularLeave.isEmpty()) {
            description = particularLeave.get(0).getDate();
            startDate = description;
            if (particularLeave.size() > 1) {
                endDate = particularLeave.get(particularLeave.size() - 1).getDate();
                description = description +" "+endDate;
            }
        }

        description = description +" "+ mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).getReason();

        /*if (leaveStatus.equalsIgnoreCase(resources.getString(R.string.pending))) {
            showPendingDialog();
        } else if (leaveStatus.equalsIgnoreCase(resources.getString(R.string.approve))) {
            showNormalDialog(resources.getString(R.string.rejected));
        } else if (leaveStatus.equalsIgnoreCase(resources.getString(R.string.rejected))) {
            showNormalDialog(resources.getString(R.string.approve));

        }*/

        Intent intent = new Intent(this,LeaveCommentActivity.class);
        intent.putExtra(resources.getString(R.string.change_leave_status),leaveStatus);
        intent.putExtra(ConstantVariable.MIX_ID.ID,position);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,headerPosition);
        intent.putExtra(resources.getString(R.string.leave_id),id);
        intent.putExtra(ConstantVariable.UserPrefVar.USER_ID,appliedById);
        intent.putExtra(resources.getString(R.string.description),description);
        intent.putExtra(resources.getString(R.string.start_date),startDate);
        intent.putExtra(resources.getString(R.string.end_date),endDate);
        intent.putExtra(resources.getString(R.string.name),mApproveLeaveArrayList.get(headerPosition).getName());
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            int position = data.getIntExtra(ConstantVariable.MIX_ID.ID,-1);
            int headerPosition = data.getIntExtra(ConstantVariable.MIX_ID.ACTION,-1);
            String status = data.getStringExtra(resources.getString(R.string.change_leave_status));
            mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).setStatus(status);
             mAdapter.notifyDataSetChanged();
        }
    }


    /* private void showNormalDialog(final String actionButton) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(R.string.change_leave_status);
        builder.setCancelable(true);
        builder.setNegativeButton(resources.getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(actionButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                int actionValue;
                if (actionButton.equalsIgnoreCase(resources.getString(R.string.approve))) {
                    actionValue = resources.getInteger(R.integer.approve);
                } else {
                    actionValue = resources.getInteger(R.integer.rejected);
                }

                performAction(actionValue, mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).getId());
            }
        });

        builder.show();
    }
*/
   /* private void showPendingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.change_leave_status);
        builder.setCancelable(true);
        builder.setNegativeButton(resources.getString(R.string.rejected), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                performAction(resources.getInteger(R.integer.rejected), mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).getId());
            }
        });

        builder.setNeutralButton(resources.getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(R.string.approve, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                performAction(resources.getInteger(R.integer.approve), mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).getId());
            }
        });

        builder.show();
    }*/

    /*private void performAction(int actionValue, int leaveRequestId) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            String appliedUserId = mApproveLeaveArrayList.get(headerPosition).getUserId();
            Log.d(TAG, " " + appliedUserId);
            Call<StringResponse> call = api.changeLeaveStatus(userPref.getUserId(), appliedUserId, actionValue, leaveRequestId);
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null && !stringResponse.isError()) {
                            mApproveLeaveArrayList.get(headerPosition).getMyLeavesArrayList().get(position).setStatus(stringResponse.getMessage());
                            mAdapter.notifyDataSetChanged();
                        }
                        showMessage(stringResponse.getMessage());
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
    }*/
}
