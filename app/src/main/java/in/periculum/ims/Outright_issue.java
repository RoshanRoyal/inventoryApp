package in.periculum.ims;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Sites;
import in.periculum.ims.ds.Stock;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ConnectionDetector;
import in.periculum.ims.utility.ImsUtility;
import in.periculum.ims.utility.InputFilterMinMax;

public class Outright_issue extends AppCompatActivity {
    JSONArray jsonArray = new JSONArray();

    TableLayout stk;
    String siteid;
    RadioGroup radioGroup;
    int qtyissue = 0, stockSrl = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outright_issue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stk = (TableLayout) findViewById(R.id.table_main);


        findViewById(R.id.directive_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(Outright_issue.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                ((EditText) findViewById(R.id.directive_date)).setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                                ((EditText) findViewById(R.id.issued_to)).requestFocus();
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
        });
        findViewById(R.id.addstockissuetolist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getInstance(getApplicationContext()).isConnectingToInternet()) {

                    JSONObject jsonObject = getData();
                    if (jsonObject == null)
                        return;
                    jsonArray.put(jsonObject);
                    findViewById(R.id.issue_stock_button).setClickable(true);

                    stk.removeAllViews();
                    ((EditText) findViewById(R.id.siteselect)).setText("");
                    ((EditText) findViewById(R.id.mcode)).setText("");
                    ((EditText) findViewById(R.id.directive_type)).setText("");
                    ((EditText) findViewById(R.id.directive_ref)).setText("");
                    ((EditText) findViewById(R.id.directive_date)).setText("");
                    ((EditText) findViewById(R.id.issued_to)).setText("");
                    ((EditText) findViewById(R.id.remark_checking)).setText("");
                    ((EditText) findViewById(R.id.endorsement_remark)).setText("");

                    ((TextView) findViewById(R.id.inlist)).setText(jsonArray.length() + "");

                } else {
                    Snackbar.make(findViewById(R.id.outright_issue_activity), "Internet Not Available!!!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                jsonArray=null;
            }
        });
        findViewById(R.id.issue_stock_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.hideKeyboard(Outright_issue.this);

                if (ConnectionDetector.getInstance(getApplicationContext()).isConnectingToInternet()) {

                    if (jsonArray != null && jsonArray.length() > 0) {

                        JSONObject jobj = new JSONObject();
                        try {
                            jobj.put("stocks", jsonArray);
                            jobj.put("tag", "outrightIssue");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }

                        CommonUtility.showProgressDialog(Outright_issue.this, "", "Issueing Stock", true, false);
                        CommonHandler.loadTheData("issuestock", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
                            @Override
                            public void response(String errro, JSONObject returnType) {
                                try {
                                    if (Integer.parseInt(returnType.getString("success")) != 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            try {
                                                TableHelper stockHelper = TableHelper.getInstance(getApplicationContext());
                                                Stock stock = stockHelper.getStock(jsonArray.getJSONObject(i).getString("materialCode"),
                                                        jsonArray.getJSONObject(i).getString("stockSrl"));
                                                int x = Integer.parseInt(stock.getQtyGround()) - qtyissue;
                                                stock.setQtyGround("" + x);
                                                stockHelper.updateStock(stock);
                                                stockHelper.close();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        CommonUtility.showProgressDialog(Outright_issue.this, "", "Issueing Stock", false, false);
                                        Snackbar.make(findViewById(R.id.outright_issue_activity), "Stock Issued", Snackbar.LENGTH_SHORT).show();
                                        clearFields();
                                        jsonArray = new JSONArray();
                                    } else {
                                        CommonUtility.showProgressDialog(Outright_issue.this, "", "Issueing Stock", false, false);
                                        Snackbar.make(findViewById(R.id.outright_issue_activity), "Stock Not Issued", Snackbar.LENGTH_SHORT).show();
                                        jsonArray = new JSONArray();
                                    }
                                } catch (JSONException e) {
                                    CommonUtility.showProgressDialog(Outright_issue.this, "", "Issueing Stock", false, false);
                                    Snackbar.make(findViewById(R.id.outright_issue_activity), "Stock Not Issued", Snackbar.LENGTH_SHORT).show();
                                    jsonArray = new JSONArray();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String errorMessage) {
                                CommonUtility.showProgressDialog(Outright_issue.this, "", "Issueing Stock", false, false);
                                Snackbar.make(findViewById(R.id.outright_issue_activity), "Stock Not Issued", Snackbar.LENGTH_SHORT).show();
                                jsonArray = new JSONArray();
                            }
                        }, jobj);

                    } else {
                        Snackbar.make(findViewById(R.id.outright_issue_activity), "Item Not Added in List", Snackbar.LENGTH_SHORT).show();
                    }
                } else
                    Snackbar.make(findViewById(R.id.outright_issue_activity), "Internet Not Available!!!", Snackbar.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.siteselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableHelper sitesHelper = new TableHelper(getApplicationContext());
                final List<Sites> sites = sitesHelper.getAllSites();
                sitesHelper.close();
                if (sites != null) {
                    final AlertDialog levelDialog;
                    final CharSequence[] filterBy;
                    filterBy = new CharSequence[sites.size()];
                    for (int i = 0; i < sites.size(); i++) {
                        filterBy[i] = sites.get(i).getWHDesc();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(Outright_issue.this);
                    builder.setTitle("Choose");

                    builder.setSingleChoiceItems(filterBy, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                    siteid = sites.get(item).getWHNo();
                                    ((EditText) findViewById(R.id.siteselect)).setText(sites.get(item).getWHDesc());
                                    dialog.dismiss();
                                }
                            });

                    levelDialog = builder.create();
                    levelDialog.show();
                } else {
                    Snackbar.make(findViewById(R.id.outright_issue_activity), "Sites Not Available!!!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.directive_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> directiveType = Arrays.asList(getResources().getStringArray(R.array.directive_Type));
                final AlertDialog levelDialog;
                final CharSequence[] filterBy;
                filterBy = new CharSequence[directiveType.size()];
                for (int i = 0; i < directiveType.size(); i++) {
                    filterBy[i] = directiveType.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        Outright_issue.this);
                builder.setTitle("Choose");

                builder.setSingleChoiceItems(filterBy, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                ((EditText) findViewById(R.id.directive_type)).setText(directiveType.get(item));
                                dialog.dismiss();
                                ((EditText) findViewById(R.id.directive_ref)).requestFocus();
                            }
                        });

                levelDialog = builder.create();
                levelDialog.show();
            }
        });

        findViewById(R.id.search_material).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(((EditText) findViewById(R.id.siteselect)).getText().toString())) {
                    CommonUtility.hideKeyboard(Outright_issue.this);
                    stk.removeAllViews();
                    String mcode = ((EditText) findViewById(R.id.mcode)).getText().toString();
                    if (!TextUtils.isEmpty(mcode)) {
                        TableHelper stockHelper = TableHelper.getInstance(getApplicationContext());
                        List<String> searchStocks = stockHelper.getSearchStocksMcode(mcode,siteid);
                        stockHelper.close();
                        showSearchedStock(searchStocks);
                    } else {
                        Snackbar.make(findViewById(R.id.outright_issue_activity), "Enter Material Code", Snackbar.LENGTH_SHORT).show();
                    }
                } else
                    Snackbar.make(findViewById(R.id.outright_issue_activity), "Select Site First.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void showSearchedStock(final List<String> searchedMaterial) {
        final AlertDialog levelDialog;
        final CharSequence[] filterBy;
        filterBy = new CharSequence[searchedMaterial.size()];
        for (int i = 0; i < searchedMaterial.size(); i++) {
            filterBy[i] = searchedMaterial.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(
                Outright_issue.this);
        builder.setTitle("Choose");
        builder.setSingleChoiceItems(filterBy, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int item) {

                        ((EditText) findViewById(R.id.mcode)).setText(searchedMaterial.get(item));
                        TableHelper stockHelper = TableHelper.getInstance(getApplicationContext());
                        List<Stock> searchStocks = stockHelper.getStockByMcode(searchedMaterial.get(item));
                        stockHelper.close();
                        dialog.dismiss();
                        setSearchedStock(searchStocks);

                    }
                });

        levelDialog = builder.create();
        levelDialog.show();


    }

    private void clearFields() {
        stk.removeAllViews();
        ((EditText) findViewById(R.id.siteselect)).setText("");
        ((EditText) findViewById(R.id.mcode)).setText("");
        ((EditText) findViewById(R.id.directive_type)).setText("");
        ((EditText) findViewById(R.id.directive_ref)).setText("");
        ((EditText) findViewById(R.id.directive_date)).setText("");
        ((EditText) findViewById(R.id.issued_to)).setText("");
        ((EditText) findViewById(R.id.remark_checking)).setText("");
        ((EditText) findViewById(R.id.endorsement_remark)).setText("");

        ((TextView) findViewById(R.id.inlist)).setText("");
        qtyissue = 0;
        stockSrl = 0;
    }

    private void setSearchedStock(List<Stock> searchedMaterial) {


        TableRow tbrow0 = new TableRow(this);
        tbrow0.setBackgroundColor(Color.BLUE);
        TextView tv0 = new TextView(this);
        tv0.setText(" StockSrl ");
        tv0.setPadding(3, 3, 3, 3);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Free Stock ");
        tv1.setPadding(3, 3, 3, 3);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Qty Issue *");
        tv2.setPadding(3, 3, 3, 3);
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
        radioGroup = new RadioGroup(this);

        for (Stock stock : searchedMaterial) {
            if (Integer.parseInt(stock.getQtyGround()) > 0) {
                TableRow row = new TableRow(this);
                row.setWeightSum(3);
                row.setPadding(3, 3, 3, 3);


                TextView stockSrl = new TextView(this);
                stockSrl.setText(stock.getStockSrl());
                stockSrl.setText(stock.getStockSrl());
                stockSrl.setTag("stockSrl" + searchedMaterial.indexOf(stock));
                stockSrl.setTextColor(Color.WHITE);
                stockSrl.setGravity(Gravity.CENTER);
                row.addView(stockSrl);

                TextView freeStock = new TextView(this);
                freeStock.setText(stock.getQtyGround());
                freeStock.setTextColor(Color.WHITE);
                freeStock.setGravity(Gravity.CENTER);
                row.addView(freeStock);

                EditText issueQty = new EditText(this);
                issueQty.setTag("issueQty" + searchedMaterial.indexOf(stock));
                issueQty.setInputType(InputType.TYPE_CLASS_NUMBER);
                issueQty.setOnFocusChangeListener(new MyWatcher(searchedMaterial.size(), searchedMaterial.indexOf(stock)));
                issueQty.setFilters(new InputFilter[]{new InputFilterMinMax("1", stock.getQtyGround())});
                issueQty.setBackgroundColor(Color.WHITE);

                issueQty.setGravity(Gravity.CENTER);
                row.addView(issueQty);
                stk.addView(row);
            }
        }

    }

    private boolean validate() {


        View focusView = null;


        if (TextUtils.isEmpty(((EditText) findViewById(R.id.siteselect)).getText().toString())) {
            focusView = findViewById(R.id.siteselect);
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Select Site", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }


        if (TextUtils.isEmpty(((EditText) findViewById(R.id.mcode)).getText().toString())) {
            focusView = findViewById(R.id.mcode);

            Snackbar.make(findViewById(R.id.outright_issue_activity), "Choose Material Code", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }


        if (TextUtils.isEmpty(((EditText) findViewById(R.id.directive_type)).getText().toString())) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Select Directive Type", Snackbar.LENGTH_SHORT).show();
            focusView = findViewById(R.id.directive_type);
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.directive_ref)).getText().toString())) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Enter Directive Ref.", Snackbar.LENGTH_SHORT).show();
            focusView = findViewById(R.id.directive_ref);
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.directive_date)).getText().toString())) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Enter Directive Date", Snackbar.LENGTH_SHORT).show();
            focusView = findViewById(R.id.directive_date);
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.issued_to)).getText().toString())) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Enter Issued To", Snackbar.LENGTH_SHORT).show();
            focusView = findViewById(R.id.issued_to);
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.remark_checking)).getText().toString())) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Enter Remark Checking", Snackbar.LENGTH_SHORT).show();
            focusView = findViewById(R.id.remark_checking);
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.endorsement_remark)).getText().toString())) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Enter Endorsement Remarks", Snackbar.LENGTH_SHORT).show();
            focusView = findViewById(R.id.endorsement_remark);
            focusView.requestFocus();
            return false;
        }
        if (stk.getChildCount() == 0) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Select Correct Material", Snackbar.LENGTH_SHORT).show();
            focusView = findViewById(R.id.mcode);
            focusView.requestFocus();
            return false;
        } else {
            for (int i = 0; i < stk.getChildCount() - 1; i++) {
                if (!TextUtils.isEmpty(((EditText) stk.findViewWithTag("issueQty" + (i))).getText().toString())) {
                    qtyissue = Integer.parseInt(((EditText) stk.findViewWithTag("issueQty" + (i))).getText().toString());
                    stockSrl = Integer.parseInt(((TextView) stk.findViewWithTag("stockSrl" + (i))).getText().toString());
                }
            }
        }

        if (qtyissue == 0 && stockSrl == 0) {
            Snackbar.make(findViewById(R.id.outright_issue_activity), "Enter no to issue.", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public JSONObject getData() {
        if (validate()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("whno", siteid);
                jsonObject.put("urgencyCode", "N");
                jsonObject.put("dateRaised", CommonUtility.getDate());
                jsonObject.put("dateTimeRegistered", CommonUtility.getDate());
                jsonObject.put("materialCode", ((EditText) findViewById(R.id.mcode)).getText().toString());


                jsonObject.put("dateTimeRequiredBy", CommonUtility.getDate());
                jsonObject.put("directiveType", ((EditText) findViewById(R.id.directive_type)).getText().toString());
                jsonObject.put("directiveRef", ((EditText) findViewById(R.id.directive_ref)).getText().toString());
                jsonObject.put("directiveDate", ((EditText) findViewById(R.id.directive_date)).getText().toString());
                jsonObject.put("raisedBy", ((EditText) findViewById(R.id.issued_to)).getText().toString());
                jsonObject.put("closingCode", "F");
                jsonObject.put("dateTimeClosed", "");
                jsonObject.put("dateChecked", "");
                jsonObject.put("remarksChecking", ((EditText) findViewById(R.id.remark_checking)).getText().toString());
                jsonObject.put("checkedBy", ImsUtility.getUser(getApplicationContext()).getEmployeeId());
                jsonObject.put("toBeReturnedBy", "");
                jsonObject.put("machineryMaterialCode", "");
                jsonObject.put("machinerySrlNo", "");
                jsonObject.put("requistionType", "L");
                jsonObject.put("dateTimeDelivered", CommonUtility.getDate());
                jsonObject.put("handedOverBy", "");
                jsonObject.put("dateTimeTakenOver", "");
                jsonObject.put("takenOverBy", "");
                jsonObject.put("gatePassId", CommonUtility.getDate());
                jsonObject.put("endorsementRemaks", ((EditText) findViewById(R.id.endorsement_remark)).getText().toString());


                jsonObject.put("stockSrl", stockSrl);

                jsonObject.put("qty", qtyissue);


                return jsonObject;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyWatcher implements View.OnFocusChangeListener {
        int size, current;

        public MyWatcher(int size, int current) {
            this.size = size;
            this.current = current;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                for (int i = 0; i < size; i++) {
                    if (i != current&&null!=((EditText) stk.findViewWithTag("issueQty" + (i)))) {
                        ((EditText) stk.findViewWithTag("issueQty" + (i))).setText("");
                        qtyissue = 0;
                        stockSrl = 0;
                    }
                }
        }
    }

}
