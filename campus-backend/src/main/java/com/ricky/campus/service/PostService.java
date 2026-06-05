package com.ricky.campus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ricky.campus.model.dto.PostCreateDTO;
import com.ricky.campus.model.dto.PostQueryDTO;
import com.ricky.campus.model.entity.Post;
import com.ricky.campus.model.vo.PostVO;

public interface PostService extends IService<Post> {

    /**
     * 帖子列表（分类筛选 + 置顶优先 + 分页）
     */
    Page<PostVO> getPostList(PostQueryDTO query);

    /**
     * 帖子详情
     */
    Post getPostDetail(Long postId);

    /**
     * 发帖（校验敏感词，校验公告权限）
     * @param postCreateDTO 前端表单数据
     * @param studentId 发帖人学号（从请求参数获取，不可伪造）
     */
    Post createPost(PostCreateDTO postCreateDTO, String studentId);

    /**
     * 删帖（本人或管理员）
     */
    void deletePost(Long postId, String studentId);

    /**
     * 置顶/取消置顶（管理员）
     */
    void togglePin(Long postId);

    /**
     * 点赞/取消点赞（toggle）
     */
    void toggleLike(Long postId, String studentId);

    /**
     * 收藏/取消收藏（toggle）
     */
    void toggleFavorite(Long postId, String studentId);

    /**
     * 我的收藏列表
     */
    Page<PostVO> getMyFavorites(String studentId, Integer page, Integer size);
}
