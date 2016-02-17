package in.periculum.ims;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import in.periculum.ims.application.AppController;
import in.periculum.ims.ds.Employee;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ImsUtility;

public class MpinCreate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_create);
        Employee emp=ImsUtility.getUser(getApplicationContext());
        ((TextView)findViewById(R.id.empid)).setText(emp.getEmployeeId());
        ((TextView)findViewById(R.id.empname)).setText(emp.getName());
        ((TextView)findViewById(R.id.designation)).setText(emp.getDesig());
        ((TextView)findViewById(R.id.mobile)).setText(emp.getMobileNo());
        NetworkImageView ntwkImg = (NetworkImageView) findViewById(R.id.profileimg);
        ntwkImg.setDefaultImageResId(R.drawable.profile);
        ntwkImg.setImageUrl(emp.getEmpImagePath(), AppController.getInstance().getImageLoader());
        findViewById(R.id.mpinbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.hideKeyboard(MpinCreate.this);
                processAuth();
            }
        });
    }

    public void processAuth() {
        EditText mpinEdittext = (EditText) findViewById(R.id.mpin);
        EditText cmpinEdittext = (EditText) findViewById(R.id.cnfrmmpin);

        String mpin = mpinEdittext.getText().toString();
        String cnfrmmpin = cmpinEdittext.getText().toString();

        // Reset errors.
        mpinEdittext.setError(null);
        cmpinEdittext.setError(null);

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(mpin)) {
            mpinEdittext.setError(getString(R.string.error_field_required));
            focusView = mpinEdittext;
            cancel = true;
        }
        if (TextUtils.isEmpty(cnfrmmpin)) {
            cmpinEdittext.setError(getString(R.string.error_field_required));
            focusView = cmpinEdittext;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if (mpin.equals(cnfrmmpin)) {
                ImsUtility.saveEmpMpin(getApplicationContext(), String.valueOf(mpin));
                JSONObject jobj=new JSONObject();
                try {
                    jobj.put("tag", "deviceregister");
                    jobj.put("employeeid", CommonUtility.getDeviceId(MpinCreate.this));

                    jobj.put("deviceid", CommonUtility.getDeviceId(MpinCreate.this));
                    jobj.put("devicemodel", CommonUtility.getDeviceName());
                    jobj.put("deviceversion", CommonUtility.getAndroidVersion());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CommonUtility.showProgressDialog(this, "Registering Device.", "Registering Device. Please Wait", true, false);
                CommonHandler.loadTheData("deviceregister", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
                    @Override
                    public void response(String errro, JSONObject returnType) {
                        CommonUtility.showProgressDialog(MpinCreate.this,"Registering Device.", "Registering Device. Please Wait", false, false);
                        Intent i = new Intent(MpinCreate.this, Dashboard.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        CommonUtility.showProgressDialog(MpinCreate.this, "", "Registering Device.", false, false);
                        Snackbar.make(findViewById(R.id.auth_form_layout), "error in device registeration.", Snackbar.LENGTH_SHORT).show();

                    }
                }, jobj);

            } else {
                Snackbar.make(findViewById(R.id.auth_form_layout), "MPIN not matched", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
