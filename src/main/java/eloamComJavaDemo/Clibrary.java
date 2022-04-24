package eloamComJavaDemo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

//继承Library，用于加载库文件
public interface Clibrary extends Library {
    Clibrary EloamView = (Clibrary) Native.loadLibrary("D:\\Program Files\\EloamView_Base_3.1.6\\bin\\eloamDll.dll", Clibrary.class);


    boolean EloamGlobal_InitDevs();
    boolean EloamVideo_StartRecord(String filePath, long iFrameRate);
    void EloamVideo_StopRecord();

    public static void main(String[] args) {


//        boolean res = Clibrary.EloamView.EloamVideo_StartRecord("E:\\record\\3.mp4",60);
//        Clibrary.EloamView.EloamVideo_StopRecord();
    }
//    Boolean Fun_CDBWB(String path_DEM, double latt, double lont, double latr, double lonr, double f,
//                      double pt, double htg, double hrg, double[] Gt, double[] Gr,
//                      IntByReference IDCode, DoubleByReference E, DoubleByReference L, DoubleByReference T);
//
//    Pointer Fun_CDBWB_Zone(String path_DEM, double latt, double lont, double[] latrlim, double[] lonrlim, double f,
//                           double pt, double htg, double hrg, double[] Gt, double[] Gr, int scalefactor,
//                           IntByReference IDCode, IntByReference row, IntByReference col);



}