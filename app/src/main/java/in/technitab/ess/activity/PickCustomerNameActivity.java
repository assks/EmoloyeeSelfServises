package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.AutoCompleteAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Customer;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickCustomerNameActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.project_budget_hours)
    EditText projectBudgetHours;
    @BindView(R.id.customerRecyclerView)
    RecyclerView customerRecyclerView;

    ArrayList<Object> mObjectArrayList;
    private AutoCompleteAdapter adapter;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_customer_name);
        ButterKnife.bind(this);

        init();
        setToolbar();
        projectBudgetHours.addTextChangedListener(new MyCustomerNameWatcher(projectBudgetHours));
    }

    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        handler = new Handler();
        mObjectArrayList = new ArrayList<>();

        customerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        customerRecyclerView.setHasFixedSize(false);
        adapter = new AutoCompleteAdapter(getResources().getString(R.string.customer_name), mObjectArrayList);
        customerRecyclerView.setAdapter(adapter);
        adapter.setonItemClickListener(this);
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setTitle(getResources().getString(R.string.customer_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        Customer customer = (Customer) mObjectArrayList.get(position);
        Intent intent = new Intent();
        intent.putExtra(ConstantVariable.MIX_ID.ID, customer.getId());
        intent.putExtra(ConstantVariable.UserPrefVar.NAME, customer.getCustomerName());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }


    private class MyCustomerNameWatcher implements TextWatcher {
        private View view;

        public MyCustomerNameWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
            handler.removeCallbacks(runnable);
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (charSequence.length() > 0) {
                        getCustomName(charSequence.toString().trim());
                    }
                }
            };
            handler.postDelayed(runnable, 500);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void getCustomName(String str) {
        mObjectArrayList.clear();

        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Customer>> call = api.fetchCustomerList(str);
            call.enqueue(new Callback<ArrayList<Customer>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Customer>> call, @NonNull Response<ArrayList<Customer>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<Customer> customers = response.body();
                        if (customers != null) {
                            mObjectArrayList.addAll(customers);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Customer>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showSnackBar(getResources().getString(R.string.internet_not_available));
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

