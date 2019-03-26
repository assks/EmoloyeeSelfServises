package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.AddResponse;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.CustomEditText;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTripActivity extends AppCompatActivity {

    private static final String TAG = CreateTripActivity.class.getSimpleName();
    @BindView(R.id.projectName)
    CustomEditText projectName;
    @BindView(R.id.tripStartDate)
    CustomEditText tripStartDate;
    @BindView(R.id.memberLayout)
    LinearLayout memberLayout;
    @BindView(R.id.from)
    EditText from;
    @BindView(R.id.to)
    EditText to;
    @BindView(R.id.member_textview)
    TextView memberTextview;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.update)
    Button update;

    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private Resources resources;
    private ArrayList<TripMember> mUserList;
    private int projectId = 0,tripId = 0;
    private String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        ButterKnife.bind(this);

        init();
        setToolbar();
    }

    private void init() {
        resources = getResources();
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        mUserList = new ArrayList<>();
        api = APIClient.getClient().create(RestApi.class);
        new CustomDate(tripStartDate, this, null, null);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            if (action != null && action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE)) {
                Trip trip = bundle.getParcelable(resources.getString(R.string.trip));
                mUserList = bundle.getParcelableArrayList(resources.getString(R.string.trip_member));
                update.setVisibility(View.VISIBLE);
                projectName.setText(trip.getProjectName());
                projectId = trip.getProjectId();
                tripId = trip.getId();
                from.setText(trip.getSource());
                to.setText(trip.getDestination());
                tripStartDate.setText(trip.getStartDate());
                showImage(mUserList);
            }else{
                submit.setVisibility(View.VISIBLE);
            }
        }

    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(action.equalsIgnoreCase(ConstantVariable.MIX_ID.SUBMIT) ? resources.getString(R.string.submit_trip) : resources.getString(R.string.update_trip));
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @OnClick(R.id.projectName)
    protected void onProjectName() {
        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.search));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                projectName.setText(data.getStringExtra(resources.getString(R.string.project)));
                projectId = data.getIntExtra(ConstantVariable.Project.ID, 0);
                to.setText(data.getStringExtra(ConstantVariable.Project.SITE_LOCATION));
                from.setText(userPref.getBaseLocation());
                fetchProjectUser(projectId);
            }
        }
    }

    private void fetchProjectUser(int projectId) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            Call<ArrayList<TripMember>> call = api.fetchProjectUser(projectId);
            call.enqueue(new Callback<ArrayList<TripMember>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TripMember>> call, @NonNull Response<ArrayList<TripMember>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<TripMember> userList = response.body();
                        if (userList != null) {
                            mUserList.addAll(userList);
                            showImage(userList);
                        }
                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TripMember>> call, @NonNull Throwable t) {
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

    private void showImage(ArrayList<TripMember> list) {
        if (memberLayout.getChildCount() > 0) {
            memberLayout.removeAllViews();
        }

        for (TripMember user : list) {
            memberTextview.setVisibility(View.VISIBLE);
            memberLayout.setVisibility(View.VISIBLE);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_member, null);
            final CheckBox checkedTextView = view.findViewById(R.id.name);
            if (checkedTextView.getParent() != null) {
                ((ViewGroup) checkedTextView.getParent()).removeView(checkedTextView);
            }

            checkedTextView.setText(user.getMemberId() == Integer.parseInt(userPref.getUserId()) ? "Self" : user.getName());
            checkedTextView.setChecked(user.getMemberId() == Integer.parseInt(userPref.getUserId()) || user.isSelected());
            user.setSelected(user.getMemberId() == Integer.parseInt(userPref.getUserId()) || user.isSelected());

            memberLayout.addView(checkedTextView);
            checkedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = memberLayout.indexOfChild(checkedTextView);
                    mUserList.get(position).setSelected(!mUserList.get(position).isSelected());

                }
            });
        }
    }


    @OnClick(R.id.submit)
    protected void onSubmit() {
        ArrayList<TripMember> selectedTripMembers = new ArrayList<>();
        for (TripMember user : mUserList) {
            if (user.isSelected())
                selectedTripMembers.add(user);
        }

        Gson gson = new Gson();
        final String list = gson.toJson(selectedTripMembers);
        Log.d(TAG, "json " + list);
        final String strProjectName = projectName.getText().toString().trim();
        final String strSource = from.getText().toString().trim();
        final String strDestination = to.getText().toString().trim();
        final String strTripDate = tripStartDate.getText().toString().trim();

        if (invalidate(selectedTripMembers, strProjectName, strSource, strDestination, strTripDate)) {
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();

                Call<AddResponse> call = api.createTrip(projectId, strProjectName, strSource, strDestination, strTripDate, userPref.getUserId(), list);
                call.enqueue(new Callback<AddResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AddResponse> call, @NonNull Response<AddResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            AddResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                if (!stringResponse.isError()) {
                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                                showMessage(stringResponse.getMessage());

                            }
                        } else {
                            showMessage(resources.getString(R.string.problem_to_connect));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddResponse> call, @NonNull Throwable t) {
                        dialog.dismissDialog();
                        if (t instanceof SocketTimeoutException) {
                            showMessage(getResources().getString(R.string.slow_internet_connection));
                            //emptyTextView.setVisibility(View.GONE);
                        }
                    }
                });

            } else {
                showMessage(getResources().getString(R.string.internet_not_available));
            }

        }
    }

    @OnClick(R.id.update)
    protected void onUpdate() {
        ArrayList<TripMember> selectedTripMembers = new ArrayList<>();
        for (TripMember user : mUserList) {
            if (user.isSelected())
                selectedTripMembers.add(user);
        }

        Gson gson = new Gson();
        final String list = gson.toJson(selectedTripMembers);
        final String strProjectName = projectName.getText().toString().trim();
        final String strSource = from.getText().toString().trim();
        final String strDestination = to.getText().toString().trim();
        final String strTripDate = tripStartDate.getText().toString().trim();

        if (invalidate(selectedTripMembers, strProjectName, strSource, strDestination, strTripDate)) {
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();
                Call<AddResponse> call = api.editTrip(tripId,projectId, strProjectName, strSource, strDestination, strTripDate, userPref.getUserId(), list);
                call.enqueue(new Callback<AddResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AddResponse> call, @NonNull Response<AddResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            AddResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                if (!stringResponse.isError()) {
                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                                showMessage(stringResponse.getMessage());

                            }
                        } else {
                            showMessage(resources.getString(R.string.problem_to_connect));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddResponse> call, @NonNull Throwable t) {
                        dialog.dismissDialog();
                        if (t instanceof SocketTimeoutException) {
                            showMessage(getResources().getString(R.string.slow_internet_connection));
                            //emptyTextView.setVisibility(View.GONE);
                        }
                    }
                });

            } else {
                showMessage(getResources().getString(R.string.internet_not_available));
            }

        }
    }

    private boolean invalidate(ArrayList<TripMember> selectedTripMembers, String strProjectName, String strSource, String strDestination, String strTripDate) {
        boolean valid = true;
        if (strProjectName.isEmpty()) {
            showMessage("Project Name is required");
            valid = false;
        } else if (strSource.isEmpty()) {
            showMessage("From is required");
            valid = false;
        } else if (strDestination.isEmpty()) {
            showMessage("To is required");
            valid = false;
        } else if (strTripDate.isEmpty()) {
            showMessage("Start date is required");
            valid = false;
        } else if (selectedTripMembers.isEmpty()) {
            showMessage("Select at least one member");
            valid = false;
        }
        return valid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
