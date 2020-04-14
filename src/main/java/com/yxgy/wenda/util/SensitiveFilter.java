package com.yxgy.wenda.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SensitiveFilter {

    private static final String REPLACEMENT = "***";

    //根结点
    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将一个敏感词添加到前缀树
    private void addKeyword(String keyword) {
        TrieNode tem = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tem.getSubNode(c);
            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                tem.addSubNode(c, subNode);
            }
            tem = subNode;
        }
        //每个单词结束加上标志
        tem.setKeywordEnd(true);
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //指针1
        TrieNode tem = root;
        //指针2
        int begin = 0;
        //指针3
        int pos = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while (pos < text.length()) {
            char c = text.charAt(pos);
            //略过特殊符号
            if (isSymbol(c)) {
                //若指针1处于根结点，将此符号计入结果，让指针2走一步
                if (tem == root) {
                    sb.append(c);
                    begin++;
                }
                //无论特殊符号在开头或中间，指针3都要往下走一步
                pos++;
                continue;
            }

            //检查下级节点
            tem = tem.getSubNode(c);
            if (tem == null) {
                //以begin为前缀的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                pos = ++begin;
                //指针1重新指向根
                tem = root;
            } else if (tem.isKeywordEnd()) {
                //发现敏感词，将begin-pos字符串替换
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++pos;
                //指针1重新指向根
                tem = root;
            } else {
                //检查下一个字符
                pos++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断特殊符号
    private boolean isSymbol(Character c) {
        //0x2E80-0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //前缀树
    private class TrieNode {

        //key是构建敏感词的每个字符
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        //关键词结束标志
        private boolean isKeywordEnd = false;

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }
}
