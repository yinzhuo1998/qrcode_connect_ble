package com.manong.putSystem.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.manong.putSystem.R;

import java.util.List;


/**
 * 主页设备列表
 */

public class MainListDialogAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    @SuppressWarnings("unused")
    private Context mContext = null;
    private List<BluetoothDevice> devices = null;

    public MainListDialogAdapter(Context context, List<BluetoothDevice> devices){
        this.mContext = context;
        this.devices = devices;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setDevices(List<BluetoothDevice> devices) {
        this.devices = devices;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != devices){
            count = devices.size();
        }
        return count;
    }

    @Override
    public BluetoothDevice getItem(int position) {
        BluetoothDevice device = null;
        if (null != devices && 0 != devices.size()){
            device = devices.get(position);
        }
        return device;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dialog_main_activity_item,null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_dialog_main_equipment_name);
            holder.address = (TextView) convertView.findViewById(R.id.tv_dialog_main_equipment_id);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        BluetoothDevice device = getItem(position);
        if (null != device){
            if ((device.getName().startsWith("Hoist_Ble") == true)){
                String name = device.getName().replace("Hoist_Ble","智能启闭机");
                holder.name.setText(name);
            }else{
                holder.name.setText(device.getName());
            }
            holder.address.setText(device.getAddress());
        }
        return convertView;
    }

    public final class ViewHolder{
        public TextView name;
        public TextView address;
    }



}
