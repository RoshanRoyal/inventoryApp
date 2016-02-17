package in.periculum.ims;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import in.periculum.ims.database.TableHelper;
import in.periculum.ims.ds.Material;
import in.periculum.ims.handler.CommonHandler;
import in.periculum.ims.listener.HttpRequestCallback;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ConnectionDetector;
import in.periculum.ims.utility.ImsUtility;


public class MaterialManagementFragment extends Fragment {


    public MaterialManagementFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_material_management, container, false);
        CommonUtility.showKeyboard(getActivity());
        view.findViewById(R.id.materialdeno).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.hideKeyboard(getActivity());
                final List<String> materialDeno = Arrays.asList(getResources().getStringArray(R.array.material_Denomination));
                final AlertDialog levelDialog;
                final CharSequence[] filterBy;
                filterBy = new CharSequence[materialDeno.size()];
                for (int i = 0; i < materialDeno.size(); i++) {
                    filterBy[i] = materialDeno.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setTitle("Choose");

                builder.setSingleChoiceItems(filterBy, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                ((EditText) view.findViewById(R.id.materialdeno)).setText(materialDeno.get(item));
                                view.findViewById(R.id.prefloc).requestFocus();
                                dialog.dismiss();
                            }
                        });

                levelDialog = builder.create();
                levelDialog.show();
            }
        });


        view.findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.hideKeyboard(getActivity());
                ((EditText) view.findViewById(R.id.mcode)).setText("");
                ((EditText) view.findViewById(R.id.description)).setText("");
                ((EditText) view.findViewById(R.id.materialdeno)).setText("");
                ((EditText) view.findViewById(R.id.prefloc)).setText("");
                ((EditText) view.findViewById(R.id.bayinfo)).setText("");
                ((EditText) view.findViewById(R.id.bininfo)).setText("");
            }
        });
        view.findViewById(R.id.add_material_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CommonUtility.hideKeyboard(getActivity());
                if (validate()) {
                    String materialCode = ((EditText) view.findViewById(R.id.mcode)).getText().toString();
                    String materialDesc = ((EditText) view.findViewById(R.id.description)).getText().toString();
                    String materialDeno = ((EditText) view.findViewById(R.id.materialdeno)).getText().toString();
                    String prefferedLoc = ((EditText) view.findViewById(R.id.prefloc)).getText().toString();
                    String bay = ((EditText) view.findViewById(R.id.bayinfo)).getText().toString();
                    String bin = ((EditText) view.findViewById(R.id.bininfo)).getText().toString();

                    final Material material = new Material(materialCode, materialDesc, materialDeno, prefferedLoc, bay, bin, "0", CommonUtility.getDate());

                    if (ConnectionDetector.getInstance(getActivity().getApplicationContext()).isConnectingToInternet()) {
                        CommonUtility.showProgressDialog(getActivity(), "", "Adding Material", true, false);
                        JSONObject jobj = null;
                        try {
                            Gson gson = new Gson();
                            jobj = new JSONObject(gson.toJson(material));
                            jobj.put("tag", "saveMaterial");
                            jobj.put("employeeid",ImsUtility.getUser(getActivity().getApplicationContext()).getEmployeeId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtility.showProgressDialog(getActivity(), "", "Adding Material", false, false);
                        }

                        CommonHandler.loadTheData("addMaterial", ImsUtility.BASE_URL, new HttpRequestCallback<JSONObject>() {
                            @Override
                            public void response(String errro, JSONObject returnType) {
                                try {
                                    if (Integer.parseInt(returnType.getString("success")) != 0) {
                                        TableHelper materialHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                                        if (materialHelper.dbMaterialHasData(material.getMaterialCode())) {
                                            Snackbar.make(view, "Material Already Exist", Snackbar.LENGTH_SHORT).show();
                                            CommonUtility.showProgressDialog(getActivity(), "", "Adding Material", false, false);
                                        } else {
                                            material.setServer_uploaded("1");
                                            materialHelper.addCMaterial(material);
                                            Snackbar.make(view, "Material Added", Snackbar.LENGTH_SHORT).show();
                                            CommonUtility.showProgressDialog(getActivity(), "", "Adding Material", false, false);
                                        }
                                        view.findViewById(R.id.reset_button).callOnClick();
                                    } else {
                                        Snackbar.make(view, "Material Already Exist", Snackbar.LENGTH_SHORT).show();
                                        CommonUtility.showProgressDialog(getActivity(), "", "Adding Material", false, false);
                                    }
                                } catch (JSONException e) {
                                    Snackbar.make(view, "Material Not Added, Server Problem", Snackbar.LENGTH_SHORT).show();
                                    CommonUtility.showProgressDialog(getActivity(), "", "Adding Material", false, false);
                                }
                            }

                            @Override
                            public void onError(String errorMessage) {
                                CommonUtility.showProgressDialog(getActivity(), "", "Adding Material", false, false);
                                Snackbar.make(view, "Material Not Added", Snackbar.LENGTH_SHORT).show();
                            }
                        }, jobj);
                    } else {
                        TableHelper materialHelper = TableHelper.getInstance(getActivity().getApplicationContext());
                        if (materialHelper.dbCMaterialHasData(material.getMaterialCode())) {
                            Snackbar.make(view, "Material Already Exist", Snackbar.LENGTH_SHORT).show();
                        } else {
                            materialHelper.addCMaterial(material);
                            Snackbar.make(view, "Material Added", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
        return view;
    }

    private boolean validate() {
        View focusView = null;

        if (TextUtils.isEmpty(((EditText) getActivity().findViewById(R.id.mcode)).getText().toString())) {
            focusView = getActivity().findViewById(R.id.mcode);
            Snackbar.make(getActivity().findViewById(R.id.add_material_form_layout), "Enter Material Code", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) getActivity().findViewById(R.id.description)).getText().toString())) {
            focusView = getActivity().findViewById(R.id.description);
            Snackbar.make(getActivity().findViewById(R.id.add_material_form_layout), "Enter Description", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) getActivity().findViewById(R.id.materialdeno)).getText().toString())) {
            focusView = getActivity().findViewById(R.id.materialdeno);
            Snackbar.make(getActivity().findViewById(R.id.add_material_form_layout), "Enter Material Deno", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) getActivity().findViewById(R.id.prefloc)).getText().toString())) {
            focusView = getActivity().findViewById(R.id.prefloc);
            Snackbar.make(getActivity().findViewById(R.id.add_material_form_layout), "Enter Preffered Location", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) getActivity().findViewById(R.id.bayinfo)).getText().toString())) {
            focusView = getActivity().findViewById(R.id.bayinfo);
            Snackbar.make(getActivity().findViewById(R.id.add_material_form_layout), "Enter Bay Ifo", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) getActivity().findViewById(R.id.bininfo)).getText().toString())) {
            focusView = getActivity().findViewById(R.id.bininfo);
            Snackbar.make(getActivity().findViewById(R.id.add_material_form_layout), "Enter Bin Info", Snackbar.LENGTH_SHORT).show();
            focusView.requestFocus();
            return false;
        }
        return true;
    }

}
