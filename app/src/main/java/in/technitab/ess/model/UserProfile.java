package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.technitab.ess.util.ConstantVariable;

public class UserProfile {
    @SerializedName(ConstantVariable.UserProfile.NAME)
    private String name;
    @SerializedName(ConstantVariable.UserProfile.DESIGNATION)
    private String designation;
    @SerializedName(ConstantVariable.UserProfile.EMERGENCY_NUMBER)
    private String emergencyNumber;
    @SerializedName(ConstantVariable.UserProfile.MOBILE_NUMBER)
    private String mobileNumber;
    @SerializedName(ConstantVariable.UserProfile.OFFICIAL_EMAIL_ID)
    private String officialEmailId;
    @SerializedName(ConstantVariable.UserProfile.PERSONAL_EMAIL_ID)
    private String personalEmailId;
    @SerializedName(ConstantVariable.UserProfile.CURRENT_FULL_ADDRES)
    private String currentFullAddress;
    @SerializedName(ConstantVariable.UserProfile.PERMANENT_FULL_ADDRESS)
    private String permanentFullAddress;
    @SerializedName(ConstantVariable.UserProfile.BLOOD_GROUP)
    private String bloodGroup;
    @SerializedName(ConstantVariable.UserProfile.BIRTH_DATE)
    private String birthDate;
    @SerializedName(ConstantVariable.UserProfile.MARITAL_STATUS)
    private String maritalStatus;
    @SerializedName(ConstantVariable.UserProfile.MARRIAGE_DATE)
    private String marriageDate;
    @SerializedName(ConstantVariable.UserProfile.FATHER)
    private String father;
    @SerializedName(ConstantVariable.UserProfile.SPOUSE)
    private String spouse;
    @SerializedName(ConstantVariable.UserProfile.NATIONALITY)
    private String nationality;
    @SerializedName(ConstantVariable.UserProfile.RELIGION)
    private String religion;
    @SerializedName(ConstantVariable.UserProfile.GENDER)
    private String gender;
    @SerializedName(ConstantVariable.UserProfile.BASE_OFFICE_LOCATION)
    private String baseOfficeLocation;
    @SerializedName(ConstantVariable.UserProfile.REPORTING_TO)
    private String reportingTo;
    @SerializedName(ConstantVariable.UserProfile.JOINING_DATE)
    private String joiningDate;
    @SerializedName(ConstantVariable.UserProfile.APPOINTMENT_DATE)
    private String appointmentDate;
    @SerializedName(ConstantVariable.UserProfile.PAN_NUMBER)
    private String panNumber;
    @SerializedName(ConstantVariable.UserProfile.PASSPORT_NUMBER)
    private String passportNumber;
    @SerializedName(ConstantVariable.UserProfile.AADHAR_NUMBER)
    private String aadharNumber;
    @SerializedName(ConstantVariable.UserProfile.DRIVING_LICENSE_NUMBER)
    private String drivingLicenseNumber;
    @SerializedName(ConstantVariable.UserProfile.VOTER_ID_NUMBER)
    private String voterIdNumber;
    @SerializedName(ConstantVariable.UserProfile.BANK_NAME)
    private String bankName;
    @SerializedName(ConstantVariable.UserProfile.BANK_ADDRESS)
    private String bankAddress;
    @SerializedName(ConstantVariable.UserProfile.ACCOUNT_NUMER)
    private String accountNumer;
    @SerializedName(ConstantVariable.UserProfile.IFSC_CODE)
    private String ifscCode;
    @SerializedName(ConstantVariable.UserProfile.TENTH_YEAR)
    private String TenYear;
    @SerializedName(ConstantVariable.UserProfile.TENTH_SCHOOL)
    private String TenSchool;
    @SerializedName(ConstantVariable.UserProfile.TENTH_BOARD)
    private String TenBoard;
    @SerializedName(ConstantVariable.UserProfile.TENTH_PERCENTAGE)
    private String TenPercentage;
    @SerializedName(ConstantVariable.UserProfile.TWELVE_YEAR)
    private String TwelveYear;
    @SerializedName(ConstantVariable.UserProfile.TWELVE_SCHOOL)
    private String TwelveSchool;
    @SerializedName(ConstantVariable.UserProfile.TWELVE_BOARD)
    private String TwelveBoard;
    @SerializedName(ConstantVariable.UserProfile.TWELVE_PERCENTAGE)
    private String TwelvePercentage;
    @SerializedName(ConstantVariable.UserProfile.DIPLOMA_YEAR)
    private String DiplomaYear;
    @SerializedName(ConstantVariable.UserProfile.DIPLOMA_SCHOOL)
    private String DiplomaSchool;
    @SerializedName(ConstantVariable.UserProfile.DIPLOMA_BOARD)
    private String DiplomaBoard;
    @SerializedName(ConstantVariable.UserProfile.DIPLOMA_PERCENTAGE)
    private String DiplomaPercentage;
    @SerializedName(ConstantVariable.UserProfile.GRAD_YEAR)
    private String GradYear;
    @SerializedName(ConstantVariable.UserProfile.GRAD_SCHOOL)
    private String GradSchool;
    @SerializedName(ConstantVariable.UserProfile.GRAD_BOARD)
    private String GradBoard;
    @SerializedName(ConstantVariable.UserProfile.GRAD_PERCENTAGE)
    private String GradPercentage;
    @SerializedName(ConstantVariable.UserProfile.POST_GRAD_YEAR)
    private String PostGradYear;
    @SerializedName(ConstantVariable.UserProfile.POST_GRAD_SCHOOL)
    private String PostGradSchool;
    @SerializedName(ConstantVariable.UserProfile.POST_GRAD_BOARD)
    private String PostGradBoard;
    @SerializedName(ConstantVariable.UserProfile.POST_GRAD_PERCENTAGE)
    private String PostGradPercentage;

    public UserProfile(String name, String designation, String emergencyNumber, String mobileNumber, String officialEmailId, String personalEmailId, String currentFullAddress, String permanentFullAddress, String bloodGroup, String birthDate, String maritalStatus, String marriageDate, String father, String spouse, String nationality, String religion, String gender, String baseOfficeLocation,String reportingTo, String joiningDate, String appointmentDate, String panNumber, String passportNumber, String aadharNumber, String drivingLicenseNumber, String voterIdNumber, String bankName, String bankAddress, String accountNumer, String ifscCode, String tenYear, String tenSchool, String tenBoard, String tenPercentage, String twelveYear, String twelveSchool, String twelveBoard, String twelvePercentage, String diplomaYear, String diplomaSchool, String diplomaBoard, String diplomaPercentage, String gradYear, String gradSchool, String gradBoard, String gradPercentage, String postGradYear, String postGradSchool, String postGradBoard, String postGradPercentage) {
        this.name = name;
        this.designation = designation;
        this.emergencyNumber = emergencyNumber;
        this.mobileNumber = mobileNumber;
        this.officialEmailId = officialEmailId;
        this.personalEmailId = personalEmailId;
        this.currentFullAddress = currentFullAddress;
        this.permanentFullAddress = permanentFullAddress;
        this.bloodGroup = bloodGroup;
        this.birthDate = birthDate;
        this.maritalStatus = maritalStatus;
        this.marriageDate = marriageDate;
        this.father = father;
        this.spouse = spouse;
        this.nationality = nationality;
        this.religion = religion;
        this.gender = gender;
        this.baseOfficeLocation = baseOfficeLocation;
        this.reportingTo = reportingTo;
        this.joiningDate = joiningDate;
        this.appointmentDate = appointmentDate;
        this.panNumber = panNumber;
        this.passportNumber = passportNumber;
        this.aadharNumber = aadharNumber;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.voterIdNumber = voterIdNumber;
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.accountNumer = accountNumer;
        this.ifscCode = ifscCode;
        TenYear = tenYear;
        TenSchool = tenSchool;
        TenBoard = tenBoard;
        TenPercentage = tenPercentage;
        TwelveYear = twelveYear;
        TwelveSchool = twelveSchool;
        TwelveBoard = twelveBoard;
        TwelvePercentage = twelvePercentage;
        DiplomaYear = diplomaYear;
        DiplomaSchool = diplomaSchool;
        DiplomaBoard = diplomaBoard;
        DiplomaPercentage = diplomaPercentage;
        GradYear = gradYear;
        GradSchool = gradSchool;
        GradBoard = gradBoard;
        GradPercentage = gradPercentage;
        PostGradYear = postGradYear;
        PostGradSchool = postGradSchool;
        PostGradBoard = postGradBoard;
        PostGradPercentage = postGradPercentage;
    }

    public String getName() {
        return name;
    }

    public String getReportingTo() {
        return reportingTo;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getOfficialEmailId() {
        return officialEmailId;
    }

    public String getPersonalEmailId() {
        return personalEmailId;
    }

    public String getCurrentFullAddress() {
        return currentFullAddress;
    }

    public String getPermanentFullAddress() {
        return permanentFullAddress;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getMarriageDate() {
        return marriageDate;
    }

    public String getFather() {
        return father;
    }

    public String getSpouse() {
        return spouse;
    }

    public String getNationality() {
        return nationality;
    }

    public String getReligion() {
        return religion;
    }

    public String getGender() {
        return gender;
    }

    public String getBaseOfficeLocation() {
        return baseOfficeLocation;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public String getVoterIdNumber() {
        return voterIdNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public String getAccountNumer() {
        return accountNumer;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getTenYear() {
        return TenYear;
    }

    public String getTenSchool() {
        return TenSchool;
    }

    public String getTenBoard() {
        return TenBoard;
    }

    public String getTenPercentage() {
        return TenPercentage;
    }

    public String getTwelveYear() {
        return TwelveYear;
    }

    public String getTwelveSchool() {
        return TwelveSchool;
    }

    public String getTwelveBoard() {
        return TwelveBoard;
    }

    public String getTwelvePercentage() {
        return TwelvePercentage;
    }

    public String getDiplomaYear() {
        return DiplomaYear;
    }

    public String getDiplomaSchool() {
        return DiplomaSchool;
    }

    public String getDiplomaBoard() {
        return DiplomaBoard;
    }

    public String getDiplomaPercentage() {
        return DiplomaPercentage;
    }

    public String getGradYear() {
        return GradYear;
    }

    public String getGradSchool() {
        return GradSchool;
    }

    public String getGradBoard() {
        return GradBoard;
    }

    public String getGradPercentage() {
        return GradPercentage;
    }

    public String getPostGradYear() {
        return PostGradYear;
    }

    public String getPostGradSchool() {
        return PostGradSchool;
    }

    public String getPostGradBoard() {
        return PostGradBoard;
    }

    public String getPostGradPercentage() {
        return PostGradPercentage;
    }
}
