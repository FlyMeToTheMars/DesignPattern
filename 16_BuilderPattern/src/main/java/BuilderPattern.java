import lombok.Builder;
import lombok.Data;

/**
 * @program: SimpleFactoryPattern
 * @description: 生成器模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 22:24
 **/
public class BuilderPattern {
    public static void main(String[] args) {
        new House.HouseBuilder().wall(new Wall()).window(new Window()).door(new Door()).build();
    }
}

@Data
@Builder
class House{
    private Window window;
    private Door door;
    private Wall wall;
}

class Window{

}

class Door{

}

class Wall{

}

// 在Idea的插件中搜索Builder Generator插件，可以通过Loombook注释自动导入并且隐藏多余的代码，具体的生成器模式并没有值得去特别解释的，相比于使用构造器来生成类，使用Builder能够直接指定生成出来的实例的属性。