import java.util.HashSet;
import java.util.Set;

/**
 * @program: SimpleFactoryPattern
 * @description: 享元模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 20:18
 **/
public class FlyWeightPattern {
    public static void main(String[] args) {
        BikeFlyWeight bike1 = BikeFlyWeightFactory.getInstance().getBike();
        bike1.ride("ZhangSan");
        // bike1.back();

        BikeFlyWeight bike2 = BikeFlyWeightFactory.getInstance().getBike();
        bike2.ride("Zhaosi");
        bike2.back();

        BikeFlyWeight bike3 = BikeFlyWeightFactory.getInstance().getBike();
        bike3.ride("Wangwu");
        bike3.back();

        System.out.println(bike1 == bike2);
        System.out.println(bike2 == bike3);
    }
}

abstract class BikeFlyWeight {
    // 内部状态
    protected Integer state = 0; // 0 是未使用， 1 是使用中

    // userName 外部状态
    abstract void ride(String userName);
    abstract void back();

    public Integer getState() {
        return state;
    }
}

class MoBikeFlyWeight extends BikeFlyWeight{
    //定义新的内部状态，车架号
    private String bikeId;
    public MoBikeFlyWeight(String bikeId){
        this.bikeId = bikeId;
    }

    @Override
    void ride(String userName) {
        state = 1;
        System.out.println(userName + "骑" + bikeId + "号 自行车出行！");
    }

    @Override
    void back() {
        state = 0;
    }
}

class BikeFlyWeightFactory {
    private static BikeFlyWeightFactory instance = new BikeFlyWeightFactory();
    private Set<BikeFlyWeight> pool = new HashSet<>();

    public static BikeFlyWeightFactory getInstance() {
        return instance;
    }

    private BikeFlyWeightFactory() {
        for (int i = 0; i < 2; i++) {
            pool.add(new MoBikeFlyWeight(i + "号"));
        }
    }

    public BikeFlyWeight getBike() {
        for (BikeFlyWeight bike : pool) {
            if (bike.getState() == 0) {
                return bike;
            }
        }
        return null;
    }
}

//享元模式：运用共享技术有效地支持大量细粒度的对象。最常见的应用就是在线程池里面。
//缺点：增加了系统复杂性