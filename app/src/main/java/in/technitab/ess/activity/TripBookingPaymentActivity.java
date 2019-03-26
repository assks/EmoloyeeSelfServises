package in.technitab.ess.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.SpinAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.BookingMode;
import in.technitab.ess.model.BookingPayment;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.TripBooking;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.CustomEditText;
import in.technitab.ess.util.DateCal;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.FileNamePath;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.Permissions;
import in.technitab.ess.util.UserPref;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.technitab.ess.util.ConstantVariable.Value.getPaymentTerm;
import static in.technitab.ess.util.FileNamePath.getMimeType;
import static in.technitab.ess.util.ImageFile.reduceFileSize;

public class TripBookingPaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subHeading)
    TextView subHeading;
    @BindView(R.id.paymentMode)
    RadioGroup paymentMode;
    @BindView(R.id.paymentDate)
    CustomEditText paymentDate;
    @BindView(R.id.dueDate)
    CustomEditText dueDate;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.bookingPaymentTerm)
    Spinner bookingPaymentTerm;
    @BindView(R.id.reference_number)
    CustomEditText referenceNumber;
    @BindView(R.id.notes)
    EditText notes;
    @BindView(R.id.attachment)
    TextView attachment;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.billDate)
    CustomEditText billDate;
    @BindView(R.id.vendor)
    TextView vendor;

    private TripBooking tripBooking;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int RC_PERMISSIONS = 1;
    private UserPref userPref;
    private BookingPayment payment;
    private Dialog dialog;
    private NetConnection connection;
    private RestApi api;
    private Resources resources;

    private String actionBy = "";
    private int RC_CAPTURE = 3, RC_PICK = 4, RC_CONVERT = 5;
    private File mFile = null;
    private Uri mFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_booking_payment);
        ButterKnife.bind(this);

        init();
        setToolbar();
        setSpinner();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tripBooking = bundle.getParcelable(ConstantVariable.TRIP_BOOKINGS);
            actionBy = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            title.setText(resources.getString(R.string.booking_request_title, tripBooking.getTravel_type(), tripBooking.getAdminBookingMode()));
            vendor.setText(tripBooking.getAdminVendor());
            StringBuilder strSubHeading = new StringBuilder();
            strSubHeading.append(resources.getString(R.string.total_amount));
            strSubHeading.append(" ");
            strSubHeading.append(tripBooking.getTotalAmount());
            subHeading.setText(strSubHeading);
            new CustomDate(billDate, this, null, null);
            billDate.addTextChangedListener(new MyWatcher());

        } else {
            finish();
        }

    }

    private void init() {
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        resources = getResources();

        tripBooking = new TripBooking();
        payment = new BookingPayment();

        new CustomDate(paymentDate,this,null,null);

    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.payment_entry));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setSpinner() {
        List<Object> mTravelTypeList = new ArrayList<>();
        mTravelTypeList.addAll(getPaymentTerm());
        SpinAdapter suggestionAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, mTravelTypeList);
        bookingPaymentTerm.setAdapter(suggestionAdapter);
        bookingPaymentTerm.setOnItemSelectedListener(this);

    }

    private void setupPaymentDate(BookingMode bookingMode) {
        String strBillDate = billDate.getText().toString().trim();
        if (!TextUtils.isEmpty(strBillDate))
            dueDate.setText(getDate(bookingMode, strBillDate));
    }

    private String getDate(BookingMode bookingMode, String billDate) {
        String date;
        if (bookingMode.getTitle().equalsIgnoreCase("Due on Receipt")) {
            date = billDate;
        } else if (bookingMode.getTitle().equalsIgnoreCase("Due end of the month")) {
            date = DateCal.getLastDateOfMonth(billDate);
        } else if (bookingMode.getTitle().equalsIgnoreCase("Due end of the next month")) {
            date = DateCal.getNextMonth(billDate);
        } else {
            date = DateCal.getAddOnDate(billDate, Integer.parseInt(bookingMode.getValue()));
        }
        return date;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));
            BookingMode bookingMode = (BookingMode) adapterView.getSelectedItem();
            setupPaymentDate(bookingMode);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @OnClick(R.id.submit)
    protected void onSubmit() {
        int paymentModeSelectedId = paymentMode.getCheckedRadioButtonId();
        if (paymentModeSelectedId != -1) {
            payment.setPaymentMode(((RadioButton) findViewById(paymentModeSelectedId)).getText().toString());
        }
        payment.setPaidBy(actionBy.equalsIgnoreCase(resources.getString(R.string.user)) ? resources.getString(R.string.employee) : resources.getString(R.string.account));
        payment.setBillDate(billDate.getText().toString().trim());
        payment.setDueDate(dueDate.getText().toString().trim());
        BookingMode bookingMode = (BookingMode) bookingPaymentTerm.getSelectedItem();
        payment.setPaymentTerm(bookingMode.getTitle());
        payment.setPaymentTermLabel(bookingMode.getValue());
        payment.setPaymentDate(paymentDate.getText().toString().trim());
        String strAmount = amount.getText().toString().trim();
        double amountValue = TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount);
        payment.setPaidAmount(amountValue);
        payment.setReferenceNumber(referenceNumber.getText().toString().trim());
        payment.setNotes(notes.getText().toString().trim());
        payment.setBookingId(tripBooking.getId());
        payment.setCreateById(Integer.parseInt(userPref.getUserId()));

        if (invalidate()) {
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();
                tripBooking.setModified_by_id(Integer.parseInt(userPref.getUserId()));

                Gson gson = new Gson();
                String json = gson.toJson(payment);
                Map<String, RequestBody> myMap = new HashMap<>();
                if (mFile != null) {
                    String extension = mFile.getName().substring(mFile.getName().lastIndexOf(".") + 1);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/" + extension), mFile);
                    myMap.put("file\"; filename=\"" + mFile.getName(), fileBody);
                }

                RequestBody rbProjectId = RequestBody.create(MediaType.parse("text/plain"), json);
                myMap.put(ConstantVariable.PAYMENT_JSON, rbProjectId);
                RequestBody rbAction = RequestBody.create(MediaType.parse("text/plain"), "booking payment");
                myMap.put(ConstantVariable.MIX_ID.ACTION, rbAction);

                Call<StringResponse> bookingRequestApi = api.tripBookingPayment(myMap);
                bookingRequestApi.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            StringResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                showMessage(stringResponse.getMessage());
                                startPreviousActiviti();
                            }
                        } else {
                            showMessage(resources.getString(R.string.problem_to_connect));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
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
    }

    private void startPreviousActiviti() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }, 500);
    }

    private boolean invalidate() {
        boolean valid = true;
        if (TextUtils.isEmpty(payment.getPaymentDate())) {
            valid = false;
            showMessage("Payment date is required");
        }else if (TextUtils.isEmpty(payment.getPaymentMode())) {
            valid = false;
            showMessage("Payment Mode is required");
        } else if (TextUtils.isEmpty(payment.getPaidBy())) {
            valid = false;
            showMessage("Paid By is required");
        } else if (TextUtils.isEmpty(payment.getBillDate()) && payment.getPaymentMode().equalsIgnoreCase(resources.getString(R.string.online))) {
            valid = false;
            showMessage("Bill date is required");
        } else if (TextUtils.isEmpty(payment.getPaymentTerm())) {
            valid = false;
            showMessage("Payment term is required");
        } else if (payment.getPaidAmount() < 1) {
            valid = false;
            showMessage("Paid amount is required");
        } else if (TextUtils.isEmpty(payment.getReferenceNumber())) {
            valid = false;
            showMessage("Reference Number is required");
        }
        return valid;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance_leave, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setIcon(resources.getDrawable(R.drawable.ic_attachment_vector));
        menuItem.setTitle(resources.getString(R.string.attachment));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_leave) {

            if (Permissions.hasPermissions(this, PERMISSIONS)) {
                showImageCaptureDialog();
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, RC_PERMISSIONS);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showImageCaptureDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    takePhoto();
                } else if (items[item].equals("Choose from Library")) {
                    showDocDialog();
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

    private void showDocDialog() {
        Intent intent = new Intent();
        intent.setType("*/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent = Intent.createChooser(intent, "Select file");
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimetypes = {"*/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        }
        startActivityForResult(intent, RC_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri mainPath = data.getData();
            if (mainPath != null) {
                String mimeType = getMimeType(this, mainPath);
                if (mimeType.equalsIgnoreCase("pdf")) {
                    String path = FileNamePath.getPathFromUri(this, mainPath);
                    setupFile(path);
                } else if (mimeType.equalsIgnoreCase("png") || mimeType.equalsIgnoreCase("jpg") || mimeType.equalsIgnoreCase("jpeg")) {
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("uri", mainPath.toString());
                    startActivityForResult(intent, RC_CONVERT);
                } else
                    showMessage("Invalid file");
            }
        } else if (requestCode == RC_CAPTURE && resultCode == Activity.RESULT_OK) {
            mFile = reduceFileSize(mFile);
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("uri", mFileUri.toString());
            startActivityForResult(intent, RC_CONVERT);

        } else if (requestCode == RC_CONVERT && resultCode == Activity.RESULT_OK && data != null) {
            String path = data.getStringExtra("uri");
            if (path != null) {
                String extension = path.substring(path.lastIndexOf(".") + 1);
                if (extension.equalsIgnoreCase("pdf")) {
                    setupFile(path);
                }
            } else
                showMessage("Unknown Path. Please move file into internal storage");
        }

    }

    private void setupFile(String path) {
        mFile = new File(path);
        if (mFile.exists()) {
            long fileSize = mFile.length() / 1024;
            if (fileSize > 2048) {
                showMessage("File Size error");
                attachment.setTextColor(Color.RED);
                attachment.setText(resources.getString(R.string.invalid_file_size));
                mFile = null;
            } else {
                attachment.setVisibility(View.VISIBLE);
                attachment.setText(resources.getString(R.string.bill_attached));
                attachment.setTextColor(resources.getColor(R.color.colorPrimary));
                tripBooking.setBookingAttachment(mFile.getName());
            }
        } else {
            showMessage("Unknown file. Please move file internal storage");
        }
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        if (requestCode == RC_PERMISSIONS && grantResults.length > 0) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED)
                    valid = true;
                else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    valid = false;
                }
            }

            if (valid) {
                showImageCaptureDialog();
            }
        }
    }


    private class MyWatcher implements TextWatcher {

        private MyWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String date = billDate.getText().toString().trim();
            if (!date.isEmpty()) {
                BookingMode bookingMode = (BookingMode) bookingPaymentTerm.getSelectedItem();
                setupPaymentDate(bookingMode);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
