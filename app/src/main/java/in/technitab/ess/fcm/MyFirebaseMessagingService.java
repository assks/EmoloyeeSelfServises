package in.technitab.ess.fcm;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.activity.RequestedProjectActivity;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.MyNotification;
import in.technitab.ess.util.UserPref;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private final String JSON_RESPONSE = "json_response";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        UserPref userPref = new UserPref(getApplicationContext());
        userPref.storeToken(token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MyNotification myNotification = new MyNotification(getApplicationContext());

        JSONObject json;
        try {
            json = new JSONObject(remoteMessage.getData().get("body"));
            Log.d(TAG, " body" + json);
            String title = json.getString("module");
            String body;
            if (title.equalsIgnoreCase("request_leave") || title.equalsIgnoreCase(ConstantVariable.LEAVE_APPROVED) || title.equalsIgnoreCase(ConstantVariable.LEAVE_REJECTED) || title.equalsIgnoreCase(ConstantVariable.LEAVE_CANCEL)) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String userName = jsonObject.getString("user_name");
                String leave_type = jsonObject.getString("leave_type");
                body = userName + " " + leave_type;
                myNotification.displayNotification(title, body, MainActivity.class);

            } else if (title.equalsIgnoreCase("tec")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String status = jsonObject.getString("bill_date");
                String projectName = jsonObject.getString("entry_category");
                String name = jsonObject.getString("user_name");
                body = name + " " + projectName + " " + status;
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("vendor")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                body = jsonObject.getString("vendor");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Request Add Project")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                body = jsonObject.getString("user_name");
                body = body + " " + jsonObject.getString("project_name");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Request Add Project")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                body = jsonObject.getString("name");
                body = body + ", " + jsonObject.getString("project_name");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Assign Project")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = jsonObject.getString("project_name");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Pay Vendor")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                body = jsonObject.getString("vendor_name");
                body = body + " " + jsonObject.getString("user_name");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Vendor Request")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                body = jsonObject.getString("display_contact_name");
                body = body + " By " + jsonObject.getString("user_name");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Approved Vendor")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                body = jsonObject.getString("display_contact_name");
                body = body + " By " + jsonObject.getString("user_name");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Attendance Request")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = jsonObject.getString("date");
                body = body + " By " + jsonObject.getString("user_name") +" "+ jsonObject.getString("remark");
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equals("Request Assign Project")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = jsonObject.getString("project_name");
                body = body + " By " + jsonObject.getString("user_name");
                myNotification.displayNotification(title, body, RequestedProjectActivity.class);
            } else if (title.equals("Fix Asset Request")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = jsonObject.getString(ConstantVariable.FixAsset.ORG_UNIT_ASSET);
                body = body + " By " + jsonObject.getString("user_name");
                myNotification.displayNotification(title, body, RequestedProjectActivity.class);
            } else if (title.equalsIgnoreCase("Submit Tec")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = jsonObject.getString(ConstantVariable.Project.PROJECT_NAME);
                body = body + " By " + jsonObject.getString(ConstantVariable.MIX_ID.USER_NAME);
                myNotification.displayNotification(title, body, MainActivity.class);
            } else if (title.equalsIgnoreCase("Update Tec")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                String status = jsonObject.getString("status");
                String projectName = jsonObject.getString("project_name");
                body = projectName + " " + status;
                myNotification.displayNotification(title, body, MainActivity.class);

            }else if (title.equalsIgnoreCase("Payment Request")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = jsonObject.getString(ConstantVariable.MIX_ID.USER_NAME);
                myNotification.displayNotification(title, body, MainActivity.class);

            }else if (title.equalsIgnoreCase("Booking Request")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = jsonObject.getString(ConstantVariable.MIX_ID.USER_NAME);
                myNotification.displayNotification(title, body, MainActivity.class);

            }else if (title.equalsIgnoreCase("Assign Leave")) {
                JSONObject jsonObject = json.getJSONObject(JSON_RESPONSE);
                body = "By "+jsonObject.getString(ConstantVariable.MIX_ID.USER_NAME);
                myNotification.displayNotification(title, body, MainActivity.class);

            }else if (title.equalsIgnoreCase("Payment Done")) {
                JSONArray jsonArray = json.getJSONArray(JSON_RESPONSE);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                body = jsonObject.getString("booking_id");
                body = "booking id : "+body + " By " + jsonObject.getString("user_name");
                myNotification.displayNotification(title, body, MainActivity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
