package roundRobinPrgm;

public class ProcessGenerator {
	
	private Processor processor1;
	private int ID = 1000; 
	
	public ProcessGenerator(Processor processor1) {
		this.processor1 = processor1;
	}
	
	public RRProcess createProcess() {
		RRProcess process1 = new RRProcess();
		setVariables(process1);
		this.ID++;
		return process1;
	}
	
	public RRProcess getFiller() {
		RRProcess spaceFiller = new RRProcess();
		spaceFiller.setID(-1);
		return spaceFiller;
	}
	
	private void setVariables(RRProcess process1) {
		process1.setID(ID);
		process1.setServiceTime(randomNum());
		process1.setArrivalTime(processor1.getTime());
		process1.setInnitialServiceTimeLeft();
		process1.setLog();
	}
	
	private int randomNum() {
		return (int)((Math.random() * 100) + 1);
	}

}
