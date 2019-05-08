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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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


public class SelfCallFragment extends Fragment implements LocationListener {

    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;

    private SharedPreferences sharedPreferencesLoginDetails;
    String cFullName,cMobileNo,cRole,cAccessToken,cProfilePic,cPassword,cUserName,cID;
    SharedPreferences sharedPreferencesAction;
    SignaturePad customerSignaturePad,technicianSignaturePad;
    Button saveButtonCustomer, clearButtonCustomer,saveButtonTechnician, clearButtonTechnician,Snapshot_11,Snapshot_22,Snapshot_33,submitBTN,spareBTN,backBTN;
    Bitmap custSign,techSign;
    String path,path2,SpResult;
    String base64strCustSign,base64strTechSign;
    LocationManager locationManager;
    TextView siteLoginLocation,sp1,sp2,sp3;

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST2 = 1889;
    private static final int CAMERA_REQUEST3 = 1887;
    ImageView snapshot1,snapshot2,snapshot3;
    Bitmap snapPic1,snapPic2,snapPic3;
    String base64str,base64str2,base64str3;

    TextView capacityName;
    EditText mDiagnosis,mCorrective_action,mTechnical_suggestion,mSpare_required,mSpare_replaced,mcustomerFeedback;
    EditText powerCapacity_no,agencyName,customerName,address,city,contactPerson_name,mobileNo,emailId,serial_no;
    Spinner productCategorySpinner,productTypeSpinner,complaintTypeSpinner,stateSpinner,districtSpinner;

    private ArrayList<String> productCategoryList;
    ArrayAdapter<String> productCategoryAdapter;

    private ArrayList<Integer> productIdlist;

    private ArrayList<String> productTypeList;
    ArrayAdapter<String> productTypeAdapter;

    private ArrayList<String> complaintTypeList;
    ArrayAdapter<String> complaintTypeAdapter;

    private ArrayList<Integer> stateIdlist;
    private ArrayList<String> stateList;
    private ArrayList<String> districtList;
    ArrayAdapter<String> stateNameAdapter;
    ArrayAdapter<String> districtNameAdapter;

    EditText spareName,qQty,rRemark;
    public ArrayList<Spares> spares = new ArrayList<Spares>();
    public SelfCallTempSpareListAdapter adapter;
    public ListView spareListView;

    public SelfCallFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_call, container, false);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        getLocation();

        sharedPreferencesLoginDetails=getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        cFullName=sharedPreferencesLoginDetails.getString("Name","");
        cMobileNo=sharedPreferencesLoginDetails.getString("MobileNo","");
        cRole=sharedPreferencesLoginDetails.getString("Role","");
        cAccessToken=sharedPreferencesLoginDetails.getString("AccessToken","");
        cProfilePic=sharedPreferencesLoginDetails.getString("ProfilePic","");
        cPassword=sharedPreferencesLoginDetails.getString("pass","");
        cUserName=sharedPreferencesLoginDetails.getString("username","");
        cID=sharedPreferencesLoginDetails.getString("Id","");

        sharedPreferencesAction=getActivity().getApplicationContext().getSharedPreferences("action", Context.MODE_PRIVATE);
        SpResult=sharedPreferencesAction.getString("spareList","");

        if (SpResult.equals("OK")){
            new tempSpareList(cID).execute();
        }

        linlaHeaderProgress =  view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);

        productCategorySpinner=view.findViewById(R.id.product_category_spinner);
        productTypeSpinner=view.findViewById(R.id.product_type_spinner);
        powerCapacity_no=view.findViewById(R.id.power_capacity_no);
        customerName=view.findViewById(R.id.customer_name);
        address=view.findViewById(R.id.address_et);
        city=view.findViewById(R.id.city_et);
        districtSpinner=view.findViewById(R.id.district_spinner);
        stateSpinner=view.findViewById(R.id.state_spinner);
        contactPerson_name=view.findViewById(R.id.contact_person_name);
        mobileNo=view.findViewById(R.id.mobile_no);
        agencyName=view.findViewById(R.id.agency_name);
        emailId=view.findViewById(R.id.email_id);
        complaintTypeSpinner=view.findViewById(R.id.complaint_type_spinner);
        serial_no=view.findViewById(R.id.serial_no);
        capacityName=view.findViewById(R.id.capacityName);

        mDiagnosis=view.findViewById(R.id.diagnosis);
        mCorrective_action=view.findViewById(R.id.corrective_action);
        mTechnical_suggestion=view.findViewById(R.id.technical_suggestion);
        mSpare_required=view.findViewById(R.id.spare_required);
        mSpare_replaced=view.findViewById(R.id.spare_replaced);
        mcustomerFeedback=view.findViewById(R.id.customerFeedback);
        customerSignaturePad=view.findViewById(R.id.customer_signaturePad);
        technicianSignaturePad=view.findViewById(R.id.technician_signaturePad);
        siteLoginLocation=view.findViewById(R.id.SiteLoginLocation);

        sp1=view.findViewById(R.id.snapshot_1_title);
        sp2=view.findViewById(R.id.snapshot_2_title);
        sp3=view.findViewById(R.id.snapshot_3_title);

        snapshot1=view.findViewById(R.id.snapshot_1);
        snapshot2=view.findViewById(R.id.snapshot_2);
        snapshot3=view.findViewById(R.id.snapshot_3);
        Snapshot_11=view.findViewById(R.id.snapshot_11);
        Snapshot_22=view.findViewById(R.id.snapshot_22);
        Snapshot_33=view.findViewById(R.id.snapshot_33);

        saveButtonCustomer = view.findViewById(R.id.saveButtonCust);
        clearButtonCustomer = view.findViewById(R.id.clearButtonCust);
        saveButtonTechnician = view.findViewById(R.id.saveButtonTech);
        clearButtonTechnician = view.findViewById(R.id.clearButtonTech);

        submitBTN=view.findViewById(R.id.submit_btn);
        backBTN=view.findViewById(R.id.back_btn);
        spareBTN=view.findViewById(R.id.spares_btn);
        spareListView = view.findViewById(R.id.Self_spare_temp_list);
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
        productIdlist=new ArrayList<>();
        productCategoryList=new ArrayList<>();
        productTypeList=new ArrayList<>();
        complaintTypeList=new ArrayList<>();
        stateIdlist=new ArrayList<>();
        stateList=new ArrayList<>();
        districtList=new ArrayList<>();

        new ProductCategoryList().execute();
        new GetStateList().execute();
        new GetComplaintTypeList().execute();

        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new GetProductTypeList(productIdlist.get(position)).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetDistrictList(stateIdlist.get(position)).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( powerCapacity_no.getText().toString().length() == 0 ){
                    powerCapacity_no.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Capacity is Required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (capacityName.getText().toString().length() == 0 ){
                    powerCapacity_no.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Capacity is Required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (agencyName.getText().toString().length() == 0 ){
                    agencyName.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Agency name is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (customerName.getText().toString().length() == 0 ){
                    customerName.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Customer name is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (address.getText().toString().length() == 0 ){
                    address.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Address is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (city.getText().toString().length() == 0 ){
                    city.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"City is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (contactPerson_name.getText().toString().length() == 0 ){
                    contactPerson_name.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Contact person is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (mobileNo.getText().toString().length() == 0 ){
                    mobileNo.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Mobile number is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();
                }

                else if (serial_no.getText().toString().length() == 0 ){
                    serial_no.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Serial number is required",Toast.LENGTH_LONG);
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

                    String ProductCategorySpinner = String.valueOf(productCategorySpinner.getSelectedItemId()+1);
                    String ProductTypeSpinner = productTypeSpinner.getSelectedItem().toString();
                    String PowerCapacity_no = powerCapacity_no.getText().toString();
                    String PowerCapacity_Name = capacityName.getText().toString();
                    String AgencyName = agencyName.getText().toString();
                    String CustomerName = customerName.getText().toString();
                    String EState = stateSpinner.getSelectedItem().toString();
                    String EDistrict = districtSpinner.getSelectedItem().toString();
                    String City = city.getText().toString();
                    String EAddress = address.getText().toString();
                    String ContactPerson_name = contactPerson_name.getText().toString();
                    String MobileNo = mobileNo.getText().toString();
                    String EmailId = emailId.getText().toString();
                    String ComplaintTypeSpinner = complaintTypeSpinner.getSelectedItem().toString();
                    String Serial_no = serial_no.getText().toString();

                    String SiteLoginLocation=siteLoginLocation.getText().toString();
                    String TechnicianDiagnosis=mDiagnosis.getText().toString();
                    String CorrectiveAction=mCorrective_action.getText().toString();
                    String TechnicianSuggestion=mTechnical_suggestion.getText().toString();
                    String SpareRequired=mSpare_required.getText().toString();
                    String SpareReplace=mSpare_replaced.getText().toString();
                    String CustomerFeedBack=mcustomerFeedback.getText().toString();
                    String CustomerSignature=base64strCustSign;
                    String TechnicianSignature=base64strTechSign;
                    String Snap1=base64str;
                    String Snap2=base64str2;
                    String Snap3=base64str3;

                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    new NewSelfCallComplaintRegister(PowerCapacity_no, CustomerName, EAddress, City, ContactPerson_name, MobileNo,AgencyName, EmailId, Serial_no, ProductCategorySpinner, ProductTypeSpinner, EDistrict, EState, ComplaintTypeSpinner, PowerCapacity_Name,SiteLoginLocation,TechnicianDiagnosis,CorrectiveAction,TechnicianSuggestion,SpareRequired,SpareReplace,CustomerFeedBack,CustomerSignature,TechnicianSignature,Snap1,Snap2,Snap3).execute();
                }
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                HomeFragment fragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();

            }
        });

        spareBTN.setOnClickListener(new View.OnClickListener() {

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
                        String TechnicianID=cID;
                        String SpareName=spareName.getText().toString();
                        String Qty=qQty.getText().toString();
                        String Remark=rRemark.getText().toString();

                        handler=new JsonObjectHandler();
                        new tempSpareList(cID).execute();
                        new PostSpareIndent(TechnicianID,SpareName,Qty,Remark).execute();

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        adapter = new SelfCallTempSpareListAdapter(this,getFragmentManager());
        spareListView.setAdapter(adapter);
        handler=new JsonObjectHandler();
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        return view;
    }
    void getLocation() {
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
        siteLoginLocation.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude()+",");
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
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static String saveTechnicianSignatureImage(Bitmap bm2){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm2.compress(Bitmap.CompressFormat.PNG, 100, baos);
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
    public class ProductCategoryList extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        ProductCategoryList(){

        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                jsonObject=handler.makeHttpRequest(AppConstant.productCategoryListUrl, "GET", data,null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected  void onPostExecute(final JSONObject jsonObject){
            linlaHeaderProgress.setVisibility(View.GONE);
            try {
                if(jsonObject!=null) {

                    JSONObject statusObject = jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        JSONArray productCategoryObjectList=jsonObject.optJSONArray("Result");

                        for(int i=0;i<productCategoryObjectList.length();i++){
                            JSONObject tempobject=productCategoryObjectList.getJSONObject(i);
                            productCategoryList.add(tempobject.getString("name"));
                            productIdlist.add(tempobject.getInt("id"));
                        }
                        productCategoryAdapter= new ArrayAdapter<String>(getActivity(),R.layout.spinneritem,productCategoryList);
                        productCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        productCategorySpinner.setAdapter(productCategoryAdapter);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    public class GetProductTypeList extends AsyncTask<Void,Void,JSONObject>{

        JSONObject jsonObject=null;
        int cateId;
        GetProductTypeList(int CatId) {
            this.cateId=CatId;
            productTypeList.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("productcat",""+cateId);
                jsonObject=handler.makeHttpRequest(AppConstant.productTypeListUrl, "POST", data,null);

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
                        JSONObject resultObject=jsonObject.optJSONObject("Result");
                        if (!resultObject.equals(null)) {
                            JSONArray productTypeByCateObject = resultObject.optJSONArray("producttype");
                            if (!productTypeByCateObject.equals(null)) {
                                for (int i = 0; i < productTypeByCateObject.length(); i++) {
                                    JSONObject tempobject = productTypeByCateObject.getJSONObject(i);
                                    productTypeList.add(tempobject.getString("type"));

                                    productTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinneritem, productTypeList);
                                    productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    productTypeSpinner.setAdapter(productTypeAdapter);
                                }

                            }
                            JSONObject capacityByProductCat = resultObject.optJSONObject("Capacity");
                            if (!capacityByProductCat.equals(null)) {
                                String Capacity = capacityByProductCat.getString("capacity");
                                capacityName.setText(Capacity);
                            }
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

            }catch (Exception e){

            }
        }
    }

    public class GetComplaintTypeList extends AsyncTask<Void,Void,JSONObject>{

        JSONObject jsonObject=null;
        GetComplaintTypeList() {
            complaintTypeList.clear();
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HashMap<String,String> data=new HashMap<>();
                jsonObject=handler.makeHttpRequest(AppConstant.selfComplaintTypeListUrl, "GET", data,null);

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
                        JSONArray resultObject=jsonObject.optJSONArray("Result");
                            if (!resultObject.equals(null)) {
                                complaintTypeList.clear();
                                for (int i = 0; i < resultObject.length(); i++) {
                                    JSONObject tempobject = resultObject.getJSONObject(i);
                                    complaintTypeList.add(tempobject.getString("complain_type"));

                                    complaintTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinneritem, complaintTypeList);
                                    complaintTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    complaintTypeSpinner.setAdapter(complaintTypeAdapter);
                                }

                            }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

            }catch (Exception e){

            }
        }
    }

    public class GetStateList extends AsyncTask<Void,Void,JSONObject>{

        JSONObject jsonObject=null;
        GetStateList() {

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HashMap<String,String> data=new HashMap<>();
                jsonObject=handler.makeHttpRequest(AppConstant.stateListURL, "GET", data,null);

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
                        JSONArray stateObjectList=jsonObject.optJSONArray("Result");

                        for (int i=0;i<stateObjectList.length();i++){
                            JSONObject tempobject=stateObjectList.getJSONObject(i);
                            stateList.add(tempobject.getString("name"));
                            stateIdlist.add(tempobject.getInt("id"));
                            stateNameAdapter= new ArrayAdapter<String>(getActivity(),R.layout.spinneritem,stateList);
                            stateNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stateSpinner.setAdapter(stateNameAdapter);
                        }
                    }
                }

            }catch (Exception e){

            }
        }
    }

    public class GetDistrictList extends AsyncTask<Void,Void,JSONObject>{

        JSONObject jsonObject=null;
        int stateid;
        GetDistrictList(int stateid) {
            this.stateid=stateid;
            districtList.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("state",""+stateid);
                jsonObject=handler.makeHttpRequest(AppConstant.districtListURL, "GET", data,null);

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
                        JSONArray stateObjectList=jsonObject.optJSONArray("Result");
                        for (int i=0;i<stateObjectList.length();i++){
                            JSONObject tempobject=stateObjectList.getJSONObject(i);
                            districtList.add(tempobject.getString("name"));

                            districtNameAdapter= new ArrayAdapter<String>(getActivity(),R.layout.spinneritem,districtList);
                            districtNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            districtSpinner.setAdapter(districtNameAdapter);
                        }
                    }
                }

            }catch (Exception e){

            }
        }
    }

    public class NewSelfCallComplaintRegister extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String powerCapacity_no;
        String customerName;
        String eAddress;
        String city;
        String contactPerson_name;
        String mobileNo;
        String agencyName;
        String emailId;
        String serial_no;
        String productCategorySpinner;
        String productType;
        String eDistrict;
        String eState;
        String complaintTypeSpinner;
        String powerCapacity_Name;
        String siteLoginLocation;
        String technicianDiagnosis;
        String correctiveAction;
        String technicianSuggestion;
        String spareRequired;
        String spareReplace;
        String customerFeedBack;
        String customerSignature;
        String technicianSignature;
        String snap1;
        String snap2;
        String snap3;

        NewSelfCallComplaintRegister(String PowerCapacity_no, String CustomerName, String EAddress, String City, String ContactPerson_name, String MobileNo, String AgencyName, String EmailId, String Serial_no, String ProductCategorySpinner, String ProductType, String EDistrict, String EState, String ComplaintTypeSpinner,String PowerCapacity_Name, String SiteLoginLocation, String TechnicianDiagnosis, String CorrectiveAction, String TechnicianSuggestion, String SpareRequired, String SpareReplace, String CustomerFeedBack, String CustomerSignature, String TechnicianSignature, String Snap1, String Snap2, String Snap3){
            this. powerCapacity_no=PowerCapacity_no;
            this. customerName=CustomerName;
            this. eAddress=EAddress;
            this. city=City;
            this. contactPerson_name=ContactPerson_name;
            this. mobileNo=MobileNo;
            this. agencyName=AgencyName;
            this. emailId=EmailId;
            this. serial_no=Serial_no;
            this. productCategorySpinner=ProductCategorySpinner;
            this. productType=ProductType;
            this. eDistrict=EDistrict;
            this. eState=EState;
            this. complaintTypeSpinner=ComplaintTypeSpinner;
            this. powerCapacity_Name=PowerCapacity_Name;
            this. siteLoginLocation=SiteLoginLocation;
            this. technicianDiagnosis=TechnicianDiagnosis;
            this. correctiveAction=CorrectiveAction;
            this. technicianSuggestion=TechnicianSuggestion;
            this. spareRequired=SpareRequired;
            this. spareReplace=SpareReplace;
            this. customerFeedBack=CustomerFeedBack;
            this. customerSignature=CustomerSignature;
            this. technicianSignature=TechnicianSignature;
            this. snap1=Snap1;
            this. snap2=Snap2;
            this. snap3=Snap3;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("technician_id",""+cID);
                data.put("product_category",""+productCategorySpinner);
                data.put("product_type",""+productType);
                data.put("capacity_name",""+powerCapacity_Name);
                data.put("capacity",""+powerCapacity_no);
                data.put("agency_name",""+agencyName);
                data.put("customer_name",""+customerName);
                data.put("address",""+eAddress);
                data.put("city",""+city);
                data.put("district",""+eDistrict);
                data.put("state",""+eState);
                data.put("contact_person",""+contactPerson_name);
                data.put("mobile",""+mobileNo);
                data.put("email",""+emailId);
                data.put("serial_no",""+serial_no);
                data.put("site_login_loc",""+siteLoginLocation);
                data.put("eng_diag_obs",""+technicianDiagnosis);
                data.put("corrective_action",""+correctiveAction);
                data.put("tech_suggestion",""+technicianSuggestion);
                data.put("spare_required",""+spareRequired);
                data.put("spare_replaced",""+spareReplace);
                data.put("customer_feedback",""+customerFeedBack);
                data.put("customer_signature",""+customerSignature);
                data.put("customer_photo",""+snap3);
                data.put("engi_signature",""+technicianSignature);
                data.put("sys_snap1",""+snap1);
                data.put("sys_snap2",""+snap2);
                data.put("complain_type",""+complaintTypeSpinner);
                jsonObject=handler.makeHttpRequest(AppConstant.selfCallComplaintRegisterUrl, "POST", data,null);

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

                        Toast toast=Toast.makeText(getContext(),"Complaint submitted successfully",Toast.LENGTH_LONG);
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
                        fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        String technicianId;
        String spareName;
        String qQty;
        String remarks;

        PostSpareIndent(String TechnicianID,String SpareName,String Qty, String Remark){
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
                data.put("technician_id",""+technicianId);
                data.put("spare_name",""+spareName);
                data.put("qty",""+qQty);
                data.put("remarks",""+remarks);
                data.put("complain_id",""+"");
                data.put("type",""+"selfcall");
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
        tempSpareList(String TechnicianID){
            this.technicianId=TechnicianID;

        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("technician_id",""+technicianId);
                data.put("complain_id",""+"");
                data.put("type",""+"selfcall");
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
