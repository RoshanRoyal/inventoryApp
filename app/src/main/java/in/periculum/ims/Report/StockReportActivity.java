package in.periculum.ims.Report;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.ArrayList;

import in.periculum.ims.R;
import in.periculum.ims.adapter.ReportRecyclerViewAdapter;
import in.periculum.ims.adapter.StockRcvRecyclerViewAdapter;
import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Stock;

public class StockReportActivity extends AppCompatActivity {


    String siteid = "", mcode = "", mname = "", stockqty = "";
    ArrayList<Stock> stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().getStringExtra("siteid") != null) {
            siteid = getIntent().getStringExtra("siteid");

        }
        if (getIntent().getStringExtra("mcode") != null)
            mcode = getIntent().getStringExtra("mcode");
        if (getIntent().getStringExtra("mname") != null)
            mname = getIntent().getStringExtra("mname");
        if (getIntent().getStringExtra("stockqty") != null)
            stockqty = getIntent().getStringExtra("stockqty");
        TableHelper stockHelper = TableHelper.getInstance(getApplicationContext());


        if (TextUtils.isEmpty(stockqty) && !TextUtils.isEmpty(mcode) && TextUtils.isEmpty(mname) && TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByMcode(mcode);
        if (TextUtils.isEmpty(stockqty) && TextUtils.isEmpty(mcode) && !TextUtils.isEmpty(mname) && TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByMName(mname);
        if (TextUtils.isEmpty(stockqty) && TextUtils.isEmpty(mcode) && TextUtils.isEmpty(mname) && !TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByCondition(siteid);
        if (!TextUtils.isEmpty(stockqty) && TextUtils.isEmpty(mcode) && TextUtils.isEmpty(mname) && TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByStockQty(stockqty);

        if (TextUtils.isEmpty(stockqty) && !TextUtils.isEmpty(mcode) && TextUtils.isEmpty(mname) && !TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByCondition(siteid, mcode);
        if (TextUtils.isEmpty(stockqty) && !TextUtils.isEmpty(mcode) && !TextUtils.isEmpty(mname) && !TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByCondition(siteid, mcode, mname);
        if (!TextUtils.isEmpty(stockqty) && !TextUtils.isEmpty(mcode) && !TextUtils.isEmpty(mname) && !TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByCondition(siteid, mcode, mname, stockqty);
        if (!TextUtils.isEmpty(stockqty) && !TextUtils.isEmpty(mcode) && !TextUtils.isEmpty(mname) && TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByConditionNoSite(mcode, mname, stockqty);
        if (!TextUtils.isEmpty(stockqty) && TextUtils.isEmpty(mcode) && !TextUtils.isEmpty(mname) && TextUtils.isEmpty(siteid))
            stocks = stockHelper.getStockByConditionNoSiteMcode(mname, stockqty);

        stockHelper.close();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ReportRecyclerViewAdapter mAdapter = new ReportRecyclerViewAdapter(stocks, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
