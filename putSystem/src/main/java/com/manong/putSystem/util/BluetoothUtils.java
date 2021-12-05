package com.manong.putSystem.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.manong.putSystem.activity.BleConnActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothUtils {
	private static final String TAG = "BluetoothUtils";
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	public static BluetoothGatt bluetoothGatt;
	private Context mContext;


	public BluetoothUtils(Context context) {
		this.mContext = context;
		mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
	}

	/**
	 * 开始扫描设备
	 * @param uuids 可选项，该项为扫描指定的服务，uuids为指定服务的uuid集合
	 */
	@SuppressWarnings("deprecation")
	public void startScan(UUID[] uuids){
		if (uuids == null || uuids.length < 1) {
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		}else {
			mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
		}
	}

	/**
	 * 停止扫描
	 */
	@SuppressWarnings("deprecation")
	public void stopScan() {
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
	}

	/**
	 * 连接设备，注册连接回调方法
	 * @param device 要连接的蓝牙设备
	 */
	public void connect(BluetoothDevice device){
		if (bluetoothGatt != null) {
			bluetoothGatt.close();
		}
		bluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
	}

	public void disconnect() {
		if (mBluetoothAdapter == null || bluetoothGatt == null) {
			return;
		}
		bluetoothGatt.disconnect();
		bluetoothGatt.close();
	}

	/**
	 * 发现设备服务，回调方法onServicesDiscovered
	 */
	public void discoverServices(){
		bluetoothGatt.discoverServices();
	}


	private LeScanCallback mLeScanCallback = new LeScanCallback() {

		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			// 不符合规则的都不要
			if(device == null || device.getName() == null || !device.getName().startsWith("Hoist_Ble")){
				return;
			}

			Message msg = new Message();
			msg.what = Constants.FIND_NEW_DEVICE;
			Bundle bundle = new Bundle();
			// 如果已经存在则忽略
			String flag = BleConnActivity.deviceCheck.get(device.getAddress());
			if(flag != null){
				return;
			}
			BleConnActivity.deviceCheck.put(device.getAddress(), device.getAddress());
			bundle.putParcelable(Constants.NEW_DEVICE, device);
			msg.setData(bundle);
			BleConnActivity.myHandler.sendMessage(msg);
		}
	};


	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		//通过通知来获取改变的数据
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);
			Message msg = new Message();
			msg.what = Constants.CHARACTERISTIC_NOTIFY;
			Bundle bundle = new Bundle();
			bundle.putByteArray("value", characteristic.getValue());
			msg.setData(bundle);
			BleConnActivity.myHandler.sendMessage(msg);

		}


		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);
			Message msg = new Message();
			msg.what = Constants.CHARACTERISTIC_READ;
			Bundle bundle = new Bundle();
			bundle.putByteArray("value", characteristic.getValue());
			msg.setData(bundle);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				//发送下一条数据
			}
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			super.onConnectionStateChange(gatt, status, newState);
			System.out.println(System.currentTimeMillis() + "=================================连接状态: " + newState);
			switch (newState) {

				// 连接成功
				case BluetoothProfile.STATE_CONNECTED:

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// 获取服务
					bluetoothGatt.discoverServices();

					break;

			}

		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			Log.i(TAG, "onReadRemoteRssi");
			super.onReadRemoteRssi(gatt, rssi, status);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);
			List<BluetoothGattService> bluetoothGattServices = gatt.getServices();
			if(bluetoothGattServices != null && bluetoothGattServices.size() > 0){
				if(BleConnActivity.services == null){
					BleConnActivity.services = new ArrayList<>();
				}
				BleConnActivity.services = bluetoothGattServices;
			}
			Log.i(TAG, "设备:"+ gatt.getDevice().getName() +"发现了 "+bluetoothGattServices.size()+" 个服务");
			Message msg = new Message();
			msg.what = Constants.FIND_SERVICES;
			BleConnActivity.myHandler.sendMessage(msg);
		}
	};


	/**
	 * 发现服务下的特征信息
	 * @param //需要发现特征信息的服务
	 * @return 
	 */
	public List<BluetoothGattCharacteristic> discoverCharacteristic(UUID uuid) {
		BluetoothGattService service = bluetoothGatt.getService(uuid);
		List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
		Log.i(TAG, "服务: "+service.getUuid().toString()+" 发现了 "+characteristics.size()+"个特征信息");
		return characteristics;
	}

	/**
	 * 根据服务ID,特征ID得到特征对象
	 * @param serviceUUID
	 * @param characterUUID
	 * @return
	 */
	public BluetoothGattCharacteristic getCharacteristic(UUID serviceUUID, UUID characterUUID) {
		return bluetoothGatt.getService(serviceUUID).getCharacteristic(characterUUID);
	}
	
	/**
	 * 调用特征读取方法，读取特征数据，数据从回调中返回 onCharacteristicRead
	 * @param serviceUUID
	 * @param characterUUID
	 */
    public void readCharacteristic(UUID serviceUUID, UUID characterUUID){
    	bluetoothGatt.readCharacteristic(getCharacteristic(serviceUUID, characterUUID));
    }
    
    /**
     * 给某个特征写入值
     * @param serviceUUID
     * @param characterUUID
     * @param value
     */
    public void writeCharacteristic(UUID serviceUUID, UUID characterUUID, byte[] value){
    	BluetoothGattCharacteristic characteristic = getCharacteristic(serviceUUID, characterUUID);
		characteristic.setValue(value);
		bluetoothGatt.writeCharacteristic(characteristic);
    }

    /**
     * 发送notify命令，会隔几秒通过onCharacteristicChange返回改特征的值
     * @param serviceUUID
     * @param characterUUID
     * @param flag  开关值,true开启,false关闭
     */
    public void setNotify(UUID serviceUUID, UUID characterUUID, boolean flag){
    	BluetoothGattCharacteristic characteristic = getCharacteristic(serviceUUID, characterUUID);
		if (mBluetoothAdapter == null || bluetoothGatt == null) {
			return;
		}
		if (characteristic.getDescriptor(UUID
				.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG)) != null) {
			if (flag == true) {
				BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
						.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
				descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				bluetoothGatt.writeDescriptor(descriptor);
			} else {
				BluetoothGattDescriptor descriptor = characteristic
						.getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
				descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
				bluetoothGatt.writeDescriptor(descriptor);
			}
		}
		bluetoothGatt.setCharacteristicNotification(characteristic, flag);
    }

	public final static String DEVICE_DOES_NOT_SUPPORT_UART = "com.jimmy.ble.DEVICE_DOES_NOT_SUPPORT_UART";
	public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
	public static final UUID RX_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public static final UUID TX_CHAR_UUID = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb");

	public void enableTXNotification() {

		if (bluetoothGatt == null) {
			broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
			return;
		}

		BluetoothGattService RxService = bluetoothGatt.getService(RX_SERVICE_UUID);
		if (RxService == null) {
			broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
			return;
		}
		BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_UUID);
		if (TxChar == null) {
			broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
			return;
		}
		bluetoothGatt.setCharacteristicNotification(TxChar, true);

		BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
		descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		bluetoothGatt.writeDescriptor(descriptor);
	}

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
	}

	/**
	 * 改变BLE默认的单次发包、收包的最大长度,用于android 5.0及以上版本
	 * @param mtu
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static boolean requestMtu(int mtu){
		if (bluetoothGatt != null) {
			return bluetoothGatt.requestMtu(mtu);
		}
		return false;
	}
}
