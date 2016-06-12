package hello;

import library.RandomHello;

public class HelloWorld {

    public static void main(String[] args){
        System.out.println(hello("Gradle"));
        System.out.println(new RandomHello().randomHello(5));
    }

    public static String hello(String name){
        return "Hello "+name+"!";
    }
}
