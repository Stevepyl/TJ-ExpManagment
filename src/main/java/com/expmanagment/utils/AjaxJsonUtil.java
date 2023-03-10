package com.expmanagment.utils;

import java.io.Serializable;
import java.util.List;


/**
 * ajax请求返回Json格式数据的封装
 */
public class AjaxJsonUtil implements Serializable {

    private static final long serialVersionUID = 1L;    // 序列化版本号

    public static final int CODE_SUCCESS = 200;            // 成功状态码
    public static final int CODE_ERROR = 500;            // 错误状态码
    public static final int CODE_WARNING = 501;            // 警告状态码
    public static final int CODE_NOT_JUR = 499;            // 无权限状态码

    public static final int CODE_NOT_LOGIN = 401;        // 未登录状态码

    public static final int CODE_MY_NOT_FOUND = 402;     // 查询结果不存在 / 查询结果列表为空 若不需要更具体的话就抛出这个异常
    public static final int CODE_ALREADY_EXIST = 403;     // 查询结果已存在
    public static final int CODE_USER_NOT_FOUND = 404;     // 用户不存在
    public static final int CODE_COURSE_NOT_FOUND = 405;     // 课程不存在
    public static final int CODE_TASK_NOT_FOUND = 406;     // 实验项目不存在
    public static final int CODE_STUDENT_NOT_FOUND = 407;     // 学生不存在
    public static final int CODE_TEACHER_NOT_FOUND = 408;     // 教师/助教不存在
    public static final int CODE_EMAIL_ALREADY_REGISTERED = 409;     // 邮箱已被注册
    public static final int CODE_FAIL_IN_READING_SUBMIT_INFO = 410;     // 读取学生提交的作业的信息失败
    public static final int CODE_FILE_NAME_ILLEGAL = 411;     // 上传的作业的文件名非法
    public static final int CODE_FILE_STORAGE = 412;     // 文件存储失败
    public static final int CODE_MY_FILE_NOT_FOUND = 413;     // 未找到该文件
    public static final int CODE_PASSWORD_NOT_CORRECT = 414;     // 登录时密码错误
    // 跳过了415是因为415有个传参格式错误的错误信息，不冲突似乎会好一点
    public static final int CODE_ROLE_NOT_MATCH = 416;     // 角色不匹配(如把课程负责教师填成学生)
    public static final int CODE_EXPIRED = 417; // 已过期

    public static final int CODE_INVALID_REQUEST = 400;    // 无效请求状态码

    public int code;    // 状态码
    public String msg;    // 描述信息
    public Object data; // 携带对象
    public Long dataCount;    // 数据总数，用于分页

    public int getCode() {
        return this.code;
    }

    public AjaxJsonUtil setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public AjaxJsonUtil setData(Object data) {
        this.data = data;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(Class<T> cs) {
        return (T) data;
    }

    // ============================  build  ==================================

    public AjaxJsonUtil(int code, String msg, Object data, Long dataCount) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.dataCount = dataCount;
    }

    // 返回成功
    public static AjaxJsonUtil getSuccess() {
        return new AjaxJsonUtil(CODE_SUCCESS, "ok", null, null);
    }

    public static AjaxJsonUtil getSuccess(String msg) {
        return new AjaxJsonUtil(CODE_SUCCESS, msg, null, null);
    }

    public static AjaxJsonUtil getSuccess(String msg, Object data) {
        return new AjaxJsonUtil(CODE_SUCCESS, msg, data, null);
    }

    public static AjaxJsonUtil getSuccessData(Object data) {
        return new AjaxJsonUtil(CODE_SUCCESS, "ok", data, null);
    }

    public static AjaxJsonUtil getSuccessArray(Object... data) {
        return new AjaxJsonUtil(CODE_SUCCESS, "ok", data, null);
    }

    // 返回失败
    public static AjaxJsonUtil getError() {
        return new AjaxJsonUtil(CODE_ERROR, "error", null, null);
    }

    public static AjaxJsonUtil getError(String msg) {
        return new AjaxJsonUtil(CODE_ERROR, msg, null, null);
    }

    // 返回警告
    public static AjaxJsonUtil getWarning() {
        return new AjaxJsonUtil(CODE_ERROR, "warning", null, null);
    }

    public static AjaxJsonUtil getWarning(String msg) {
        return new AjaxJsonUtil(CODE_WARNING, msg, null, null);
    }

    // 返回未登录
    public static AjaxJsonUtil getNotLogin() {
        return new AjaxJsonUtil(CODE_NOT_LOGIN, "未登录，请登录后再次访问", null, null);
    }

    // 返回没有权限的
    public static AjaxJsonUtil getNotJur(String msg) {
        return new AjaxJsonUtil(CODE_NOT_JUR, msg, null, null);
    }

    // 返回一个自定义状态码的
    public static AjaxJsonUtil get(int code, String msg) {
        return new AjaxJsonUtil(code, msg, null, null);
    }

    public static AjaxJsonUtil getMyNotFound(String msg) {
        return new AjaxJsonUtil(CODE_MY_NOT_FOUND, msg, null, null);
    }

    public static AjaxJsonUtil getAlreadyExist(String msg) {
        return new AjaxJsonUtil(CODE_ALREADY_EXIST, msg, null, null);
    }

    public static AjaxJsonUtil getUserNotFound(String msg) {
        return new AjaxJsonUtil(CODE_USER_NOT_FOUND, msg, null, null);
    }

    public static AjaxJsonUtil getCourseNotFound(String msg) {
        return new AjaxJsonUtil(CODE_COURSE_NOT_FOUND, msg, null, null);
    }

    public static AjaxJsonUtil getTaskNotFound(String msg) {
        return new AjaxJsonUtil(CODE_TASK_NOT_FOUND, msg, null, null);
    }

    public static AjaxJsonUtil getStudentNotFound(String msg) {
        return new AjaxJsonUtil(CODE_STUDENT_NOT_FOUND, msg, null, null);
    }

    public static AjaxJsonUtil getTeacherNotFound(String msg) {
        return new AjaxJsonUtil(CODE_TEACHER_NOT_FOUND, msg, null, null);
    }

    public static AjaxJsonUtil getEmailAlreadyRegistered(String msg) {
        return new AjaxJsonUtil(CODE_EMAIL_ALREADY_REGISTERED, msg, null, null);
    }

    public static AjaxJsonUtil getFailInReadingSubmitInfo(String msg) {
        return new AjaxJsonUtil(CODE_FAIL_IN_READING_SUBMIT_INFO, msg, null, null);
    }

    public static AjaxJsonUtil getFileNameIllegal(String msg) {
        return new AjaxJsonUtil(CODE_FILE_NAME_ILLEGAL, msg, null, null);
    }

    public static AjaxJsonUtil getFileStorage(String msg) {
        return new AjaxJsonUtil(CODE_FILE_STORAGE, msg, null, null);
    }

    public static AjaxJsonUtil getMyFileNotFound(String msg) {
        return new AjaxJsonUtil(CODE_MY_FILE_NOT_FOUND, msg, null, null);
    }

    public static AjaxJsonUtil getPasswordNotCorrect(String msg) {
        return new AjaxJsonUtil(CODE_PASSWORD_NOT_CORRECT, msg, null, null);
    }

    public static AjaxJsonUtil getRoleNotMatch(String msg) {
        return new AjaxJsonUtil(CODE_ROLE_NOT_MATCH, msg, null, null);
    }

    public static AjaxJsonUtil getExpired(String msg) {
        return new AjaxJsonUtil(CODE_EXPIRED, msg, null, null);
    }

    public static AjaxJsonUtil getPageData(Long dataCount, Object data) {
        return new AjaxJsonUtil(CODE_SUCCESS, "ok", data, dataCount);
    }

    public static AjaxJsonUtil getByLine(int line) {
        if (line > 0) {
            return getSuccess("ok", line);
        }
        return getError("error").setData(line);
    }

    public static AjaxJsonUtil getByBoolean(boolean b) {
        return b ? getSuccess("ok") : getError("error");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public String toString() {
        String data_string = null;
        if (data == null) {

        } else if (data instanceof List) {
            data_string = "List(length=" + ((List) data).size() + ")";
        } else {
            data_string = data.toString();
        }
        return "{" + "\"code\": " + this.getCode() + ", \"msg\": \"" + this.getMsg() + "\"" + ", \"data\": " + data_string + ", \"dataCount\": " + dataCount + "}";
    }
}
