package in.technitab.ess.activity;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.TripBooking;
import in.technitab.ess.util.ConstantVariable;
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

import static in.technitab.ess.util.FileNamePath.getMimeType;
import static in.technitab.ess.util.ImageFile.reduceFileSize;

public class AttachBillActivity extends AppCompatActivity {

    @BindView(R.id.bookingMode)
    TextView bookingMode;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.attachment)
    TextView attachment;
    @BindView(R.id.viewAttachment)
    ImageView viewAttachment;
    private int RC_CAPTURE = 3, RC_PICK = 4, RC_CONVERT = 5;
    private File mFile = null;
    private Uri mFileUri = null;

    private TripBooking tripBooking;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int RC_PERMISSIONS = 1;
    private UserPref userPref;
    private Dialog dialog;
    private NetConnection connection;
    private RestApi api;
    private Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_bill);
        ButterKnife.bind(this);

        init();
        setToolbar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tripBooking = bundle.getParcelable(ConstantVariable.TRIP_BOOKINGS);
            if (tripBooking != null) {
                bookingMode.setText(tripBooking.getAdminBookingMode());
                String strDescription;
                if (tripBooking.getAdminBookingMode().equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode))) {
                    strDescription = tripBooking.getAdminVendor() + " | " + tripBooking.getAdminCityArea() + " | " + tripBooking.getAdminCheckIn() + " | " + tripBooking.getAdminCheckOut() + " | " + tripBooking.getTotalAmount();
                } else {
                    strDescription = tripBooking.getAdminVendor() + " | " + tripBooking.getAdminDeparture() + " | " + tripBooking.getAdminArrival() + " | " + tripBooking.getAdminSource() + " | " + tripBooking.getAdminDestination();
                }
                description.setText(strDescription);

                if (!tripBooking.getBookingAttachment().isEmpty()) {
                    viewAttachment.setVisibility(View.VISIBLE);
                    String fileName = tripBooking.getBookingAttachment();
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    attachment.setText(fileName);
                    attachment.setVisibility(View.VISIBLE);
                    attachment.setTextColor(resources.getColor(R.color.colorPrimary));
                }
            }

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
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.booking_bill));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @OnClick(R.id.viewAttachment)
    protected void onViewAttachment(){
        Intent intent = new Intent(this, ProjectListActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.trip));
        intent.putExtra(getResources().getString(R.string.project),tripBooking.getAdminVendor());
        intent.putExtra("file_url", tripBooking.getBookingAttachment());
        startActivity(intent);
    }

    @OnClick(R.id.submit)
    protected void onSubmit(){
        if (mFile != null){
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();
                Map<String, RequestBody> myMap = new HashMap<>();
                if (mFile != null) {
                    String extension = mFile.getName().substring(mFile.getName().lastIndexOf(".") + 1);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/" + extension), mFile);
                    myMap.put("file\"; filename=\"" + mFile.getName(), fileBody);
                }

                Log.d("Bill ","id "+tripBooking.getPaymentId());
                RequestBody rbProjectId = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(tripBooking.getPaymentId()));
                myMap.put(ConstantVariable.TripBooking.ID, rbProjectId);
                RequestBody rbCreatedById = RequestBody.create(MediaType.parse("text/plain"),userPref.getUserId());
                myMap.put(ConstantVariable.TripBooking.CREATED_BY_ID, rbCreatedById);
                RequestBody rbAction = RequestBody.create(MediaType.parse("text/plain"),"attach bill");
                myMap.put(ConstantVariable.MIX_ID.ACTION, rbAction);

                Call<StringResponse> bookingRequestApi = api.attachBillOnBooking(myMap);
                bookingRequestApi.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            StringResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                if (!stringResponse.isError()){
                                    startParentActivity();
                                }
                                showMessage(stringResponse.getMessage());
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
            }else {
                showMessage(resources.getString(R.string.internet_not_available));
            }
        }
    }

    private void startParentActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(Activity.RESULT_OK);
                finish();
            }
        },500);
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

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
