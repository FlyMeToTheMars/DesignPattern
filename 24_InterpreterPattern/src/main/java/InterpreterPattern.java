/**
 * @program: SimpleFactoryPattern
 * @description: 解释器模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 13:52
 **/
public class InterpreterPattern {
    public static void main(String[] args) {
        String express = "2 + 3";
        String[] keyWords = express.split(" ");
        String operator = "+";
        Interpreter[] nums = new Interpreter[2];
        int index = 0;
        for (String keyword : keyWords){
            try {
                int tmp = Integer.parseInt(keyword);
                nums[index++] = new NumberInterpreter(tmp);
            }catch (NumberFormatException e) {
                operator = keyword;
            }
        }
        if (operator.equals("+")){
            Interpreter add = new AddInterpreter(nums[0],nums[1]);
            System.out.println(add.interpret());
        }
    }
}

interface Interpreter {
    int interpret();
}

class NumberInterpreter implements Interpreter{
    private int num;

    public NumberInterpreter(int num) {
        this.num = num;
    }
    public NumberInterpreter(String num) {
        this.num = Integer.parseInt(num);
    }


    @Override
    public int interpret() {
        return this.num;
    }
}

class AddInterpreter implements Interpreter{
    private Interpreter first,second;

    public AddInterpreter(Interpreter first, Interpreter second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "+";
    }

    @Override
    public int interpret() {
        return first.interpret() + second.interpret();
    }
}

//解释器模式属于行为型模式。它实现了一个表达式的接口，用于解释上下文。比如正则表达式解析，SQL解析等等。
//这种模式应用上较少，那在框架上用的还是比较多的，比如上文提到了正则表达式，还有XML解析，SQL解析等等都有着应用。

//那我们就用一个简单的例子来实现这个模式。
//先来讲讲解释器模式中的几个角色：
//
//抽象表达式(AbstractExpression)：是所有终结符表达式和非终结符表达式的公共父类。
//终结符表达式(TerminalExpression)：指的是与终结符相关的表达式。
//非终结符表达式(NonterminalExpression)：即没有终结符的表达式，通常一个解释器模式中，由少数几个终结符表达式与非终结符表达式构成。
//Context：上下文，一般是存储需要解释的语句。

//我们就简单实现了加法运算，那值得注意的有以下几点：
//
//在main方法中的解析应该转移到工具类当中，客户端应该直接调用工具类。
//既然符号是终结符表达式，那么以上的express应该改为后缀表达式”2 3 +”，然后通过栈来求整个表达式的值。
//那么以上几点就是我的修改建议，感兴趣的朋友可以接着写下去，实现其他运算，加上工具类的封装。
//
//优缺点
//解释器模式的优点在于可扩展性良好，并且十分灵活，当我们新增一个表达式时，原有代码无需修改。
//那缺点在于如果语法过于复杂，解析起来会十分麻烦，类的数量会急剧增加，导致难以维护。其次由于解释器模式使用了大量的循环和递归，当面对较复杂语句时执行速度会受到影响。
//
//总结
//解释器模式为语言的设计和实现提供了一种解决方案，通过定义表达式来解析语言中的语句，在项目中的使用频率不是很高，但如要实现一门语言或者解析语句，那这个模式就应用的很广泛。
