package com.ricky.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ricky.campus.model.entity.SensitiveWord;
import java.util.Map;

public interface SensitiveWordService extends IService<SensitiveWord> {

    /**
     * 新增敏感词（管理员）
     */
    void addWord(String word);

    /**
     * 删除敏感词（管理员）
     */
    void deleteWord(Long id);

    /**
     * 校验文本是否含敏感词
     * @return { hasSensitive: boolean, word: string|null }
     */
    Map<String, Object> checkText(String text);
}
