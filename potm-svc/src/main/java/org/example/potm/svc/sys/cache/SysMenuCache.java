package org.example.potm.svc.sys.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.potm.svc.sys.model.po.SysMenu;
import org.example.potm.svc.sys.model.po.SysMenuConfig;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.config.redis.RedisCacheConstant;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SysMenuCache {

    private final String MENU_CACHE = RedisCacheConstant.MENU_CACHE;
    private final String MENU_LIST_CACHE = RedisCacheConstant.MENU_LIST_CACHE;

    private final RedissonClient redissonClient;

    public final static Cache<String, SysMenuConfig> LocalCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public final static Cache<String, List<SysMenu>> MenuListLocalCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public void put(SysMenuConfig obj) {
        LocalCache.put(MENU_CACHE, obj);
        redissonClient.getBucket(MENU_CACHE).set(obj);
    }

    public SysMenuConfig load() {
        if(LocalCache.getIfPresent(MENU_CACHE) != null) {
            return LocalCache.getIfPresent(MENU_CACHE);
        }
        return (SysMenuConfig) redissonClient.getBucket(MENU_CACHE).get();
    }

    public void clear() {
        LocalCache.invalidate(MENU_CACHE);
        redissonClient.getBucket(MENU_CACHE).delete();
    }

    public void putList(List<SysMenu> menuList) {
        MenuListLocalCache.put(MENU_LIST_CACHE, menuList);
        redissonClient.getBucket(MENU_LIST_CACHE).set(menuList);
    }

    public List<SysMenu> loadList() {
        if(MenuListLocalCache.getIfPresent(MENU_LIST_CACHE) != null) {
            return MenuListLocalCache.getIfPresent(MENU_LIST_CACHE);
        }
        return (List<SysMenu>) redissonClient.getBucket(MENU_LIST_CACHE).get();
    }

    public void clearList() {
        MenuListLocalCache.invalidate(MENU_LIST_CACHE);
        redissonClient.getBucket(MENU_LIST_CACHE).delete();
    }
}
