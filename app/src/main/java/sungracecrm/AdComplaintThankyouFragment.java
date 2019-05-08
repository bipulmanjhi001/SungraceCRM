package sungracecrm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import co.sungracecrm.R;

import static android.content.Context.MODE_PRIVATE;


public class AdComplaintThankyouFragment extends Fragment {
    TextView crnNo;
    String CUID,CRN;
    Button addNewComplaint,backToDashboard;
    private SharedPreferences sharedPreferencesCustomerDetails;
    public AdComplaintThankyouFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_complaint_thankyou, container, false);
        crnNo=view.findViewById(R.id.complaint_no);
        addNewComplaint=view.findViewById(R.id.add_new_complaint_btn);
        backToDashboard=view.findViewById(R.id.add_back_to_dashboard_btn);

        sharedPreferencesCustomerDetails= getContext().getSharedPreferences("customerDetails", MODE_PRIVATE);
        CUID=sharedPreferencesCustomerDetails.getString("CUID","");
        CRN=sharedPreferencesCustomerDetails.getString("CRN","");

        crnNo.setText(CRN);
        addNewComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdCustomerComplaintRegisterFragment fragment = new AdCustomerComplaintRegisterFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                        .addToBackStack("AdComplaintThankyouFragment")
                        .commit();
            }
        });
        backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                AdDashboardFragment fragment = new AdDashboardFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment)
                        .addToBackStack("AdComplaintThankyouFragment")
                        .commit();
            }
        });

        return view;
    }
}
