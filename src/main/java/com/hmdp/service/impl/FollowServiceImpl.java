package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        //获取登陆用户
        Long userId = UserHolder.getUser().getId();
        //1.判断是关注还是取关
        if(isFollow){
            //2.关注，新增数据
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            save(follow);
        }else{
            //3.取关，删除
            remove(new LambdaQueryWrapper<Follow>().eq(Follow::getUserId, userId)
                    .eq(Follow::getFollowUserId, followUserId));
        }
        return Result.ok();
    }


    @Override
    public Result isFollow(Long followUserId) {
        //1.获取登陆用户
        Long userId = UserHolder.getUser().getId();
        //2.查询是否关注
        Integer count = lambdaQuery().eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, followUserId).count();
        return Result.ok(count > 0);
    }


}
