package doubleCheckLock.test;

import doubleCheckLock.DclSingleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DclSingletonReflectionTest {
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
