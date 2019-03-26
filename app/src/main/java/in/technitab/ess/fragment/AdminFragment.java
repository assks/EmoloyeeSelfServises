package in.technitab.ess.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.activity.AddVendorActivity;
import in.technitab.ess.activity.AdminTECListActivity;

import in.technitab.ess.activity.AdminTripListActivity;
import in.technitab.ess.activity.AdminVendorActivity;
import in.technitab.ess.activity.ApproveLeaveActivity;
import in.technitab.ess.activity.ExistingProjectActivity;
import in.technitab.ess.activity.FetchCustomNameActivity;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.activity.RequestedProjectActivity;
import in.technitab.ess.util.ConstantVariable;


public class AdminFragment extends Fragment {

    Activity activity;
    Unbinder unbinder;

    public AdminFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        unbinder = ButterKnife.bind(this, view);

        activity = getActivity();
        if (activity != null) {
            ((MainActivity) activity).setToolbar(getResources().getString(R.string.admin));
            ((MainActivity)activity).setToolBarSubtitle(null);
        }

        return view;
    }


    @OnClick({R.id.assign_activity,R.id.assign_leaave,R.id.approve_timesheet,R.id.add_timesheet})
    protected void onViewClick(View view){
        String actonViewType = "";
        switch (view.getId()){

            case R.id.assign_activity:
                actonViewType = getResources().getString(R.string.project_name);
                break;

            case R.id.assign_leaave:
                actonViewType = getResources().getString(R.string.user_name);
                break;

            case R.id.approve_timesheet:
                actonViewType = getResources().getString(R.string.project);
                break;

            case R.id.add_timesheet:
                actonViewType = getResources().getString(R.string.add_timesheet);
                break;

        }

        startActivity(new Intent(getActivity(), FetchCustomNameActivity.class)
                .putExtra(ConstantVariable.MIX_ID.ACTION_VIEW_TYPE,actonViewType));
    }


    @OnClick({R.id.add_project,R.id.approve_project})
    protected void onAddProject(View view){
        String action = "";
        switch (view.getId()){

            case R.id.add_project:
                action = activity.getResources().getString(R.string.add);
                break;

            case R.id.approve_project:
                action = activity.getResources().getString(R.string.approve);
        }

        startActivity(new Intent(getActivity(), ExistingProjectActivity.class).putExtra(ConstantVariable.MIX_ID.ACTION,action));
    }

    @OnClick(R.id.approve_leave)
    protected void onApproveLeave(){
        startActivity(new Intent(getActivity(), ApproveLeaveActivity.class));
    }


    @OnClick(R.id.requestedProject)
    protected void onRequestedProject(){
        startActivity(new Intent(getActivity(), RequestedProjectActivity.class));
    }

    @OnClick(R.id.add_vendor)
    protected void onAddVendor(){
        startActivity(new Intent(getActivity(), AddVendorActivity.class).putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.ADD));
    }

    @OnClick(R.id.approve_vendor)
    protected void onApproveVendor(){
        startActivity(new Intent(getActivity(), AdminVendorActivity.class).putExtra(ConstantVariable.MIX_ID.ACTION,ConstantVariable.MIX_ID.APPROVE));
    }


    @OnClick(R.id.approve_tec)
    protected void OnApproveTEC(){
        startActivity(new Intent(getActivity(), AdminTECListActivity.class).putExtra(ConstantVariable.MIX_ID.ACTION,getResources().getString(R.string.approve_project)));
    }




    @OnClick(R.id.user_trip)
    protected void onUserTrip(){
        startActivity(new Intent(getActivity(), AdminTripListActivity.class).putExtra(ConstantVariable.MIX_ID.ACTION,getResources().getString(R.string.approve)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
