package com.manong.putSystem.bean;

import java.util.List;

/**
 * 响应体
 */
public class Result {

    private int code;

    private String msg;

    private List<ResponseBean> response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }


    public static class ResponseBean{

        private boolean isExist;

        public boolean isExist() {
            return isExist;
        }

        public void setExist(boolean exist) {
            isExist = exist;
        }
    }
}
