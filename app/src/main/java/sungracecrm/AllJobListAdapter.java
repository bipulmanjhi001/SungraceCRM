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

public class AllJobListAdapter extends BaseAdapter{
    Context context;
    AllJobListFragment assignedComplaint;
    FragmentManager fragmentManager;

    AllJobListAdapter(AllJobListFragment assignedComplaint, FragmentManager fragmentManager)
    {
        this.assignedComplaint = assignedComplaint;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  assignedComplaint.complaints.size();
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
        RelativeLayout assignedJobListLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final AllJobListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AllJobListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) assignedComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.all_job_cell, null);

            holder.sId = convertView.findViewById(R.id.id_tech);
            holder.sProductCategory = convertView.findViewById(R.id.productCategory_tech);
            holder.sCustomerName = convertView.findViewById(R.id.customerName_tech);
            holder.sCity=convertView.findViewById(R.id.city_tech);
            holder.sDistrict = convertView.findViewById(R.id.district_tech);
            holder.assignedJobListLayout = convertView.findViewById(R.id.assigned_job_list_layout);

            holder.assignedJobListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.sId.getText());

                    JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AllJobListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (AllJobListAdapter.ViewHolderItem) convertView.getTag();
        }
        holder.sId.setText(this.assignedComplaint.complaints.get(position).Id);
        holder.sProductCategory.setText(this.assignedComplaint.complaints.get(position).ProductCategory);
        holder.sCustomerName.setText(this.assignedComplaint.complaints.get(position).CustomerName);
        holder.sCity.setText(this.assignedComplaint.complaints.get(position).mCity);
        holder.sDistrict.setText(this.assignedComplaint.complaints.get(position).mDistrict);

        holder.assignedJobListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());

                JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AllJobListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
