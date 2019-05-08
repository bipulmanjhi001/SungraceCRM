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

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONObject;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sId;
    LinearLayout linlaHeaderProgress;
    JsonObjectHandler handler;
    TextView mAssignedComplaintNumber,mProgressComplaintNumber,mAllComplaintNumber,mCompletedComplaintNumber,selfCall_number22,spare_requested_number,spare_dispatched_number;
    LinearLayout catId1,catId2,catId3,catId11,catId22,Cat_Id222,catId111,catId2222;

    public HomeFragment() {
    }
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        mAssignedComplaintNumber=view.findViewById(R.id.assigned_complaint_number);
        mProgressComplaintNumber=view.findViewById(R.id.progress_complaint_number);
        mAllComplaintNumber=view.findViewById(R.id.all_complaint_number);
        mCompletedComplaintNumber=view.findViewById(R.id.completed_complaint_number);
        selfCall_number22=view.findViewById(R.id.selfCall_number22);
        spare_requested_number=view.findViewById(R.id.spare_requested_number);
        spare_dispatched_number=view.findViewById(R.id.spare_dispatched_number);
        catId1=view.findViewById(R.id.cat_Id1);
        catId2=view.findViewById(R.id.cat_Id2);
        catId3=view.findViewById(R.id.cat_Id3);
        catId11=view.findViewById(R.id.cat_Id11);
        catId22=view.findViewById(R.id.cat_Id22);
        Cat_Id222=view.findViewById(R.id.cat_Id222);
        catId111=view.findViewById(R.id.cat_Id111);
        catId2222=view.findViewById(R.id.cat_Id2222);

        linlaHeaderProgress =  view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        new GetTotalNumbersCountForDashboard(sId,sRole).execute();
        catId111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SparesIndentListFragment fragment = new SparesIndentListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("HomeFragment")
                        .commit();
            }
        });

        catId2222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SpareIndentDispachedListFragment fragment = new SpareIndentDispachedListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("HomeFragment")
                        .commit();
            }
        });

        catId2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AssignedJobListFragment fragment = new AssignedJobListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("HomeFragment")
                        .commit();
            }
        });

        catId3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                ProgressJobListFragment fragment = new ProgressJobListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("HomeFragment")
                        .commit();
            }
        });


        catId11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AllJobListFragment fragment = new AllJobListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("HomeFragment")
                        .commit();
            }
        });

        catId22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                CompletedJobListFragment fragment = new CompletedJobListFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                        .addToBackStack("HomeFragment")
                        .commit();
            }
        });
        Cat_Id222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SelfCallListFragment fragment = new SelfCallListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("HomeFragment")
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

                        String AllComplaints = resultObject.getString("allcomplain");
                        String TotalPendingComplaints = resultObject.getString("pending");
                        String TotalProgressComplaints = resultObject.getString("progress");
                        String TotalCompletedComplaints = resultObject.getString("completed");
                        String TotalSelfCallsComplaints = resultObject.getString("selfcall");
                        String TotalRequestedSpares = resultObject.getString("requested");
                        String TotalDispatchedSpare = resultObject.getString("dispached");

                        mAllComplaintNumber.setText(AllComplaints);
                        mAssignedComplaintNumber.setText(TotalPendingComplaints);
                        mProgressComplaintNumber.setText(TotalProgressComplaints);
                        mCompletedComplaintNumber.setText(TotalCompletedComplaints);
                        selfCall_number22.setText(TotalSelfCallsComplaints);
                        spare_requested_number.setText(TotalRequestedSpares);
                        spare_dispatched_number.setText(TotalDispatchedSpare);

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
