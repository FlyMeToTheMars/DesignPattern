import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @program: SimpleFactoryPattern
 * @description: 迭代器模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 07:53
 **/
public class IteratorPattern {
    public static void main(String[] args) {
        //迭代器模式是一种行为涉及模式，让你能在不暴露集合底层表现形式的情况下遍历集合中所有的元素。
        //JDK中迭代器模式的应用

        //数组
        new ArrayList<>().iterator();
        new ArrayList<>().listIterator();
        //链表
        new LinkedList<>().iterator();
    }
}
