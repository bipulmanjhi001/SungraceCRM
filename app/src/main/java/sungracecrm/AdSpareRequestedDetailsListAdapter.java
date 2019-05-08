package sungracecrm;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.sungracecrm.R;

import sungracecrm.apihandler.JsonObjectHandler;

public class AdSpareRequestedDetailsListAdapter extends BaseAdapter {
    Activity activity;
    JsonObjectHandler handler;
    AdSpareRequestedDetailsListFragment spareFragment;
    FragmentManager fragmentManager;
    AdSpareRequestedDetailsListAdapter(AdSpareRequestedDetailsListFragment spareFragment,FragmentManager fragmentManager){
        this.spareFragment=spareFragment;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  spareFragment.spares.size();
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
        TextView spareName,qqty,ddate,remarks;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final AdSpareRequestedDetailsListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new AdSpareRequestedDetailsListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) spareFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ad_spare_requested_details_cell, null);

            holder.spareName = convertView.findViewById(R.id.spare_name);
            holder.qqty = convertView.findViewById(R.id.qty);
            holder.ddate = convertView.findViewById(R.id.date);
            holder.remarks = convertView.findViewById(R.id.remark);

            convertView.setTag(holder);

        }
        else
        {
            holder = (AdSpareRequestedDetailsListAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.spareName.setText(this.spareFragment.spares.get(position).cSpareName);
        holder.qqty.setText(this.spareFragment.spares.get(position).cQty);
        holder.ddate.setText(this.spareFragment.spares.get(position).cDate);
        holder.remarks.setText(this.spareFragment.spares.get(position).cRemark);


        return convertView;
    }

}
