package org.example.potm.svc.sys.cache;

import cn.dev33.satoken.stp.StpUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.example.potm.svc.sys.model.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.example.potm.framework.config.redis.RedisCacheConstant;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SysUserCache {

    private final RedissonClient redissonClient;

    public final static Cache<String, SysUserVO> LocalCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public void put(SysUserVO user) {
        LocalCache.put(buildUserCacheName(user.getId()), user);
        redissonClient.getBucket(buildUserCacheName(user.getId())).set(user);
    }

    public SysUserVO get(Long userId) {
        if(LocalCache.getIfPresent(buildUserCacheName(userId)) != null) {
            return LocalCache.getIfPresent(buildUserCacheName(userId));
        }
        return (SysUserVO) redissonClient.getBucket(buildUserCacheName(userId)).get();
    }

    public void clear(Long userId) {
        LocalCache.invalidate(buildUserCacheName(userId));
        redissonClient.getBucket(buildUserCacheName(userId)).delete();
        StpUtil.logout(userId);
    }

    private String buildUserCacheName(Long userId) {
        return RedisCacheConstant.USER_CACHE.formatted(userId);
    }
}
