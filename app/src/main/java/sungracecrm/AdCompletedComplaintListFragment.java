package sungracecrm;

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

public class AdCompletedComplaintListFragment extends Fragment {

    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    public ArrayList<Complaints> complaints = new ArrayList<Complaints>();
    public AdCompletedComplaintListAdapter adapter;
    public ListView completedComplaintList;
    RelativeLayout back_layout;

    public AdCompletedComplaintListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_completed_complaint_list, container, false);
        completedComplaintList = view.findViewById(R.id.completedComplaint_list);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        back_layout = view.findViewById(R.id.back_layout);

        adapter = new AdCompletedComplaintListAdapter(this,getFragmentManager());
        completedComplaintList.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new GetCompletedComplaintList().execute();

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdDashboardFragment fragment = new AdDashboardFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
            }
        });

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

    public class GetCompletedComplaintList extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject=null;
        GetCompletedComplaintList() {
            complaints.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                HashMap<String,String> data=new HashMap<>();
                jsonObject=handler.makeHttpRequest(AppConstant.completedComplaintList, "GET", data,null);

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
                        JSONArray technicianObjectList=jsonObject.optJSONArray("Result");
                        if (!technicianObjectList.equals(null)) {
                            get_data(technicianObjectList.toString());
                        }
                    }
                }
            }catch(Exception e){

            }


        }


    }
}
