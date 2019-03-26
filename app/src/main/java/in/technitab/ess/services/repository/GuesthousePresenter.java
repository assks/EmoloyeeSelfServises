package in.technitab.ess.services.repository;

import java.util.List;

import in.technitab.ess.model.GuesthouseBooking;

public class GuesthousePresenter implements GuesthouseContract.Presenter, GuesthouseContract.Model.OnFinishedListener {

    private GuesthouseContract.View guesthouseListView;

    private GuesthouseBookingRepo guesthouseListModel;


    public GuesthousePresenter(GuesthouseContract.View guesthouseListView) {
        this.guesthouseListView = guesthouseListView;
        guesthouseListModel = new GuesthouseBookingRepo();
    }

    @Override
    public void onDestroy() {
        this.guesthouseListView = null;
    }

    @Override
    public void getMoreData(int pageNo) {

        if (guesthouseListView != null) {
            guesthouseListView.showProgress();
        }
        guesthouseListModel.getBookingList(this, "100", 0);
    }

    @Override
    public void requestDataFromServer() {

        if (guesthouseListView != null) {
            guesthouseListView.showProgress();
        }
        guesthouseListModel.getBookingList(this, "100", 0);
    }

    @Override
    public void onFinished(List<GuesthouseBooking> guesthouseBookingList) {
        guesthouseListView.setDataToRecyclerView(guesthouseBookingList);
        if (guesthouseListView != null) {
            guesthouseListView.hideProgress();
        }
    }

    @Override
    public void onUnSuccess(int errorCode, String message) {
        guesthouseListView.onFailure(errorCode, message);
        if (guesthouseListView != null)
            guesthouseListView.hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        guesthouseListView.onResponseFailure(t);
        if (guesthouseListView != null) {
            guesthouseListView.hideProgress();
        }
    }
}
