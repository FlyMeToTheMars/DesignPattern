/**
 * @program: SimpleFactoryPattern
 * @description: 模板方法模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 18:06
 **/
public class TempleMethodPattern {
    public static void main(String[] args) {
        Cooking cooking = new CookingFood();
        cooking.cook();
    }
}

abstract  class Cooking{
    protected abstract void step1();
    protected abstract void step2();

    public void cook() {
        System.out.println("做饭开始");
        step1();
        step2();
        System.out.println("做饭结束");
    }
}

class CookingFood extends Cooking {

    @Override
    protected void step1() {
        System.out.println("放鸡蛋和西红柿");
    }

    @Override
    protected void step2() {
        System.out.println("少放盐和味精");
    }
}

//模版方法模式是指，定义一个操作中的算法的框架，将一些步骤延迟到子类中。使子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。