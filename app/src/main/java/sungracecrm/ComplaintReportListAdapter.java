package sungracecrm;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.sungracecrm.R;

import sungracecrm.apihandler.JsonObjectHandler;

public class ComplaintReportListAdapter extends BaseAdapter {
    Activity activity;
    JsonObjectHandler handler;
    ComplaintReportListFragment complaintReport;
    FragmentManager fragmentManager;

    ComplaintReportListAdapter(ComplaintReportListFragment complaintReport,FragmentManager fragmentManager)
    {
        this.complaintReport = complaintReport;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  complaintReport.spares.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolderItem {
        TextView sCRNno,sCustomerName,sId;
        RelativeLayout cComplaint_report_layout;
        ImageView eyeReport;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ComplaintReportListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new ComplaintReportListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) complaintReport.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.complaint_report_cell, null);

            holder.sId = convertView.findViewById(R.id.id_spare);
            holder.sCRNno = convertView.findViewById(R.id.crnNo);
            holder.sCustomerName = convertView.findViewById(R.id.customer_name);
            holder.eyeReport = convertView.findViewById(R.id.eye);
            holder.cComplaint_report_layout=convertView.findViewById(R.id.complaint_report_layout);
            holder.eyeReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String complaintID = String.valueOf(holder.sId.getText());

                    ComplaintSubReportListFragment fragment=ComplaintSubReportListFragment.newInstance(holder.sId.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("ComplaintReportListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (ComplaintReportListAdapter.ViewHolderItem) convertView.getTag();
        }
        holder.sId.setText(this.complaintReport.spares.get(position).cId);
        holder.sCRNno.setText(this.complaintReport.spares.get(position).CRNno);
        holder.sCustomerName.setText(this.complaintReport.spares.get(position).cCustomerName);

        holder.cComplaint_report_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());

                ComplaintSubReportListFragment fragment=ComplaintSubReportListFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("ComplaintReportListFragment")
                        .commit();
            }
        });

        holder.eyeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaintID = String.valueOf(holder.sId.getText());
                ComplaintSubReportListFragment fragment=ComplaintSubReportListFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("ComplaintReportListFragment")
                        .commit();
            }
        });

        return convertView;
    }

}
