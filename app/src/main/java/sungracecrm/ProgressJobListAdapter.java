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

public class ProgressJobListAdapter extends BaseAdapter {
    Context context;
    ProgressJobListFragment progressComplaint;
    FragmentManager fragmentManager;

    ProgressJobListAdapter(ProgressJobListFragment progressComplaint,FragmentManager fragmentManager)
    {
        this.progressComplaint = progressComplaint;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  progressComplaint.complaints.size();
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
        RelativeLayout progressJobListLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ProgressJobListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new ProgressJobListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) progressComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.progress_job_cell, null);

            holder.sId = convertView.findViewById(R.id.id_tech);
            holder.sProductCategory = convertView.findViewById(R.id.productCategory_tech);
            holder.sCustomerName = convertView.findViewById(R.id.customerName_tech);
            holder.sCity=convertView.findViewById(R.id.city_tech);
            holder.sDistrict = convertView.findViewById(R.id.district_tech);
            holder.progressJobListLayout = convertView.findViewById(R.id.progress_job_list_layout);

            holder.progressJobListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.sId.getText());

                    JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .disallowAddToBackStack()
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (ProgressJobListAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.sId.setText(this.progressComplaint.complaints.get(position).Id);
        holder.sProductCategory.setText(this.progressComplaint.complaints.get(position).ProductCategory);
        holder.sCustomerName.setText(this.progressComplaint.complaints.get(position).CustomerName);
        holder.sCity.setText(this.progressComplaint.complaints.get(position).mCity);
        holder.sDistrict.setText(this.progressComplaint.complaints.get(position).mDistrict);

        holder.progressJobListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.sId.getText());

                JobDetailsFragment fragment=JobDetailsFragment.newInstance(holder.sId.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .disallowAddToBackStack()
                        .commit();
            }
        });
        return convertView;
    }
}

