package in.periculum.ims.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.periculum.ims.R;
import in.periculum.ims.ReceiptsActivity;
import in.periculum.ims.ds.StockRcv;
import in.periculum.ims.utility.ConnectionDetector;

/**
 * Created by ROYAL on 10/31/2015.
 */
public class StockRcvRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView reqno;

        private final TextView materialCode;
        private final TextView qty;

        public TextView getReqno() {
            return reqno;
        }



        public TextView getMaterialCode() {
            return materialCode;
        }

        public TextView getQty() {
            return qty;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            reqno = (TextView) itemView.findViewById(R.id.reqno);
            materialCode = (TextView) itemView.findViewById(R.id.material);
            qty = (TextView) itemView.findViewById(R.id.qty);
        }
    }

    ArrayList<StockRcv> stockRcvs;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Activity activity;
    public StockRcvRecyclerViewAdapter(ArrayList<StockRcv> stockRcvs,Activity activity) {
        this.stockRcvs = stockRcvs;
        this.activity=activity;
    }

    @Override
    public int getItemViewType(int position) {
        return stockRcvs.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    RecyclerView.ViewHolder vh;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.text_row_item, parent, false);

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

                ((ViewHolder) holder).getReqno().setText(stockRcvs.get(position).getRequisitionNo());
                ((ViewHolder) holder).getMaterialCode().setText(stockRcvs.get(position).getMaterialCode());

                ((ViewHolder) holder).getQty().setText(stockRcvs.get(position).getQty());

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return stockRcvs.size();
    }

}
