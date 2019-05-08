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
import android.widget.ListView;
import android.widget.RelativeLayout;

import sungracecrm.Modals.Complaints;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CompletedJobListFragment extends Fragment {

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sId;
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    public ArrayList<Complaints> complaints = new ArrayList<Complaints>();
    public CompletedJobListAdapter adapter;
    public ListView completedJobList;
    RelativeLayout back_layout;

    public CompletedJobListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_job_list, container, false);
        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sFullName=sharedPreferencesLoginDetails.getString("Name","");
        sMobileNo=sharedPreferencesLoginDetails.getString("MobileNo","");
        sEmailId=sharedPreferencesLoginDetails.getString("EmailId","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sAccessToken=sharedPreferencesLoginDetails.getString("AccessToken","");
        sProfilePic=sharedPreferencesLoginDetails.getString("ProfilePic","");
        sPassword=sharedPreferencesLoginDetails.getString("pass","");
        sUserName=sharedPreferencesLoginDetails.getString("username","");
        sId=sharedPreferencesLoginDetails.getString("Id","");
        completedJobList = view.findViewById(R.id.completed_job_list);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        back_layout = view.findViewById(R.id.back_layout);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                    HomeFragment fragment = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();

            }
        });

        adapter = new CompletedJobListAdapter(this,getFragmentManager());
        completedJobList.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new GetCompletedJobList(sId).execute();
        return view;
    }

    public void get_data(String data)
    {
        try {
            JSONArray data_array=new JSONArray(data);

            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());
                Complaints add=new Complaints();
                add.Id = obj.getString("id");
                add.ProductCategory = obj.getString("product_category");
                add.CustomerName = obj.getString("customer_name");
                add.mCity = obj.getString("village");
                add.mDistrict = obj.getString("district");
                complaints.add(add);
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class GetCompletedJobList extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject=null;
        String id;
        GetCompletedJobList(String ID) {
            this.id=ID;
            complaints.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+id);
                jsonObject=handler.makeHttpRequest(AppConstant.completedJobListByTechIdUrl, "POST", data,null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(final JSONObject jsonobject) {
            linlaHeaderProgress.setVisibility(View.GONE);
            try
            {
                if(jsonObject!=null) {
                    JSONObject statusObject = jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if(statusObject.getBoolean("status")){
                        JSONArray resultObjectList=jsonObject.optJSONArray("Result");
                        if (!resultObjectList.equals(null)) {
                            get_data(resultObjectList.toString());
                        }
                    }else {
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
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Something went wrong try later")
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
            }catch(Exception e){

            }


        }


    }
}
