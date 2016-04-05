package processorInterface;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

import roundRobinPrgm.*;
import generic.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Interface {
	
	ExecutorService executor = Executors.newFixedThreadPool(5);	
	
	private boolean running = true;
	
	private Processor processor1;
	private Stage primaryStage;
	
	private BorderPane mainPane;
	private HBox topPane;
	private HBox bottomPane;
	private Pane centerPane;
	private BorderPane leftPane;
	
	private VBox leftTopPane;
	private GridPane leftBottomPane;
	
	private Label timeLabel;
	private int timeInt;
	private Text timeValue;
	private String time = "00";
	private int lastTime;
	private int timeDiff;
	
	private Label trnarndLabel;
	private Text trnarndValue;
	private String trnarnd = "00";
	
	private Label innitialWaitLabel;
	private Text innitialWaitText;
	private String innitialWaitString = "00";  
	
	private Label serviceLabel;
	private Text serviceText;
	private String serviceString = "00";  
	
	private Label interLabel;
	private Text interText;
	private String interString = "00";
	
	private RRProcess finishedProcessView;
	private Text centerHeadingText= new Text("First Process");
	private Text headingID = new Text("ID: ");
	private Text headingArrive = new Text("Arrival: ");
	private Text headingInnitialWait = new Text("Innitial Wait: ");
	private Text headingServiceTime = new Text("Service Time: ");
	private Text headingTrndArndTime = new Text("TrnArnd Time: ");
	
	private Text headingIDValue = new Text(" ");
	private Text headingArriveValue = new Text(" ");
	private Text headingInnitialWaitValue = new Text(" ");
	private Text headingServiceTimeValue = new Text(" ");
	private Text headingTrndArndTimeValue = new Text(" ");
	
	private Label activeQueueSize = new Label("Active Queue Size: ");
	private String activeQueueSizeString = "";
	private Text activeQueueSizeValue = new Text(" ");
	private Label rejectedProcessLabel = new Label("Rejected Processes: ");
	private String rejectedProcessString = "";
	private Text rejectedProcessValue = new Text(" ");
	private Label blockedQueueSize = new Label("Bocked Queue Size: ");
	private String blockedQueueSizeString = "";
	private Text blockedQueueSizeValue = new Text(" ");
	
	private Text rightTime = new Text();
	private String rightTimeString = " ";
	private Text leftTime = new Text();
	private String leftTimeString = " ";
	private Text centerTime = new Text();
	private String centerTimeString = " ";
	
	Queue<Circle> circleQueue = new LinkedList<>();
	
	private Button runButton;    
	private Button viewFinishButton;
	private Button stopButton;
	
	public Interface() {
		primaryStage = new Stage();
		processor1 = new Processor(this);
		this.finishedProcessView = this.processor1.getLastFinishedProcess();
		getInterface();
	}
	
	public boolean getRunning() {
		return this.running;
	}
	
	public void getInterface() {
		mainPane = new BorderPane();
		
		getCenterPane();
		getTopPane();
		getBottomPane();	
		getLeftPane();
		
		
		mainPane.setBottom(this.bottomPane);	
		mainPane.setTop(this.topPane);
		mainPane.setCenter(this.centerPane);
		mainPane.setLeft(this.leftPane);
	
		Scene scene = new Scene(mainPane, 1000, 600);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Process Interface");
		primaryStage.show();

	}
	
	private void getTopPane() {
		this.topPane = new HBox(15);
		this.topPane.setPadding(new Insets(15, 15, 15, 15));
		
		this.timeLabel = new Label("Time: ");
		this.timeLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.timeLabel);
		
		this.timeValue = new Text(this.time);
		this.timeValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.timeValue);
		
		this.trnarndLabel = new Label("Average TurnAround: ");
		this.trnarndLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.trnarndLabel);
		
		this.trnarndValue = new Text(this.trnarnd);
		this.trnarndValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.trnarndValue);
		
		this.innitialWaitLabel = new Label("Average Innitial Wait: ");
		this.innitialWaitLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.innitialWaitLabel);
		
		this.innitialWaitText = new Text(this.innitialWaitString);
		this.innitialWaitText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.innitialWaitText);
		
		this.serviceLabel = new Label("Service Time: ");
		this.serviceLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.serviceLabel);
		
		this.serviceText = new Text(this.serviceString);
		this.serviceText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.serviceText);
		
		this.interLabel = new Label("IntArrival Time: ");
		this.interLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.interLabel);
		
		this.interText = new Text(this.interString);
		this.interText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		this.topPane.getChildren().add(this.interText);
	}
	
	private void getBottomPane() {
		this.bottomPane = new HBox(15);
		this.bottomPane.setPadding(new Insets(15, 15, 15, 15));
		
		this.runButton = new Button("Run");
		this.runButton.setOnAction(e -> {
			run();
		});
		
		this.viewFinishButton = new Button("Finished Process");
		this.viewFinishButton.setOnAction(e -> {
			finishProcess();
		});
		
		this.stopButton = new Button("Stop");
		this.stopButton.setOnAction(e -> {
			this.running = false; report();
		});
		
		this.bottomPane.getChildren().add(this.runButton);
		this.bottomPane.getChildren().add(this.viewFinishButton);
		this.bottomPane.getChildren().add(this.stopButton);
	}
	
	private void getCenterPane() {
		this.centerPane = new Pane();

		this.centerPane.setStyle("-fx-border-color: black");
		
		Line breakLine = new Line(0, 450, 900, 450);
		
		Line centerLine = new Line(400, 450, 400, 430);
		Line quarterLine = new Line(200, 450, 200, 435);
		Line threequarterLine = new Line(600, 450, 600, 435);
		
		Line oneeightLine = new Line(100, 450, 100, 440);
		Line twoeightLine = new Line(300, 450, 300, 440);
		Line threeeightLine = new Line(500, 450, 500, 440);
		Line foureightLine = new Line(700, 450, 700, 440);
		
		breakLine.setStroke(Color.WHITE);
		centerLine.setStroke(Color.WHITE);
		quarterLine.setStroke(Color.WHITE);
		threequarterLine.setStroke(Color.WHITE);
		oneeightLine.setStroke(Color.WHITE);
		twoeightLine.setStroke(Color.WHITE);
		threeeightLine.setStroke(Color.WHITE);
		foureightLine.setStroke(Color.WHITE);
		
		getCenterValues(true, processor1.retrieveAnimationPoints());
		
		this.rightTime.setText(this.rightTimeString);
		this.rightTime.setX(800);
		this.rightTime.setY(480);
		this.rightTime.setFill(Color.WHITE);
		
		this.leftTime.setText(this.leftTimeString);
		this.leftTime.setX(10);
		this.leftTime.setY(480);
		this.leftTime.setFill(Color.WHITE);
		
		this.centerTime.setText(this.centerTimeString);
		this.centerTime.setX(380);
		this.centerTime.setY(480);
		this.centerTime.setFill(Color.WHITE);

		centerPane.getChildren().addAll(breakLine, centerLine, quarterLine, 
				threequarterLine, oneeightLine, twoeightLine, threeeightLine,
				foureightLine, this.rightTime, this.leftTime, this.centerTime);
		centerPane.setStyle("-fx-background-color: black");
	}
	
	private void getLeftPane() {
		this.leftPane = new BorderPane();
		this.leftPane.setStyle("-fx-border-color: black");
		getLeftTopPane();
		getLeftBottomPane();
		
		leftPane.setBottom(this.leftBottomPane);	
		leftPane.setTop(this.leftTopPane);
	}
	
	private void getTopValues() {
		this.lastTime = this.timeInt;
		this.timeInt = processor1.getTime() / 1000;
		this.timeDiff = this.timeInt - this.lastTime;
		this.time = Integer.toString(timeInt);
		
		int trnInt = processor1.getTrnarnd();
		this.trnarnd = Integer.toString(trnInt);
		
		int waitInt = processor1.getInnitWaitTime();
		this.innitialWaitString = Integer.toString(waitInt);
		
		int serviceInt = processor1.getServiceTime();
		this.serviceString = Integer.toString(serviceInt);
		
		int interInt = processor1.getInterArrivalTime();
		this.interString = Integer.toString(interInt);
	}
	
	private void setTop() {		
		this.timeValue.setText(this.time);
		this.trnarndValue.setText(this.trnarnd);
		this.innitialWaitText.setText(this.innitialWaitString);
		this.serviceText.setText(this.serviceString);
		this.interText.setText(this.interString);
	}
		
	private void getCenterValues(boolean get, int[] animationPoints) {
		if(get) {
			int rightTimeInt = processor1.getTime();
			int leftTimeInt = rightTimeInt + 10000;
			int centerTimeInt = rightTimeInt + 5000;
			this.rightTimeString = Integer.toString(rightTimeInt);
			this.leftTimeString = Integer.toString(leftTimeInt);
			this.centerTimeString = Integer.toString(centerTimeInt);
			Circle queueCircle = new Circle(800 - (this.timeInt - (animationPoints[0]/1000)), 
					400 - (animationPoints[1] * 4), 2);
			queueCircle.setFill(Color.GREEN);
			Circle blockedCircle = new Circle(800 - (this.timeInt - (animationPoints[0]/1000)), 
					400 - (animationPoints[2] * 4), 2);
			blockedCircle.setFill(Color.RED);
			Circle cleanCircle = new Circle(800 - (this.timeInt - (animationPoints[0]/1000)), 
					400 - (animationPoints[3] * 4), 2);
			cleanCircle.setFill(Color.YELLOW);
			circleQueue.offer(queueCircle);
			circleQueue.offer(blockedCircle);
			circleQueue.offer(cleanCircle);
		}
		
	}
	
	private void setCenterValues() {
		this.rightTime.setText(this.rightTimeString);
		this.leftTime.setText(this.leftTimeString);
		this.centerTime.setText(this.centerTimeString);
		moveDots();
	}
	
	private void moveDots() {
		
		while(this.circleQueue.size() > 0) {
			centerPane.getChildren().add(circleQueue.poll());
		}
		int x = this.centerPane.getChildren().size();

		boolean[] remove = new boolean[x];
		Circle lastCircle = null;
		
		for(int i = 0; i < centerPane.getChildren().size(); i++) {
			Node e = centerPane.getChildren().get(i);
			
			if(e instanceof Circle) {

				if(e.getLayoutX() <= -800) {
					remove[i] = true;
				}
				e.setLayoutX(e.getLayoutX() - this.timeDiff);
//				if(!(lastCircle == null)) {
//					System.out.println("line");
//					Line line = new Line(e.getLayoutX(), e.getLayoutX(), 
//							lastCircle.getLayoutX(), lastCircle.getLayoutX());
//					line.setStroke(Color.GREEN);
//					centerPane.getChildren().add(line);
//				}
				lastCircle = (Circle) e;
			}
		}
		removeDots(remove);
	}
	
	private void removeDots(boolean[] remove) {
		for(int i = remove.length - 1; i >= 0; i--) {
			if(remove[i] == true) {
				centerPane.getChildren().remove(i);
			}
		}
	}
	
	private void getLeftTopPane() {
		this.leftTopPane = new VBox();
		this.leftTopPane.setPadding(new Insets(15, 15, 15, 15));
		this.leftTopPane.getChildren().addAll(this.activeQueueSize, this.activeQueueSizeValue,
				this.rejectedProcessLabel, this.rejectedProcessValue, this.blockedQueueSize,
				this.blockedQueueSizeValue);
	}
	
	private void getLeftBottomPane() {
		this.leftBottomPane = new GridPane();
		this.leftBottomPane.setPadding(new Insets(15, 15, 15, 15));
		leftBottomPane.add(this.centerHeadingText, 1, 1);
		leftBottomPane.add(this.headingID, 1, 2);
		leftBottomPane.add(this.headingArrive, 1, 3);
		leftBottomPane.add(this.headingInnitialWait, 1, 4);
		leftBottomPane.add(this.headingServiceTime, 1, 5);
		leftBottomPane.add(this.headingTrndArndTime, 1, 6);
		
		setLeftBottom();
		
		leftBottomPane.add(this.headingIDValue, 2, 2);
		leftBottomPane.add(this.headingArriveValue, 2, 3);
		leftBottomPane.add(this.headingInnitialWaitValue, 2, 4);
		leftBottomPane.add(this.headingServiceTimeValue, 2, 5);
		leftBottomPane.add(this.headingTrndArndTimeValue, 2, 6);
	}
	
	public void setLeftBottom() {
		int v = this.finishedProcessView.getID();
		int w = this.finishedProcessView.getArrivalTime() / 10000;
		int x = this.finishedProcessView.getInnitialWaitTime();
		int y = this.finishedProcessView.getServiceTime();
		int z = this.finishedProcessView.getTurnAroundTime();
		
		this.headingIDValue.setText(Integer.toString(v));
		this.headingArriveValue.setText(Integer.toString(w));
		this.headingInnitialWaitValue.setText(Integer.toString(x));
		this.headingServiceTimeValue.setText(Integer.toString(y));
		this.headingTrndArndTimeValue.setText(Integer.toString(z));
	}

	public void getLeftValues() {
		int activeQuantity = processor1.getActiveQueue();
		this.activeQueueSizeString = Integer.toString(activeQuantity);
		
		int rejectedInt = processor1.getRejectedProcess();
		this.rejectedProcessString = Integer.toString(rejectedInt);
		
		int blockedInt = processor1.getBlockedQueue();
		this.blockedQueueSizeString = Integer.toString(blockedInt);
	}
	
	public void setLeft() {
		this.rejectedProcessValue.setText(rejectedProcessString);
		this.activeQueueSizeValue.setText(activeQueueSizeString);
		this.blockedQueueSizeValue.setText(blockedQueueSizeString);
	}
		
	private void run() {
		executor.execute(new TopInterfaceTask(this));
		executor.execute(new LeftInterfaceTask(this));
		executor.execute(new ProcessorTask(this.processor1, this));
		executor.execute(new AnimateTimeline(this.processor1, this));
		
	}
	
	private void finishProcess() {
		this.centerHeadingText.setText("Last Finished Process");
		executor.execute(new RetreivieLastTask(this.processor1, this));
	}

	public void setProcessView(RRProcess process1) {
		this.finishedProcessView = process1;
	}
	
	public void report() {
		String[] reporting = new String[]{this.time,
					this.trnarnd,
					this.innitialWaitString, 
					this.serviceString, 
					this.interString};
		FirstHundredPrinter fhp = new FirstHundredPrinter(this.processor1.getFirstHundred(), reporting);
		fhp.generateReport();
	}

	/*------------------------------------------------------------*/
	/*runnable classes*/
	
	class TopInterfaceTask implements Runnable {
		Interface interface1;
		public TopInterfaceTask(Interface interface1) {
			this.interface1 = interface1;
		}

		@Override
		public void run() {
			while(this.interface1.getRunning()) {
				try {
					
					interface1.getTopValues();
					
					Platform.runLater(() -> interface1.setTop());
						
					Thread.sleep(100);
					
				}
				catch (InterruptedException ex) {
					
				}
			}

		}
		
	}
	
	class LeftInterfaceTask implements Runnable {
		Interface interface1;
		public LeftInterfaceTask(Interface interface1) {
			this.interface1 = interface1;
		}

		@Override
		public void run() {
			while(this.interface1.getRunning()) {
				try {
					
					interface1.getLeftValues();
					
					Platform.runLater(() -> interface1.setLeft());
						
					Thread.sleep(100);
					
				}
				catch (InterruptedException ex) {
					
				}
			}

		}
		
	}
	
	class ProcessorTask implements Runnable {
		Processor processor1;
		Interface interface1;
		public ProcessorTask(Processor processor1, Interface interface1) {
			this.processor1 = processor1;
			this.interface1 = interface1;
		}

		@Override
		public void run() {
			while(this.interface1.getRunning()) {
				try {
					if(this.processor1.getQueueSize() > 0) {
						processor1.run();
					} else {
						this.processor1.idleCycle();
					}
						
					Thread.sleep(100);
					
				}
				catch (InterruptedException ex) {
					
				}
			}
			
		}
		
	}
	
	class RetreivieLastTask implements Runnable {		
		Processor processor1; 
		Interface interface1;
		public RetreivieLastTask(Processor processor1, Interface interface1) {
			this.processor1 = processor1;
			this.interface1 = interface1;
		}
		@Override
		public void run() {
			interface1.setProcessView(processor1.getLastFinishedProcess());			
			Platform.runLater(() -> interface1.setLeftBottom());
		}		
	}	

	class AnimateTimeline implements Runnable {
		
		Processor processor1; 
		Interface interface1;
		public AnimateTimeline(Processor processor1, Interface interface1) {
			this.processor1 = processor1;
			this.interface1 = interface1;
		}
		@Override
		public void run() {
			while(this.interface1.getRunning()) {
				int x = 0;
				boolean get = false;
				try {
					if(x == 0) {
						get = true;
						x = 5;
					}
					interface1.getCenterValues(get, processor1.retrieveAnimationPoints());
					Platform.runLater(() -> interface1.setCenterValues());						
					Thread.sleep(500);
					x--;
				}
				catch (InterruptedException ex) {
					
				}
			}		
		}		
	}
}
