package org.kennyzhu.arithmetic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
public class TreeNode {
    private TreeNode left;
    private TreeNode right;
    private int val;

    public TreeNode(int val) {
        this.val = val;
        left = null;
        right = null;
    }

    public TreeNode() {

    }

    /**
     * 根据前序遍历和中序遍历构建树
     *
     * @param preList 前序遍历结果
     * @param midList 中序遍历结果
     * @return 返回根节点
     */
    public TreeNode buildTree(List<Integer> preList, List<Integer> midList) {
        if (CollectionUtils.isEmpty(preList) && CollectionUtils.isEmpty(midList)) {
            return null;
        }
        //1、前序遍历的第一个节点就是根节点。
        TreeNode root = new TreeNode(preList.get(0));
        //2、找到左右子树的中序遍历。从中序遍历中找到根节点，根节点的左侧即为左子树，右侧即为右子树
        List<Integer> midLeftList = new ArrayList<>();
        List<Integer> midRightList = new ArrayList<>();
        int flag = 0;
        for (Integer midVal : midList) {
            if (flag == 1) {
                midRightList.add(midVal);
            } else {
                if (midVal == root.getVal()) {
                    flag = 1;
                } else {
                    midLeftList.add(midVal);
                }
            }
        }
        //3、找到左右子树的前序遍历序列
        List<Integer> preLeftList = new ArrayList<>();
        List<Integer> preRightList = new ArrayList<>();
        for (int preIndex = 1; preIndex < preList.size(); preIndex++) {
            int preNodeVal = preList.get(preIndex);
            if (!midLeftList.contains(preNodeVal)) {
                preRightList = preList.subList(preIndex, preList.size() - 1);
                preLeftList = preList.subList(1, preIndex);
                break;
            }
        }
        if (!CollectionUtils.isEmpty(preLeftList)) {
            root.setLeft(new TreeNode(preLeftList.get(0)));
            buildTree(preLeftList, midLeftList);
        }
        if (!CollectionUtils.isEmpty(preRightList)) {
            root.setRight(new TreeNode(preRightList.get(0)));
            buildTree(preRightList, midRightList);
        }
        return root;
    }

    public static void main(String[] args) {

        TreeNode treeNode = new TreeNode();
        List<Integer> midList = Arrays.asList(9, 10, 13, 14, 15, 17, 20, 21);
        List<Integer> preList = Arrays.asList(15, 13, 10, 9, 14, 20, 17, 21);
        TreeNode rootNode = treeNode.buildTree(preList, midList);
        log.info("RootNode:{} left is {} right is {}", rootNode.getVal(), rootNode.getLeft().getVal(), rootNode.getRight().getVal());
    }
}
