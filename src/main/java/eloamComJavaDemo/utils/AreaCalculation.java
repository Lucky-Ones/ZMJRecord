package eloamComJavaDemo.utils;

//import com.ec.zmjcalculation.constant.Constant;
import eloamComJavaDemo.constant.Constant;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AreaCalculation {
    public double getArea(Mat image,String model,int thresh) throws Exception {
        double zmjArea;
        double relZmjArea;
//        //截取图像，只剩背景和芝麻酱，选择条件比较苛刻，须对实际图像进行调整。
//        Rect rect =new Rect(220,550,600,600);      //参数须调节
//        Mat image = new Mat(image1,rect);

        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
        System.load(url.getPath());
        if (image.empty()){
            throw new Exception("image is empty!");
        }
        Mat oldImage = image;
        Imgcodecs.imwrite("./opencvImage/oldzmj.jpg", oldImage);

        //图片转灰度
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        //Imgcodecs.imwrite("./opencvImage/grayscale.jpg", image);

        //高斯模糊
        Imgproc.GaussianBlur(image, image, new Size(15, 15), 0);
        //Imgcodecs.imwrite("./opencvImage/gaussianBlur.jpg", image);

        //中值滤波
        Imgproc.medianBlur(image,image,5);
        //Imgcodecs.imwrite("./opencvImage/medianBlur.jpg", image);

        //去除光斑。腐蚀,使高亮区域被周围腐蚀
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        Imgproc.erode(image,image,kernel,new Point(-1,-1),21);
        //Imgcodecs.imwrite("./opencvImage/erode.jpg", image);
        //膨胀，理解为腐蚀后膨胀回来
        Imgproc.dilate(image,image,kernel,new Point(-1,-1),21);
        //Imgcodecs.imwrite("./opencvImage/dilate.jpg", image);

//        //去除阴影，先闭运算
//        Mat closeImage = new Mat();
//        Imgproc.morphologyEx(image,closeImage,Imgproc.MORPH_CLOSE, kernel,new Point(-1,-1),4);
//        Imgcodecs.imwrite("D:/opencvImage/closeImage.jpg", closeImage);
//        // 然后减去原灰度图再取反
//        Mat calcImage = new Mat();
//        Core.subtract(image ,closeImage,calcImage);  //相减
//        //Core.bitwise_not(calcImage,calcImage);      //取反
//        Imgcodecs.imwrite("D:/opencvImage/calcImage.jpg", calcImage);
//        //归一化
//        Imgproc.resize(calcImage,image,new Size(1,1),0,0,Imgproc.INTER_AREA);
//        Imgcodecs.imwrite("D:/opencvImage/resize.jpg", image);

        if(model.equals("OTSU")){
            Imgproc.threshold(image,image,0,255,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
        }
        else if (model.equals("normal")){
            Imgproc.threshold(image,image,thresh,255,Imgproc.THRESH_BINARY);
            Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
        }
        else if(model.equals("adaptive")){
            Imgproc.adaptiveThreshold(image,image,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,21,5);
            Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
        }
        else throw new Exception("没有这种算法");
        //全局二值化操作,固定阈值
        Imgproc.threshold(image,image,0,255,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//        double thresh = Imgproc.threshold(image,image,120,255,Imgproc.THRESH_BINARY);
//        //System.out.println(thresh);
        Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
        //局部二值化操作，自适应阈值
//        Imgproc.adaptiveThreshold(image,image,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,21,5);
//        Imgcodecs.imwrite("D:/opencvImage/binarization.jpg", image);

          //形态学闭操作
//        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3),new Point(-1,-1));
//        Imgproc.morphologyEx(image,image,Imgproc.MORPH_CLOSE, kernel,new Point(-1,-1),2);
//        Imgcodecs.imwrite("D:/opencvImage/structuring.jpg", image);
        //轮廓发现，得到contours和hierarchy
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_NONE, new Point(-1, -1));
        //Imgcodecs.imwrite("./opencvImage/line.jpg", hierarchy);

        //相当于创建和原图尺寸相同一张黑色的图，用于后面画线作图
        Mat contoursImg = Mat.zeros(oldImage.size(),CvType.CV_8UC3);

        double areas[] = new double[contours.size()];
        for(int i=0;i<contours.size();i++){
      //      Rect rect = Imgproc.boundingRect(contours.get(i));
//            if (rect.width < mat.cols() / 2)
//                continue;
            //在contoursImg上绘制最大的轮廓
            Imgproc.drawContours(contoursImg,contours,i, new Scalar(0,0,255),
                    2,8,hierarchy,0,new Point(0, 0));
            double area = Imgproc.contourArea(contours.get(i));
            areas[i] = area;
            System.out.println("第"+i+"个图像的面积是 "+area);
        }
        Arrays.sort(areas);
        if(areas.length>1){
            zmjArea = areas[areas.length-2];
            relZmjArea= Constant.CONTAINER_WIDTH*Constant.CONTAINER_HEIGHT*areas[areas.length-2]/areas[areas.length-1];
        }
        else{
            zmjArea = areas[0];
            relZmjArea = 0;
        }
        Imgcodecs.imwrite("./opencvImage/final.jpg", contoursImg);
        return relZmjArea;
    }

    public double getAreaByKmeans(Mat image) throws IOException {
        int width = image.width();
        int height = image.height();
        int pointCount = width * height;
        int dims = image.channels();
        Mat points=new Mat(pointCount, dims, CvType.CV_32F);
        int index=0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                index = i * width + j;
                points.put(index,0,image.get(i, j)[0]);
                points.put(index,1,image.get(i, j)[1]);
                points.put(index,2,image.get(i, j)[2]);
            }
        }
        Mat bestLabels=new Mat();
        Mat centers=new Mat(3, 3, CvType.CV_32F);
        TermCriteria criteria=new TermCriteria(TermCriteria.COUNT + TermCriteria.EPS, 10, 0.1);
        Core.kmeans(points, 2, bestLabels, criteria, 3, Core.KMEANS_PP_CENTERS, centers);
        double[][] color={{0,0,255},{0,255,0},{255,0,0}};

        Mat result = Mat.zeros(image.size(), image.type());
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                index = i * width + j;
                int lable = (int)bestLabels.get(index,0)[0];
                result.put(i, j, color[lable]);
            }
        }
        Mat2BufferedImageSave(result);
        double area=0;
        //图片转灰度
        Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2GRAY);
        //Imgcodecs.imwrite("D:/opencvImage/grayscale.jpg", result);

        //高斯模糊
        Imgproc.GaussianBlur(result, result, new Size(15, 15), 0);
        //Imgcodecs.imwrite("D:/opencvImage/gaussianBlur.jpg", result);

        //中值滤波
        Imgproc.medianBlur(result,result,5);
        //Imgcodecs.imwrite("D:/opencvImage/medianBlur.jpg", result);

        //去除光斑。腐蚀,使高亮区域被周围腐蚀
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        Imgproc.erode(result,result,kernel,new Point(-1,-1),21);
        //Imgcodecs.imwrite("D:/opencvImage/erode.jpg", result);
        //膨胀，理解为腐蚀后膨胀回来
        Imgproc.dilate(result,result,kernel,new Point(-1,-1),21);
        //Imgcodecs.imwrite("D:/opencvImage/dilate.jpg", result);
        //全局二值化操作,固定阈值
        double thresh = Imgproc.threshold(result,result,0,255,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);    //参数须调节
        Imgcodecs.imwrite("./opencvImage/binarization.jpg", result);

        //轮廓发现，得到contours和hierarchy
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(result, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_NONE, new Point(-1, -1));
        //Imgcodecs.imwrite("D:/opencvImage/line.jpg", hierarchy);

        //相当于创建和原图尺寸相同一张黑色的图，用于后面画线作图
        Mat contoursImg = Mat.zeros(result.size(),CvType.CV_8UC3);

        double areas[] = new double[contours.size()];
        for(int i=0;i<contours.size();i++){
            //在contoursImg上绘制最大的轮廓
            Imgproc.drawContours(contoursImg,contours,i, new Scalar(0,0,255),
                    2,8,hierarchy,0,new Point(0, 0));
            double partialArea = Imgproc.contourArea(contours.get(i));
            areas[i] = partialArea;
            System.out.println("第"+i+"个图像的面积是 "+area);
        }
        Arrays.sort(areas);
        if(areas.length>1){
            area= Constant.CONTAINER_WIDTH*Constant.CONTAINER_HEIGHT*areas[areas.length-2]/areas[areas.length-1];
        }
        else{
            area = 0;
        }
        Imgcodecs.imwrite("./opencvImage/final.jpg", contoursImg);
        return area;
    }
    private static void Mat2BufferedImageSave(Mat result) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", result, matOfByte);
        byte[] byteArray=matOfByte.toArray();
        InputStream in=new ByteArrayInputStream(byteArray);
        BufferedImage bufImage= ImageIO.read(in);
        File output=new File("./opencvImage/kmeans.jpg");
        ImageIO.write(bufImage, "jpg", output);
    }

    public double getAreaPer(Mat image,int thresh) throws Exception {
        double zmjArea;
        double relZmjArea;
//        //截取图像，只剩背景和芝麻酱，选择条件比较苛刻，须对实际图像进行调整。
//        Rect rect =new Rect(220,550,600,600);      //参数须调节
//        Mat image = new Mat(image1,rect);

        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
        System.load(url.getPath());
        if (image.empty()){
            throw new Exception("image is empty!");
        }
        Mat oldImage = image;
        Imgcodecs.imwrite("./opencvImage/oldzmj.jpg", oldImage);

        //图片转灰度
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite("./opencvImage/grayscale.jpg", image);

        //高斯模糊
        Imgproc.GaussianBlur(image, image, new Size(15, 15), 0);
        Imgcodecs.imwrite("./opencvImage/gaussianBlur.jpg", image);

        //中值滤波
        Imgproc.medianBlur(image,image,5);
        Imgcodecs.imwrite("./opencvImage/medianBlur.jpg", image);

        //去除光斑。腐蚀,使高亮区域被周围腐蚀
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        Imgproc.erode(image,image,kernel,new Point(-1,-1),21);
        Imgcodecs.imwrite("./opencvImage/erode.jpg", image);
        //膨胀，理解为腐蚀后膨胀回来
        Imgproc.dilate(image,image,kernel,new Point(-1,-1),21);
        Imgcodecs.imwrite("./opencvImage/dilate.jpg", image);

//        //去除阴影，先闭运算
//        Mat closeImage = new Mat();
//        Imgproc.morphologyEx(image,closeImage,Imgproc.MORPH_CLOSE, kernel,new Point(-1,-1),4);
//        Imgcodecs.imwrite("D:/opencvImage/closeImage.jpg", closeImage);
//        // 然后减去原灰度图再取反
//        Mat calcImage = new Mat();
//        Core.subtract(image ,closeImage,calcImage);  //相减
//        //Core.bitwise_not(calcImage,calcImage);      //取反
//        Imgcodecs.imwrite("D:/opencvImage/calcImage.jpg", calcImage);
//        //归一化
//        Imgproc.resize(calcImage,image,new Size(1,1),0,0,Imgproc.INTER_AREA);
//        Imgcodecs.imwrite("D:/opencvImage/resize.jpg", image);
        Imgproc.threshold(image,image,thresh,255,Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
//        if(model.equals("OTSU")){
//            Imgproc.threshold(image,image,0,255,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//            Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
//        }
//        else if (model.equals("normal")){
//
//        }
//        else if(model.equals("adaptive")){
//            Imgproc.adaptiveThreshold(image,image,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,21,5);
//            Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
//        }
//        else throw new Exception("没有这种算法");
        //全局二值化操作,固定阈值
        Imgproc.threshold(image,image,0,255,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//        double thresh = Imgproc.threshold(image,image,120,255,Imgproc.THRESH_BINARY);
//        //System.out.println(thresh);
        Imgcodecs.imwrite("./opencvImage/binarization.jpg", image);
        //局部二值化操作，自适应阈值
//        Imgproc.adaptiveThreshold(image,image,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,21,5);
//        Imgcodecs.imwrite("D:/opencvImage/binarization.jpg", image);

        //形态学闭操作
//        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3),new Point(-1,-1));
//        Imgproc.morphologyEx(image,image,Imgproc.MORPH_CLOSE, kernel,new Point(-1,-1),2);
//        Imgcodecs.imwrite("D:/opencvImage/structuring.jpg", image);
        //轮廓发现，得到contours和hierarchy
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_NONE, new Point(-1, -1));
        Imgcodecs.imwrite("./opencvImage/line.jpg", hierarchy);

        //相当于创建和原图尺寸相同一张黑色的图，用于后面画线作图
        Mat contoursImg = Mat.zeros(oldImage.size(),CvType.CV_8UC3);

        double areas[] = new double[contours.size()];
        for(int i=0;i<contours.size();i++){
            //      Rect rect = Imgproc.boundingRect(contours.get(i));
//            if (rect.width < mat.cols() / 2)
//                continue;
            //在contoursImg上绘制最大的轮廓
            Imgproc.drawContours(contoursImg,contours,i, new Scalar(0,0,255),
                    2,8,hierarchy,0,new Point(0, 0));
            double area = Imgproc.contourArea(contours.get(i));
            areas[i] = area;
            //System.out.println("第"+i+"个图像的面积是 "+area);
        }
        Arrays.sort(areas);
        if(areas.length>1){
            zmjArea = areas[areas.length-2];
            relZmjArea= Constant.CONTAINER_WIDTH*Constant.CONTAINER_HEIGHT*areas[areas.length-2]/areas[areas.length-1];
        }
        else{
            zmjArea = areas[0];
            relZmjArea = 0;
        }
        Imgcodecs.imwrite("./opencvImage/final.jpg", contoursImg);
        return relZmjArea;
    }
}
