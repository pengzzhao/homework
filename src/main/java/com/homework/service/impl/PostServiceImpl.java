package com.homework.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homework.entity.Post;
import com.homework.entity.User;
import com.homework.mapper.PostMapper;
import com.homework.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lv-success
 * @since 2018-10-14
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void join(Map<String, Object> map, String field) {
        Map<String, Object> joinColumns = new HashMap<>();

        if(map == null || map.get(field) == null) {
            return;
        }

        //字段的值
        String linkfieldValue = map.get(field).toString();

        Post post = this.getById(linkfieldValue);

        if(post == null) return;

        joinColumns.put("id", post.getId());
        joinColumns.put("title", post.getTitle());
        joinColumns.put("created", DateUtil.formatDate(post.getCreated()));

        map.put("post", joinColumns);
    }

    /**
     * 初始化首页的周评论排行榜
     */
    @Override
    public void initIndexWeekRank() {
        //缓存最近7天的文章评论数量
        List<Post> last7DayPosts = this.list(new QueryWrapper<Post>()
                .ge("created", DateUtil.offsetDay(new Date(), -7).toJdkDate())
                .select("id, title, user_id, comment_count, view_count, created"));

        for (Post post : last7DayPosts) {
            String key = "day_rank:" + DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_PATTERN);

            //设置有效期
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            //缓存文章到set中，评论数量作为排行标准
            redisUtil.zSet(key, post.getId(), post.getCommentCount());

            //缓存文章基本信息（hash结构）
            redisUtil.hset("rank_post_" + post.getId(), "post:id", post.getId(), expireTime);
            redisUtil.hset("rank_post_" + post.getId(), "post:title", post.getTitle(), expireTime);
            //redisUtil.hset("rank_post_" + post.getId(), "post:comment_count", post.getCommentCount(), expireTime);

            redisUtil.expire(key, expireTime);
        }

        //7天阅读相加。
        redisUtil.zUnionAndStoreLast7DaysForLastWeekRank();
    }
}
