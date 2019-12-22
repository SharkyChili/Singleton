# Singleton

git:  https://github.com/fw1036994377/Singleton.git  
非线程安全的，就不说了。
# 双重加锁验证Double Check Lock
首先是地球人都知道的双重检查单例，这个不用说了吧，不会的该打屁屁了  
```java
public class DclSingleton {
    private volatile static DclSingleton instance = null;

    private DclSingleton(){}

    public static DclSingleton getInstance() {
        if (instance == null) {
            synchronized (DclSingleton.class) {
                if (instance == null) {
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }
}
```
# 《Effective Java》 2nd English version
学习Effective java，顺便翻译一下  
## 翻译：  
在java1.5之前，有两种实现单例的方法，两种都是讲构造方法私有化然后暴露一个public static的属性来提供对单例的访问。  
## 第一种：  
```java
public class EjSingleton1 {
    //public
    public static final EjSingleton1 instance = new EjSingleton1();

    private EjSingleton1(){}

    public void leaveTheBuilding(){

    }
}
```
注意：有权限的客户端可以通过AccessibleObject.setAccessible来调用构造方法生成新的实例。如果你需要防止这种情况，修改构造方法，使它在被第二次被调用时抛出异常。  
## 第二种
```java
public class EjSingleton2 {
    //private
    private static final EjSingleton2 instance = new EjSingleton2();
    
    private EjSingleton2(){}
    
    public static EjSingleton2 getInstance(){
        return instance;
    }
    
    public void leaveTheBuilding(){}
}
```
第二种和第一种大同小异，只是调用方式从属性变成了方法而已。同样有第一种的反射问题。  
除此之外，在序列化时也要有些额外的工作，这里不展开了。  
## 1.5之后  
简单的用单例模式即可。  
```java
public enum  EjSingleton3 {
    INSTANCE;
    
    public void leaveTheBuilding(){}
}
```  
这种方式相当于第二种方式里的public属性的方式，除此之外，它更加简明，**提供了序列化机制（即解决了序列化时的问题），即使在面对复杂的序列化和反射时，也能保证不出现多实例问题。**   
a single-element enum type is the best way to implement a singleton.  
**单元素枚举是实现单例模式最好的方法。**  


## Static线程安全的问题
《深入理解java虚拟机》第二版  
P225：  
<clinit>()方法是由编译器自动收集类中的所有**类变量的赋值动作**和静态语句块（static{}块）中的语句合并产生的。  
P226：  
虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确的加锁、同步，如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的<clinit>（）方法，其他线程都需要阻塞等待，直到活动线程执行<cinit>()方法执行完毕。  

# 枚举相对双重加锁的优势  
## 双重加锁
### 反射的问题
talk is cheap，对双重加锁的单例模式来一番反射操作试试。  
```java
public class DclSingletonTest {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DclSingleton s1 = DclSingleton.getInstance();
        DclSingleton s2 = DclSingleton.getInstance();

        Constructor<?>[] constructors = DclSingleton.class.getDeclaredConstructors();
        Constructor<DclSingleton> constructor = (Constructor<DclSingleton>)constructors[0];
        constructor.setAccessible(true);
        DclSingleton s3 = constructor.newInstance();
        System.out.println("非反射下的两个实例是否相同："+ (s1==s2));
        System.out.println("反射下的两个实例是否相同："+ (s1==s3));
    }
}
```
```java
非反射下的两个实例是否相同：true
反射下的两个实例是否相同：false
```  
### 序列化的问题
```java
public class DclSingletonSerializeTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DclSingleton s1 = DclSingleton.getInstance();
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Singleton.obj"));
        outputStream.writeObject(s1);
        outputStream.flush();
        outputStream.close();
        
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Singleton.obj"));
        DclSingleton s2 = (DclSingleton) inputStream.readObject();
        inputStream.close();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println("序列化前后是否为同一个：" + (s1==s2));
    }
}
```
```java
doubleCheckLock.DclSingleton@7f31245a
doubleCheckLock.DclSingleton@4dd8dc3
序列化前后是否为同一个：false
```
结果很尴尬啊，笔试写了那么多次的双重检查单例好像也不是那么靠谱啊。 

## 枚举试一下
### 反射
```java
public class EjSingleton3ReflectionTest {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        EjSingleton3 s1 = EjSingleton3.INSTANCE;
        EjSingleton3 s2 = EjSingleton3.INSTANCE;

        Constructor<?>[] constructors = EjSingleton3.class.getDeclaredConstructors();
        Constructor<EjSingleton3> constructor = (Constructor<EjSingleton3>)constructors[0];
        constructor.setAccessible(true);
        EjSingleton3 s3 = constructor.newInstance();
        System.out.println("非反射下的两个实例是否相同："+ (s1==s2));
        System.out.println("反射下的两个实例是否相同："+ (s1==s3));
    }
}
```
```java
Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
	at java.lang.reflect.Constructor.newInstance(Constructor.java:417)
	at effectiveJava.test.EjSingleton3ReflectionTest.main(EjSingleton3ReflectionTest.java:17)
```
直接报错，有点刚啊  
打个断点看看  
![](https://user-gold-cdn.xitu.io/2019/12/22/16f2e360bb78d575?w=1071&h=424&f=png&s=60221)  
可以发现，得到了一个参数为(String,int)的构造方法，纳尼，看不懂啊  
看看EjSingleton3编译之后的样子：
![](https://user-gold-cdn.xitu.io/2019/12/22/16f2e3576f4bf2f8?w=778&h=174&f=png&s=15222)
可以看到我们的单例类继承了Enum类，打开Enum类
```java
    protected Enum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }
```
既然如此，我们就用父类的构造器好了
```java
public class EjSingleton3ReflectionTest {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        EjSingleton3 s1 = EjSingleton3.INSTANCE;
        EjSingleton3 s2 = EjSingleton3.INSTANCE;

//        Constructor<?>[] constructors = EjSingleton3.class.getDeclaredConstructors();
//        Constructor<EjSingleton3> constructor = (Constructor<EjSingleton3>)constructors[0];
//        Constructor<EjSingleton3> constructor = (Constructor<EjSingleton3>)constructors[0];
        Constructor<EjSingleton3> constructor = EjSingleton3.class.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        EjSingleton3 s3 = constructor.newInstance();
        System.out.println("非反射下的两个实例是否相同："+ (s1==s2));
        System.out.println("反射下的两个实例是否相同："+ (s1==s3));
    }
}
```
```java
Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
	at java.lang.reflect.Constructor.newInstance(Constructor.java:417)
	at effectiveJava.test.EjSingleton3ReflectionTest.main(EjSingleton3ReflectionTest.java:19)
```
我们还是进到Constructor.newInstance方法好了，可以看到
```java
    public T newInstance(Object ... initargs)
        throws InstantiationException, IllegalAccessException,
               IllegalArgumentException, InvocationTargetException
    {
        if (!override) {
            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
                Class<?> caller = Reflection.getCallerClass();
                checkAccess(caller, clazz, null, modifiers);
            }
        }
        //看这里
        if ((clazz.getModifiers() & Modifier.ENUM) != 0)
            throw new IllegalArgumentException("Cannot reflectively create enum objects");
        ConstructorAccessor ca = constructorAccessor;   // read volatile
        if (ca == null) {
            ca = acquireConstructorAccessor();
        }
        @SuppressWarnings("unchecked")
        T inst = (T) ca.newInstance(initargs);
        return inst;
    }
```
龟龟，看来是java直接禁止了新建enum类啊，从报错其实也看得出来，看来**enum单例模式是利用JAVA内部的机制防止了反射产生多个实例的可能性**。
### 序列化
```java
public class EjSingleton3SerializeTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        EjSingleton3 s1 = EjSingleton3.INSTANCE;
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("EjSingleton3.obj"));
        outputStream.writeObject(s1);
        outputStream.flush();
        outputStream.close();

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("EjSingleton3.obj"));
        EjSingleton3 s2 = (EjSingleton3) inputStream.readObject();
        inputStream.close();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println("序列化前后是否为同一个：" + (s1==s2));
    }
}
```
```java
INSTANCE
INSTANCE
序列化前后是否为同一个：true
```
给力啊，而且可以看到，toString方法都被重写了，不用多说，又是ENUM类干的  

# 套娃模式(非重点，娱乐)
## Holder  
```java
public class HolderSington {
    /**
     * 静态的成员内部类
     * 内部类的实例与外部类的实例没有绑定关系
     * 也就是只有被调用到才会装载，也就是实现了延迟加载
     */
    private static class InnerHolder{
        /**
         * 静态初始化，由JVM来保证安全
         */
        private static HolderSington instance = new HolderSington();
    }

    private HolderSington(){}

    public static HolderSington getInstance(){
        return InnerHolder.instance;
    }
}
```
这种方式，我觉得算是饿汉式与懒汉式的一种结合，既利用了static语句的实现了安全，又实现了延迟加载  
**然而，并通不过反射测试和序列化测试，尴尬**
## holder+enum 套娃模式开启
```java
public class HolderEnumSingleton {
    private HolderEnumSingleton(){}

    private enum InnerHolder{
        INSTANCE;
        private static HolderEnumSingleton instance = new HolderEnumSingleton();

        private HolderEnumSingleton getInstance(){
            return instance;
        }
    }

    public static HolderEnumSingleton getInstance(){
        return InnerHolder.INSTANCE.getInstance();
    }
}
```
**悲伤的是，也通不过反射测试和序列化测试，尴尬**  
这里其实也可以类比成，spring里一个单例对象如何注入多例对象，在不进行额外的操作的情况下，单例内部当然只能有单例，只有在进行一些额外操作之后，才能注入多例，例如  https://my.oschina.net/u/3994737/blog/2643652

# 总结
双重加锁和枚举，都掌握一下好了，套娃模式还是算了吧  
我的理解：双重加锁，是在我们的程序的层面进行控制，holder利用的是jvm的机制，枚举是在jdk层面进行控制，不过这不重要哈哈哈哈。  

以后笔试用枚举实现单例？万一面试官不懂咋办，心想你就给我写个这个？那就尴尬了
