package in.periculum.ims;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Stock;
import in.periculum.ims.ds.StockRcv;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ConnectionDetector;
import in.periculum.ims.utility.ImsUtility;

public class ReceiptsActivity extends AppCompatActivity {
    StockRcv stockRcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_receipts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stockRcv = (StockRcv) getIntent().getSerializableExtra("stockRcv");

        ((TextView) findViewById(R.id.requistionNo)).setText(stockRcv.getRequisitionNo());
        ((TextView) findViewById(R.id.material)).setText(stockRcv.getMaterialCode());
        ((EditText) findViewById(R.id.qtyissued)).setText(stockRcv.getQtyCarried());

        findViewById(R.id.packtype).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> packitems = Arrays.asList(getResources().getStringArray(R.array.pack_Type));
                final AlertDialog levelDialog;
                final CharSequence[] filterBy;
                filterBy = new CharSequence[packitems.size()];
                for (int i = 0; i < packitems.size(); i++) {
                    filterBy[i] = packitems.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ReceiptsActivity.this);
                builder.setTitle("Choose Pack Type");

                builder.setSingleChoiceItems(filterBy, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                ((EditText) findViewById(R.id.packtype)).setText(packitems.get(item));
                                dialog.dismiss();
                            }
                        });

                levelDialog = builder.create();
                levelDialog.show();
            }
        });
        findViewById(R.id.mismatched_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> mismatched_type = Arrays.asList(getResources().getStringArray(R.array.receipt_MismatchType));
                final AlertDialog levelDialog;
                final CharSequence[] filterBy;
                filterBy = new CharSequence[mismatched_type.size()];
                for (int i = 0; i < mismatched_type.size(); i++) {
                    filterBy[i] = mismatched_type.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ReceiptsActivity.this);
                builder.setTitle("Choose Pack Type");

                builder.setSingleChoiceItems(filterBy, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                ((EditText) findViewById(R.id.mismatched_type)).setText(mismatched_type.get(item));
                                dialog.dismiss();
                            }
                        });

                levelDialog = builder.create();
                levelDialog.show();
            }
        });
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.rcv_stock_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getInstance(getApplicationContext()).isConnectingToInternet()) {
                    JSONObject jobj = new JSONObject();
                    try {
                        JSONObject jsonObject = getData();
                        if (jsonObject == null)
                            return;
                        jobj.put("stocks", jsonObject);
                        jobj.put("tag", "goodsReceiptInsert");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    CommonUtility.showProgressDialog(ReceiptsActivity.this, "", "Receiving Stock", true, false);

                    CommonHandler.loadTheData("stockRcv", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
                        @Override
                        public void response(String errro, JSONObject returnType) {
                            try {
                                if (Integer.parseInt(returnType.getString("success")) != 0) {
                                    TableHelper stockHelper = TableHelper.getInstance(getApplicationContext());

                                    Stock stock = stockHelper.getStock(stockRcv.getMaterialCode(), stockRcv.getSrl());
                                    if (stock != null) {
                                        int x = Integer.parseInt(stock.getQtyGround()) +
                                                Integer.parseInt(((EditText) findViewById(R.id.qtyreceived)).getText().toString());
                                        stock.setQtyGround("" + x);
                                        stockHelper.updateStock(stock);
                                    } else {
                                        stockHelper.addStock(new Stock(stockRcv.getMaterialCode(), stockRcv.getSrl(), stockRcv.getWHNo()
                                                , ((EditText) findViewById(R.id.batchno)).getText().toString()
                                                , "New", ((EditText) findViewById(R.id.s_issue_qty)).getText().toString(),
                                                ((EditText) findViewById(R.id.packtype)).getText().toString()
                                                , "NA", ((EditText) findViewById(R.id.qtyreceived)).getText().toString(), "NA", CommonUtility.getDate()));

                                    }
                                    int x = Integer.parseInt(stockRcv.getQty()) -
                                            Integer.parseInt(((EditText) findViewById(R.id.qtyreceived)).getText().toString());
                                    stockRcv.setQty("" + x);
                                    stockRcv.setQtyReceived(((EditText) findViewById(R.id.qtyreceived)).getText().toString());
                                    stockHelper.updateStockRcvs(stockRcv);
                                    stockHelper.close();
                                    CommonUtility.showProgressDialog(ReceiptsActivity.this, "", "", false, false);
                                    Snackbar.make(findViewById(R.id.stock_rcv_form_layout), ((EditText) findViewById(R.id.qtyreceived)).getText().toString() + "Stock Received", Snackbar.LENGTH_SHORT).show();
                                    ((EditText) findViewById(R.id.qtyissued)).setText(""+x);
                                    showMsgAlert();
                                } else {
                                    CommonUtility.showProgressDialog(ReceiptsActivity.this, "", "", false, false);
                                    Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Stock Not Received", Snackbar.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                CommonUtility.showProgressDialog(ReceiptsActivity.this, "", "", false, false);
                                Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Stock Not Received", Snackbar.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            CommonUtility.showProgressDialog(ReceiptsActivity.this, "", "", false, false);
                            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Stock Not Received", Snackbar.LENGTH_SHORT).show();

                        }
                    }, jobj);
                } else
                    Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Internet Not Available!!!", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void showMsgAlert() {
        String title = "Alert!";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(title);
        builder.setMessage("Stock Received Successfully").setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((EditText) findViewById(R.id.remarks)).setText("");
                        ((EditText) findViewById(R.id.batchno)).setText("");
                        ((EditText) findViewById(R.id.s_issue_qty)).setText("");
                        ((EditText) findViewById(R.id.packtype)).setText("");
                        ((EditText) findViewById(R.id.qtyreceived)).setText("");
                        ((EditText) findViewById(R.id.endorsement)).setText("");
                        ((EditText) findViewById(R.id.stock_loc)).setText("");
                        ((EditText) findViewById(R.id.mismatched_qty)).setText("");
                        ((EditText) findViewById(R.id.mismatched_type)).setText("");
                        ((EditText) findViewById(R.id.mismatched_remarks)).setText("");
                        ((EditText) findViewById(R.id.qtyaccepted)).setText("");
                        dialog.dismiss();
                        ((EditText) findViewById(R.id.qtyreceived)).requestFocus();
                    }

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private boolean validate() {
        View focusView;

        if (TextUtils.isEmpty(((EditText) findViewById(R.id.qtyissued)).getText().toString())) {
            focusView = findViewById(R.id.qtyissued);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "issued qty is null", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.qtyreceived)).getText().toString())) {
            focusView = findViewById(R.id.qtyreceived);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Enter Qty received", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.qtyaccepted)).getText().toString())) {
            focusView = findViewById(R.id.qtyaccepted);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Enter Qty Accepted", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.packtype)).getText().toString())) {
            focusView = findViewById(R.id.packtype);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Choose Pack Type", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.s_issue_qty)).getText().toString())) {
            focusView = findViewById(R.id.s_issue_qty);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Enter Smallest issue Qty", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.batchno)).getText().toString())) {
            focusView = findViewById(R.id.batchno);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Enter Batch no", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.stock_loc)).getText().toString())) {
            focusView = findViewById(R.id.stock_loc);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Enter Stock Location", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.remarks)).getText().toString())) {
            focusView = findViewById(R.id.remarks);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Enter Remarks", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.endorsement)).getText().toString())) {
            focusView = findViewById(R.id.endorsement);
            Snackbar.make(findViewById(R.id.stock_rcv_form_layout), "Enter Endorsement", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    private JSONObject getData() {
        if (validate()) {
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("materialCode", stockRcv.getMaterialCode());
                jsonObject.put("WHNo", stockRcv.getWHNo());
                jsonObject.put("goodsReceiptChoice", "R");
                jsonObject.put("indentNo", "");
                jsonObject.put("vendorCode", "");
                jsonObject.put("pODate", "");
                jsonObject.put("pODetailNo", "");
                jsonObject.put("materialSurveyConsumerCode", "");
                jsonObject.put("materialSurveyNo", "");
                jsonObject.put("remarks", ((EditText) findViewById(R.id.remarks)).getText().toString());
                jsonObject.put("dateTimeApproved", CommonUtility.getDate());
                jsonObject.put("approvedBy", ImsUtility.getUser(getApplicationContext()).getEmployeeId());
                jsonObject.put("consumerCode", stockRcv.getWHNo());
                jsonObject.put("requisitionNo", stockRcv.getRequisitionNo());
                jsonObject.put("accessories", "");
                jsonObject.put("pettyPurchaseID", "");
                jsonObject.put("pettyPurchaseDetailNo", "");
                jsonObject.put("batchNo", ((EditText) findViewById(R.id.batchno)).getText().toString());
                jsonObject.put("smallestIssuablePackQty", ((EditText) findViewById(R.id.s_issue_qty)).getText().toString());
                jsonObject.put("packType", ((EditText) findViewById(R.id.packtype)).getText().toString());
                jsonObject.put("qtyReceived", ((EditText) findViewById(R.id.qtyreceived)).getText().toString());
                jsonObject.put("endorsment", ((EditText) findViewById(R.id.endorsement)).getText().toString());
                jsonObject.put("locationMarking", ((EditText) findViewById(R.id.stock_loc)).getText().toString());
                jsonObject.put("receiptMismatchQty", ((EditText) findViewById(R.id.mismatched_qty)).getText().toString());
                jsonObject.put("receiptMismatchType", ((EditText) findViewById(R.id.mismatched_type)).getText().toString());
                jsonObject.put("receiptMismatchRemarks", ((EditText) findViewById(R.id.mismatched_remarks)).getText().toString());
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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
