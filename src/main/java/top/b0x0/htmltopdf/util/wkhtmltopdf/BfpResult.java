package top.b0x0.htmltopdf.util.wkhtmltopdf;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class BfpResult implements Serializable {

    private Integer status;
    private String msg;
    private Object data;

    public static BfpResult build(Integer status, String msg, Object data) {
        return new BfpResult(status, msg, data);
    }

    public static BfpResult build(Integer status) {
        return new BfpResult(status, null, null);
    }

    public static BfpResult ok(Object data) {
        return new BfpResult(data);
    }

    public static BfpResult ok() {
        return new BfpResult(null);
    }

    /**
     * 添加构造方法
     *
     * @param status  /
     * @param message /
     * @return /
     */
    public static BfpResult fail(Integer status, String message) {
        return new BfpResult(status, message, null);
    }

    public static BfpResult fail(String failMsg) {
        return new BfpResult(400, failMsg, null);
    }

    public BfpResult() {
    }

    public static BfpResult build(Integer status, String msg) {
        return new BfpResult(status, msg, null);
    }

    private BfpResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private BfpResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
