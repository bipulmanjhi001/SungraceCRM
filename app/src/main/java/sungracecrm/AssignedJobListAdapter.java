package sungracecrm;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.sungracecrm.R;

public class AssignedJobListAdapter extends BaseAdapter {
    Context context;
    AssignedJobListFragment pendingComplaint;
    FragmentManager fragmentManager;

    AssignedJobListAdapter(AssignedJobListFragment pendingComplaint, FragmentManager fragmentManager)
    {
        this.pendingComplaint = pendingComplaint;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  pendingComplaint.complaints.size();
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
        TextView sProductCategory,sCustomerName,sCity,sDistrict,sId;
        RelativeLayout pendingJobListLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final AssignedJobListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AssignedJobListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) pendingComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.assigned_job_cell, null);

            holder.sId = convertView.findViewById(R.id.id_tech);
            holder.sProductCategory = convertView.findViewById(R.id.productCategory_tech);
            holder.sCustomerName = convertView.findViewById(R.id.customerName_tech);
            holder.sCity=convertView.findViewById(R.id.city_tech);
            holder.sDistrict = convertView.findViewById(R.id.district_tech);
            holder.pendingJobListLayout = convertView.findViewById(R.id.pending_job_list_layout);

            holder.pendingJobListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.sId.getText());

                    JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AssignedJobListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (AssignedJobListAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.sId.setText(this.pendingComplaint.complaints.get(position).Id);
        holder.sProductCategory.setText(this.pendingComplaint.complaints.get(position).ProductCategory);
        holder.sCustomerName.setText(this.pendingComplaint.complaints.get(position).CustomerName);
        holder.sCity.setText(this.pendingComplaint.complaints.get(position).mCity);
        holder.sDistrict.setText(this.pendingComplaint.complaints.get(position).mDistrict);

        holder.pendingJobListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());

                JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AssignedJobListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
