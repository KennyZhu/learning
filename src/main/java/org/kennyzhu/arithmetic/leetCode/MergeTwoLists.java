package org.kennyzhu.arithmetic.leetCode;

import lombok.extern.slf4j.Slf4j;

/**
 * desc:两个有序列表合并
 * author: yanlongzhu
 * date:2020/8/16.
 */
@Slf4j
public class MergeTwoLists {
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }

    }

    static class ListNode {
        private int val;
        private ListNode next;
    }

    public static void main(String[] args) {

    }
}
