package effectiveJava.test;

import doubleCheckLock.DclSingleton;
import effectiveJava.EjSingleton3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
