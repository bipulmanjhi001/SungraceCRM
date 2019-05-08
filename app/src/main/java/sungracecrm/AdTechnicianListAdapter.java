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

public class AdTechnicianListAdapter extends BaseAdapter {
    Context context;
    AdTechnicianListFragment adTechnician;
    FragmentManager fragmentManager;

    AdTechnicianListAdapter(AdTechnicianListFragment adTechnician,FragmentManager fragmentManager)
    {
        this.adTechnician = adTechnician;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  adTechnician.adTechnicians.size();
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
        TextView Fullname,UserName,Password,Id;
        RelativeLayout technicianLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final AdTechnicianListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AdTechnicianListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) adTechnician.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ad_technician_cell, null);

            holder.Id = convertView.findViewById(R.id.id_tech);
            holder.Fullname = convertView.findViewById(R.id.fullname_tech);
            holder.UserName = convertView.findViewById(R.id.mobile_tech);
            holder.Password=convertView.findViewById(R.id.state_tech);
            holder.technicianLayout = convertView.findViewById(R.id.technician_list_layout);

            holder.technicianLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.Id.getText());

                    AdTechnicianDetailsFragment fragment=AdTechnicianDetailsFragment.newInstance(holder.Id.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AdTechnicianListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (AdTechnicianListAdapter.ViewHolderItem) convertView.getTag();
        }


        holder.Id.setText(this.adTechnician.adTechnicians.get(position).Id);
        holder.Fullname.setText(this.adTechnician.adTechnicians.get(position).Fullname);
        holder.UserName.setText(this.adTechnician.adTechnicians.get(position).UserName);
        holder.Password.setText(this.adTechnician.adTechnicians.get(position).Password);

        holder.technicianLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.Id.getText());

                AdTechnicianDetailsFragment fragment=AdTechnicianDetailsFragment.newInstance(holder.Id.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdTechnicianListFragment")
                        .commit();
            }
        });
        return convertView;
    }
}
