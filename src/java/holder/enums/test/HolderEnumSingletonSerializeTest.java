package holder.enums.test;

import holder.HolderSington;
import holder.enums.HolderEnumSingleton;

import java.io.*;

public class HolderEnumSingletonSerializeTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HolderEnumSingleton s1 = HolderEnumSingleton.getInstance();
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Singleton.obj"));
        outputStream.writeObject(s1);
        outputStream.flush();
        outputStream.close();

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Singleton.obj"));
        HolderEnumSingleton s2 = (HolderEnumSingleton) inputStream.readObject();
        inputStream.close();

        System.out.println(s1);
        System.out.println(s2);
        System.out.println("序列化前后是否为同一个：" + (s1 == s2));
    }}
