package in.technitab.ess.fragment;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.SalarySlip;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayrollFragment extends Fragment {


    @BindView(R.id.payment_month)
    TextView paymentMonth;
    @BindView(R.id.payperiod)
    TextView payperiod;
    @BindView(R.id.pay_day)
    TextView payDay;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.employee_code)
    TextView employeeCode;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.pan_num)
    TextView panNum;
    @BindView(R.id.esic)
    TextView esic;
    @BindView(R.id.pf_num)
    TextView pfNum;
    @BindView(R.id.calc_month_days)
    TextView calcMonthDays;
    @BindView(R.id.available_leave)
    TextView availableLeave;
    @BindView(R.id.lwp)
    TextView lwp;
    @BindView(R.id.availed_el)
    TextView availedEl;
    @BindView(R.id.present_days)
    TextView presentDays;
    @BindView(R.id.balance_el)
    TextView balanceEl;
    @BindView(R.id.total_payable_day)
    TextView totalPayableDay;
    @BindView(R.id.current_el)
    TextView currentEl;
    @BindView(R.id.gross_basic)
    TextView grossBasic;
    @BindView(R.id.ctc_basic)
    TextView ctcBasic;
    @BindView(R.id.gross_hra)
    TextView grossHra;
    @BindView(R.id.ctc_hra)
    TextView ctcHra;
    @BindView(R.id.gross_ca)
    TextView grossCa;
    @BindView(R.id.ctc_ca)
    TextView ctcCa;
    @BindView(R.id.gross_ma)
    TextView grossMa;
    @BindView(R.id.ctc_ma)
    TextView ctcMa;
    @BindView(R.id.gross_sa)
    TextView grossSa;
    @BindView(R.id.ctc_sa)
    TextView ctcSa;
    @BindView(R.id.gross_lta)
    TextView grossLta;
    @BindView(R.id.ctc_lta)
    TextView ctcLta;
    @BindView(R.id.ctc_ea)
    TextView ctcEa;
    @BindView(R.id.gross_ea)
    TextView grossEa;
    @BindView(R.id.gross_oa)
    TextView grossOa;
    @BindView(R.id.ctc_oa)
    TextView ctcOa;
    @BindView(R.id.gross_ap)
    TextView grossAp;
    @BindView(R.id.ctc_ap)
    TextView ctcAp;
    @BindView(R.id.ctc_total)
    TextView ctcTotal;
    @BindView(R.id.gross_total)
    TextView grossTotal;
    @BindView(R.id.pf_amount)
    TextView pfAmount;
    @BindView(R.id.esic_amount)
    TextView esicAmount;
    @BindView(R.id.insure_medi_amount)
    TextView insureMediAmount;
    @BindView(R.id.advance_claim)
    TextView advanceClaim;
    @BindView(R.id.income_tax_amount)
    TextView incomeTaxAmount;
    @BindView(R.id.pt_amount)
    TextView ptAmount;
    @BindView(R.id.total_deduction_amount)
    TextView totalDeductionAmount;
    @BindView(R.id.net_amount)
    TextView netAmount;
    Unbinder unbinder;
    @BindView(R.id.rootLayout)
    NestedScrollView rootLayout;

    private UserPref userPref;
    private Dialog dialog;
    private NetConnection connection;
    private RestApi api;
    private Resources resources;
    private String selectYear = "", selectedMonth = "";
    private Calendar calendar = Calendar.getInstance();
    private Activity activity;

    public PayrollFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payroll, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month == 1?11:month;
        selectedMonth = month < 10 ? "0" + month : "" + month;
        selectYear = String.valueOf(year);
        getSalarySlip(selectYear, selectedMonth);

        setToolBarSubtitle(selectedMonth,selectYear);

        return view;
    }

    private void setToolBarSubtitle(String month,String year) {
        ((MainActivity)activity).setToolbar(resources.getString(R.string.salary_slip));
        ((MainActivity)activity).setToolBarSubtitle(getYearMonth(month,year));
    }

    private void init() {
        activity = getActivity();
        resources = activity.getResources();
        userPref = new UserPref(activity);
        connection = new NetConnection();
        dialog = new Dialog(activity);
        api = APIClient.getClient().create(RestApi.class);
    }

    private String getYearMonth(String selectedMonth, String selectYear) {
        String str = selectYear + "-" + selectedMonth;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        try {
            Date d = format.parse(str);
            SimpleDateFormat strFormat = new SimpleDateFormat("yyyy MMMM", Locale.getDefault());
            str = strFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    private void getSalarySlip(String selectYear, String selectedMonth) {
        if (connection.isNetworkAvailable(activity)) {
            dialog.showDialog();
            Call<SalarySlip> call = api.fetchUserSalarySlip(getYearMonth(selectedMonth,selectYear), userPref.getUserId());
            call.enqueue(new Callback<SalarySlip>() {
                @Override
                public void onResponse(@NonNull Call<SalarySlip> call, @NonNull Response<SalarySlip> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {

                        SalarySlip salarySlip = response.body();
                        if (salarySlip != null) {
                            showData(salarySlip);
                        }

                    } else {
                        showMessage(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SalarySlip> call, @NonNull Throwable t) {
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

    private void showData(SalarySlip salarySlip) {
        paymentMonth.setText(resources.getString(R.string.payment_slip,getYearMonth(selectedMonth,selectYear)));
        payperiod.setText(salarySlip.getPayPeriod());
        payDay.setText(salarySlip.getPayDay());
        name.setText(salarySlip.getName());
        employeeCode.setText(salarySlip.getEmpId());
        designation.setText(salarySlip.getDesignation());
        panNum.setText(salarySlip.getPanNum());
        esic.setText(salarySlip.getESICNum());
        pfNum.setText(salarySlip.getPfNum());
        calcMonthDays.setText(salarySlip.getCalcMonthDays());
        availableLeave.setText(salarySlip.getAvailableLeave());
        lwp.setText(salarySlip.getLwpNjDays());
        availedEl.setText(salarySlip.getAvailedElMonth());
        presentDays.setText(salarySlip.getPresentDays());
        balanceEl.setText(salarySlip.getBalanceLeave());
        totalPayableDay.setText(salarySlip.getTotalDayPayable());
        currentEl.setText(salarySlip.getCurrentBalance());
        grossBasic.setText(salarySlip.getPayableBasicSal());
        ctcBasic.setText(salarySlip.getCtcBasicSal());
        ctcHra.setText(salarySlip.getCtcHouseRentAllowance());
        grossHra.setText(salarySlip.getPayableHouseRentAllowance());
        ctcCa.setText(salarySlip.getCtcCA());
        grossCa.setText(salarySlip.getGrossCA());
        ctcMa.setText(salarySlip.getCtcMedicalAllowance());
        grossMa.setText(salarySlip.getPayableMedicalAllowance());
        ctcSa.setText(salarySlip.getCtcSpecialAllowance());
        grossSa.setText(salarySlip.getPayableSpecialAllowance());
       // ctcLta.setText(salarySlip.getCtcLTA());
       // grossLta.setText(salarySlip.getPayableLTA());
       // ctcEa.setText(salarySlip.getCtcEduAllowance());
        //grossEa.setText(salarySlip.getPayableEduAllowance());
        ctcOa.setText(salarySlip.getCtcOtherAllowance());
        grossOa.setText(salarySlip.getPayableOtherAllowance());
        ctcAp.setText(salarySlip.getCtcArrearsPayment());
        grossAp.setText(salarySlip.getPayableArrearsPayment());
        ctcTotal.setText(salarySlip.getCtcGrossTotal());
        grossTotal.setText(salarySlip.getPayableGrossTotal());
        pfAmount.setText(salarySlip.getProvidentFund());
        esicAmount.setText(salarySlip.getEsic());
        advanceClaim.setText(salarySlip.getAdvanceLoan());
        insureMediAmount.setText(salarySlip.getInsuranceMedicalBenefit());
        incomeTaxAmount.setText(salarySlip.getIncomeTax());
        ptAmount.setText(salarySlip.getPF());
        totalDeductionAmount.setText(salarySlip.getTotalDeduction());
        netAmount.setText(resources.getString(R.string.net_amount_payable,salarySlip.getNetAmountPayable()));
    }

    private void showMessage(String message) {
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_timesheet_fliter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            showFilterDialog();
            return true;
        } else
            return super.onOptionsItemSelected(item);

    }


    private void showFilterDialog() {
        final String[] yearLists = getResources().getStringArray(R.array.yearArray);
        selectYear = "2018";
        selectedMonth = yearLists[0];
        final AlertDialog builder = new AlertDialog.Builder(activity).create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_year_month_picker, null);
        builder.setView(view);

        final NumberPicker month = view.findViewById(R.id.month);
        NumberPicker year = view.findViewById(R.id.year);
        Button done = view.findViewById(R.id.done);
        Button cancel = view.findViewById(R.id.cancel);

        month.setMinValue(0);
        month.setMaxValue(11);
        month.setDisplayedValues(yearLists);

        year.setMinValue(2016);
        year.setMaxValue(2018);

        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectYear = String.valueOf(newVal);
            }
        });


        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newVal += 1;
                selectedMonth = newVal < 10 ? "0" + newVal : "" + newVal;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                setToolBarSubtitle(selectedMonth, selectYear);
                getSalarySlip(selectYear, selectedMonth);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        builder.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
