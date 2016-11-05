import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataSource {
	private Data data;

	public DataSource(String dataPath, String namesPath){
		try {
			Stream<String> stream = Files.lines(Paths.get(dataPath));

			data = new Data();
			
			stream.forEach(line -> addLine(line));
			stream.close();
			
			data.setUniqClasses();
			
			stream = Files.lines(Paths.get(namesPath));
			stream.forEach(line -> addNamesTypes(line));
			stream.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void addLine(String line){
		String[] lineTab;
		String[] argument;
		String classValue;
		int lineLength;
		
		if(!line.equals("")){

		lineTab = line.split(",");
		lineLength = lineTab.length;
		argument = new String[lineLength - 1];
		classValue = lineTab[lineLength -1];

		for(int i = 0; i < argument.length; i++){
			argument[i] = lineTab[i];
		}

		data.addArgument(argument);
		data.addClass(classValue);
		}
	}
	
	private void addNamesTypes(String line){
		String[] lineTab;
		
		lineTab = line.split(",");
		data.addName(lineTab[0]);
		data.addType(lineTab[1]);
	}

	public Data getData(){
		return data;
	}

}
