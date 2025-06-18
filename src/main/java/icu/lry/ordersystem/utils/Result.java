package icu.lry.ordersystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;// 响应码，1 代表成功; 0 代表失败
    private String msg;  // 响应信息 描述字符串
    private Object data; // 返回的数据
    private Object data1; // 返回的数据

    // 增删改 成功响应
    public static Result success() {
        return new Result(1, "success", null, null);
    }

    // 查询 成功响应
    public static Result success(Object data) {
        return new Result(1, "success", data, null);
    }

    public static Result success1(Object data, Object data2) {
        return new Result(1, "success", data, data2);
    }

    // 失败响应
    public static Result error(String msg) {
        return new Result(0, "error", msg, null);
    }

    public static Result error1(Object data, String msg) {
        return new Result(0, "error", data, msg);
    }
}
