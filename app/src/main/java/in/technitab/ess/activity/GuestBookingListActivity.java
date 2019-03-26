package in.technitab.ess.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import in.technitab.ess.R;
import in.technitab.ess.adapter.GuesthouseAdapter;
import in.technitab.ess.databinding.ActivityGuestBookingListBinding;
import in.technitab.ess.model.GuesthouseBooking;
import in.technitab.ess.services.repository.GuesthouseContract;
import in.technitab.ess.services.repository.GuesthousePresenter;

public class GuestBookingListActivity extends AppCompatActivity implements GuesthouseContract.View {

    ActivityGuestBookingListBinding binding;
    private GuesthousePresenter movieListPresenter;
    private List<GuesthouseBooking> guesthouseBookingList;
    private GuesthouseAdapter adapter;
    private Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_guest_booking_list);

        initUI();
        setToolbar();

        movieListPresenter = new GuesthousePresenter(this);
        movieListPresenter.requestDataFromServer();
    }


    private void initUI() {
        resources = getResources();
        guesthouseBookingList = new ArrayList<>();
        adapter = new GuesthouseAdapter(guesthouseBookingList);
        binding.bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.bookingRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.bookingRecyclerView.setAdapter(adapter);

    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.bookings));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void showProgress() {
        binding.pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        binding.pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(List<GuesthouseBooking> list) {
        guesthouseBookingList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        binding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(int code, String message) {
        binding.emptyView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request_leave, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_request);
        menuItem.setTitle(resources.getString(R.string.booking));
        menuItem.setIcon(resources.getDrawable(R.drawable.ic_add));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_request) {
            startActivityForResult(new Intent(this, GuesthouseBookingRequestActivity.class),1);
            return true;
        } else
            return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
