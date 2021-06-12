import java.util.ArrayList;
import java.util.List;

/**
 * @program: SimpleFactoryPattern
 * @description: 组合模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 08:01
 **/
public class CompositePattern {
    public static void main(String[] args) {
        Composite china = new Composite();
        china.add(new City(10000));//直辖市 -- 北京
        china.add(new City(20000));//直辖市 -- 上海

        Composite ShanXi = new Composite();
        ShanXi.add(new City(3000));//大同市
        ShanXi.add(new City(3000));//太原市

        china.add(ShanXi);
        System.out.println(china.count());

        // 优点： 可以利用多态和递归方便的使用复杂树结构，符合开闭原则，无需修改现有代码。
    }
}

// 统计接口
interface Counter {
    //计数
    int count();
}

// 叶子节点
class City implements Counter {
    private int sum = 0;

    public City(int sum) {
        this.sum = sum;
    }

    @Override
    public int count() {
        return sum;
    }
}

// 容器
class Composite implements Counter {
    private List<Counter> counterList = new ArrayList<>();

    public void add(Counter counter) {
        counterList.add(counter);
    }

    public void delete(Counter counter) {
        counterList.remove(counter);
    }

    public List<Counter> getChild() {
        return counterList;
    }

    @Override
    public int count() {
        int sum = 0;
        for (Counter counter : counterList) {
            sum += counter.count();
        }

        return sum;
    }
}