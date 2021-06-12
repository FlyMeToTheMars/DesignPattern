import java.util.Stack;

/**
 * @program: SimpleFactoryPattern
 * @description: 备忘录模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 03:20
 **/
public class MementoPattern {
    public static void main(String[] args) {

        History history = new History();
        Document document = new Document();
        document.change("abc");
        history.add(document.save());

        document.change("def");
        history.add(document.save());

        document.change("ghi");
        history.add(document.save());

        document.change("lmn");

        document.resume(history.getLastVersion());
        document.print();

        document.resume(history.getLastVersion());
        document.print();
    }

}

//  文档类
class Document {
    private String content;//要备份的数据

    public BackUp save() {
        return new BackUp(content);
    }

    public void resume(BackUp backUp) {
        content = backUp.content;
    }

    public void change(String content) {
        this.content = content;
    }

    public void print() {
        System.out.println(content);
    }
}

//  备忘录接口 定义元数据方法
interface Memento{

}

// 备忘录类
class BackUp implements Memento{
    String content;

    public BackUp(String content) {
        this.content = content;
    }
}

//备忘录栈
class History{
    Stack<BackUp> backUpStack = new Stack<>();

    public void add(BackUp backUp) {
        backUpStack.add(backUp);
    }

    public BackUp getLastVersion(){
        return backUpStack.pop();
    }
}

//备忘录模式是一种行为涉及模式，允许在不暴露对象实现细节的情况下保存和恢复对象之前的状态。
//备忘录模式和原型模式非常相似，都会将克隆或者快照的工作交给别人去做，备忘录对象是不包含任何方法的，