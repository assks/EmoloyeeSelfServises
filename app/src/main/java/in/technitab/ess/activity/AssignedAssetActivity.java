package in.technitab.ess.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.AssignedAssetAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.OnBottomReachedListener;
import in.technitab.ess.model.AssignedFixAsset;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignedAssetActivity extends AppCompatActivity implements OnBottomReachedListener {

    @BindView(R.id.fixAssetsRecyclerView)
    RecyclerView fixAssetsRecyclerView;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private List<AssignedFixAsset> mFixAssetArrayList;
    private AssignedAssetAdapter adapter;
    private ArrayList<String> mSelectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_asset);
        ButterKnife.bind(this);

        init();
        setToolbar();
        initRecyclerView();
        getFixAssets();
    }
    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void getFixAssets() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            Call<ArrayList<AssignedFixAsset>> call = api.fetchAssignedFixAssets("assigned assets", userPref.getUserId());
            call.enqueue(new Callback<ArrayList<AssignedFixAsset>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<AssignedFixAsset>> call, @NonNull Response<ArrayList<AssignedFixAsset>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<AssignedFixAsset> stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!mFixAssetArrayList.isEmpty() && stringResponse.isEmpty()) {
                                showMessage(getResources().getString(R.string.no_more_project));

                            } else if (stringResponse.isEmpty()) {
                                showMessage(getResources().getString(R.string.no_history_found));
                            } else {
                                mFixAssetArrayList.addAll(stringResponse);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<AssignedFixAsset>> call, @NonNull Throwable t) {
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

    private void initRecyclerView() {
        fixAssetsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fixAssetsRecyclerView.setHasFixedSize(false);
        fixAssetsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new AssignedAssetAdapter(this, mFixAssetArrayList, fixAssetsRecyclerView);
        fixAssetsRecyclerView.setAdapter(adapter);
        adapter.setOnBottomReachedListener(this);
    }

    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);

        mFixAssetArrayList = new ArrayList<>();
        mSelectedList = new ArrayList<>();
    }

    @Override
    public void onLoadMore() {
        /*if (mFixAssetArrayList.size() >= 20) {
            mFixAssetArrayList.add(null);
            adapter.notifyItemInserted(mFixAssetArrayList.size() - 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFixAssetArrayList.remove(mFixAssetArrayList.size() - 1);
                    adapter.notifyItemRemoved(mFixAssetArrayList.size());
                    int index = mFixAssetArrayList.size();
                    getFixAssets(index);
                }
            }, 3000);
        }*/
    }

    @Override
    public void onRootClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        AssignedFixAsset asset = mFixAssetArrayList.get(position);
        if (!mSelectedList.contains(String.valueOf(asset.getId()))) {
            mSelectedList.add(String.valueOf(asset.getId()));
            //mFixAssetArrayList.get(position).setSelected(true);
        } else {
            //mFixAssetArrayList.get(position).setSelected(false);
            mSelectedList.remove(String.valueOf(asset.getId()));
        }
        adapter.notifyItemChanged(position);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance_leave,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setIcon(R.drawable.ic_add_user);
        menuItem.setTitle(getResources().getString(R.string.fix_asset));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave){
            startActivityForResult(new Intent(this,OrgFixAssetActivity.class),1);

            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
