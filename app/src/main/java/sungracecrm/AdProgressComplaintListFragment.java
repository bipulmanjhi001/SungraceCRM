package sungracecrm;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class AdProgressComplaintListFragment extends Fragment {

    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    public ArrayList<Complaints> complaints = new ArrayList<Complaints>();
    public AdProgressComplaintListAdapter adapter;
    public ListView progressComplaintList;
    RelativeLayout back_layout;

    public AdProgressComplaintListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_progress_complaint_list, container, false);
        progressComplaintList = view.findViewById(R.id.progressComplaint_list);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        back_layout = view.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdDashboardFragment fragment = new AdDashboardFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
            }
        });

        adapter = new AdProgressComplaintListAdapter(this,getFragmentManager());
        progressComplaintList.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new GetProgressComplaintList().execute();
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

    public class GetProgressComplaintList extends AsyncTask<Void, Void, JSONObject> {
        JSONObject jsonObject=null;
        GetProgressComplaintList() {
            complaints.clear();
        }
        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                HashMap<String,String> data=new HashMap<>();
                jsonObject=handler.makeHttpRequest(AppConstant.progressComplaintList, "GET", data,null);

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
                        JSONArray technicianObjectList=jsonObject.optJSONArray("Result");
                        if (!technicianObjectList.equals(null)) {
                            get_data(technicianObjectList.toString());
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
