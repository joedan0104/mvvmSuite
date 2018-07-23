package com.jindan.network.entity;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/8/10 14:33
 * <p/>
 * Description:
 */
public class Params {
    public static final String RES_CODE = "error_code";
    public static final String RES_PAGE = "page";
    public static final String RES_MSG = "message";
    public static final String RES_DATA = "data";

    public static final int RES_OK = 0; // 正常
    public static final int RES_SUCCEED = 200;
    public static final int Error = -1; // 错误
    public static final int PWD_ERROR = 401;
    public static final int PWD_OVERLIMIT = 402;
    public static final int JSON_PARSE_ERROR = 403;

    /** 登录状态失效(重新登录) */
    public static final int LOGOUT_ERROR = 1000;
    /** 手机号不正确 */
    public static final int PHONE_NUMBER_ERROR = 1001;
    /** 密码不正确 */
    public static final int PASSWORD_ERROR = 1002;
    /** 用户未注册 Alert弹窗 */
    public static final int NOT_REGISTER = 1003;
    /** 统一挡板弹窗 */
    public static final int BAFFLE_ERROR = 1005;
    /** 手机号已经被注册 */
    public static final int PHONE_NUMBER_OCCUPY = 1006;
    /** 用户不存在 */
    public static final int USER_NOT_EXISTS = 1009;
    /** 请输入正确的验证码/验证码过期 */
    public static final int VERIFY_CODE_ERROR = 1010;

    /** 参数错误 */
    public static final int PARAMETER_ERROR = 1100;
    /** 邀请码错误 */
    public static final int INVITATION_CODE_ERROR = 1300;
    /** 微信未关联用户账号 */
    public static final int WECHAT_NO_ACCOUNT = 4004;
    /** 需要设置自动投标 */
    public static final int NO_SET_AUTO_INVEST = 4008;
    /** 通用信息提交存在未完成的必须项 */
    public static final int SAVE_CONFIRM = 4010;

    /** 大额提现受限 */
    public static final int LARGE_WITHDRAW_LIMIT_ERROR = 4009;
    /** 交易密码未验证/错误 */
    public static final int SAFE_PWD_ERROR = 5002;
    /** 账户被锁定 Alert */
    public static final int ACCOUNT_LOCK = 6001;

    /** 服务器特殊挡板弹窗(需要跳转到服务器指定的错误页面) */
    public static final int NET_ERROR_DIALOG = 9999;

    // 网络错误码定义
    public static final int NET_TIME_OUT = 90000; // 网络超时
    public static final int NET_DATA_ERROR = 90001; // 网络传输数据错误
    public static final int NET_GENERAL_ERROR = 90002; // 网络通用错误标识
    public static final int NET_NEED_BUY = 90003; // 网络超时
    public static final int NET_SSL_ERROR = 90004; // SSL验证错误
    public static final int NET_NONE = 90005; // 本地网络未连接
    public static final int UNKNOWN_HOST_ERROR = 90007; // 本地网络未连接
}
