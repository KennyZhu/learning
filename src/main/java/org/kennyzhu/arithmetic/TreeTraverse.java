package org.kennyzhu.arithmetic;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 树的深度遍历
 */
@Slf4j
public class TreeTraverse {


    /**
     * 遍历二叉树的所有路劲
     *
     * @param root
     * @return
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> paths = new ArrayList<String>();
        constructPaths(root, "", paths);
        return paths;
    }

    public void constructPaths(TreeNode root, String path, List<String> paths) {
        if (root != null) {
            StringBuilder pathSB = new StringBuilder(path);
            pathSB.append(root.getVal());
            if (root.getLeft() == null && root.getRight() == null) {  // 当前节点是叶子节点
                paths.add(pathSB.toString());  // 把路径加入到答案中
            } else {
                pathSB.append("->");  // 当前节点不是叶子节点，继续递归遍历
                constructPaths(root.getLeft(), pathSB.toString(), paths);
                constructPaths(root.getRight(), pathSB.toString(), paths);
            }
        }
    }

    /**
     * 求路劲和等于sum的所有路劲，
     *
     * @param root
     * @param sum
     * @return
     */
    public List<List<Integer>> sum(TreeNode root, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        traverse(root, result, new ArrayList<>());

        for (List<Integer> list : result) {
            StringBuilder sb = new StringBuilder();
            for (Integer val : list) {
                sb.append(val).append(",");
            }
            log.info(sb.toString());
        }
        return result;
    }

    public void traverse(TreeNode root, List<List<Integer>> result, List<Integer> temp) {
        if (root != null) {
            List<Integer> list = new ArrayList<>(temp);
            list.add(root.getVal());//append之前的值
            if (root.getLeft() == null && root.getRight() == null) {
                result.add(list);//叶子节点，把遍历路劲放到结果中。
            } else {
                traverse(root.getLeft(), result, list);
                traverse(root.getRight(), result, list);

            }
        }

    }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode();
        List<Integer> midList = Arrays.asList(9, 10, 13, 14, 15, 17, 20, 21);
        List<Integer> preList = Arrays.asList(15, 13, 10, 9, 14, 20, 17, 21);
        TreeNode rootNode = treeNode.buildTree(preList, midList);
        TreeTraverse treeTraverse = new TreeTraverse();
        treeTraverse.sum(rootNode, 10);
    }


}

