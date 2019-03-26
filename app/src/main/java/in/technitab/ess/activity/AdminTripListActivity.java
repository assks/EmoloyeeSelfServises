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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.TripAdapter;
import in.technitab.ess.adapter.TripHeaderAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.model.TripResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import in.technitab.ess.viewholder.TripMainHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminTripListActivity extends AppCompatActivity implements TripAdapter.TripListener {

    private static final String TAG = AdminTripListActivity.class.getSimpleName();
    @BindView(R.id.userTripRecyclerView)
    RecyclerView userTripRecyclerView;
    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @BindView(R.id.rootLayout)
    CoordinatorLayout rootLayout;

    private Activity mActivity;
    private Resources resources;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private ArrayList<TripResponse> mTripResponseArrayList;
    private TripHeaderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_trip_list);
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
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        api = APIClient.getClient().create(RestApi.class);
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.travel_booking_menu));
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
    private void setRcyclerView() {
        userTripRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        userTripRecyclerView.setHasFixedSize(false);
        userTripRecyclerView.setNestedScrollingEnabled(false);
        userTripRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        adapter = new TripHeaderAdapter(resources.getInteger(R.integer.admin), mTripResponseArrayList);
        userTripRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(this);
    }


    private void fetchProjectTask() {
        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<TripResponse>> call = api.fetchTrip(Integer.parseInt(userPref.getUserId()), ConstantVariable.MIX_ID.APPROVE);
            call.enqueue(new Callback<ArrayList<TripResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TripResponse>> call, @NonNull Response<ArrayList<TripResponse>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<TripResponse> assignProject = response.body();
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
                public void onFailure(@NonNull Call<ArrayList<TripResponse>> call, @NonNull Throwable t) {
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
    public void onRootClick(RecyclerView.ViewHolder viewHolder, final int headerPosition, final int position) {
        TripMainHolder mainHolder = (TripMainHolder) viewHolder;
        PopupMenu popup = new PopupMenu(mActivity, mainHolder.rootLayout);

        popup.getMenuInflater().inflate(R.menu.menu_trip, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.trip_booking){
                    Trip trip = mTripResponseArrayList.get(headerPosition).getTripArrayList().get(position);
                    ArrayList<TripMember> memberList = mTripResponseArrayList.get(headerPosition).getTripArrayList().get(position).getMemberJson();
                    Intent intent = new Intent(mActivity,NewTravelBookingActivity.class);
                    intent.putExtra(resources.getString(R.string.trip),trip);
                    intent.putExtra(ConstantVariable.LIST,memberList);
                    startActivity(intent);

                }else if (item.getItemId() == R.id.trip_tec){
                    Trip trip = mTripResponseArrayList.get(headerPosition).getTripArrayList().get(position);
                    Intent intent = new Intent(mActivity,TripTecsActivity.class);
                    intent.putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.UPDATE);
                    intent.putExtra(ConstantVariable.Trip.TRIP_ID,trip.getId());
                    startActivity(intent);
                }
                return true;
            }
        });

        popup.show();

    }

    @Override
    public void onActionClick(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
