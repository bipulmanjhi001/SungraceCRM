package sungracecrm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import co.sungracecrm.R;
import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class AdDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    JsonObjectHandler handler;
    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName;
    TextView userName;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferencesLoginDetails=getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sFullName=sharedPreferencesLoginDetails.getString("Name","");
        sMobileNo=sharedPreferencesLoginDetails.getString("MobileNo","");
        sEmailId=sharedPreferencesLoginDetails.getString("EmailId","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sAccessToken=sharedPreferencesLoginDetails.getString("AccessToken","");
        sProfilePic=sharedPreferencesLoginDetails.getString("ProfilePic","");
        sPassword=sharedPreferencesLoginDetails.getString("pass","");
        sUserName=sharedPreferencesLoginDetails.getString("username","");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new AdDashboardFragment()).commit();

        View hView =  navigationView.getHeaderView(0);
        userName=hView.findViewById(R.id.userFullName);
        userImage=hView.findViewById(R.id.userImage);

        if (sAccessToken != null && sAccessToken != "") {
            userName.setText("Hello"+" "+sUserName+"!");
            Picasso.with(AdDashboardActivity.this)
                    .load(sProfilePic)
                    .error(R.mipmap.smalllogo)
                    .placeholder(R.drawable.ic_person_white)
                    .into(userImage);

        }else{
            userName.setText("Hello Admin!");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ad_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new AdDashboardFragment();
        } else if (id == R.id.nav_new_complaints) {
            fragment = new AdNewComplaintListFragment();
        } else if (id == R.id.nav_pending_complaints) {
            fragment = new AdPendingComplaintListFragment();
        } else if (id == R.id.nav_progress_complaints) {
            fragment = new AdProgressComplaintListFragment();
        } else if (id == R.id.nav_completed_complaints) {
            fragment = new AdCompletedComplaintListFragment();
        } else if (id == R.id.nav_add_complaint) {
            fragment = new AdCustomerComplaintRegisterFragment();
        } else if (id == R.id.nav_technician_List) {
            fragment = new AdTechnicianListFragment();
        } else if (id == R.id.nav_Add_technician) {
            fragment = new AdAddTechnicianFragment();
        } else if (id == R.id.nav_Report_list) {
            fragment = new ComplaintReportListFragment();
        } else if (id == R.id.nav_SelfCall_list) {
            fragment = new SelfCallListFragment();


        } else if (id == R.id.nav_Spare_Request_list) {
            fragment = new AdSpareRequestedListFragment();

        } else if (id == R.id.nav_logout) {
            if (sAccessToken != null && sAccessToken != "") {
                new UserLogout(sUserName).execute();
            }
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack("AdDashboardActivity")
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class UserLogout extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String mUserName;

        UserLogout(String UserName){
            this.mUserName=UserName;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("username",""+mUserName);
                jsonObject=handler.makeHttpRequest(AppConstant.userLogoutURL, "POST", data,null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        SharedPreferences.Editor editor=sharedPreferencesLoginDetails.edit();
                            editor.putString("Name", "");
                            editor.putString("MobileNo", "");
                            editor.putString("EmailId", "");
                            editor.putString("Role", "");
                            editor.putString("AccessToken", "");
                            editor.putString("ProfilePic", "");
                            editor.putString("pass", "");
                            editor.putString("username", "");
                            editor.commit();

                            Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(loginIntent);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage(message)
                                .setTitle("Warning")
                                .setIcon(R.drawable.ic_warning)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert =builder.create();
                        alert.show();
                    }
                }

            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Network busy try again")
                        .setTitle("Warning")
                        .setIcon(R.drawable.ic_warning)
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert =builder.create();
                alert.show();
            }
        }
    }
}
