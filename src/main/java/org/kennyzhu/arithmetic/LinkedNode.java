package org.kennyzhu.arithmetic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 链表节点类
 * 
 * 数据结构：双向链表节点
 * - 支持前向和后向遍历
 * - 适用于需要双向访问的场景
 * 
 * 应用场景：
 * - LRU缓存
 * - 浏览器历史记录
 * - 文本编辑器的撤销/重做功能
 */
@Data
@Slf4j
public class LinkedNode {
    private int val;           // 节点值
    private LinkedNode next;   // 后继节点指针
    private LinkedNode prev;   // 前驱节点指针（修复命名：pre → prev）

    /**
     * 默认构造函数
     */
    public LinkedNode() {
    }

    /**
     * 带值构造函数
     * 
     * @param val 节点值
     */
    public LinkedNode(int val) {
        this.val = val;
    }

    /**
     * 根据整数列表构建单向链表
     * 
     * 实现过程：
     * 1. 遍历输入列表
     * 2. 为每个元素创建节点
     * 3. 依次连接节点，形成链表
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param input 整数列表
     * @return 链表的头节点
     */
    public static LinkedNode buildLinkedNode(List<Integer> input) {
        LinkedNode root = new LinkedNode();
        
        // 边界检查：输入为空
        if (CollectionUtils.isEmpty(input)) {
            return root;
        }
        
        LinkedNode currentNode = null;
        
        // 遍历输入列表，构建链表
        for (Integer item : input) {
            LinkedNode node = new LinkedNode(item);
            
            if (currentNode == null) {
                // 第一个节点，设为头节点
                root = node;
                currentNode = root;
            } else {
                // 后续节点，连接到当前节点之后
                currentNode.setNext(node);
                currentNode = node;
            }
        }
        return root;
    }

    /**
     * 打印链表所有节点的值
     * 
     * 输出格式：1,2,3,4,5
     * 
     * 实现：
     * 1. 从头节点开始遍历
     * 2. 收集每个节点的值
     * 3. 用逗号分隔
     * 
     * 时间复杂度：O(n)
     * 
     * @param root 链表头节点
     */
    public static void printLinkedList(LinkedNode root) {
        // 边界检查：链表为空
        if (root == null) {
            log.info("LinkedList have no node.");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        
        // 遍历链表，收集所有节点的值
        do {
            sb.append(root.getVal()).append(",");
            root = root.getNext();  // 移动到下一个节点
        } while (root != null);
        
        // 去掉最后一个逗号，输出结果
        log.info(sb.substring(0, sb.length() - 1));
    }

    /**
     * 测试方法
     * 
     * 演示：构建并打印链表 [1,2,3,4,5,9]
     */
    public static void main(String[] args) {
        LinkedNode.printLinkedList(
            LinkedNode.buildLinkedNode(Arrays.asList(1, 2, 3, 4, 5, 9))
        );
    }


}
