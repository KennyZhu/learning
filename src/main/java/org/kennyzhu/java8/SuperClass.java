package org.kennyzhu.java8;


import com.rits.cloning.Cloner;

import java.util.Arrays;
import java.util.List;

/**
 * desc
 * author yanlongzhu
 * date:2017/4/20.
 */
public class SuperClass {
    private List<SubClass> subClass;
    private int value;


    public static void main(String[] args) throws Exception {
        SubClass subClass = new SubClass();
        subClass.setType(11);
        SuperClass perClass = new SuperClass();
        perClass.setSubClass(Arrays.asList(subClass));
        perClass.setValue(22);

        SuperClass per2 = new SuperClass();
        Cloner cloner = Cloner.standard();
        per2 = cloner.deepClone(perClass);
        per2.getSubClass().get(0).setType(33);
        System.out.println(per2.getSubClass() + "===" + perClass.getSubClass() + "===" + perClass.getSubClass().get(0).getType());
    }

    public List<SubClass> getSubClass() {
        return subClass;
    }

    public void setSubClass(List<SubClass> subClass) {
        this.subClass = subClass;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
