package sungracecrm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,LocationListener {
    LinearLayout linlaHeaderProgress;
    JsonObjectHandler handler;
    private SharedPreferences sharedPreferencesLoginDetails,sharedPreferencesTechOfficeTimeCNF;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sUserID,Logincnf;
    TextView userName;
    ImageView userImage;
    LocationManager locationManager;
    String aAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linlaHeaderProgress =  findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);
        if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        getLocation();

        sharedPreferencesLoginDetails=getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sFullName=sharedPreferencesLoginDetails.getString("Name","");
        sMobileNo=sharedPreferencesLoginDetails.getString("MobileNo","");
        sEmailId=sharedPreferencesLoginDetails.getString("EmailId","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sAccessToken=sharedPreferencesLoginDetails.getString("AccessToken","");
        sProfilePic=sharedPreferencesLoginDetails.getString("ProfilePic","");
        sPassword=sharedPreferencesLoginDetails.getString("pass","");
        sUserName=sharedPreferencesLoginDetails.getString("username","");
        sUserID=sharedPreferencesLoginDetails.getString("Id","");

        sharedPreferencesTechOfficeTimeCNF=getApplicationContext().getSharedPreferences("officeLogin", Context.MODE_PRIVATE);
        Logincnf=sharedPreferencesLoginDetails.getString("Conformation","");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new HomeFragment()).commit();

        View hView =  navigationView.getHeaderView(0);
        userName=hView.findViewById(R.id.userFullName);
        userImage=hView.findViewById(R.id.userImage);

        if (sAccessToken != null && sAccessToken != "") {
            userName.setText("Hello"+" "+sUserName+"!");
            Picasso.with(HomeActivity.this)
                    .load(sProfilePic)
                    .error(R.mipmap.smalllogo)
                    .placeholder(R.drawable.ic_person_white)
                    .into(userImage);
        }else{
            userName.setText("Hello Guest!");
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
        getMenuInflater().inflate(R.menu.home, menu);
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
            fragment = new HomeFragment();

        } else if (id == R.id.nav_all_job) {
            fragment = new AllJobListFragment();

        } else if (id == R.id.nav_assigned_job) {
            fragment = new AssignedJobListFragment();

        } else if (id == R.id.nav_progress_job) {
            fragment = new ProgressJobListFragment();

        } else if (id == R.id.nav_completed_job) {
            fragment = new CompletedJobListFragment();

        } else if (id == R.id.nav_SparesIndent_List) {
            fragment=new SparesIndentListFragment();

        } else if (id == R.id.nav_SparesIndent_dispatched_List) {
            fragment=new SpareIndentDispachedListFragment();

        } else if (id == R.id.nav_Complaint_report_List) {
            fragment=new ComplaintReportListFragment();

        } else if (id == R.id.nav_selfcall) {
            fragment=new SelfCallFragment();

        } else if (id == R.id.nav_selfcall_report) {
            fragment=new SelfCallListFragment();

        } else if (id == R.id.nav_office_In) {
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                new OfficeInTimePost(sUserID,aAddress).execute();

        } else if (id == R.id.nav_office_Out) {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
                new OfficeOutTimePost(sUserID,aAddress).execute();


        } else if (id == R.id.nav_logout) {
            if (sAccessToken != null && sAccessToken != "") {
                new UserLogout(sUserName).execute();
            }
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack("HomeActivity")
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
    public class OfficeInTimePost extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String uUserId;
        String aAddress;

        OfficeInTimePost(String USERID,String ADDRESS){
            this.uUserId=USERID;
            this.aAddress=ADDRESS;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("technician_id",""+uUserId);
                data.put("inlocation",""+aAddress);
                jsonObject=handler.makeHttpRequest(AppConstant.officeInDataPostURL, "POST", data,null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONObject resultObject=jsonObject.optJSONObject("Result");

                        String Conformation = resultObject.getString("logincnf");
                        Toast toast=Toast.makeText(getApplicationContext(),Conformation,Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
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
    public class OfficeOutTimePost extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String uUserId;
        String aAddress;
        OfficeOutTimePost(String USERID,String ADDRESS){
            this.uUserId=USERID;
            this.aAddress=ADDRESS;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("technician_id",""+uUserId);
                data.put("outlocation",""+aAddress);
                jsonObject=handler.makeHttpRequest(AppConstant.officeOutDataPostURL, "POST", data,null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONObject resultObject=jsonObject.optJSONObject("Result");

                        String Conformation = resultObject.getString("logincnf");
                        Toast toast=Toast.makeText(getApplicationContext(),Conformation,Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
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
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        aAddress=("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude()+",");

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            aAddress=(aAddress + "\n"+addresses.get(0).getAddressLine(0));
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Please Enable GPS and Internet")
                .setTitle("Location Permission")
                .setIcon(R.drawable.ic_location)
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert =builder.create();
        alert.show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }
}
