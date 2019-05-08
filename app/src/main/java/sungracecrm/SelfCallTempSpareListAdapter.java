package sungracecrm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.sungracecrm.R;

import sungracecrm.apihandler.AppConstant;
import sungracecrm.apihandler.JsonObjectHandler;

import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SelfCallTempSpareListAdapter extends BaseAdapter {
    Activity activity;
    JsonObjectHandler handler;
    SelfCallFragment technicianAction;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferencesAction;

    SelfCallTempSpareListAdapter(SelfCallFragment technicianAction, FragmentManager fragmentManager)
    {
        this.technicianAction = technicianAction;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return  technicianAction.spares.size();
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
        TextView sId,sSpareName,sQty;
        ImageView sdelete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final SelfCallTempSpareListAdapter.ViewHolderItem holder;
        if (convertView == null) {
            holder = new SelfCallTempSpareListAdapter.ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) technicianAction.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spare_temp_cell, null);

            holder.sId = convertView.findViewById(R.id.spId);
            holder.sSpareName = convertView.findViewById(R.id.spareName);
            holder.sQty = convertView.findViewById(R.id.qty);
            holder.sdelete = convertView.findViewById(R.id.delete);

            holder.sdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(technicianAction.getContext());
                    builder.setMessage("Make sure you want to delete the Item ")
                            .setTitle("Spare Delete?")
                            .setIcon(R.drawable.ic_warning)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new deleteSpare(holder.sId.getText().toString()).execute();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert =builder.create();
                    alert.show();
                }
            });
            convertView.setTag(holder);
        }
        else
        {
            holder = (SelfCallTempSpareListAdapter.ViewHolderItem) convertView.getTag();
        }

        holder.sId.setText(this.technicianAction.spares.get(position).cId);
        holder.sSpareName.setText(this.technicianAction.spares.get(position).cSpareName);
        holder.sQty.setText(this.technicianAction.spares.get(position).cQty);

        holder.sdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(technicianAction.getContext());
                builder.setMessage("Make sure you want to delete the Item ")
                        .setTitle("Spare Delete?")
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new deleteSpare(holder.sId.getText().toString()).execute();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert =builder.create();
                alert.show();



            }
        });

        return convertView;
    }

    public class deleteSpare extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject =null;
        String id;

        deleteSpare(String ID){
            this.id=ID;
        }
        @Override
        protected JSONObject doInBackground(Void... voids){
            handler = new JsonObjectHandler();
            try {

                HashMap<String,String> data=new HashMap<>();
                data.put("id",""+id);
                jsonObject = handler.makeHttpRequest(AppConstant.deleteSpareFromAction,"POST",data,null);

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
                        Toast toast=Toast.makeText(technicianAction.getContext(),"Item deleted successfully",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0,0);
                        View view=toast.getView();
                        TextView  view1=(TextView)view.findViewById(android.R.id.message);
                        view1.setTextColor(Color.WHITE);
                        view1.setTextSize(18);
                        view1.setPadding(10,5,10,5);
                        view.setBackgroundResource(R.color.colorAccent);
                        toast.show();
                        sharedPreferencesAction=technicianAction.getActivity().getSharedPreferences("action",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferencesAction.edit();
                        editor.putString("spareList","OK");
                        editor.commit();

                        FragmentManager fragmentManager = technicianAction.getFragmentManager();
                        SelfCallFragment fragment = new SelfCallFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();

                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(technicianAction.getContext());
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