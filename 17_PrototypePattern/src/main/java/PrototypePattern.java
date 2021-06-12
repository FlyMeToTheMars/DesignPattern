/**
 * @program: SimpleFactoryPattern
 * @description: 原型模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 02:10
 **/
public class PrototypePattern {
    public static void main(String[] args) {
        Plane plane = new Plane();
        System.out.println(plane.getName() + " " + plane.getType());
        Plane clone = (Plane) plane.clone();
        System.out.println(clone.getName() + " " + clone.getType());
    }

}

interface Prototype{
    Object clone();
}

// 飞机类
class Plane implements Prototype{

    private String name;
    private String type;

    public Plane() {
        name = "Name:" + Math.random();
        type = "Type:" + Math.random();
    }

    public Plane(Plane plane){
        this.name = plane.name;
        this.type = plane.type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public Object clone() {
        return new Plane(this);
    }
}

//创建型模式是指提供创建对象的机制，单例 工厂 抽象工厂和命令模式都属于创建型模式，
//原型模式使用克隆API，能够深度复制每一个属性，包括私有属性。