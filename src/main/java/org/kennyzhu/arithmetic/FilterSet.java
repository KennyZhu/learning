package org.kennyzhu.arithmetic;

/**
 * 位图（BitSet）实现的高效整数过滤集合
 * 
 * 设计思想：
 * - 使用位（bit）来标记整数是否存在
 * - 每个long型（64位）可以存储64个整数的状态
 * - 支持0-65535范围内的整数
 * 
 * 优势：
 * 1. 空间效率高：相比HashSet，节省大量内存
 * 2. 操作速度快：位运算效率高，O(1)时间复杂度
 * 3. 适用场景：大量整数的存在性判断
 * 
 * 实现原理：
 * - 数组索引 = 整数值 / 64 (即 no >>> 6)
 * - 位位置 = 整数值 % 64 (即 no & 63)
 * 
 * 创建时间：2016年8月30日
 * @author andy
 * @version 2.2
 */
public class FilterSet {

    // 存储元素的long数组，每个long可以存储64个整数的状态
    private final long[] elements;

    /**
     * 构造函数
     * 
     * 容量计算：
     * - 支持 0-65535 范围的整数
     * - 需要 65536 / 64 = 1024 个long
     * - 即 1 + (65535 >>> 6) = 1024
     */
    public FilterSet() {
        elements = new long[1 + (65535 >>> 6)];  // 1024个long，共65536位
    }

    /**
     * 添加单个元素
     * 
     * 实现：
     * 1. no >>> 6 = no / 64，确定在哪个long中
     * 2. no & 63 = no % 64，确定在long的哪一位
     * 3. 1L << (no & 63) 创建掩码
     * 4. |= 操作将该位设置为1
     * 
     * 时间复杂度：O(1)
     * 
     * @param no 要添加的整数（0-65535）
     */
    public void add(final int no) {
        elements[no >>> 6] |= (1L << (no & 63));
    }

    /**
     * 批量添加元素
     * 
     * @param no 要添加的整数数组
     */
    public void add(final int... no) {
        for (int currNo : no)
            elements[currNo >>> 6] |= (1L << (currNo & 63));
    }

    /**
     * 移除元素
     * 
     * 实现：
     * 1. 创建掩码：1L << (no & 63)
     * 2. 取反：~(...) 得到除该位外都是1的掩码
     * 3. &= 操作将该位设置为0
     * 
     * 时间复杂度：O(1)
     * 
     * @param no 要移除的整数
     */
    public void remove(final int no) {
        elements[no >>> 6] &= ~(1L << (no & 63));
    }

    /**
     * 添加元素并返回是否成功
     * 
     * 与 add() 的区别：
     * - add()：无返回值
     * - addAndNotify()：返回是否新增成功
     * 
     * @param no 要添加的整数
     * @return true: 添加成功（元素原本不存在）
     *         false: 添加失败（元素原本已存在）
     */
    public boolean addAndNotify(final int no) {
        int eWordNum = no >>> 6;  // 计算数组索引
        long oldElements = elements[eWordNum];  // 保存原值
        elements[eWordNum] |= (1L << (no & 63));  // 添加元素
        boolean result = elements[eWordNum] != oldElements;  // 判断是否有变化
        // 如果需要维护size，可以在这里size++
        return result;
    }

    /**
     * 移除元素并返回是否成功
     * 
     * @param no 要移除的整数
     * @return true: 移除成功（元素原本存在）
     *         false: 移除失败（元素原本不存在）
     */
    public boolean removeAndNotify(final int no) {
        int eWordNum = no >>> 6;  // 计算数组索引
        long oldElements = elements[eWordNum];  // 保存原值
        elements[eWordNum] &= ~(1L << (no & 63));  // 移除元素
        boolean result = elements[eWordNum] != oldElements;  // 判断是否有变化
        return result;
    }

    /**
     * 判断元素是否存在
     * 
     * 实现：
     * 1. 找到对应的long值
     * 2. 与掩码进行&运算
     * 3. 结果不为0说明该位是1，即元素存在
     * 
     * 时间复杂度：O(1)
     * 
     * @param no 要判断的整数
     * @return true: 存在，false: 不存在
     */
    public boolean contains(final int no) {
        return (elements[no >>> 6] & (1L << (no & 63))) != 0;
    }

    /**
     * 判断是否包含所有指定元素
     * 
     * @param no 要判断的整数数组
     * @return true: 全部存在，false: 至少有一个不存在
     */
    public boolean containsAll(final int... no) {
        if (no.length == 0)
            return true;  // 空数组，返回true
            
        // 遍历检查每个元素
        for (int currNo : no)
            if ((elements[currNo >>> 6] & (1L << (currNo & 63))) == 0)
                return false;  // 发现不存在的元素，立即返回false
        return true;  // 所有元素都存在
    }

    /**
     * containsAll的另一种实现方式（已废弃，效率低）
     * 
     * @deprecated 此方法效率低下，建议直接使用 {@link #containsAll(int...)}
     * 
     * @param no 要判断的整数数组
     * @return true: 全部存在，false: 至少有一个不存在
     */
    @Deprecated
    public boolean containsAll_ueslessWay(final int... no) {
        // 直接调用推荐的方法
        return containsAll(no);
    }

    /**
     * 获取集合中元素的个数
     * 
     * 实现方式：
     * - 遍历所有long，统计每个long中1的个数
     * - Long.bitCount() 方法返回long中1的个数
     * 
     * 注意：
     * - 目前没有维护size变量，每次调用都需要重新计算
     * - 时间复杂度：O(n)，n为数组长度（1024）
     * - 如果频繁调用，建议维护size变量
     * 
     * @return 集合中元素的个数
     */
    public int size() {
        int size = 0;
        // 遍历每个long，累加其中1的个数
        for (long element : elements)
            size += Long.bitCount(element);  // Java内置方法，统计1的个数
        return size;
    }

    public static void main(String[] args) {
        FilterSet oi = new FilterSet();
        System.out.println(oi.elements.length);
    }

}