package in.technitab.ess.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.activity.AssignedAssetActivity;
import in.technitab.ess.activity.GuestBookingListActivity;
import in.technitab.ess.activity.GuesthouseBookingRequestActivity;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.activity.OrgFixAssetActivity;
import in.technitab.ess.activity.PolicyActivity;
import in.technitab.ess.activity.SalarySlipActivity;
import in.technitab.ess.util.ConstantVariable;


public class ResourceFragment extends Fragment {

    Unbinder unbinder;

    public ResourceFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        unbinder = ButterKnife.bind(this, view);

        Activity activity = getActivity();
        if (activity != null) {
            ((MainActivity) activity).setToolbar(getResources().getString(R.string.resource));
            ((MainActivity)activity).setToolBarSubtitle(null);
        }
        return view;
    }

    @OnClick(R.id.policies)
    protected void onPolicies(){
        startActivity(new Intent(getActivity(), PolicyActivity.class).putExtra(ConstantVariable.MIX_ID.VIEW_TYPE,getResources().getString(R.string.policies)));
    }


    @OnClick(R.id.fixAsset)
    protected void onFixAsset(){
        startActivity(new Intent(getActivity(), AssignedAssetActivity.class));
    }

    @OnClick(R.id.salarySlip)
    protected void onSalary(){
        startActivity(new Intent(getActivity(), SalarySlipActivity.class));
    }

    @OnClick(R.id.guesthouseBooking)
    protected void onGuesthouseBooking(){
        startActivity(new Intent(getActivity(), GuestBookingListActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
