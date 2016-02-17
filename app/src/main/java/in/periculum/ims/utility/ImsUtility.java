package in.periculum.ims.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;

import com.google.gson.Gson;

import in.periculum.ims.ds.Employee;

/**
 * Created by ROYAL on 10/29/2015.
 */
public class ImsUtility {
    public static final String EMPLOYEE_DS_SCN = "empinfo";
    public static final String EMPLOYEE_MPIN = "empmpin";
    public static final String IS_SYNCING = "issyncing";
    public static final String IS_FIRST = "isfirst";
    public static final String Site_Date= "sitedate";
    public static final String Material_Date = "materialdate";
    public static final String Notification_Date = "materialdate";
    public static final String Stock_Issue_Date = "stockissuedate";
    public static final String Stock_Rcv_Date = "stockrcvdate";
    public static final String BASE_URL= "http://www.analytics.periculum.in/index.php";
    public static final String DATABASE_NAME = "ims.sqlite";

    public static void saveEmpMpin(Context context, String mpin) {

        RawStorageProvider.getInstance(context).dumpDataToStorage(EMPLOYEE_MPIN, mpin);
    }

    public static void deleteEmpMpin(Context context) {
        RawStorageProvider.getInstance(context).delete(EMPLOYEE_MPIN);
    }

    public static String getEmpMpin(Context context) {
        String rawmpin = RawStorageProvider.getInstance(context).getDataFromStorage(EMPLOYEE_MPIN) ;

        if (rawmpin == null) {
            return null;
        }else
        return rawmpin;
    }

    public static void saveIsSyncing(Context context) {

        RawStorageProvider.getInstance(context).dumpDataToStorage(IS_SYNCING, true);
    }

    public static void deleteIsSyncing(Context context) {
        RawStorageProvider.getInstance(context).delete(IS_SYNCING);
    }

    public static boolean getIsSyncing(Context context) {
        boolean status = RawStorageProvider.getInstance(context).getBooleanFromStorage(IS_SYNCING) ;

        return status;
    }
    public static void saveIsFirst(Context context) {

        RawStorageProvider.getInstance(context).dumpDataToStorage(IS_FIRST, true);
    }

    public static void deleteIsFirst(Context context) {
        RawStorageProvider.getInstance(context).delete(IS_FIRST);
    }

    public static boolean getIsFirst(Context context) {
        boolean status = RawStorageProvider.getInstance(context).getBooleanFromStorage(IS_FIRST) ;

        return status;
    }

    public static void saveDate(Context context, String date,String forwhom) {

        RawStorageProvider.getInstance(context).dumpDataToStorage(forwhom, date);
    }

    public static void deleteDate(Context context,String forwhom) {
        RawStorageProvider.getInstance(context).delete(forwhom);
    }

    public static String getDate(Context context,String forwhom) {
        String materialDate = RawStorageProvider.getInstance(context).getDataFromStorage(forwhom) ;

        if (materialDate == null) {
            return "";
        }else
            return  materialDate;
    }



    public static void saveUser(Context context, Employee ds) {
        String jsonValue = new Gson().toJson(ds);
        RawStorageProvider.getInstance(context).dumpDataToStorage(EMPLOYEE_DS_SCN, jsonValue);
    }

    public static void deleteUser(Context context) {
        RawStorageProvider.getInstance(context).delete(EMPLOYEE_DS_SCN);
    }

    public static Employee getUser(Context context) {
        String user = RawStorageProvider.getInstance(context).getDataFromStorage(EMPLOYEE_DS_SCN);
        if ((user != null) && (!user.equals(""))) {
            return (Employee) new Gson().fromJson(user, Employee.class);
        }
        return null;
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Activity activity, final View mProgressView, final View mLoginFormView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
