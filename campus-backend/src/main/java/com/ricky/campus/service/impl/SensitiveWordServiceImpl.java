package com.ricky.campus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ricky.campus.mapper.SensitiveWordMapper;
import com.ricky.campus.model.entity.SensitiveWord;
import com.ricky.campus.service.SensitiveWordService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {

    @Override
    public void addWord(String word) {
        SensitiveWord sw = new SensitiveWord();
        sw.setWord(word);
        this.save(sw);
    }

    @Override
    public void deleteWord(Long id) {
        this.removeById(id);
    }

    @Override
    public Map<String, Object> checkText(String text) {
        List<SensitiveWord> words = this.list();
        for (SensitiveWord sw : words) {
            if (text.contains(sw.getWord())) {
                Map<String, Object> result = new HashMap<>();
                result.put("hasSensitive", true);
                result.put("word", sw.getWord());
                return result;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("hasSensitive", false);
        result.put("word", null);
        return result;
    }
}
