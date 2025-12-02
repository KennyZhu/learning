package org.kennyzhu.arithmetic;

/**
 * 快速排序算法实现
 * 
 * 算法思想：
 * 1. 选择基准值（pivot）：通常选择第一个元素
 * 2. 分区操作：将小于基准的元素放左边，大于基准的放右边
 * 3. 递归排序：对左右两个子数组分别进行快速排序
 * 
 * 时间复杂度：
 * - 平均：O(n log n)
 * - 最好：O(n log n)
 * - 最坏：O(n²) - 当数组已排序或逆序时
 * 
 * 空间复杂度：O(log n) - 递归调用栈
 * 
 * User: Yanlong
 * Date: 13-9-25
 */
public class QSort {
    /**
     * 快速排序主方法
     * 
     * @param a     待排序数组
     * @param start 排序起始位置
     * @param end   排序结束位置
     */
    public static void quickSort(int a[], int start, int end) {
        // 边界检查：数组为空、长度为0、或起始位置不合法
        if ((a == null) || (a.length == 0) || start >= end) {
            return;
        }
        
        int i, j;
        i = start;  // 左指针
        j = end;    // 右指针
        
        // 分区过程：选择 a[start] 作为基准值（pivot）
        while (i < j) {
            // 第一步：从右向左扫描，找到第一个小于等于基准值的元素
            // 以 a[start] 为基准，右侧扫描
            while (i < j && a[i] <= a[j]) {
                j--;  // 右指针左移
            }
            
            // 找到了比基准小的元素，交换到左边
            if (i < j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
            
            // 第二步：从左向右扫描，找到第一个大于基准值的元素
            // 此时 a[j] 中存储着基准值
            while (i < j && a[i] < a[j]) {
                i++;  // 左指针右移
            }
            
            // 找到了比基准大的元素，交换到右边
            if (i < j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        // 此时 i == j，基准值已经在正确的位置
        
        // 第三步：递归排序左子数组
        if (i - start > 1) {
            // 左侧还有多个元素需要排序
            quickSort(a, start, i - 1);
        }
        
        // 第四步：递归排序右子数组
        if (end - j > 1) {
            // 右侧还有多个元素需要排序
            quickSort(a, j + 1, end);
        }
    }

    public static void main(String[] args) {
        int source[] = {12, 35, 10, 3, 99, 84, 100, 136, 113};
        QSort.quickSort(source, 0, source.length - 1);
        for (int i = 0; i < source.length; i++) {
            System.out.print(source[i] + ",");
        }
    }

}
