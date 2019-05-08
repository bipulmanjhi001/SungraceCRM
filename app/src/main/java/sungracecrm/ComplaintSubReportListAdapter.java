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

public class ComplaintSubReportListAdapter extends BaseAdapter {
    Activity activity;
    JsonObjectHandler handler;
    ComplaintSubReportListFragment complaintSubReport;
    FragmentManager fragmentManager;

    ComplaintSubReportListAdapter(ComplaintSubReportListFragment complaintSubReport,FragmentManager fragmentManager)
    {
        this.complaintSubReport = complaintSubReport;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  complaintSubReport.spares.size();
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
        TextView sDate,sCustomerName,sId;
        RelativeLayout cComplaint_sub_report_layout;
        ImageView eyeReport;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ComplaintSubReportListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new ComplaintSubReportListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) complaintSubReport.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.complaint_sub_report_cell, null);

            holder.sId = convertView.findViewById(R.id.id_spare);
            holder.sDate = convertView.findViewById(R.id.complaintDate);
            holder.sCustomerName = convertView.findViewById(R.id.customer_name);
            holder.eyeReport = convertView.findViewById(R.id.eye);
            holder.cComplaint_sub_report_layout=convertView.findViewById(R.id.complaint_sub_report_layout);
            holder.eyeReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String complaintID = String.valueOf(holder.sId.getText());
                    ComplaintReportDetailsFragment fragment=ComplaintReportDetailsFragment.newInstance(holder.sId.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("ComplaintSubReportListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (ComplaintSubReportListAdapter.ViewHolderItem) convertView.getTag();
        }
        holder.sId.setText(this.complaintSubReport.spares.get(position).cId);
        holder.sDate.setText(this.complaintSubReport.spares.get(position).cDate);
        holder.sCustomerName.setText(this.complaintSubReport.spares.get(position).cCustomerName);

        holder.cComplaint_sub_report_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());
                ComplaintReportDetailsFragment fragment=ComplaintReportDetailsFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("ComplaintSubReportListFragment")
                        .commit();
            }
        });

        holder.eyeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaintID = String.valueOf(holder.sId.getText());
                ComplaintReportDetailsFragment fragment=ComplaintReportDetailsFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("ComplaintSubReportListFragment")
                        .commit();
            }
        });

        return convertView;
    }

}
