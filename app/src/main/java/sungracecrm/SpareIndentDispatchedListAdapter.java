package sungracecrm;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import co.sungracecrm.R;
import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;
import org.json.JSONObject;
import java.util.HashMap;

public class SpareIndentDispatchedListAdapter extends BaseAdapter {
    JsonObjectHandler handler;
    SpareIndentDispachedListFragment spareDispatched;
    FragmentManager fragmentManager;

    SpareIndentDispatchedListAdapter(SpareIndentDispachedListFragment spareDispatched,FragmentManager fragmentManager)
    {
        this.spareDispatched = spareDispatched;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  spareDispatched.spares.size();
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
        TextView sSpareName,sQty,sRemark,sId,scrn;
        Button sStatus;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final SpareIndentDispatchedListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new SpareIndentDispatchedListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) spareDispatched.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spare_indent_dispatched_cell, null);

            holder.sId = convertView.findViewById(R.id.idspare);
            holder.sSpareName = convertView.findViewById(R.id.spare_name);
            holder.sQty = convertView.findViewById(R.id.qty);
            holder.sRemark = convertView.findViewById(R.id.remark);
            holder.sStatus = convertView.findViewById(R.id.statusR);
            holder.scrn=convertView.findViewById(R.id.crn);
            holder.sStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     String Status="Received";
                     new ChangeStatusToRecieved(holder.sId.getText().toString(),Status,holder.scrn.getText().toString()).execute();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (SpareIndentDispatchedListAdapter.ViewHolderItem) convertView.getTag();
        }
        holder.sId.setText(this.spareDispatched.spares.get(position).cId);
        holder.sSpareName.setText(this.spareDispatched.spares.get(position).cSpareName);
        holder.sQty.setText(this.spareDispatched.spares.get(position).cQty);
        holder.sRemark.setText(this.spareDispatched.spares.get(position).cRemark);
        holder.scrn.setText(this.spareDispatched.spares.get(position).CRNno);

        holder.sStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Status="Received";
                new ChangeStatusToRecieved(holder.sId.getText().toString(),Status,holder.scrn.getText().toString()).execute();
            }
        });

        return convertView;
    }
    public class ChangeStatusToRecieved extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String id;
        String status;
        String crn;

        ChangeStatusToRecieved(String ID,String Status, String CRNno){
            this.id=ID;
            this.status=Status;
            this.crn=CRNno;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {

                 HashMap<String,String> data=new HashMap<>();
                 data.put("id",""+id);
                 data.put("status",""+status);
                 data.put("CRN_NO",""+crn);
                 jsonObject = handler.makeHttpRequest(AppConstant.ChangeStatusToReceivedURL,"POST",data,null);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return jsonObject;
        }
        @Override
        protected void onPostExecute(final JSONObject jsonObject){
            try {
                if (jsonObject!=null){
                    JSONObject statusObject=jsonObject.optJSONObject("reqStatus");
                    String status=statusObject.getString("status");
                    String message=statusObject.getString("message");
                    if (statusObject.getBoolean("status")){
                        Toast toast=Toast.makeText(spareDispatched.getContext(),"Item received Successfully",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();
                        HomeFragment fragment=new HomeFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container,fragment)
                                .commit();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(spareDispatched.getContext());
                        builder.setMessage(message)
                                .setTitle("Warning")
                                .setIcon(R.drawable.ic_warning)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert =builder.create();
                        alert.show();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();

            }
        }
    }
}
