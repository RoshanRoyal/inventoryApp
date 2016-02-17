package in.periculum.ims;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import in.periculum.ims.ds.Employee;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ImsUtility;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mEmpIdView;
    private EditText mMobileView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmpIdView = (EditText) findViewById(R.id.empid);

        mMobileView = (EditText) findViewById(R.id.mobile);
        mMobileView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        CommonUtility.hideKeyboard(LoginActivity.this);
        // Reset errors.
        mEmpIdView.setError(null);
        mMobileView.setError(null);

        // Store values at the time of the login attempt.
        String empid = mEmpIdView.getText().toString();
        String mobile = mMobileView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid empid address.
        if (TextUtils.isEmpty(empid)) {
            mEmpIdView.setError(getString(R.string.error_field_required));
            focusView = mEmpIdView;
            cancel = true;
        } else if (!isEmpIdValid(empid)) {
            mEmpIdView.setError(getString(R.string.error_invalid_empid));
            focusView = mEmpIdView;
            cancel = true;
        }

        // Check for a valid mobile, if the user entered one.
        if (TextUtils.isEmpty(mobile)) {
            mMobileView.setError(getString(R.string.error_field_required));
            focusView = mMobileView;
            cancel = true;
        } else if (!isMobileValid(mobile)) {
            mMobileView.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("tag", "login");
                jsonObject.put("employeeid", empid);
                jsonObject.put("mobile", mobile);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            CommonUtility.showProgressDialog(LoginActivity.this,"Logging In","Authenticating Please Wait!!!",true,false);
            //background login process
            CommonHandler.loadTheData("login", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
                @Override
                public void response(String errro, JSONObject returnType) {

                    try {
                        if (Integer.parseInt(returnType.getString("success")) != 0) {
                            Employee emp = new Gson().fromJson(
                                    returnType.getString("user"),
                                    Employee.class);
                            ImsUtility.saveUser(getApplicationContext(), emp);
                            CommonUtility.showProgressDialog(LoginActivity.this, "Logging In", "Authenticating Please Wait!!!", false, false);
                            nextActivity();
                        } else {
                            CommonUtility.showProgressDialog(LoginActivity.this,"Logging In","Authenticating Please Wait!!!",false,false);

                            Toast.makeText(getApplicationContext(), "error in login please try again!!!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        CommonUtility.showProgressDialog(LoginActivity.this, "Logging In", "Authenticating Please Wait!!!", false, false);
                        Toast.makeText(getApplicationContext(), "error in login please try again!!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    CommonUtility.showProgressDialog(LoginActivity.this, "Logging In", "Authenticating Please Wait!!!", false, false);
                    Toast.makeText(getApplicationContext(), "error in login please try again!!!", Toast.LENGTH_LONG).show();
                }
            }, jsonObject);

        }
    }

    protected void nextActivity() {
        Intent i = new Intent(this, ProfileWithAuth.class);
        startActivity(i);
        finish();
    }

    private boolean isEmpIdValid(String empid) {
        //TODO: Replace this with your own logic
        return empid.length() >= 7;
    }

    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        return mobile.length() == 10;
    }


}

