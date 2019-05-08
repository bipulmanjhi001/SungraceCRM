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

import sungracecrm.Modals.Spares;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ComplaintSubReportListFragment extends Fragment {
    String sComplaintId,sRole;
    private static final String ComplaintId = "Cid";
    JsonObjectHandler handler;
    SharedPreferences sharedPreferencesComplaintDetails,sharedPreferencesLoginDetails;
    LinearLayout linlaHeaderProgress;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public ComplaintSubReportListAdapter adapter;
    public ListView reportSubListView;
    RelativeLayout back_layout;

    public static ComplaintSubReportListFragment newInstance(String CompId) {
        ComplaintSubReportListFragment fragment = new ComplaintSubReportListFragment();
        Bundle args = new Bundle();
        args.putString(ComplaintId, CompId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sComplaintId= getArguments().getString(ComplaintId);
        }
    }

    public ComplaintSubReportListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint_sub_report_list, container, false);
        sharedPreferencesComplaintDetails=getActivity().getSharedPreferences("complaintDetails",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferencesComplaintDetails.edit();
        editor.putString("ComplaintID", sComplaintId);
        editor.commit();
        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sRole=sharedPreferencesLoginDetails.getString("Role","");

        reportSubListView = view.findViewById(R.id.report_sub_list);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);

        adapter = new ComplaintSubReportListAdapter(this,getFragmentManager());
        reportSubListView.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        back_layout = view.findViewById(R.id.back_layout);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            if (sRole.equals("Admin")){
                AdDashboardFragment fragment = new AdDashboardFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
            }else {
                HomeFragment fragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
            }

            }
        });

        new GetComplaintSubReportList(sComplaintId).execute();
        return view;
    }
    public void get_data(String data)
    {
        try {
            JSONArray data_array=new JSONArray(data);

            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());

                Spares add=new Spares();
                add.cDate = obj.getString("job_login_date");
                add.cCustomerName = obj.getString("custome_name");
                add.cId = obj.getString("action_id");
                spares.add(add);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class GetComplaintSubReportList extends AsyncTask<Void, Void, JSONObject> {
        JSONObject jsonObject=null;
        String id;
        GetComplaintSubReportList(String ID) {
            this.id=ID;
            spares.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("complain_id",""+id);
                jsonObject=handler.makeHttpRequest(AppConstant.getComplaintSubReportListURL, "POST", data,null);

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
