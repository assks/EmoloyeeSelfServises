package in.technitab.ess.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.EducationAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.Education;
import in.technitab.ess.model.UserProfile;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_icon)
    ImageView userIcon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.employee_designation)
    TextView employeeDesignation;
    @BindView(R.id.personal_detail)
    TextView personalDetail;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.marital_status)
    TextView maritalStatus;
    @BindView(R.id.nationality)
    TextView nationality;
    @BindView(R.id.dob)
    TextView dob;
    @BindView(R.id.father)
    TextView father;
    @BindView(R.id.religion)
    TextView religion;
    @BindView(R.id.reporting_to)
    TextView reportingTo;
    @BindView(R.id.joining_date)
    TextView joiningDate;
    @BindView(R.id.appointment_date)
    TextView appointmentDate;
    @BindView(R.id.permanent_address)
    TextView permanentAddress;
    @BindView(R.id.current_address)
    TextView currentAddress;
    @BindView(R.id.official_email)
    TextView officialEmail;
    @BindView(R.id.personal_email)
    TextView personalEmail;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.emergency_number)
    TextView emergencyNumber;

    @BindView(R.id.profile_recycler_view)
    RecyclerView profileRecyclerView;

    @BindView(R.id.attachment_list)
    Button attachmentList;
    @BindView(R.id.pan_number)
    TextView panNumber;
    @BindView(R.id.passport_number)
    TextView passportNumber;
    @BindView(R.id.aadhar_number)
    TextView aadharNumber;
    @BindView(R.id.dl)
    TextView dl;
    @BindView(R.id.voter_id_number)
    TextView voterIdNumber;
    @BindView(R.id.bank_name)
    TextView bankName;
    @BindView(R.id.bank_address)
    TextView bankAddress;
    @BindView(R.id.account_number)
    TextView accountNumber;
    @BindView(R.id.ifsc_code)
    TextView ifscCode;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private ArrayList<Education> educationArrayList;
    private EducationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        init();
        setToolbar();
        initRecyclerView();
        getUserProfile();
    }

    private void initRecyclerView() {
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        profileRecyclerView.setNestedScrollingEnabled(false);
        profileRecyclerView.setHasFixedSize(false);
        adapter = new EducationAdapter(this, educationArrayList);
        profileRecyclerView.setAdapter(adapter);
    }

    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        educationArrayList = new ArrayList<>();
        api = APIClient.getClient().create(RestApi.class);

    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void getUserProfile() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<UserProfile> call = api.userProfile(userPref.getRoleId(), userPref.getRelatedTable());
            call.enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        UserProfile addTimesheet = response.body();
                        if (addTimesheet != null) {
                            showData(addTimesheet);
                        } else {
                            showMessage(getResources().getString(R.string.no_projet_assigned));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
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

    private void showData(UserProfile profile) {
        name.setText(profile.getName());
        employeeDesignation.setText(getResources().getString(R.string.emp_designation, userPref.getRoleId(), profile.getDesignation()));
        father.setText(profile.getFather());
        dob.setText(profile.getBirthDate());
        nationality.setText(profile.getNationality());
        maritalStatus.setText(profile.getMaritalStatus());
        reportingTo.setText(profile.getReportingTo());
        gender.setText(profile.getGender());
        joiningDate.setText(profile.getJoiningDate());
        appointmentDate.setText(profile.getAppointmentDate());
        officialEmail.setText(profile.getOfficialEmailId());
        personalEmail.setText(profile.getPersonalEmailId());
        number.setText(profile.getMobileNumber());
        emergencyNumber.setText(profile.getEmergencyNumber());
        currentAddress.setText(profile.getCurrentFullAddress());
        permanentAddress.setText(profile.getPermanentFullAddress());
        aadharNumber.setText(profile.getAadharNumber());
        panNumber.setText(profile.getPanNumber());
        voterIdNumber.setText(profile.getVoterIdNumber());
        dl.setText(profile.getDrivingLicenseNumber());
        bankAddress.setText(profile.getBankAddress());
        bankName.setText(profile.getBankName());
        ifscCode.setText(profile.getIfscCode());
        passportNumber.setText(profile.getPassportNumber());
        accountNumber.setText(profile.getAccountNumer());

        educationArrayList.add(new Education("High School", profile.getTenSchool(), profile.getTenYear(), profile.getTenBoard(), profile.getTenPercentage()));
        educationArrayList.add(new Education("Intermediate", profile.getTwelveSchool(), profile.getTwelveYear(), profile.getTwelveBoard(), profile.getTwelvePercentage()));
        educationArrayList.add(new Education("Diploma", profile.getDiplomaSchool(), profile.getDiplomaYear(), profile.getDiplomaBoard(), profile.getDiplomaPercentage()));
        educationArrayList.add(new Education("Graduation", profile.getGradSchool(), profile.getGradYear(), profile.getGradBoard(), profile.getGradPercentage()));
        educationArrayList.add(new Education("Post Graduation", profile.getPostGradSchool(), profile.getPostGradYear(), profile.getPostGradBoard(), profile.getPostGradPercentage()));
        adapter.notifyDataSetChanged();

    }

    @OnClick(R.id.attachment_list)
    protected void onAttachment(){
        startActivity(new Intent(this,PolicyActivity.class).putExtra(ConstantVariable.MIX_ID.VIEW_TYPE,"profile"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
