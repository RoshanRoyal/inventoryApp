package in.periculum.ims;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.periculum.ims.adapter.StockRcvRecyclerViewAdapter;
import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.StockRcv;
import in.periculum.ims.ds.Sites;
import in.periculum.ims.listener.RecyclerItemClickListener;
import in.periculum.ims.utility.ConnectionDetector;


public class ReceiveGoodFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private StockRcvRecyclerViewAdapter mAdapter;
    private ArrayList<StockRcv> myDataset;


    public ReceiveGoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_receive_good, container, false);


        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                // do whatever
                StockrcvDetail(myDataset.get(position));
            }
        }));
         v.findViewById(R.id.sitesspinner).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(final View v) {
                 TableHelper sitesHelper = new TableHelper(getActivity().getApplicationContext());
                 final List<Sites> sites = sitesHelper.getAllSites();
                 sitesHelper.close();
                 if (sites != null) {
                     final AlertDialog levelDialog;
                     final CharSequence[] filterBy;
                     filterBy = new CharSequence[sites.size()];
                     for (int i = 0; i < sites.size(); i++) {
                         filterBy[i] = sites.get(i).getWHDesc();
                     }

                     AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                     builder.setTitle("Choose");

                     builder.setSingleChoiceItems(filterBy, -1,
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog,
                                                     int item) {
                                     ((EditText) v.findViewById(R.id.sitesspinner)).setText(sites.get(item).getWHDesc());
                                     TableHelper stockRcvHelper = new TableHelper(getActivity().getApplicationContext());
                                     myDataset = stockRcvHelper.getAllStockRcvsBySite(sites.get(item).getWHNo());
                                     dialog.dismiss();
                                     loadIntoRecycleView();
                                 }
                             });

                     levelDialog = builder.create();
                     levelDialog.show();
                 } else {
                     Snackbar.make(getActivity().findViewById(R.id.receive_good_frag), "Sites Not Available!!!", Snackbar.LENGTH_SHORT).show();
                 }
             }
         });


        return v;

    }
    public void StockrcvDetail(final StockRcv stockRcv){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the
        // dialog layout
        View view=inflater.inflate(R.layout.rcv_stock_detail, null);

        ((TextView)view.findViewById(R.id.requistionNo)).setText(stockRcv.getRequisitionNo());
        ((TextView)view.findViewById(R.id.materialCode)).setText(stockRcv.getMaterialCode());
        ((TextView)view.findViewById(R.id.materialDesc)).setText(stockRcv.getMaterialDesc());
        ((TextView)view.findViewById(R.id.qty)).setText(stockRcv.getQty());
        ((TextView)view.findViewById(R.id.qtycarried)).setText(stockRcv.getQtyCarried());
        ((TextView)view.findViewById(R.id.qtyReceived)).setText(stockRcv.getQtyReceived());
        ((TextView)view.findViewById(R.id.receiptMismatchQty)).setText(stockRcv.getReceiptMismatchQty());
        ((TextView)view.findViewById(R.id.qtytorcv)).setText(stockRcv.getQtyPendingToReceive());
        ((TextView)view.findViewById(R.id.materialDeno)).setText(stockRcv.getMaterialDeno());



        builder.setTitle("StockRcv Details");
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(view)
                // Add action buttons
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Receive", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(ConnectionDetector.getInstance(getActivity().getApplicationContext()).isConnectingToInternet())
                        {
                            Intent i = new Intent(getActivity(), ReceiptsActivity.class);
                            i.putExtra("stockRcv",stockRcv);
                            getActivity().startActivity(i);
                        }else{
                            Toast.makeText(getActivity(), "Connect To Internet For StockRcv Receive", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        builder.create();
        builder.show();
    }
    private void loadIntoRecycleView() {
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new StockRcvRecyclerViewAdapter(myDataset,getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }



}
