package eloamComJavaDemo;

public class Test {

    public native void sayHello();

    public static void main(String[] args) {
        // ���ض�̬���ӿ�
        boolean a = Clibrary.EloamView.EloamGlobal_InitDevs();

        // ���ö�̬���ӿⷽ��
//        Test callCMethod = new Test();
//        callCMethod.sayHello();
    }

}
