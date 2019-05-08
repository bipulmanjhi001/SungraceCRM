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

public class AdPendingComplaintListAdapter extends BaseAdapter {
    Context context;
    AdPendingComplaintListFragment pendingComplaint;
    FragmentManager fragmentManager;

    AdPendingComplaintListAdapter(AdPendingComplaintListFragment pendingComplaint,FragmentManager fragmentManager)
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
        TextView ProductCategory,CustomerName,City,District,Id;
        RelativeLayout pendingComplaintLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final AdPendingComplaintListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AdPendingComplaintListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) pendingComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ad_pending_complaint_cell, null);

            holder.Id = convertView.findViewById(R.id.id_complaint);
            holder.ProductCategory = convertView.findViewById(R.id.product_category_complaint);
            holder.CustomerName = convertView.findViewById(R.id.customer_name_complaint);
            holder.City=convertView.findViewById(R.id.city_complaint);
            holder.District = convertView.findViewById(R.id.district_complaint);
            holder.pendingComplaintLayout = convertView.findViewById(R.id.pending_complaint_list_layout);

            holder.pendingComplaintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.Id.getText());

                    AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AdPendingComplaintListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (AdPendingComplaintListAdapter.ViewHolderItem) convertView.getTag();
        }


        holder.Id.setText(this.pendingComplaint.complaints.get(position).Id);
        holder.ProductCategory.setText(this.pendingComplaint.complaints.get(position).ProductCategory);
        holder.CustomerName.setText(this.pendingComplaint.complaints.get(position).CustomerName);
        holder.City.setText(this.pendingComplaint.complaints.get(position).mCity);
        holder.District.setText(this.pendingComplaint.complaints.get(position).mDistrict);

        holder.pendingComplaintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.Id.getText());

                AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdPendingComplaintListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
