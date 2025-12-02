package org.kennyzhu.arithmetic;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 二叉树的深度优先遍历（DFS）
 * 
 * 深度优先遍历特点：
 * - 优先访问深度最深的节点
 * - 使用递归或栈实现
 * - 适用于路径查找、树的遍历等场景
 * 
 * 本类包含两个功能：
 * 1. 找出所有从根到叶子的路径
 * 2. 找出路径和等于指定值的所有路径
 */
@Slf4j
public class TreeTraverse {


    /**
     * 遍历二叉树的所有路径（从根节点到叶子节点）
     * 
     * LeetCode #257: Binary Tree Paths
     * 
     * 示例：
     *     1
     *   /   \
     *  2     3
     *   \
     *    5
     * 输出：["1->2->5", "1->3"]
     * 
     * 时间复杂度：O(n)，n为节点数
     * 空间复杂度：O(h)，h为树的高度（递归栈深度）
     *
     * @param root 根节点
     * @return 所有路径的字符串列表
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> paths = new ArrayList<String>();
        constructPaths(root, "", paths);
        return paths;
    }

    /**
     * 递归构建路径
     * 
     * 算法思路：
     * 1. 将当前节点值加入路径
     * 2. 如果是叶子节点，将完整路径加入结果
     * 3. 如果不是叶子节点，继续递归左右子树
     * 
     * @param root  当前节点
     * @param path  当前路径字符串
     * @param paths 结果列表
     */
    public void constructPaths(TreeNode root, String path, List<String> paths) {
        if (root != null) {
            // 将当前节点值加入路径
            StringBuilder pathSB = new StringBuilder(path);
            pathSB.append(root.getVal());
            
            // 判断是否为叶子节点
            if (root.getLeft() == null && root.getRight() == null) {
                // 叶子节点：将完整路径加入结果
                paths.add(pathSB.toString());
            } else {
                // 非叶子节点：添加箭头，继续递归
                pathSB.append("->");
                constructPaths(root.getLeft(), pathSB.toString(), paths);   // 递归左子树
                constructPaths(root.getRight(), pathSB.toString(), paths);  // 递归右子树
            }
        }
    }

    /**
     * 找出路径和等于指定值的所有路径
     * 
     * LeetCode #113: Path Sum II
     * 
     * 示例：
     *       5
     *      / \
     *     4   8
     *    /   / \
     *   11  13  4
     *  /  \      \
     * 7    2      1
     * 
     * targetSum = 22
     * 输出：[[5,4,11,2], [5,8,4,5]] （路径和等于22的所有路径）
     *
     * @param root 根节点
     * @param sum  目标路径和
     * @return 所有路径和等于sum的路径
     */
    public List<List<Integer>> sum(TreeNode root, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        traverse(root, sum, 0, result, new ArrayList<>());

        // 打印所有符合条件的路径
        for (List<Integer> list : result) {
            StringBuilder sb = new StringBuilder();
            int pathSum = 0;
            for (Integer val : list) {
                sb.append(val).append(",");
                pathSum += val;
            }
            log.info("Path: " + sb.toString() + " Sum: " + pathSum);
        }
        return result;
    }

    /**
     * 递归遍历树，收集路径和等于目标值的路径
     * 
     * 算法思路：
     * 1. 将当前节点值加入路径，更新当前和
     * 2. 如果是叶子节点，判断当前和是否等于目标和
     * 3. 如果不是叶子节点，递归访问左右子树
     * 4. 回溯：移除当前节点（重要！）
     * 
     * 关键点：
     * - 使用回溯法，避免创建大量List副本
     * - 只在符合条件时才保存路径副本
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)，h为树的高度
     * 
     * @param root       当前节点
     * @param targetSum  目标和
     * @param currentSum 当前路径和
     * @param result     结果列表
     * @param path       当前路径
     */
    private void traverse(TreeNode root, int targetSum, int currentSum, 
                         List<List<Integer>> result, List<Integer> path) {
        if (root != null) {
            // 将当前节点加入路径
            path.add(root.getVal());
            currentSum += root.getVal();
            
            // 判断是否为叶子节点
            if (root.getLeft() == null && root.getRight() == null) {
                // 叶子节点：检查路径和是否等于目标和
                if (currentSum == targetSum) {
                    result.add(new ArrayList<>(path));  // 创建副本保存
                }
            } else {
                // 非叶子节点：继续递归左右子树
                traverse(root.getLeft(), targetSum, currentSum, result, path);
                traverse(root.getRight(), targetSum, currentSum, result, path);
            }
            
            // 回溯：移除当前节点，恢复路径状态
            path.remove(path.size() - 1);
        }
    }

    /**
     * 测试方法
     * 
     * 构建的树结构：
     *          15
     *        /    \
     *       13     20
     *      /  \   /  \
     *     10  14 17  21
     *    /
     *   9
     * 
     * 路径和示例：
     * - 15 -> 13 -> 10 -> 9 = 47
     * - 15 -> 13 -> 14 = 42
     * - 15 -> 20 -> 17 = 52
     * - 15 -> 20 -> 21 = 56
     * 
     * 测试查找路径和等于 42 的所有路径
     */
    public static void main(String[] args) {
        // 构建二叉树
        TreeNode treeNode = new TreeNode();
        List<Integer> midList = Arrays.asList(9, 10, 13, 14, 15, 17, 20, 21);
        List<Integer> preList = Arrays.asList(15, 13, 10, 9, 14, 20, 17, 21);
        TreeNode rootNode = treeNode.buildTree(preList, midList);
        
        // 查找路径和等于 42 的所有路径
        TreeTraverse treeTraverse = new TreeTraverse();
        treeTraverse.sum(rootNode, 42);  // 应该找到 [15, 13, 14]
    }


}

