package sungracecrm;

import android.app.AlertDialog;
import android.content.Context;
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
import java.util.ArrayList;
import java.util.HashMap;
import static android.app.Activity.RESULT_OK;

public class AdAddTechnicianFragment extends Fragment {

    EditText mfullName,memailId,mmobileNo,muserId,mpassword,mcity,maddress;
    Spinner stateSpinner1,districtSpinner1,stateSpinner2,districtSpinner2,stateSpinner3,districtSpinner3;
    Button techsignUp,home,fromCamerabtn,fromGallerybtn,clearbtn;

    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    ImageView techImageUrl;
    Bitmap snapPic1;

    private ArrayList<Integer> stateIdlist;
    private ArrayList<String> stateList;
    private ArrayList<String> districtList;
    ArrayAdapter<String> stateNameAdapter;
    ArrayAdapter<String> districtNameAdapter;

    private ArrayList<Integer> stateIdlist2;
    private ArrayList<String> stateList2;
    private ArrayList<String> districtList2;
    ArrayAdapter<String> stateNameAdapter2;
    ArrayAdapter<String> districtNameAdapter2;

    private ArrayList<Integer> stateIdlist3;
    private ArrayList<String> stateList3;
    private ArrayList<String> districtList3;
    ArrayAdapter<String> stateNameAdapter3;
    ArrayAdapter<String> districtNameAdapter3;

    private SharedPreferences sharedPreferencesTechnicianDetails;
    LinearLayout linlaHeaderProgress;
    JsonObjectHandler handler;
    String base64str;

    public AdAddTechnicianFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_add_technician, container, false);

        mfullName=view.findViewById(R.id.fullName);
        memailId=view.findViewById(R.id.emailId);
        mmobileNo=view.findViewById(R.id.mobileNo);
        muserId=view.findViewById(R.id.userId);
        mpassword=view.findViewById(R.id.password);
        stateSpinner1=view.findViewById(R.id.state_spinner1);
        districtSpinner1=view.findViewById(R.id.dist_spinner1);
        stateSpinner2=view.findViewById(R.id.state_spinner2);
        districtSpinner2=view.findViewById(R.id.dist_spinner2);
        stateSpinner3=view.findViewById(R.id.state_spinner3);
        districtSpinner3=view.findViewById(R.id.dist_spinner3);
        techImageUrl=view.findViewById(R.id.imageUrl);
        techsignUp=view.findViewById(R.id.signUp);
        home=view.findViewById(R.id.home);
        clearbtn=view.findViewById(R.id.clear_btn);
        fromCamerabtn=view.findViewById(R.id.take_image_from_camera);
        fromGallerybtn=view.findViewById(R.id.take_image_from_gallery);

        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);

        home.setOnClickListener(new View.OnClickListener() {
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
                stateSpinner1.setAdapter(stateNameAdapter);
                districtSpinner1.setAdapter(districtNameAdapter);
                stateSpinner2.setAdapter(stateNameAdapter2);
                districtSpinner2.setAdapter(districtNameAdapter2);
                stateSpinner3.setAdapter(stateNameAdapter3);
                districtSpinner3.setAdapter(districtNameAdapter3);
                mfullName.setText("");
                memailId.setText("");
                mmobileNo.setText("");
                muserId.setText("");
                mpassword.setText("");
                techImageUrl.setImageBitmap(null);

            }
        });

        handler=new JsonObjectHandler();

        stateIdlist=new ArrayList<>();
        stateList=new ArrayList<>();
        districtList=new ArrayList<>();
        new GetStateList().execute();

        stateIdlist2=new ArrayList<>();
        stateList2=new ArrayList<>();
        districtList2=new ArrayList<>();
        new GetStateList2().execute();

        stateIdlist3=new ArrayList<>();
        stateList3=new ArrayList<>();
        districtList3=new ArrayList<>();
        new GetStateList3().execute();


        stateSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetDistrictList(stateIdlist.get(position)).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetDistrictList2(stateIdlist2.get(position)).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetDistrictList3(stateIdlist3.get(position)).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        techsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( mfullName.getText().toString().length() == 0 )
                    mfullName.setError( "Field is required!" );
                else if (mmobileNo.getText().toString().length() == 0 )
                    mmobileNo.setError( "Field is required!" );
                else if (muserId.getText().toString().length() == 0 )
                    muserId.setError( "Field is required!" );
                else if (mpassword.getText().toString().length() == 0 )
                    mpassword.setError( "Field is required!" );
                else if(stateSpinner1.getSelectedItemPosition()==0){
                    Toast toast=Toast.makeText(getContext(),"Working Area1 is Required",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                    View view=toast.getView();
                    TextView  view1=(TextView)view.findViewById(android.R.id.message);
                    view1.setTextColor(Color.WHITE);
                    view1.setTextSize(18);
                    view1.setPadding(10,5,10,5);
                    view.setBackgroundResource(R.color.colorAccent);
                    toast.show();

                }else {

                    String FullName=mfullName.getText().toString();
                    String EmailId = memailId.getText().toString();
                    String MobileNo = mmobileNo.getText().toString();
                    String UserId = muserId.getText().toString();
                    String Password = mpassword.getText().toString();
                    String State1 = stateSpinner1.getSelectedItem().toString();
                    String District1 = districtSpinner1.getSelectedItem().toString();
                    String State2 = stateSpinner2.getSelectedItem().toString();
                    String District2 = districtSpinner2.getSelectedItem().toString();
                    String State3 = stateSpinner3.getSelectedItem().toString();
                    String District3 = districtSpinner3.getSelectedItem().toString();
                    String TechnicianImageurl = base64str;

                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    new NewTechnicianAdd(FullName, EmailId, MobileNo, UserId, Password, State1, District1,State2, District2,State3, District3, TechnicianImageurl).execute();
                }
            }
        });
        fromCamerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        fromGallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        return view;
    }

    //--------//----------//------------- Camera snapshot1--------//----------//-------------//


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            snapPic1 = (Bitmap) data.getExtras().get("data");
            techImageUrl.setImageBitmap(snapPic1);
            techImageUrl.setVisibility(View.VISIBLE);
            base64str = encodeFromString(snapPic1);

       }else if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            techImageUrl.setImageURI(selectedImageUri);
            techImageUrl.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            try {
                snapPic1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            base64str = encodeFromString(snapPic1);

        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        byte[] b = baos.toByteArray();

       Bitmap bitmap2 = BitmapFactory.decodeByteArray(b,0,b.length);


        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    public class GetStateList extends AsyncTask<Void,Void,JSONObject> {

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
                            stateNameAdapter= new ArrayAdapter<String>(getContext(),R.layout.spinneritem,stateList);
                            stateNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stateSpinner1.setAdapter(stateNameAdapter);
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

                            districtNameAdapter= new ArrayAdapter<String>(getContext(),R.layout.spinneritem,districtList);
                            districtNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            districtSpinner1.setAdapter(districtNameAdapter);
                        }
                    }
                }

            }catch (Exception e){

            }
        }
    }

    public class GetStateList2 extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        GetStateList2() {

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
                            stateList2.add(tempobject.getString("name"));
                            stateIdlist2.add(tempobject.getInt("id"));
                            stateNameAdapter2= new ArrayAdapter<String>(getContext(),R.layout.spinneritem,stateList);
                            stateNameAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stateSpinner2.setAdapter(stateNameAdapter2);
                        }
                    }
                }

            }catch (Exception e){

            }
        }
    }

    public class GetDistrictList2 extends AsyncTask<Void,Void,JSONObject>{

        JSONObject jsonObject=null;
        int stateid2;
        GetDistrictList2(int stateid2) {
            this.stateid2=stateid2;
            districtList2.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("state",""+stateid2);
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
                            districtList2.add(tempobject.getString("name"));

                            districtNameAdapter2= new ArrayAdapter<String>(getContext(),R.layout.spinneritem,districtList2);
                            districtNameAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            districtSpinner2.setAdapter(districtNameAdapter2);
                        }
                    }
                }

            }catch (Exception e){

            }
        }
    }

    public class GetStateList3 extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        GetStateList3() {

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
                            stateList3.add(tempobject.getString("name"));
                            stateIdlist3.add(tempobject.getInt("id"));
                            stateNameAdapter3= new ArrayAdapter<String>(getContext(),R.layout.spinneritem,stateList);
                            stateNameAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stateSpinner3.setAdapter(stateNameAdapter3);
                        }
                    }
                }

            }catch (Exception e){

            }
        }
    }

    public class GetDistrictList3 extends AsyncTask<Void,Void,JSONObject>{

        JSONObject jsonObject=null;
        int stateid3;
        GetDistrictList3(int stateid3) {
            this.stateid3=stateid3;
            districtList3.clear();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("state",""+stateid3);
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
                            districtList3.add(tempobject.getString("name"));

                            districtNameAdapter3= new ArrayAdapter<String>(getContext(),R.layout.spinneritem,districtList3);
                            districtNameAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            districtSpinner3.setAdapter(districtNameAdapter3);
                        }
                    }
                }

            }catch (Exception e){

            }
        }
    }


    public class NewTechnicianAdd extends AsyncTask<Void,Void,JSONObject> {

        JSONObject jsonObject=null;
        String fullname;
        String emailid;
        String mobileno;
        String userid;
        String password;
        String state1;
        String district1;
        String state2;
        String district2;
        String state3;
        String district3;
        String techImageurl;


        NewTechnicianAdd(String FullName,String EmailId,String MobileNo,String UserId, String Password, String State1, String District1,String State2, String District2,String State3, String District3, String TechImcheUrl){
            this.fullname=FullName;
            this.emailid=EmailId;
            this.mobileno=MobileNo;
            this.userid=UserId;
            this.password=Password;
            this.state1=State1;
            this.district1=District1;
            this.state2=State2;
            this.district2=District2;
            this.state3=State3;
            this.district3=District3;
            this.techImageurl=TechImcheUrl;

        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("fullname",""+fullname);
                data.put("emailid",""+emailid);
                data.put("mobile",""+mobileno);
                data.put("username",""+userid);
                data.put("password",""+password);
                data.put("photo",""+techImageurl);
                data.put("state1",""+state1);
                data.put("district1",""+district1);
                data.put("state2",""+state2);
                data.put("district2",""+district2);
                data.put("state3",""+state3);
                data.put("district3",""+district3);

                jsonObject=handler.makeHttpRequest(AppConstant.addNewTechnicianURL, "POST", data,null);

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

                        String FullName = resultObject.getString("fullname");
                        String MobileNo = resultObject.getString("mobile");
                        String UserName = resultObject.getString("username");
                        String Password = resultObject.getString("password");

                        sharedPreferencesTechnicianDetails=getActivity().getSharedPreferences("technicianDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferencesTechnicianDetails.edit();
                        editor.putString("FullName", FullName);
                        editor.putString("MobileNo", MobileNo);
                        editor.putString("UserName", UserName);
                        editor.putString("Password", Password);
                        editor.commit();

                        Toast toast=Toast.makeText(getContext(),"Engineer Added Successfully",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();

                        stateSpinner1.setAdapter(stateNameAdapter);
                        districtSpinner1.setAdapter(districtNameAdapter);
                        stateSpinner2.setAdapter(stateNameAdapter2);
                        districtSpinner2.setAdapter(districtNameAdapter2);
                        stateSpinner3.setAdapter(stateNameAdapter3);
                        districtSpinner3.setAdapter(districtNameAdapter3);
                        mfullName.setText("");
                        memailId.setText("");
                        mmobileNo.setText("");
                        muserId.setText("");
                        mpassword.setText("");
                        techImageUrl.setImageBitmap(null);

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
