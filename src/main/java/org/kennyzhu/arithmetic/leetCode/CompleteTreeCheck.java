package org.kennyzhu.arithmetic.leetCode;

import lombok.Data;

import java.util.LinkedList;
import java.util.Queue;

/**
 * desc:校验是否为完全二叉树
 * author: yanlongzhu
 * date:2020/10/28.
 * leeCode:https://leetcode-cn.com/problems/check-completeness-of-a-binary-tree/
 */
public class CompleteTreeCheck {
    public boolean isCompleteTree(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode prev = root;
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.remove();
            if (prev == null && node != null)
                return false;
            if (node != null) {
                queue.add(node.left);
                queue.add(node.right);
            }
            prev = node;
        }
        return true;
    }

    @Data
    static class TreeNode {
        private TreeNode left;
        private TreeNode right;
    }
}
