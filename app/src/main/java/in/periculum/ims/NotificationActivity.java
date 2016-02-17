package in.periculum.ims;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import in.periculum.ims.adapter.NotificationRecyclerViewAdapter;
import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.NotificationDs;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ConnectionDetector;
import in.periculum.ims.utility.ImsUtility;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NotificationRecyclerViewAdapter mAdapter;
    private List<NotificationDs> notificationDses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRecyclerView();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tag", "getEmpNotification");
            jsonObject.put("employeeid", ImsUtility.getUser(getApplicationContext()).getEmployeeId());
            jsonObject.put("deviceid", CommonUtility.getDeviceId(getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ConnectionDetector.getInstance(getApplicationContext()).isConnectingToInternet()) {
            CommonUtility.showProgressDialog(NotificationActivity.this, "", "Getting Notifications", true, false);
            CommonHandler.loadTheData("notification", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
                @Override
                public void response(String errro, JSONObject returnType) {

                    try {
                        if (Integer.parseInt(returnType.getString("success")) == 1) {
                            NotificationDs[] allData = new Gson().fromJson(
                                    returnType.getString("notification"),
                                    NotificationDs[].class);
                            TableHelper notificationHelper = TableHelper.getInstance(getApplicationContext());
                            notificationHelper.addAllNotification(Arrays.asList(allData));
                            notificationDses = notificationHelper.getAllNotification();
                            notificationHelper.close();
                            mAdapter.notifyDataSetChanged();
                            CommonUtility.showProgressDialog(NotificationActivity.this, "", "Getting Notifications", false, false);
                        } else {
                            Toast.makeText(getApplicationContext(), "No New Notifications", Toast.LENGTH_SHORT).show();
                            CommonUtility.showProgressDialog(NotificationActivity.this, "", "Getting Notifications", false, false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        CommonUtility.showProgressDialog(NotificationActivity.this, "", "Getting Notifications", false, false);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    CommonUtility.showProgressDialog(NotificationActivity.this, "", "Getting Notifications", false, false);
                }
            }, jsonObject);
        }else
            Toast.makeText(getApplicationContext(), "Internet Failure", Toast.LENGTH_SHORT).show();
    }

    private void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        TableHelper notificationHelper = TableHelper.getInstance(getApplicationContext());
        notificationDses = notificationHelper.getAllNotification();
        mAdapter = new NotificationRecyclerViewAdapter(notificationDses, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
