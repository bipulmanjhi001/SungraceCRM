package sungracecrm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class AdTechnicianDetailsFragment extends Fragment {

    private SharedPreferences sharedPreferencesLoginDetails;
    String sFullName,sMobileNo,sEmailId,sRole,sAccessToken,sProfilePic,sPassword,sUserName,sUserID;
    TextView mFullName,mUserName,mPassword,mEmailId,mWorkingArea3,mWorkingArea2,mWorkingArea1,mMobileNo,mRole,mStatus;
    ImageView mProfilePic;

    String sTechId;
    private static final String TechId = "id";
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;
    public AdTechnicianDetailsFragment() {
    }
    public static AdTechnicianDetailsFragment newInstance(String Id) {
        AdTechnicianDetailsFragment fragment = new AdTechnicianDetailsFragment();
        Bundle args = new Bundle();
        args.putString(TechId, Id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sTechId= getArguments().getString(TechId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_technician_details, container, false);

        sharedPreferencesLoginDetails=getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        sFullName=sharedPreferencesLoginDetails.getString("Name","");
        sMobileNo=sharedPreferencesLoginDetails.getString("MobileNo","");
        sEmailId=sharedPreferencesLoginDetails.getString("EmailId","");
        sRole=sharedPreferencesLoginDetails.getString("Role","");
        sAccessToken=sharedPreferencesLoginDetails.getString("AccessToken","");
        sProfilePic=sharedPreferencesLoginDetails.getString("ProfilePic","");
        sPassword=sharedPreferencesLoginDetails.getString("pass","");
        sUserName=sharedPreferencesLoginDetails.getString("username","");
        sUserID=sharedPreferencesLoginDetails.getString("Id","");

        linlaHeaderProgress = view.findViewById(R.id.linlaHeaderProgress);
        mFullName=view.findViewById(R.id.fullName);
        mUserName=view.findViewById(R.id.userName);
        mPassword=view.findViewById(R.id.password);
        mEmailId=view.findViewById(R.id.emailId);
        mWorkingArea1=view.findViewById(R.id.city);
        mWorkingArea2=view.findViewById(R.id.district);
        mWorkingArea3=view.findViewById(R.id.state);
        mProfilePic=view.findViewById(R.id.profilePic);
        mMobileNo=view.findViewById(R.id.mobileNo);
        mRole=view.findViewById(R.id.role);
        mStatus=view.findViewById(R.id.status);
        handler=new JsonObjectHandler();
        new getTechnicianDetailById(sTechId).execute();

        return view;
    }

    public class getTechnicianDetailById extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String technicianId;

        getTechnicianDetailById(String TechnicianId){
            this.technicianId=TechnicianId;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+technicianId);
                jsonObject = handler.makeHttpRequest(AppConstant.technicianDetailsById,"POST",data,null);
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

                        String UserName = resultObject.getString("username");
                        String Password = resultObject.getString("password");
                        String Role = resultObject.getString("role");
                        String Status = resultObject.getString("status");
                        String FullName = resultObject.getString("fullname");
                        String EmailId = resultObject.getString("emailid");
                        String MobileNo = resultObject.getString("mobile");
                        String WorkingArea1 = resultObject.getString("workingarea1");
                        String WorkingArea2 = resultObject.getString("workingarea2");
                        String WorkingArea3 = resultObject.getString("workingarea3");
                        String ProfilePic = resultObject.getString("photo");

                        mUserName.setText(":"+" "+UserName);
                        mPassword.setText(":"+" "+Password);
                        mRole.setText(":"+" "+Role);
                        mStatus.setText(":"+" "+Status);
                        mFullName.setText(FullName);
                        mEmailId.setText(":"+" "+EmailId);
                        mMobileNo.setText(":"+" "+MobileNo);
                        mWorkingArea1.setText(":"+" "+WorkingArea1);
                        mWorkingArea2.setText(":"+" "+WorkingArea2);
                        mWorkingArea3.setText(":"+" "+WorkingArea3);


                        if(ProfilePic.equals(null) || ProfilePic.equals("")){
                            mProfilePic.setVisibility(View.GONE);
                        }
                        else {
                            ProfilePic=ProfilePic.replace('~','/');
                            Picasso.with(getContext())
                                    .load(ProfilePic)
                                    .error(R.drawable.ic_photo_camera_)
                                    .placeholder(R.drawable.ic_photo_camera_)
                                    .into(mProfilePic);
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
