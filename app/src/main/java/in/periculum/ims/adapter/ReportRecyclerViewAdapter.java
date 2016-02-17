package in.periculum.ims.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import in.periculum.ims.R;
import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Stock;
import in.periculum.ims.ds.StockRcv;

/**
 * Created by ROYAL on 10/31/2015.
 */
public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView materialName;
        private final TextView stockSrl;
        private final TextView materialCode;
        private final TextView qty;

        public TextView getMaterialName() {
            return materialName;
        }

        public TextView getStockSrl() {
            return stockSrl;
        }

        public TextView getMaterialCode() {
            return materialCode;
        }

        public TextView getQty() {
            return qty;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            materialName = (TextView) itemView.findViewById(R.id.materialName);
            materialCode = (TextView) itemView.findViewById(R.id.materialCode);
            qty = (TextView) itemView.findViewById(R.id.qty);
            stockSrl = (TextView) itemView.findViewById(R.id.stockSrl);

        }
    }

    ArrayList<Stock> stock;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Activity activity;
    private TableHelper stockHelper;

    public ReportRecyclerViewAdapter(ArrayList<Stock> stock, Activity activity) {
        this.stock = stock;
        this.activity = activity;
        stockHelper = new TableHelper(activity.getApplicationContext());
    }

    @Override
    public int getItemViewType(int position) {
        return stock.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    RecyclerView.ViewHolder vh;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.report_row_item, parent, false);

            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            ((ViewHolder) holder).getMaterialCode().setText(stock.get(position).getMaterialCode());
            ((ViewHolder) holder).getQty().setText(stock.get(position).getQtyGround());
            ((ViewHolder) holder).getMaterialName().setText(stockHelper.getMaterialName(stock.get(position).getMaterialCode()));
            ((ViewHolder) holder).getStockSrl().setText(stock.get(position).getStockSrl());

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
        stockHelper.close();
    }

    @Override
    public int getItemCount() {
        return stock.size();
    }

}
