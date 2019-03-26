package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class SalarySlip {
    @SerializedName("id")
    private String id;
    @SerializedName("pay_period")
    private String  payPeriod ;
    @SerializedName("pay_day")
    private String payDay;
    @SerializedName("name")
    private String  name ;
    @SerializedName("emp_id")
    private String  empId ;
    @SerializedName("designation")
    private String  designation ;
    @SerializedName("pan_num")
    private String  panNum ;
    @SerializedName("esic_num")
    private String  ESICNum ;
    @SerializedName("pf_num")
    private String  pfNum ;
    @SerializedName("calc_month_days")
    private String  calcMonthDays ;
    @SerializedName("lwp_nj_days")
    private String  lwpNjDays ;
    @SerializedName("present_days")
    private String  presentDays ;
    @SerializedName("total_day_payable")
    private String  totalDayPayable ;
    @SerializedName("available_leave")
    private String  availableLeave ;
    @SerializedName("availed_el_month")
    private String  availedElMonth ;
    @SerializedName("balance_leave")
    private String  balanceLeave ;
    @SerializedName("current_balance")
    private String  currentBalance ;
    @SerializedName("ctc_basic_sal")
    private String  ctcBasicSal ;
    @SerializedName("ctc_house_rent_allowance")
    private String  ctcHouseRentAllowance ;
    @SerializedName("ctc_medical_allowance")
    private String  ctcMedicalAllowance ;
    @SerializedName("ctc_special_allowance")
    private String  ctcSpecialAllowance ;
    @SerializedName("ctc_l_t_a")
    private String  ctcLTA ;
    @SerializedName("ctc_edu_allowance")
    private String  ctcEduAllowance ;
    @SerializedName("ctc_other_allowance")
    private String  ctcOtherAllowance ;
    @SerializedName("ctc_arrears_payment")
    private String  ctcArrearsPayment ;
    @SerializedName("ctc_ot_payment")
    private String  ctcOtPayment ;
    @SerializedName("provident_fund")
    private String  providentFund ;
    @SerializedName("esic")
    private String  esic ;
    @SerializedName("advance_loan")
    private String  advanceLoan ;
    @SerializedName("insurance_medical_benefit")
    private String  insuranceMedicalBenefit ;
    @SerializedName("income_tax")
    private String  incomeTax ;
    @SerializedName("professional_tax")
    private String  professionalTax ;
    @SerializedName("total_deduction")
    private String  totalDeduction ;
    @SerializedName("ctc_gross_total")
    private String  ctcGrossTotal ;
    @SerializedName("net_amount_payable")
    private String  netAmountPayable ;
    @SerializedName("payable_basic_sal")
    private String  payableBasicSal ;
    @SerializedName("payable_house_rent_allowance")
    private String  payableHouseRentAllowance ;
    @SerializedName("payable_medical_allowance")
    private String  payableMedicalAllowance ;
    @SerializedName("payable_special_allowance")
    private String  payableSpecialAllowance ;
    @SerializedName("payable_l_t_a")
    private String  payableLTA ;
    @SerializedName("payable_edu_allowance")
    private String  payableEduAllowance ;
    @SerializedName("payable_other_allowance")
    private String  payableOtherAllowance ;
    @SerializedName("payable_arrears_payment")
    private String  payableArrearsPayment ;
    @SerializedName("payable_ot_payment")
    private String  payableOtPayment ;
    @SerializedName("payable_gross_total")
    private String  payableGrossTotal;
    @SerializedName("ctc_ca")
    private String ctcCA;
    @SerializedName("gross_ca")
    private String grossCA;


    public SalarySlip(String id, String payPeriod, String payDay, String name, String empId, String designation, String panNum, String ESICNum, String pfNum, String calcMonthDays, String lwpNjDays, String presentDays, String totalDayPayable, String availableLeave, String availedElMonth, String balanceLeave, String currentBalance, String ctcBasicSal, String ctcHouseRentAllowance, String ctcMedicalAllowance, String ctcSpecialAllowance, String ctcLTA, String ctcEduAllowance, String ctcOtherAllowance, String ctcArrearsPayment, String ctcOtPayment, String providentFund, String esic, String advanceLoan, String insuranceMedicalBenefit, String incomeTax, String PF, String totalDeduction, String ctcGrossTotal, String netAmountPayable, String payableBasicSal, String payableHouseRentAllowance, String payableMedicalAllowance, String payableSpecialAllowance, String payableLTA, String payableEduAllowance, String payableOtherAllowance, String payableArrearsPayment, String payableOtPayment, String payableGrossTotal, String ctcCA, String grossCA) {
        this.id = id;
        this.payPeriod = payPeriod;
        this.payDay = payDay;
        this.name = name;
        this.empId = empId;
        this.designation = designation;
        this.panNum = panNum;
        this.ESICNum = ESICNum;
        this.pfNum = pfNum;
        this.calcMonthDays = calcMonthDays;
        this.lwpNjDays = lwpNjDays;
        this.presentDays = presentDays;
        this.totalDayPayable = totalDayPayable;
        this.availableLeave = availableLeave;
        this.availedElMonth = availedElMonth;
        this.balanceLeave = balanceLeave;
        this.currentBalance = currentBalance;
        this.ctcBasicSal = ctcBasicSal;
        this.ctcHouseRentAllowance = ctcHouseRentAllowance;
        this.ctcMedicalAllowance = ctcMedicalAllowance;
        this.ctcSpecialAllowance = ctcSpecialAllowance;
        this.ctcLTA = ctcLTA;
        this.ctcEduAllowance = ctcEduAllowance;
        this.ctcOtherAllowance = ctcOtherAllowance;
        this.ctcArrearsPayment = ctcArrearsPayment;
        this.ctcOtPayment = ctcOtPayment;
        this.providentFund = providentFund;
        this.esic = esic;
        this.advanceLoan = advanceLoan;
        this.insuranceMedicalBenefit = insuranceMedicalBenefit;
        this.incomeTax = incomeTax;
        this.professionalTax = PF;
        this.totalDeduction = totalDeduction;
        this.ctcGrossTotal = ctcGrossTotal;
        this.netAmountPayable = netAmountPayable;
        this.payableBasicSal = payableBasicSal;
        this.payableHouseRentAllowance = payableHouseRentAllowance;
        this.payableMedicalAllowance = payableMedicalAllowance;
        this.payableSpecialAllowance = payableSpecialAllowance;
        this.payableLTA = payableLTA;
        this.payableEduAllowance = payableEduAllowance;
        this.payableOtherAllowance = payableOtherAllowance;
        this.payableArrearsPayment = payableArrearsPayment;
        this.payableOtPayment = payableOtPayment;
        this.payableGrossTotal = payableGrossTotal;
        this.ctcCA = ctcCA;
        this.grossCA = grossCA;
    }

    public String getESICNum() {
        return ESICNum;
    }

    public String getPfNum() {
        return pfNum;
    }


    public String getCtcCA() {
        return ctcCA;
    }

    public String getGrossCA() {
        return grossCA;
    }

    public String getId() {
        return id;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public String getPayDay() {
        return payDay;
    }

    public String getName() {
        return name;
    }

    public String getEmpId() {
        return empId;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPanNum() {
        return panNum;
    }

    public String getCalcMonthDays() {
        return calcMonthDays;
    }

    public String getLwpNjDays() {
        return lwpNjDays;
    }

    public String getPresentDays() {
        return presentDays;
    }

    public String getTotalDayPayable() {
        return totalDayPayable;
    }

    public String getAvailableLeave() {
        return availableLeave;
    }

    public String getAvailedElMonth() {
        return availedElMonth;
    }

    public String getBalanceLeave() {
        return balanceLeave;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public String getCtcBasicSal() {
        return ctcBasicSal;
    }

    public String getCtcHouseRentAllowance() {
        return ctcHouseRentAllowance;
    }

    public String getCtcMedicalAllowance() {
        return ctcMedicalAllowance;
    }

    public String getCtcSpecialAllowance() {
        return ctcSpecialAllowance;
    }

    public String getCtcLTA() {
        return ctcLTA;
    }

    public String getCtcEduAllowance() {
        return ctcEduAllowance;
    }

    public String getCtcOtherAllowance() {
        return ctcOtherAllowance;
    }

    public String getCtcArrearsPayment() {
        return ctcArrearsPayment;
    }

    public String getCtcOtPayment() {
        return ctcOtPayment;
    }

    public String getProvidentFund() {
        return providentFund;
    }

    public String getEsic() {
        return esic;
    }

    public String getAdvanceLoan() {
        return advanceLoan;
    }

    public String getInsuranceMedicalBenefit() {
        return insuranceMedicalBenefit;
    }

    public String getIncomeTax() {
        return incomeTax;
    }

    public String getPF() {
        return professionalTax;
    }

    public String getTotalDeduction() {
        return totalDeduction;
    }

    public String getCtcGrossTotal() {
        return ctcGrossTotal;
    }

    public String getNetAmountPayable() {
        return netAmountPayable;
    }

    public String getPayableBasicSal() {
        return payableBasicSal;
    }

    public String getPayableHouseRentAllowance() {
        return payableHouseRentAllowance;
    }

    public String getPayableMedicalAllowance() {
        return payableMedicalAllowance;
    }

    public String getPayableSpecialAllowance() {
        return payableSpecialAllowance;
    }

    public String getPayableLTA() {
        return payableLTA;
    }

    public String getPayableEduAllowance() {
        return payableEduAllowance;
    }

    public String getPayableOtherAllowance() {
        return payableOtherAllowance;
    }

    public String getPayableArrearsPayment() {
        return payableArrearsPayment;
    }

    public String getPayableOtPayment() {
        return payableOtPayment;
    }

    public String getPayableGrossTotal() {
        return payableGrossTotal;
    }
}
