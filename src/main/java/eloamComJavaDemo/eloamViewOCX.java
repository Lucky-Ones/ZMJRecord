package eloamComJavaDemo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.LONG;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.OleListener;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Shell;

public class eloamViewOCX {
	OleFrame _frame;
	static Shell liuShell = null;
	OleControlSite _site;
	OleAutomation _auto;

	public void InitOcx(Shell sShell) {
		liuShell = sShell;
		_frame = new OleFrame(liuShell, SWT.NONE);
		_site = new OleControlSite(_frame, SWT.NONE, "{CA2184F0-5A78-4D81-80F2-B0A7BFC74FBF}");
		_auto = new OleAutomation(_site);
		
		_site.doVerb(OLE.OLEIVERB_SHOW);
	}

	public void SetFrameSize(int i, int j) {
		// _frame.redraw(0,0,i,j, false);
		_frame.setSize(i, j);
	}

	public void SetClientSize(int cx, int cy) {
		_frame.setSize(cx, cy);
	}

	public void addEventListener(int eventID, OleListener listener) {
		_site.addEventListener(eventID, listener);
	}

	public void removeEventListener(int eventID, OleListener listener) {
		_site.removeEventListener(eventID, listener);
	}

	protected Variant invoke(String methodName, Variant[] args) {
		int methodId = _auto.getIDsOfNames(new String[] { methodName })[0];
		return _auto.invoke(methodId, args);
	}
	
	public boolean InitDev() {
		int mid[] = _auto.getIDsOfNames(new String[] { "InitDev" });
		int id = mid[0];

		if (id < 0)
			return false;

		Variant ret = _auto.invoke(id);
		return ret.getBoolean();
	}
	
	public boolean DeInitDev() {
		int mid[] = _auto.getIDsOfNames(new String[] { "DeInitDev" });
		int id = mid[0];

		if (id < 0)
			return false;

		Variant ret = _auto.invoke(id);
		return ret.getBoolean();
	}
	
	public boolean OpenVideo(long iDev) {
		int mid[] = _auto.getIDsOfNames(new String[] { "OpenVideo" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(iDev);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public boolean OpenVideoEx(long iDev, long iMode, long iResolution) {
		int mid[] = _auto.getIDsOfNames(new String[] { "OpenVideoEx" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[3];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iMode);
		rgvarg[2] = new Variant(iResolution);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}
	
	public boolean CloseVideo(long iDev) {
		int mid[] = _auto.getIDsOfNames(new String[] { "CloseVideo" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(iDev);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}
	
	
	public long GetResolutionNumber(long iDev) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetResolutionNumber" });
		int id = mid[0];

		if (id < 0)
			return -1;
		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(iDev);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getLong();
	}
	
	public long GetResolutionNumberEx(long iDev, long iMode) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetResolutionNumberEx" });
		int id = mid[0];

		if (id < 0)
			return -1;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iMode);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getLong();
	}

	public String GetResolution(long iDev, long iResolutoin) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetResolution" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iResolutoin);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public boolean SetResolution(long iDev, long iResolution) {
		int mid[] = _auto.getIDsOfNames(new String[] { "SetResolution" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iResolution);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public boolean SetPreviewWindow(long iDev, long x, long y, long w, long h) {
		int mid[] = _auto.getIDsOfNames(new String[] { "SetPreviewWindow" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[5];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(x);
		rgvarg[2] = new Variant(y);
		rgvarg[3] = new Variant(w);
		rgvarg[4] = new Variant(h);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public boolean Deskew(long iDev, boolean bOpen) {
		int mid[] = _auto.getIDsOfNames(new String[] { "Deskew" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(bOpen);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}
	
	
	public boolean ImageFormat(long iDev, long iFormat) {
		int mid[] = _auto.getIDsOfNames(new String[] { "ImageFormat" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iFormat);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public boolean VideoRotate(long iDev, long iRotate) {
		int mid[] = _auto.getIDsOfNames(new String[] { "VideoRotate" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iRotate);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public boolean SetMode(long iDev, long iMode) {
		int mid[] = _auto.getIDsOfNames(new String[] { "SetMode" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iMode);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public boolean SetState(long iDev, boolean iState) {
		int mid[] = _auto.getIDsOfNames(new String[] { "SetState" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iState);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public String GetResolutionWidth(long iDev, long iResolutoin) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetResolutionWidth" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iResolutoin);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public String GetResolutionHeight(long iDev, long iResolutoin) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetResolutionHeight" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iResolutoin);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public boolean SetDisplayRect(long iDev, long x, long y, long w, long h) {
		int mid[] = _auto.getIDsOfNames(new String[] { "SetDisplayRect" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[5];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(x);
		rgvarg[2] = new Variant(y);
		rgvarg[3] = new Variant(w);
		rgvarg[4] = new Variant(h);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public boolean Scan(long iDev, String filePath, long flag) {
		int mid[] = _auto.getIDsOfNames(new String[] { "Scan" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[3];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(filePath);
		rgvarg[2] = new Variant(flag);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public String GetBase64(long iDev, long fmt, long flag) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetBase64" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[3];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(fmt);
		rgvarg[2] = new Variant(flag);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public boolean SaveFileByBase64(String ImageBase64, String FilePath) {
		int mid[] = _auto.getIDsOfNames(new String[] { "SaveFileByBase64" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(ImageBase64);
		rgvarg[1] = new Variant(FilePath);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}
	
	
	public boolean ShowProperty(long iDev) {
		int mid[] = _auto.getIDsOfNames(new String[] { "ShowProperty" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(iDev);
		Variant ret = _auto.invoke(id, rgvarg);
		System.out.println(ret);
		return ret.getBoolean();
	}
	//开始录像
	public boolean StartRecord(String FilePath, long iFrameRate) {
		int mid[] = _auto.getIDsOfNames(new String[] { "StartRecord" });
//		int mid2[] = _auto.getIDsOfNames(new String[] { "SaveFileByBase64" });
//		System.out.println(mid[0]);
//		System.out.println(mid2[0]);
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(FilePath);
		rgvarg[1] = new Variant(iFrameRate);
		Variant ret = _auto.invoke(id, rgvarg);
//		System.out.println(ret);

		return ret.getBoolean();
	}
	//停止录像

	public void StopRecord() {
		int mid[] = _auto.getIDsOfNames(new String[] { "StopRecord" });
		int id = mid[0];

		if (id < 0)
			return ;
		Variant[] rgvarg = new Variant[0];
		Variant ret = _auto.invoke(id,rgvarg);
		System.out.println("Record Stop");

		//return ret;


	}

	public String GetBarcodeCount(long iDev, long iResolutoin) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetBarcodeCount" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iResolutoin);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public String GetBarcode(long iDev, long iResolutoin) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetBarcode" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(iDev);
		rgvarg[1] = new Variant(iResolutoin);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public boolean InitIdCard() {
		int mid[] = _auto.getIDsOfNames(new String[] { "InitIdCard" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant ret = _auto.invoke(id);
		return ret.getBoolean();
	}

	public boolean DeinitIdCard() {
		int mid[] = _auto.getIDsOfNames(new String[] { "DeinitIdCard" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant ret = _auto.invoke(id);
		return ret.getBoolean();
	}
	
	public boolean StartIdcardReader() {
		int mid[] = _auto.getIDsOfNames(new String[] { "StartIdcardReader" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant ret = _auto.invoke(id);
		return ret.getBoolean();
	}
	
	public boolean StopIdcardReader() {
		int mid[] = _auto.getIDsOfNames(new String[] { "StopIdcardReader" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant ret = _auto.invoke(id);
		return ret.getBoolean();
	}

	public String GetIdCardInfo(String picFilePath) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetIdCardInfo" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(picFilePath);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public String GetIdCardInfoEx(String picFilePath) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetIdCardInfoEx" });
		int id = mid[0];

		if (id < 0)
			return "";
		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(picFilePath);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getString();
	}

	public boolean GetIdcardImage(String picFilePath, long flag) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetIdcardImage" });
		int id = mid[0];

		if (id < 0)
			return false;
		Variant[] rgvarg = new Variant[2];
		rgvarg[0] = new Variant(picFilePath);
		rgvarg[1] = new Variant(flag);
		Variant ret = _auto.invoke(id, rgvarg);
		return ret.getBoolean();
	}

	public int GetState(long iDev) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetState" });
		int id = mid[0];

		if (id < 0)
			return -2;

		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(iDev);
		rgvarg[0] = _auto.invoke(id, rgvarg);
		return rgvarg[0].getInt();
	}

	public Variant SetState(int s) {
		int mid[] = _auto.getIDsOfNames(new String[] { "SetState" });
		int id = mid[0];

		if (id < 0)
			return null;

		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(s);
		rgvarg[0] = _auto.invoke(id, rgvarg);

		return rgvarg[0];
	}
	
	public int GetState(int iDev) {
		int mid[] = _auto.getIDsOfNames(new String[] { "GetState" });
		int id = mid[0];

		if (id < 0)
			return -2;

		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(iDev);
		rgvarg[0] = _auto.invoke(id, rgvarg);

		return rgvarg[0].getInt();
	}
	
	public OleAutomation GetAuto() {

		return _auto;
	}


}
