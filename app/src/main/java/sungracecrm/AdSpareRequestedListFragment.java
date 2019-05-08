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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdSpareRequestedListFragment extends Fragment {

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sId,sRole;
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public AdSpareRequestedListAdapter adapter;
    ListView spareRequestListView;
    RelativeLayout back_layout;

    public AdSpareRequestedListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_spare_requested_list, container, false);

        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sFullName=sharedPreferencesLoginDetails.getString("Name","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sId=sharedPreferencesLoginDetails.getString("Id","");

        spareRequestListView = view.findViewById(R.id.spare_request_list);
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

        adapter=new AdSpareRequestedListAdapter(this,getFragmentManager());
        spareRequestListView.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        new GetSpareRequestedList(sId,sRole).execute();
        return view;
    }

    public void get_data(String data){
        try {
            JSONArray data_array=new JSONArray(data);
            for (int i = 0;i<data_array.length();i++){
                JSONObject obj = new JSONObject(data_array.get(i).toString());
                Spares add = new Spares();
                add.cId = obj.getString("complain_id");
                add.CRNno=obj.getString("CRN_NO");
                add.cCustomerName=obj.getString("customer_name");
                add.cTechnicianName=obj.getString("technician");
                spares.add(add);
            }
            adapter.notifyDataSetChanged();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class GetSpareRequestedList extends AsyncTask<Void, Void, JSONObject>{
        JSONObject jsonObject =null;
        String userId;
        String role;
        GetSpareRequestedList(String USERID, String ROLE){
            this.userId=USERID;
            this.role=ROLE;
            spares.clear();
        }
        @Override
        protected JSONObject doInBackground(Void... params){
            try {
                HashMap<String,String> data= new HashMap<>();
                data.put("userid",""+userId);
                data.put("role",""+role);
                jsonObject=handler.makeHttpRequest(AppConstant.getSpareRequestedList,"POST",data,null);
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
                        JSONArray resultObjectList=jsonObject.optJSONArray("Result");
                        if(!resultObjectList.equals(null)){
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

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
