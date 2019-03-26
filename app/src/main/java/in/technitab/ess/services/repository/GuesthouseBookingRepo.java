package in.technitab.ess.services.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.GuesthouseBooking;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GuesthouseBookingRepo implements GuesthouseContract.Model {

    @Override
    public void getBookingList(final OnFinishedListener onFinishedListener, String userId, int pageNo) {
        RestApi api = APIClient.getClient().create(RestApi.class);
        api.fetchBookingList("self_booking", userId).enqueue(new Callback<List<GuesthouseBooking>>() {
            @Override
            public void onResponse(@NonNull Call<List<GuesthouseBooking>> call, @NonNull Response<List<GuesthouseBooking>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        onFinishedListener.onFinished(response.body());
                    }
                }else{
                    onFinishedListener.onUnSuccess(response.code(),response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GuesthouseBooking>> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
