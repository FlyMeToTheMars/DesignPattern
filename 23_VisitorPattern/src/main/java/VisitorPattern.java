/**
 * @program: SimpleFactoryPattern
 * @description: 访问者模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 10:37
 **/
public class VisitorPattern {
    public static void main(String[] args) {
        EggRobot erDan = new EggRobot();
        erDan.calc();

        Visitor updatePackage = new UpdateVisitor();
        erDan.accept(updatePackage);
        erDan.calc();

    }
}

class EggRobot{
    private HardDisk disk;
    private CPU cpu;

    public EggRobot() {
        disk = new HardDisk("记住 1 + 1 = 1");
        cpu = new CPU("1+1=1");
    }

    public void calc() {
        disk.run();
        cpu.run();
    }

    public void accept(Visitor visitor) {
        cpu.accept(visitor);
        disk.accept(visitor);
    }

}

abstract class Hardware{
    String command;

    public Hardware(String command) {
        this.command = command;
    }

    public void run() {
        System.out.println(command);
    }

    public abstract void accept(Visitor visitor);
}

interface Visitor{
    void visitCPU(CPU cpu);

    void visitDisk(HardDisk disk);
}

class UpdateVisitor implements Visitor{

    @Override
    public void visitCPU(CPU cpu) {
        cpu.command += ": 1 + 1 = 2";
    }

    @Override
    public void visitDisk(HardDisk disk) {
        disk.command += ": 记住： 1 + 1 = 2";
    }
}

class CPU extends Hardware{


    public CPU(String command) {
        super(command);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitCPU(this);
    }
}

class HardDisk extends Hardware {

    public HardDisk(String command) {
        super(command);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDisk(this);
    }
}

//访问者模式，用于凤凰一些作用域某种数据结构中的个元素的操作，他可以在不改变数据结构的前提下定义作用域这些元素的新的操作。
//上面的这个例子属于是 典型的应用场景：硬件固件升级
//优点：符合开闭原则，满足单一职责
//缺点：访问者模式要定义很多访问元素的方法，新增一个元素就要新增一个方法，还有就是我们的元素必须要暴露相应的方法，是为了让内部元素可见。