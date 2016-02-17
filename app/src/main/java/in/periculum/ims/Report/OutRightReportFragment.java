package in.periculum.ims.Report;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import in.periculum.ims.R;

public class OutRightReportFragment extends Fragment {
    String siteid;

    public static OutRightReportFragment newInstance() {
        OutRightReportFragment fragment = new OutRightReportFragment();
        return fragment;
    }

    public OutRightReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.outright_fragment_report, container, false);

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
