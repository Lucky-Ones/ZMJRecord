/*编译前请先安装eloamCom控件*/
/*本项目使用SWT图形库构建界面，使用JACOB组件调用COM组件，在编译之前请先安装这两个库，请使用32位版本eclipse*/
/*两个库文件在安装目录 下java\eloamComJavaDemo\lib里面*/
/*安装方法：将jacob.dll放到d:\java\jdk1.7\jre\bin下面，jacob.jar放到d:\java\jdk1.7\jre\lib\ext下面，SWT安装方法相同*/
/*运行时请选择[run],[run As ], [JAVA Application]*/
package eloamComJavaDemo;

import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.javafx.css.CalculatedValue;
import dao.ExperimentMapper;
import dao.PictureMapper;
import eloamComJavaDemo.bean.ImageInfo;
import eloamComJavaDemo.utils.AreaCalculation;
import eloamComJavaDemo.utils.CircleCalculation;
import org.apache.ibatis.session.SqlSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.opencv.core.Mat;
import pojo.Experiment;
import pojo.Picture;
import utils.MybatisUtils;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class eloamComMain {
	//1
	private eloamViewOCX ocx1;// 预览窗口
	private eloamThumbnailOCX ocx2;// 缩略图窗口

	//	private Dispatch EloamGlobal;
	private int Device;// 当前播放视频设备
	private int circleTimes = 100000;
	private String stopPhotoSite;
	private String lablePhotoSite;
	private String VideoSite;
	private int stopTest=0;
	private String[] devs = {"0-主摄像头", "1-副摄像头"};// 设备列表

	private Shell shell;

	//输入框 实验地点，样品编号
	Label PlaceLabel;
	Text PlaceText;

	Label SampleLabel;
	Text SampleText;



	private Label devicelListLabel;
	private Label messageLabel;

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
	private Button StartRecordBtn; //开始摄像按钮
	private Button StopRecordBtn;//结束摄像按钮
	private Button RecordAndPhotoBtn;//结束摄像按钮
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

	//计算面积函数
	public String getArea(String sPicName) throws Exception{
//		String sPicName = "C:\\Users\\83811\\Desktop\\5.jpg";
		System.out.println(sPicName);
//		URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java455.dll");
		System.load(System.getProperty("exe.path")+"lib/opencv/opencv_java455.dll");
		AreaCalculation areaCalculation = new AreaCalculation();//计算面积
		Mat image = imread(sPicName, 1);
		double area = areaCalculation.getArea(image,"OTSU",150);//面积
		DecimalFormat df = new DecimalFormat("0");
		String arean = String.valueOf(df.format(area));
		System.out.println("芝麻酱面积"+arean);
		return arean;
	}

	public void open() {

//		EloamGlobal = null;
		Device = 0;

		Display display = Display.getDefault();

		createContents();
		InitDevices();

		shell.open();
		boolean ret = ocx1.SetPreviewWindow(Device, 100, 100, 800, 600);//固定选框


		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shell = new Shell();
		shell.setText("eloamComJavaDemo");
		shell.setSize(800, 750);
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




		//输入框 实验地点，样品编号
		PlaceLabel = new Label(group,SWT.NONE);
		PlaceLabel.setText("实验地点：");
//		PlaceLabel.setBounds(65, 100, 40,20);

		SampleText = new Text(group, SWT.SINGLE);

		SampleLabel = new Label(group,SWT.NONE);
		SampleLabel.setText("样品编号：");
//		SampleLabel.setBounds(65, 165, 40, 20);
		PlaceText = new Text(group, SWT.SINGLE);

		devicelListLabel = new Label(group, SWT.NONE);
		devicelListLabel.setText("设备列表:");




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
				resolution = 11;//只选择11 即1024*768
				mode=1;
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
		modeLabel.setText("模式:");

		modeCombo = new Combo(group, SWT.READ_ONLY);
		modeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				int mode = modeCombo.getSelectionIndex();
				mode = 1;//写死1， 即"1-MJPG模式"
				System.out.println("SetMode() mode:" + mode);
				boolean ret = ocx1.SetMode(Device, mode);
				System.out.println("ocx1.SetMode() ret:" + ret);
				if(ret) {
					System.out.println("ocx1.SetMode() success.");
				}
			}
		});

		resolutionLabel = new Label(group, SWT.NONE);
		resolutionLabel.setText("分辨率:");

		resolutionCombo = new Combo(group, SWT.READ_ONLY);
		resolutionCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				int resolution = resolutionCombo.getSelectionIndex();
				resolution = 11;//写死分辨率
				System.out.println("SetResolution() resolution:" + resolution);
				boolean ret = ocx1.SetResolution(Device, resolution);
				System.out.println("ocx1.SetResolution() ret:" + ret);
				if(ret) {
					System.out.println("ocx1.SetResolution() success.");
				}
			}
		});

//		openBtn = new Button(group, SWT.NONE);
//		openBtn.setText("预览视频");
//		openBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				if(ocx1 != null) {
//                    Device = deviceCombo.getSelectionIndex();
//                    int mode = modeCombo.getSelectionIndex();
//                    int resolution = resolutionCombo.getSelectionIndex();
//                    System.out.println("OpenVideoEx() device:" + Device);
//                    System.out.println("OpenVideoEx() mode:" + mode);
//                    System.out.println("OpenVideoEx() resolution:" + resolution);
//                    boolean ret = ocx1.OpenVideoEx(Device, mode, resolution);
//                    System.out.println("ocx1.OpenVideoEx() ret:" + ret);
//                    if(ret) {
//                    	System.out.println("ocx1.OpenVideo() success.");
//                    }
//				}
//			}
//		});

//		closeBtn = new Button(group, SWT.NONE);
//		closeBtn.setText("关闭视频");
//		closeBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				if (ocx1 != null) {
//					Device = deviceCombo.getSelectionIndex();
//                    boolean ret = ocx1.CloseVideo(Device);
//                    System.out.println("ocx1.CloseVideo() ret:" + ret);
//                    if(ret) {
//                    	System.out.println("ocx1.CloseVideo() success.");
//                    }
//				}
//			}
//		});

//		rotate90Btn = new Button(group, SWT.NONE);
//		rotate90Btn.setText("旋转90°");
//		rotate90Btn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				boolean ret = ocx1.VideoRotate(Device, 0);
//				if(ret) {
//					System.out.println("ocx1.VideoRotate(Device, 90) ret:" + ret);
//				}
//			}
//		});

//		rotate180Btn = new Button(group, SWT.NONE);
//		rotate180Btn.setText("旋转180°");
//		rotate180Btn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				boolean ret = ocx1.VideoRotate(Device, 1);
//				if(ret) {
//					System.out.println("ocx1.VideoRotate(Device, 180) ret:" + ret);
//				}
//			}
//		});

//		rotate270Btn = new Button(group, SWT.NONE);
//		rotate270Btn.setText("旋转270°");
//		rotate270Btn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				boolean ret = ocx1.VideoRotate(Device, 2);
//				if(ret) {
//					System.out.println("ocx1.VideoRotate(Device, 270) ret:" + ret);
//				}
//			}
//		});

		attributeBtn = new Button(group, SWT.NONE);
		attributeBtn.setText("属性");
		attributeBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Device = deviceCombo.getSelectionIndex();
				boolean ret = ocx1.ShowProperty(Device);
				if(ret) {
					System.out.println("ocx1.ShowProperty(Device) ret:" + ret);
				}
			}
		});

//		StartRecordBtn = new Button(group, SWT.NONE);
//		StartRecordBtn.setText("开始录像");
//		StartRecordBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				long A  = 60;
//				boolean ret = ocx1.StartRecord("E:\\zmj\\record\\1.mp4",A);
//				if(ret) {
//					System.out.println("ocx1.StartRecord(Device) ret:" + ret);
//				}else{
//					System.out.println("ocx1.StartRecord(Device) ret:null" );
//				}
//			}
//		});
//
//		StopRecordBtn = new Button(group, SWT.NONE);
//		StopRecordBtn.setText("停止录像");
//		StopRecordBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				//没返回值
//				ocx1.StopRecord();
////				if(ret) {
////					System.out.println("ocx1.StartRecord(Device) ret:" + ret);
////				}
//			}
//		});

		//拍照录像空格监听代码
		Display.getDefault().addFilter(SWT.KeyDown, new Listener() {
			public void handleEvent(Event e) {
				if (e.keyCode == ' ' ) {
					//让按键原有的功能失效
					e.doit = false ;
					//执行你自己的事件
//					MessageBox box = new  MessageBox( new  Shell(), SWT.ICON_INFORMATION | SWT.OK);
//					box.setText("提示信息" );
//					box.setMessage("按空格键了" );
//					box.open();
					Device = deviceCombo.getSelectionIndex();


					String sampleText = SampleText.getText();//样品编号
					String placeText = PlaceText.getText();//实验地点
					messageLabel.setText("");


					if (sampleText.isEmpty() || placeText.isEmpty()) {
						MessageBox mb = new MessageBox(shell,SWT.NONE);
						mb.setText("提示");
						mb.setMessage("实验地点或样品编号未填写");
						mb.open();
					}
					else{
						System.out.println("实验地点:"+placeText);
						System.out.println("样品编号:"+sampleText);


						Thread tRecordAndPhoto;

						tRecordAndPhoto = new Thread(() ->{
							try {
								Date dt1 = new Date();
								DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
								String nowTime1 = "";
								nowTime1 = df1.format(dt1);//start_time
								String video_name = nowTime1 + ".mp4";//video_name
								String fileName1 = System.getProperty("exe.path")+"\\zmj\\record\\" +"v"+ nowTime1 + ".mp4";//video_site
								long A  = 60;
								boolean ret = ocx1.StartRecord(fileName1,A);
								if(ret) {
									System.out.println("ocx1.StartRecord(Device) ret:" + ret);
								}else{
									System.out.println("ocx1.StartRecord(Device) ret:null" );
								}

								//增加一条实验数据，拿到返回的实验id
								SqlSession session1 = MybatisUtils.getSession();
								ExperimentMapper mapper1 = session1.getMapper(ExperimentMapper.class);

								Experiment experiment = new Experiment(1,video_name,fileName1,nowTime1,placeText,sampleText);
								mapper1.addExperiment(experiment);
								int experiment_id = experiment.getExperiment_id();//本次实验id:  experiment_id
								session1.commit();
								session1.close();

								stopTest =0;
								circleTimes =10000;

								for (int i=0;i<circleTimes;i++){//这里设置拍10张照片，数据库连接好后改为死循环
									Date dt = new Date();
									DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
									String nowTime = "";
									nowTime = df.format(dt);
									DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
									String photoName = dfs.format(dt);

									String fileName2 = System.getProperty("exe.path")+"\\zmj\\photo\\" +"p"+ nowTime + ".jpg";
									String fileName3 = nowTime + ".jpg";
									boolean ret2 = ocx1.Scan(Device, fileName2, 0);
									System.out.println("i:"+i+",ocx1.Scan(Device, fileName, 0) ret:" + ret);
									if(ret2) {
										ocx2.Add(fileName2);
									}
									String area = getArea(fileName2);//这次面积数值
									//存储数据库：
									//sampleText 样品编号变量
									//placeText 实验地点变量
									//area  这次面积变量

									//插入图片，拿到本次图片id
									SqlSession session2 = MybatisUtils.getSession();
									PictureMapper mapper2 = session2.getMapper(PictureMapper.class);
									Picture picture = new Picture(fileName3,nowTime,fileName2,area,experiment_id);
									mapper2.addPicture(picture);
									int picture_id = picture.getPicture_id();//本次图片id=picture_id，上次图片id=picture_id-1
									session2.commit(); //提交事务
									session2.close();


									//读取上一次面积：
									SqlSession session3 = MybatisUtils.getSession();
									PictureMapper mapper = session3.getMapper(PictureMapper.class);
									String agoArea= mapper.getSampleAreaById(picture_id-1);//上次图片的样品面积agoArea
									session3.close();

									//判断面积是一致：
									if (area.equals(agoArea)){

//									String R1="R1";
//									="R2";
//									String RM="RM";
										//String picture_id_2="picture_id_2";//图片二地址

										//根据图片id查找图片地址
										SqlSession session5 = MybatisUtils.getSession();
										PictureMapper mapper4 = session5.getMapper(PictureMapper.class);
										String picture_site= mapper4.getSiteById(picture_id);
										session5.close();
										CircleCalculation circle = new CircleCalculation();
										String picture_id_2 = System.getProperty("exe.path")+"\\zmj\\out\\"+"mp"+photoName+".jpg";
										ImageInfo imageInfo =  circle.canny(picture_site,picture_id_2);
										String R1 = String.valueOf(imageInfo.getR1());
										String R2 = String.valueOf(imageInfo.getR2());
										String RM = String.valueOf(imageInfo.getRm());

										//面积一致则本次实验结束，更新实验数据
										SqlSession session4 = MybatisUtils.getSession();
										ExperimentMapper mapper3 = session4.getMapper(ExperimentMapper.class);
										Experiment experiment2 = new Experiment(experiment_id,nowTime,area,R1,R2,RM,picture_id,picture_id_2);
										mapper3.updateExperiment(experiment2);
										session4.commit();
										session4.close();


//										MessageBox mb = new MessageBox(shell,SWT.NONE);
//										mb.setText("提示");
//										mb.setMessage("测算结束，停止图片保存至 "+fileName2+"\n标注图片保存至："+picture_id_2+"\n视频文件保存至："+fileName1);
//										mb.open();
//										messageLabel.setText("测算结束，停止图片保存至 "+fileName2+"\n标注图片保存至："+picture_id_2+"\n视频文件保存至："+fileName1);

										stopTest = 1;
										stopPhotoSite=fileName2;
										lablePhotoSite=picture_id_2;
										VideoSite=fileName1;
										Display.getDefault().syncExec(new Runnable() {
											public void run() {
//											messageLabel.setText("finish");
												MessageBox mb = new MessageBox(shell, SWT.NONE);
												mb.setText("提示");
												mb.setMessage("测算结束，停止图片保存至 " + stopPhotoSite + "\n标注图片保存至：" + lablePhotoSite + "\n视频文件保存至：" + VideoSite);
												mb.open();
											}
										});

										break;//一致跳出循环停止拍照，写好后把上面for循环改为死循环
									}else{
										Thread.sleep(2500);//等待2.5秒后继续拍照
									}
								}
//							ocx1.StopRecord();
							} catch (Exception exception) {
								exception.printStackTrace();
							}finally {
								ocx1.StopRecord();
							}

						});
						tRecordAndPhoto.start();

//						for (;;){
//							if (stopTest == 1) {
//								MessageBox mb = new MessageBox(shell, SWT.NONE);
//								mb.setText("提示");
//								mb.setMessage("测算结束，停止图片保存至 " + stopPhotoSite + "\n标注图片保存至：" + lablePhotoSite + "\n视频文件保存至：" + VideoSite);
//								mb.open();
//								break;
//							}
//							if(circleTimes==0) break;
//						}
					}


				}
			}
		});




//		CheckDeskewBtn = new Button(group, SWT.CHECK);
//		CheckDeskewBtn.setText("纠偏裁边");
//		CheckDeskewBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				boolean b = CheckDeskewBtn.getSelection();
//				ocx1.Deskew(Device, b);
//			}
//		});
//
//		checkRectBtn = new Button(group, SWT.CHECK);
//		checkRectBtn.setText("手动框选");
//		checkRectBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				System.out.println("ocx1.GetResolutionWidth(Device, resolution) Device:" + Device);
//				boolean b = checkRectBtn.getSelection();
//				System.out.println("CheckDeskewBtn.getSelection() b:" + b);
//				ocx1.SetState(Device, b);
//			}
//		});


//		Device = deviceCombo.getSelectionIndex();
//		boolean ret = ocx1.SetPreviewWindow(Device, 10, 10, 4000, 3000);
//		if(!ret) {
//			System.out.println("ocx1.SetPreviewWindow(Device, 30, 20, width - 20, height - 20) ret:" + ret);
//		}

//		displayRectBtn = new Button(group, SWT.NONE);
//		displayRectBtn.setText("指定选框");
//		displayRectBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				System.out.println("ocx1.GetResolutionWidth(Device, resolution) Device:" + Device);
//				int resolution = resolutionCombo.getSelectionIndex();
//				int width = Integer.valueOf(ocx1.GetResolutionWidth(Device, resolution)).intValue();
//				System.out.println("ocx1.GetResolutionWidth(Device, resolution) width:" + width);
//				int height = Integer.valueOf(ocx1.GetResolutionHeight(Device, resolution)).intValue();
//				System.out.println("ocx1.GetResolutionHeight(Device, resolution) height:" + height);
//				boolean ret = ocx1.SetPreviewWindow(Device, 10, 10, width - 20, height -20);
//				if(ret) {
//					System.out.println("ocx1.SetPreviewWindow(Device, 30, 20, width - 60, height - 40) ret:" + ret);
//				}
//			}
//		});


//		unDisplayRectBtn = new Button(group, SWT.NONE);
//		unDisplayRectBtn.setText("取消选框");
//		unDisplayRectBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//				boolean ret = ocx1.SetPreviewWindow(Device, 0, 0, 0, 0);
//				if(ret) {
//					System.out.println("ocx1.SetPreviewWindow(Device, 0, 0, 0, 0) ret:" + ret);
//				}
//			}
//		});

		//拍照录像按钮代码

		RecordAndPhotoBtn = new Button(group, SWT.NONE);
		RecordAndPhotoBtn.setText("开始");
		RecordAndPhotoBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt .events.SelectionEvent e)  {
				Device = deviceCombo.getSelectionIndex();


				String sampleText = SampleText.getText();//样品编号
				String placeText = PlaceText.getText();//实验地点
				messageLabel.setText("");


				if (sampleText.isEmpty() || placeText.isEmpty()) {
					MessageBox mb = new MessageBox(shell,SWT.NONE);
					mb.setText("提示");
					mb.setMessage("实验地点或样品编号未填写");
					mb.open();
				}
				else{
					System.out.println("实验地点:"+placeText);
					System.out.println("样品编号:"+sampleText);


					Thread tRecordAndPhoto;

					tRecordAndPhoto = new Thread(() ->{
						try {
							Date dt1 = new Date();
							DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
							String nowTime1 = "";
							nowTime1 = df1.format(dt1);//start_time
							String video_name = nowTime1 + ".mp4";//video_name
							String fileName1 = System.getProperty("exe.path")+"\\zmj\\record\\" +"v"+ nowTime1 + ".mp4";//video_site
							long A  = 60;
							boolean ret = ocx1.StartRecord(fileName1,A);
							if(ret) {
								System.out.println("ocx1.StartRecord(Device) ret:" + ret);
							}else{
								System.out.println("ocx1.StartRecord(Device) ret:null" );
							}

							//增加一条实验数据，拿到返回的实验id
							SqlSession session1 = MybatisUtils.getSession();
							ExperimentMapper mapper1 = session1.getMapper(ExperimentMapper.class);

							Experiment experiment = new Experiment(1,video_name,fileName1,nowTime1,placeText,sampleText);
							mapper1.addExperiment(experiment);
							int experiment_id = experiment.getExperiment_id();//本次实验id:  experiment_id
							session1.commit();
							session1.close();

							stopTest =0;
							circleTimes =10000;

							for (int i=0;i<circleTimes;i++){//这里设置拍10张照片，数据库连接好后改为死循环
								Date dt = new Date();
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
								String nowTime = "";
								nowTime = df.format(dt);
								DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
								String photoName = dfs.format(dt);

								String fileName2 = System.getProperty("exe.path")+"\\zmj\\photo\\" +"p"+ nowTime + ".jpg";
								String fileName3 = nowTime + ".jpg";
								boolean ret2 = ocx1.Scan(Device, fileName2, 0);
								System.out.println("i:"+i+",ocx1.Scan(Device, fileName, 0) ret:" + ret);
								if(ret2) {
									ocx2.Add(fileName2);
								}
								String area = getArea(fileName2);//这次面积数值
								//存储数据库：
								//sampleText 样品编号变量
								//placeText 实验地点变量
								//area  这次面积变量

								//插入图片，拿到本次图片id
								SqlSession session2 = MybatisUtils.getSession();
								PictureMapper mapper2 = session2.getMapper(PictureMapper.class);
								Picture picture = new Picture(fileName3,nowTime,fileName2,area,experiment_id);
								mapper2.addPicture(picture);
								int picture_id = picture.getPicture_id();//本次图片id=picture_id，上次图片id=picture_id-1
								session2.commit(); //提交事务
								session2.close();


								//读取上一次面积：
								SqlSession session3 = MybatisUtils.getSession();
								PictureMapper mapper = session3.getMapper(PictureMapper.class);
								String agoArea= mapper.getSampleAreaById(picture_id-1);//上次图片的样品面积agoArea
								session3.close();

								//判断面积是一致：
								if (area.equals(agoArea)){

//									String R1="R1";
//									="R2";
//									String RM="RM";
									//String picture_id_2="picture_id_2";//图片二地址

									//根据图片id查找图片地址
									SqlSession session5 = MybatisUtils.getSession();
									PictureMapper mapper4 = session5.getMapper(PictureMapper.class);
									String picture_site= mapper4.getSiteById(picture_id);
									session5.close();
									CircleCalculation circle = new CircleCalculation();
									String picture_id_2 = System.getProperty("exe.path")+"\\zmj\\out\\"+"mp"+photoName+".jpg";
									ImageInfo imageInfo =  circle.canny(picture_site,picture_id_2);
									String R1 = String.valueOf(imageInfo.getR1());
									String R2 = String.valueOf(imageInfo.getR2());
									String RM = String.valueOf(imageInfo.getRm());

									//面积一致则本次实验结束，更新实验数据
									SqlSession session4 = MybatisUtils.getSession();
									ExperimentMapper mapper3 = session4.getMapper(ExperimentMapper.class);
									Experiment experiment2 = new Experiment(experiment_id,nowTime,area,R1,R2,RM,picture_id,picture_id_2);
									mapper3.updateExperiment(experiment2);
									session4.commit();
									session4.close();


//									MessageBox mb = new MessageBox(shell,SWT.NONE);
//									mb.setText("提示");
//									mb.setMessage("测算结束，停止图片保存至 "+fileName2+"\n标注图片保存至："+picture_id_2+"\n视频文件保存至："+fileName1);
//									mb.open();
									stopTest = 1;
									stopPhotoSite=fileName2;
									lablePhotoSite=picture_id_2;
									VideoSite=fileName1;
									Display.getDefault().syncExec(new Runnable() {
										public void run() {
//											messageLabel.setText("finish");
											MessageBox mb = new MessageBox(shell, SWT.NONE);
											mb.setText("提示");
											mb.setMessage("测算结束，停止图片保存至 " + stopPhotoSite + "\n标注图片保存至：" + lablePhotoSite + "\n视频文件保存至：" + VideoSite);
											mb.open();
										}
									});



									break;//一致跳出循环停止拍照，写好后把上面for循环改为死循环
								}else{
									Thread.sleep(2500);//等待2.5秒后继续拍照
								}
							}
//							ocx1.StopRecord();
						} catch (Exception exception) {
							exception.printStackTrace();
						}finally {
							ocx1.StopRecord();
						}

					});
					tRecordAndPhoto.start();

//					for (;;){
//						if (stopTest == 1) {
//							MessageBox mb = new MessageBox(shell, SWT.NONE);
//							mb.setText("提示");
//							mb.setMessage("测算结束，停止图片保存至 " + stopPhotoSite + "\n标注图片保存至：" + lablePhotoSite + "\n视频文件保存至：" + VideoSite);
//							mb.open();
//							break;
//						}
//						if(circleTimes==0) break;
//					}
				}
			}
		});


		StopRecordBtn = new Button(group, SWT.NONE);
		StopRecordBtn.setText("取消测量");
		StopRecordBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

				circleTimes = 0;


				MessageBox mb = new MessageBox(shell,SWT.NONE);
				mb.setText("提示");
				mb.setMessage("本次测量已取消");
				mb.open();
			}
		});

		messageLabel = new Label(group,SWT.NONE);

//		takingPicturesBtn = new Button(group, SWT.NONE);
//		takingPicturesBtn.setText("拍  照");
//		takingPicturesBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				Device = deviceCombo.getSelectionIndex();
//
//				Date dt = new Date();
//				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//				String nowTime = "";
//				nowTime = df.format(dt);
//
//				String fileName = "E:\\zmj\\photo\\" + nowTime + ".jpg";
//
//				boolean ret = ocx1.Scan(Device, fileName, 0);
//				if(ret) {
//					System.out.println("ocx1.Scan(Device, fileName, 0) ret:" + ret);
//					ocx2.Add(fileName);
//				}else{
//					System.out.println("ocx1.Scan(Device, fileName, 0) ret:" + ret);
//				}
//			}
//		});

//		IDcardBtn = new Button(group, SWT.NONE);
//		IDcardBtn.setText("获取身份证信息");
//		IDcardBtn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//
//				Date dt = new Date();
//				DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//				String nowTime = "";
//				nowTime = df.format(dt);
//				String fileName = "D:\\" + nowTime + ".jpg";
////				boolean res = ocx1.StartIdcardReader();
////				System.out.println("ocx1.StartIdcardReader() res:" + res);
////				res = ocx1.StopIdcardReader();
////				System.out.println("ocx1.StopIdcardReader() res:" + res);
////
////				String ret = ocx1.GetIdCardInfoEx(fileName);
////				System.out.println("ocx1.GetIdCardInfoEx(fileName) ret:" + ret);
//				String ret = ocx1.GetIdCardInfo(fileName);
//				System.out.println("ocx1.GetIdCardInfo(fileName) ret:" + ret);
//				if (ret != null && ret.length() > 0) {
//					MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
//					dialog.setText("读取成功");
//					dialog.setMessage(ret);
//					dialog.open();
//				} else {
//					System.out.println("读卡失败！");
//				}
//			}
//		});

		ocx2 = new eloamThumbnailOCX();
		ocx2.InitOcx(shell);
		ocx2.SetClientSize(600, 200);
	}

	public void InitDevices() {
		//boolean ret = Dispatch.call(EloamView, "InitDev").getBoolean();
		boolean ret = ocx1.InitDev();
		System.out.println("ocx1.InitDev() ret:" + ret);
		if (ret) {
			ret = ocx1.OpenVideoEx(0, 1, 11);
			System.out.println("ocx1.OpenVideo() ret:" + ret);
			int rst = ocx1.GetState(0);
			System.out.println("ocx1.GetState() rst:" + rst);

			// 获取分辨率
			deviceCombo.removeAll();
			deviceCombo.add(devs[0], 0);
//			deviceCombo.add(devs[1], 1);

			deviceCombo.select(0);
			Device = deviceCombo.getSelectionIndex();

			modeCombo.removeAll();
//			modeCombo.add("0-YUY2模式");
			modeCombo.add("1-MJPG模式");
			modeCombo.select(0);

//			long result = ocx1.GetResolutionNumberEx(Device, modeCombo.getSelectionIndex());
			long result = ocx1.GetResolutionNumberEx(Device, 1);//写死1 即"1-MJPG模式"

			int count = 0;
			count = (int)result;

			resolutionCombo.removeAll();
			String res;
			res = ocx1.GetResolution(Device, 11);
			resolutionCombo.add(res);//只添加11 即1024*768
			resolutionCombo.select(0);
//			for (int i = 0; i < count; i++) {
//				res = ocx1.GetResolution(Device, i);
//				resolutionCombo.add(res);
//				resolutionCombo.select(0);
//			}
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
