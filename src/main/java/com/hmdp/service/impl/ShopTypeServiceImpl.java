package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;
import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_TTL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result getList() {
        //1.从redis查询商铺缓存
        String shopTypeKey = CACHE_SHOP_TYPE_KEY;
        List<String> shopTypeJson = stringRedisTemplate.opsForList().range(shopTypeKey, 0, -1);
        //2.判断是否存在
        if (!shopTypeJson.isEmpty()) {
            //3.存在，直接返回
            List<ShopType> shopTypeList = new ArrayList<>();
            for (String s : shopTypeJson) {
                ShopType shopType = JSONUtil.toBean(s, ShopType.class);
                shopTypeList.add(shopType);
            }

            return Result.ok(shopTypeList);
        }
        //4.不存在，根据id查询数据库
        List<ShopType> shopTypeList = lambdaQuery().orderByAsc(ShopType::getSort).list();
        //5.不存在，返回错误
        if (shopTypeList.isEmpty()) {
            return Result.fail("不存在分类！");
        }
        //6.存在，写入redis
        for (ShopType shopType : shopTypeList) {
            String s = JSONUtil.toJsonStr(shopType);
            shopTypeJson.add(s);
        }
        stringRedisTemplate.opsForList().rightPushAll(shopTypeKey, shopTypeJson);
        stringRedisTemplate.expire(shopTypeKey, CACHE_SHOP_TYPE_TTL, TimeUnit.MINUTES);
        //7.返回
        return Result.ok(shopTypeList);
    }
}
