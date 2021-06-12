import java.util.ArrayList;
import java.util.List;

/**
 * @program: SimpleFactoryPattern
 * @description: 中介者模式
 * @author: Fly.Hugh
 * @create: 2021-06-12 09:07
 **/
public class MediatorPattern {
    public static void main(String[] args) {
        // 定义中介者
        MarriageAgencyImpl marriageAgency = new MarriageAgencyImpl();

        // 第一位男嘉宾
        Person giao桑 = new Person("Giao桑", 18, Sex.MALE, 18, marriageAgency);

        // 四位女嘉宾
        Person 迪丽热巴 = new Person("迪丽热巴", 25, Sex.FEMALE, 18, marriageAgency);
        Person 杨幂 = new Person("杨幂", 25, Sex.FEMALE, 18, marriageAgency);
        Person 高圆圆 = new Person("高圆圆", 25, Sex.FEMALE, 18, marriageAgency);
        Person 郭老师 = new Person("郭老师", 18, Sex.FEMALE, 18, marriageAgency);

/*        marriageAgency.register(giao桑);
        marriageAgency.register(迪丽热巴);
        marriageAgency.register(杨幂);
        marriageAgency.register(高圆圆);
        marriageAgency.register(郭老师);*/

        marriageAgency.pair(giao桑);
    }
}

// 中介者类 -- 婚姻中介所
interface MarriageAgency {
    void register(Person person); // 注册会员
    void pair(Person person); // 为Person配对
}

// 参与者
class Person {
    String name;
    int age;
    Sex sex;
    int requestAge;
    MarriageAgency agency;

    public Person(String name, int age, Sex sex, int requestAge, MarriageAgency agency) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.requestAge = requestAge;
        this.agency = agency;
        agency.register(this);// 注册会员
    }

    public void findPartner() {
        agency.pair(this);
    }
}

// 中介者实现类
class MarriageAgencyImpl implements MarriageAgency {
    List<Person> people = new ArrayList<>(); // 女会员

    @Override
    public void register(Person person) {
        people.add(person);
    }

    @Override
    public void pair(Person person) {
        for (Person p : people) {
            if (p.age == person.requestAge && p.sex != person.sex) {
                System.out.println("将 " + person.name + " 与 " + p.name + " 送入洞房！ ");
            }
        }
    }
}

enum Sex {
    MALE, FEMALE;
}

// 中介者模式是一种行为涉及模式，能让你减少对象之间混乱无序的依赖关系。该模式会限制对象之间的之间交互，迫使他们通过一个中介对象进行合作。
// 优点：简化了对象之间的交互，引入中介者，将各参与者进行解耦，简化各类的设计和实现
// 缺点：中介者模式包含了所有参与者之间的交互细节，可能使中介者对象本身很复杂并且难以维护。