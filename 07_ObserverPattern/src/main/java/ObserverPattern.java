import java.util.ArrayList;
import java.util.List;

/**
 * @program: SimpleFactoryPattern
 * @description: 观察者模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 10:02
 **/
public class ObserverPattern {
    public static void main(String[] args) {
        Debit zhangSan = new ZhangSan();
        zhangSan.borrow(new Wangwu());
        zhangSan.borrow(new Zhaosi());
        // ...TODO state 状态改变（也就是有钱了）

        zhangSan.notifyCredits();
    }
}

interface Debit{
    void borrow(Credit credit);
    void notifyCredits();
}

class ZhangSan implements Debit {
    private List<Credit> allCredits = new ArrayList<>();
    private Integer state = 0; // 1 表示有钱
    @Override
    public void borrow(Credit credit) {
        allCredits.add(credit);
    }

    @Override
    public void notifyCredits() {
        allCredits.forEach(credit -> credit.takeMoney());
    }
}

interface Credit{
    void takeMoney();
}

class Wangwu implements Credit {

    @Override
    public void takeMoney() {
        System.out.println("王五要钱");
    }
}

class Zhaosi implements Credit {

    @Override
    public void takeMoney() {
        System.out.println("赵四要钱");
    }
}

// 此案例中，张三是借钱方（发布消息方），在状态改变之后会向所有的借款方发送消息
/*发布、订阅模式 -> 观察者模式
        观察者模式（Observer Pattern）：定义对象间的一种一对多依赖关系，使得每当一个对象状态发生改变时，其相关依赖对象都会得到通知并且被自动更新*/
