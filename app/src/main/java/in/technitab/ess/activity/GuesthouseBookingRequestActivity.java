package in.technitab.ess.activity;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.technitab.ess.R;
import in.technitab.ess.adapter.SpinAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.databinding.ActivityGuesthouseBookingRequestBinding;
import in.technitab.ess.model.AddResponse;
import in.technitab.ess.model.GuesthouseBooking;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.DateCal;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuesthouseBookingRequestActivity extends AppCompatActivity {

    ActivityGuesthouseBookingRequestBinding binding;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private Resources resources;
    private MyClickHandlers handlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guesthouse_booking_request);

        init();
        setToolbar();

    }

    private void init() {
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        resources = getResources();
        api = APIClient.getClient().create(RestApi.class);


        new CustomDate(binding.checkIn, this, null, null);
        new CustomDate(binding.checkOut, this, null, null);
        binding.checkIn.addTextChangedListener(new MyWatcher(binding.checkIn));
        binding.checkOut.addTextChangedListener(new MyWatcher(binding.checkOut));

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.paidViaArray));
        List<Object> mList = new ArrayList<>();
        mList.addAll(list);
        SpinAdapter spinAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, mList);
        binding.paidViaSpinner.setAdapter(spinAdapter);

        handlers = new MyClickHandlers(this);
        binding.setHandlers(handlers);
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Guesthouse Booking");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    public class MyClickHandlers {
        Context context;

        public MyClickHandlers(Context context) {
            this.context = context;
        }

        public void onSubmit(View view) {
            String strCheckIn = binding.checkIn.getText().toString().trim();
            String strCheckOut = binding.checkOut.getText().toString().trim();
            String strRefernceNum = binding.referenceNumber.getText().toString().trim();
            String strPaidVia = binding.paidViaSpinner.getSelectedItem().toString();
            boolean isRoomCheck = binding.availabiltyCheck.isChecked();
            boolean isRuleCheck = binding.guesthouseRuleCheck.isChecked();

            GuesthouseBooking guesthouseBooking = new GuesthouseBooking(strCheckIn, strCheckOut, strPaidVia, strRefernceNum, isRoomCheck, isRuleCheck);

            StringResponse validateResponse = guesthouseBooking.validate(guesthouseBooking);
            if (!validateResponse.isError()) {
                guesthouseBooking.setCreatedById(Integer.parseInt(userPref.getUserId()));
                Gson gson = new Gson();
                String bookingJson = gson.toJson(guesthouseBooking);
                if (connection.isNetworkAvailable(context)) {
                    dialog.showDialog();
                    Call<AddResponse> call = api.guesthouseBooking("booking", bookingJson);
                    call.enqueue(new Callback<AddResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<AddResponse> call, @NonNull Response<AddResponse> response) {
                            dialog.dismissDialog();
                            if (response.isSuccessful()) {
                                AddResponse stringResponse = response.body();
                                if (stringResponse != null) {
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
                                showMessage(resources.getString(R.string.slow_internet_connection));
                            }
                        }
                    });
                } else {
                    showMessage(resources.getString(R.string.internet_not_available));
                }


            } else {
                showMessage(validateResponse.getMessage());
            }


        }


    }

    private class MyWatcher implements TextWatcher {

        private View view;

        private MyWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            switch (view.getId()) {

                case R.id.checkIn:
                    String date = binding.checkIn.getText().toString().trim();
                    if (!date.isEmpty())
                        new CustomDate(binding.checkOut, GuesthouseBookingRequestActivity.this, date, null);
                    calculateNights();
                    break;


                case R.id.checkOut:
                    calculateNights();
                    break;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }


    private void calculateNights() {
        String strHotelFirst = binding.checkIn.getText().toString().trim();
        String strHotelLast = binding.checkOut.getText().toString().trim();
        if (strHotelFirst.isEmpty() || strHotelLast.isEmpty()) {
            binding.totalAmount.setVisibility(View.GONE);
        } else {
            binding.totalAmount.setVisibility(View.VISIBLE);
            String strTotalAmount = "Rent amount " + String.valueOf(100 * DateCal.getDays(strHotelFirst, strHotelLast));
            binding.totalAmount.setText(strTotalAmount);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
