/**
 * @program: SimpleFactoryPattern
 * @description: 适配器模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 09:43
 **/
public class AdapterPattern {
    public static void main(String[] args) {
        String translate = new Adapter(new Speaker()).translate();
        System.out.println(translate);
    }
}

class Speaker{
    public String speak(){
        return "China No.1";
    }
}

interface Translator{
    String translate();
}

class Adapter implements Translator{
    private Speaker speaker;

    public Adapter(Speaker speaker) {
        this.speaker = speaker;
    }

    @Override
    public String translate() {
        String result = speaker.speak();
        //...手语
        return result;
    }

}

/**
将一个类的接口编程客户端所期待的另一种接口，从而使原本接口不匹配而无法在一起工作的两个类能够在一起工作。
**/
