package org.kennyzhu.arithmetic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 二叉树节点类
 * 
 * 功能：根据前序遍历和中序遍历结果构建二叉树
 * 
 * 前序遍历：根 -> 左 -> 右
 * 中序遍历：左 -> 根 -> 右
 * 后序遍历：左 -> 右 -> 根
 * 
 * 核心原理：
 * 1. 前序遍历的第一个元素一定是根节点
 * 2. 在中序遍历中找到根节点，左边是左子树，右边是右子树
 * 3. 递归构建左右子树
 */
@Data
@Slf4j
public class TreeNode {
    private TreeNode left;   // 左子节点
    private TreeNode right;  // 右子节点
    private int val;         // 节点值

    /**
     * 带值构造函数
     * 
     * @param val 节点值
     */
    public TreeNode(int val) {
        this.val = val;
        left = null;
        right = null;
    }

    /**
     * 默认构造函数
     */
    public TreeNode() {

    }

    /**
     * 根据前序遍历和中序遍历构建二叉树（公共方法）
     * 
     * 算法复杂度：
     * - 时间复杂度：O(n²)，每个节点需要在中序遍历中查找
     * - 空间复杂度：O(n)，存储节点映射和递归调用栈
     * 
     * 示例：
     * preList = [15, 13, 10, 9, 14, 20, 17, 21]
     * midList = [9, 10, 13, 14, 15, 17, 20, 21]
     * 
     * 构建过程：
     * 1. 根节点：15（前序第一个）
     * 2. 在中序中找15，左边[9,10,13,14]是左子树，右边[17,20,21]是右子树
     * 3. 递归构建左右子树
     * 
     * @param preList 前序遍历结果
     * @param midList 中序遍历结果
     * @return 二叉树的根节点
     */
    public TreeNode buildTree(List<Integer> preList, List<Integer> midList) {
        // 参数校验
        if (CollectionUtils.isEmpty(preList) || 
            CollectionUtils.isEmpty(midList) || 
            preList.size() != midList.size()) {
            return null;
        }
        
        // 使用Map存储所有节点，key为节点值，value为节点对象
        Map<Integer, TreeNode> treeNods = new HashMap<>(preList.size());
        treeNods.put(preList.get(0), new TreeNode(preList.get(0)));
        
        // 递归构建树
        buildTree(preList, midList, treeNods);

        // 返回根节点
        if (CollectionUtils.isEmpty(treeNods)) {
            return null;
        }
        return treeNods.get(preList.get(0));
    }

    /**
     * 递归构建二叉树的核心方法（私有）
     * 
     * 算法步骤：
     * 1. 从前序遍历获取根节点（第一个元素）
     * 2. 在中序遍历中找到根节点，划分左右子树
     * 3. 根据左右子树的中序遍历，从前序遍历中提取对应的前序序列
     * 4. 递归构建左右子树
     * 
     * 示例：
     * preList = [15, 13, 10, 9, 14]
     * midList = [9, 10, 13, 14, 15]
     * 
     * 步骤1：根节点 = 15
     * 步骤2：在中序中找到15，左子树中序=[9,10,13,14]，右子树中序=[]
     * 步骤3：左子树前序=[13,10,9,14]，右子树前序=[]
     * 步骤4：递归构建左子树
     *
     * @param preList   前序遍历结果
     * @param midList   中序遍历结果
     * @param treeNodes 节点映射表，用于存储所有节点
     */
    private void buildTree(List<Integer> preList, List<Integer> midList, Map<Integer, TreeNode> treeNodes) {
        // 递归终止条件：序列为空
        if (CollectionUtils.isEmpty(preList) && CollectionUtils.isEmpty(midList)) {
            return;
        }

        // 步骤1：前序遍历的第一个节点就是根节点
        TreeNode root = treeNodes.get(preList.get(0));
        
        // 叶子节点判断：如果序列长度为1，说明是叶子节点，直接返回
        if (preList.size() == 1 && midList.size() == 1) {
            return;
        }
        
        // 步骤2：从中序遍历中分离左右子树
        // 在中序遍历中找到根节点，根节点左侧是左子树，右侧是右子树
        List<Integer> leftMidList = new ArrayList<>();   // 左子树的中序遍历
        List<Integer> rightMidList = new ArrayList<>();  // 右子树的中序遍历
        int flag = 0;  // 标志位：0表示在根节点左侧，1表示在根节点右侧
        
        for (Integer midVal : midList) {
            if (midVal == root.getVal()) {
                // 找到根节点，切换标志位
                flag = 1;
                continue;  // 跳过根节点本身
            }
            if (flag == 1) {
                rightMidList.add(midVal);  // 根节点右侧，属于右子树
            } else {
                leftMidList.add(midVal);   // 根节点左侧，属于左子树
            }
        }
        
        // 步骤3：从前序遍历中分离左右子树
        // 根据左右子树的中序遍历，提取对应的前序序列
        // 性能优化：使用 HashSet 提升查找效率，从 O(n³) 降到 O(n²)
        Set<Integer> leftMidSet = new HashSet<>(leftMidList);    // O(n) 转换
        Set<Integer> rightMidSet = new HashSet<>(rightMidList);  // O(n) 转换
        
        List<Integer> leftPreList = new ArrayList<>();   // 左子树的前序遍历
        List<Integer> rightPreList = new ArrayList<>();  // 右子树的前序遍历
        
        // 从第2个元素开始（跳过根节点）
        for (int preIndex = 1; preIndex < preList.size(); preIndex++) {
            int preNodeVal = preList.get(preIndex);
            
            // 判断当前节点属于左子树还是右子树（O(1) 查找）
            if (leftMidSet.contains(preNodeVal)) {
                // 在左子树的中序遍历中找到，说明属于左子树
                leftPreList.add(preNodeVal);
            }
            if (rightMidSet.contains(preNodeVal)) {
                // 在右子树的中序遍历中找到，说明属于右子树
                rightPreList.add(preNodeVal);
            }
        }
        
        // 步骤4a：递归构建左子树
        if (!CollectionUtils.isEmpty(leftPreList)) {
            TreeNode left = new TreeNode(leftPreList.get(0));  // 创建左子节点
            root.setLeft(left);                                // 连接到父节点
            treeNodes.put(left.getVal(), left);                // 保存到映射表
            buildTree(leftPreList, leftMidList, treeNodes);    // 递归构建
        }
        
        // 步骤4b：递归构建右子树
        if (!CollectionUtils.isEmpty(rightPreList)) {
            TreeNode right = new TreeNode(rightPreList.get(0)); // 创建右子节点
            root.setRight(right);                               // 连接到父节点
            treeNodes.put(right.getVal(), right);               // 保存到映射表
            buildTree(rightPreList, rightMidList, treeNodes);   // 递归构建
        }
    }

    /**
     * 测试方法
     * 
     * 示例树结构：
     *          15
     *        /    \
     *       13     20
     *      /  \   /  \
     *     10  14 17  21
     *    /
     *   9
     * 
     * 前序遍历：15, 13, 10, 9, 14, 20, 17, 21 （根->左->右）
     * 中序遍历：9, 10, 13, 14, 15, 17, 20, 21 （左->根->右）
     */
    public static void main(String[] args) {

        TreeNode treeNode = new TreeNode();
        
        // 中序遍历结果：左->根->右
        List<Integer> midList = Arrays.asList(9, 10, 13, 14, 15, 17, 20, 21);
        
        // 前序遍历结果：根->左->右
        List<Integer> preList = Arrays.asList(15, 13, 10, 9, 14, 20, 17, 21);
        
        // 根据两种遍历结果构建二叉树
        TreeNode rootNode = treeNode.buildTree(preList, midList);
        
        // 输出根节点的值：15
        log.info("Root value: " + rootNode.getVal());
    }
}
