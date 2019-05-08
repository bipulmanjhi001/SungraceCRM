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

public class SpareIndentListAdapter extends BaseAdapter{
    Activity activity;
    JsonObjectHandler handler;
    SparesIndentListFragment spareComplaint;
    FragmentManager fragmentManager;

    SpareIndentListAdapter(SparesIndentListFragment spareComplaint,FragmentManager fragmentManager)
    {
        this.spareComplaint = spareComplaint;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  spareComplaint.spares.size();
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
        TextView sCRNno,sCustomerName,sId,complaintReport,complaintId,technicianId;
        RelativeLayout spareComplaintListLayout;
        ImageView list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final SpareIndentListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new SpareIndentListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) spareComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spare_indent_complaint_cell, null);
            holder.sId = convertView.findViewById(R.id.id_spare);
            holder.sCRNno = convertView.findViewById(R.id.crnNo);
            holder.sCustomerName = convertView.findViewById(R.id.customer_name);
            holder.spareComplaintListLayout = convertView.findViewById(R.id.spare_indent_complaint_layout);
            holder.list = convertView.findViewById(R.id.list);
            holder.complaintReport = convertView.findViewById(R.id.complaint_report);
            holder.complaintId = convertView.findViewById(R.id.complaint_Id);
            holder.technicianId = convertView.findViewById(R.id.technician_Id);
            holder.list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.sId.getText());
                    SparesIndentListByCRNFragment fragment=SparesIndentListByCRNFragment.newInstance("0",holder.complaintId.getText().toString(),holder.sCRNno.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("SparesIndentListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (SpareIndentListAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.sId.setText(this.spareComplaint.spares.get(position).cId);
        holder.sCRNno.setText(this.spareComplaint.spares.get(position).CRNno);
        holder.sCustomerName.setText(this.spareComplaint.spares.get(position).cCustomerName);
        holder.complaintId.setText(this.spareComplaint.spares.get(position).cId);
        holder.technicianId.setText(this.spareComplaint.spares.get(position).TechnicianID);

        holder.spareComplaintListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());

                SparesIndentListByCRNFragment fragment=SparesIndentListByCRNFragment.newInstance("0",holder.complaintId.getText().toString(),holder.sCRNno.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("SparesIndentListFragment")
                        .commit();
            }
        });

        holder.list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());

                SparesIndentListByCRNFragment fragment=SparesIndentListByCRNFragment.newInstance("0",holder.complaintId.getText().toString(),holder.sCRNno.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("SparesIndentListFragment")
                        .commit();
            }
        });

        return convertView;
    }

}
