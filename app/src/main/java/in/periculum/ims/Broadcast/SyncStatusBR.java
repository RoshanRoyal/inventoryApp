package in.periculum.ims.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import in.periculum.ims.Services.SyncService;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;

import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ImsUtility;

/**
 * Created by ROYAL on 11/5/2015.
 */
public class SyncStatusBR extends BroadcastReceiver {


    // Method gets called when Broad Case is issued from MainActivity for every 10 seconds
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub


        JSONObject jobj = new JSONObject();
        try {
            jobj.put("tag", "getUnsyncStatus");
            jobj.put("employeeid", ImsUtility.getUser(context).getEmployeeId());
            jobj.put("deviceid", CommonUtility.getDeviceId(context));
            jobj.put("stockLastUpdated", ImsUtility.getDate(context, ImsUtility.Stock_Issue_Date));
            jobj.put("goodsReceiptRequisitonLastUpdate", ImsUtility.getDate(context, ImsUtility.Stock_Rcv_Date));
            jobj.put("materialLastUpdate", ImsUtility.getDate(context, ImsUtility.Material_Date));
            jobj.put("siteAccessLastUpdate", ImsUtility.getDate(context, ImsUtility.Site_Date));
            jobj.put("notificationLastUpdate", "2015-01-01");
            Log.e("syncobj",jobj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Checks if new records are inserted in Remote DB to proceed with Sync operation
        CommonHandler.loadTheData("syncstatus", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
            @Override
            public void response(String errro, JSONObject returnType) {
                try {

                    if (returnType.getInt("success") != 0) {
                        final Intent intnt = new Intent(context, SyncService.class);
                        // Set unsynced count in intent data
                        intnt.putExtra("intntdata", returnType.getString("status"));
                        // Call MyService
                        context.startService(intnt);
                    } else {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        }, jobj);

    }
}