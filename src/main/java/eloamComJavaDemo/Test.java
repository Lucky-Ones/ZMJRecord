package eloamComJavaDemo;

import eloamComJavaDemo.utils.AreaCalculation;
import org.opencv.core.Mat;
import sun.net.www.content.image.png;

import java.awt.*;
import java.net.URL;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class Test {

    public native void sayHello();

    public static void main(String[] args) throws Exception {


        URL url = ClassLoader.getSystemResource("F:\\code\\javaProject\\zmj\\ZMJRecord\\src\\main\\resources\\lib\\opencv\\opencv_java455.dll");
        System.out.println(url.getPath());
//        System.load(url.getPath());
        System.load("F:\\code\\javaProject\\zmj\\ZMJRecord\\src\\main\\resources\\lib\\opencv\\opencv_java455.dll");






    }

}
