package sungracecrm;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText mUserName,mPassword;
    private CheckBox rememberMe;
    private boolean isRmChecked=false;
    private SharedPreferences sharedPreferencesLoginDetails;
    JsonObjectHandler handler;
    LinearLayout linlaHeaderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linlaHeaderProgress =  findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);

        mUserName =  findViewById(R.id.userName);
        mPassword =  findViewById(R.id.password);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.login_btn);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                if( mUserName.getText().toString().length() == 0 ) {
                    mUserName.setError("Enter UserName!");
                    return;
                }
                if( mPassword.getText().toString().length() == 0 ) {
                    mPassword.setError("Enter password!");
                    return;
                }
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                String userName=mUserName.getText().toString();
                String password=mPassword.getText().toString();

                if (isRmChecked == true){
                    SharedPreferences.Editor editor=sharedPreferencesLoginDetails.edit();
                    editor.putString("pass", ""+mPassword);
                    editor.putString("username", ""+mUserName);
                    editor.putString("remember","true");
                }
                new UserLoginTask(userName, password, isRmChecked).execute();
            }
        });

        rememberMe=findViewById(R.id.rememberme_checkbox);
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRmChecked=isChecked;
            }
        });
        sharedPreferencesLoginDetails=getSharedPreferences("loginDetails",MODE_PRIVATE);
        if(sharedPreferencesLoginDetails.getBoolean("remember",true))
        {
            rememberMe.setChecked(true);
            mUserName.setText(sharedPreferencesLoginDetails.getString("username",""));
            mPassword.setText(sharedPreferencesLoginDetails.getString("pass",""));
        }else{
            rememberMe.setChecked(false);
        }
    }

    protected void onResume(){
        super.onResume();
        closeKeyboard();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void closeKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public class UserLoginTask extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject=null;
        String mUserNameView;
        String mPasswordView;
        Boolean rememberme;

        UserLoginTask(String UserName,String mPasswordView,Boolean rememberme){
            this.mUserNameView=UserName;
            this.mPasswordView=mPasswordView;
            this.rememberme=rememberme;
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            handler=new JsonObjectHandler();
            try {
                HashMap<String,String> data=new HashMap<>();
                data.put("username",""+mUserNameView);
                data.put("password",""+mPasswordView);
                data.put("remember",""+rememberme);
                jsonObject=handler.makeHttpRequest(AppConstant.userLoginURL, "POST", data,null);

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
                        JSONObject loginDetails=resultObject.optJSONObject("logindetails");

                        String Id = loginDetails.getString("id");
                        String Name = loginDetails.getString("name");
                        String UserName = loginDetails.getString("username");
                        String MobileNo = loginDetails.getString("mobile");
                        String EmailId = loginDetails.getString("email");
                        String Password = loginDetails.getString("password");
                        String Role = loginDetails.getString("role");
                        String AccessToken = loginDetails.getString("token");
                        String ProfilePic = loginDetails.getString("photo");

                        SharedPreferences.Editor editor=sharedPreferencesLoginDetails.edit();
                        editor.putString("Id", Id);
                        editor.putString("Name", Name);
                        editor.putString("MobileNo", MobileNo);
                        editor.putString("EmailId", EmailId);
                        editor.putString("Role", Role);
                        editor.putString("AccessToken", AccessToken);
                        editor.putString("ProfilePic", ""+ProfilePic);

                        editor.putString("pass", mPasswordView);
                        editor.putString("username", ""+mUserNameView);
                        editor.commit();

                        if (Role.equals("Admin")){
                            Intent intent=new Intent(getApplicationContext(),AdDashboardActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

            }
        }

    }
}