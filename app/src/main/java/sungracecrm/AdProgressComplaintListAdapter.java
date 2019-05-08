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

public class AdProgressComplaintListAdapter extends BaseAdapter{
    Context context;
    AdProgressComplaintListFragment progressComplaint;
    FragmentManager fragmentManager;

    AdProgressComplaintListAdapter(AdProgressComplaintListFragment progressComplaint,FragmentManager fragmentManager)
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
        TextView ProductCategory,CustomerName,City,District,Id;
        RelativeLayout progressComplaintLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final AdProgressComplaintListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AdProgressComplaintListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) progressComplaint.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ad_progress_complaint_cell, null);

            holder.Id = convertView.findViewById(R.id.id_complaint);
            holder.ProductCategory = convertView.findViewById(R.id.product_category_complaint);
            holder.CustomerName = convertView.findViewById(R.id.customer_name_complaint);
            holder.City=convertView.findViewById(R.id.city_complaint);
            holder.District = convertView.findViewById(R.id.district_complaint);
            holder.progressComplaintLayout = convertView.findViewById(R.id.progress_complaint_list_layout);

            holder.progressComplaintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.Id.getText());

                    AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AdProgressComplaintListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (AdProgressComplaintListAdapter.ViewHolderItem) convertView.getTag();
        }
        holder.Id.setText(this.progressComplaint.complaints.get(position).Id);
        holder.ProductCategory.setText(this.progressComplaint.complaints.get(position).ProductCategory);
        holder.CustomerName.setText(this.progressComplaint.complaints.get(position).CustomerName);
        holder.City.setText(this.progressComplaint.complaints.get(position).mCity);
        holder.District.setText(this.progressComplaint.complaints.get(position).mDistrict);

        holder.progressComplaintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.Id.getText());

                AdComplaintDetailsFragment fragment=AdComplaintDetailsFragment.newInstance(holder.Id.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdProgressComplaintListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
