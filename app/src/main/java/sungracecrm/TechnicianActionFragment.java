package sungracecrm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import sungracecrm.Modals.Spares;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class TechnicianActionFragment extends Fragment implements LocationListener {

    private static final String CrnID = "crnid";
    private static final String ComplaintId = "Cid";
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    private SharedPreferences sharedPreferencesComplaintDetails,sharedPreferencesLoginDetails;
    String sTechnicianId,ssComplaintId,sCRNNo,sssCRNNo,sComplaintId,SpResult;

    SharedPreferences sharedPreferencesAction;
    SignaturePad customerSignaturePad,technicianSignaturePad;
    Button saveButtonCustomer, clearButtonCustomer,saveButtonTechnician, clearButtonTechnician,Snapshot_11,Snapshot_22,Snapshot_33,sSubmit_btn;
    Bitmap custSign,techSign;
    String path,path2;
    String base64strCustSign,base64strTechSign;

    LocationManager locationManager;
    TextView crnNo,siteLoginLocation,reportid,sp1,sp2,sp3;
    EditText mDiagnosis,mCorrective_action,mTechnical_suggestion,mSpare_required,mSpare_replaced,mCustomerName_present,mMobileNo_present;
    Button submitBTN,sparesBTN,completedBTN;

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST2 = 1889;
    private static final int CAMERA_REQUEST3 = 1887;
    ImageView snapshot1,snapshot2,snapshot3;
    Bitmap snapPic1,snapPic2,snapPic3;
    String base64str,base64str2,base64str3;
    EditText spareName,qQty,rRemark;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public TechnicianActionTempSpareListAdapter adapter;
    public ListView spareListView;

    public static TechnicianActionFragment newInstance(String CRNid,String CompId) {
        TechnicianActionFragment fragment = new TechnicianActionFragment();
        Bundle args = new Bundle();
        args.putString(CrnID, CRNid);
        args.putString(ComplaintId, CompId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sssCRNNo= getArguments().getString(CrnID);
            ssComplaintId= getArguments().getString(ComplaintId);


            sharedPreferencesComplaintDetails=getActivity().getSharedPreferences("complaintDetails",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferencesComplaintDetails.edit();
            editor.putString("CRN_No", sssCRNNo);
            editor.putString("sComplaintId", ssComplaintId);
            editor.commit();
        }
    }
    public TechnicianActionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_technician_action, container, false);

        linlaHeaderProgress =  view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);
        sharedPreferencesLoginDetails=getActivity().getApplicationContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sTechnicianId=sharedPreferencesLoginDetails.getString("Id","");
        sharedPreferencesComplaintDetails=getActivity().getApplicationContext().getSharedPreferences("complaintDetails", Context.MODE_PRIVATE);
        sCRNNo=sharedPreferencesComplaintDetails.getString("CRN_No","");
        sComplaintId=sharedPreferencesComplaintDetails.getString("sComplaintId","");

        sharedPreferencesAction=getActivity().getApplicationContext().getSharedPreferences("action", Context.MODE_PRIVATE);
        SpResult=sharedPreferencesAction.getString("spareList","");

        if (SpResult.equals("OK")){
            new tempSpareList(sTechnicianId,sComplaintId).execute();
        }
        reportid=view.findViewById(R.id.report_Id);
        crnNo=view.findViewById(R.id.crn_Id);
        mDiagnosis=view.findViewById(R.id.diagnosis);
        mCorrective_action=view.findViewById(R.id.corrective_action);
        mTechnical_suggestion=view.findViewById(R.id.technical_suggestion);
        mSpare_required=view.findViewById(R.id.spare_required);
        mSpare_replaced=view.findViewById(R.id.spare_replaced);
        mCustomerName_present=view.findViewById(R.id.customerName_present);
        mMobileNo_present=view.findViewById(R.id.mobileNo_present);
        customerSignaturePad=view.findViewById(R.id.customer_signaturePad);
        technicianSignaturePad=view.findViewById(R.id.technician_signaturePad);
        sp1=view.findViewById(R.id.snapshot_1_title);
        sp2=view.findViewById(R.id.snapshot_2_title);
        sp3=view.findViewById(R.id.snapshot_3_title);

        snapshot1=view.findViewById(R.id.snapshot_1);
        snapshot2=view.findViewById(R.id.snapshot_2);
        snapshot3=view.findViewById(R.id.snapshot_3);
        Snapshot_11=view.findViewById(R.id.snapshot_11);
        Snapshot_22=view.findViewById(R.id.snapshot_22);
        Snapshot_33=view.findViewById(R.id.snapshot_33);

        submitBTN=view.findViewById(R.id.submit_btn);
        sparesBTN=view.findViewById(R.id.spares_btn);
        completedBTN=view.findViewById(R.id.completed_btn);
        siteLoginLocation=view.findViewById(R.id.SiteLoginLocation);

        crnNo.setText(sCRNNo);

        saveButtonCustomer = view.findViewById(R.id.saveButtonCust);
        clearButtonCustomer = view.findViewById(R.id.clearButtonCust);
        saveButtonTechnician = view.findViewById(R.id.saveButtonTech);
        clearButtonTechnician = view.findViewById(R.id.clearButtonTech);
        spareListView = view.findViewById(R.id.spare_temp_list);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        getLocation();
        customerSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

                saveButtonCustomer.setEnabled(true);
                clearButtonCustomer.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButtonCustomer.setEnabled(false);
                clearButtonCustomer.setEnabled(false);
            }
        });
        saveButtonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerSignaturePad.setEnabled(false);
                custSign = customerSignaturePad.getSignatureBitmap();
                path = saveCustomerSignatureImage(custSign);
                base64strCustSign = saveCustomerSignatureImage(custSign);
                Toast toast=Toast.makeText(getContext(),"Signature Saved",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                View view=toast.getView();
                TextView  view1=(TextView)view.findViewById(android.R.id.message);
                view1.setTextColor(Color.WHITE);
                view1.setTextSize(18);
                view1.setPadding(10,5,10,5);
                view.setBackgroundResource(R.color.colorAccent);
                toast.show();

            }
        });

        clearButtonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerSignaturePad.clear();
                customerSignaturePad.setEnabled(true);
            }
        });

        technicianSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

                saveButtonTechnician.setEnabled(true);
                clearButtonTechnician.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveButtonTechnician.setEnabled(false);
                clearButtonTechnician.setEnabled(false);
            }
        });
        saveButtonTechnician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                technicianSignaturePad.setEnabled(false);
                techSign = technicianSignaturePad.getSignatureBitmap();
                path2 = saveTechnicianSignatureImage(techSign);
                base64strTechSign = saveCustomerSignatureImage(techSign);
                Toast toast=Toast.makeText(getContext(),"Signature Saved",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                View view=toast.getView();
                TextView  view1=(TextView)view.findViewById(android.R.id.message);
                view1.setTextColor(Color.WHITE);
                view1.setTextSize(18);
                view1.setPadding(10,5,10,5);
                view.setBackgroundResource(R.color.colorAccent);
                toast.show();
            }
        });

        clearButtonTechnician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                technicianSignaturePad.clear();
                technicianSignaturePad.setEnabled(true);
            }
        });
        Snapshot_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        Snapshot_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent2, CAMERA_REQUEST2);
            }
        });
        Snapshot_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent3, CAMERA_REQUEST3);
            }
        });
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( mCustomerName_present.getText().toString().length() == 0 ){
                    mCustomerName_present.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Customer Name is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }else if (mMobileNo_present.getText().toString().length() == 0 ){
                    mMobileNo_present.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Mobile Number is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }else {

                    String ComplaintId = sComplaintId;
                    String TechnicianId = sTechnicianId;
                    String SiteLoginLocation = siteLoginLocation.getText().toString();
                    String TechnicianDiagnosis = mDiagnosis.getText().toString();
                    String CorrectiveAction = mCorrective_action.getText().toString();
                    String TechnicianSuggestion = mTechnical_suggestion.getText().toString();
                    String SpareRequired = mSpare_required.getText().toString();
                    String SpareReplace = mSpare_replaced.getText().toString();
                    String CustomerNamePresent = mCustomerName_present.getText().toString();
                    String CustomerMobilePresent = mMobileNo_present.getText().toString();
                    String CustomerSignature = base64strCustSign;
                    String TechnicianSignature = base64strTechSign;
                    String Snap1 = base64str;
                    String Snap2 = base64str2;
                    String Snap3 = base64str3;
                    String Status = "Progress";
                    submitBTN.setVisibility(View.GONE);
                    completedBTN.setVisibility(View.GONE);
                    sparesBTN.setVisibility(View.GONE);
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    handler = new JsonObjectHandler();
                    new PostTechActionProgress(ComplaintId, TechnicianId, SiteLoginLocation, TechnicianDiagnosis, CorrectiveAction, TechnicianSuggestion, SpareRequired, SpareReplace, CustomerNamePresent, CustomerMobilePresent, CustomerSignature, TechnicianSignature, Snap1, Snap2, Snap3, Status).execute();

                }
            }
        });

        completedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCustomerName_present.getText().toString().length() == 0) {
                    mCustomerName_present.setError("Field is required!");
                    Toast toast=Toast.makeText(getContext(),"Customer Name is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                } else if (mMobileNo_present.getText().toString().length() == 0) {
                    mMobileNo_present.setError("Field is required!");
                    Toast toast=Toast.makeText(getContext(),"Mobile Number is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();

                } else {
                    String ComplaintId = sComplaintId;
                    String TechnicianId = sTechnicianId;
                    String SiteLoginLocation = siteLoginLocation.getText().toString();
                    String TechnicianDiagnosis = mDiagnosis.getText().toString();
                    String CorrectiveAction = mCorrective_action.getText().toString();
                    String TechnicianSuggestion = mTechnical_suggestion.getText().toString();
                    String SpareRequired = mSpare_required.getText().toString();
                    String SpareReplace = mSpare_replaced.getText().toString();
                    String CustomerNamePresent = mCustomerName_present.getText().toString();
                    String CustomerMobilePresent = mMobileNo_present.getText().toString();
                    String CustomerSignature = base64strCustSign;
                    String TechnicianSignature = base64strTechSign;
                    String Snap1 = base64str;
                    String Snap2 = base64str2;
                    String Snap3 = base64str3;
                    String Status = "Completed";

                    submitBTN.setVisibility(View.GONE);
                    completedBTN.setVisibility(View.GONE);
                    sparesBTN.setVisibility(View.GONE);
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    handler = new JsonObjectHandler();
                    new PostTechActionProgress(ComplaintId, TechnicianId, SiteLoginLocation, TechnicianDiagnosis, CorrectiveAction, TechnicianSuggestion, SpareRequired, SpareReplace, CustomerNamePresent, CustomerMobilePresent, CustomerSignature, TechnicianSignature, Snap1, Snap2, Snap3, Status).execute();
                }
            }
        });

        sparesBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.spare_indent_add_popup);
                dialog.setTitle("Title...");

                spareName =  dialog.findViewById(R.id.spare_name);
                qQty =  dialog.findViewById(R.id.qty);
                rRemark =  dialog.findViewById(R.id.remark);

                Button sSubmit_btn = (Button) dialog.findViewById(R.id.submit_btn);
                sSubmit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ComplaintID=sComplaintId;
                        String TechnicianID=sTechnicianId;
                        String SpareName=spareName.getText().toString();
                        String Qty=qQty.getText().toString();
                        String Remark=rRemark.getText().toString();
                        completedBTN.setEnabled(false);
                        handler=new JsonObjectHandler();
                        new tempSpareList(sTechnicianId,sComplaintId).execute();
                        new PostSpareIndent(ComplaintID,TechnicianID,SpareName,Qty,Remark).execute();

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        adapter = new TechnicianActionTempSpareListAdapter(this,getFragmentManager());
        spareListView.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        return view;
    }
    void getLocation() {
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        linlaHeaderProgress.setVisibility(View.GONE);
        siteLoginLocation.setText(" Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude()+",");
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            siteLoginLocation.setText(siteLoginLocation.getText() + "\n"+addresses.get(0).getAddressLine(0));
        }catch(Exception e)
        {

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Enable GPS and Internet")
                .setTitle("Location Permission")
                .setIcon(R.drawable.ic_location)
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert =builder.create();
        alert.show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    public static String saveCustomerSignatureImage(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static String saveTechnicianSignatureImage(Bitmap bm2){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm2.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            snapPic1 = (Bitmap) data.getExtras().get("data");
            snapshot1.setImageBitmap(snapPic1);
            snapshot1.setVisibility(View.VISIBLE);
            sp1.setVisibility(View.VISIBLE);
            base64str = encodeFromString(snapPic1);

        }else if(requestCode == CAMERA_REQUEST2 && resultCode == RESULT_OK) {

            snapPic2 = (Bitmap) data.getExtras().get("data");
            snapshot2.setImageBitmap(snapPic2);
            snapshot2.setVisibility(View.VISIBLE);
            sp2.setVisibility(View.VISIBLE);
            base64str2 = encodeFromString2(snapPic2);

        }else if(requestCode == CAMERA_REQUEST3 && resultCode == RESULT_OK) {

            snapPic3 = (Bitmap) data.getExtras().get("data");
            snapshot3.setImageBitmap(snapPic3);
            snapshot3.setVisibility(View.VISIBLE);
            sp3.setVisibility(View.VISIBLE);
            base64str3 = encodeFromString3(snapPic3);

        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Something went wrong please try again")
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
    public static String encodeFromString(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static String encodeFromString2(Bitmap bm2){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm2.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static String encodeFromString3(Bitmap bm3){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm3.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public class PostTechActionProgress extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String complaintId;
        String technicianId;
        String siteLoginLocation;
        String technicianDiagnosis;
        String correctiveAction;
        String technicianSuggestion;
        String sparesRequired;
        String sparesReplace;
        String customerNamePresent;
        String customerMobilePresent;
        String customerSignature;
        String technicianSignature;
        String snap1;
        String snap2;
        String snap3;
        String sStatus;

        PostTechActionProgress(String ComplaintId,String TechnicianId,String SiteLoginLocation,String TechnicianDiagnosis, String CorrectiveAction, String TechnicianSuggestion,String SpareRequired, String SpareReplace, String CustomerNamePresent, String CustomerMobilePresent,String CustomerSignature, String TechnicianSignature,String Snap1,String Snap2,String Snap3,String Status){
            this.complaintId=ComplaintId;
            this.technicianId=TechnicianId;
            this.siteLoginLocation=SiteLoginLocation;
            this.technicianDiagnosis=TechnicianDiagnosis;
            this.correctiveAction=CorrectiveAction;
            this.technicianSuggestion=TechnicianSuggestion;
            this.sparesRequired=SpareRequired;
            this.sparesReplace=SpareReplace;
            this.customerNamePresent=CustomerNamePresent;
            this.customerMobilePresent=CustomerMobilePresent;
            this.customerSignature=CustomerSignature;
            this.technicianSignature=TechnicianSignature;
            this.snap1=Snap1;
            this.snap2=Snap2;
            this.snap3=Snap3;
            this.sStatus=Status;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("complain_id",""+complaintId);
                data.put("technician_id",""+technicianId);
                data.put("site_login_loc",""+siteLoginLocation);
                data.put("eng_diag_observ",""+technicianDiagnosis);
                data.put("corrective_action",""+correctiveAction);
                data.put("tech_suggetion",""+technicianSuggestion);
                data.put("spares_req",""+sparesRequired);
                data.put("spares_rep",""+sparesReplace);
                data.put("custome_name",""+customerNamePresent);
                data.put("customer_mobile",""+customerMobilePresent);
                data.put("customer_sig",""+customerSignature);
                data.put("eng_sign",""+technicianSignature);
                data.put("snap1",""+snap1);
                data.put("snap2",""+snap2);
                data.put("customer_photo",""+snap3);
                data.put("Status",""+sStatus);
                jsonObject=handler.makeHttpRequest(AppConstant.PostTechActionProgress, "POST", data,null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONObject resultObject=jsonObject.optJSONObject("Result");
                        Toast toast=Toast.makeText(getContext(),"Report submitted successfully",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();
                        FragmentManager fragmentManager = getFragmentManager();
                        HomeFragment fragment = new HomeFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                                .disallowAddToBackStack()
                                .commit();
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

            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Something went wrong please Try later")
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

    public class PostSpareIndent extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String complaintId;
        String technicianId;
        String spareName;
        String qQty;
        String remarks;

        PostSpareIndent(String ComplaintID,String TechnicianID,String SpareName,String Qty, String Remark){
            this.complaintId=ComplaintID;
            this.technicianId=TechnicianID;
            this.spareName=SpareName;
            this.qQty=Qty;
            this.remarks=Remark;

        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("complain_id",""+complaintId);
                data.put("technician_id",""+technicianId);
                data.put("spare_name",""+spareName);
                data.put("qty",""+qQty);
                data.put("remarks",""+remarks);
                data.put("type",""+"");
                jsonObject=handler.makeHttpRequest(AppConstant.postSpareIndentURL, "POST", data,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONArray resultObject=jsonObject.optJSONArray("Result");
                        if (!resultObject.equals(null)){
                            get_data(resultObject.toString());
                            sharedPreferencesAction=getActivity().getSharedPreferences("action",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferencesAction.edit();
                            editor.clear();
                            editor.commit();
                        }
                        Toast toast=Toast.makeText(getContext(),message,Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();
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

            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Something went wrong please Try later")
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

    public void get_data(String data)
    {
        try {
            JSONArray data_array=new JSONArray(data);
            spares.clear();
            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj=new JSONObject(data_array.get(i).toString());

                Spares add=new Spares();
                add.cId = obj.getString("id");
                add.cSpareName = obj.getString("spare_name");
                add.cQty = obj.getString("qty");
                spares.add(add);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class tempSpareList extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject=null;
        String technicianId;
        String complaintId;
        tempSpareList(String TechnicianID,String ComplaintID){
            this.technicianId=TechnicianID;
            this.complaintId=ComplaintID;
            spares.clear();
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("technician_id",""+technicianId);
                data.put("complain_id",""+complaintId);
                data.put("type",""+"");
                jsonObject=handler.makeHttpRequest(AppConstant.TempSpareIndentListURL, "POST", data,null);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONArray resultObject=jsonObject.optJSONArray("Result");
                        if (!resultObject.equals(null)){
                            get_data(resultObject.toString());

                            sharedPreferencesAction=getActivity().getSharedPreferences("action",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferencesAction.edit();
                            editor.clear();
                            editor.commit();
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

            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Something went wrong please Try later")
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
}
