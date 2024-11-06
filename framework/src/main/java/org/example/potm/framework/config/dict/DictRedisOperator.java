package org.example.potm.framework.config.dict;

import org.example.potm.framework.config.permission.PermissionConstant;
import org.example.potm.framework.config.redis.RedisCacheConstant;
import org.redisson.api.*;

import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/11
 */
public class DictRedisOperator {

    private final String DICT_CACHE = RedisCacheConstant.DICT_CACHE;

    private final RedissonClient redissonClient;

    public DictRedisOperator(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public List<SysDict> loadAll() {
        return (List<SysDict>) redissonClient.getBucket(DICT_CACHE).get();
    }

    public void putAll(List<SysDict> dictList) {
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());
        transaction.getBucket(DICT_CACHE).set(dictList);
        transaction.commit();
    }

    public void clear() {
        redissonClient.getBucket(DICT_CACHE).delete();
    }
}
