package musixise.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_LOCAL = "local";
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_NO_SWAGGER = "no-swagger";
    // Spring profile used to disable running liquibase
    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String QINIU_IMG_DOMAIN = "http://oaeyej2ty.bkt.clouddn.com/%s";
    public static final String QINIU_AUDIO_DOMAIN = "http://oiqvdjk3s.bkt.clouddn.com/%s";

    private Constants() {
    }


    //系统错误
    public static final Integer ERROR_CODE_SYSTEM = 10001;//系统错误

    //通用错误
    public static final Integer ERROR_CODE_APPLICATION = 20000;//应用程序错误
    public static final Integer ERROR_CODE_PARAMS = 20001;//传入参数错误
    public static final Integer ERROR_CODE_NO_LOGIN = 20002;//用户未登陆
    public static final Integer ERROR_CODE_LOGIN_FAIL = 20003;//用户登陆失败

    //用户相关
    public static final Integer ERROR_CODE_USER_NOT_BIND = 30001;//未绑定的账号
    public static final Integer ERROR_CODE_CREATE_USER_ACCOUNT_FAIL = 30002;//创建账号失败
    public static final Integer ERROR_CODE_SOCIAL_PLATFORM_NOT_EXIST = 30003;//不存在的平台标识
    public static final Integer ERROR_CODE_USER_AUTH_FAIL = 30004;//认证失败, 用户名或密码错误
    public static final Integer ERROR_CODE_USERNAME_ALREADY_USED = 30005;//用户名已存在
    public static final Integer ERROR_CODE_EMAIL_ALREADY_USED = 30006;//邮箱已存在
    public static final Integer ERROR_CODE_THIRD_ALREADY_BIND = 30007;//第三方账号已绑定
    public static final Integer ERROR_CODE_THIRD_BIND_CONFLICT = 30008;//同一个平台只能绑定一个账号
    public static final Integer ERROR_CODE_USER_NOT_FOUND = 30009;//用户不存在

    //作品相关
    public static final Integer ERROR_CODE_WORK_NOT_EXIST = 40001;//作品不存在

    //收藏
    public static final Integer ERROR_CODE_FAVORITE_WORK_NOT_EXIST = 50001;//该作品不存在或已删除
    public static final Integer ERROR_CODE_FAVORITE_ALREADY_ADD = 50002;//已收藏过

    //关注
    public static final Integer ERROR_CODE_USER_ALREADY_FOLLOW = 60000;//已关注

    //微信
    public static final Integer ERROR_CODE_WECHAT_GET_TICKET_FAIL = 70001;//获取微信TICKET失败
}
