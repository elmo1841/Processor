package roundRobinPrgm;

import java.util.ArrayList;

public class RRProcess {
	
	private int ID;
	private int serviceTime;
	private int arrivalTime;
	private int serviceTimeLeft;
	private int serviceStamps = 0;
	
	private boolean flagForFinish = false;
	
	private ArrayList<Integer> log;
	
	private int innitialWaitTime = 0;
	private int turnAroundTime = 0;
	private int innactiveTime = 0;
	private int priority;
	
	public RRProcess() {
		
	}
	
	//getters and setters ------------------------------------
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		this.ID = iD;
		//System.out.println(this.ID);
	}
	
	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
		if(serviceTime < 20) {
			this.priority = 1;
		}
		else if(serviceTime > 50) {
			this.priority = 2;
		} 
		else {
			this.priority = 3;
		}
		//System.out.println(this.serviceTime);
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
		//System.out.println(this.arrivalTime);
	}
	
	public int getServiceTimeLeft() {
		return serviceTimeLeft;
	}

	public void setServiceTimeLeft(int serviceTimeLeft) {
		this.serviceTimeLeft = serviceTimeLeft;
	}
	
	public int getServiceStamp() {
		return this.serviceStamps;
	}
	
	public boolean getFlag() {
		return this.flagForFinish;
	}
	
	public ArrayList<Integer> getLog() {
		return log;
	}

	public void setLog() {
		this.log = new ArrayList<Integer>();
		log.add(this.arrivalTime);
	}
	
	public int getInnitialWaitTime() {
		return this.innitialWaitTime;
	}

	public int getTurnAroundTime() {
		return this.turnAroundTime;
	}
	
	public int getInnactiveTime() {
		return this.innactiveTime;
	}
	
	public void setPriority(int x) {
		this.priority = x;
	}

	public int getPriority() {
		return this.priority;
	}

	
	//functions---------------------------------------------
	
	public void setInnitialServiceTimeLeft() {
		this.serviceTimeLeft = this.serviceTime;
		//System.out.println(this.serviceTimeLeft);
	}
	
	public void addServiceStamp(int time) {
		log.add(time);
		this.serviceStamps++;
	}
	
	public void perform(int time) {
		this.serviceTimeLeft += time;
		if(time == 0) {
			createLog();
		}
		checkServiceTimeLeft();
	}
	
	public void checkServiceTimeLeft() {
		if(this.serviceTimeLeft == 0) {
			this.flagForFinish = true;		
		}

	}
	
	public void returntoActive() {
		this.serviceStamps -= 2;
	}

	
	public void createLog() {
		this.innitialWaitTime = this.log.get(1) - this.log.get(0);
		this.turnAroundTime = this.log.get(this.log.size() -1) - this.log.get(0);
		this.innactiveTime = this.turnAroundTime - this.serviceTime;
	}
	
	public void printLog() {
		System.out.println("ID: " + this.ID);
		System.out.println("innitialWaitTime: " + this.innitialWaitTime);
		System.out.println("turnAroundTime: " + this.turnAroundTime);
		System.out.println("innactiveTime: " + this.innactiveTime);
	}
}





