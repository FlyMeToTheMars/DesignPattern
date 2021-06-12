/**
 * @program: Design Pattern
 * @description:    简单工厂模式（静态工厂）
 * @author: Fly.Hugh
 * @create: 2021-06-10 13:14
 **/
public class SimpleFactory {
    public static Product createProduct(String type){
        if(type.equals("A")) return new ProductA();
        else return new ProductB();
    }

    public static void main(String[] args) {
        Product product = SimpleFactory.createProduct("A");
        product.print();
    }
}

abstract class Product{
    public abstract void print();
}

class ProductA extends Product{

    @Override
    public void print() {
        System.out.println("产品A");
    }
}

class ProductB extends Product{

    @Override
    public void print() {
        System.out.println("产品B");
    }
}
/*
    优点：实现对象的创建和使用完全分离
    缺点：不够灵活，新增产品就要修改判断逻辑,违反了开闭原则
    使用：在JDK的DateFormat等工具类中广泛存在
**/