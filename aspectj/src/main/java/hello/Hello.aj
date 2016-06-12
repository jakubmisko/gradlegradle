package hello;

public aspect Hello {
    pointcut hello(): call(public void HelloWorld.hello(*));

    before(): hello(){
        System.out.println("Aspectj from gradle:");
    }
}