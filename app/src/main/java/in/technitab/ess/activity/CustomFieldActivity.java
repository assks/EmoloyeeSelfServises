package in.technitab.ess.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.FileNamePath;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import in.technitab.ess.util.VerhoeffAlgorithm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.technitab.ess.util.ImageFile.reduceFileSize;

public class CustomFieldActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.name_of_bank)
    EditText nameOfBank;
    @BindView(R.id.bank_address)
    EditText bankAddress;
    @BindView(R.id.ifsc_code)
    EditText ifscCode;
    @BindView(R.id.bank_account_number)
    EditText bankAccountNumber;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.bank_attachment)
    TextView bankAttachment;
    @BindView(R.id.error_bank_attachment)
    ImageView errorBankAttachment;
    @BindView(R.id.bank_layout)
    RelativeLayout bankLayout;
    @BindView(R.id.id_proof_attachment)
    TextView idProofAttachment;
    @BindView(R.id.error_id_proof_attachment)
    ImageView errorIdProofAttachment;
    @BindView(R.id.id_proof_layout)
    RelativeLayout idProofLayout;
    @BindView(R.id.add_vendor)
    Button addVendor;
    @BindView(R.id.reject_vendor)
    Button rejectVendor;
    @BindView(R.id.view_action_layout)
    RelativeLayout viewActionLayout;
    @BindView(R.id.attachedBankFile)
    TextView attachedBankFile;
    @BindView(R.id.attachedProofFile)
    TextView attachedProofFile;
    @BindView(R.id.view_bank_passbook)
    ImageButton viewBankPassbook;
    @BindView(R.id.view_id_proof)
    ImageButton viewIdProof;
    @BindView(R.id.bank_holder_name)
    EditText bankHolderName;
    @BindView(R.id.idProofSpinner)
    Spinner idProofSpinner;
    @BindView(R.id.id_proof_num)
    EditText idProofNum;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.id_proof_main_layout)
    LinearLayout idProofMainLayout;
    @BindView(R.id.bill_num)
    EditText billNum;
    @BindView(R.id.view_bill)
    ImageButton viewBill;
    @BindView(R.id.attachedBillFile)
    TextView attachedBillFile;
    @BindView(R.id.error_bill_attachment)
    ImageView errorBillAttachment;
    @BindView(R.id.bill_attachment)
    TextView billAttachment;
    @BindView(R.id.bill_view_layout)
    RelativeLayout billViewLayout;
    @BindView(R.id.billLayout)
    LinearLayout billLayout;
    @BindView(R.id.bank_doc_layout)
    LinearLayout bankDocLayout;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private Resources resources;
    private String action;
    private File bankFile, idProofFile, billFile, mFile;
    private Vendor vendor;
    private List<String> mAttachmentTypeList;

    private int RC = 1;
    private static final int RC_CAPTURE = 1, RC_PICK = 2, RC_CONVERT = 3;
    private Uri mFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_field);
        ButterKnife.bind(this);

        init();
        setupSpinner();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vendor = bundle.getParcelable(ConstantVariable.MIX_ID.VENDOR);
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            setToolbar();

            setActionButton(action);
            setupAttachmentVisibility(action);

            if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.VIEW) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.APPROVE)) {
                setupDataValue(attachedBankFile);

            } else if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE)) {
                setupDataValue(null);
            } else {
                bankAttachment.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_attachment_vector), null, null, null);
                idProofAttachment.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_attachment_vector), null, null, null);
                billAttachment.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_attachment_vector), null, null, null);
            }
        } else {
            finish();
        }
    }

    private void bindIdProofValue(TextView approveView){
        String number = "";
        if (!vendor.getAdhaarNumber().isEmpty()) {
            number = vendor.getAdhaarNumber();
            idProofSpinner.setSelection(0);
        } else if (!vendor.getPanNumber().isEmpty()) {
            number = vendor.getPanNumber();
            idProofSpinner.setSelection(1);
        } else if (!vendor.getIdProof().isEmpty()) {
            number = vendor.getVoterId();
            idProofSpinner.setSelection(2);
        }
        idProofNum.setText(number);

        String idProofFieName = vendor.getIdProof().substring(vendor.getIdProof().lastIndexOf("/") + 1);
        if (approveView != null){
            attachedProofFile.setText(idProofFieName);
        }

        if (!idProofFieName.isEmpty()) {
            errorIdProofAttachment.setBackground(resources.getDrawable(R.drawable.ic_check_circle_file));
        }
    }

    private void bindBillValue(TextView approveView){
        billNum.setText(vendor.getBillNumber());
        String billFieName = vendor.getBillFilePath().substring(vendor.getBillFilePath().lastIndexOf("/") + 1);

        if (approveView != null){
            attachedBillFile.setText(billFieName);
        }
        if (!billFieName.isEmpty()) {
            errorBillAttachment.setBackground(resources.getDrawable(R.drawable.ic_check_circle_file));
        }
    }

    private void bindBankValue(TextView approveView){
        nameOfBank.setText(vendor.getBankName());
        bankAddress.setText(vendor.getBankAddress());
        bankAccountNumber.setText(vendor.getAccount_number());
        ifscCode.setText(vendor.getIfsc());
        bankHolderName.setText(vendor.getBankHolderName());

        String bankFieName = vendor.getBankFile().substring(vendor.getBankFile().lastIndexOf("/") + 1);
        if (approveView != null){
            attachedBankFile.setText(bankFieName);
        }
        if (!bankFieName.isEmpty()) {
            errorBankAttachment.setBackground(resources.getDrawable(R.drawable.ic_check_circle_file));
        }
    }

    private void setupDataValue(TextView textView) {

        if (vendor.getGstNumber().isEmpty() && vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            bindBankValue(textView);
            bindIdProofValue(textView);
        } else if (vendor.getGstNumber().isEmpty() && !vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            bindBillValue(textView);
            bindIdProofValue(textView);
        } else if (!vendor.getGstNumber().isEmpty() && vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            bindBankValue(textView);
        } else if (!vendor.getGstNumber().isEmpty() && !vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            bindBillValue(textView);
        }

    }

    private void setSelectedViewBasedValue(){

        if (vendor.getGstNumber().isEmpty() && vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))){
            idProofMainLayout.setVisibility(View.VISIBLE);
            bankDocLayout.setVisibility(View.VISIBLE);
        }
        else if (vendor.getGstNumber().isEmpty() && !vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))){
            idProofMainLayout.setVisibility(View.VISIBLE);
            billLayout.setVisibility(View.VISIBLE);

        }else if (!vendor.getGstNumber().isEmpty() && vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))){
            bankDocLayout.setVisibility(View.VISIBLE);
        }else {
            billLayout.setVisibility(View.VISIBLE);
        }
    }


    private void setupAttachmentVisibility(String action) {
        switch (action) {
            case ConstantVariable.MIX_ID.APPROVE:
                viewIdProof.setVisibility(View.VISIBLE);
                viewBankPassbook.setVisibility(View.VISIBLE);
                attachedProofFile.setVisibility(View.VISIBLE);
                attachedBankFile.setVisibility(View.VISIBLE);
                attachedBillFile.setVisibility(View.VISIBLE);
                viewBankPassbook.setVisibility(View.VISIBLE);
                viewBill.setVisibility(View.VISIBLE);
                viewIdProof.setVisibility(View.VISIBLE);
                break;

            case ConstantVariable.MIX_ID.UPDATE:
                errorIdProofAttachment.setVisibility(View.VISIBLE);
                errorBankAttachment.setVisibility(View.VISIBLE);
                idProofAttachment.setVisibility(View.VISIBLE);
                errorBillAttachment.setVisibility(View.VISIBLE);
                bankAttachment.setVisibility(View.VISIBLE);
                billAttachment.setVisibility(View.VISIBLE);
                viewBankPassbook.setVisibility(View.VISIBLE);
                viewBill.setVisibility(View.VISIBLE);
                viewIdProof.setVisibility(View.VISIBLE);
                break;

            case ConstantVariable.MIX_ID.SUBMIT:
                errorIdProofAttachment.setVisibility(View.VISIBLE);
                errorBankAttachment.setVisibility(View.VISIBLE);
                idProofAttachment.setVisibility(View.VISIBLE);
                bankAttachment.setVisibility(View.VISIBLE);
                billAttachment.setVisibility(View.VISIBLE);
                break;

            case ConstantVariable.MIX_ID.ADD:
                errorIdProofAttachment.setVisibility(View.VISIBLE);
                errorBankAttachment.setVisibility(View.VISIBLE);
                idProofAttachment.setVisibility(View.VISIBLE);
                bankAttachment.setVisibility(View.VISIBLE);
                billAttachment.setVisibility(View.VISIBLE);
                break;

            case ConstantVariable.MIX_ID.VIEW:
                viewIdProof.setVisibility(View.VISIBLE);
                viewBankPassbook.setVisibility(View.VISIBLE);
                attachedProofFile.setVisibility(View.VISIBLE);
                attachedBankFile.setVisibility(View.VISIBLE);
                attachedBillFile.setVisibility(View.VISIBLE);
                viewBankPassbook.setVisibility(View.VISIBLE);
                viewBill.setVisibility(View.VISIBLE);
                viewIdProof.setVisibility(View.VISIBLE);
                break;

        }

        setSelectedViewBasedValue();
    }

    private void setupSpinner() {
        mAttachmentTypeList = Arrays.asList(resources.getStringArray(R.array.idProofTypeArray));
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAttachmentTypeList);
        idProofSpinner.setAdapter(paymentAdapter);
        idProofSpinner.setOnItemSelectedListener(this);
    }

    private void init() {
        resources = getResources();
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);

    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getToolbarTitle(action.toLowerCase()));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    private String getToolbarTitle(String title) {
        String newTitle;
        switch (title) {
            case ConstantVariable.MIX_ID.APPROVE:
                newTitle = resources.getString(R.string.approve_vendor);
                break;

            case ConstantVariable.MIX_ID.UPDATE:
                newTitle = resources.getString(R.string.update_vendor);
                break;

            case ConstantVariable.MIX_ID.SUBMIT:
                newTitle = resources.getString(R.string.submit_vendor);
                break;

            case ConstantVariable.MIX_ID.VIEW:
                newTitle = resources.getString(R.string.view_vendor_detail);
                break;

            default:
                newTitle = resources.getString(R.string.add_vendor);
                break;
        }

        return newTitle;
    }


    private void setActionButton(String action) {
        if (userPref.getAccessControlId() == resources.getInteger(R.integer.admin) && action.equalsIgnoreCase(resources.getString(R.string.approve))) {
            viewActionLayout.setVisibility(View.VISIBLE);
        } else if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.VIEW)) {
            submit.setVisibility(View.GONE);
            viewActionLayout.setVisibility(View.GONE);
        } else if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.SUBMIT) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.ADD)) {
            submit.setVisibility(View.VISIBLE);
            submit.setText(resources.getString(R.string.submit));
        } else if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE)) {
            submit.setVisibility(View.VISIBLE);
            submit.setText(resources.getString(R.string.update));
        }

    }

    @OnClick({R.id.view_bank_passbook, R.id.view_id_proof, R.id.view_bill})
    protected void onView(View view) {
        String url;
        switch (view.getId()) {

            case R.id.view_bank_passbook:
                url = vendor.getBankFile();
                break;

            case R.id.view_bill:
                url = vendor.getBillFilePath();
                break;

            default:
                url = vendor.getIdProof();
                break;
        }

        if (!url.isEmpty()) {
            Intent intent = new Intent(this, ProjectListActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, "tec");
            intent.putExtra("file_url", url);
            intent.putExtra(resources.getString(R.string.project), vendor.getContactName());
            startActivity(intent);
        }
    }

    @OnClick({R.id.bank_attachment, R.id.id_proof_attachment, R.id.bill_attachment})
    protected void onAttachmentClick(View view) {
        switch (view.getId()) {

            case R.id.bank_attachment:
                RC = 1;
                break;


            case R.id.id_proof_attachment:
                RC = 2;
                break;

            case R.id.bill_attachment:
                RC = 3;
        }
        showImageCaptureDialog();
    }

    private void showImageCaptureDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    takePhoto();
                } else if (items[item].equals("Choose from Library")) {
                    selectImageFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFile = new File(getOutputMediaFile(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (Build.VERSION.SDK_INT < 24) {
            mFileUri = Uri.fromFile(mFile);
        } else {
            mFileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".my.package.name.provider", mFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, RC_CAPTURE);
    }

    private String getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Ess");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return mediaStorageDir.getPath();
    }


    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, RC_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CAPTURE && resultCode == Activity.RESULT_OK) {
            mFile = reduceFileSize(mFile);
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("uri", mFileUri.toString());
            startActivityForResult(intent, RC_CONVERT);
        } else if (requestCode == RC_CONVERT && resultCode == Activity.RESULT_OK && data != null) {
            mFileUri = null;
            String path = data.getStringExtra("uri");
            if (path != null) {
                String extension = path.substring(path.lastIndexOf(".") + 1);
                if (extension.equalsIgnoreCase("pdf")) {
                    setupFile(path);
                } else {
                    showSnackBar("Unknown file");
                }
            }

        } else if (requestCode == RC_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri mainPath = data.getData();
            if (mainPath != null) {
                String mimeType = FileNamePath.getMimeType(this, mainPath);
                if (mimeType.equalsIgnoreCase("pdf")) {
                    String path = FileNamePath.getPathFromUri(this, mainPath);
                    setupFile(path);
                } else if (mimeType.equalsIgnoreCase("png") || mimeType.equalsIgnoreCase("jpg") || mimeType.equalsIgnoreCase("jpeg")) {
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("uri", mainPath.toString());
                    startActivityForResult(intent, RC_CONVERT);
                } else {
                    showSnackBar("Invalid File");
                }
            }
        }
    }

    private void setupFile(String path) {
        if (RC == 1) {
            bankFile = new File(path);
            long fileSize = bankFile.length() / 1024;
            if (fileSize >= 2048) {
                errorBankAttachment.setBackground(resources.getDrawable(R.drawable.ic_highlight_off));
            } else {
                errorBankAttachment.setBackground(resources.getDrawable(R.drawable.ic_check_circle_file));
                vendor.setBankFile(bankFile.getName());
            }
            errorBankAttachment.setVisibility(View.VISIBLE);
        } else if (RC == 2) {

            idProofFile = new File(path);
            long fileSize = idProofFile.length() / 1024;
            if (fileSize >= 2048) {
                errorIdProofAttachment.setBackground(resources.getDrawable(R.drawable.ic_highlight_off));
            } else {
                errorIdProofAttachment.setBackground(resources.getDrawable(R.drawable.ic_check_circle_file));
                vendor.setIdProof(idProofFile.getName());
            }
            errorIdProofAttachment.setVisibility(View.VISIBLE);

        } else if (RC == 3) {

            billFile = new File(path);
            long fileSize = billFile.length() / 1024;
            if (fileSize > 2048) {
                errorBillAttachment.setBackground(resources.getDrawable(R.drawable.ic_highlight_off));
            } else {
                errorBillAttachment.setBackground(resources.getDrawable(R.drawable.ic_check_circle_file));
                vendor.setBillFilePath(billFile.getName());
            }
            errorBillAttachment.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.submit, R.id.add_vendor})
    protected void onSubmit() {

        String strNameOfBank = nameOfBank.getText().toString().trim();
        String strBankAddress = bankAddress.getText().toString().trim();
        String strBankAccountNumber = bankAccountNumber.getText().toString().trim();
        String strIfscCode = ifscCode.getText().toString().trim();
        String strBankHolderName = bankHolderName.getText().toString().trim();
        String idProofValue = idProofSpinner.getSelectedItem().toString().trim();
        String idProofNumValue = idProofNum.getText().toString().trim();
        String strBillNum = billNum.getText().toString().trim();


        if (invalidate(idProofValue, idProofNumValue, strNameOfBank, strBankHolderName, strBankAddress, strBankAccountNumber, strIfscCode, strBillNum)) {

            if (vendor.getGstNumber().isEmpty()) {
                if (idProofValue.equalsIgnoreCase(mAttachmentTypeList.get(0))) {
                    vendor.setAdhaarNumber(idProofNumValue);
                } else if (idProofValue.equalsIgnoreCase(mAttachmentTypeList.get(1))) {
                    vendor.setPanNumber(idProofNumValue);
                } else if (idProofValue.equalsIgnoreCase(mAttachmentTypeList.get(2))) {
                    vendor.setVoterId(idProofNumValue);
                } else {
                    vendor.setBillNumber(strBillNum);
                }
            } else {
                vendor.setBillNumber(strBillNum);
            }

            vendor.setBillNumber(strBillNum);
            vendor.setBankAddress(strBankAddress);
            vendor.setBankName(strNameOfBank);
            vendor.setBankHolderName(strBankHolderName);
            vendor.setAccount_number(strBankAccountNumber);
            vendor.setIfsc(strIfscCode);

            if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.ADD) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.SUBMIT)){
                vendor.setCreateById(Integer.parseInt(userPref.getUserId()));
            }else if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.APPROVE)){
                vendor.setModifiedById(Integer.parseInt(userPref.getUserId()));
            }

            Gson gson = new Gson();
            String json = gson.toJson(vendor);
            proceedAddVendor(json);

        }
    }

    private void startVendorActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(Activity.RESULT_OK);
                finish();
            }
        },500);
    }

    private void proceedAddVendor(String json) {
        RequestBody jsonBody = RequestBody.create(MediaType.parse("text/plain"), json);
        RequestBody rbAction = RequestBody.create(MediaType.parse("text/plain"), action);

        Map<String, RequestBody> myMap = new HashMap<>();
        if (bankFile != null) {
            RequestBody bankFileRB = RequestBody.create(MediaType.parse("application/pdf"), bankFile);
            myMap.put("bank_name_attachment\"; filename=\"" + bankFile.getName(), bankFileRB);
        }
        if (idProofFile != null) {
            RequestBody fileBodyRB = RequestBody.create(MediaType.parse("application/pdf"), idProofFile);
            myMap.put("id_proof_attachment\"; filename=\"" + idProofFile.getName(), fileBodyRB);
        }

        if (billFile != null) {
            RequestBody fileBodyRB = RequestBody.create(MediaType.parse("application/pdf"), billFile);
            myMap.put("bill_attachment\"; filename=\"" + billFile.getName(), fileBodyRB);
        }

        myMap.put(ConstantVariable.MIX_ID.ACTION, rbAction);
        myMap.put("vendor_json", jsonBody);


        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> call;
            call = api.addVendor(myMap);
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            showSnackBar(stringResponse.getMessage());
                            startVendorActivity();
                        }
                    } else {
                        showSnackBar(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });

        } else {
            showSnackBar(resources.getString(R.string.internet_not_available));
        }
    }

    private boolean invalidate(String idProofValue, String idProofNumValue, String strNameOfBank, String strBankHolderName, String strBankAddress, String strBankAccountNumber, String strIfscCode, String billNum) {
        boolean valid = true;
        if (vendor.getGstNumber().isEmpty() && vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            if (!idProofValidation(idProofValue, idProofNumValue)) {
                valid = false;
            } else if (!bankValidation(strBankAccountNumber, strBankAddress, strBankHolderName, strIfscCode, strNameOfBank)) {
                valid = false;
            }

        } else if (vendor.getGstNumber().isEmpty() && !vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            if (!billValidation(billNum)) {
                valid = false;
            }
        } else if (!vendor.getGstNumber().isEmpty() && !vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            if (!billValidation(billNum)) {
                valid = false;
            }
        } else if (!vendor.getGstNumber().isEmpty() && vendor.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.bank))) {
            if (!bankValidation(strBankAccountNumber, strBankAddress, strBankHolderName, strIfscCode, strNameOfBank)) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean billValidation(String billNum) {
        boolean valid = true;
        if (billNum.isEmpty()) {
            valid = false;
            showSnackBar("Bill num is required");
        } else if (vendor.getBillFilePath().isEmpty()) {
            valid = false;
            showSnackBar("Bill attachment is required");
        }
        return valid;
    }

    private boolean idProofValidation(String idProofValue, String idProofNumValue) {
        boolean valid = true;
        if (idProofValue.equalsIgnoreCase(mAttachmentTypeList.get(0)) && (idProofNumValue.isEmpty() || !validateAadharNumber(idProofNumValue))) {
            valid = false;
            showSnackBar("Aadhar number is invalid");
        } else if (idProofValue.equalsIgnoreCase(mAttachmentTypeList.get(1)) && (idProofNumValue.isEmpty() || !validPanNumber(idProofNumValue))) {
            valid = false;
            showSnackBar("PAN number is invalid");
        } else if (idProofValue.equalsIgnoreCase(mAttachmentTypeList.get(2)) && (idProofNumValue.isEmpty())) {
            valid = false;
            showSnackBar("Voter id number is required");
        } else if (vendor.getIdProof().isEmpty()) {
            valid = false;
            showSnackBar("Id proof is required");
        }
        return valid;
    }

    private boolean bankValidation(String strBankAccountNumber, String strBankAddress, String strBankHolderName, String strIfscCode, String strNameOfBank) {
        boolean valid = true;
        if (strNameOfBank.isEmpty()) {
            valid = false;
            showSnackBar("Bank name is required");

        } else if (strBankAddress.isEmpty()) {
            valid = false;
            showSnackBar("Bank address is required");

        } else if (strBankHolderName.isEmpty()) {
            valid = false;
            showSnackBar("Bank Holder Name is required");

        } else if (strBankAccountNumber.isEmpty()) {
            valid = false;
            showSnackBar("Bank account number is required");

        } else if (strIfscCode.isEmpty()) {
            valid = false;
            showSnackBar("IFSC number is required");

        } else if (!validIFSCNumber(strIfscCode)) {
            valid = false;
            showSnackBar("Invalid IFSC number");

        } else if (vendor.getBankFile().isEmpty()) {
            showSnackBar("Please attach bank passbook/cheque");
            valid  = false;
        }
        return valid;
    }


    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }


    private static boolean validPanNumber(String strPanNum) {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(strPanNum);
        return matcher.matches();
    }

    private static boolean validIFSCNumber(String ifsc) {
        Pattern pattern = Pattern.compile("[A-Za-z]{4}0[A-Z0-9a-z]{6}");
        Matcher matcher = pattern.matcher(ifsc);
        return matcher.matches();
    }

    private void showSnackBar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
