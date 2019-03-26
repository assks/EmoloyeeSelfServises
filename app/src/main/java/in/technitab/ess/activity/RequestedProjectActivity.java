package in.technitab.ess.activity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.RequestProjectAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.RequestedProject;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestedProjectActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.notificationRecyclerView)
    RecyclerView notificationRecyclerView;
    @BindView(R.id.emptyTextView)
    TextView emptyTextView;
    @BindView(R.id.tryAgain)
    Button tryAgain;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.newMessageFab)
    FloatingActionButton newMessageFab;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private Resources resources;
    private ArrayList<RequestedProject> mRequestedProjectArrayList;
    private RequestProjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_project);
        ButterKnife.bind(this);
        init();
        setToolbar();
        initRecyclerView();
        fetchNotification();
    }

    private void fetchNotification() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<RequestedProject>> call = api.fetchProject("requested project");
            call.enqueue(new Callback<ArrayList<RequestedProject>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<RequestedProject>> call, @NonNull Response<ArrayList<RequestedProject>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<RequestedProject> hospitalBuildings = response.body();
                        if (hospitalBuildings != null) {
                            if (!mRequestedProjectArrayList.isEmpty()) {
                                mRequestedProjectArrayList.clear();
                            }
                            mRequestedProjectArrayList.addAll(hospitalBuildings);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<RequestedProject>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showToast(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(resources.getString(R.string.internet_not_available));
        }

    }

    private void showToast(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void init() {
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        resources = getResources();
        mRequestedProjectArrayList = new ArrayList<>();
        api = APIClient.getClient().create(RestApi.class);
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Requested Project");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        notificationRecyclerView.setLayoutManager(layoutManager);
        notificationRecyclerView.setHasFixedSize(false);
        notificationRecyclerView.setNestedScrollingEnabled(false);
        notificationRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new RequestProjectAdapter(this, mRequestedProjectArrayList);
        notificationRecyclerView.setAdapter(adapter);
        adapter.setTaskListener(this);

        notificationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int visibility = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (visibility != 0)
                    newMessageFab.show();
                else
                    newMessageFab.hide();
            }
        });
    }

    @OnClick(R.id.newMessageFab)
    protected void onNewMessage() {
        notificationRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onClickListener(final RecyclerView.ViewHolder viewHolder, final int position) {
        final RequestedProject project =  mRequestedProjectArrayList.get(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(resources.getString(R.string.request_project_message));
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.proceed), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                projectRequest(project,viewHolder.getAdapterPosition());
            }
        });
        builder.show();
    }


    private void projectRequest(final RequestedProject project, final int position) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> call = api.approveRequestedProject("assign requested project",project.getId(),project.getProjectId(),project.getProjectName(),project.getProjectTypeId(),project.getCreatedById(),project.getCreatedBy(),Integer.parseInt(userPref.getUserId()));
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!stringResponse.isError()){
                                mRequestedProjectArrayList.remove(project);
                                adapter.notifyItemRemoved(position);
                            }
                            showToast(stringResponse.getMessage());
                        }
                    } else {
                        showToast(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showToast(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(resources.getString(R.string.internet_not_available));
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
