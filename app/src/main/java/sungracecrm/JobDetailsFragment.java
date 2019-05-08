package sungracecrm;

import android.app.AlertDialog;
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

import static android.content.Context.MODE_PRIVATE;


public class JobDetailsFragment extends Fragment {
    String sComplaintId,CrnNO;
    private static final String ComplaintId = "id";
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    TextView mProductCategory,mProductType,mComplaintType,mControllerSerialNo,mCapacityName,mCapacityNumber,mProblem_nature,mCustomerName,mCity,mDistrict,mState;
    TextView mFullAddress,mContact_person_name,mMobileNo,mAlternate_mobileNo,mEmailId,mstatus,mPostDate,mCRNNumber,mJobAssignedTechName,mJobAssigneddate;
    TextView mWarrantyStatus,mPurchaseDate,mAgencyName;
    ImageView mSnapshot1,mSnapshot2,deleteComplaint;
    Button backBTN,actionBTN;
    private SharedPreferences sharedPreferencesComplaintDetails;
    public JobDetailsFragment() {
    }

    public static JobDetailsFragment newInstance(String Id) {
        JobDetailsFragment fragment = new JobDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ComplaintId, Id);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_details, container, false);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        mProductCategory=view.findViewById(R.id.productCategory);
        mProductType=view.findViewById(R.id.productType);
        mComplaintType=view.findViewById(R.id.complaintType);
        mControllerSerialNo=view.findViewById(R.id.controllerSerialNo);
        mCapacityName=view.findViewById(R.id.capacityName);
        mCapacityNumber=view.findViewById(R.id.capacityNumber);
        mProblem_nature=view.findViewById(R.id.problem_nature);
        mCustomerName=view.findViewById(R.id.customerName);
        mCity=view.findViewById(R.id.city);
        mDistrict=view.findViewById(R.id.district);
        mState=view.findViewById(R.id.state);
        mFullAddress=view.findViewById(R.id.fullAddress);
        mContact_person_name=view.findViewById(R.id.contact_person_name);
        mMobileNo=view.findViewById(R.id.mobileNo);
        mAlternate_mobileNo=view.findViewById(R.id.alternate_mobileNo);
        mEmailId=view.findViewById(R.id.emailId);
        mSnapshot1=view.findViewById(R.id.snapshot1);
        mSnapshot2=view.findViewById(R.id.snapshot2);
        mstatus=view.findViewById(R.id.status);
        mPostDate=view.findViewById(R.id.postDate);
        mCRNNumber=view.findViewById(R.id.crnNumber);
        mJobAssignedTechName=view.findViewById(R.id.jobAssignedTo);
        mJobAssigneddate=view.findViewById(R.id.jobAssignedDate);
        deleteComplaint=view.findViewById(R.id.delete_complaint);
        backBTN=view.findViewById(R.id.back_btn);
        actionBTN=view.findViewById(R.id.action_btn);

        mWarrantyStatus = view.findViewById(R.id.warrantyStatus);
        mPurchaseDate = view.findViewById(R.id.purchaseDate);
        mAgencyName = view.findViewById(R.id.Agency_Govt);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                HomeFragment fragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                        .addToBackStack("JobDetailsFragment")
                        .commit();
            }
        });
        actionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TechnicianActionFragment fragment=TechnicianActionFragment.newInstance(CrnNO,sComplaintId);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.frame_container,fragment)
                                .disallowAddToBackStack()
                                .commit();

            }
        });
        handler = new JsonObjectHandler();
        new getComplaintDetailById(sComplaintId).execute();

        return view;
    }

    public class getComplaintDetailById extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String complaintId;

        getComplaintDetailById(String ComplaintId){
            this.complaintId=ComplaintId;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+complaintId);
                jsonObject = handler.makeHttpRequest(AppConstant.complaintDetailsById,"POST",data,null);
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

                        String CapacityName = resultObject.getString("capacityName");
                        String CapacityNumber = resultObject.getString("capacityNo");
                        String CustomerName = resultObject.getString("customerName");
                        String FullAddress = resultObject.getString("address");
                        String City = resultObject.getString("village");
                        String District = resultObject.getString("district");
                        String State = resultObject.getString("state");
                        String ProductCategory = resultObject.getString("productCategory");
                        String ProductType = resultObject.getString("productType");
                        String ComplaintPostDate = resultObject.getString("date");
                        String CRNNumber = resultObject.getString("complain_sno");
                        String ContactPersonName = resultObject.getString("contactPerson");
                        String MobileNo = resultObject.getString("mobile");
                        String AlternateMobileNo = resultObject.getString("amobile");
                        String EmailId = resultObject.getString("email");
                        String ComplaintType = resultObject.getString("complain_type");
                        String ControllerSerialNo = resultObject.getString("controller_sno");
                        String NatureOfProblem = resultObject.getString("nature_of_p");
                        String Snap1 = resultObject.getString("productsnap1");
                        String Snap2 = resultObject.getString("productsnap2");
                        String ComplaintStatus = resultObject.getString("status");
                        String JobAssignedTechName = resultObject.getString("technician");
                        String JobAssignedDate = resultObject.getString("assignDate");
                        String ComplaintID = resultObject.getString("complain_id");
                        String WarrantyStatus = resultObject.getString("warranty_status");
                        String PurchaseDate = resultObject.getString("date_of_purchase");
                        String AgencyName = resultObject.getString("agency_name");

                        if (ComplaintStatus.equals("Completed")){

                            actionBTN.setVisibility(View.GONE);
                        }

                        mWarrantyStatus.setText(":"+" "+WarrantyStatus);
                        mPurchaseDate.setText(":"+" "+PurchaseDate);
                        mAgencyName.setText(":"+" "+AgencyName);

                        CrnNO =CRNNumber;
                        mProductCategory.setText(":"+" "+ProductCategory);
                        mProductType.setText(":"+" "+ProductType);
                        mComplaintType.setText(":"+" "+ComplaintType);
                        mControllerSerialNo.setText(":"+" "+ControllerSerialNo);
                        mCapacityName.setText(CapacityName);
                        mCapacityNumber.setText(CapacityNumber);
                        mProblem_nature.setText(":"+" "+NatureOfProblem);
                        mCustomerName.setText(":"+" "+CustomerName);
                        mCity.setText(":"+" "+City);
                        mDistrict.setText(":"+" "+District);
                        mState.setText(":"+" "+State);
                        mFullAddress.setText(":"+" "+FullAddress);
                        mContact_person_name.setText(":"+" "+ContactPersonName);
                        mMobileNo.setText(":"+" "+MobileNo);
                        mAlternate_mobileNo.setText(":"+" "+AlternateMobileNo);
                        mEmailId.setText(":"+" "+EmailId);

                        mstatus.setText(":"+" "+ComplaintStatus);

                        mPostDate.setText(ComplaintPostDate);
                        mCRNNumber.setText(CrnNO);
                        mJobAssignedTechName.setText(":"+" "+JobAssignedTechName);
                        mJobAssigneddate.setText(":"+" "+JobAssignedDate);

                        if(!Snap1.equals(null) || !Snap1.equals("")){
                            Snap1=Snap1.replace('~','/');
                            Picasso.with(getContext())
                                    .load(Snap1)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(mSnapshot1);
                        }
                        else {
                            mSnapshot1.setVisibility(View.GONE);
                        }

                        if(!Snap2.equals(null) || !Snap2.equals("")){
                            Snap2=Snap2.replace('~','/');
                            Picasso.with(getContext())
                                    .load(Snap2)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(mSnapshot2);
                        }
                        else {
                            mSnapshot2.setVisibility(View.GONE);
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
    public class GetChangeStatusToPending extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String complaintId;
        GetChangeStatusToPending(String ComplaintId){
            this.complaintId=ComplaintId;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+complaintId);
                jsonObject = handler.makeHttpRequest(AppConstant.changeStatusToPendingByTech,"POST",data,null);
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

                        String TechnicianID = resultObject.getString("technician_id");
                        String ComplaintID = resultObject.getString("complain_id");
                        String CRN_No = resultObject.getString("CRN_NO");

                        sharedPreferencesComplaintDetails=getActivity().getSharedPreferences("complaintDetails",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferencesComplaintDetails.edit();
                        editor.putString("TechnicianID", TechnicianID);
                        editor.putString("ComplaintID", ComplaintID);
                        editor.putString("CRN_No", CRN_No);
                        editor.commit();
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
