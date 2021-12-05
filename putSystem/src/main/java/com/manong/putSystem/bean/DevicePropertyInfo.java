package com.manong.putSystem.bean;

/**
 * 设备信息
 */
public class DevicePropertyInfo {

    /** UID */
    private String uid;

    /** 版本号 */
    private String version;

    /** iccid */
    private String iccid;

    /** imei */
    private String imei;

    /** 蓝牙mac */
    private String ble_mac;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBle_mac() {
        return ble_mac;
    }

    public void setBle_mac(String ble_mac) {
        this.ble_mac = ble_mac;
    }
}
