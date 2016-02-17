package in.periculum.ims;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import in.periculum.ims.ds.Employee;
import in.periculum.ims.utility.ImsUtility;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String mpin = ImsUtility.getEmpMpin(getApplicationContext());
                Employee emp = ImsUtility.getUser(getApplicationContext());

                if (mpin == null || mpin.equals("")) {

                    if (emp != null) {
                        Intent i = new Intent(Splash.this, ProfileWithAuth.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(Splash.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {

                        Intent i = new Intent(Splash.this, MpinLogin.class);
                        startActivity(i);
                        finish();


                }
            }
        }, 2000);
    }
}
