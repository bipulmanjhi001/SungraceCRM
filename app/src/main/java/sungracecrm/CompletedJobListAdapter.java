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

public class CompletedJobListAdapter extends BaseAdapter {
    Context context;
    CompletedJobListFragment completedComplaint;
    FragmentManager fragmentManager;

    CompletedJobListAdapter(CompletedJobListFragment completedComplaint,FragmentManager fragmentManager)
    {
        this.completedComplaint = completedComplaint;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  completedComplaint.complaints.size();
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
        RelativeLayout completedJobListLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final CompletedJobListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new CompletedJobListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) completedComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.completed_job_cell, null);

            holder.sId = convertView.findViewById(R.id.id_tech);
            holder.sProductCategory = convertView.findViewById(R.id.productCategory_tech);
            holder.sCustomerName = convertView.findViewById(R.id.customerName_tech);
            holder.sCity=convertView.findViewById(R.id.city_tech);
            holder.sDistrict = convertView.findViewById(R.id.district_tech);
            holder.completedJobListLayout = convertView.findViewById(R.id.completed_job_list_layout);

            holder.completedJobListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.sId.getText());

                    JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("CompletedJobListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (CompletedJobListAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.sId.setText(this.completedComplaint.complaints.get(position).Id);
        holder.sProductCategory.setText(this.completedComplaint.complaints.get(position).ProductCategory);
        holder.sCustomerName.setText(this.completedComplaint.complaints.get(position).CustomerName);
        holder.sCity.setText(this.completedComplaint.complaints.get(position).mCity);
        holder.sDistrict.setText(this.completedComplaint.complaints.get(position).mDistrict);

        holder.completedJobListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());

                JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("CompletedJobListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
