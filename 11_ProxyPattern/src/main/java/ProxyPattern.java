/**
 * @program: SimpleFactoryPattern
 * @description: 代理模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 12:12
 **/
public class ProxyPattern {
    public static void main(String[] args) throws ClassNotFoundException{
        new RealSubjectProxy().doWork();
    }
}

interface Subject{
    void doWork();
}

class RealSubject implements Subject{

    @Override
    public void doWork() {
        System.out.println("Hello World");
    }
}

class RealSubjectProxy implements Subject{
    private RealSubject subject;

    /*    public RealSubjectProxy(RealSubject subject) {
            this.subject = subject;
        }

        这么写从语法上来看会很像装饰器模式，但是代理模式关注的并不是这个具体的方式，并不关注这个加载的时机，而是关注必须通过代理类访问，仅此而已。

        下面的方式也仅仅是使用了类加载器加载而已。

        代理模式是为其他对象提供一种代理以控制对这个对象的访问，平时我们使用的代理过多，这里就不多做介绍了。
        */
    public RealSubjectProxy() {
        try{
            this.subject = (RealSubject) this.getClass().getClassLoader().loadClass("RealSubject").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        System.out.println("建立连接");
    }

    public void log(){
        System.out.println("日志记录");
    }

    @Override
    public void doWork() {
        connect();
        subject.doWork();
        log();
    }
}
