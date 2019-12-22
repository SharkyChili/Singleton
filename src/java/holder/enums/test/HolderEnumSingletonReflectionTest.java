package holder.enums.test;

import holder.HolderSington;
import holder.enums.HolderEnumSingleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class HolderEnumSingletonReflectionTest {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        HolderEnumSingleton s1 = HolderEnumSingleton.getInstance();
        HolderEnumSingleton s2 = HolderEnumSingleton.getInstance();

        Constructor<?>[] constructors = HolderEnumSingleton.class.getDeclaredConstructors();
        Constructor<HolderEnumSingleton> constructor = (Constructor<HolderEnumSingleton>)constructors[0];
        constructor.setAccessible(true);
        HolderEnumSingleton s3 = constructor.newInstance();
        System.out.println("非反射下的两个实例是否相同："+ (s1==s2));
        System.out.println("反射下的两个实例是否相同："+ (s1==s3));
    }}
