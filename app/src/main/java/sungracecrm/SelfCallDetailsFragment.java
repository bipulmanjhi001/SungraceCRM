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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;


public class SelfCallDetailsFragment extends Fragment {

    String ssId,sRole,sId;
    private static final String Id = "id";
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    TextView ssvn_no,ssvn_date,productCategory,productType,complaintType,serialNo,capacityName,capacityNumber,status,technician,site_login_loc,customer_name,ssnapshot1_text,ssnapshot2_text,ssnapshot3_text;
    TextView agency_name,city,district,state,address,contact_person,mobile,email,eng_diag_obs,corrective_action,tech_suggestion,spare_required,spare_replaced,customer_feedback;
    ImageView customer_signature,engi_signature,snapshot1,snapshot2,snapshot3;
    Button backbtn,homebtn;
    SharedPreferences sharedPreferencesLoginDetails;
    TextView site_login_loc_text;

    public static SelfCallDetailsFragment newInstance(String sId) {
        SelfCallDetailsFragment fragment = new SelfCallDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Id, sId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ssId= getArguments().getString(Id);
        }
    }

    public SelfCallDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_call_details, container, false);

        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sId=sharedPreferencesLoginDetails.getString("Id","");
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);

        ssnapshot1_text=view.findViewById(R.id.snapshot1_text);
        ssnapshot2_text=view.findViewById(R.id.snapshot2_text);
        ssnapshot3_text=view.findViewById(R.id.snapshot3_text);

        ssvn_no = view.findViewById(R.id.ssvn_no);
        ssvn_date = view.findViewById(R.id.ssvn_date);
        productCategory = view.findViewById(R.id.productCategory);
        productType = view.findViewById(R.id.productType);
        complaintType = view.findViewById(R.id.complaintType);
        serialNo = view.findViewById(R.id.serialNo);
        capacityName = view.findViewById(R.id.capacityName);
        capacityNumber = view.findViewById(R.id.capacityNumber);
        status = view.findViewById(R.id.status);
        technician = view.findViewById(R.id.technician);
        site_login_loc = view.findViewById(R.id.site_login_loc);
        customer_name = view.findViewById(R.id.customer_name);
        agency_name = view.findViewById(R.id.agency_name);
        city = view.findViewById(R.id.city);
        district = view.findViewById(R.id.district);
        state = view.findViewById(R.id.state);
        address = view.findViewById(R.id.address);
        contact_person = view.findViewById(R.id.contact_person);
        mobile = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);
        eng_diag_obs = view.findViewById(R.id.eng_diag_obs);
        corrective_action = view.findViewById(R.id.corrective_action);
        tech_suggestion = view.findViewById(R.id.tech_suggestion);
        spare_required = view.findViewById(R.id.spare_required);
        spare_replaced = view.findViewById(R.id.spare_replaced);
        customer_feedback = view.findViewById(R.id.customer_feedback);
        customer_signature = view.findViewById(R.id.customer_signature);
        engi_signature = view.findViewById(R.id.engi_signature);
        snapshot1 = view.findViewById(R.id.snapshot1);
        snapshot2 = view.findViewById(R.id.snapshot2);
        snapshot3 = view.findViewById(R.id.snapshot3);
        backbtn = view.findViewById(R.id.back_btn);
        homebtn = view.findViewById(R.id.home_btn);
        site_login_loc_text = view.findViewById(R.id.site_login_loc_text);
        site_login_loc_text.setVisibility(View.GONE);
        site_login_loc.setVisibility(View.GONE);

        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new GetSelfCallReportDetails(ssId).execute();

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                if (sRole.equals("Admin")){
                    AdDashboardFragment fragment = new AdDashboardFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                            .addToBackStack("SelfCallDetailsFragment")
                            .commit();
                }else {
                    HomeFragment fragment = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                            .addToBackStack("SelfCallDetailsFragment")
                            .commit();
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SelfCallListFragment fragment = new SelfCallListFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                        .addToBackStack("SelfCallDetailsFragment")
                        .commit();
            }
        });

        return view;
    }

    public class GetSelfCallReportDetails extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String Id;

        GetSelfCallReportDetails(String ID){
            this.Id=ID;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+Id);
                jsonObject = handler.makeHttpRequest(AppConstant.GetSelfcallDetails,"POST",data,null);
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


                        String ID="id";
                                String TechName=resultObject.getString("technician");
                                String SSVN=resultObject.getString("ssvn_no");
                                String SSVNDate=resultObject.getString("ssvn_date");
                                String proCat=resultObject.getString("product_category");
                                String ProType=resultObject.getString("product_type");
                                String CapName=resultObject.getString("capacity_name");
                                String CapNumber=resultObject.getString("capacity");
                                String AgencyName=resultObject.getString("agency_name");
                                String Customername=resultObject.getString("customer_name");
                                String Address=resultObject.getString("address");
                                String City=resultObject.getString("city");
                                String District=resultObject.getString("district");
                                String State=resultObject.getString("state");
                                String ContactPerson=resultObject.getString("contact_person");
                                String Mobile=resultObject.getString("mobile");
                                String Email=resultObject.getString("email");
                                String ComplaintType=resultObject.getString("complain_type");
                                String SerialNo=resultObject.getString("serial_no");
                                String SiteLoginLocation=resultObject.getString("site_login_loc");
                                String EngDiagNosis=resultObject.getString("eng_diag_obs");
                                String CorrectiveAction=resultObject.getString("corrective_action");
                                String TechSuggestion=resultObject.getString("tech_suggestion");
                                String SpareRequired=resultObject.getString("spare_required");
                                String SpareReplaced=resultObject.getString("spare_replaced");
                                String FeedBack=resultObject.getString("customer_feedback");
                                String CustomerSignature= resultObject.getString("customer_signature");
                                String CustomerPhoto=resultObject.getString("customer_photo");
                                String EngSignature=resultObject.getString("engi_signature");
                                String snap1=resultObject.getString("sys_snap1");
                                String snap2=resultObject.getString("sys_snap2");
                                String Status=resultObject.getString("status");

                        ssvn_no.setText(SSVN);
                        ssvn_date.setText(SSVNDate);
                        productCategory.setText(":"+" "+proCat);
                        productType.setText(":"+" "+ProType);
                        complaintType.setText(":"+" "+ComplaintType);
                        serialNo.setText(":"+" "+SerialNo);
                        capacityName.setText(" "+CapName);
                        capacityNumber.setText(" "+CapNumber);
//                        status.setText(Status);
                        technician.setText(":"+" "+TechName);

                        if (sRole.equals("Admin")){
                            site_login_loc.setText(":"+" "+SiteLoginLocation);
                            site_login_loc_text.setVisibility(View.VISIBLE);
                            site_login_loc.setVisibility(View.VISIBLE);
                        }else {
                            site_login_loc_text.setVisibility(View.GONE);
                            site_login_loc.setVisibility(View.GONE);
                        }
                        site_login_loc.setText(":"+" "+SiteLoginLocation);
                        customer_name.setText(":"+" "+Customername);
                        agency_name.setText(":"+" "+AgencyName);
                        city.setText(":"+" "+City);
                        district.setText(":"+" "+District);
                        state.setText(":"+" "+State);
                        address.setText(":"+" "+Address);
                        contact_person.setText(":"+" "+ContactPerson);
                        mobile.setText(":"+" "+Mobile);
                        email.setText(":"+" "+Email);
                        eng_diag_obs.setText(":"+" "+EngDiagNosis);
                        corrective_action.setText(":"+" "+CorrectiveAction);
                        tech_suggestion.setText(":"+" "+TechSuggestion);
                        spare_required.setText(":"+" "+SpareRequired);
                        spare_replaced.setText(":"+" "+SpareReplaced);
                        customer_feedback.setText(":"+" "+FeedBack);



                        if(CustomerSignature.equals(null) || CustomerSignature.equals("")){
                            customer_signature.setVisibility(View.GONE);
                        }
                        else {
                            CustomerSignature=CustomerSignature.replace('~','/');
                            Picasso.with(getContext())
                                    .load(CustomerSignature)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(customer_signature);
                        }

                        if(EngSignature.equals(null) || EngSignature.equals("")){
                            engi_signature.setVisibility(View.GONE);
                        }
                        else {
                            EngSignature=EngSignature.replace('~','/');
                            Picasso.with(getContext())
                                    .load(EngSignature)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(engi_signature);
                        }




                        if(snap1.equals(null) || snap1.equals("")){
                            snapshot1.setVisibility(View.GONE);
                            ssnapshot1_text.setVisibility(View.GONE);
                        }
                        else {
                            snap1=snap1.replace('~','/');
                            Picasso.with(getContext())
                                    .load(snap1)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(snapshot1);
                            snapshot1.setVisibility(View.VISIBLE);
                            ssnapshot1_text.setVisibility(View.VISIBLE);
                        }

                        if(snap2.equals(null) || snap2.equals("")){
                            snapshot2.setVisibility(View.GONE);
                            ssnapshot2_text.setVisibility(View.GONE);
                        }
                        else {
                            snap2=snap2.replace('~','/');
                            Picasso.with(getContext())
                                    .load(snap2)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(snapshot2);
                            snapshot2.setVisibility(View.VISIBLE);
                            ssnapshot2_text.setVisibility(View.VISIBLE);
                        }
                        if(CustomerPhoto.equals(null) || CustomerPhoto.equals("")){
                            snapshot3.setVisibility(View.GONE);
                            ssnapshot3_text.setVisibility(View.GONE);
                        }
                        else {
                            CustomerPhoto=CustomerPhoto.replace('~','/');
                            Picasso.with(getContext())
                                    .load(CustomerPhoto)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(snapshot3);
                            snapshot3.setVisibility(View.VISIBLE);
                            ssnapshot3_text.setVisibility(View.VISIBLE);
                        }

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
