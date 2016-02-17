package in.periculum.ims;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Material;
import in.periculum.ims.ds.NotificationDs;
import in.periculum.ims.ds.Sites;
import in.periculum.ims.ds.Stock;
import in.periculum.ims.ds.StockRcv;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ConnectionDetector;
import in.periculum.ims.utility.ImsUtility;


public class DataSyncFragment extends Fragment {

    private static final String SyncData = "syncStatusData";
    private static final String isAutoSync = "isAuto";
    private boolean isAuto;
    private JSONArray SyncStatusData;
    View v;
    int j;

    public static DataSyncFragment newInstance(String SyncStatusData, boolean isAuto) {
        DataSyncFragment fragment = new DataSyncFragment();
        Bundle args = new Bundle();
        args.putString(SyncData, SyncStatusData);
        args.putBoolean(isAutoSync, isAuto);
        fragment.setArguments(args);
        return fragment;
    }


    public DataSyncFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAuto = getArguments().getBoolean(isAutoSync);
        if (getArguments().getString(SyncData) != null) {
            try {
                SyncStatusData = new JSONArray(getArguments().getString(SyncData));
                j = 0;
            } catch (JSONException e) {
                SyncStatusData = null;
                e.printStackTrace();
            }
        }

    }

    public void recursiveCall() {
        if (j < SyncStatusData.length()) {
            JSONObject jsonObj = null;
            try {
                jsonObj = SyncStatusData.getJSONObject(j);
                if (ConnectionDetector.getInstance(getActivity().getApplicationContext()).isConnectingToInternet())
                    updateTable(jsonObj.getString("TableName"), jsonObj.getInt("UpdateTrueFalse"), v);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
            showMsgAlert(getActivity(), "Data Sync Completed");
        }
    }

    private void showMsgAlert(final Activity activity, String msg) {
        String title = "Alert!";
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(getActivity(), Dashboard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_data_sync, container, false);


        if (SyncStatusData != null && isAuto) {
            recursiveCall();
        }

        v.findViewById(R.id.sync_sites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Starting Download Service */
                siteSync();
            }
        });
        v.findViewById(R.id.sync_material).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Starting Download Service */
                uploadClientAddedMaterial();
            }
        });
        v.findViewById(R.id.sync_stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Starting Download Service */

                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("tag", "stockSelect");
                    jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                    jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));
                    jobj.put("fromslno", "0");
                    jobj.put("toslno", "0");
                    jobj.put("lastupdateddate", ImsUtility.getDate(getActivity().getApplicationContext(), ImsUtility.Stock_Issue_Date));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commonCaller(jobj, 2);
            }
        });
        v.findViewById(R.id.sync_stock_rcv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       /* Starting Download Service */
                TableHelper siteHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                List<Sites> allSites = siteHelper.getAllSites();
                siteHelper.close();
                JSONArray jsonArray = new JSONArray();
                for (Sites sites : allSites) {
                    jsonArray.put(sites.getWHNo());
                }

                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("tag", "stockRcvSelect");
                    jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                    jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));
                    jobj.put("toslno", "0");
                    jobj.put("fromslno", "0");
                    jobj.put("lastupdateddate", ImsUtility.getDate(getActivity().getApplicationContext(), ImsUtility.Stock_Rcv_Date));

                    jobj.put("sites", jsonArray);


                } catch (JSONException e) {
                    ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
                    e.printStackTrace();
                }
                commonCaller(jobj, 3);
            }
        });
        v.findViewById(R.id.sync_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Starting Download Service */

                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("tag", "getEmpNotification");
                    jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                    jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commonCaller(jobj, 4);
            }
        });
        return v;
    }

    private void updateTable(String tableName, int sync, View v) {
        ImsUtility.saveIsSyncing(getActivity().getApplicationContext());
        switch (tableName) {
            case "Material":
                if (sync == 1) {
                    uploadClientAddedMaterial();
                } else {
                    j++;
                    recursiveCall();
                }
                break;
            case "SiteAccess":
                if (sync == 1) {
                    siteSync();
                } else {
                    j++;
                    recursiveCall();
                }
                break;
            case "Stock":
                if (sync == 1) {

                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("tag", "stockSelect");
                        jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                        jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));
                        jobj.put("fromslno", "0");
                        jobj.put("toslno", "0");
                        jobj.put("lastupdateddate", ImsUtility.getDate(getActivity().getApplicationContext(), ImsUtility.Stock_Issue_Date));


                    } catch (JSONException e) {
                        ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
                        e.printStackTrace();
                    }
                    commonCaller(jobj, 2);
                } else {
                    j++;
                    recursiveCall();
                }
                break;
            case "Notification":
                if (sync == 1) {
                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("tag", "getEmpNotification");
                        jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                        jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));

                    } catch (JSONException e) {
                        ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
                        e.printStackTrace();
                    }
                    commonCaller(jobj, 4);
                } else {
                    j++;
                    recursiveCall();
                }
                break;
            case "GoodsReceiptRequisition":
                if (sync == 1) {
                    TableHelper siteHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                    List<Sites> allSites = siteHelper.getAllSites();
                    siteHelper.close();
                    JSONArray jsonArray = new JSONArray();
                    for (Sites sites : allSites) {
                        jsonArray.put(sites.getWHNo());
                    }

                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("tag", "stockRcvSelect");
                        jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                        jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));
                        jobj.put("toslno", "0");
                        jobj.put("fromslno", "0");
                        jobj.put("lastupdateddate", ImsUtility.getDate(getActivity().getApplicationContext(), ImsUtility.Stock_Rcv_Date));

                        jobj.put("sites", jsonArray);


                    } catch (JSONException e) {
                        ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
                        e.printStackTrace();
                    }
                    commonCaller(jobj, 3);
                } else {
                    j++;
                    recursiveCall();
                }
                break;
            default:
                break;
        }
    }

    private void commonCaller(JSONObject jsonObject, final int forWhom) {
        CommonUtility.showProgressDialog(getActivity(), "", "getting data to sync", true, false);
        CommonHandler.loadTheData("syncdb", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
            @Override
            public void response(String errro, JSONObject returnType) {
                try {
                    if (Integer.parseInt(returnType.getString("success")) == 1) {
                            /* Sending result back to activity */
                        CommonUtility.showProgressDialog(getActivity(), "", "syncing", false, false);
                        switch (forWhom) {
                            case 1:
                                new SyncMaterialDb().execute(returnType.getString("materials"));
                                break;
                            case 2:
                                new SyncStockDb().execute(returnType.getString("stock"));
                                break;
                            case 3:
                                new SyncStockRcvDb().execute(returnType.getString("stockrcv"));
                                break;
                            case 4:
                                new SyncNotificationDb().execute(returnType.getString("notification"));
                                break;
                        }
                    } else if (Integer.parseInt(returnType.getString("success")) == 2) {
                        CommonUtility.showProgressDialog(getActivity(), "", "", false, false);
                        Toast.makeText(getActivity().getApplicationContext(), "No New Data", Toast.LENGTH_LONG).show();
                        if (SyncStatusData != null && isAuto) {
                            j++;
                            recursiveCall();
                        }
                    }else{
                        CommonUtility.showProgressDialog(getActivity(), "", "", false, false);
                    }
                } catch (JSONException e) {
                    CommonUtility.showProgressDialog(getActivity(), "", "", false, false);
                    Toast.makeText(getActivity().getApplicationContext(), "Error in sync", Toast.LENGTH_LONG).show();
                    ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
                }
            }

            @Override
            public void onError(String errorMessage) {

                CommonUtility.showProgressDialog(getActivity(), "", "syncing ", false, false);
                Toast.makeText(getActivity().getApplicationContext(), "Error in sync " + errorMessage, Toast.LENGTH_LONG).show();
                ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
            }
        }, jsonObject);
    }


    private void uploadClientAddedMaterial() {
        final TableHelper materialHelper = TableHelper.getInstance(getActivity().getApplicationContext());
        if (materialHelper.getClientAddedMaterials() != null && materialHelper.getClientAddedMaterials().size() > 0) {
            CommonUtility.showProgressDialog(getActivity(), "", "syncing Material", true, false);

            JSONObject jobj = new JSONObject();
            try {
                jobj.put("tag", "saveMaterial");
                jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                jobj.put("materials", new JSONArray(materialHelper.getClientAddedMaterials()));

                CommonHandler.loadTheData("saveMaterial", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
                    @Override
                    public void response(String errro, JSONObject returnType) {
                        CommonUtility.showProgressDialog(getActivity(), "", "syncing Material", false, false);
                        materialHelper.emptyCMaterials();
                        materialHelper.close();
                        JSONObject jobj = new JSONObject();
                        try {
                            jobj.put("tag", "getMaterial");
                            jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                            jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));
                            jobj.put("fromslno", "0");
                            jobj.put("toslno", "0");
                            jobj.put("lastupdateddate", ImsUtility.getDate(getActivity().getBaseContext(), ImsUtility.Material_Date));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        commonCaller(jobj, 1);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        CommonUtility.showProgressDialog(getActivity(), "", "syncing Material", false, false);
                        ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());

                    }
                }, jobj);
            } catch (JSONException e) {
                CommonUtility.showProgressDialog(getActivity(), "", "syncing Material", false, false);
                ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
                e.printStackTrace();
            }
        } else {
            JSONObject jobj = new JSONObject();
            try {
                jobj.put("tag", "getMaterial");
                jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));
                jobj.put("fromslno", "0");
                jobj.put("toslno", "0");
                jobj.put("lastupdateddate", ImsUtility.getDate(getActivity().getBaseContext(), ImsUtility.Material_Date));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            commonCaller(jobj, 1);
        }
    }


    private void siteSync() {
        CommonUtility.showProgressDialog(getActivity(), "", "syncing sites", true, false);
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("tag", "getEmpSites");
            jobj.put("employeeid", ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
            jobj.put("deviceid", CommonUtility.getDeviceId(getActivity()));
            jobj.put("lastupdateddate", ImsUtility.getDate(getActivity().getApplicationContext(), ImsUtility.Site_Date));


        } catch (JSONException e) {
            ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
            e.printStackTrace();
        }
        CommonHandler.loadTheData("syncSites", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
            @Override
            public void response(String errro, JSONObject returnType) {
                try {
                    if (Integer.parseInt(returnType.getString("success")) == 1) {
                            /* Sending result back to activity */
                        Sites[] allData = new Gson().fromJson(returnType.getString("sites"), Sites[].class);
                        TableHelper sitesHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                        for (Sites site : allData) {
                            if (!sitesHelper.dbSiteHasData(site.getWHNo()))
                                sitesHelper.addSite(site);
                            else
                                sitesHelper.updateSite(site);
                        }
                        sitesHelper.close();
                        ImsUtility.saveDate(getActivity().getBaseContext(), CommonUtility.giveDate(), ImsUtility.Site_Date);
                        CommonUtility.showProgressDialog(getActivity(), "", "syncing sites", false, false);
                        if (SyncStatusData != null) {
                            j++;
                            recursiveCall();
                        }
                    } else if (Integer.parseInt(returnType.getString("success")) == 2) {
                        CommonUtility.showProgressDialog(getActivity(), "", "syncing sites", false, false);
                        Toast.makeText(getActivity().getApplicationContext(), "No New Data", Toast.LENGTH_LONG).show();
                        if (SyncStatusData != null && isAuto) {
                            j++;
                            recursiveCall();
                        }
                    }
                } catch (JSONException e) {

                    CommonUtility.showProgressDialog(getActivity(), "", "syncing sites", false, false);
                    Toast.makeText(getActivity().getApplicationContext(), "Error in sync", Toast.LENGTH_LONG).show();
                    ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());
                }
            }

            @Override
            public void onError(String errorMessage) {

                CommonUtility.showProgressDialog(getActivity(), "", "syncing sites", false, false);
                Toast.makeText(getActivity().getApplicationContext(), "Error in sync", Toast.LENGTH_LONG).show();
                ImsUtility.deleteIsSyncing(getActivity().getApplicationContext());

            }
        }, jobj);
    }

    public class SyncMaterialDb extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtility.showProgressDialog(getActivity(), "", "syncing materials", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            Material[] allData = new Gson().fromJson(params[0], Material[].class);
            if (allData.length > 0) {
                TableHelper materialHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                materialHelper.addAllMaterial(Arrays.asList(allData));
                materialHelper.close();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
            }
            ImsUtility.saveDate(getActivity().getBaseContext(), CommonUtility.giveDate(), ImsUtility.Material_Date);

            return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            CommonUtility.showProgressDialog(getActivity(), "", "syncing materials", false, false);
            if (SyncStatusData != null && isAuto) {
                j++;
                recursiveCall();
            }
        }
    }

    public class SyncStockDb extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtility.showProgressDialog(getActivity(), "", "syncing stock", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            Stock[] allData = new Gson().fromJson(params[0], Stock[].class);
            if (allData.length > 0) {
                TableHelper stockHelper = TableHelper.getInstance(getActivity().getApplicationContext());

                stockHelper.addAllStock(Arrays.asList(allData));
                stockHelper.close();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
            }
            ImsUtility.saveDate(getActivity().getBaseContext(), CommonUtility.giveDate(), ImsUtility.Material_Date);

            return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CommonUtility.showProgressDialog(getActivity(), "", "syncing materials", false, false);
            if (SyncStatusData != null && isAuto) {
                j++;
                recursiveCall();
            }
        }
    }

    public class SyncStockRcvDb extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtility.showProgressDialog(getActivity(), "", "syncing stock to receive", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            StockRcv[] allData = new Gson().fromJson(params[0], StockRcv[].class);
            if (allData.length > 0) {
                TableHelper stockRcvHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                stockRcvHelper.addAllStockRcv(Arrays.asList(allData));
                stockRcvHelper.close();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
            }
            ImsUtility.saveDate(getActivity().getBaseContext(), CommonUtility.giveDate(), ImsUtility.Material_Date);

            return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CommonUtility.showProgressDialog(getActivity(), "", "syncing materials", false, false);
            if (SyncStatusData != null && isAuto) {
                j++;
                recursiveCall();
            }
        }
    }

    public class SyncNotificationDb extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtility.showProgressDialog(getActivity(), "", "syncing notifications", true, false);
        }

        @Override
        protected String doInBackground(String... params) {

            NotificationDs[] allData = new Gson().fromJson(params[0], NotificationDs[].class);
            if (allData.length > 0) {
                TableHelper notificationHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                notificationHelper.addAllNotification(Arrays.asList(allData));
                notificationHelper.close();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
            }
            ImsUtility.saveDate(getActivity().getBaseContext(), CommonUtility.giveDate(), ImsUtility.Notification_Date);

            return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CommonUtility.showProgressDialog(getActivity(), "", "", false, false);
            if (SyncStatusData != null && isAuto) {
                j++;
                recursiveCall();
            }
        }
    }
}
