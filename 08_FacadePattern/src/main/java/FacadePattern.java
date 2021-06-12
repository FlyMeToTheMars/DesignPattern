/**
 * @program: SimpleFactoryPattern
 * @description: 外观模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 10:34
 **/
public class FacadePattern {
    public static void main(String[] args) {
        boolean prove = new Facade().prove();
        System.out.println(prove);
    }
}

class SubFlow1{
    boolean isTrue(){
        return true;
    }
}

class  SubFlow2{
    boolean isOK(){
        return true;
    }
}

class SubFlow3{
    boolean isGoodMan(){
        return true;
    }
}

class Facade{
    SubFlow1 s1 = new SubFlow1();
    SubFlow2 s2 = new SubFlow2();
    SubFlow3 s3 = new SubFlow3();

    boolean prove(){
        return s1.isTrue() && s2.isOK() && s3.isGoodMan();
    }
}

// 通过向现有的系统添加一个新的接口，隐藏掉系统的复杂性。外观模式要求一个子系统的外部与其内部的通信必须通过一个统一的对象进行。外观模式提供一个高层次的接口，使得子系统更容易使用。
// 缺点：不符合开闭原则，如果子系统要拓展的话，我们必须修改外观模式的类。