package org.kennyzhu.arithmetic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    public TreeNode buildTree(List<Integer> preList, List<Integer> midList) {
        if (CollectionUtils.isEmpty(preList) || CollectionUtils.isEmpty(midList) || preList.size() != midList.size()) {
            return null;
        }
        Map<Integer, TreeNode> treeNods = new HashMap<>(preList.size());
        treeNods.put(preList.get(0), new TreeNode(preList.get(0)));
        buildTree(preList, midList, treeNods);

        if (CollectionUtils.isEmpty(treeNods)) {
            return null;
        }
        return treeNods.get(preList.get(0));
    }

    /**
     * 根据前序遍历和中序遍历构建树
     *
     * @param preList 前序遍历结果
     * @param midList 中序遍历结果
     * @return 返回根节点
     */
    private void buildTree(List<Integer> preList, List<Integer> midList, Map<Integer, TreeNode> treeNodes) {
        if (CollectionUtils.isEmpty(preList) && CollectionUtils.isEmpty(midList)) {
            return;
        }

        //1、前序遍历的第一个节点就是根节点。
        TreeNode root = treeNodes.get(preList.get(0));
        if (preList.size() == 1 && midList.size() == 1) {//叶子节点
            return;
        }
        //2、找到左右子树的中序遍历。从中序遍历中找到根节点，根节点的左侧即为左子树，右侧即为右子树
        List<Integer> leftMidList = new ArrayList<>();
        List<Integer> rightMidList = new ArrayList<>();
        int flag = 0;
        for (Integer midVal : midList) {
            if (midVal == root.getVal()) {
                flag = 1;
                continue;
            }
            if (flag == 1) {
                rightMidList.add(midVal);
            } else {
                leftMidList.add(midVal);
            }
        }
        //3、找到左右子树的前序遍历序列
        List<Integer> leftPreList = new ArrayList<>();
        List<Integer> rightPreList = new ArrayList<>();
        //刨除根节点
        for (int preIndex = 1; preIndex < preList.size(); preIndex++) {
            int preNodeVal = preList.get(preIndex);
            if (leftMidList.contains(preNodeVal)) {//判断当前节点是否在左子树的中序遍历中，如果在，说明属于左子树节点
                leftPreList.add(preNodeVal);
            }
            if (rightMidList.contains(preNodeVal)) {
                rightPreList.add(preNodeVal);
            }
        }
        if (!CollectionUtils.isEmpty(leftPreList)) {
            TreeNode left = new TreeNode(leftPreList.get(0));
            root.setLeft(left);
            treeNodes.put(left.getVal(), left);
            buildTree(leftPreList, leftMidList, treeNodes);
        }
        if (!CollectionUtils.isEmpty(rightPreList)) {
            TreeNode right = new TreeNode(rightPreList.get(0));
            root.setRight(right);
            treeNodes.put(right.getVal(), right);
            buildTree(rightPreList, rightMidList, treeNodes);
        }
    }

    public static void main(String[] args) {

        TreeNode treeNode = new TreeNode();
        List<Integer> midList = Arrays.asList(9, 10, 13, 14, 15, 17, 20, 21);
        List<Integer> preList = Arrays.asList(15, 13, 10, 9, 14, 20, 17, 21);
        TreeNode rootNode = treeNode.buildTree(preList, midList);
        log.info(rootNode.getVal() + "");
    }
}
