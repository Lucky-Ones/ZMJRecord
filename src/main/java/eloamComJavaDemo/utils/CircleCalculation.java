package eloamComJavaDemo.utils;

import eloamComJavaDemo.bean.ImageInfo;
import eloamComJavaDemo.constant.Constant;
import eloamComJavaDemo.bean.ImageInfo;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CircleCalculation {
    //静态代码块加载动态链接库
    static {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
        URL url = ClassLoader.getSystemResource(System.getProperty("exe.path")+"/lib/opencv/opencv_java455.dll");
        System.setProperty("java.awt.headless", "false");
        System.load(System.getProperty("exe.path")+"/lib/opencv/opencv_java455.dll");
    }
    //霍夫变换圆形检测
    public void houghCircles(String[] args) {
//        Mat src = Imgcodecs.imread("D:\\opencvImage\\basketball.jpg");
        Mat src = Imgcodecs.imread(System.getProperty("exe.path")+"\\opencvImage\\basketball.jpg");

        Mat dst = src.clone();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
        Mat circles = new Mat();
        Imgproc.HoughCircles(dst, circles, Imgproc.HOUGH_GRADIENT, 1, 100, 600, 50, 0, 150);
        for (int i = 0; i < circles.cols(); i++)
        {
            double[] circle = circles.get(0, i);

            Point center = new Point(circle[0], circle[1]);
            int radius = (int) Math.round(circle[2]);

            // circle center
            Imgproc.circle(src, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
            // circle outline
            Imgproc.circle(src, center, radius, new Scalar(0, 255, 0), 3, 8, 0);

        }
//        Imgcodecs.imwrite("D:\\opencvImage\\basketball111.jpg", src);
        Imgcodecs.imwrite(System.getProperty("exe.path")+"\\opencvImage\\basketball111.jpg", src);
        HighGui.imshow("圆形检测", src);
        HighGui.waitKey();
    }

    //最小外接圆
    public ImageInfo threshold(String originalImagePath, String maskedImagePath) throws Exception {
        Mat image = Imgcodecs.imread(originalImagePath);
        if (image.empty()){
            throw new Exception("image is empty!");
        }
        Mat oldImage = image;

        //图片转灰度
        Mat color = new Mat();
        Imgproc.cvtColor(image, color, Imgproc.COLOR_BGR2GRAY);
        //高斯模糊
        Mat GaussianBlur = new Mat();
        Imgproc.GaussianBlur(color, GaussianBlur, new Size(15, 15), 0);
        //中值滤波
        Mat medianBlur = new Mat();
        Imgproc.medianBlur(GaussianBlur,medianBlur,5);
        //去除光斑。腐蚀,使高亮区域被周围腐蚀
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        Mat erode = new Mat();
        Imgproc.erode(medianBlur,erode,kernel,new Point(-1,-1),21);
        //膨胀，理解为腐蚀后膨胀回来
        Mat dilate = new Mat();
        Imgproc.dilate(erode,dilate,kernel,new Point(-1,-1),21);
        //二值化
        Mat threshold = new Mat();
        Imgproc.threshold(dilate,threshold,0,255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
//        Imgcodecs.imwrite("D:\\opencvImage\\binarization.jpg", threshold);
        Imgcodecs.imwrite(System.getProperty("exe.path")+"\\opencvImage\\binarization.jpg", threshold);

        //index=1,面积第二大的区域
        ImageInfo imageInfo = findContoursAndDraw(oldImage,threshold,1,originalImagePath,maskedImagePath);

        return imageInfo;

    }


    //Canny边缘检测
    public ImageInfo canny(String originalImagePath, String maskedImagePath) throws IOException {
        Mat src = Imgcodecs.imread(originalImagePath);

        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray, Imgproc.COLOR_BGR2GRAY);

        Mat gaus = new Mat();
        Imgproc.GaussianBlur(gray,gaus,new Size(3,3),0);

        //中值滤波
        Mat medianBlur = new Mat();
        Imgproc.medianBlur(gaus,medianBlur,5);
        //去除光斑。腐蚀,使高亮区域被周围腐蚀
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        Mat erode = new Mat();
        Imgproc.erode(medianBlur,erode,kernel,new Point(-1,-1),21);
        //膨胀，理解为腐蚀后膨胀回来
        Mat dilate = new Mat();
        Imgproc.dilate(erode,dilate,kernel,new Point(-1,-1),21);

        Mat edges = new Mat();
        Imgproc.Canny(dilate,edges,50,150);
        Imgcodecs.imwrite(System.getProperty("exe.path")+"\\opencvImage\\canny.jpg", edges);


        //index=2,差不多每个区域都分内轮廓和外轮廓。

        ImageInfo imageInfo = findContoursAndDraw(src,edges,2,originalImagePath,maskedImagePath);

        return imageInfo;

    }

    //直线检测 做不出来不想做了
    public void HoughLinesP(Mat src){
        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray, Imgproc.COLOR_BGR2GRAY);

        Mat gaus = new Mat();
        Imgproc.GaussianBlur(gray,gaus,new Size(3,3),0);

        //中值滤波
        Mat medianBlur = new Mat();
        Imgproc.medianBlur(gaus,medianBlur,5);
        //去除光斑。腐蚀,使高亮区域被周围腐蚀
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        Mat erode = new Mat();
        Imgproc.erode(medianBlur,erode,kernel,new Point(-1,-1),21);
        //膨胀，理解为腐蚀后膨胀回来
        Mat dilate = new Mat();
        Imgproc.dilate(erode,dilate,kernel,new Point(-1,-1),21);

        Mat edges = new Mat();
        Imgproc.Canny(dilate,edges,50,150);

//        Mat laplacian = new Mat();
//        Imgproc.Laplacian(edges,laplacian,edges.depth(),3,1,0);

        Mat lines = new Mat();
        Imgproc.HoughLinesP(edges,lines,1, Math.PI/180,50,20,20);

        Imgcodecs.imwrite(System.getProperty("exe.path")+"\\opencvImage\\line.jpg", lines);

    }

    public static ImageInfo findContoursAndDraw(Mat image, Mat singleChannel, int index, String originalImagePath, String maskedImagePath) throws IOException {
        //轮廓发现，得到contours和hierarchy
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(singleChannel, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_NONE, new Point());

        contours = contours.stream().sorted(new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                return (int) (Imgproc.contourArea(o2)- Imgproc.contourArea(o1));
            }
        }).collect(Collectors.toList());

        for (int i = 0; i < contours.size(); i++) {
            System.out.println(Imgproc.contourArea(contours.get(i)));
        }

        Mat contoursImg = Mat.zeros(image.size(), CvType.CV_8UC3);
        Imgproc.drawContours(contoursImg,contours,index,new Scalar(0,0,255),1,8,hierarchy);

        //contours.get(i)选择区域。
        MatOfPoint2f point2f = new MatOfPoint2f(contours.get(index).toArray());//将MatOfPoint转换为MatOfPoint2f
        //绘制轮廓的最小外结圆
        Point center = new Point();
        float[] radius = new float[1];
        Imgproc.minEnclosingCircle(point2f,center,radius);
        //Imgproc.circle(contoursImg,center,(int) radius[0],new Scalar(0,0,255),1);

        //得到边缘的点集
        List<Point> listCircle = point2f.toList();
        //得到最大直径与区域轮廓长度为最小外接圆半径的交点
        Point intersection = new Point();
        double min= Integer.MAX_VALUE;
        for (int i = 0; i < listCircle.size(); i++) {
            //得到边缘上与圆心最远的点。
            double flag = radius[0]*radius[0]-(listCircle.get(i).x-center.x)*(listCircle.get(i).x-center.x)-(listCircle.get(i).y-center.y)*(listCircle.get(i).y-center.y);
            if (flag<min){
                min = flag;
                intersection.x=listCircle.get(i).x;
                intersection.y=listCircle.get(i).y;
            }
        }
        //(x0,y0)为圆心,(x1,y1)为交点intersection。
        //最大直径的方程 y = (y0-y1)/(x0-x1)*x + (y1*x0-y0*x1)/(x0-x1)
        //法线直径的方程 y = (x1-x0)/(y0-y1)*x + (x0*x0+y0*y0-(x0*x1+y0*y1))/(y0-y1)
        double x0 = center.x;
        double y0 = center.y;
        double x1 = intersection.x;
        double y1 = intersection.y;
        //得到最大直径的另一个交点
        Point intersection0 = new Point();
        double min0 = Integer.MAX_VALUE;
        for (int i = 0; i < listCircle.size(); i++) {
            double flag0 = Math.abs((y0-y1)/(x0-x1)*listCircle.get(i).x+(y1*x0-y0*x1)/(x0-x1)-listCircle.get(i).y);
            if ((x0-x1)*(x0-listCircle.get(i).x)<0 && (y0-y1)*(y0-listCircle.get(i).y)<0 && flag0<min0){
                min0 = flag0;
                intersection0.x=listCircle.get(i).x;
                intersection0.y=listCircle.get(i).y;
            }
        }
        //法线直径的方程 y = (x1-x0)/(y0-y1)*x + (x0*x0+y0*y0-(x0*x1+y0*y1))/(y0-y1)
        //法线直径的第一个交点。
        Point normal0 = new Point();
        double min1 = Integer.MAX_VALUE;
        for (int i = 0; i < listCircle.size(); i++) {
            double flag1 = Math.abs(((x1-x0)/(y0-y1)*listCircle.get(i).x+(x0*x0+y0*y0-(x0*x1+y0*y1))/(y0-y1)-listCircle.get(i).y));
            if (flag1<min1){
                min1 = flag1;
                normal0.x = listCircle.get(i).x;
                normal0.y = listCircle.get(i).y;
            }
        }
        //法线直径的第二个交点
        Point normal1 = new Point();
        double min2 = Integer.MAX_VALUE;
        for (int i = 0; i < listCircle.size(); i++) {
            double flag2 = Math.abs((y0-normal0.y)/(x0-normal0.x)*listCircle.get(i).x+(normal0.y*x0-y0*normal0.x)/(x0-normal0.x)-listCircle.get(i).y);
            if ((x0-normal0.x)*(x0-listCircle.get(i).x)<0 && (y0-normal0.y)*(y0-listCircle.get(i).y)<0 && flag2<min2){
                min2 = flag2;
                normal1.x=listCircle.get(i).x;
                normal1.y=listCircle.get(i).y;
            }
        }
        Imgproc.line(contoursImg,intersection,intersection0,new Scalar(0,0,255,1));
        Imgproc.line(contoursImg,normal0,normal1,new Scalar(0,0,255));
        Imgcodecs.imwrite(System.getProperty("exe.path")+"\\opencvImage\\BlackAndWhite.jpg", contoursImg);

        Imgproc.line(image,intersection,intersection0,new Scalar(0,255,0),2);
        Imgproc.line(image,normal0,normal1,new Scalar(0,255,0),2);
        Imgproc.drawContours(image,contours,index,new Scalar(0,0,255),1,8,hierarchy);
        Imgcodecs.imwrite(maskedImagePath, image);

        ImageInfo imageInfo = new ImageInfo();
        double R1 = Math.sqrt(Math.pow(intersection.x-intersection0.x,2)+ Math.pow(intersection.y-intersection0.y,2));
        double R2 = Math.sqrt(Math.pow(normal0.x-normal1.x,2)+ Math.pow(normal0.y-normal1.y,2));

        //按比例放缩。
        R1 = R1 * Math.sqrt(Constant.CONTAINER_WIDTH*Constant.CONTAINER_HEIGHT/ Imgproc.contourArea(contours.get(index-1)));
        R2 = R2 * Math.sqrt(Constant.CONTAINER_WIDTH*Constant.CONTAINER_HEIGHT/ Imgproc.contourArea(contours.get(index-1)));
        Double Rm = (R1+R2)/2;
        //保留小数点后三位
        String str = String.format("%.3f",R1);
        R1 = Double.parseDouble(str);
        str = String.format("%.3f",R2);
        R2 = Double.parseDouble(str);
        str = String.format("%.3f",Rm);
        Rm = Double.parseDouble(str);

        imageInfo.setR1(R1);
        imageInfo.setR2(R2);
        imageInfo.setRm(Rm);
        imageInfo.setMaxDiameterPoint(intersection);
        imageInfo.setVerticalDiameterPoint(normal0);
        imageInfo.setOriginalImagePath(originalImagePath);
        imageInfo.setMaskedImagePath(maskedImagePath);

        //添加水印
        BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(image);
        addWaterMark(imageInfo,bufferedImage);

        return imageInfo;
    }

    //添加水印
    public static void addWaterMark(ImageInfo imageInfo,BufferedImage bufferedImage) throws IOException {
        //创建画笔
        Graphics2D pen = bufferedImage.createGraphics();
        pen.setColor(new Color(0,255,0));
        pen.setFont(new Font("微软雅黑", Font.ITALIC, 30));
        pen.drawString("R1",(int) imageInfo.getMaxDiameterPoint().x+10,(int) imageInfo.getMaxDiameterPoint().y+10);
        pen.drawString("R2",(int) imageInfo.getVerticalDiameterPoint().x+10,(int) imageInfo.getVerticalDiameterPoint().y+10);

        pen.setColor(new Color(0,0,0));
        String s = "R1 = "+imageInfo.getR1()+" mm";
        pen.drawString(s,0+50,bufferedImage.getHeight()-110);
        s = "R2 = "+imageInfo.getR2()+" mm";
        pen.drawString(s,0+50,bufferedImage.getHeight()-80);
        s = "Rm = "+imageInfo.getRm()+" mm";
        pen.drawString(s,0+50,bufferedImage.getHeight()-50);

        //创建新图片文件
        File file = new File(imageInfo.getMaskedImagePath());
        // 将处理好的图片数据写入到新图片文件中
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(bufferedImage,"jpg",fos);
        fos.close();

    }

}
