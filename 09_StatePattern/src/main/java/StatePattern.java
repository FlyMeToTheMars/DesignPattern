import java.awt.*;

/**
 * @program: SimpleFactoryPattern
 * @description: 状态模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 10:43
 **/
public class StatePattern {
    public static void main(String[] args) {
        Context zhangSan = new Context();
        zhangSan.changeState(new Happy());
        zhangSan.doSomething();
        zhangSan.changeState(new Sad());
        zhangSan.doSomething();
    }
}

abstract class State{
    abstract void doWork();
}

class Happy extends State{

    @Override
    void doWork() {
        System.out.println("积极主动");;
    }
}

class Angry extends State{

    @Override
    void doWork() {
        System.out.println("无精打采");
    }
}

class Sad extends State{

    @Override
    void doWork() {
        System.out.println("啥也不干");
    }
}

class Context{
    private State state;

    public void changeState(State state){
        this.state = state;
    }

    public void doSomething(){
        state.doWork();
    }
}

//允许一个对象在其内部状态改变时改变它的行为，对象看起来似乎修改了它的类，其别名为状态对象(Objects for States)，状态模式是一种对象行为型模式。
