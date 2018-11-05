package org.kennyzhu.arithmetic;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 */
public class WordFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordFilter.class);
    private static final FilterSet set = new FilterSet(); // 存储首字
    private static final Map<Integer, WordNode> nodes = new HashMap<Integer, WordNode>(1024, 1); // 存储节点
    private static final Set<Integer> stopwdSet = new HashSet<>(); // 停顿词

    static {
        try {
            init();
        } catch (Exception e) {
            LOGGER.error("###SensitiveWordFilter init error.Cause：" + e.getMessage(), e);
        }
    }

    private static void init() {
        // 获取敏感词
        addStopWord("!,.,,,#,$,%,&,*,(,),|,?,/,@,\",',;,[,],{,},+,~,-,_,=,^,<,>,！,。,，,￥,（,）,？,、,“,‘,；,【,】,——,……,《, ,》");
    }

    /**
     * 增加停顿词
     *
     * @param stopWords
     */
    private static void addStopWord(String stopWords) {
        try {
            if (StringUtils.isBlank(stopWords)) {
                return;
            }
            List<String> words = Arrays.asList(stopWords.split(","));
            if (!isEmpty(words)) {
                char[] chs;
                for (String curr : words) {
                    chs = curr.toCharArray();
                    for (char c : chs) {
                        stopwdSet.add(charConvert(c));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("#Add stop word error.Cause:" + e.getMessage(), e);
        }
    }

    /**
     * 添加DFA节点
     * 首字符->subNode(其他字符)
     *
     * @param words
     */
    private static void addSensitiveWord(final List<String> words) {
        if (!isEmpty(words)) {
            char[] chs;
            int fchar;//转换后的字符 大小写转换、全角半角转换
            int lastIndex;
            WordNode fnode; // 首字母节点
            for (String curr : words) {//遍历每个敏感词
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                if (!set.contains(fchar)) {// 没有首字定义
                    set.add(fchar);// 首字标志位 可重复add,反正判断了，不重复了
                    fnode = new WordNode(fchar, chs.length == 1);
                    nodes.put(fchar, fnode);
                } else {
                    fnode = nodes.get(fchar);
                    if (!fnode.isLast() && chs.length == 1)
                        fnode.setLast(true);
                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }

    public static final String getSensitiveWord(final String src) {
        StringBuffer target = new StringBuffer();
        if (set != null && nodes != null) {
            char[] chs = src.toCharArray();
            int length = chs.length;
            int currc; // 当前检查的字符
            int cpcurrc; // 当前检查字符的备份
            int k;
            WordNode node;
            for (int i = 0; i < length; i++) {
                currc = charConvert(chs[i]);
                if (!set.contains(currc)) {//不是敏感词
                    continue;
                }
                node = nodes.get(currc);// 日 2
                if (node == null)// 其实不会发生，习惯性写上了
                    continue;
                boolean couldMark = false;
                int markNum = -1;
                if (node.isLast()) {// 单字匹配（日）
                    couldMark = true;
                    markNum = 0;
                }
                k = i;
                cpcurrc = currc; // 当前字符的拷贝
                for (; ++k < length; ) {//后面的字符
                    int temp = charConvert(chs[k]);
                    if (temp == cpcurrc)
                        continue;
                    if (stopwdSet != null && stopwdSet.contains(temp))
                        continue;
                    node = node.querySub(temp);//查找是否在子节点中
                    if (node == null)// 没有了
                        break;
                    if (node.isLast()) {
                        couldMark = true;
                        markNum = k - i;// 3-2
                    }
                    cpcurrc = temp;
                }
                if (couldMark) {
                    for (k = 0; k <= markNum; k++) {
                        target.append(chs[k + i]);
                    }
                    i = i + markNum;
                }
            }
            LOGGER.info("#Target is " + target.toString());
            return new String(chs);
        }

        return src;
    }

    /**
     * @param txt
     * @param sensitiveWord
     * @param target
     * @return
     */
    public static String replace(String txt, String sensitiveWord, String target) {

        Set<String> toReplaceSet = getSensitiveWord(txt, sensitiveWord);
        if (CollectionUtils.isNotEmpty(toReplaceSet)) {
            for (String toReplaceStr : toReplaceSet) {
                txt = txt.replaceAll(toReplaceStr, target);
            }
        }
        return txt;
    }

    /**
     * 过滤判断 将敏感词转化为成屏蔽词
     *
     * @param txt
     * @return
     */
    public static final Set<String> getSensitiveWord(final String txt, String sourceSensitiveWord) {
        if (StringUtils.isBlank(txt) || StringUtils.isBlank(sourceSensitiveWord)) {
            return null;
        }
        addSensitiveWord(Arrays.asList(new String[]{sourceSensitiveWord}));//添加敏感词
        Set<String> result = new HashSet<>();
        if (set != null && nodes != null) {
            char[] chs = txt.toCharArray();
            int length = chs.length;
            int currc; // 当前检查的字符
            int cpcurrc; // 当前检查字符的备份
            int k;
            WordNode node;
            for (int i = 0; i < length; i++) {
                currc = charConvert(chs[i]);
                if (!set.contains(currc)) {//不是敏感词
                    continue;
                }
                node = nodes.get(currc);// 日 2
                if (node == null)// 其实不会发生，习惯性写上了
                    continue;
                boolean couldMark = false;
                int markNum = -1;
                if (node.isLast()) {// 单字匹配
                    couldMark = true;
                    markNum = 0;
                }
                k = i;
                cpcurrc = currc; // 当前字符的拷贝
                for (; ++k < length; ) {//后面的字符
                    int temp = charConvert(chs[k]);
                    if (temp == cpcurrc)
                        continue;
                    if (stopwdSet != null && stopwdSet.contains(temp))
                        continue;
                    node = node.querySub(temp);//查找是否在子节点中
                    if (node == null)// 没有了
                        break;
                    if (node.isLast()) {
                        couldMark = true;
                        markNum = k - i;// 3-2
                    }
                    cpcurrc = temp;
                }
                if (couldMark) {
                    StringBuffer target = new StringBuffer();
                    for (k = 0; k <= markNum; k++) {
                        target.append(chs[k + i]);
                    }
                    result.add(target.toString());
                    i = i + markNum;
                }
            }
            LOGGER.info("#Txt is " + txt + " SenstiveWord is " + sourceSensitiveWord + " Target is " + result.toString());
        }

        return result;
    }

    /**
     * 是否包含敏感词
     *
     * @param src
     * @return
     */
    public static final boolean isContains(final String src) {
        if (set != null && nodes != null) {
            char[] chs = src.toCharArray();
            int length = chs.length;
            int currc; // 当前检查的字符
            int cpcurrc; // 当前检查字符的备份
            int k;
            WordNode node;
            for (int i = 0; i < length; i++) {
                currc = charConvert(chs[i]);
                if (!set.contains(currc)) {
                    continue;
                }
                node = nodes.get(currc);// 日 2
                if (node == null)// 其实不会发生，习惯性写上了
                    continue;
                boolean couldMark = false;
                if (node.isLast()) {// 单字匹配（日）
                    couldMark = true;
                }
                k = i;
                cpcurrc = currc;
                for (; ++k < length; ) {
                    int temp = charConvert(chs[k]);
                    if (temp == cpcurrc)
                        continue;
                    if (stopwdSet != null && stopwdSet.contains(temp))
                        continue;
                    node = node.querySub(temp);
                    if (node == null)// 没有了
                        break;
                    if (node.isLast()) {
                        couldMark = true;
                    }
                    cpcurrc = temp;
                }
                if (couldMark) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 大写转化为小写 全角转化为半角
     *
     * @param src
     * @return
     */
    private static int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }

    /**
     * 判断一个集合是否为空
     *
     * @param col
     * @return
     */
    public static <T> boolean isEmpty(final Collection<T> col) {
        if (col == null || col.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        String source = "你好么hao+评有红包你好么";
        String sensitiveWord = "欢迎下单";
        System.out.println(WordFilter.replace(source, source, sensitiveWord));
//        System.out.println("三椒*蹄".replaceAll("三椒\\*蹄", "三椒美容蹄"));
//        System.out.println("三椒*蹄".contains("*"));
//        if (source.contains("*")) {
//            System.out.println(source.replaceAll("\\*", "美味"));
//        }

//        if (StringUtils.isNotBlank(source) && source.contains("*") && sensitiveWord.contains("*")) {
//            System.out.println("1:" + source.replaceAll("\\*", "美味"));
//            System.out.println("2:" + sensitiveWord.replaceAll("\\*", "\\\\*"));
//        } else {
//        System.out.println(source.replaceAll(source, sensitiveWord));
//        }
    }

}
