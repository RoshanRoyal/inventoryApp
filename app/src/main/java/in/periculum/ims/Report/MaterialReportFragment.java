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

public class MaterialReportFragment extends Fragment {
    String siteid;

    public static MaterialReportFragment newInstance() {
        MaterialReportFragment fragment = new MaterialReportFragment();
        return fragment;
    }

    public MaterialReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.material_fragment_report, container, false);

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
