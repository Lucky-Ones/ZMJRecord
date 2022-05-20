package eloamComJavaDemo;

import dao.PictureMapper;
import eloamComJavaDemo.utils.AreaCalculation;
import org.apache.ibatis.session.SqlSession;
import org.opencv.core.Mat;
import pojo.Picture;
import sun.net.www.content.image.png;
import utils.MybatisUtils;

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


//        SqlSession session2 = MybatisUtils.getSession();
//        PictureMapper mapper2 = session2.getMapper(PictureMapper.class);
//        Picture picture = new Picture("fileName3","nowTime","fileName2","area",16);
//        mapper2.addPicture(picture);
//        session2.commit(); //提交事务
//        session2.close();




    }

}
