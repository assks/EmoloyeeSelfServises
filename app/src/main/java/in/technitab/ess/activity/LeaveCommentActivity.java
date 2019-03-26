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
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveCommentActivity extends AppCompatActivity {

    @BindView(R.id.leave_description)
    TextView leaveDescription;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.submit)
    Button submit;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private Resources resources;
    private int position, headerPosition, leaveRequestId;
    private String appliedUserId, status, name = "",startDate="",endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_comment);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt(ConstantVariable.MIX_ID.ID);
            headerPosition = bundle.getInt(ConstantVariable.MIX_ID.ACTION);
            leaveRequestId = bundle.getInt(resources.getString(R.string.leave_id));
            appliedUserId = bundle.getString(ConstantVariable.UserPrefVar.USER_ID);
            String description = bundle.getString(resources.getString(R.string.description));
            startDate = bundle.getString(resources.getString(R.string.start_date));
            endDate = bundle.getString(resources.getString(R.string.end_date));
            leaveDescription.setText(description);
            status = bundle.getString(resources.getString(R.string.change_leave_status));
            name = bundle.getString(resources.getString(R.string.name));
        }

        setToolbar();
    }

    private void init() {
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        resources = getResources();
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @OnClick(R.id.submit)
    protected void onSubmit() {

        String strComment = comment.getText().toString().trim();

        if (status.equalsIgnoreCase(resources.getString(R.string.pending))) {
            showPendingDialog(strComment);
        } else if (status.equalsIgnoreCase(resources.getString(R.string.approved))) {
            showNormalDialog(resources.getString(R.string.rejected), strComment);
        } else if (status.equalsIgnoreCase(resources.getString(R.string.rejected))) {
            showNormalDialog(resources.getString(R.string.approved), strComment);
        }
    }

    
    private void showNormalDialog(final String actionButton, final String comment) {
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
                String actionValue;
                if (actionButton.equalsIgnoreCase(resources.getString(R.string.approved))) {
                    actionValue = resources.getString(R.string.approve);
                } else {
                    actionValue = resources.getString(R.string.reject);
                }

                performAction(actionValue, leaveRequestId, comment);
            }
        });

        builder.show();
    }

    private void showPendingDialog(final String strComment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.change_leave_status);
        builder.setCancelable(true);
        builder.setNegativeButton(resources.getString(R.string.rejected), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                performAction(resources.getString(R.string.reject), leaveRequestId, strComment);
            }
        });

        builder.setNeutralButton(resources.getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(R.string.approved, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                performAction(resources.getString(R.string.approve), leaveRequestId, strComment);
            }
        });

        builder.show();
    }

    private void performAction(String actionValue, int leaveRequestId, String comment) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> call = api.changeLeaveStatus("change_leave_request_status",leaveRequestId, actionValue,Integer.parseInt(userPref.getUserId()), comment);
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {

                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!stringResponse.isError()) {
                                Intent intent = new Intent();
                                intent.putExtra(ConstantVariable.MIX_ID.ACTION, headerPosition);
                                intent.putExtra(ConstantVariable.MIX_ID.ID, position);
                                intent.putExtra(resources.getString(R.string.change_leave_status), stringResponse.getMessage());
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
