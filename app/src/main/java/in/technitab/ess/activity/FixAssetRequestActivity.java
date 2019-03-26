package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class FixAssetRequestActivity extends AppCompatActivity {


    @BindView(R.id.orgTypeName)
    TextView orgTypeName;
    @BindView(R.id.remark)
    EditText remark;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private Resources resources;
    private int orgUnitId;
    private String orgUnitName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_asset_request);
        ButterKnife.bind(this);

        init();
        setToolbar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            orgUnitName = bundle.getString(ConstantVariable.FixAsset.ORG_UNIT_ASSET);
            orgTypeName.setText(orgUnitName);
            orgUnitId = bundle.getInt(ConstantVariable.FixAsset.ORG_UNIT_ASSET_ID);
        }
    }

    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
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


    @OnClick(R.id.submit)
    protected void onSubmit(){
        String strRemark = remark.getText().toString().trim();
        if (!validateMandatoryFields(strRemark)){
            if (connection.isNetworkAvailable(this)){
                dialog.showDialog();

                Call<StringResponse> call = api.requestFixAssets(ConstantVariable.REQUEST,userPref.getUserId(),orgUnitId,orgUnitName,strRemark);

                call.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            StringResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                if (!stringResponse.isError()) {
                                    startHomeActivity(0);
                                }
                                showMessage(stringResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                        dialog.dismissDialog();
                        if (t instanceof SocketTimeoutException) {
                            showMessage(getResources().getString(R.string.slow_internet_connection));
                        }
                    }
                });


            }else{
                showMessage(resources.getString(R.string.internet_not_available));
            }

        }
    }

    private void startHomeActivity(final int id) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(ConstantVariable.MIX_ID.ID,id);
                setResult(Activity.RESULT_OK,intent);
            }
        },500);
    }

    private boolean validateMandatoryFields(String strRemark){
        boolean error = false;
        if (TextUtils.isEmpty(strRemark)){
            error = true;
            showMessage("Remark is required");
        }else if (strRemark.length() > 300){
            error = true;
            showMessage("Remark character must be less 300");

        }
        return error;
    }


    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG).show();
    }
}
