package in.technitab.ess.services.repository;

import java.util.List;

import in.technitab.ess.model.GuesthouseBooking;

public interface GuesthouseContract {
    interface Model {

        interface OnFinishedListener {
            void onFinished(List<GuesthouseBooking> bookingList);
            void onUnSuccess(int errorCode,String message);
            void onFailure(Throwable t);
        }

        void getBookingList(OnFinishedListener onFinishedListener,String userId, int pageNo);

    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(List<GuesthouseBooking> movieArrayList);

        void onResponseFailure(Throwable throwable);

        void onFailure(int code,String message);

    }

    interface Presenter {

        void onDestroy();

        void getMoreData(int pageNo);

        void requestDataFromServer();

    }
}
