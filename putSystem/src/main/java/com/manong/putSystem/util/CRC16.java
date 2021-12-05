package com.manong.putSystem.util;


/**
 * Created by lenovo on 2017/12/28.
 */

public class CRC16 {
    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        String crc = Integer.toHexString(CRC);
        for (int j2 = 0; j2 < 4 - crc.length(); j2++) {
            builder.append("0");
        }
        builder.append(crc);

        return builder.toString();
    }


    /**
     * 计算CRC16校验码
     *
     * @param hexString
     *            十六进制字符串
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getCRC(String hexString) {

        StringBuilder builder = new StringBuilder();

        if(hexString == null){
            builder.append("");
        }else{

            byte[] bytes = DataUtil.HexString2Bytes(hexString);

            int CRC = 0x0000ffff;
            int POLYNOMIAL = 0x0000a001;
            int i, j;
            for (i = 0; i < bytes.length; i++) {
                CRC ^= ((int) bytes[i] & 0x000000ff);
                for (j = 0; j < 8; j++) {
                    if ((CRC & 0x00000001) != 0) {
                        CRC >>= 1;
                        CRC ^= POLYNOMIAL;
                    } else {
                        CRC >>= 1;
                    }
                }
            }
            String crc = Integer.toHexString(CRC);
            for (int j2 = 0; j2 < 4 - crc.length(); j2++) {
                builder.append("0");
            }
            builder.append(crc.toUpperCase());

            // 前后2位位置对换高低八位问题
            builder.append(builder.substring(0, 2));
            builder.delete(0, 2);
        }
        return builder.toString();
    }
}
