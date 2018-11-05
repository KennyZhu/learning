package org.kennyzhu.rxJava;

/**
 * desc
 * author yanlongzhu
 * date:2017/5/17.
 */
public class RxJavaTest {
    public static void main(String[] args) {
        int pageSize = 5;
        int pageNum = 1;
        for (int i = 0; i < 3; i++) {
            int offset = (pageNum - 1) * pageSize ;
            System.out.println("#PageNum is " + pageNum + " offset is " + offset);
            pageNum++;
        }

    }
}
