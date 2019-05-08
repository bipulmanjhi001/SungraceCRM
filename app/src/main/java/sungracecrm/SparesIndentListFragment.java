package sungracecrm;

import android.content.Context;
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

import sungracecrm.Modals.Spares;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SparesIndentListFragment extends Fragment {

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sId;
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public SpareIndentListAdapter adapter;
    public ListView spareIndentComplaintList;
    ImageView addlnk,listlnk;
    RelativeLayout back_layout;
    public SparesIndentListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spares_indent_list, container, false);
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

        spareIndentComplaintList = view.findViewById(R.id.spare_indent_complaint_list);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
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
        addlnk=view.findViewById(R.id.add);
        listlnk=view.findViewById(R.id.list);
        adapter = new SpareIndentListAdapter(this,getFragmentManager());
        spareIndentComplaintList.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new GetSpareIndentList(sId).execute();
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
                add.cId = obj.getString("complain_id");
                add.CRNno = obj.getString("CRN_NO");
                add.cCustomerName = obj.getString("customer_name");
                add.TechnicianID = obj.getString("technician_id");
                spares.add(add);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class GetSpareIndentList extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject=null;
        String id;
        GetSpareIndentList(String ID) {
            this.id=ID;
            spares.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("technician_id",""+id);
                jsonObject=handler.makeHttpRequest(AppConstant.getSpareIndentComplaintListURL, "POST", data,null);

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
                    if(statusObject.getBoolean("status")){
                        JSONArray resultObjectList=jsonObject.optJSONArray("Result");
                        if (!resultObjectList.equals(null)) {
                            get_data(resultObjectList.toString());
                        }
                    }
                }
            }catch(Exception e){

            }
        }
    }
}
