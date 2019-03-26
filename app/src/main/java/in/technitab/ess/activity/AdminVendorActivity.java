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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.RelativeLayout;

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
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminVendorActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.vendorRecyclerView)
    RecyclerView vendorRecyclerView;
    @BindView(R.id.create_project)
    Button createProject;
    @BindView(R.id.empty_layout)
    RelativeLayout emptyLayout;

    private Activity mActivity;
    private Resources resources;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private ArrayList<Vendor> mVendorList;
    private VendorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);
        ButterKnife.bind(this);

        init();
        setToolbar();
        fetVendorList();
    }


    private void init() {
        mActivity = this;
        resources = getResources();
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        api = APIClient.getClient().create(RestApi.class);


        vendorRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        vendorRecyclerView.setHasFixedSize(true);
        vendorRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mVendorList = new ArrayList<>();
        adapter = new VendorAdapter(mActivity,ConstantVariable.MIX_ID.ACTION, mVendorList);
        vendorRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.approve_vendor));
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void fetVendorList() {
        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<Vendor>> call = api.fetchVendorList(userPref.getUserId(),ConstantVariable.MIX_ID.APPROVE);
            call.enqueue(new Callback<ArrayList<Vendor>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Vendor>> call, @NonNull Response<ArrayList<Vendor>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<Vendor> list = response.body();
                        if (!mVendorList.isEmpty())
                            mVendorList.clear();

                        if (list != null) {
                            mVendorList.addAll(list);
                            adapter.notifyDataSetChanged();
                            if (mVendorList.isEmpty()) {
                                showToast("No pending vendor created yet");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Vendor>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        dialog.dismissDialog();
                        showToast(mActivity.getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(mActivity.getResources().getString(R.string.internet_not_available));
        }
    }

    private void showToast(String message) {
        Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        Intent intent = new Intent(mActivity, AddVendorActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.VENDOR, mVendorList.get(position));
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.APPROVE);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            fetVendorList();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

