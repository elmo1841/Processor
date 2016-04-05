package roundRobinPrgm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;

public class FirstHundredPrinter {
	
	private Queue<RRProcess> firstHundred = new LinkedList<>();
	private String[] reporting;
	private String folderName;
	private java.io.File file;

	public FirstHundredPrinter(Queue<RRProcess> firstHundred, String[] reporting) {
		this.firstHundred = firstHundred;	
		this.reporting = reporting;
	}
	
	public void generateReport() {
		Calendar gc1 = new GregorianCalendar();
		
		Integer Day = gc1.get(gc1.DATE);
		String DateString = Day.toString();
		
		Integer Month = gc1.get(gc1.MONTH);
		DateString += Month.toString();
		
		Integer Hour = gc1.get(gc1.HOUR);
		DateString += Hour.toString();
		Integer Minute = gc1.get(gc1.MINUTE);
		DateString += Minute.toString();
		Integer Second = gc1.get(gc1.SECOND);
		DateString += Second.toString();
		
		this.folderName = DateString;
		
		makeFolder();
		printReport();
	}
	
	
	public void makeFolder() {
		File dir = new File(folderName);
		dir.mkdir();
	}
	
	public void printReport() {
		this.file = new java.io.File(this.folderName + "/first_hundred_report.txt");
		if (file.exists()) {
			System.out.println("File already exists");
			System.exit(1);
		}
		try {
			java.io.PrintWriter output = new java.io.PrintWriter(this.file);
			System.out.println(this.firstHundred.size());
			output.println("Time at stop: " + this.reporting[0] + "000");
			output.println("Average turn around time: " + this.reporting[1]);
			output.println("Average innitial wait time: " + this.reporting[2]);
			output.println("Average service time: " + this.reporting[3]);
			output.println("Average inter arrival time: " + this.reporting[4]);
			output.println("----------------------------------------------------");
			output.println("----------------------------------------------------");
			
			for(int i = 0; i < 100; i ++) {
				
				RRProcess current = this.firstHundred.remove();
				int ID = current.getID();
				int ServiceTime = current.getServiceTime();
				int ArrivalTime = current.getArrivalTime();
				int InnitialWaitTime = current.getInnitialWaitTime();
				int TurnAroundTime = current.getTurnAroundTime();
				int InnactiveTime = current.getInnactiveTime();

				output.println("Process ID:            " + ID);
				output.println("Service Time:          " + ServiceTime);
				output.println("Arrival Time:          " + ArrivalTime);
				output.println("Innitial Wait Time:    " + InnitialWaitTime);
				output.println("Turn Around Time:      " + TurnAroundTime);
				output.println("Innactive Time:        " + InnactiveTime);
				output.println("--------Process # " + i + "----------------------------------");

			}
		
			
			
			output.close();
		} catch (Exception IOException) {
			System.out.println("exception here");
			System.exit(1);
		}		
	}
	
}