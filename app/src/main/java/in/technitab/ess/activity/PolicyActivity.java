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
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.PolicyAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.UserDoc;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyActivity extends AppCompatActivity implements RecyclerViewItemClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    String viewType = "";
    private ArrayList<Object> mObjectArrayList;
    private PolicyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ButterKnife.bind(this);

        init();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            viewType = bundle.getString(ConstantVariable.MIX_ID.VIEW_TYPE);
            if (viewType.equalsIgnoreCase(getResources().getString(R.string.policies))){
                List<String> policyList = Arrays.asList(getResources().getStringArray(R.array.privacyPolicyArray));
                mObjectArrayList.addAll(policyList);
                adapter.notifyDataSetChanged();
            }else {
                fetchUserDoc();
            }
         }

        setToolbar();
    }

    private void fetchUserDoc() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<UserDoc>> call = api.userDoc(userPref.getUserId());

            call.enqueue(new Callback<ArrayList<UserDoc>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<UserDoc>> call, @NonNull Response<ArrayList<UserDoc>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<UserDoc> stringResponse = response.body();
                        if (stringResponse != null) {
                            mObjectArrayList.addAll(stringResponse);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<UserDoc>> call, @NonNull Throwable t) {
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
        Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG).show();
    }

    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        mObjectArrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(false);
        adapter = new PolicyAdapter(mObjectArrayList);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(viewType.equalsIgnoreCase(getResources().getString(R.string.policies))?getResources().getString(R.string.policies):getResources().getString(R.string.attachment_list));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        if (mObjectArrayList.get(position) instanceof String){
            String policy = String.valueOf(mObjectArrayList.get(position));
            startActivity(new Intent(this, ProjectListActivity.class)
                    .putExtra(ConstantVariable.MIX_ID.ACTION, getResources().getString(R.string.policies))
                    .putExtra(ConstantVariable.UserPrefVar.NAME,policy)
                    .putExtra(ConstantVariable.UserPrefVar.USER_ID,position+1));


        }else if (mObjectArrayList.get(position) instanceof UserDoc){
            UserDoc userDoc = (UserDoc) mObjectArrayList.get(position);
            if (userDoc.getScanned() == 1) {
                startActivity(new Intent(this, ProjectListActivity.class)
                        .putExtra(ConstantVariable.MIX_ID.ACTION, getResources().getString(R.string.document_submission_form))
                        .putExtra(ConstantVariable.UserPrefVar.NAME, userDoc.getDocumentName())
                        .putExtra("file_url", userDoc.getFilePath()));
            }else{
                Toast.makeText(this, "You have not submit document", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
