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

public class ComplaintReportDetailsFragment extends Fragment {

    String sReportId,sTechnicianId,sComplaintId,sId,sRole,ActionID,CRNNO;
    private static final String ReportId = "Rid";
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    TextView cCrnNo,eEngineer_observation,cCorrective_action,tTechnical_suggestion,sSpares_required,sSpares_replaced,cCustomer_name_site,cCustomer_mobileNo_present_at_site,reportId;
    ImageView cCustomer_signature,eEngineer_signature,sSnapshot1,sSnapshot2,customerPhoto;
    Button backBTN,sparesBTN,homeBTN;
    TextView date,Job_login_location_text,Job_login_location,cComplaintDate,tTechnicianName,tTechnicianNameText,crnCustomerName,ssnapshot1_text,ssnapshot2_text,ssnapshot3_text,fsrNo;
    SharedPreferences sharedPreferencesComplaintDetails,sharedPreferencesLoginDetails;

    public static ComplaintReportDetailsFragment newInstance(String Report) {
        ComplaintReportDetailsFragment fragment = new ComplaintReportDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ReportId, Report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sReportId= getArguments().getString(ReportId);
        }
    }

    public ComplaintReportDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint_report_details, container, false);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);

        sharedPreferencesComplaintDetails=getActivity().getApplicationContext().getSharedPreferences("complaintDetails", Context.MODE_PRIVATE);
        sTechnicianId=sharedPreferencesComplaintDetails.getString("TechnicianID","");
        sComplaintId=sharedPreferencesComplaintDetails.getString("ComplaintID","");

        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sId=sharedPreferencesLoginDetails.getString("Id","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");

        cCrnNo=view.findViewById(R.id.crnNo);
        date=view.findViewById(R.id.date);
        ssnapshot1_text=view.findViewById(R.id.snapshot1_text);
        ssnapshot2_text=view.findViewById(R.id.snapshot2_text);
        ssnapshot3_text=view.findViewById(R.id.snapshot3_text);

        cComplaintDate=view.findViewById(R.id.complaintDate);
        tTechnicianName=view.findViewById(R.id.tTechnicianName);
        crnCustomerName=view.findViewById(R.id.CrnCustomerName);
        tTechnicianNameText=view.findViewById(R.id.TechnicianName_text);

        eEngineer_observation=view.findViewById(R.id.engineer_observation);
        cCorrective_action=view.findViewById(R.id.corrective_action);
        tTechnical_suggestion=view.findViewById(R.id.technical_suggestion);
        sSpares_required=view.findViewById(R.id.spares_required);
        sSpares_replaced=view.findViewById(R.id.spares_replaced);
        cCustomer_name_site=view.findViewById(R.id.customer_name_site);
        cCustomer_mobileNo_present_at_site=view.findViewById(R.id.customer_mobileNo_present_at_site);
        cCustomer_signature=view.findViewById(R.id.customer_signature);
        eEngineer_signature=view.findViewById(R.id.engineer_signature);
        sSnapshot1=view.findViewById(R.id.snapshot1);
        sSnapshot2=view.findViewById(R.id.snapshot2);
        backBTN=view.findViewById(R.id.back_btn);
        sparesBTN=view.findViewById(R.id.spares_btn);
        homeBTN=view.findViewById(R.id.home_btn);
        reportId=view.findViewById(R.id.reportId);
        Job_login_location_text=view.findViewById(R.id.job_login_location_text);
        Job_login_location=view.findViewById(R.id.job_login_location);
        customerPhoto=view.findViewById(R.id.snapshot3);
        fsrNo=view.findViewById(R.id.fsrNo);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComplaintSubReportListFragment fragment=ComplaintSubReportListFragment.newInstance(sComplaintId);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .commit();

            }
        });
            sparesBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparesIndentListByCRNFragment fragment=SparesIndentListByCRNFragment.newInstance(ActionID,sComplaintId,CRNNO);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("ComplaintReportDetailsFragment")
                            .commit();
                }
            });

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                HomeFragment fragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
            }
        });

        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        new getComplaintReportDetailById(sReportId).execute();

        return view;
    }
    public class getComplaintReportDetailById extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String ReportID;

        getComplaintReportDetailById(String ReportID){
            this.ReportID=ReportID;

        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("action_id",""+ReportID);
                jsonObject = handler.makeHttpRequest(AppConstant.complaintDetailsReportByIdURL,"POST",data,null);
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

                        JSONObject actionObject=resultObject.optJSONObject("Action");
                        ReportID = actionObject.getString("action_id");
                        String JobLoginDate = actionObject.getString("job_login_date");
                        String SiteLoginLocation = actionObject.getString("site_login_loc");
                        String EngineerObservation = actionObject.getString("eng_diag_observ");
                        String CorrectiveAction = actionObject.getString("corrective_action");
                        String TechnicalSuggestion = actionObject.getString("tech_suggetion");
                        String SparesRequired = actionObject.getString("spares_req");
                        String SparesReplaced = actionObject.getString("spares_rep");
                        String CustomerName = actionObject.getString("custome_name");
                        String CustomerMobileNo = actionObject.getString("customer_mobile");
                        String CustomerSignature = actionObject.getString("customer_sig");
                        String EngineerSignature = actionObject.getString("eng_sign");
                        String ssSnapshot1 = actionObject.getString("snap1");
                        String ssSnapshot2 = actionObject.getString("snap2");
                        String CRN = actionObject.getString("CRN_NO");
                        String CustomerPhoto=actionObject.getString("customer_photo");
                        String ComplaintCustomerName=actionObject.getString("customer_name");
                        String ComplaintDate=actionObject.getString("complain_date");
                        String TechnicianName=actionObject.getString("technician_name");
                        String FSRNo=actionObject.getString("fsrno");

                        CRNNO=CRN;
                        ActionID=ReportID;
                        if (sRole.equals("Admin")){
                            Job_login_location.setText(SiteLoginLocation);
                            Job_login_location.setVisibility(View.VISIBLE);
                            Job_login_location_text.setVisibility(View.VISIBLE);
                            tTechnicianName.setText(TechnicianName);
                            tTechnicianName.setVisibility(View.VISIBLE);
                            tTechnicianNameText.setVisibility(View.VISIBLE);

                        }else {
                            Job_login_location.setVisibility(View.GONE);
                            Job_login_location_text.setVisibility(View.GONE);
                            tTechnicianName.setVisibility(View.GONE);
                            tTechnicianNameText.setVisibility(View.GONE);
                        }

                        cComplaintDate.setText(ComplaintDate);
                        crnCustomerName.setText(ComplaintCustomerName);

                        reportId.setText(ReportID);

                        eEngineer_observation.setText(EngineerObservation);
                        cCorrective_action.setText(CorrectiveAction);
                        tTechnical_suggestion.setText(TechnicalSuggestion);
                        sSpares_required.setText(SparesRequired);
                        sSpares_replaced.setText(SparesReplaced);
                        cCustomer_name_site.setText(CustomerName);
                        cCustomer_mobileNo_present_at_site.setText(CustomerMobileNo);
                        cCrnNo.setText(CRN);
                        date.setText(JobLoginDate);
                        fsrNo.setText(FSRNo);


                        if(CustomerSignature.equals(null) || CustomerSignature.equals("")){
                            cCustomer_signature.setVisibility(View.GONE);
                        }
                        else {
                            CustomerSignature=CustomerSignature.replace('~','/');
                            Picasso.with(getContext())
                                    .load(CustomerSignature)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(cCustomer_signature);
                            cCustomer_signature.setVisibility(View.VISIBLE);
                        }


                        if(EngineerSignature.equals(null) || EngineerSignature.equals("")){
                            eEngineer_signature.setVisibility(View.GONE);
                        }
                        else {
                            EngineerSignature=EngineerSignature.replace('~','/');
                            Picasso.with(getContext())
                                    .load(EngineerSignature)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(eEngineer_signature);
                            eEngineer_signature.setVisibility(View.VISIBLE);
                        }


                        if(ssSnapshot1.equals(null) || ssSnapshot1.equals("")){
                            sSnapshot1.setVisibility(View.GONE);
                            ssnapshot1_text.setVisibility(View.GONE);
                        }
                        else {
                            ssSnapshot1=ssSnapshot1.replace('~','/');
                            Picasso.with(getContext())
                                    .load(ssSnapshot1)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(sSnapshot1);
                            sSnapshot1.setVisibility(View.VISIBLE);
                        }


                        if(ssSnapshot2.equals(null) || ssSnapshot2.equals("")){
                            sSnapshot2.setVisibility(View.GONE);
                            ssnapshot2_text.setVisibility(View.GONE);
                        }
                        else {
                            ssSnapshot2=ssSnapshot2.replace('~','/');
                            Picasso.with(getContext())
                                    .load(ssSnapshot2)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(sSnapshot2);
                            sSnapshot2.setVisibility(View.VISIBLE);
                        }


                        if(CustomerPhoto.equals(null) || CustomerPhoto.equals("")){
                            customerPhoto.setVisibility(View.GONE);
                            ssnapshot3_text.setVisibility(View.GONE);
                        }
                        else {
                            CustomerPhoto=CustomerPhoto.replace('~','/');
                            Picasso.with(getContext())
                                    .load(CustomerPhoto)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(customerPhoto);
                            customerPhoto.setVisibility(View.VISIBLE);
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
