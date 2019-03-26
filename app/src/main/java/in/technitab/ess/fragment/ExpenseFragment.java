package in.technitab.ess.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.activity.ProjectActivity;
import in.technitab.ess.activity.TecEntryActivity;
import in.technitab.ess.adapter.TecHeaderAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.NewTec;
import in.technitab.ess.model.TecResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExpenseFragment extends Fragment implements ViewHeaderListener {

    @BindView(R.id.userProjectRecyclerView)
    RecyclerView userProjectRecyclerView;
    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @BindView(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    Unbinder unbinder;

    private Activity mActivity;
    private Resources resources;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;

    private ArrayList<TecResponse> mTecArrayList;
    private TecHeaderAdapter adapter;

    public ExpenseFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        setRcyclerView();
        fetchProjectTask();
        return view;
    }

    private void fetchProjectTask() {
        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<TecResponse>> call = api.getUserTEC(Integer.parseInt(userPref.getUserId()));
            call.enqueue(new Callback<ArrayList<TecResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TecResponse>> call, @NonNull Response<ArrayList<TecResponse>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<TecResponse> assignProject = response.body();
                        if (assignProject != null) {

                            if (!mTecArrayList.isEmpty()){
                                mTecArrayList.clear();
                            }
                            mTecArrayList.addAll(assignProject);
                            adapter.notifyDataSetChanged();
                            if (mTecArrayList.isEmpty()) {
                                showMessage(resources.getString(R.string.no_history_found));
                            }
                        }
                    }else{
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
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setRcyclerView() {
        userProjectRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        userProjectRecyclerView.setHasFixedSize(false);
        userProjectRecyclerView.setNestedScrollingEnabled(false);
        userProjectRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity,DividerItemDecoration.VERTICAL));
        adapter = new TecHeaderAdapter(resources.getInteger(R.integer.user),mTecArrayList);
        userProjectRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(this);
    }

    private void init() {
        mActivity = getActivity();
        resources = getResources();
        mTecArrayList = new ArrayList<>();
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        api = APIClient.getClient().create(RestApi.class);
        ((MainActivity) mActivity).setToolbar(resources.getString(R.string.expenses));
        ((MainActivity)mActivity).setToolBarSubtitle(null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_attendance_leave, menu);
        MenuItem leaveItem = menu.findItem(R.id.menu_leave);
        leaveItem.setTitle(resources.getString(R.string.submittted_tec));
        leaveItem.setIcon(resources.getDrawable(R.drawable.ic_budget));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {

            Intent intent = new Intent(getActivity(), ProjectActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.tec));
            startActivityForResult(intent,1);
            return true;
        }
          else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            fetchProjectTask();

        }else if (requestCode == 2){
            fetchProjectTask();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder, int headerPosition, int position) {
        Intent intent = new Intent(mActivity, TecEntryActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.SUBMIT);
        intent.putExtra(ConstantVariable.Tec.ID, mTecArrayList.get(headerPosition).getTecArrayList().get(position));
        startActivityForResult(intent,2);
    }
}
