package in.technitab.ess.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import in.technitab.ess.activity.ExistingProjectActivity;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.adapter.AssignProjectAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.AssignProject;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectTaskFragment extends Fragment {

    @BindView(R.id.userProjectRecyclerView)
    RecyclerView userProjectRecyclerView;
    @BindView(R.id.create_project)
    Button createProject;
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

    private ArrayList<AssignProject> projectTaskArrayList;
    private AssignProjectAdapter adapter;

    public ProjectTaskFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_task, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();

        setRcyclerView();
        fetchProjectTask();
        return view;
    }

    private void fetchProjectTask() {
        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<AssignProject>> call = api.fetchProjectTask(userPref.getUserId());
            call.enqueue(new Callback<ArrayList<AssignProject>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<AssignProject>> call, @NonNull Response<ArrayList<AssignProject>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<AssignProject> assignProject = response.body();
                        if (assignProject != null) {
                            projectTaskArrayList.addAll(assignProject);
                            adapter.notifyDataSetChanged();
                            if (projectTaskArrayList.isEmpty()) {
                                emptyLayout.setVisibility(View.VISIBLE);
                            } else {
                                emptyLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<AssignProject>> call, @NonNull Throwable t) {
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
        adapter = new AssignProjectAdapter(mActivity, projectTaskArrayList);
        userProjectRecyclerView.setAdapter(adapter);
    }

    private void init() {
        mActivity = getActivity();
        resources = getResources();
        projectTaskArrayList = new ArrayList<>();
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        api = APIClient.getClient().create(RestApi.class);
        ((MainActivity) mActivity).setToolbar(resources.getString(R.string.project_task));
        ((MainActivity) mActivity).setToolBarSubtitle(null);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_attendance_leave, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setVisible(userPref.getAccessControlId() != resources.getInteger(R.integer.admin));
        menuItem.setTitle(resources.getString(R.string.submit_project));
        menuItem.setIcon(resources.getDrawable(R.drawable.ic_team));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            Intent intent = new Intent(getActivity(), ExistingProjectActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.submit));
            startActivity(intent);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

}

