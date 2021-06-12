/**
 * @program: SimpleFactoryPattern
 * @description: 工厂模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 08:31
 **/
public class FactoryPattern {
    public static void main(String[] args) {
        HuaWeiFactory factory = new HuaWeiFactory();
        Phone phone = factory.createPhone();
        phone.print();
    }
}

interface Phone{
    void print();
}

class iPhone implements Phone{
    @Override
    public void print() {
        System.out.println("iPhone");
    }
}

class HuaWeiPhone implements Phone{
    @Override
    public void print() {
        System.out.println("HuaWeiPhone");
    }
}

interface Factory{
    Phone createPhone();
}

class IPhoneFactory implements Factory {

    @Override
    public Phone createPhone() {
        return new iPhone();
    }
}

class HuaWeiFactory implements Factory {

    @Override
    public Phone createPhone() {
        return new HuaWeiPhone();
    }
}

/**
工厂模式相对于简单工厂来说没有去固定创建产品所需的工厂，仅仅给了一个接口，在JDK中典型的应用就是Collection工具类，符合开闭原则
**/