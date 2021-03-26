package org.kennyzhu.arithmetic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
public class LinkedNode {
    private int val;
    private LinkedNode next;
    private LinkedNode pre;

    public LinkedNode() {
    }

    public LinkedNode(int val) {
        this.val = val;
    }

    /**
     * 构建链表
     *
     * @param input
     * @return
     */
    public static LinkedNode buildLinkedNode(List<Integer> input) {
        LinkedNode root = new LinkedNode();
        if (CollectionUtils.isEmpty(input)) {
            return root;
        }
        LinkedNode currentNode = null;
        for (Integer item : input) {
            LinkedNode node = new LinkedNode(item);
            if (currentNode == null) {
                root = node;
                currentNode = root;
            } else {
                currentNode.setNext(node);
                currentNode = node;
            }
        }
        return root;
    }

    public static void printLinkedList(LinkedNode root) {
        if (root == null) {
            log.info("LinkedList have no node.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(root.getVal()).append(",");

            root = root.getNext();
        } while (root != null);
        log.info(sb.substring(0, sb.length() - 1));
    }

    public static void main(String[] args) {
        LinkedNode.printLinkedList(LinkedNode.buildLinkedNode(Arrays.asList(1, 2, 3, 4, 5, 9)));
    }


}
