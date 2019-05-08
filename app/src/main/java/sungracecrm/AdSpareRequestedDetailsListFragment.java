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
import android.widget.TextView;

import sungracecrm.Modals.Spares;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AdSpareRequestedDetailsListFragment extends Fragment {

    String sComplaintId,sCRN;
    private static final String ComplaintId = "cid";
    private static final String CRN = "crn";

    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public AdSpareRequestedDetailsListAdapter adapter;
    ListView spare_request_details_list;
    RelativeLayout back_layout;
    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sId,sRole;
    TextView technicianName,crn;

    public static AdSpareRequestedDetailsListFragment newInstance(String cId, String cCRN) {
        AdSpareRequestedDetailsListFragment fragment = new AdSpareRequestedDetailsListFragment();
        Bundle args = new Bundle();
        args.putString(ComplaintId, cId);
        args.putString(CRN,cCRN);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sComplaintId= getArguments().getString(ComplaintId);
            sCRN = getArguments().getString(CRN);
        }
    }

    public AdSpareRequestedDetailsListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_spare_requested_details_list, container, false);

        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sFullName=sharedPreferencesLoginDetails.getString("Name","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sId=sharedPreferencesLoginDetails.getString("Id","");
        technicianName=view.findViewById(R.id.crn_no_heading);
        crn=view.findViewById(R.id.customer_name_heading);

        spare_request_details_list = view.findViewById(R.id.spare_requested_details);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        back_layout = view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                if (sRole.equals("Admin")){
                    AdSpareRequestedListFragment fragment = new AdSpareRequestedListFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
                }else {
                    HomeFragment fragment = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
                }
            }
        });

        adapter=new AdSpareRequestedDetailsListAdapter(this,getFragmentManager());
        spare_request_details_list.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        new GetSpareRequesteddetailsList(sComplaintId,sCRN).execute();
        return view;
    }
    public void get_data(String data){
        try {
            JSONArray data_array=new JSONArray(data);
            for (int i = 0;i<data_array.length();i++){
                JSONObject obj = new JSONObject(data_array.get(i).toString());
                Spares add = new Spares();
                add.cSpareName = obj.getString("spare_name");
                add.cQty=obj.getString("qty");
                add.cRemark=obj.getString("remarks");
                add.cDate=obj.getString("date");
                spares.add(add);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class GetSpareRequesteddetailsList extends AsyncTask<Void, Void, JSONObject> {
        JSONObject jsonObject =null;
        String complaintId;
        String ccrn;
        GetSpareRequesteddetailsList(String COMPLAINTID, String CRN){
            this.complaintId=COMPLAINTID;
            this.ccrn=CRN;

        }
        @Override
        protected JSONObject doInBackground(Void... params){
            try {
                HashMap<String,String> data= new HashMap<>();
                data.put("complain_id",""+complaintId);
                data.put("CRN_NO",""+ccrn);
                jsonObject=handler.makeHttpRequest(AppConstant.getSpareRequestedDetailsList,"POST",data,null);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if (!jsonObject.equals(null)){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONObject resultObject=jsonObject.optJSONObject("Result");
                        JSONArray spareObjectList=resultObject.optJSONArray("spares");
                        if(!spareObjectList.equals(null)){
                            get_data(spareObjectList.toString());
                        }
                        JSONObject detailsObject = resultObject.optJSONObject("details");
                        if (!detailsObject.equals(null)){
                            String CRN = detailsObject.getString("CRN_NO");
                            String TechnicianName = detailsObject.getString("technician");

                            technicianName.setText("Eng."+" "+TechnicianName);
                            crn.setText(CRN);
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

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
