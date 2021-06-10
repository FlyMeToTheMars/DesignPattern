package com.hugh.designpattern;

/**
 * @program: Design Pattern
 * @description:
 * @author: Fly.Hugh
 * @create: 2021-06-10 10:32
 **/
public class SingletonPattern {
    public static void main(String[] args) {

    }
}

// 饿汉式
// Version 1
class Single1 {
    private static Single1 instance;
    public static Single1 getInstance() {
        if (instance == null) {
            instance = new Single1();
        }
        return instance;
    }
}

// Version 2
// 进一步，把构造器改为私有的，这样能够防止被外部的类调用。
class Single2 {
    private static Single2 instance;
    private Single2() {}
    public static Single2 getInstance() {
        if (instance == null) {
            instance = new Single2();
        }
        return instance;
    }
}

// 当多线程工作的时候，如果有多个线程同时运行到`if (instance == null)`，都判断为null，那么两个线程就各自会创建一个实例——这样一来，就不是单例了。
// Version 2
class Single3 {
    private static Single3 instance;
    private Single3() {}
    public static synchronized Single3 getInstance() {
        if (instance == null) {
            instance = new Single3();
        }
        return instance;
    }
}

// 继续优化，给gitInstance方法加锁，虽然会避免了可能会出现的多个实例问题，但是会强制除T1之外的所有线程等待，实际上会对程序的执行效率造成负面影响。
// Version 4
class Single4 {
    private static Single4 instance;
    private Single4() {}
    public static Single4 getInstance() {
        if (instance == null) {
            synchronized (Single4.class) {
                if (instance == null) {
                    instance = new Single4();
                }
            }
        }
        return instance;
    }
}

// Version 5
// 完全体 更多说明见文档
class Single5 {
    private static volatile Single5 instance;
    private Single5() {}
    public static Single5 getInstance() {
        if (instance == null) {
            synchronized (Single5.class) {
                if (instance == null) {
                    instance = new Single5();
                }
            }
        }
        return instance;
    }
}

// 更多拓展见文档