package entity;

public class Result {

    // 设置true或者false
    private boolean flag;
    // 自己规定状态码 20000表示成功 20001表示失败
    private Integer code;
    // 提示信息
    private String message;
    // 执行的查询，要返回的数据
    private Object data;

    //服务与服务之间的远程调用需要提供无参的构造方法
    public Result() {
    }

    // 给增删改使用
    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    // 给查询使用
    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
