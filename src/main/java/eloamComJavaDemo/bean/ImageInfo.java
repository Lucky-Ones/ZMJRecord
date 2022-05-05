package eloamComJavaDemo.bean;

import lombok.Data;
import org.opencv.core.Point;

@Data
public class ImageInfo {

    Double R1;

    Double R2;

    Double Rm;

    Point maxDiameterPoint;

    Point verticalDiameterPoint;

    String maskedImagePath;

    String originalImagePath;

}
