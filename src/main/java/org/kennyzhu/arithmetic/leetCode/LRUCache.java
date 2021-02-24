package org.kennyzhu.arithmetic.leetCode;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * LRU Cache
 */
public class LRUCache {
    int capacity;
    Map<String, Node> map;//存储KV对
    private Node head;//头
    private Node tail;//尾

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    @Data
    static class Node {
        private String key;
        private String value;
        private Node pre;
        private Node next;

        public Node(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }


    /**
     * 添加元素
     * 1.元素存在，放到队尾
     * 2.不存在，判断链表是否满。
     * 如果满，则删除队首元素，放入队尾元素，删除更新哈希表
     * 如果不满，放入队尾元素，更新哈希表
     */
    public void put(String key, String value) {
        Node node = map.get(key);
        if (node != null) {//存在
            node.value = value;//覆盖旧值
            moveNodeToTail(node);
            return;
        } else {
            Node newNode = new Node(key, value);
            if (map.size() == capacity) {//容量满了
                Node delNode = removeFirst();
                map.remove(delNode.key);
            }
            addToList(newNode);
            map.put(key, newNode);
        }
    }

    public String get(String key) {
        Node node = map.get(key);
        if (node != null) {
            moveNodeToTail(node);
            return node.value;
        }
        return null;
    }


    /**
     * 移动到队尾
     *
     * @param node
     */
    public void moveNodeToTail(Node node) {
        if (tail == node) {//已经是队尾
            return;
        }
        if (node == head) {//是队头
            head = node.next;
            head.pre = null;
        } else {//中间
            node.next.pre = node.pre;
            node.pre.next = node.next;
        }
        node.pre = tail;
        node.next = null;
        tail = node;
    }

    public void addToList(Node node) {
        if (head == null) {
            head = node;
        } else {
            node.pre = tail;
            tail.next = node;
        }
        tail = node;
    }

    /**
     * 删除头结点
     *
     * @return
     */
    public Node removeFirst() {
        if (head == null) {
            return null;
        }
        Node res = head;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = res.next;
            head.pre = null;
            res.next = null;
        }
        return res;
    }
}
