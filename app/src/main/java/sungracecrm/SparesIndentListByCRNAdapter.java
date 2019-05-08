package sungracecrm;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.sungracecrm.R;
import sungracecrm.apihandler.JsonObjectHandler;


public class SparesIndentListByCRNAdapter extends BaseAdapter {
    JsonObjectHandler handler;
    SparesIndentListByCRNFragment spareByCRN;
    FragmentManager fragmentManager;

    SparesIndentListByCRNAdapter(SparesIndentListByCRNFragment spareByCRN,FragmentManager fragmentManager)
    {
        this.spareByCRN = spareByCRN;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  spareByCRN.spares.size();
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
        TextView sSpareName,sQty,sRemark,sId,sStatus;
        ImageView edit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final SparesIndentListByCRNAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new SparesIndentListByCRNAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) spareByCRN.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spare_indent_by_crn_cell, null);

            holder.sId = convertView.findViewById(R.id.idspare);
            holder.sSpareName = convertView.findViewById(R.id.spare_name);
            holder.sQty = convertView.findViewById(R.id.qty);
            holder.sRemark = convertView.findViewById(R.id.remark);
            holder.sStatus = convertView.findViewById(R.id.status);


            convertView.setTag(holder);

        }
        else
        {
            holder = (SparesIndentListByCRNAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.sId.setText(this.spareByCRN.spares.get(position).cId);
        holder.sSpareName.setText(this.spareByCRN.spares.get(position).cSpareName);
        holder.sQty.setText(this.spareByCRN.spares.get(position).cQty);
        holder.sRemark.setText(this.spareByCRN.spares.get(position).cRemark);
        holder.sStatus.setText(this.spareByCRN.spares.get(position).cStatus);

        return convertView;
    }

}
