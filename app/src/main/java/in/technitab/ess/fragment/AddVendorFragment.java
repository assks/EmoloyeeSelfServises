package in.technitab.ess.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.activity.VendorListActivity;
import in.technitab.ess.adapter.VendorAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddVendorFragment extends Fragment implements RecyclerViewItemClickListener, SearchView.OnQueryTextListener {

    @BindView(R.id.vendorRecyclerView)
    RecyclerView vendorRecyclerView;
    Unbinder unbinder;
    private Activity mActivity;
    private Resources resources;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private ArrayList<Vendor> mVendorList;
    private VendorAdapter adapter;
    private SearchView searchView;
    private MenuItem myActionMenuItem;
    private Handler handler;
    private Runnable runnable;

    public AddVendorFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_vendor, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        fetVendorList("");
        return view;

    }

    private void fetVendorList(String s) {
        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<Vendor>> call = api.fetchVendorList(s);
            call.enqueue(new Callback<ArrayList<Vendor>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Vendor>> call, @NonNull Response<ArrayList<Vendor>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<Vendor> list = response.body();
                        if (list != null) {
                            mVendorList.addAll(list);
                            adapter.notifyDataSetChanged();
                            if (mVendorList.isEmpty()) {
                                showToast("No pending vendor created yet");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Vendor>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        dialog.dismissDialog();
                        showToast(mActivity.getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(mActivity.getResources().getString(R.string.internet_not_available));
        }
    }

    private void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        mActivity = getActivity();
        resources = getResources();
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        handler = new Handler();
        api = APIClient.getClient().create(RestApi.class);
        ((MainActivity) mActivity).setToolbar(resources.getString(R.string.vendor));
        ((MainActivity)mActivity).setToolBarSubtitle(null);

        vendorRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        vendorRecyclerView.setHasFixedSize(true);
        vendorRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mVendorList = new ArrayList<>();
        adapter = new VendorAdapter(mActivity,ConstantVariable.MIX_ID.VIEW, mVendorList);
        vendorRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }


    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, final int position) {
        /*VendorAdapter.ViewHolder holder = (VendorAdapter.ViewHolder) viewHolder;
        PopupMenu popup = new PopupMenu(mActivity, holder.actionButton);
        popup.getMenuInflater().inflate(R.menu.menu_vendor, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.view_detail) {
                    Intent intent = new Intent(mActivity, AddVendorActivity.class);
                    intent.putExtra(ConstantVariable.MIX_ID.VENDOR, mVendorList.get(position));
                    intent.putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.VIEW);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.pay_vendor){
                    Intent intent = new Intent(mActivity, PayVendorListActivity.class);
                    intent.putExtra(ConstantVariable.MIX_ID.ACTION,resources.getString(R.string.submit));
                    intent.putExtra(ConstantVariable.MIX_ID.VENDOR, mVendorList.get(position));
                    startActivity(intent);
                }

                return true;
            }
        });

        popup.show(); //showing popup menu
*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.vendor, menu);

        myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint(resources.getString(R.string.search_hint));
        searchView.setIconified(false);
        EditText searchEditText =  searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorIconText));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorSecondary));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (userPref.getAccessControlId() == 2) {
            MenuItem menuItem = menu.findItem(R.id.submit_vendor);
            menuItem.setTitle(resources.getString(R.string.add_vendor));
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if( ! searchView.isIconified()) {
            searchView.setIconified(true);
            adapter.getFilter().filter(query.trim());

        }
        myActionMenuItem.collapseActionView();



        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                adapter.getFilter().filter(newText.trim());
            }
        };
        handler.postDelayed(runnable, 500);
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.submit_vendor) {
                startActivity(new Intent(getActivity(), VendorListActivity.class).putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.submit)));
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
