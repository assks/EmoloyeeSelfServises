package in.technitab.ess.api;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.technitab.ess.model.AddResponse;
import in.technitab.ess.model.AddTimesheet;
import in.technitab.ess.model.ApproveLeave;
import in.technitab.ess.model.ApproveTimeSheet;
import in.technitab.ess.model.AssignProject;
import in.technitab.ess.model.AssignedFixAsset;
import in.technitab.ess.model.Customer;
import in.technitab.ess.model.GuesthouseBooking;
import in.technitab.ess.model.MyLeaves;
import in.technitab.ess.model.OrgAssetType;
import in.technitab.ess.model.Policy;
import in.technitab.ess.model.Project;
import in.technitab.ess.model.ProjectActivity;
import in.technitab.ess.model.ProjectTask;
import in.technitab.ess.model.RequestedProject;
import in.technitab.ess.model.SalarySlip;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.TecResponse;
import in.technitab.ess.model.TecTripBooking;
import in.technitab.ess.model.TecTripResponse;
import in.technitab.ess.model.TimeSheetCalender;
import in.technitab.ess.model.TimeSheetDate;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripBookingResponse;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.model.TripResponse;
import in.technitab.ess.model.TripUser;
import in.technitab.ess.model.User;
import in.technitab.ess.model.UserDoc;
import in.technitab.ess.model.UserProfile;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface RestApi {

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login(@Field(ConstantVariable.UserPrefVar.EMAIL) String email,
                       @Field("password") String password,
                       @Field("token") String fcmToken);

    @Multipart
    @POST("attendance.php")
    Call<StringResponse> addPunchIn(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("attendance.php")
    Call<StringResponse> addPunchOut(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("attendance.php")
    Call<StringResponse> manualAttendance(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                          @Field(ConstantVariable.Tbl_Attendance.DATE) String date,
                                          @Field(ConstantVariable.Tbl_Attendance.PUNCH_IN) String punchIn,
                                          @Field(ConstantVariable.Tbl_Attendance.PUNCH_OUT) String punchOut,
                                          @Field(ConstantVariable.Tbl_Attendance.ATTENDANCE) String attendance,
                                          @Field("attendance_duration") String attendanceDuration,
                                          @Field("remark") String remark,
                                          @Field(ConstantVariable.MIX_ID.ACTION) String action);


    @FormUrlEncoded
    @POST("leave.php")
    Call<StringResponse> leaveRequest(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                      @Field("leave_json") String leaveJson);


    @FormUrlEncoded
    @POST("cancel_leave_request.php")
    Call<StringResponse> cancelRequest(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                       @Field("applied_by_user_id") String id,
                                       @Field("status_value") int statusValue,
                                       @Field("leave_request_id") int leaveRequestId);


    @FormUrlEncoded
    @POST("fetch_leaves.php")
    Call<List<MyLeaves>> getLeaveList(@Field(ConstantVariable.UserPrefVar.USER_ID) String user_id);


    @FormUrlEncoded
    @POST("fetch_employee_data.php")
    Call<String> fetchEmployeeData(@Field("user_id") String employeeId);


    @Multipart
    @POST("fetch_leave_balance.php")
    Call<String> fetchLeaveBalance(@PartMap Map<String, RequestBody> map);


    @FormUrlEncoded
    @POST("add_timesheet.php")
    Call<StringResponse> addTimesheet(@Field(ConstantVariable.UserPrefVar.EMAIL) String email,
                                      @Field(ConstantVariable.MIX_ID.CREATED_BY_ID) int createdById,
                                      @Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                      @Field(ConstantVariable.Timesheet.STAFF_NAME) String staffName,
                                      @Field(ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS) String timesheetLogHous,
                                      @Field(ConstantVariable.Timesheet.DATE) String date,
                                      @Field(ConstantVariable.Timesheet.PROJECT_JSON) String projectJson);


    @FormUrlEncoded
    @POST("fetch_timesheet_date.php")
    Call<ArrayList<TimeSheetDate>> fetchDateWiseTimesheetList(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                                              @Field(ConstantVariable.MIX_ID.YEAR) String year,
                                                              @Field(ConstantVariable.MIX_ID.MONTH) String month);

    @FormUrlEncoded
    @POST("fetch_timesheet_project.php")
    Call<ArrayList<TimeSheetDate>> fetchProjectWiseTimesheetList(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                                                 @Field(ConstantVariable.MIX_ID.YEAR) String year,
                                                                 @Field(ConstantVariable.MIX_ID.MONTH) String month);

    @FormUrlEncoded
    @POST("fetch_timesheet.php")
    Call<ArrayList<TimeSheetCalender>> fetchTimesheetList(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                                          @Field(ConstantVariable.MIX_ID.YEAR) String year,
                                                          @Field(ConstantVariable.MIX_ID.MONTH) String month);


    @FormUrlEncoded
    @POST("fetch_assigned_user_project.php")
    Call<ArrayList<ProjectTask>> fetchProjectAssignList(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId);


    @FormUrlEncoded
    @POST("fetch_customer_names.php")
    Call<ArrayList<Customer>> fetchCustomerList(@Field("customer_name") String customerName);


    @FormUrlEncoded
    @POST("fetch_customer_state.php")
    Call<ArrayList<Customer>> fetchCustomer(@Field("state") String customerName);


    @FormUrlEncoded
    @POST("fetch_unassigned_project.php")
    Call<ArrayList<Project>> fetchUnassignedProject(@Field("customer_id") int customerName,
                                                    @Field("user_id") String userId,
                                                    @Field("role_id") int roleId,
                                                    @Field("state") String state,
                                                    @Field("country") String country);


    @FormUrlEncoded
    @POST("fetch_project_names.php")
    Call<ArrayList<Project>> fetchProjectList(@Field("project_name") String projectName);


    @FormUrlEncoded
    @POST("fetch_bookings.php")
    Call<ArrayList<TripBookingResponse>> fetchTripBooking(@Field("trip_id") int tripId);


    @FormUrlEncoded
    @POST("project.php")
    Call<AddResponse> submitProject(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                    @Field("project_json") String projectJson);


    @FormUrlEncoded
    @POST("project.php")
    Call<AddResponse> approveProject(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                     @Field("project_json") String projectJson);


    @FormUrlEncoded
    @POST("fetch_activities.php")
    Call<ArrayList<ProjectActivity>> fetchActivities(@Field("project_id") int projectId,
                                                     @Field("project_type_id") int projectTypeId);


    @FormUrlEncoded
    @POST("fetch_user_names.php")
    Call<ArrayList<User>> fetchUserNames(@Field("name") String name);

    @POST("fetch_users.php")
    Call<ArrayList<User>> fetchUsers();


    @FormUrlEncoded
    @POST("add_activity_user.php")
    Call<StringResponse> addActivityUser(@Field(ConstantVariable.UserPrefVar.USER_ID) int userId,
                                         @Field("created_by_id") String createdById,
                                         @Field("project_id") int projectId,
                                         @Field("project_activity_json") String projectActivityJson);


    @FormUrlEncoded
    @POST("fetch_approve_leaves.php")
    Call<ArrayList<ApproveLeave>> fetchApproveLeaves(@Field("reporting_to") String userId);


    @FormUrlEncoded
    @POST("leave.php")
    Call<StringResponse> changeLeaveStatus(@Field("action") String action,
                                           @Field("id") int leaveRequestId,
                                           @Field("status") String status,
                                           @Field("modified_by_id") int modifiedById,
                                           @Field("comment") String comment);


    @FormUrlEncoded
    @POST("fetch_approval_timesheet.php")
    Call<ArrayList<ApproveTimeSheet>> fetchApproveTimesheet(@Field("project_id") int userId,
                                                            @Field("date") String id);


    @FormUrlEncoded
    @POST("fetch_project_names_ts.php")
    Call<ArrayList<Project>> fetchApproveTs(@Field("project_name") String projectName);



    @FormUrlEncoded
    @POST("update_timesheet.php")
    Call<StringResponse> updateTimesheet(@Field("user_id") String userId,
                                         @Field("project_json") String projectJson);


    @FormUrlEncoded
    @POST("fetch_punch_in_out.php")
    Call<AddTimesheet> AddUserTimesheet(@Field("user_id") int userId,
                                        @Field("date") String date);


    @FormUrlEncoded
    @POST("fetch_project_tasks.php")
    Call<ArrayList<AssignProject>> fetchProjectTask(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("fetch_user_project.php")
    Call<ArrayList<Project>> fetchUserProject(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("fetch_temp_vendor.php")
    Call<ArrayList<Vendor>> fetchVendorList(@Field("user_id") String userId,
                                            @Field("action") String action);

    @FormUrlEncoded
    @POST("fetch_vendor.php")
    Call<ArrayList<Vendor>> fetchVendorList(@Field("search_text") String searchText);


    @FormUrlEncoded
    @POST("fetch_tec_vendor.php")
    Call<ArrayList<Vendor>> fetchTecVendorList(@Field("vendor_type") String vendorType);

    @FormUrlEncoded
    @POST("get_policy_file_url.php")
    Call<Policy> fetchPolicyFile(@Field("user_id") String userId,
                                 @Field("id") int id);


    @Multipart
    @POST("vendor.php")
    Call<StringResponse> addVendor(@PartMap Map<String, RequestBody> map);


    @FormUrlEncoded
    @POST("get_user_profile.php")
    Call<UserProfile> userProfile(@Field(ConstantVariable.UserPrefVar.ROLE_ID) int roleId,
                                  @Field(ConstantVariable.UserPrefVar.RELATED_TABLE) String releatedTable);


    @FormUrlEncoded
    @POST("get_user_doc.php")
    Call<ArrayList<UserDoc>> userDoc(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId);


    @FormUrlEncoded
    @POST("fetch_user_tec.php")
    Call<ArrayList<TecResponse>> getUserTEC(@Field(ConstantVariable.Tec.CREATED_BY_ID) int roleId);



    @FormUrlEncoded
    @POST("tec.php")
    Call<TecTripResponse> fetchTECEntryList(@Field("tec_id") int tecId,
                                            @Field("id") int tripId,
                                            @Field(ConstantVariable.MIX_ID.ACTION) String action);

    @Multipart
    @POST("tec.php")
    Call<AddResponse> saveTECEntry(@PartMap Map<String, RequestBody> map);


    @FormUrlEncoded
    @POST("tec.php")
    Call<AddResponse> generateTecId(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                    @Field(ConstantVariable.UserPrefVar.ROLE_ID) int roleId,
                                    @Field(ConstantVariable.Tec.CREATED_BY_ID) String userId,
                                    @Field(ConstantVariable.Tec.ID) int tripId,
                                    @Field("project_id") int projectId,
                                    @Field("claim_start_date") String claimStartDate,
                                    @Field("base_location") String baseLocation,
                                    @Field("site_location") String siteLocation,
                                    @Field("booking_json") String bookingJson);


    @FormUrlEncoded
    @POST("tec.php")
    Call<StringResponse> submitTEC(@Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                   @Field(ConstantVariable.Tec.TEC_ID) int tecId,
                                   @Field("trip_id") int tripId,
                                   @Field(ConstantVariable.Tec.CREATED_BY_ID) int createdById,
                                   @Field("status") String status,
                                   @Field("project_name") String projectName,
                                   @Field("remark") String remark,
                                   @Field(ConstantVariable.MIX_ID.ACTION) String action);


    @FormUrlEncoded
    @POST("tec.php")
    Call<StringResponse> userSubmitTEC(@Field(ConstantVariable.Tec.SUBMIT_BY_ID) String userId,
                                       @Field("trip_id") int tripId,
                                       @Field(ConstantVariable.Tec.TEC_ID) int tecId,
                                       @Field(ConstantVariable.Project.PROJECT_NAME) String projectName,
                                       @Field("user_note") String userNote,
                                       @Field("claim_end_date") String tecEndDate,
                                       @Field("booking_json") String bookingJson,
                                       @Field(ConstantVariable.MIX_ID.ACTION) String action);


    @FormUrlEncoded
    @POST("tec.php")
    Call<StringResponse> linkBookingWithTec(@Field(ConstantVariable.Tec.CREATED_BY_ID) String userId,
                                            @Field(ConstantVariable.Tec.TEC_ID) int tecId,
                                            @Field("booking_json") String bookingJson,
                                            @Field(ConstantVariable.MIX_ID.ACTION) String action);

    @FormUrlEncoded
    @POST("tec.php")
    Call<StringResponse> deleteTecEntry(@Field("tec_entry_id") int tecEntryId,
                                        @Field("user_id") String userId,
                                        @Field("action") String action);


    @Multipart
    @POST("tec.php")
    Call<StringResponse> approveDraftEntry(@PartMap Map<String, RequestBody> map);


    @FormUrlEncoded
    @POST("fetch_projects.php")
    Call<ArrayList<Project>> fetchProjects(@Field("start_position") int index,
                                           @Field(ConstantVariable.UserPrefVar.ACCESS_CONTROL_ID) int accessControlId,
                                           @Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                           @Field(ConstantVariable.MIX_ID.ACTION) String action);

    @FormUrlEncoded
    @POST("project.php")
    Call<StringResponse> sendProjectRequest(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                            @Field(ConstantVariable.Project.ID) int projectId,
                                            @Field(ConstantVariable.Project.PROJECT_NAME) String projectName,
                                            @Field(ConstantVariable.UserPrefVar.USER_ID) String userId);

    @FormUrlEncoded
    @POST("project.php")
    Call<StringResponse> approveRequestedProject(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                                 @Field(ConstantVariable.RequestedProject.ID) int requestedId,
                                                 @Field(ConstantVariable.RequestedProject.PROJECT_ID) int projectId,
                                                 @Field(ConstantVariable.RequestedProject.PROJECT_NAME) String projectName,
                                                 @Field(ConstantVariable.RequestedProject.PROJECT_TYPE_ID) int projectTypeId,
                                                 @Field(ConstantVariable.RequestedProject.CREATED_BY_ID) int userId,
                                                 @Field(ConstantVariable.RequestedProject.CREATED_BY) String createdBy,
                                                 @Field(ConstantVariable.MIX_ID.MODIFIED_BY_ID) int ModfiedById);

    /* ------------------------------------- Temp end --------------------------*/
    @FormUrlEncoded
    @POST("fix_asset.php")
    Call<ArrayList<OrgAssetType>> fetchFixAssets(@Field(ConstantVariable.UserPrefVar.ROLE_ID) int roleId,
                                                 @Field(ConstantVariable.UserPrefVar.RELATED_TABLE) String relatedTable,
                                                 @Field(ConstantVariable.MIX_ID.ACTION) String action);


    @FormUrlEncoded
    @POST("fix_asset.php")
    Call<StringResponse> requestFixAssets(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                          @Field(ConstantVariable.UserPrefVar.USER_ID) String userId,
                                          @Field(ConstantVariable.FixAsset.ORG_UNIT_ASSET_ID) int orgUnitId,
                                          @Field(ConstantVariable.FixAsset.ORG_UNIT_ASSET) String orgUnit,
                                          @Field(ConstantVariable.FixAsset.REMARK) String remark);


    @FormUrlEncoded
    @POST("fix_asset.php")
    Call<ArrayList<AssignedFixAsset>> fetchAssignedFixAssets(@Field(ConstantVariable.MIX_ID.ACTION) String position,
                                                             @Field(ConstantVariable.UserPrefVar.USER_ID) String userId);

    @FormUrlEncoded
    @POST("fetch_user_salary_slip.php")
    Call<SalarySlip> fetchUserSalarySlip(@Field("salary_slip_month") String salarySlipMonth,
                                         @Field("user_id") String userId);


    @FormUrlEncoded
    @POST("fetch_booking_vendor.php")
    Call<ArrayList<Vendor>> fetchTripVendorList(@Field("project_id") int projectId,
                                                @Field("booking_mode") String bookingMode,
                                                @Field("travel_type") String travelType);


    @FormUrlEncoded
    @POST("fetch_project_user.php")
    Call<ArrayList<TripMember>> fetchProjectUser(@Field("project_id") int projectId);


    @FormUrlEncoded
    @POST("create_trip.php")
    Call<AddResponse> createTrip(@Field("project_id") int projectId,
                                 @Field("project_name") String projectName,
                                 @Field("source") String source,
                                 @Field("destination") String destination,
                                 @Field("trip_start_date") String tripStartDate,
                                 @Field("created_by_id") String createdById,
                                 @Field("member_json") String memberJson);

    @FormUrlEncoded
    @POST("edit_trip.php")
    Call<AddResponse> editTrip(@Field("trip_id") int trip_id,
                               @Field("project_id") int projectId,
                               @Field("project_name") String projectName,
                               @Field("source") String source,
                               @Field("destination") String destination,
                               @Field("trip_start_date") String tripStartDate,
                               @Field("created_by_id") String createdById,
                               @Field("member_json") String memberJson);

    @FormUrlEncoded
    @POST("fetch_trip.php")
    Call<ArrayList<TripResponse>> fetchTrip(@Field("created_by_id") int projectId,
                                            @Field("action") String action);

    @FormUrlEncoded
    @POST("fetch_trip_for_tec.php")
    Call<ArrayList<Trip>> fetchTripForTEC(@Field("created_by_id") int createdById);


    @FormUrlEncoded
    @POST("fetch_trip_tec_user.php")
    Call<ArrayList<TripUser>> fetchTripTec(@Field("trip_id") int trip_id);

    @FormUrlEncoded
    @POST("delete_trip.php")
    Call<StringResponse> deleteTrip(@Field("trip_id") int tripId,
                                    @Field("user_id") String userId);



    @FormUrlEncoded
    @POST("trip_booking.php")
    Call<ArrayList<TecTripBooking>> fetchTripBookingForTec(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                                           @Field("trip_id") int tripId);


    @FormUrlEncoded
    @POST("trip_booking.php")
    Call<StringResponse> tripRequestBooking(@Field("booking_json") String bookingJson,
                                            @Field(ConstantVariable.MIX_ID.ACTION) String action);

    @Multipart
    @POST("trip_booking.php")
    Call<AddResponse> tripSelfBooking(@PartMap Map<String, RequestBody> map);


    @Multipart
    @POST("trip_booking.php")
    Call<StringResponse> tripBookingOnRequest(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("trip_booking.php")
    Call<StringResponse> tripBookingPayment(@PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("trip_booking.php")
    Call<StringResponse> attachBillOnBooking(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("trip_booking.php")
    Call<StringResponse> paymentRequest(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                        @Field(ConstantVariable.MIX_ID.BOOKING_ID) int bookingId,
                                        @Field("created_by_id") String createdById);

    @FormUrlEncoded
    @POST("project.php")
    Call<ArrayList<RequestedProject>> fetchProject(@Field(ConstantVariable.MIX_ID.ACTION) String action);

    @FormUrlEncoded
    @POST("guesthouse_booking.php")
    Call<AddResponse> guesthouseBooking(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                                   @Field("booking_json") String bookingJson);

    @FormUrlEncoded
    @POST("guesthouse_booking.php")
    Call<List<GuesthouseBooking>> fetchBookingList(@Field(ConstantVariable.MIX_ID.ACTION) String action,
                                                   @Field("created_by_id") String bookingJson);
}
