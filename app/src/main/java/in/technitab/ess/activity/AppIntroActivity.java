package in.technitab.ess.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import in.technitab.ess.R;
import in.technitab.ess.util.UserPref;

public class AppIntroActivity extends AppCompatActivity {

    UserPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_intro);

        pref = new UserPref(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (pref.isLogin()) {
                    intent = new Intent(AppIntroActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(AppIntroActivity.this, AppPermissionActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
