package in.periculum.ims;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import in.periculum.ims.application.AppController;
import in.periculum.ims.ds.Employee;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ImsUtility;

public class ProfileWithAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_with_auth);
        final Employee emp = ImsUtility.getUser(getApplicationContext());
        ((TextView) findViewById(R.id.empid)).setText(emp.getEmployeeId());
        ((TextView) findViewById(R.id.empname)).setText(emp.getName());
        ((TextView) findViewById(R.id.designation)).setText(emp.getDesig());
        ((TextView) findViewById(R.id.mobile)).setText(emp.getMobileNo());
        NetworkImageView ntwkImg = (NetworkImageView) findViewById(R.id.profileimg);
        ntwkImg.setDefaultImageResId(R.drawable.profile);
        ntwkImg.setImageUrl(emp.getEmpImagePath(), AppController.getInstance().getImageLoader());
        findViewById(R.id.authbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.hideKeyboard(ProfileWithAuth.this);
                String authcode = ((EditText) findViewById(R.id.authcode)).getText().toString();
                //background authentication process
                if (emp.getAuthenticationCode().equals(authcode)) {
                    Intent i = new Intent(ProfileWithAuth.this, MpinCreate.class);
                    startActivity(i);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.auth_form_layout), "Wrong Auth Code!! Please try again!!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }


}
