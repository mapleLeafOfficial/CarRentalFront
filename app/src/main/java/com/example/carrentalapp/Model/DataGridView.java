package com.example.carrentalapp.Model;

/**
 * @author luoyi-
 */
public class DataGridView<C> {
    @Override
    public String toString() {
        return "DataGridView{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }

    /**
     * 封装LayUI数据表格的数据对象
     */
    private Integer code=0;
    private String msg="";
    private Long count;
    private Object data;

    public DataGridView() {
    }

    public DataGridView(Object data) {
        super();
        this.data = data;
    }

    public DataGridView(Long count, Object data) {
        super();
        this.count = count;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
