package sungracecrm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.sungracecrm.R;


import org.json.JSONObject;

import java.util.HashMap;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

public class AdDashboardFragment extends Fragment {

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sId;
    LinearLayout linlaHeaderProgress;
    JsonObjectHandler handler;
    TextView mNewComplaintNumber,mPendingComplaintNumber,mProgressComplaintNumber,mDispatchedSpareNumber,mCompletedComplaintNumber;
    TextView mTotalTechnicianNumber,mSelfcallNumbers,mSpareRequested_number;
    LinearLayout catId1,catId2,catId3,catId11,catId22,catId222,catId111,catId1111;

    public AdDashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_ad_dashboard, container, false);

        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sId=sharedPreferencesLoginDetails.getString("Id","");
        sFullName=sharedPreferencesLoginDetails.getString("Name","");
        sMobileNo=sharedPreferencesLoginDetails.getString("MobileNo","");
        sEmailId=sharedPreferencesLoginDetails.getString("EmailId","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sAccessToken=sharedPreferencesLoginDetails.getString("AccessToken","");
        sProfilePic=sharedPreferencesLoginDetails.getString("ProfilePic","");
        sPassword=sharedPreferencesLoginDetails.getString("pass","");
        sUserName=sharedPreferencesLoginDetails.getString("username","");

        mNewComplaintNumber=view.findViewById(R.id.new_complaint_number);
        mPendingComplaintNumber=view.findViewById(R.id.pending_complaint_number);
        mProgressComplaintNumber=view.findViewById(R.id.progress_complaint_number);
        mDispatchedSpareNumber=view.findViewById(R.id.spare_dispatched_number);
        mCompletedComplaintNumber=view.findViewById(R.id.completed_complaint_number);
        mTotalTechnicianNumber=view.findViewById(R.id.total_technician_number);
        mSelfcallNumbers=view.findViewById(R.id.total_selfCalls_number);
        mSpareRequested_number=view.findViewById(R.id.spareRequested_number);
        catId1=view.findViewById(R.id.cat_Id1);
        catId2=view.findViewById(R.id.cat_Id2);
        catId3=view.findViewById(R.id.cat_Id3);
        catId11=view.findViewById(R.id.cat_Id11);
        catId22=view.findViewById(R.id.cat_Id22);
        catId111=view.findViewById(R.id.cat_Id111);
        catId1111=view.findViewById(R.id.cat_Id1111);
        catId222=view.findViewById(R.id.cat_Id222);

        linlaHeaderProgress =  view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new GetTotalNumbersCountForDashboard(sId,sRole).execute();

        catId1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdNewComplaintListFragment fragment = new AdNewComplaintListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });

        catId2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdPendingComplaintListFragment fragment = new AdPendingComplaintListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });

        catId3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdProgressComplaintListFragment fragment = new AdProgressComplaintListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });


        catId22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdCompletedComplaintListFragment fragment = new AdCompletedComplaintListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });

        catId111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdTechnicianListFragment fragment = new AdTechnicianListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });
        catId1111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SelfCallListFragment fragment = new SelfCallListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });
        catId222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdSpareRequestedListFragment fragment = new AdSpareRequestedListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });
        catId11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SpareIndentDispachedListFragment fragment = new SpareIndentDispachedListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdDashboardFragment")
                        .commit();
            }
        });

        return view;
    }

    public class GetTotalNumbersCountForDashboard extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String id;
        String role;

        GetTotalNumbersCountForDashboard(String ID, String Role){
            this.id=ID;
            this.role=Role;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+id);
                data.put("role",""+role);
                jsonObject = handler.makeHttpRequest(AppConstant.totalNumberOfComplaintCountUrl,"POST",data,null);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return jsonObject;
        }
        @Override
        protected void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONObject resultObject=jsonObject.optJSONObject("Result");

                        String TotalTechnicians = resultObject.getString("technician");
                        String DispatchedSpares = resultObject.getString("dispached");
                        String TotalNewComplaints = resultObject.getString("newcomplain");
                        String TotalPendingComplaints = resultObject.getString("pending");
                        String TotalProgressComplaints = resultObject.getString("progress");
                        String TotalCompletedComplaints = resultObject.getString("completed");
                        String TotalSpareRequested = resultObject.getString("requested");
                        String TotalSelfcalls = resultObject.getString("selfcall");

                        mTotalTechnicianNumber.setText(TotalTechnicians);
                        mDispatchedSpareNumber.setText(DispatchedSpares);
                        mNewComplaintNumber.setText(TotalNewComplaints);
                        mPendingComplaintNumber.setText(TotalPendingComplaints);
                        mProgressComplaintNumber.setText(TotalProgressComplaints);
                        mCompletedComplaintNumber.setText(TotalCompletedComplaints);
                        mSpareRequested_number.setText(TotalSpareRequested);
                        mSelfcallNumbers.setText(TotalSelfcalls);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
