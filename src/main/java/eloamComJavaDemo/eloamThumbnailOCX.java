package eloamComJavaDemo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Shell;

public class eloamThumbnailOCX {
	private OleFrame _frame;
	private static Shell liuShell = null;
	private OleControlSite _site;
	private OleAutomation _auto;

	public void InitOcx(Shell sShell) {
		liuShell = sShell;
		_frame = new OleFrame(liuShell, SWT.NONE);

		_site = new OleControlSite(_frame, SWT.NONE, "{924BE286-51F3-4DEF-A050-FF97D56C48D6}");
		_auto = new OleAutomation(_site);

		_site.doVerb(OLE.OLEIVERB_SHOW);
	}

	public void SetFrameSize(int i, int j) {
		// _frame.redraw(0,0,i,j, false);
		_frame.setSize(i, j);
	}

	public void Add(String fileName) {
		int mid[] = _auto.getIDsOfNames(new String[] { "Add" });
		int id = mid[0];

		if (id < 0)
			return;

		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(fileName);
		rgvarg[0] = _auto.invoke(id, rgvarg);
	}

	public void SetClientSize(int cx, int cy) {
		_frame.setSize(cx, cy);
	}
}
