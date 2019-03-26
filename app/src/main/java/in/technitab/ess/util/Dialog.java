package in.technitab.ess.util;

import android.app.ProgressDialog;
import android.content.Context;

public class Dialog {
    private ProgressDialog dialog;

    public Dialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
    }

    public void showDialog(){
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
