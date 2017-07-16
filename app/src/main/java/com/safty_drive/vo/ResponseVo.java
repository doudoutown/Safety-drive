package com.safty_drive.vo;

/**
 * Created by fanwenbin on 2017/7/9.
 */

public class ResponseVo<E> {
    public String status;
    public E result;
    public Integer error_code;
    public String error_msg;

    private ResponseVo(String status, E result, Integer error_code, String error_msg) {
        this.status = status;
        this.result = result;
        this.error_code = error_code;
        this.error_msg = error_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public final static <E> ResponseVo<E> SUCCESS(E result) {
        return new ResponseVo("success", result, null, null);
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", result=" + result +
                ", error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                '}';
    }
}
