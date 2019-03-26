package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
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
import in.technitab.ess.adapter.TripTecHeaderAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.TripUser;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripTecsActivity extends AppCompatActivity implements ViewHeaderListener {

    @BindView(R.id.userProjectRecyclerView)
    RecyclerView userProjectRecyclerView;
    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @BindView(R.id.rootLayout)
    CoordinatorLayout rootLayout;

    private Activity mActivity;
    private Resources resources;
    private NetConnection connection;
    private Dialog dialog;
    private UserPref userPref;
    RestApi api;
    private int trip_id = 0;

    private ArrayList<TripUser> mTripResponseArrayList;
    private TripTecHeaderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_tecs);
        ButterKnife.bind(this);

        init();
        setToolbar();
        setRcyclerView();
        fetchProjectTask();
    }

    private void init() {
        mActivity = this;
        resources = getResources();
        mTripResponseArrayList = new ArrayList<>();
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            trip_id = bundle.getInt(ConstantVariable.Trip.TRIP_ID);
        }
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.trip_tec_lable,trip_id));
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setRcyclerView() {
        userProjectRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        userProjectRecyclerView.setHasFixedSize(false);
        userProjectRecyclerView.setNestedScrollingEnabled(false);
        adapter = new TripTecHeaderAdapter( mTripResponseArrayList);
        userProjectRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(TripTecsActivity.this);
    }

    private void fetchProjectTask() {
        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<TripUser>> call = api.fetchTripTec(trip_id);
            call.enqueue(new Callback<ArrayList<TripUser>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TripUser>> call, @NonNull Response<ArrayList<TripUser>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<TripUser> assignProject = response.body();
                        if (assignProject != null) {

                            if(!mTripResponseArrayList.isEmpty()){
                                mTripResponseArrayList.clear();
                            }

                            mTripResponseArrayList.addAll(assignProject);
                            adapter.notifyDataSetChanged();
                            if (mTripResponseArrayList.isEmpty()) {
                                showMessage(resources.getString(R.string.no_booking_history_found));
                            }
                        }
                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TripUser>> call, @NonNull Throwable t) {
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
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {
        if (userPref.getRoleId() == resources.getInteger(R.integer.admin)) {
            Intent intent = new Intent(this, AdminTecEntryActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, ConstantVariable.MIX_ID.SUBMIT);
            intent.putExtra(ConstantVariable.Tec.ID, mTripResponseArrayList.get(headerPosition).getTecUserArrayList().get(position));
            startActivity(intent);
        }
    }
}
