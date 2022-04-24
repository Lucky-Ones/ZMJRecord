package eloamComJavaDemo;

public class Test {

    public native void sayHello();

    public static void main(String[] args) {
        // 加载动态链接库
        boolean a = Clibrary.EloamView.EloamGlobal_InitDevs();

        // 调用动态链接库方法
//        Test callCMethod = new Test();
//        callCMethod.sayHello();
    }

}
