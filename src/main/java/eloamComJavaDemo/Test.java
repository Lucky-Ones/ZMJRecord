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
//        // 加载动态链接库
//        boolean a = Clibrary.EloamView.EloamGlobal_InitDevs();
//
//        // 调用动态链接库方法
////        Test callCMethod = new Test();
////        callCMethod.sayHello();


//        long time1 = System.currentTimeMillis();
//        String picname = time1 + ".jpg";
//        String sPicName = "./pictures/" + picname;
        String sPicName = "C:\\Users\\83811\\Desktop\\5.jpg";
        System.out.println(sPicName);
//        long time2 = System.currentTimeMillis();
//        long timecent =  time2-time1;//误差时间
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
//        System.setProperty("java.awt.headless", "false");
        System.load(url.getPath());
        AreaCalculation areaCalculation = new AreaCalculation();//计算面积
        Thread.sleep(1250);
        Mat image = imread(sPicName, 1);
        //Mat image = imread("./pictures/zmj.jpg", 1);
        double area = areaCalculation.getArea(image,"OTSU",150);//面积
        //double area = random();
        String arean = String.valueOf(area);
//				areaLabel.setText("芝麻将面积为："+arean);
        System.out.println("芝麻酱面积"+arean);








    }

}
