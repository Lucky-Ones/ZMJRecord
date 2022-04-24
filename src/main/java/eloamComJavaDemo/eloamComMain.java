/*����ǰ���Ȱ�װeloamCom�ؼ�*/
/*����Ŀʹ��SWTͼ�ο⹹�����棬ʹ��JACOB�������COM������ڱ���֮ǰ���Ȱ�װ�������⣬��ʹ��32λ�汾eclipse*/
/*�������ļ��ڰ�װĿ¼ ��java\eloamComJavaDemo\lib����*/
/*��װ��������jacob.dll�ŵ�d:\java\jdk1.7\jre\bin���棬jacob.jar�ŵ�d:\java\jdk1.7\jre\lib\ext���棬SWT��װ������ͬ*/
/*����ʱ��ѡ��[run],[run As ], [JAVA Application]*/
package eloamComJavaDemo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class eloamComMain {
	//1
	private eloamViewOCX ocx1;// Ԥ������
	private eloamThumbnailOCX ocx2;// ����ͼ����

//	private Dispatch EloamGlobal;
	private int Device;// ��ǰ������Ƶ�豸
	private String[] devs = {"0-������ͷ", "1-������ͷ"};// �豸�б�

	private Shell shell;

	private Label devicelListLabel;
	private Combo deviceCombo;

	private Label resolutionLabel;
	private Combo resolutionCombo;

	private Label modeLabel;
	private Combo modeCombo;

	private Button openBtn;
	private Button closeBtn;

	private Button rotate90Btn;
	private Button rotate180Btn;
	private Button rotate270Btn;
	private Button attributeBtn;
	private Button StartRecordBtn;
	private Button StopRecordBtn;
	private Button takingPicturesBtn;

	private Button getBase64Btn;
	private Button displayRectBtn;
	private Button unDisplayRectBtn;
	private Button checkRectBtn;
	private Button CheckDeskewBtn;

	private Button IDcardBtn;

	private Group group;

	public static void main(String[] args) {
		eloamComMain demo = new eloamComMain();
		demo.open();
	}

	public void open() {

//		EloamGlobal = null;
		Device = 0;

		Display display = Display.getDefault();

		createContents();
		InitDevices();

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shell = new Shell();
		shell.setText("eloamComJavaDemo");
		shell.setSize(800, 700);
		shell.addShellListener(new ShellAdapter() {

			public void shellClosed(ShellEvent e) {
				super.shellClosed(e);
				releaseDevices();
			}
		});

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
		shell.setLayout(gridLayout);

		ocx1 = new eloamViewOCX();
		ocx1.InitOcx(shell);
		ocx1.SetClientSize(600, 400);

		group = new Group(shell, SWT.NONE);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));

		devicelListLabel = new Label(group, SWT.NONE);
		devicelListLabel.setText("�豸�б�:");

		deviceCombo = new Combo(group, SWT.READ_ONLY);
		deviceCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
                boolean ret = ocx1.CloseVideo(Device);
                System.out.println("ocx1.CloseVideo() ret:" + ret);
                if(ret) {
                	System.out.println("ocx1.CloseVideo() success.");
                }

				long result = ocx1.GetResolutionNumberEx(Device, modeCombo.getSelectionIndex());

				int count = 0;
				count = (int)result;

				resolutionCombo.removeAll();
				String res;
				for (int i = 0; i < count; i++) {
					res = ocx1.GetResolution(Device, i);
					resolutionCombo.add(res);
				}
				resolutionCombo.select(0);

                Device = deviceCombo.getSelectionIndex();
                int mode = modeCombo.getSelectionIndex();
                int resolution = resolutionCombo.getSelectionIndex();
                System.out.println("OpenVideoEx() device:" + Device);
                System.out.println("OpenVideoEx() mode:" + mode);
                System.out.println("OpenVideoEx() resolution:" + resolution);
                ret = ocx1.OpenVideoEx(Device, mode, resolution);
                System.out.println("ocx1.OpenVideoEx() ret:" + ret);
                if(ret) {
                	System.out.println("ocx1.OpenVideo() success.");
                }
			}
		});

		modeLabel = new Label(group, SWT.NONE);
		modeLabel.setText("ģʽ:");

		modeCombo = new Combo(group, SWT.READ_ONLY);
		modeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				int mode = modeCombo.getSelectionIndex();
				System.out.println("SetMode() mode:" + mode);
                boolean ret = ocx1.SetMode(Device, mode);
                System.out.println("ocx1.SetMode() ret:" + ret);
                if(ret) {
                	System.out.println("ocx1.SetMode() success.");
                }
			}
		});

		resolutionLabel = new Label(group, SWT.NONE);
		resolutionLabel.setText("�ֱ���:");

		resolutionCombo = new Combo(group, SWT.READ_ONLY);
		resolutionCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				int resolution = resolutionCombo.getSelectionIndex();
				System.out.println("SetResolution() resolution:" + resolution);
                boolean ret = ocx1.SetResolution(Device, resolution);
                System.out.println("ocx1.SetResolution() ret:" + ret);
                if(ret) {
                	System.out.println("ocx1.SetResolution() success.");
                }
			}
		});

		openBtn = new Button(group, SWT.NONE);
		openBtn.setText("Ԥ����Ƶ");
		openBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(ocx1 != null) {
                    Device = deviceCombo.getSelectionIndex();
                    int mode = modeCombo.getSelectionIndex();
                    int resolution = resolutionCombo.getSelectionIndex();
                    System.out.println("OpenVideoEx() device:" + Device);
                    System.out.println("OpenVideoEx() mode:" + mode);
                    System.out.println("OpenVideoEx() resolution:" + resolution);
                    boolean ret = ocx1.OpenVideoEx(Device, mode, resolution);
                    System.out.println("ocx1.OpenVideoEx() ret:" + ret);
                    if(ret) {
                    	System.out.println("ocx1.OpenVideo() success.");
                    }
				}
			}
		});

		closeBtn = new Button(group, SWT.NONE);
		closeBtn.setText("�ر���Ƶ");
		closeBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (ocx1 != null) {
					Device = deviceCombo.getSelectionIndex();
                    boolean ret = ocx1.CloseVideo(Device);
                    System.out.println("ocx1.CloseVideo() ret:" + ret);
                    if(ret) {
                    	System.out.println("ocx1.CloseVideo() success.");
                    }
				}
			}
		});

		rotate90Btn = new Button(group, SWT.NONE);
		rotate90Btn.setText("��ת90��");
		rotate90Btn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				boolean ret = ocx1.VideoRotate(Device, 0);
				if(ret) {
					System.out.println("ocx1.VideoRotate(Device, 90) ret:" + ret);
				}
			}
		});

		rotate180Btn = new Button(group, SWT.NONE);
		rotate180Btn.setText("��ת180��");
		rotate180Btn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				boolean ret = ocx1.VideoRotate(Device, 1);
				if(ret) {
					System.out.println("ocx1.VideoRotate(Device, 180) ret:" + ret);
				}
			}
		});

		rotate270Btn = new Button(group, SWT.NONE);
		rotate270Btn.setText("��ת270��");
		rotate270Btn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				boolean ret = ocx1.VideoRotate(Device, 2);
				if(ret) {
					System.out.println("ocx1.VideoRotate(Device, 270) ret:" + ret);
				}
			}
		});

		attributeBtn = new Button(group, SWT.NONE);
		attributeBtn.setText("����");
		attributeBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				boolean ret = ocx1.ShowProperty(Device);
				if(ret) {
					System.out.println("ocx1.ShowProperty(Device) ret:" + ret);
				}
			}
		});

		StartRecordBtn = new Button(group, SWT.NONE);
		StartRecordBtn.setText("��ʼ¼��");
		StartRecordBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				long A  = 60;
				boolean ret = ocx1.StartRecord("E:\\record\\1.mp4",A);
				if(ret) {
					System.out.println("ocx1.StartRecord(Device) ret:" + ret);
				}else{
					System.out.println("ocx1.StartRecord(Device) ret:null" );
				}
			}
		});

		StopRecordBtn = new Button(group, SWT.NONE);
		StopRecordBtn.setText("ֹͣ¼��");
		StopRecordBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				//û����ֵ
				ocx1.StopRecord();
//				if(ret) {
//					System.out.println("ocx1.StartRecord(Device) ret:" + ret);
//				}
			}
		});




		CheckDeskewBtn = new Button(group, SWT.CHECK);
		CheckDeskewBtn.setText("��ƫ�ñ�");
		CheckDeskewBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				boolean b = CheckDeskewBtn.getSelection();
				ocx1.Deskew(Device, b);
			}
		});

		checkRectBtn = new Button(group, SWT.CHECK);
		checkRectBtn.setText("�ֶ���ѡ");
		checkRectBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				System.out.println("ocx1.GetResolutionWidth(Device, resolution) Device:" + Device);
				boolean b = checkRectBtn.getSelection();
				System.out.println("CheckDeskewBtn.getSelection() b:" + b);
				ocx1.SetState(Device, b);
			}
		});

		displayRectBtn = new Button(group, SWT.NONE);
		displayRectBtn.setText("ָ��ѡ��");
		displayRectBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				System.out.println("ocx1.GetResolutionWidth(Device, resolution) Device:" + Device);
				int resolution = resolutionCombo.getSelectionIndex();
				int width = Integer.valueOf(ocx1.GetResolutionWidth(Device, resolution)).intValue();
				System.out.println("ocx1.GetResolutionWidth(Device, resolution) width:" + width);
				int height = Integer.valueOf(ocx1.GetResolutionHeight(Device, resolution)).intValue();
				System.out.println("ocx1.GetResolutionHeight(Device, resolution) height:" + height);
				boolean ret = ocx1.SetPreviewWindow(Device, 10, 10, width - 20, height -20);
				if(ret) {
					System.out.println("ocx1.SetPreviewWindow(Device, 30, 20, width - 60, height - 40) ret:" + ret);
				}
			}
		});

		unDisplayRectBtn = new Button(group, SWT.NONE);
		unDisplayRectBtn.setText("ȡ��ѡ��");
		unDisplayRectBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				boolean ret = ocx1.SetPreviewWindow(Device, 0, 0, 0, 0);
				if(ret) {
					System.out.println("ocx1.SetPreviewWindow(Device, 0, 0, 0, 0) ret:" + ret);
				}
			}
		});

		takingPicturesBtn = new Button(group, SWT.NONE);
		takingPicturesBtn.setText("��  ��");
		takingPicturesBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();

				Date dt = new Date();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				String nowTime = "";
				nowTime = df.format(dt);

				String fileName = "D:\\" + nowTime + ".jpg";

				boolean ret = ocx1.Scan(Device, fileName, 0);
				if(ret) {
					System.out.println("ocx1.Scan(Device, fileName, 0) ret:" + ret);
					ocx2.Add(fileName);
				}
			}
		});

		IDcardBtn = new Button(group, SWT.NONE);
		IDcardBtn.setText("��ȡ���֤��Ϣ");
		IDcardBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

				Date dt = new Date();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				String nowTime = "";
				nowTime = df.format(dt);
				String fileName = "D:\\" + nowTime + ".jpg";
//				boolean res = ocx1.StartIdcardReader();
//				System.out.println("ocx1.StartIdcardReader() res:" + res);
//				res = ocx1.StopIdcardReader();
//				System.out.println("ocx1.StopIdcardReader() res:" + res);
//
//				String ret = ocx1.GetIdCardInfoEx(fileName);
//				System.out.println("ocx1.GetIdCardInfoEx(fileName) ret:" + ret);
				String ret = ocx1.GetIdCardInfo(fileName);
				System.out.println("ocx1.GetIdCardInfo(fileName) ret:" + ret);
				if (ret != null && ret.length() > 0) {
					MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
					dialog.setText("��ȡ�ɹ�");
					dialog.setMessage(ret);
					dialog.open();
				} else {
					System.out.println("����ʧ�ܣ�");
				}
			}
		});

		ocx2 = new eloamThumbnailOCX();
		ocx2.InitOcx(shell);
		ocx2.SetClientSize(600, 200);
	}

	public void InitDevices() {
		//boolean ret = Dispatch.call(EloamView, "InitDev").getBoolean();
		boolean ret = ocx1.InitDev();
		System.out.println("ocx1.InitDev() ret:" + ret);
		if (ret) {
			ret = ocx1.OpenVideoEx(0, 1, 0);
			System.out.println("ocx1.OpenVideo() ret:" + ret);
			int rst = ocx1.GetState(0);
			System.out.println("ocx1.GetState() rst:" + rst);

			// ��ȡ�ֱ���
			deviceCombo.removeAll();
			deviceCombo.add(devs[0], 0);
			deviceCombo.add(devs[1], 1);

			deviceCombo.select(0);
			Device = deviceCombo.getSelectionIndex();

			modeCombo.removeAll();
			modeCombo.add("0-YUY2ģʽ");
			modeCombo.add("1-MJPGģʽ");
			modeCombo.select(1);

			long result = ocx1.GetResolutionNumberEx(Device, modeCombo.getSelectionIndex());

			int count = 0;
			count = (int)result;

			resolutionCombo.removeAll();
			String res;
			for (int i = 0; i < count; i++) {
				res = ocx1.GetResolution(Device, i);
				resolutionCombo.add(res);
				resolutionCombo.select(0);
			}
		}

		ret = ocx1.InitIdCard();
		System.out.println("ocx1.InitIdCard() ret:" + ret);
		if(ret) {
			System.out.println("ocx1.InitIdCard() success.");
		}
	}

	public void releaseDevices() {
		boolean ret = ocx1.DeinitIdCard();
		System.out.println("ocx1.DeinitIdCard() ret:" + ret);
		if(ret) {
			System.out.println("ocx1.DeinitIdCard() success.");
		}
		ret = ocx1.DeInitDev();
		System.out.println("ocx1.DeInitDev() ret:" + ret);
		if(ret) {
			System.out.println("ocx1.DeInitDev() success.");
		}
	}
}
