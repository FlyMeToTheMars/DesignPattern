# SingletonPattern

## 定义

`单例对象的类必须保证只有一个实例存在`——这是维基百科上对单例的定义，这也可以作为对意图实现单例模式的代码进行检验的标准。

对单例的实现可以分为两大类——`懒汉式`和`饿汉式`，他们的区别在于：

`懒汉式`：指全局的单例实例在**第一次**被使用时构建。

`饿汉式`：指全局的单例实例在**类装载**时构建。

日常我们使用的较多的应该是`懒汉式`的单例，毕竟按需加载才能做到资源的最大化利用。

## 单例模式产生多个实例的情况：

1. 分布式系统中，会存在多个JVM虚拟机，每个虚拟机都会有一个实例。

2. 一个JVM虚拟机，使用了多个类加载器同时加载这个类，产生多个实例。

第一种情况在分布式系统中是我们希望遇到的情况，第二种情况下涉及到多级别类加载器的知识，在这里只附上简单的介绍：会有两个类加载器，这个时候的单例模式并不能保证单例。所以在这种非单一类加载器的情况下，我们需要指定类加载器来执行单例类的创建。
```java
private static Class getClass(String classname) throws ClassNotFoundException {
      
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if(classLoader == null)
        // 如果当前线程的classLoader为null，则指定单例模式的类加载器加载
        classLoader = Singleton.class.getClassLoader();
      return (classLoader.loadClass(classname));
   }
}
```

## 单例模式的使用场景局限性

如果这个单例模式的实例具有状态的话，在某处修改之后在所有地方都会被修改，所以单例模式的理想使用场景最好是无状态的。

## 单例模式的演化过程

### 懒汉式

```java
// Version 1
public class Single1 {
    private static Single1 instance;
    public static Single1 getInstance() {
        if (instance == null) {
            instance = new Single1();
        }
        return instance;
    }
}
```

再进一步，把构造器改为私有的，这样能够防止被外部的类调用。

```java
// Version 1.1
public class Single1 {
    private static Single1 instance;
    private Single1() {}
    public static Single1 getInstance() {
        if (instance == null) {
            instance = new Single1();
        }
        return instance;
    }
}
```

**当多线程工作的时候，如果有多个线程同时运行到`if (instance == null)`，都判断为null，那么两个线程就各自会创建一个实例——这样一来，就不是单例了**。

```java
// Version 2 
public class Single2 {
    private static Single2 instance;
    private Single2() {}
    public static synchronized Single2 getInstance() {
        if (instance == null) {
            instance = new Single2();
        }
        return instance;
    }
}

```

但是，这种写法也有一个问题：**给gitInstance方法加锁，虽然会避免了可能会出现的多个实例问题，但是会强制除T1之外的所有线程等待，实际上会对程序的执行效率造成负面影响。**

```java
// Version 3 
public class Single3 {
    private static Single3 instance;
    private Single3() {}
    public static Single3 getInstance() {
        if (instance == null) {
            synchronized (Single3.class) {
                if (instance == null) {
                    instance = new Single3();
                }
            }
        }
        return instance;
    }
}
```

这个版本的代码看起来有点复杂，注意其中有两次`if (instance == null)`的判断，这个叫做『双重检查 Double-Check』。

* 第一个null判断，是为了解决Version2中的效率问题，只有instance是null的时候，才进入下面的synchronized的代码段，大大减少了几率。
* 第二个判断就是跟上面一个版本一样，是为了防止可能出现的实例情况。

这么优化和同步锁的原理有关：如果有两个线程（T1、T2）同时执行到这个方法时，会有其中一个线程T1获得同步锁，得以继续执行，而另一个线程T2则需要等待，当第T1执行完毕getInstance之后（完成了null判断、对象创建、获得返回值之后）

使用两个判断后，在第一次判断之后直接跳了出去。

第二个判断的用处是，如果出现了两个线程一起访问第一个if的情况，然后到了锁，其中一个线程进去创建对象，如果没有这第二个if判断的话，那么等线程一释放了锁之后，线程二就会进去创建新对象了。


#### 进阶

首先介绍概念 ：`原子操作`、`指令重排`。

##### 原子操作

比如，简单的赋值是一个原子操作：

> m = 6; // 这是个原子操作

假如m原先的值为0，那么对于这个操作，要么执行成功m变成了6，要么是没执行m还是0，而不会出现诸如m=3这种中间态——即使是在并发的线程中。

而，声明并赋值就不是一个原子操作：

> int n = 6; // 这不是一个原子操作

对于这个语句，至少有两个操作：
①声明一个变量n
②给n赋值为6
——这样就会有一个中间状态：变量n已经被声明了但是还没有被赋值的状态。
——这样，在多线程中，由于线程执行顺序的不确定性，如果两个线程都使用m，就可能会导致不稳定的结果出现。

##### 指令重排

简单来说，就是计算机为了提高执行效率，会做的一些优化，在不影响最终结果的情况下，可能会对一些语句的执行顺序进行调整。
比如，这一段代码：



```java
int a ;   // 语句1 
a = 8 ;   // 语句2
int b = 9 ;     // 语句3
int c = a + b ; // 语句4
```

正常来说，对于顺序结构，执行的顺序是自上到下，也即1234。
但是，由于`指令重排`的原因，因为不影响最终的结果，所以，实际执行的顺序可能会变成3124或者1324。
由于语句3和4没有原子性的问题，语句3和语句4也可能会拆分成原子操作，再重排。
——也就是说，对于非原子性的操作，在不影响最终结果的情况下，其拆分成的原子操作可能会被重新排列执行顺序。

上面的代码主要问题：

>主要在于singleton = new Singleton()这句，这并非是一个原子操作，事实上在 JVM 中这句话大概做了下面 3 件事情。
>
>1. 给 singleton 分配内存
>2. 调用 Singleton 的构造函数来初始化成员变量，形成实例
>3. 将singleton对象指向分配的内存空间（执行完这步 singleton才是非 null 了） 但是在 JVM 的即时编译器中存在指令重排序的优化。也就是说上面的第二步和第三步的顺序是不能保证的，最终的执行顺序可能是 1-2-3 也可能是 1-3-2。如果是后者，则在 3 执行完毕、2 未执行之前，被线程二抢占了，这时 instance 已经是非 null 了（但却没有初始化），所以线程二会直接返回 instance，然后使用，然后顺理成章地报错。

就是说，由于有一个『instance已经不为null但是仍没有完成初始化』的中间状态，而这个时候，如果有其他线程刚好运行到第一层`if (instance == null)`这里，这里读取到的instance已经不为null了，所以就直接把这个中间状态的instance拿去用了，就会产生问题。
这里的关键在于——线程T1对instance的写操作没有完成，线程T2就执行了读操作。

当然这种几率是非常小的。


##### 完全体：

只需要给instance的声明加上`volatile`关键字即可，Version4版本：

```java
// Version 4 
public class Single4 {
    private static volatile Single4 instance;
    private Single4() {}
    public static Single4 getInstance() {
        if (instance == null) {
            synchronized (Single4.class) {
                if (instance == null) {
                    instance = new Single4();
                }
            }
        }
        return instance;
    }
}
```

`volatile`关键字的一个作用是禁止`指令重排`，把instance声明为`volatile`之后，对它的写操作就会有一个`内存屏障`，这样，在它的赋值完成之前，就不用会调用读操作。

>  volatile阻止的不是*singleton = new Singleton()*这句话内部[1-2-3]的指令重排，而是保证了在一个写操作（[1-2-3]）完成之前，不会调用读操作（`if (instance == null)`）。

### 饿汉式

如上所说，`饿汉式`单例是指：指全局的单例实例在类装载时构建的实现方式。

由于类装载的过程是由类加载器（ClassLoader）来执行的，这个过程也是由JVM来保证同步的，所以这种方式先天就有一个优势——能够免疫许多由多线程引起的问题。

```java
//饿汉式实现
public class SingleB {
    private static final SingleB INSTANCE = new SingleB();
    private SingleB() {}
    public static SingleB getInstance() {
        return INSTANCE;
    }
}
```

对于一个饿汉式单例的写法来说，它基本上是完美的了。

所以它的缺点也就只是饿汉式单例本身的缺点所在了——由于INSTANCE的初始化是在类加载时进行的，而类的加载是由ClassLoader来做的，所以开发者本来对于它初始化的时机就很难去准确把握：

1. 可能由于初始化的太早，造成资源的浪费
2. 如果初始化本身依赖于一些其他数据，那么也就很难保证其他数据会在它初始化之前准备好。

当然，如果所需的单例占用的资源很少，并且也不依赖于其他数据，那么这种实现方式也是很好的。

### Effective Java中提供的实现方式

```java
// Effective Java 第一版推荐写法
public class Singleton {
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    private Singleton (){}
    public static final Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```

我佛了，合二为一，既使用了ClassLoader来保证了同步，同时又能让开发者控制类加载的时机。从内部看是一个饿汉式的单例，但是从外部看来，又的确是懒汉式的实现。

```java
// Effective Java 第二版推荐写法
public enum SingleInstance {
    INSTANCE;
    public void fun1() { 
        // do something
    }
}

// 使用
SingleInstance.INSTANCE.fun1();
```

这是一个枚举类型，极简。
由于创建枚举实例的过程是线程安全的，所以这种写法也没有同步的问题。

作者对这个方法的评价：

> 这种写法在功能上与共有域方法相近，但是它更简洁，无偿地提供了序列化机制，绝对防止对此实例化，即使是在面对复杂的序列化或者反射攻击的时候。虽然这中方法还没有广泛采用，但是单元素的枚举类型已经成为实现Singleton的最佳方法。

枚举单例这种方法问世一些，许多分析文章都称它是实现单例的最完美方法——写法超级简单，而且又能解决大部分的问题。
这种方法虽然很优秀，但是它仍然不是完美的——比如，在需要继承的场景，它就不适用了。



### 拓展：enum

最后一种写法涉及到一些Java枚举类的不常见使用方法，简单介绍一下。

```java
    public enum Day2 {
        MONDAY("星期一",1),
        TUESDAY("星期二",2),
        WEDNESDAY("星期三",3),
        THURSDAY("星期四",4),
        FRIDAY("星期五",5),
        SATURDAY("星期六",6),
        SUNDAY("星期日",7);//记住要用分号结束

    private String desc;//文字描述
    private Integer code; //对应的代码

    /**
     * 私有构造,防止被外部调用
     * @param desc
     */
    private Day2(String desc，Integer code){
        this.desc=desc;
     this.code=code;
        }
    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     * @return
     */
    public String getDesc(){
        return desc;
    }

     /**
         * 定义方法,返回代码,跟常规类的定义没区别
         * @return
         */
        public String getCode(){
            return code;
        }
        
    public static void main(String[] args){
        for (Day2 day:Day2.values()) {
            System.out.println("name:"+day.name()+
                    ",desc:"+day.getDesc());
        }
    }

```





### 拓展：类的加载和初始化简单了解

####  类什么时候加载

类的加载是通过类加载器（Classloader）完成的，它既可以是饿汉式[eagerly load]（只要有其它类引用了它就加载）加载类，也可以是懒加载[lazy load]（等到类初始化发生的时候才加载）。不过我相信这跟不同的JVM实现有关，然而他又是受JLS保证的（当有静态初始化需求的时候才被加载）。

#### 类什么时候初始化

加载完类后，类的初始化就会发生，意味着它会初始化所有类静态成员，以下情况一个类被初始化：

1. 实例通过使用new()关键字创建或者使用class.forName()反射，但它有可能导致ClassNotFoundException。
2. 类的静态方法被调用
3. 类的静态域被赋值
4. 静态域被访问，而且它不是常量
5. 在顶层类中执行assert语句

反射同样可以使类初始化，比如java.lang.reflect包下面的某些方法，JLS严格的说明：一个类不会被任何除以上之外的原因初始化。

#### 类是如何被初始化的

现在我们知道什么时候触发类的初始化了，他精确地写在Java语言规范中。但了解清楚 域（fields，静态的还是非静态的）、块（block静态的还是非静态的）、不同类（子类和超类）和不同的接口（子接口，实现类和超接口）的初始化顺序也很重要类。事实上很多核心Java面试题和SCJP问题都是基于这些概念，下面是类初始化的一些规则：

1. 类从顶至底的顺序初始化，所以声明在顶部的字段的早于底部的字段初始化
2. 超类早于子类和衍生类的初始化
3. 如果类的初始化是由于访问静态域而触发，那么只有声明静态域的类才被初始化，而不会触发超类的初始化或者子类的初始化即使静态域被子类或子接口或者它的实现类所引用。
4. 接口初始化不会导致父接口的初始化。
5. 静态域的初始化是在类的静态初始化期间，非静态域的初始化时在类的实例创建期间。这意味这静态域初始化在非静态域之前。
6. 非静态域通过构造器初始化，子类在做任何初始化之前构造器会隐含地调用父类的构造器，他保证了非静态或实例变量（父类）初始化早于子类

例子：

```java
/**
 * Java program to demonstrate class loading and initialization in Java.
 */
public class ClassInitializationTest {
 
    public static void main(String args[]) throws InterruptedException {
 
        NotUsed o = null; //this class is not used, should not be initialized
        Child t = new Child(); //initializing sub class, should trigger super class initialization
        System.out.println((Object)o == (Object)t);
    }
}
 
/**
 * Super class to demonstrate that Super class is loaded and initialized before Subclass.
 */
class Parent {
    static { System.out.println("static block of Super class is initialized"); }
    {System.out.println("non static blocks in super class is initialized");}
}
 
/**
 * Java class which is not used in this program, consequently not loaded by JVM
 */
class NotUsed {
    static { System.out.println("NotUsed Class is initialized "); }
}
 
/**
 * Sub class of Parent, demonstrate when exactly sub class loading and initialization occurs.
 */
class Child extends Parent {
    static { System.out.println("static block of Sub class is initialized in Java "); }
    {System.out.println("non static blocks in sub class is initialized");}
}
 

```

Output:
static block of Super class is initialized
static block of Sub class is initialized in Java
non static blocks in super class is initialized
non static blocks in sub class is initialized
false

从上面结果可以看出：

1. 超类初始化早于子类
2. 静态变量或代码块初始化早于非静态块和域
3. 没使用的类根本不会被初始化，因为他没有被使用

再来一个例子：

```java
/**
 * Another Java program example to demonstrate class initialization and loading in Java.
 */
 
public class ClassInitializationTest {
 
    public static void main(String args[]) throws InterruptedException {
 
       //accessing static field of Parent through child, should only initialize Parent
       System.out.println(Child.familyName);
    }
}
 
class Parent {
    //compile time constant, accessing this will not trigger class initialization
    //protected static final String familyName = "Lawson";
 
    protected static String familyName = "Lawson";
 
    static { System.out.println("static block of Super class is initialized"); }
    {System.out.println("non static blocks in super class is initialized");}
}
 
Output:
static block of Super class is initialized
Lawson
```

分析：

1. 这里的初始化发生是因为有静态域被访问，而且不一个编译时常量。如果声明的”familyName”是使用final关键字修饰的编译时常量使用（就是上面的注释代码块部分）超类的初始化就不会发生。
2. 尽管静态与被子类所引用但是也仅仅是超类被初始化

还有另外一个例子与接口相关的，JLS清晰地解释子接口的初始化不会触发父接口的初始化。强烈推荐阅读JLS14.4理解类加载和初始化细节。以上所有就是有关类被初始化和加载的全部内容。

#### 总结：

类什么时候被加载/类加载时机：
第一：生成该类对象的时候，会加载该类及该类的所有父类；
第二：访问该类的静态成员的时候；

第三：CLASS．FORNAME("类名")；

加载完以后JVM中就有了该类的元数据，知道这个CLASS的成员变量和方法等信息，当要NEW一个类的实例时就会根据这个CLASS对象去内存中开辟空间，存放该类的实例对象

先初始化父类的静态代码--->初始化子类的静态代码-->初始化父类的非静态代码--->初始化父类构造函数--->初始化子类非静态代码--->初始化子类构造函数

JVM是比较底层的内容，上面只是简单一些推理，今后还要多加学习。

### 拓展：volatile

在Sun的JDK官方文档是这样形容volatile的：

The Java programming language provides a second mechanism, volatile fields, that is more convenient than locking for some purposes. A field may be declared volatile, in which case the Java Memory Model ensures that all threads see a consistent value for the variable.

如果一个变量加了volatile关键字，就会告诉编译器和JVM的内存模型：这个变量是对所有线程共享的、可见的，每次jvm都会读取最新写入的值并使其最新值在所有CPU可见。**volatile似乎是有时候可以代替简单的锁，似乎加了volatile关键字就省掉了锁。但又说volatile不能保证原子性（java程序员很熟悉这句话：volatile仅仅用来保证该变量对所有线程的可见性，但不保证原子性）**。让人费解。

**不要将volatile用在getAndOperate场合（这种场合不原子，需要再加锁），仅仅set或者get的场景是适合volatile的**。

#### volatile没有原子性举例：AtomicInteger自增

例如你让一个volatile的integer自增（i++），其实要分成3步：1）读取volatile变量值到local； 2）增加变量的值；3）把local的值写回，让其它的线程可见。这3步的jvm指令为：

```
mov    0xc(%r10),%r8d ; Load
inc    %r8d           ; Increment
mov    %r8d,0xc(%r10) ; Store
lock addl $0x0,(%rsp) ; StoreLoad Barrier
```

注意最后一步是**内存屏障**。

#### 什么是内存屏障（Memory Barrier）？

内存屏障是一个CPU指令。基本上，它是这样一条指令： a) 确保一些特定操作执行的顺序； b) 影响一些数据的可见性(可能是某些指令执行后的结果)。编译器和CPU可以在保证输出结果一样的情况下对指令重排序，使性能得到优化。插入一个内存屏障，相当于告诉CPU和编译器先于这个命令的必须先执行，后于这个命令的必须后执行。内存屏障另一个作用是强制更新一次不同CPU的缓存。例如，一个写屏障会把这个屏障前写入的数据刷新到缓存，这样任何试图读取该数据的线程将得到最新值，而不用考虑到底是被哪个cpu核心或者哪颗CPU执行的。

内存屏障和volatile什么关系？上面的虚拟机指令里面有提到，如果你的字段是volatile，Java内存模型将在写操作后插入一个写屏障指令，在读操作前插入一个读屏障指令。这意味着如果你对一个volatile字段进行写操作，你必须知道：1、一旦你完成写入，任何访问这个字段的线程将会得到最新的值。2、在你写入前，会保证所有之前发生的事已经发生，并且任何更新过的数据值也是可见的，因为内存屏障会把之前的写入值都刷新到缓存。

#### volatile为什么没有原子性?

明白了内存屏障([memory barrier](http://en.wikipedia.org/wiki/Memory_barrier)) 这个CPU指令，回到前面的JVM指令：从Load到store到内存屏障，一共4步，其中最后一步jvm让这个最新的变量的值在所有线程可见，也就是最后一步让所有的CPU内核都获得了最新的值，但**中间的几步（从Load到Store）**是不安全的，中间如果其他的CPU修改了值将会丢失。下面的测试代码可以实际测试voaltile的自增没有原子性：

```java
    private static volatile long _longVal = 0;
     
    private static class LoopVolatile implements Runnable {
        public void run() {
            long val = 0;
            while (val < 10000000L) {
                _longVal++;
                val++;
            }
        }
    }
     
    private static class LoopVolatile2 implements Runnable {
        public void run() {
            long val = 0;
            while (val < 10000000L) {
                _longVal++;
                val++;
            }
        }
    }
     
    private  void testVolatile(){
        Thread t1 = new Thread(new LoopVolatile());
        t1.start();
         
        Thread t2 = new Thread(new LoopVolatile2());
        t2.start();
         
        while (t1.isAlive() || t2.isAlive()) {
        }
 
        System.out.println("final val is: " + _longVal);
    }
 
Output:-------------
     
final val is: 11223828
final val is: 17567127
final val is: 12912109
```

#### volatile没有原子性举例：singleton单例模式实现

这是一段线程不安全的singleton（单例模式）实现，尽管使用了volatile：

```java
public class wrongsingleton {
    private static volatile wrongsingleton _instance = null; 
 
    private wrongsingleton() {}
 
    public static wrongsingleton getInstance() {
 
        if (_instance == null) {
            _instance = new wrongsingleton();
        }
 
        return _instance;
    }
}
```

下面的测试代码可以测试出是线程不安全的：

```java
public class wrongsingleton {
    private static volatile wrongsingleton _instance = null; 
 
    private wrongsingleton() {}
 
    public static wrongsingleton getInstance() {
 
        if (_instance == null) {
            _instance = new wrongsingleton();
            System.out.println("--initialized once.");
        }
 
        return _instance;
    }
}
 
private static void testInit(){
         
        Thread t1 = new Thread(new LoopInit());
        Thread t2 = new Thread(new LoopInit2());
        Thread t3 = new Thread(new LoopInit());
        Thread t4 = new Thread(new LoopInit2());
        t1.start();
        t2.start();
        t3.start();
        t4.start();
         
        while (t1.isAlive() || t2.isAlive() || t3.isAlive()|| t4.isAlive()) {
             
        }
 
    }
输出：有时输出"--initialized once."一次，有时输出好几次
```

原因自然和上面的例子是一样的。因为**volatile保证变量对线程的可见性，但不保证原子性**。

#### 为什么AtomicXXX具有原子性和可见性？

就拿AtomicLong来说，它既解决了上述的volatile的原子性没有保证的问题，又具有可见性。它是如何做到的？CAS（比较并交换）指令。 其实AtomicLong的源码里也用到了volatile，但只是用来读取或写入，见源码：

```java
public class AtomicLong extends Number implements java.io.Serializable {
    private volatile long value;
 
    /**
     * Creates a new AtomicLong with the given initial value.
     *
     * @param initialValue the initial value
     */
    public AtomicLong(long initialValue) {
        value = initialValue;
    }
 
    /**
     * Creates a new AtomicLong with initial value {@code 0}.
     */
    public AtomicLong() {
    }
```

其CAS源码核心代码为：

```java
int compare_and_swap (int* reg, int oldval, int newval) 
{
  ATOMIC();
  int old_reg_val = *reg;
  if (old_reg_val == oldval) 
     *reg = newval;
  END_ATOMIC();
  return old_reg_val;
}
```

虚拟机指令为：

```
mov    0xc(%r11),%eax       ; Load
mov    %eax,%r8d            
inc    %r8d                 ; Increment
lock cmpxchg %r8d,0xc(%r11) ; Compare and exchange
```

因为CAS是基于乐观锁的，也就是说当写入的时候，如果寄存器旧值已经不等于现值，说明有其他CPU在修改，那就继续尝试。所以这就保证了操作的原子性。

### 拓展 在序列化和反序列化之后可能会出现新的单例对象

>  根据Effective Java中指出的，要使单例类使用这两种方法中的任何一种（Chapter 12），仅仅在其声明中添加实现 serializable 是不够的。要维护单例保证，应声明所有实例字段为 transient，并提供 readResolve 方法（Item-89）。否则，每次反序列化实例时，都会创建一个新实例，在我们的示例中，这会导致出现虚假的 Elvis。为了防止这种情况发生，将这个 readResolve 方法添加到 单例类中：

```java
// readResolve method to preserve singleton property
private Object readResolve() {
    // Return the one true Elvis and let the garbage collector
    // take care of the Elvis impersonator.
    return INSTANCE;
}
```

