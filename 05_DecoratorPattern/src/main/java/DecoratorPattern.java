/**
 * @program: SimpleFactoryPattern
 * @description: 装饰器模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 09:03
 **/
public class DecoratorPattern {
    public static void main(String[] args) {
        new RobotDecorator(new FirstRobot()).doMoreThing();
    }
}

interface Robot{
    void doSomething();
}

class FirstRobot implements Robot{

    @Override
    public void doSomething() {
        System.out.println("对话");
        System.out.println("唱歌");
    }
}

class RobotDecorator implements Robot {

    private Robot robot;

    public RobotDecorator(Robot robot){
        this.robot = robot;
    }

    @Override
    public void doSomething() {
        robot.doSomething();
    }

    public void doMoreThing(){
        robot.doSomething();
        System.out.println("跳舞、拖地");
    }
}

/**
从面向对象的角度上来讲，这种拓展原有类的功能，直接使用继承即可，但是继承是一种静态拓展的方式，如果我们在已经有一个类的情况下进行拓展，就需要把这个类作为参数传入装饰器中来拓展功能，装饰器模式属于是一种动态的拓展方法。
Java的IO流大量使用了这种设计模式
**/