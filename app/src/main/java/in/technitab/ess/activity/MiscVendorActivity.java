package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.MiscVendorAdapter;
import in.technitab.ess.adapter.UserNameAdapter;
import in.technitab.ess.adapter.VendorAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.User;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiscVendorActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.vendorRecyclerView)
    RecyclerView vendorRecyclerView;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private Resources resources;
    private MiscVendorAdapter adapter;
    private ArrayList<Object> mVendorList;
    private UserNameAdapter userNameAdapter;
    private ArrayList<User> mUserArrayList;
    private String action = "",title;
    private UserPref userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);

            initRecyclerView();
            if (action.equalsIgnoreCase(resources.getString(R.string.user_name))) {
                setToolbar(resources.getString(R.string.user_name));
                fetchUserList();
            }else {
                title = bundle.getString(ConstantVariable.MIX_ID.TITLE);
                setToolbar(title);
                String vendorType = bundle.getString(ConstantVariable.Tec.ID);
                fetVendorList(vendorType);
            }
        }
    }
    
    private void initRecyclerView(){
        vendorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vendorRecyclerView.setHasFixedSize(true);
        vendorRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mVendorList = new ArrayList<>();
        if (!action.equalsIgnoreCase(resources.getString(R.string.user_name))) {
            adapter = new MiscVendorAdapter(this, ConstantVariable.MIX_ID.ACTION, mVendorList);
            vendorRecyclerView.setAdapter(adapter);
            adapter.setListener(this);
        } else {
            userNameAdapter = new UserNameAdapter(this, "", mUserArrayList);
            vendorRecyclerView.setAdapter(userNameAdapter);
            userNameAdapter.setListener(this);
        }

    }

    private void fetchUserList() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<User>> call = api.fetchUsers();
            call.enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<User>> call, @NonNull Response<ArrayList<User>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<User> customers = response.body();
                        if (customers != null) {
                            mUserArrayList.addAll(customers);
                            userNameAdapter.notifyDataSetChanged();
                        }
                    }else {
                        showToast(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<User>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showToast(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(getResources().getString(R.string.internet_not_available));
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
        userPref = new UserPref(this);
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        mUserArrayList = new ArrayList<>();
    }


    private void fetVendorList(String vendorType) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Vendor>> call = api.fetchTecVendorList(vendorType);
            call.enqueue(new Callback<ArrayList<Vendor>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Vendor>> call, @NonNull Response<ArrayList<Vendor>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<Vendor> list = response.body();
                        if (list != null) {
                            mVendorList.addAll(list);
                            mVendorList.add("Self");
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
        if (viewHolder instanceof MiscVendorAdapter.ViewHolder){
            Intent intent = new Intent();
            if (mVendorList.get(position) instanceof Vendor) {
                Vendor vendor = (Vendor) mVendorList.get(position);
                intent.putExtra(ConstantVariable.MIX_ID.VENDOR, vendor.getContactName());
                intent.putExtra(ConstantVariable.Tec.SITE_LOCATION, vendor.getBillingCity());
                intent.putExtra(ConstantVariable.Tec.GSTIN, vendor.getGstNumber());
            }else {
                String name = userPref.getName()+" " + getResources().getString(R.string.contact_zoho_name,Integer.parseInt(userPref.getUserId()));
                intent.putExtra(ConstantVariable.MIX_ID.VENDOR,name);
                intent.putExtra(ConstantVariable.Tec.SITE_LOCATION, "");
                intent.putExtra(ConstantVariable.Tec.GSTIN, "");
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else if (viewHolder instanceof UserNameAdapter.ViewHolder) {
            Intent intent = new Intent();
            intent.putExtra(ConstantVariable.UserPrefVar.USER_ID, mUserArrayList.get(position).getId());
            intent.putExtra(ConstantVariable.UserPrefVar.NAME, mUserArrayList.get(position).getName());
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
