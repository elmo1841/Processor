package generic;

import processorInterface.Interface;
import javafx.application.Application;
import javafx.stage.Stage;

public class launch extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage)  {
		
		Interface interface1 = new Interface();
	}

}  