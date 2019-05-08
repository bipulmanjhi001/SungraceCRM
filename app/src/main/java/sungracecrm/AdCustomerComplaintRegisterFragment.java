package sungracecrm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AdCustomerComplaintRegisterFragment extends Fragment {

    EditText powerCapacity_no,customerName,address,city,contactPerson_name,mobileNo;
    EditText alternateNo,emailId,serial_no,problemNature,dateofPurchase,agencyGovt;
    Spinner productCategorySpinner,productTypeSpinner,complaintTypeSpinner,stateSpinner,districtSpinner,warrantySpinner;
    String WarrantyStatus;
    Button manuallyRegbtn,backbtn,cameraBTN1,cameraBTN2,galleryBTN1,galleryBTN2,clearbtn;
    TextView capacityName, serialNo_text,serial_no_text;
    private SharedPreferences sharedPreferencesCustomerDetails;
    LinearLayout linlaHeaderProgress;

    private android.app.DatePickerDialog DatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_PICTURE2 = 2;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST2 = 1889;
    ImageView snapshot1,snapshot2;
    Bitmap snapPic1,snapPic2;

    JsonObjectHandler handler;
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

    String base64str,base64str2;

    public AdCustomerComplaintRegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_customer_complaint_register, container, false);
        serial_no_text=view.findViewById(R.id.serial_no_text);
        manuallyRegbtn=view.findViewById(R.id.manually_reg_btn);
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
        alternateNo=view.findViewById(R.id.alternate_no);
        emailId=view.findViewById(R.id.email_id);
        complaintTypeSpinner=view.findViewById(R.id.complaint_type_spinner);
        serial_no=view.findViewById(R.id.serial_no);
        serialNo_text=view.findViewById(R.id.serial_no_text);
        problemNature=view.findViewById(R.id.problem_nature);
        capacityName=view.findViewById(R.id.capacityName);
        snapshot1=view.findViewById(R.id.snapshot_1);
        snapshot2=view.findViewById(R.id.snapshot_2);
        backbtn=view.findViewById(R.id.back_btn);
        clearbtn=view.findViewById(R.id.clear_btn);

        cameraBTN1=view.findViewById(R.id.take_image_from_camera);
        cameraBTN2=view.findViewById(R.id.snapshot_02);
        galleryBTN1=view.findViewById(R.id.take_image_from_gallery);
        galleryBTN2=view.findViewById(R.id.snapshot_22);

        warrantySpinner=view.findViewById(R.id.warrantyStatus_spinner);
        agencyGovt=view.findViewById(R.id.agencyGovt);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateofPurchase=view.findViewById(R.id.dateofPurchase);
        dateofPurchase.setInputType(InputType.TYPE_NULL);


        setDateTimeField();

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(getContext(),R.array.warranty,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        warrantySpinner.setAdapter(adapter);
        warrantySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text=parent.getItemAtPosition(position).toString();

                if(position>0)
                    WarrantyStatus= warrantySpinner.getSelectedItem().toString();
                else
                    WarrantyStatus="";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linlaHeaderProgress =  view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);

        handler=new JsonObjectHandler();

        productIdlist=new ArrayList<>();
        productCategoryList=new ArrayList<>();
        productTypeList=new ArrayList<>();
        complaintTypeList=new ArrayList<>();
        new ProductCategoryList().execute();

        stateIdlist=new ArrayList<>();
        stateList=new ArrayList<>();
        districtList=new ArrayList<>();
        new GetStateList().execute();
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetDistrictList(stateIdlist.get(position)).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetProductTypeList(productIdlist.get(position)).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cameraBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        cameraBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent2, CAMERA_REQUEST2);
            }
        });
        galleryBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        galleryBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE2);
            }
        });
        manuallyRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( powerCapacity_no.getText().toString().length() == 0 ){
                    powerCapacity_no.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Capacity is required",Toast.LENGTH_LONG);
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
                    Toast toast=Toast.makeText(getContext(),"Capacity is required",Toast.LENGTH_LONG);
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
                    Toast toast=Toast.makeText(getContext(),"Name is required",Toast.LENGTH_LONG);
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
                    Toast toast=Toast.makeText(getContext(),"Mobile No is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();

                }

                else if (problemNature.getText().toString().length() == 0 ){
                    problemNature.setError( "Field is required!" );
                    Toast toast=Toast.makeText(getContext(),"Nature of problem is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();

                }
                else if (stateSpinner.getSelectedItemPosition()==0){
                    Toast toast=Toast.makeText(getContext(),"State Name is required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();

                }
                else if (warrantySpinner.getSelectedItemPosition()==0){
                    Toast toast=Toast.makeText(getContext(),"Warranty is required",Toast.LENGTH_LONG);
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
                    String CustomerName = customerName.getText().toString();
                    String EAddress = address.getText().toString();
                    String City = city.getText().toString();
                    String EDistrict = districtSpinner.getSelectedItem().toString();
                    String EState = stateSpinner.getSelectedItem().toString();
                    String ContactPerson_name = contactPerson_name.getText().toString();
                    String MobileNo = mobileNo.getText().toString();
                    String AlternateNo = alternateNo.getText().toString();
                    String EmailId = emailId.getText().toString();
                    String ComplaintTypeSpinner = complaintTypeSpinner.getSelectedItem().toString();
                    String ControllerSerial_no = serial_no.getText().toString();
                    String ProblemNature = problemNature.getText().toString();

                    WarrantyStatus=warrantySpinner.getSelectedItem().toString();
                    String DateOfPurchase = dateofPurchase.getText().toString();
                    String AgencyName=agencyGovt.getText().toString();

                    String Snapshot1encodedImage = base64str;
                    String Snapshot2encodedImage = base64str2;

                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    manuallyRegbtn.setVisibility(View.GONE);
                    backbtn.setVisibility(View.GONE);
                    clearbtn.setVisibility(View.GONE);
                    new NewComplaintRegister(Snapshot1encodedImage, Snapshot2encodedImage, PowerCapacity_no, CustomerName, EAddress, City, ContactPerson_name, MobileNo, AlternateNo, EmailId, ControllerSerial_no, ProblemNature, ProductCategorySpinner, ProductTypeSpinner, EDistrict, EState, ComplaintTypeSpinner, PowerCapacity_Name,WarrantyStatus,DateOfPurchase,AgencyName).execute();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdDashboardFragment fragment = new AdDashboardFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
            }
        });
        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productCategorySpinner.setAdapter(productCategoryAdapter);
                productTypeSpinner.setAdapter(productTypeAdapter);
                powerCapacity_no.setText("");
                customerName.setText("");
                address.setText("");
                city.setText("");
                districtSpinner.setAdapter(districtNameAdapter);
                stateSpinner.setAdapter(stateNameAdapter);
                contactPerson_name.setText("");
                mobileNo.setText("");
                alternateNo.setText("");
                emailId.setText("");
                complaintTypeSpinner.setAdapter(complaintTypeAdapter);
                problemNature.setText("");
                capacityName.setText("");
                snapshot1.setImageBitmap(null);
                snapshot2.setImageBitmap(null);
                agencyGovt.setText("");
                dateofPurchase.setText("");
                serial_no.setText("");
            }
        });
        return view;
    }

    //--------//----------//------------- Camera snapshot1--------//----------//-------------//

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            snapPic1 = (Bitmap) data.getExtras().get("data");
            snapshot1.setImageBitmap(snapPic1);
            snapshot1.setVisibility(View.VISIBLE);
            base64str = encodeFromString(snapPic1);

        }else if(requestCode == CAMERA_REQUEST2 && resultCode == RESULT_OK) {

            snapPic2 = (Bitmap) data.getExtras().get("data");
            snapshot2.setImageBitmap(snapPic2);
            snapshot2.setVisibility(View.VISIBLE);
            base64str2 = encodeFromString2(snapPic2);

        }else if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            snapshot1.setImageURI(selectedImageUri);
            snapshot1.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            try {
                snapPic1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            base64str = encodeFromString(snapPic1);

        }else if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE2) {
            Uri selectedImageUri = data.getData();
            snapshot2.setImageURI(selectedImageUri);
            snapshot2.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            try {
                snapPic2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            base64str2 = encodeFromString(snapPic2);

        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Something went wrong please reload and try again")
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
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object

        byte[] b = baos.toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);


        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    //--------//----------//------------- Camera snapshot2--------//----------//-------------//

    public static String encodeFromString2(Bitmap bm2){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm2.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object

        byte[] b = baos.toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);


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
                                String SerialNoName = capacityByProductCat.getString("serialname");
                                capacityName.setText(Capacity);
                                serial_no_text.setText(SerialNoName);
                            }
                            JSONArray complaintTypeObject = resultObject.optJSONArray("ComplainType");
                            if (!complaintTypeObject.equals(null)) {
                                complaintTypeList.clear();
                                for (int i = 0; i < complaintTypeObject.length(); i++) {
                                    JSONObject tempobject = complaintTypeObject.getJSONObject(i);
                                    complaintTypeList.add(tempobject.getString("name"));

                                    complaintTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinneritem, complaintTypeList);
                                    complaintTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    complaintTypeSpinner.setAdapter(complaintTypeAdapter);
                                }

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

    public class NewComplaintRegister extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String capacity_No;
        String customerName;
        String eAddress;
        String eCity;
        String contactPerson;
        String mobile;
        String alternateNo;
        String email;
        String controllerSerialNo;
        String problemNature;
        String snapshot1encodedImage;
        String snapshot2encodedImage;
        String productCategory;
        String productType;
        String eDistrict;
        String eState;
        String complaintType;
        String capacityName;
        String warrantyStatus;
        String dateOfPurchase;
        String agencyName;


        NewComplaintRegister(String Snapshot1encodedImage,String Snapshot2encodedImage,String PowerCapacity_no,String CustomerName, String EAddress, String City,String ContcatPerson, String MobileNo, String AlternateNo, String EmailId, String ControllerSerialNo, String ProblemNature,String ProductCategory,String ProductType,String EDistrict,String EState,String ComplaintType, String CapacityName,String WarrantyStatus, String DateOfPurchase, String AgencyName){
            this.productCategory=ProductCategory;
            this.productType=ProductType;
            this.snapshot1encodedImage=Snapshot1encodedImage;
            this.snapshot2encodedImage=Snapshot2encodedImage;
            this.capacity_No=PowerCapacity_no;
            this.customerName=CustomerName;
            this.eAddress=EAddress;
            this.eCity=City;
            this.eDistrict=EDistrict;
            this.eState=EState;
            this.contactPerson=ContcatPerson;
            this.mobile=MobileNo;
            this.alternateNo=AlternateNo;
            this.email=EmailId;
            this.complaintType=ComplaintType;
            this.controllerSerialNo=ControllerSerialNo;
            this.problemNature=ProblemNature;
            this.capacityName=CapacityName;
            this.warrantyStatus=WarrantyStatus;
            this.dateOfPurchase=DateOfPurchase;
            this.agencyName=AgencyName;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("product_category",""+productCategory);
                data.put("product_type",""+productType);
                data.put("capacity",""+capacity_No);
                data.put("customer_name",""+customerName);
                data.put("address",""+eAddress);
                data.put("village",""+eCity);
                data.put("district",""+eDistrict);
                data.put("state",""+eState);
                data.put("contact_pname",""+contactPerson);
                data.put("mobile",""+mobile);
                data.put("amobile",""+alternateNo);
                data.put("email",""+email);
                data.put("complain_type",""+complaintType);
                data.put("controller_sno",""+controllerSerialNo);
                data.put("nature_of_p",""+problemNature);
                data.put("productsnap1",""+snapshot1encodedImage);
                data.put("productsnap2",""+snapshot2encodedImage);
                data.put("capacity_name",""+capacityName);
                data.put("warranty_status",""+warrantyStatus);
                data.put("date_of_purchase",""+dateOfPurchase);
                data.put("agency_name",""+agencyName);
                jsonObject=handler.makeHttpRequest(AppConstant.complaintRegisterUrl, "POST", data,null);

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

                        String CUID = resultObject.getString("cuid");
                        String CRN = resultObject.getString("complainsn");

                        sharedPreferencesCustomerDetails=getActivity().getSharedPreferences("customerDetails",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferencesCustomerDetails.edit();
                        editor.putString("CUID", CUID);
                        editor.putString("CRN", CRN);
                        editor.commit();

                        productCategorySpinner.setAdapter(productCategoryAdapter);
                        complaintTypeSpinner.setAdapter(complaintTypeAdapter);
                        agencyGovt.setText("");
                        powerCapacity_no.setText("");
                        address.setText("");
                        city.setText("");
                        contactPerson_name.setText("");
                        mobileNo.setText("");
                        emailId.setText("");
                        serial_no.setText("");
                        dateofPurchase.setText("");
                        snapshot1.setImageBitmap(null);
                        snapshot2.setImageBitmap(null);
                        stateSpinner.setAdapter(stateNameAdapter);
                        districtSpinner.setAdapter(districtNameAdapter);

                        FragmentManager fragmentManager = getFragmentManager();
                        AdComplaintThankyouFragment fragment = new AdComplaintThankyouFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                                .addToBackStack("AdCustomerComplaintRegisterFragment")
                                .commit();


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

    private void setDateTimeField() {
        dateofPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateofPurchase.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
}
