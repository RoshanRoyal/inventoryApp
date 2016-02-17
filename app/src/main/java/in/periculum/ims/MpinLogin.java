package in.periculum.ims;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ImsUtility;

public class MpinLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_login);




        ((TextView) findViewById(R.id.forget_pass)).setText((Html.fromHtml("<u>CLICK HERE</u>")).toString());

        findViewById(R.id.forget_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImsUtility.deleteEmpMpin(getApplicationContext());

                Intent i = new Intent(MpinLogin.this, ProfileWithAuth.class);
                startActivity(i);
                finish();
            }
        });


        findViewById(R.id.mpin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtility.hideKeyboard(MpinLogin.this);
                String mpin = ((EditText) findViewById(R.id.loginmpin)).getText().toString();
                String rawmpin = ImsUtility.getEmpMpin(getApplicationContext());
                if (mpin.equals(rawmpin)) {
                    Intent i = new Intent(MpinLogin.this, Dashboard.class);
                   /* if (getIntent().getStringExtra("intntdata") != null) {
                        i.putExtra("syncStatusData",getIntent().getStringExtra("intntdata"));
                    }*/

                    startActivity(i);
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.mpinparent), "WRONG MPIN!!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

    }
}
