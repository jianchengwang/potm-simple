package org.example.potm.framework.config.redis;

/**
 * @author jianchengwang
 * @date 2023/3/31
 */
public class RedisCacheConstant {
    public final static long DEFAULT_EXPIRE_TIME = 7 * 24 * 60 * 60;

    public final static String USER_CACHE = "user_%s";
    public final static String USER_ROLES_CACHE = "user_%s_roles";
    public final static String USER_PERMISSIONS_CACHE = "user_%s_permissions";

    public final static String DICT_CACHE = "dict_cache";
    public final static String DEPT_CACHE = "dept_cache";
    public static final String MENU_CACHE = "menu_cache";

    public final static String MENU_LIST_CACHE = "menu_list_cache";
    public final static String METRIC_CACHE = "metric_cache";
}
