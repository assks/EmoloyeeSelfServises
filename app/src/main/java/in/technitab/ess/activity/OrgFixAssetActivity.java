package in.technitab.ess.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.FixAssetAdapter;
import in.technitab.ess.adapter.OrgAssetAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.OnBottomReachedListener;
import in.technitab.ess.model.FixAsset;
import in.technitab.ess.model.OrgAssetType;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgFixAssetActivity extends AppCompatActivity implements OnBottomReachedListener {

    @BindView(R.id.fixAssetsRecyclerView)
    RecyclerView fixAssetsRecyclerView;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private List<OrgAssetType> mFixAssetArrayList;
    private OrgAssetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_fix_asset);
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

            Call<ArrayList<OrgAssetType>> call = api.fetchFixAssets(userPref.getRoleId(), userPref.getRelatedTable(),"asset type");
            call.enqueue(new Callback<ArrayList<OrgAssetType>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<OrgAssetType>> call, @NonNull Response<ArrayList<OrgAssetType>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<OrgAssetType> stringResponse = response.body();
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
                public void onFailure(@NonNull Call<ArrayList<OrgAssetType>> call, @NonNull Throwable t) {
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
        adapter = new OrgAssetAdapter(this, mFixAssetArrayList, fixAssetsRecyclerView);
        fixAssetsRecyclerView.setAdapter(adapter);
        adapter.setOnBottomReachedListener(this);
    }

    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        mFixAssetArrayList = new ArrayList<>();
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
        OrgAssetType orgAssetType = mFixAssetArrayList.get(position);
        Intent intent = new Intent(this,FixAssetRequestActivity.class);
        intent.putExtra(ConstantVariable.FixAsset.ORG_UNIT_ASSET,orgAssetType.getFixAsset());
        intent.putExtra(ConstantVariable.FixAsset.ORG_UNIT_ASSET_ID,orgAssetType.getId());
        startActivityForResult(intent,1);
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
