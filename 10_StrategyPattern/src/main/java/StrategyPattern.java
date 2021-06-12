/**
 * @program: SimpleFactoryPattern
 * @description: 策略模式
 * @author: Fly.Hugh
 * @create: 2021-06-11 11:51
 **/
public class StrategyPattern {
    public static void main(String[] args) {
        //定义一组算法，将每个算法都封装起来，并且是他们之间可以互换。策略模式让算法独立于使用它的客户而变化，也成为策略模式（Policy）。
        //和状态模式的区别：状态模式会根据不同的状态对应不同的行为，策略模式的关注点是具体某一行为的具体执行过程。
        //某种程度上，策略模式的目的就是为了减少if-else，JDK中，ThreadPoolExecutor中的RejectExecutionHandler就是典型的应用。
        //策略模式和状态模式在一些情况下会混用，另一些情况下不用区分。

        // 这种模式和状态模式很难区别开来，主要体现在业务中的侧重点不同，所以这里没有code示例。
    }
}
