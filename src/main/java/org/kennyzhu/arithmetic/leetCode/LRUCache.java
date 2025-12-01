package org.kennyzhu.arithmetic.leetCode;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU Cache (Least Recently Used Cache)
 * 使用 HashMap + 双向链表实现
 * HashMap: 保证 O(1) 时间复杂度的查找
 * 双向链表: 维护访问顺序，头部是最少使用的，尾部是最近使用的
 */
public class LRUCache {
    int capacity; // 缓存容量
    Map<String, Node> map; // 存储key到节点的映射，实现O(1)查找
    private Node head; // 链表头节点，指向最少使用的元素(LRU)
    private Node tail; // 链表尾节点，指向最近使用的元素(MRU)

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    /**
     * 双向链表节点
     * 用于维护访问顺序，支持O(1)时间复杂度的插入和删除
     */
    @Data
    static class Node {
        private String key;   // 键
        private String value; // 值
        private Node pre;     // 前驱节点
        private Node next;    // 后继节点

        public Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }


    /**
     * 添加或更新元素
     * 时间复杂度: O(1)
     * 
     * 情况1: 元素已存在
     *   - 更新值
     *   - 移动到队尾（标记为最近使用）
     * 
     * 情况2: 元素不存在
     *   - 如果缓存已满: 删除队首元素(最少使用)，从map中移除
     *   - 添加新节点到队尾
     *   - 更新map映射
     */
    public void put(String key, String value) {
        Node node = map.get(key); // O(1) 查找
        if (node != null) { // 键已存在
            node.value = value; // 更新值
            moveNodeToTail(node); // 移到尾部，标记为最近使用
            return;
        } else { // 键不存在，需要插入新节点
            Node newNode = new Node(key, value);
            if (map.size() == capacity) { // 缓存已满，需要淘汰
                Node delNode = removeFirst(); // 删除头节点(最少使用的)
                map.remove(delNode.key); // 从map中移除该键
            }
            addToList(newNode); // 添加到链表尾部
            map.put(key, newNode); // 更新map映射
        }
    }

  


    /**
     * 将节点移动到队尾（标记为最近使用）
     * 时间复杂度: O(1)
     * 
     * 处理三种情况:
     * 1. 节点已在队尾: 无需操作
     * 2. 节点在队头: 更新head指针，断开原有连接
     * 3. 节点在中间: 调整前后节点的指针，跳过当前节点
     * 最后将节点添加到队尾
     *
     * @param node 要移动的节点
     */
    public void moveNodeToTail(Node node) {
        if (tail == node) { // 已经是队尾，无需移动
            return;
        }
        
        // 第一步: 从原位置断开节点
        if (node == head) { // 节点在队头
            head = node.next; // head指向下一个节点
            head.pre = null;  // 新head的前驱为null
        } else { // 节点在中间
            node.next.pre = node.pre; // 后继节点的前驱指向当前节点的前驱
            node.pre.next = node.next; // 前驱节点的后继指向当前节点的后继
        }
        
        // 第二步: 将节点添加到队尾
        node.pre = tail;  // 当前节点的前驱指向原尾节点
        node.next = null; // 当前节点的后继为null
        tail.next = node; // 原尾节点的后继指向当前节点
        tail = node;      // 更新tail指针
    }

    /**
     * 添加新节点到链表尾部
     * 时间复杂度: O(1)
     * 
     * 处理两种情况:
     * 1. 链表为空: 新节点既是head也是tail
     * 2. 链表不为空: 将新节点添加到tail后面
     *
     * @param node 要添加的节点
     */
    public void addToList(Node node) {
        if (head == null) { // 链表为空
            head = node; // 第一个节点既是头也是尾
        } else { // 链表不为空
            node.pre = tail;  // 新节点的前驱指向当前尾节点
            tail.next = node; // 当前尾节点的后继指向新节点
        }
        tail = node; // 更新tail指针
    }

    /**
     * 删除头节点（删除最少使用的元素）
     * 时间复杂度: O(1)
     * 
     * 处理三种情况:
     * 1. 链表为空: 返回null
     * 2. 链表只有一个节点: head和tail都置为null
     * 3. 链表有多个节点: head指向下一个节点
     *
     * @return 被删除的节点，如果链表为空则返回null
     */
    public Node removeFirst() {
        if (head == null) { // 链表为空
            return null;
        }
        
        Node res = head; // 保存要删除的节点
        
        if (head == tail) { // 链表只有一个节点
            head = null;
            tail = null;
        } else { // 链表有多个节点
            head = res.next;  // head指向下一个节点
            head.pre = null;  // 新head的前驱为null
            res.next = null;  // 断开被删除节点的连接
        }
        
        return res; // 返回被删除的节点
    }

      /**
     * 获取缓存中的值
     * 时间复杂度: O(1)
     * 
     * @param key 键
     * @return 如果存在返回对应的值，不存在返回null
     */
      public String get(String key) {
        Node node = map.get(key); // O(1) 查找
        if (node != null) {
            moveNodeToTail(node); // 移到尾部，更新为最近使用
            return node.value;
        }
        return null; // 键不存在
    }
}
