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


public class SpareIndentDispachedListFragment extends Fragment {

    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sId;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public SpareIndentDispatchedListAdapter adapter;
    public ListView spareIndentDispatchedListView;
    RelativeLayout back_layout;

    public SpareIndentDispachedListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spare_indent_dispached_list, container, false);

        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sId=sharedPreferencesLoginDetails.getString("Id","");
        spareIndentDispatchedListView = view.findViewById(R.id.spare_indent_dispatched_list_view);
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

        adapter = new SpareIndentDispatchedListAdapter(this,getFragmentManager());
        spareIndentDispatchedListView.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        new GetSpareIndentListByCRN(sId,sRole).execute();

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
                add.CRNno= obj.getString("CRN_NO");
                spares.add(add);
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class GetSpareIndentListByCRN extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject=null;
        String id;
        String role;

        GetSpareIndentListByCRN(String Id, String Role) {
            this.id=Id;
            this.role=Role;
            spares.clear();
        }
        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+id);
                data.put("role",""+role);
                jsonObject=handler.makeHttpRequest(AppConstant.GetSpareDispatchedLidtURL, "POST", data,null);

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
                }
            }catch(Exception e){

            }


        }


    }
}
