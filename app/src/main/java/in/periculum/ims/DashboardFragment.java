package in.periculum.ims;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import in.periculum.ims.application.AppController;
import in.periculum.ims.ds.Employee;
import in.periculum.ims.utility.ImsUtility;


public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);
        NetworkImageView ntwkImg= (NetworkImageView) view.findViewById(R.id.profileimg);
        ntwkImg.setDefaultImageResId(R.drawable.profile);
        Employee emp= ImsUtility.getUser(getActivity().getApplicationContext());
         ntwkImg.setImageUrl(emp.getEmpImagePath(), AppController.getInstance().getImageLoader());
        ((TextView) view.findViewById(R.id.empid)).setText(emp.getEmployeeId());
        ((TextView)view.findViewById(R.id.empname)).setText(emp.getName());
        ((TextView)view.findViewById(R.id.designation)).setText(emp.getDesig());
        ((TextView)view.findViewById(R.id.mobile)).setText(emp.getMobileNo());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
