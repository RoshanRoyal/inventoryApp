package in.periculum.ims.Report;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import in.periculum.ims.R;
import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Sites;

public class StockReportFragment extends Fragment {
    String siteid;

    public static StockReportFragment newInstance() {
        StockReportFragment fragment = new StockReportFragment();
        return fragment;
    }

    public StockReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.stock_fragment_report, container, false);
        view.findViewById(R.id.sites).setOnClickListener(new View.OnClickListener() {
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
                        filterBy[i] =sites.get(i).getWHNo() + "  " + sites.get(i).getWHDesc();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Choose");

                    builder.setSingleChoiceItems(filterBy, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                    ((EditText) v.findViewById(R.id.sites)).setText( sites.get(item).getWHDesc());
                                    siteid = sites.get(item).getWHNo();
                                    dialog.dismiss();
                                }
                            });

                    levelDialog = builder.create();
                    levelDialog.show();
                } else {
                    Snackbar.make(getActivity().findViewById(R.id.report_activity), "Sites Not Available!!!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        view.findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mcode = ((EditText) view.findViewById(R.id.mcode)).getText().toString();
                String mname = ((EditText) view.findViewById(R.id.mdesc)).getText().toString();
                String stockqty = ((EditText) view.findViewById(R.id.stockqty)).getText().toString();
                if (TextUtils.isEmpty(siteid) && TextUtils.isEmpty(mcode) && TextUtils.isEmpty(mname) && TextUtils.isEmpty(stockqty)) {
                    Snackbar.make(getActivity().findViewById(R.id.report_activity), "Enter Search Creteria", Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), StockReportActivity.class);
                    intent.putExtra("siteid", siteid);
                    intent.putExtra("mcode", mcode);
                    intent.putExtra("mname", mname);
                    intent.putExtra("stockqty", stockqty);
                    startActivity(intent);
                }
            }
        });
        view.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) view.findViewById(R.id.sites)).setText("");
                ((EditText) view.findViewById(R.id.mcode)).setText("");
                ((EditText) view.findViewById(R.id.mdesc)).setText("");
                ((EditText) view.findViewById(R.id.stockqty)).setText("");
            }
        });
        return view;
    }


}
