package in.technitab.ess.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.TecHeaderAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.TecResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminTECListActivity extends AppCompatActivity implements ViewHeaderListener {

    @BindView(R.id.tecRecyclerView)
    RecyclerView tecRecyclerView;
    @BindView(R.id.empty_list)
    TextView emptyList;

    private ArrayList<TecResponse> mTecArrayList;
    private TecHeaderAdapter adapter;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teclist);
        ButterKnife.bind(this);

        init();
        initRecyclerView();
        setToolbar();
        getTecList();
    }

    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        mTecArrayList = new ArrayList<>();
        resources = getResources();
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle( resources.getString(R.string.submitted_tec));
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void initRecyclerView() {
        tecRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tecRecyclerView.setHasFixedSize(false);
        tecRecyclerView.setNestedScrollingEnabled(false);
        adapter = new TecHeaderAdapter(resources.getInteger(R.integer.admin),mTecArrayList);
        tecRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(this);
    }

    private void getTecList() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<TecResponse>> call = api.getUserTEC(0);
            call.enqueue(new Callback<ArrayList<TecResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TecResponse>> call, @NonNull Response<ArrayList<TecResponse>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<TecResponse> assignProject = response.body();
                        if (assignProject != null) {

                            if (!mTecArrayList.isEmpty()) {
                                mTecArrayList.clear();
                            }
                            mTecArrayList.addAll(assignProject);
                            adapter.notifyDataSetChanged();
                            if (mTecArrayList.isEmpty()) {
                                showMessage(resources.getString(R.string.no_history_found));
                            }
                        }
                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TecResponse>> call, @NonNull Throwable t) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getTecList();
        }
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {
        Intent intent = new Intent(this, AdminTecEntryActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.SUBMIT);
        intent.putExtra(ConstantVariable.Tec.ID, mTecArrayList.get(headerPosition).getTecArrayList().get(position));
        startActivityForResult(intent,1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
