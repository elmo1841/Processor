package roundRobinPrgm;

import java.util.LinkedList;
import java.util.Queue;

import processorInterface.Interface;


public class Processor {
	
	private Interface interface1;
	
	private int finishedRRProcesses = 0;
	private int trnarndTime;
	private int innitWaitTime;
	private int serviceTime;
	private int interArrivalTime = 0;
	
	private int quantime = 10;
	private int contextSwitch = 0;
	private int time = 0;
	
	private int flowControl = 0;
	
	private Queue<RRProcess> queueA = new LinkedList<>();
	private Queue<RRProcess> queueB = new LinkedList<>();
	private Queue<RRProcess> cleanQueue = new LinkedList<>();
	private Queue<RRProcess> blockedQueue = new LinkedList<>();
	
	private Queue<RRProcess> firstHundred = new LinkedList<>();
	
	private Queue<int[]> displayQueue = new LinkedList<>();
	
	int queueControl = 1;
	int rejectedRRProcesses = 0;
	private boolean blockedQueueControl = false;
	
	private boolean cleanQueueControl = false;
	private int cleanQueueTimer = 20;
	
	private ProcessGenerator ProcessGen1;
	
	private RRProcess activeRRProcess;
	private RRProcess lastFinishedRRProcess;
	
	//constructor statement----------------------------------------------------
	public Processor(Interface interface1) {
		ProcessGen1 = new ProcessGenerator(this);
		this.interface1 = interface1;
		
		queueA.offer(ProcessGen1.createProcess());
		this.lastFinishedRRProcess = this.queueA.peek();
	}
	
	
	//getters and setters-------------------------------------------------------
	
	public RRProcess getLastFinishedProcess() {
		return this.lastFinishedRRProcess;
	}
	
	public int getTrnarnd() {
		return this.trnarndTime;
	}
	
	public int getInnitWaitTime() {
		return this.innitWaitTime;
	}
	
	public int getServiceTime() {
		return this.serviceTime;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public int getBlockedQueue() {
		return this.blockedQueue.size();
	}
	
	public int getRejectedProcess() {
		return this.rejectedRRProcesses;
	}
	
	public int getQueueSize() {
		return this.queueA.size() + this.queueB.size();
	}

	public Queue<int[]> getDisplayQueue() {
		return this.displayQueue;
	}
	
	public int getActiveQueue() {		
		if(this.queueControl > 0) {
			return this.queueA.size();
		} else {
			return this.queueB.size();
		}
	}
	
	public Queue<RRProcess> getFirstHundred() {
		return this.firstHundred;
	}
	
	public int getInterArrivalTime() {
		return this.interArrivalTime;
	}

	
	//functions----------------------------------------------------------------------------------
	

	

	public void run() {	//called by thread in Interface to start each cycle
		if(queueControl > 0) {  //QueueControl decides which queue becomes active and passive
			PassQueue(this.queueA, this.queueB);			
		} else {		
			PassQueue(this.queueB, this.queueA);			
		}
	}
	
	//PassQueue checks if processes should be added from blocked queue
	//at the end it changes the value of QueueControl
	private void PassQueue(Queue<RRProcess> activeQueue, Queue<RRProcess> passiveQueue) {
		
		if(this.trnarndTime > 1000 && this.cleanQueueTimer <= 0) {
			runCleanQueue(activeQueue, passiveQueue, this.cleanQueue);
		} else {
			if(this.blockedQueueControl && activeQueue.size() < 60) {
				activeQueue = filterBlockedQueue(activeQueue);
			}
			this.cycle(activeQueue, passiveQueue);			
		}
		
		this.cycle(activeQueue, passiveQueue);
		
		this.cleanQueueTimer--;
		this.queueControl *= -1;
	}
	
	//cycle each process in the active queue goes through
	private void cycle(Queue<RRProcess> activeQueue, Queue<RRProcess> passiveQueue) {	
		while(activeQueue.size() > 0) {
			//next process is pulled from queue and set as activeProcess
			activeRRProcess = activeQueue.poll();
			//servicestamp added in process
			activeRRProcess.addServiceStamp(this.time);
			//control for where it goes at end of cycle
			boolean RRProcessFinish = false;
			//cycle based on int quantum
			int runtime = quantime;
			if(activeRRProcess.getPriority() == 3) {
				runtime = quantime;
			}
			else if(activeRRProcess.getPriority() == 1) {
				runtime = quantime + 5;
			} 
			else {
				runtime = activeRRProcess.getServiceTime();
			}
			for(int k = 0; k < runtime; k ++) {			
				activeRRProcess.perform(-1);
				this.time++;
				//each time the process is worked on it checks for the finished flag
				if(activeRRProcess.getFlag()) {
					activeRRProcess.addServiceStamp(this.time);
					//when finished process is sent one last perform command with time 0
					//this tells it to run its final activities
					activeRRProcess.perform(0);
					//k is set to quantum to finish loop
					k = runtime;
					//change boolean to control where process goes
					RRProcessFinish = true;
					this.finishedRRProcesses++;
				}
				//at the end of each time we check to see if new processes have come in
				newRRProcessCheck(activeQueue, passiveQueue);
			}
			//if the process has not finished we see if it is due for priority turnaround
			
			if(!RRProcessFinish) {
				//in a cleanQueue run any processes not finished 
				//will automatically go back to active queue
				if(this.cleanQueueControl) {
					activeQueue.offer(activeRRProcess);
				} else {
					if(activeRRProcess.getServiceStamp() > 3) {
						activeRRProcess.returntoActive();
						activeQueue.offer(activeRRProcess);
					} else {
						if(activeRRProcess.getServiceTimeLeft() < 50) {
							activeRRProcess.setPriority(3);
						}
						if(activeRRProcess.getServiceTimeLeft() < 20) {
							activeRRProcess.setPriority(1);
						}
						passiveQueue.offer(activeRRProcess);
					}
				}
				
				passiveQueue.offer(activeRRProcess);
						
			} else {
				//if the process is finished the processor produces a finsih report 
				//and updates its stats
				finishReport(activeRRProcess);
				this.lastFinishedRRProcess = activeRRProcess;
				if(this.firstHundred.size() <= 100) {
					System.out.println(this.firstHundred.size());
					this.firstHundred.add(activeRRProcess);
				}
				//createDisplay(1, activeRRProcess);
			}
			//add context switch time
			this.time += this.contextSwitch;
		}
	}
	
	
	//in the event there are no processes in the Queue, Interface will call an idle cycle
	public void idleCycle() {
		do {
			this.time++;
		//time continues to count while processor looks for new processes
		//when it finds one it breaks loop
		} while(!newRRProcessCheck(this.queueA, this.queueB));
	}
	
	//function to randomly create new processes
	private boolean newRRProcessCheck(Queue<RRProcess> activeQueue, Queue<RRProcess> passiveQueue) {
		//random number 1-48 created looking for #13
		int roulette = (int)(Math.random() * 55);
		if(--this.flowControl < 0 && roulette == 13) {
			RRProcess pr1 = ProcessGen1.createProcess();
			this.flowControl = this.getQueueSize();
			
			//check to see if the active processes is more than 50
			//in that case process goes to blocked Queue
			
			if(this.getQueueSize() >= 100) {
				this.rejectedRRProcesses++;
				this.blockedQueue.offer(pr1);
				this.blockedQueueControl = true;
				return false;
				//when space in queue new process goes directly to active queue 
			} else {
				//if processes exist in blocked queue
				//new process goes to blockedQueue
				//first process comes out of blocked and goes to active
				if(this.blockedQueue.size() > 0) {
					RRProcess moveProcess = this.blockedQueue.poll();
					if(this.cleanQueueControl) {
						passiveQueue.offer(moveProcess);
					} else {
						activeQueue.offer(moveProcess);
					}				
					this.blockedQueue.offer(pr1);
				} else {
					if(this.cleanQueueControl) {
						passiveQueue.offer(pr1);
					} else {
						activeQueue.offer(pr1);
					}			
				}			
				int total = this.queueA.size() + this.queueB.size();
				this.serviceTime = ((this.serviceTime * (total - 1)) + 
						pr1.getServiceTime()) / total;
				return true;
			}
		}
		//returns to break out of idel cycle
		return false;
	}
	
	//numbers updated each time a process finishes
	private void finishReport(RRProcess activeRRProcess) {
		this.trnarndTime = ((this.trnarndTime * (this.finishedRRProcesses - 1)) + 
				activeRRProcess.getTurnAroundTime()) / this.finishedRRProcesses;
		this.innitWaitTime = ((this.innitWaitTime * (this.finishedRRProcesses - 1)) + 
				activeRRProcess.getInnitialWaitTime()) / this.finishedRRProcesses;
		this.interArrivalTime = (((activeRRProcess.getArrivalTime() - this.lastFinishedRRProcess.getArrivalTime()) + 
				(this.innitWaitTime * this.finishedRRProcesses - 1)) / this.finishedRRProcesses);
	}
	
	//called when there is room to get a lot of processes out of blocked queue
	private Queue<RRProcess> filterBlockedQueue(Queue<RRProcess> activeQueue) {		
		for(int i =0; i < 10; i ++) {
			if(this.blockedQueue.size() > 0) {
				RRProcess pushProcess = this.blockedQueue.poll();
				activeQueue.offer(pushProcess);
			}
		}
		if (this.blockedQueue.size() == 0) {
			this.blockedQueueControl = false;
		}
		return activeQueue;
	}
	
	private void runCleanQueue(Queue<RRProcess> activeQueue, 
			Queue<RRProcess> passiveQueue, Queue<RRProcess> cleanQueue) {
		int target = (this.serviceTime / 2);
		
		while(activeQueue.size() > 0) {
			RRProcess checkProcess = activeQueue.poll();
			if(checkProcess.getServiceTimeLeft() > target) {
				cleanQueue.offer(checkProcess);
			} else {
				passiveQueue.offer(checkProcess);
			}
		}
		this.cleanQueueControl = true;
		cycle(cleanQueue, passiveQueue);
		this.cleanQueueControl = false;
		this.cleanQueueTimer = 21;
	}
	
	public int[] retrieveAnimationPoints() {
		int[] animationPoints = {
				this.getTime(),
				this.getQueueSize(),
				this.blockedQueue.size(),
				this.cleanQueue.size()};
				
		return animationPoints;
	}
}
