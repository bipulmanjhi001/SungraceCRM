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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sungracecrm.Modals.Spares;
import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SparesIndentListByCRNFragment extends Fragment {
    String sComplaintId,sActionId,sCRN;
    private static final String ActionID = "aid";
    private static final String ComplaintID = "cid";
    private static final String CRN="ccrn";
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    TextView crn,sCustomerName,sCity;
    RelativeLayout back_layout;

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sId;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public SparesIndentListByCRNAdapter adapter;
    public ListView spareIndentByCRNList;


    public static SparesIndentListByCRNFragment newInstance(String AId, String CId, String CcrnNO) {
        SparesIndentListByCRNFragment fragment = new SparesIndentListByCRNFragment();
        Bundle args = new Bundle();
        args.putString(ActionID, AId);
        args.putString(ComplaintID, CId);
        args.putString(CRN, CcrnNO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sActionId= getArguments().getString(ActionID);
            sComplaintId= getArguments().getString(ComplaintID);
            sCRN= getArguments().getString(CRN);
        }
    }

    public SparesIndentListByCRNFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spares_indent_list_by_crn, container, false);
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

        spareIndentByCRNList = view.findViewById(R.id.spare_indent_byCRN_list);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        crn = view.findViewById(R.id.crn_no_heading);
        sCustomerName = view.findViewById(R.id.customer_name_heading);
        sCity = view.findViewById(R.id.customer_city_heading);
        adapter = new SparesIndentListByCRNAdapter(this,getFragmentManager());
        spareIndentByCRNList.setAdapter(adapter);
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
                    SparesIndentListFragment fragment = new SparesIndentListFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
                }
            }
        });


        new GetSpareIndentListByCRN(sActionId,sComplaintId,sId,sCRN).execute();

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
                add.cSpareName = obj.getString("spare_name");
                add.cQty = obj.getString("qty");
                add.cRemark = obj.getString("remarks");
                add.cId = obj.getString("id");
                add.cStatus = obj.getString("status");
                spares.add(add);
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class GetSpareIndentListByCRN extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject=null;
        String actionId;
        String complaintId;
        String id;
        String ccrn;

        GetSpareIndentListByCRN(String ActionID, String ComplaintID, String ID, String CCRN) {
            this.actionId=ActionID;
            this.complaintId=ComplaintID;
            this.id=ID;
            this.ccrn=CCRN;
            spares.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("action_id",""+actionId);
                data.put("complain_id",""+complaintId);
                data.put("id",""+id);
                data.put("CRN_NO",""+ccrn);
                jsonObject=handler.makeHttpRequest(AppConstant.spareIndentListByCRNnoURL, "POST", data,null);

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
                        JSONObject resultObjectList=jsonObject.optJSONObject("Result");
                        JSONArray sparesObjectList=resultObjectList.optJSONArray("SpareDetails");
                        if (!sparesObjectList.equals(null)) {

                            if (!sparesObjectList.equals(null)) {
                                get_data(sparesObjectList.toString());
                            }

                        }
                        JSONObject customerdetailsObjectList = resultObjectList.optJSONObject("CustomerDetails");
                        if (!customerdetailsObjectList.equals(null)) {
                            String CustomerName = customerdetailsObjectList.getString("customer_name");
                            String CRNno = customerdetailsObjectList.getString("CRN_NO");
                            String city = customerdetailsObjectList.getString("district");

                            sCustomerName.setText(CustomerName);
                            crn.setText(CRNno);
                            sCity.setText(city);
                        }

                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(message)
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
