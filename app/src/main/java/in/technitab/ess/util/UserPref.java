package in.technitab.ess.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPref {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public UserPref(Context context) {
        preferences = context.getSharedPreferences(ConstantVariable.UserPrefVar.USER_FILE, Context.MODE_PRIVATE);
    }

    public void storeData(String user_id, int access_control_id, int roleId, String role,String relatedTable, String email, String name, String mobile_number,String baseLocation) {
        editor = preferences.edit();
        editor.putString(ConstantVariable.UserPrefVar.USER_ID, user_id);
        editor.putInt(ConstantVariable.UserPrefVar.ROLE_ID, roleId);
        editor.putInt(ConstantVariable.UserPrefVar.ACCESS_CONTROL_ID, access_control_id);
        editor.putString(ConstantVariable.UserPrefVar.NAME, name);
        editor.putString(ConstantVariable.UserPrefVar.RELATED_TABLE,relatedTable);
        editor.putString(ConstantVariable.UserPrefVar.EMAIL, email);
        editor.putString(ConstantVariable.UserPrefVar.USER_ROLE, role);
        editor.putString(ConstantVariable.UserPrefVar.NUMBER, mobile_number);
        editor.putString(ConstantVariable.UserPrefVar.BASE_OFFICE_LOCATION,baseLocation);
        editor.putBoolean(ConstantVariable.UserPrefVar.IS_LOGIN, true);
        editor.apply();
    }


    public int getAccessControlId() {
        return preferences.getInt(ConstantVariable.UserPrefVar.ACCESS_CONTROL_ID, 1);
    }

    public String getRelatedTable(){
        return preferences.getString(ConstantVariable.UserPrefVar.RELATED_TABLE, "");
    }

    public String getBaseLocation(){
        return preferences.getString(ConstantVariable.UserPrefVar.BASE_OFFICE_LOCATION,"");
    }

    public String getRole() {
        return preferences.getString(ConstantVariable.UserPrefVar.USER_ROLE, "");
    }

    public String getUserId() {
        return preferences.getString(ConstantVariable.UserPrefVar.USER_ID, "");
    }

    public String getNumber() {
        return preferences.getString(ConstantVariable.UserPrefVar.NUMBER, "");
    }

    public String getName() {
        return preferences.getString(ConstantVariable.UserPrefVar.NAME, "");
    }

    public String getEmail() {
        return preferences.getString(ConstantVariable.UserPrefVar.EMAIL, "");
    }

    public int getRoleId() {
        return preferences.getInt(ConstantVariable.UserPrefVar.ROLE_ID, 0);
    }

    public boolean isLogin() {
        return preferences.getBoolean(ConstantVariable.UserPrefVar.IS_LOGIN, false);
    }

    public void resetLogin() {
        editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void storeToken(String refreshedToken) {
        editor = preferences.edit();
        editor.putString(ConstantVariable.UserPrefVar.FCM_TOKEN, refreshedToken);
        editor.apply();
    }

    public String getFcmToken() {
        return preferences.getString(ConstantVariable.UserPrefVar.FCM_TOKEN, "");
    }


}
