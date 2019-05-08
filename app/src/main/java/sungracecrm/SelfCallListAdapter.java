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

public class SelfCallListAdapter extends BaseAdapter {
    Activity activity;
    JsonObjectHandler handler;
    SelfCallListFragment selfCall;
    FragmentManager fragmentManager;

    SelfCallListAdapter(SelfCallListFragment selfCall,FragmentManager fragmentManager)
    {
        this.selfCall = selfCall;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  selfCall.selfcalls.size();
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
        TextView ID,ssvn,customerName,date;
        LinearLayout self_call_layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final SelfCallListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new SelfCallListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) selfCall.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.selfcall_cell, null);

            holder.ID = convertView.findViewById(R.id.id);
            holder.ssvn = convertView.findViewById(R.id.ssvnNo);
            holder.customerName = convertView.findViewById(R.id.customerName);
            holder.date = convertView.findViewById(R.id.date);
            holder.self_call_layout = convertView.findViewById(R.id.self_call_layout);

            holder.self_call_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = String.valueOf(holder.ID.getText());

                    SelfCallDetailsFragment fragment=SelfCallDetailsFragment.newInstance(holder.ID.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment)
                            .addToBackStack("SelfCallListFragment")
                            .commit();
                }
            });
            convertView.setTag(holder);

        }
        else
        {
            holder = (SelfCallListAdapter.ViewHolderItem) convertView.getTag();
        }
        holder.ID.setText(this.selfCall.selfcalls.get(position).SSId);
        holder.ssvn.setText(this.selfCall.selfcalls.get(position).SSVNNo);
        holder.customerName.setText(this.selfCall.selfcalls.get(position).SSCustomerName);
        holder.date.setText(this.selfCall.selfcalls.get(position).SSDate);

        holder.self_call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(holder.ID.getText());

                SelfCallDetailsFragment fragment=SelfCallDetailsFragment.newInstance(holder.ID.getText().toString());
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container,fragment)
                        .addToBackStack("SelfCallListFragment")
                        .commit();
            }
        });

        return convertView;
    }

}
