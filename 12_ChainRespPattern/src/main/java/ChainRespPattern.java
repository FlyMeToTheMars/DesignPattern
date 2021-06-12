/**
 * @program: SimpleFactoryPattern
 * @description: 责任链模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 17:44
 **/
public class ChainRespPattern {
    public static void main(String[] args) {
        Handler level1 = new Leader();
        Handler level2 = new Boss();
        level1.setNextHandler(level2);

        level1.process(10);
        level1.process(11);
    }

}

abstract class Handler{
    protected Handler nextHandler;
    public void setNextHandler(Handler nextHandler){
        this.nextHandler = nextHandler;
    }

    public abstract void process(Integer info);
}

class Leader extends Handler{

    @Override
    public void process(Integer info) {
        if(info > 0 && info < 11)
            System.out.println("Leader 处理！");
        else
            nextHandler.process(info);
    }
}

class Boss extends Handler{

    @Override
    public void process(Integer info) {
        System.out.println("Boss 处理！");
    }
}

//责任链模式将请求和处理分开，请求者不需要知道谁去处理，处理者也需要知道请求的全貌，其次呢，可以提高系统灵活性，如果我们新增处理者，代价很小
//缺点就是性能比较差，如果处理着很多的时候，每个请求都要从第一个处理者开始，不方便调试