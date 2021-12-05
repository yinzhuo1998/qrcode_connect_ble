package com.manong.putSystem.util;


import com.manong.putSystem.bean.DevicePropertyInfo;

/**
 * @author: admin
 * @date: 2020/6/18
 * @Explain: 二维码解析工具类
 */
public class QrCodeUtil {


    public static DevicePropertyInfo deviceQrCodeHandle(String data){

        // 数据为空, 数据长度不够28则直接视为数据错误
        if(data == null || data.isEmpty() || data.length() < 28){

            return null;

        }

        DevicePropertyInfo result = new DevicePropertyInfo();

        // 先简单的校验一下设备UID是否以01-08开头
        String substring = data.substring(0, 2);

        switch (substring){

            // 满足条件
            case "01":
            case "02":
            case "03":
            case "04":
            case "05":
            case "06":
            case "07":
            case "08":
                break;

            // 不满足条件直接返回空
            default:
                return null;

        }

        // 如果数据长度只有28 说明只有UID信息
        if(data.length() == 28){

            result.setUid(data);

            return result;

        }

        // 到这说明有其他数据, 根据横杠分割解析
        int index = data.indexOf("-");

        if(index < 0){

            // 说明数据不符合规则
            return null;

        }

        // 将数据分割
        String[] split = data.split("-");

        if(split.length != 5){

            return null;

        }

        String uid = split[0];
        String version = split[1];
        String iccid = split[2];
        String imei = split[3];
        String mac = split[4];
        StringBuilder builder = new StringBuilder();
        builder.append(mac.substring(0, 2));
        builder.append(":");
        builder.append(mac.substring(2, 4));
        builder.append(":");
        builder.append(mac.substring(4, 6));
        builder.append(":");
        builder.append(mac.substring(6, 8));
        builder.append(":");
        builder.append(mac.substring(8, 10));
        builder.append(":");
        builder.append(mac.substring(10, 12));

        result.setUid(uid);
        result.setVersion(version);
        result.setIccid(iccid);
        result.setImei(imei);
        result.setBle_mac(builder.toString());

        return result;

    }


}
