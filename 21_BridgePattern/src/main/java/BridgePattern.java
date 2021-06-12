/**
 * @program: SimpleFactoryPattern
 * @description: 桥接模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 08:48
 **/
public class BridgePattern {

}

// 抽象类 (需不需要抽象可以根据实际场景来确定)
abstract class Abstraction{
    private Implementor implementor;

    public Abstraction(Implementor implementor) {
        this.implementor = implementor;
    }

    public void doWork(){
        this.implementor.doSomething();
    }
}

// 实现接口
interface Implementor{
     void doSomething();
}

// 实现类
class ConcreteImplementor implements Implementor{

    @Override
    public void doSomething() {
        System.out.println("Hello World");
    }
}

// 桥接模式将继承关系转化为了关联关系，从而降低了类与类之间的耦合，减少了代码的编写量
// 本例子中，关联的主体是Abstraction，关联的对象是 ConcreteImplementor，这样在需要新的关联对象的时候，直接使用即可，不违反开闭原则。
// 优点：这两个维度不管是哪一个维度要进行修改，都不需要修改另一种维度。
// 缺点：需要较高的抽象能力