package in.technitab.ess.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.activity.CreateTripActivity;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.activity.TripTecsActivity;
import in.technitab.ess.activity.UserBookingActivity;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class TravelBookingFragment extends Fragment implements TripAdapter.TripListener {

    private static final int RC_ADD = 1,RC_EDIT = 2;

    @BindView(R.id.userProjectRecyclerView)
    RecyclerView userProjectRecyclerView;
    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.empty_layout)
    RelativeLayout emptyLayout;
    Unbinder unbinder;
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

    public TravelBookingFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_booking, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        setRcyclerView();
        fetchProjectTask();

        return view;
    }

    private void init() {
        mActivity = getActivity();
        resources = getResources();
        mTripResponseArrayList = new ArrayList<>();
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        api = APIClient.getClient().create(RestApi.class);
        ((MainActivity) mActivity).setToolbar(resources.getString(R.string.travel_booking_menu));
        ((MainActivity) mActivity).setToolBarSubtitle(null);
    }

    private void setRcyclerView() {
        userProjectRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        userProjectRecyclerView.setHasFixedSize(false);
        userProjectRecyclerView.setNestedScrollingEnabled(false);
        adapter = new TripHeaderAdapter(resources.getInteger(R.integer.user), mTripResponseArrayList);
        userProjectRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(this);
    }

    private void fetchProjectTask() {
        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<TripResponse>> call = api.fetchTrip(Integer.parseInt(userPref.getUserId()), ConstantVariable.MIX_ID.SUBMIT);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_attendance_leave, menu);
        MenuItem leaveItem = menu.findItem(R.id.menu_leave);
        leaveItem.setTitle(resources.getString(R.string.submit_trip));
        leaveItem.setIcon(resources.getDrawable(R.drawable.ic_budget));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            Intent intent = new Intent(getActivity(), CreateTripActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, ConstantVariable.MIX_ID.SUBMIT);
            startActivityForResult(intent, RC_ADD);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ADD && resultCode == Activity.RESULT_OK && data != null) {
            fetchProjectTask();
            showMessage("Successfully Created");
        }else if (resultCode == Activity.RESULT_OK){
            fetchProjectTask();
        }
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
                    Intent intent = new Intent(mActivity,UserBookingActivity.class);
                    intent.putExtra(resources.getString(R.string.trip),trip);
                    intent.putExtra(resources.getString(R.string.trip_member),memberList);
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

    @SuppressLint("PrivateApi")
    @Override
    public void onActionClick(RecyclerView.ViewHolder viewHolder, final int headerPosition, final int position) {
        Trip trip = mTripResponseArrayList.get(headerPosition).getTripArrayList().get(position);
        ArrayList<TripMember> memberList = mTripResponseArrayList.get(headerPosition).getTripArrayList().get(position).getMemberJson();
        Intent intent = new Intent(mActivity,CreateTripActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.UPDATE);
        intent.putExtra(resources.getString(R.string.trip),trip);
        intent.putExtra(resources.getString(R.string.trip_member),memberList);
        startActivityForResult(intent,RC_EDIT);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
