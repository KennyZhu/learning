package org.kennyzhu.arithmetic.leetCode;

import lombok.Data;
import org.kennyzhu.arithmetic.LinkedNode;

import java.util.Arrays;

/**
 * 两个有序链表合并
 */
@Data
public class TwoLinkListMerge {

    private LinkedNode linkedNode1;
    private LinkedNode linkedNode2;


    public TwoLinkListMerge(LinkedNode input1, LinkedNode input2) {
        this.linkedNode1 = input1;
        this.linkedNode2 = input2;
    }

    public LinkedNode merge() {
        if (linkedNode1 == null && linkedNode2 == null) {
            return null;
        }
        if (linkedNode1 == null) {
            return linkedNode2;
        }
        if (linkedNode2 == null) {
            return linkedNode1;
        }
        LinkedNode result = new LinkedNode();
        LinkedNode point = result;
        while (linkedNode1 != null && linkedNode2 != null) {
            if (linkedNode1.getVal() < linkedNode2.getVal()) {
                point.setNext(linkedNode1);
                linkedNode1 = linkedNode1.getNext();
            } else {
                point.setNext(linkedNode2);
                linkedNode2 = linkedNode2.getNext();
            }
            point = point.getNext();
        }
        if (linkedNode1 != null) {
            point.setNext(linkedNode1);
        }
        if (linkedNode2 != null) {
            point.setNext(linkedNode2);
        }


        return result.getNext();
    }

    public static void main(String[] args) {
        TwoLinkListMerge linkListMerge = new TwoLinkListMerge(LinkedNode.buildLinkedNode(Arrays.asList(1, 3, 5, 7)), LinkedNode.buildLinkedNode(Arrays.asList(2, 4, 6, 8)));
        LinkedNode linkedNode = linkListMerge.merge();
        LinkedNode.printLinkedList(linkedNode);
    }
}
