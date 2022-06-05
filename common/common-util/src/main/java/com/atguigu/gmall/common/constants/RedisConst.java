package com.atguigu.gmall.common.constants;

public class RedisConst {

    public static final String SKUDETAIL_LOCK_PREFIX = "lock:detail:"; //lock:detail:50

    public static final String LOCK_PREFIX = "lock";

    public static final String CATEGORY_CACHE_KEY = "categorys";

    public static final String SKU_CACHE_KEY_PREFIX = "sku:detail:";
    public static final String BLOOM_SKU_ID = "bloom:skuId";

    public static final String SALE_ATTR_CACHE_KEY = "sale:attr";
    public static final String SKU_HOTSCORE = "sku:hotscore";
    public static final String USER_LOGIN_PREFIX = "user:login:";
    public static final String CART_KEY_PREFIX = "user:cart:";
}
