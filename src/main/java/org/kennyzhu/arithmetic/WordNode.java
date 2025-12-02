package org.kennyzhu.arithmetic;

import java.util.HashMap;
import java.util.Map;


/**
 * DFA（确定有限状态自动机）敏感词过滤树的节点类
 * 
 * 数据结构：树形结构（Trie树变体）
 * 用途：敏感词过滤
 * 
 * 设计思想：
 * - 每个节点代表一个字符
 * - 子节点列表存储后续可能的字符
 * - isLast标记该节点是否为某个敏感词的结尾
 * 
 * 示例：敏感词["日本", "日本人"]
 * 树结构：
 *   '日'(isLast=false)
 *    └─'本'(isLast=true)
 *       └─'人'(isLast=true)
 * 
 * 优势：
 * - 快速匹配：O(m)，m为文本长度
 * - 节省空间：公共前缀共享节点
 */
public class WordNode {

    private int value;  // 节点值（字符的int表示）

    private Map<Integer, WordNode> subNodes;  // 子节点映射（优化：List → Map，查找从 O(n) 到 O(1)）

    private boolean isLast;  // 是否为敏感词的最后一个字符，默认false

    /**
     * 构造函数：创建节点
     * 
     * @param value 节点值
     */
    public WordNode(int value) {
        this.value = value;
    }

    /**
     * 构造函数：创建节点并指定是否为结尾
     * 
     * @param value  节点值
     * @param isLast 是否为敏感词结尾
     */
    public WordNode(int value, boolean isLast) {
        this.value = value;
        this.isLast = isLast;
    }

    /**
     * 添加子节点（私有方法）
     * 
     * @param subNode 要添加的子节点
     * @return 返回添加的子节点
     */
    private WordNode addSubNode(final WordNode subNode) {
        if (subNodes == null)
            subNodes = new HashMap<>();  // 懒加载：首次使用时创建
        subNodes.put(subNode.value, subNode);  // 使用 Map.put 替代 List.add
        return subNode;
    }

    /**
     * 添加子节点（如果不存在）
     * 
     * 核心逻辑：
     * 1. 如果子节点映射为空，直接创建新节点
     * 2. 如果子节点中已存在该值，返回已存在的节点
     * 3. 如果不存在，创建新节点并添加
     * 
     * 特殊处理：
     * - 如果节点已存在但isLast=false，而新的isLast=true
     * - 需要更新isLast状态（处理"日本"和"日本人"同时存在的情况）
     * 
     * 性能优化：使用 Map，查找从 O(n) 优化到 O(1)
     * 
     * @param value  子节点的值
     * @param isLast 是否为敏感词结尾
     * @return 子节点（新创建的或已存在的）
     */
    public WordNode addIfNoExist(final int value, final boolean isLast) {
        if (subNodes == null) {
            // 子节点映射为空，直接创建新节点
            return addSubNode(new WordNode(value, isLast));
        }
        
        // 使用 Map.get() 查找，O(1) 时间复杂度
        WordNode subNode = subNodes.get(value);
        if (subNode != null) {
            // 找到已存在的节点
            if (!subNode.isLast && isLast)
                subNode.isLast = true;  // 更新isLast状态
            return subNode;
        }
        
        // 不存在，创建新节点
        return addSubNode(new WordNode(value, isLast));
    }

    /**
     * 查询子节点
     * 
     * 用途：在匹配过程中，根据下一个字符查找对应的子节点
     * 
     * 性能优化：使用 Map.get()，从 O(n) 优化到 O(1)
     * 
     * @param value 要查找的字符值
     * @return 找到返回对应节点，否则返回null
     */
    public WordNode querySub(final int value) {
        // 使用 Map.get() 直接查找，O(1) 时间复杂度
        return subNodes == null ? null : subNodes.get(value);
    }

    /**
     * 判断是否为敏感词结尾
     * 
     * @return true: 是结尾，false: 不是结尾
     */
    public boolean isLast() {
        return isLast;
    }

    /**
     * 设置是否为敏感词结尾
     * 
     * @param isLast 是否为结尾
     */
    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    /**
     * 重写hashCode方法
     * 使用节点值作为hash值
     * 
     * @return hash值
     */
    @Override
    public int hashCode() {
        return value;
    }

}