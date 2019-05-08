package sungracecrm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;
import com.squareup.picasso.Picasso;
import co.sungracecrm.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdComplaintDetailsFragment extends Fragment {

    FragmentManager fragmentManager;
    String sComplaintId;
    private static final String ComplaintId = "id";
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    TextView mProductCategory,mProductType,mComplaintType,mControllerSerialNo,mCapacityName,mCapacityNumber,mProblem_nature,mCustomerName,mCity,mDistrict,mState,cControllerSerialNo_text;
    TextView mFullAddress,mContact_person_name,mMobileNo,mAlternate_mobileNo,mEmailId,mstatus,mPostDate,mCRNNumber,mJobAssignedTechName,mJobAssigneddate,mStatus_text,mJobAssignedTo_text,mJobAssignedDate_text;
    TextView workingAreaText1,workingAreaText2,workingAreaText3,workingArea1,workingArea2,workingArea3,mWarrantyStatus,mPurchaseDate,mAgencyName;
    ImageView mSnapshot1,mSnapshot2,deleteComplaint;
    Spinner technicianspnr;
    Button jobAssignedBtn,homebtn,reAssignebtn,reportbtn;
    Integer techId;
    RelativeLayout workingAreaLayout,spinnerLayout;

    private ArrayList<Integer> technicianIdlist;
    private ArrayList<String> technicianList;
    ArrayAdapter<String> technicianNameAdapter;

    public AdComplaintDetailsFragment() {
    }
    public static AdComplaintDetailsFragment newInstance(String Id) {
        AdComplaintDetailsFragment fragment = new AdComplaintDetailsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_complaint_details, container, false);
        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        mStatus_text=view.findViewById(R.id.status_text);
        mJobAssignedTo_text=view.findViewById(R.id.jobAssignedTo_text);
        mJobAssignedDate_text=view.findViewById(R.id.jobAssignedDate_text);
        cControllerSerialNo_text=view.findViewById(R.id.controllerSerialNo_text);

        mWarrantyStatus = view.findViewById(R.id.warrantyStatus);
        mPurchaseDate = view.findViewById(R.id.purchaseDate);
        mAgencyName = view.findViewById(R.id.Agency_Govt);
        workingAreaText1 = view.findViewById(R.id.workingarea1_text);
        workingAreaText2=view.findViewById(R.id.workingarea2_text);
        workingAreaText3=view.findViewById(R.id.workingarea3_text);
        workingArea1=view.findViewById(R.id.workingarea1);
        workingArea2=view.findViewById(R.id.workingarea2);
        workingArea3=view.findViewById(R.id.workingarea3);
        workingAreaLayout=view.findViewById(R.id.WorkingArea_layout);
        reportbtn=view.findViewById(R.id.report_btn);


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
        technicianspnr=view.findViewById(R.id.technician_list_by_city_spinner);
        mJobAssignedTechName=view.findViewById(R.id.jobAssignedTo);
        mJobAssigneddate=view.findViewById(R.id.jobAssignedDate);
        deleteComplaint=view.findViewById(R.id.delete_complaint);
        jobAssignedBtn=view.findViewById(R.id.jobAssigned_btn);
        homebtn=view.findViewById(R.id.home_btn);
        reAssignebtn=view.findViewById(R.id.reAssigne_btn);
        spinnerLayout=view.findViewById(R.id.spnr);

        homebtn.setVisibility(View.GONE);
        jobAssignedBtn.setVisibility(View.GONE);
        reAssignebtn.setVisibility(View.GONE);
        reportbtn.setVisibility(View.GONE);


        handler=new JsonObjectHandler();
        new getComplaintDetailById(sComplaintId).execute();
        technicianIdlist=new ArrayList<>();
        technicianList=new ArrayList<>();
        new GetTechnicianListByStateAndDistrict().execute();

        technicianspnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                techId=technicianIdlist.get(position);
                new GetTechnicianWorkingArea(techId).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jobAssignedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostComplaintAssignedTo(techId,sComplaintId).execute();
            }
        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdDashboardFragment fragment = new AdDashboardFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                        .addToBackStack("AdComplaintDetailsFragment")
                        .commit();
            }
        });

        reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                ComplaintSubReportListFragment fragment=ComplaintSubReportListFragment.newInstance(sComplaintId);
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdComplaintDetailsFragment")
                        .commit();

            }
        });

        reAssignebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerLayout.setVisibility(View.VISIBLE);
                workingAreaLayout.setVisibility(View.VISIBLE);
                reAssignebtn.setEnabled(false);
            }
        });
        reAssignebtn.setEnabled(false);
        return view;
    }

    public class getComplaintDetailById extends AsyncTask<Void,Void,JSONObject>{
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
                        String WarrantyStatus = resultObject.getString("warranty_status");
                        String PurchaseDate = resultObject.getString("date_of_purchase");
                        String AgencyName = resultObject.getString("agency_name");

                        if (ComplaintStatus.equals("Completed")){

                            reportbtn.setVisibility(View.VISIBLE);
                            homebtn.setVisibility(View.VISIBLE);


                        }else if(ComplaintStatus.equals("Progress")) {
                            jobAssignedBtn.setVisibility(View.VISIBLE);
                            reAssignebtn.setVisibility(View.VISIBLE);
                            reportbtn.setVisibility(View.VISIBLE);
                        }else {
                            jobAssignedBtn.setVisibility(View.VISIBLE);
                            reAssignebtn.setVisibility(View.VISIBLE);
                            homebtn.setVisibility(View.VISIBLE);
                        }

                        mCRNNumber.setText(CRNNumber);
                        mProductCategory.setText(":"+" "+ProductCategory);
                        mProductType.setText(":"+" "+ProductType);
                        mComplaintType.setText(":"+" "+ComplaintType);

                        mWarrantyStatus.setText(":"+" "+WarrantyStatus);
                        mPurchaseDate.setText(":"+" "+PurchaseDate);
                        mAgencyName.setText(":"+" "+AgencyName);

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
                        mPostDate.setText(" "+ComplaintPostDate);


                        if (ComplaintStatus.equals("New")){
                            mstatus.setVisibility(View.GONE);
                            mStatus_text.setVisibility(View.GONE);

                        }else {
                            mstatus.setText(":"+" "+ComplaintStatus);
                        }

                        if (JobAssignedTechName.equals("")||JobAssignedTechName.equals(null)){
                            mJobAssignedTechName.setVisibility(View.GONE);
                            mJobAssignedTo_text.setVisibility(View.GONE);
                        }else {
                            mJobAssignedTechName.setText(":"+" "+JobAssignedTechName);
                        }
                        if (JobAssignedDate.equals("")||JobAssignedDate.equals(null)){
                            mJobAssigneddate.setVisibility(View.GONE);
                            mJobAssignedDate_text.setVisibility(View.GONE);
                        }else {
                            mJobAssigneddate.setText(":"+" "+JobAssignedDate);
                        }
                        if (ControllerSerialNo.equals("")||ControllerSerialNo.equals(null)){
                            mControllerSerialNo.setVisibility(View.GONE);
                            cControllerSerialNo_text.setVisibility(View.GONE);
                        }else {
                            mControllerSerialNo.setText(":"+" "+ControllerSerialNo);
                        }

                        if (!JobAssignedTechName.equals(null)&&!JobAssignedTechName.equals("")){
                            spinnerLayout.setVisibility(View.GONE);
                            workingAreaLayout.setVisibility(View.GONE);
                            reAssignebtn.setEnabled(true);
                        }else {
                            spinnerLayout.setVisibility(View.VISIBLE);
                            workingAreaLayout.setVisibility(View.VISIBLE);
                            reAssignebtn.setEnabled(false);
                        }


                        if(Snap1.equals(null) || Snap1.equals("")){
                            mSnapshot1.setVisibility(View.GONE);
                        }
                        else {
                            Snap1=Snap1.replace('~','/');
                            Picasso.with(getContext())
                                    .load(Snap1)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(mSnapshot1);
                            mSnapshot1.setVisibility(View.VISIBLE);
                        }

                        if(Snap2.equals(null) || Snap2.equals("")){
                            mSnapshot2.setVisibility(View.GONE);
                        }
                        else {
                            Snap2=Snap2.replace('~','/');
                            Picasso.with(getContext())
                                    .load(Snap2)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(mSnapshot2);
                            mSnapshot2.setVisibility(View.VISIBLE);
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

    public class GetTechnicianListByStateAndDistrict extends AsyncTask<Void,Void,JSONObject>{

        JSONObject jsonObject=null;

        GetTechnicianListByStateAndDistrict() {

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HashMap<String,String> data=new HashMap<>();

                jsonObject=handler.makeHttpRequest(AppConstant.technicianListByStateDistrictUrl, "GET", data,null);

            } catch (Exception e) {

            }

            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONArray resultObjectList=jsonObject.optJSONArray("Result");
                        for (int i=0;i<resultObjectList.length();i++){
                            JSONObject tempobject=resultObjectList.getJSONObject(i);
                            technicianList.add(tempobject.getString("name"));
                            technicianIdlist.add(tempobject.getInt("id"));

                            technicianNameAdapter= new ArrayAdapter<String>(getContext(),R.layout.spinneritem,technicianList);
                            technicianNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            technicianspnr.setAdapter(technicianNameAdapter);
                        }


                    }
                }

            }catch (Exception e){

            }
        }
    }

    public class PostComplaintAssignedTo extends AsyncTask<Void,Void,JSONObject>{
        JSONObject jsonObject =null;

        int technicianId;
        String complaintId;
        PostComplaintAssignedTo(int TechnicianId, String ComplaintId){
            this.complaintId=ComplaintId;
            this.technicianId=TechnicianId;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("complain_id",""+complaintId);
                data.put("technician_id",""+technicianId);
                jsonObject = handler.makeHttpRequest(AppConstant.complaintAssognedToUrl,"POST",data,null);
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

                        Toast toast=Toast.makeText(getContext(),"Complaint Assigned Successfully",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();

                        FragmentManager fragmentManager = getFragmentManager();
                        AdDashboardFragment fragment = new AdDashboardFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();

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

    public class GetTechnicianWorkingArea extends AsyncTask<Void,Void,JSONObject>{
        JSONObject jsonObject =null;
        Integer techID;

        GetTechnicianWorkingArea(Integer TechId){
            this.techID=TechId;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+techID);
                jsonObject = handler.makeHttpRequest(AppConstant.getTechnicianWorkingAreaURL,"POST",data,null);
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

                        String WorkingArea1 = resultObject.getString("workingarea1");
                        String WorkingArea2 = resultObject.getString("workingarea2");
                        String WorkingArea3 = resultObject.getString("workingarea3");


                        if (WorkingArea1.equals("")||WorkingArea1.equals(null)){
                            workingAreaText1.setVisibility(View.GONE);
                            workingArea1.setVisibility(View.GONE);

                        }else {
                            workingArea1.setText(WorkingArea1);
                            workingAreaText1.setVisibility(View.VISIBLE);
                            workingArea1.setVisibility(View.VISIBLE);

                        }
                        if (WorkingArea2.equals("")||WorkingArea2.equals(null)){
                            workingAreaText2.setVisibility(View.GONE);
                            workingArea2.setVisibility(View.GONE);
                        }else {
                            workingArea2.setText(WorkingArea2);
                            workingAreaText2.setVisibility(View.VISIBLE);
                            workingArea2.setVisibility(View.VISIBLE);
                        }
                        if (WorkingArea3.equals("")||WorkingArea3.equals(null)){
                            workingAreaText3.setVisibility(View.GONE);
                            workingArea3.setVisibility(View.GONE);
                        }else {
                            workingArea3.setText(WorkingArea3);
                            workingAreaText3.setVisibility(View.VISIBLE);
                            workingArea3.setVisibility(View.VISIBLE);
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
