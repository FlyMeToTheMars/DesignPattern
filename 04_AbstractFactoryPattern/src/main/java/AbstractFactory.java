/**
 * @program: SimpleFactoryPattern
 * @description: 抽象工厂
 * @author: Fly.Hugh
 * @create: 2021-06-11 08:50
 **/
public interface AbstractFactory {
    Phone createPhone(String param);
    Mask createMask(String param);
}

// 具体工厂
class SuperFactory implements AbstractFactory {

    @Override
    public Phone createPhone(String param) {
        return new iPhone();
    }

    @Override
    public Mask createMask(String param) {
        return new N95();
    }
}

// 产品大类--手机
interface Phone{}
class iPhone implements Phone{}

// 产品大类--口罩
interface Mask{}
class N95 implements Mask{}

/**
普通工厂模式的工厂只有一个固定的只能，抽象工厂模式则不一样，在抽象工厂模式里面，可以在抽象工厂接口里面定义工厂不同的职能。
**/