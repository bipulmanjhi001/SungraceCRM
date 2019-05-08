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

public class AdCompletedComplaintListAdapter extends BaseAdapter {
    Context context;
    AdCompletedComplaintListFragment completedComplaint;
    FragmentManager fragmentManager;

    AdCompletedComplaintListAdapter(AdCompletedComplaintListFragment completedComplaint,FragmentManager fragmentManager)
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
        TextView ProductCategory,CustomerName,City,District,Id;
        RelativeLayout completedComplaintLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final AdCompletedComplaintListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AdCompletedComplaintListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) completedComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ad_completed_complaint_cell, null);

            holder.Id = convertView.findViewById(R.id.id_complaint);
            holder.ProductCategory = convertView.findViewById(R.id.product_category_complaint);
            holder.CustomerName = convertView.findViewById(R.id.customer_name_complaint);
            holder.City=convertView.findViewById(R.id.city_complaint);
            holder.District = convertView.findViewById(R.id.district_complaint);
            holder.completedComplaintLayout = convertView.findViewById(R.id.completed_complaint_list_layout);

            holder.completedComplaintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.Id.getText());

                    AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AdCompletedComplaintListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (AdCompletedComplaintListAdapter.ViewHolderItem) convertView.getTag();
        }


        holder.Id.setText(this.completedComplaint.complaints.get(position).Id);
        holder.ProductCategory.setText(this.completedComplaint.complaints.get(position).ProductCategory);
        holder.CustomerName.setText(this.completedComplaint.complaints.get(position).CustomerName);
        holder.City.setText(this.completedComplaint.complaints.get(position).mCity);
        holder.District.setText(this.completedComplaint.complaints.get(position).mDistrict);

        holder.completedComplaintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.Id.getText());

                AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdCompletedComplaintListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
