package com.example.wordsbook;

public class Singleton {
    private static Singleton instance = new Singleton();

    //提供私有构造器，只有在当前类中可以使用new,外界不能构造该类的对象
    private Singleton() {
    }

    // 对外提供一个公共访问点
    public static Singleton getInstance() {
        return instance;
    }
}
