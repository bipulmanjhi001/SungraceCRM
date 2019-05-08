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

public class AdNewComplaintListAdapter extends BaseAdapter {
    Context context;
    AdNewComplaintListFragment adNewComplaint;
    FragmentManager fragmentManager;

    AdNewComplaintListAdapter(AdNewComplaintListFragment adNewComplaint,FragmentManager fragmentManager)
    {
        this.adNewComplaint = adNewComplaint;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  adNewComplaint.complaints.size();
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
        TextView ProductCategory,CustomerName,District,City,Id;
        RelativeLayout newComplaintList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final AdNewComplaintListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AdNewComplaintListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) adNewComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ad_new_complaint_cell, null);

            holder.Id = convertView.findViewById(R.id.id);
            holder.ProductCategory = convertView.findViewById(R.id.product_category);
            holder.CustomerName = convertView.findViewById(R.id.customer_name);
            holder.District=convertView.findViewById(R.id.district);
            holder.City = convertView.findViewById(R.id.city);
            holder.newComplaintList = convertView.findViewById(R.id.new_complaint_list_layout);

            holder.newComplaintList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.Id.getText());

                    AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AdNewComplaintListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (AdNewComplaintListAdapter.ViewHolderItem) convertView.getTag();
        }


        holder.Id.setText(this.adNewComplaint.complaints.get(position).Id);
        holder.ProductCategory.setText(this.adNewComplaint.complaints.get(position).ProductCategory);
        holder.CustomerName.setText(this.adNewComplaint.complaints.get(position).CustomerName);
        holder.City.setText(this.adNewComplaint.complaints.get(position).mCity);
        holder.District.setText(this.adNewComplaint.complaints.get(position).mDistrict);

        holder.newComplaintList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.Id.getText());

                AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdNewComplaintListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
