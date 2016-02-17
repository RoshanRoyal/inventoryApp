package in.periculum.ims;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Calendar;

import in.periculum.ims.Broadcast.SyncStatusBR;
import in.periculum.ims.Report.ReportSelect;
import in.periculum.ims.utility.CommonUtility;
import in.periculum.ims.utility.ImsUtility;

public class Dashboard extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    Fragment fr;
    FragmentTransaction fragmentTransaction;
    FragmentManager fm;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //broadcast start to get server notification
        registerBroadcastReceivers();


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            if(getIntent().getStringExtra("intntdata")!=null)
            {
                fr=DataSyncFragment.newInstance(getIntent().getStringExtra("intntdata"),true);
            }else if(!ImsUtility.getIsFirst(getApplicationContext())){
                ImsUtility.saveIsFirst(getApplicationContext());
                String json="[{\"TableName\":\"Material\",\"UpdateTrueFalse\":1}," +
                        "{\"TableName\":\"SiteAccess\",\"UpdateTrueFalse\":1}," +
                        "{\"TableName\":\"Stock\",\"UpdateTrueFalse\":1}," +
                        "{\"TableName\":\"Notification\",\"UpdateTrueFalse\":1}," +
                        "{\"TableName\":\"GoodsReceiptRequisition\",\"UpdateTrueFalse\":1}]";
                fr=DataSyncFragment.newInstance(json,true);
            }else {
                // Create a new Fragment to be placed in the activity layout
                fr = new DashboardFragment();
            }
            fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fr).commit();

        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                CommonUtility.showExitAlert(Dashboard.this);

            }
        }



        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.

            switch (item.getItemId()){
                case R.id.home :
                    fr = new DashboardFragment();
                    switchContent(fr);
                    break;
                case R.id.add_material :
                    fr = new MaterialManagementFragment();
                    switchContent(fr);
                    break;
                case R.id.data_sync :
                    fr = DataSyncFragment.newInstance(null,false);
                    switchContent(fr);
                    break;
                case R.id.review :
                   intent=new Intent(this,Reviews.class);
                    startActivity(intent);
                    break;
                case R.id.notification :
                    intent=new Intent(this,NotificationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.reports :
                    intent=new Intent(Dashboard.this,ReportSelect.class);
                    startActivity(intent);
                    break;
                case R.id.stock_issue :
                      intent=new Intent(Dashboard.this,Outright_issue.class);
                    startActivity(intent);
                    break;
                case R.id.stock_receive :
                    fr=new ReceiveGoodFragment();
                    switchContent(fr);
                    break;
                default:
                    break;
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    public void switchContent(Fragment fragment) {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    private void registerBroadcastReceivers(){
        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), SyncStatusBR.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 15000, 60 * 15000, pendingIntent);


    }
}
