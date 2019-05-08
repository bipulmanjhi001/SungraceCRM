package sungracecrm;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.sungracecrm.R;
import sungracecrm.apihandler.JsonObjectHandler;

public class AdSpareRequestedListAdapter extends BaseAdapter {
    Activity activity;
    JsonObjectHandler handler;
    AdSpareRequestedListFragment spareRequested;
    FragmentManager fragmentManager;

    AdSpareRequestedListAdapter(AdSpareRequestedListFragment spareRequested,FragmentManager fragmentManager)
    {
        this.spareRequested = spareRequested;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  spareRequested.spares.size();
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
        TextView sCRNno,sCustomerName,complaintId,technicianName;
        LinearLayout spareRequestedListLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final AdSpareRequestedListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AdSpareRequestedListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) spareRequested.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ad_spare_requestlist_cell, null);

            holder.complaintId = convertView.findViewById(R.id.complaintID);
            holder.sCRNno = convertView.findViewById(R.id.crnNo);
            holder.sCustomerName = convertView.findViewById(R.id.customerName);
            holder.technicianName=convertView.findViewById(R.id.engineerName);
            holder.spareRequestedListLayout=convertView.findViewById(R.id.spare_request_layout);
            holder.spareRequestedListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdSpareRequestedDetailsListFragment fragment=AdSpareRequestedDetailsListFragment.newInstance(holder.complaintId.getText().toString(),holder.sCRNno.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("AdSpareRequestedListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (AdSpareRequestedListAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.complaintId.setText(this.spareRequested.spares.get(position).cId);
        holder.sCRNno.setText(this.spareRequested.spares.get(position).CRNno);
        holder.sCustomerName.setText(this.spareRequested.spares.get(position).cCustomerName);
        holder.technicianName.setText(this.spareRequested.spares.get(position).cTechnicianName);

        holder.spareRequestedListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.complaintId.getText());

                AdSpareRequestedDetailsListFragment fragment=AdSpareRequestedDetailsListFragment.newInstance(holder.complaintId.getText().toString(),holder.sCRNno.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("AdSpareRequestedListFragment")
                        .commit();
            }
        });

        return convertView;
    }
}

