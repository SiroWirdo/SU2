import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
	
	public static void main(String[] args){
		
		int folds = 5;
		boolean width = false;
		int sectSize = 5;
		String name = "wine";
		
		DataSource dataSource = new DataSource("c:/Studia/Systemy ucz¹ce sie v2/" + name + "/" + name + "2.data", "c:/Studia/Systemy ucz¹ce sie v2/" + name + "/" + name + ".names");
		Data data = dataSource.getData();
		//data.printData();
		DataSet dataset = new DataSet(data, folds, width, sectSize);
	//	data.printUniqArguments();
		double[] finResults = {0.0, 0.0, 0.0, 0.0};
		
		for(int i = 0; i < folds; i++){
			System.out.println("-------" + i + "-------");
			Data train = dataset.getTrainingData();
		//	train.printUniqArguments();
			Data test = dataset.getTestData();
			
		//	train.printData();
			System.out.println("-------TEST-------");
		//	test.printData();
			
			Bayes bayes = new Bayes();
			bayes.trainBayes(train);
			//bayes.printOccurence();
			ArrayList<String[]> results = bayes.testBayes(test);
			
			Results result = new Results();
			
			double[] fin = result.countTF(test.getUniqClasses(), results);
			
			for(int j = 0; j < finResults.length; j++){
				finResults[j] += fin[j];
			}
			
			dataset.nextFold();
		}
		
		for(int j = 0; j < finResults.length; j++){
			finResults[j] /= folds;
		}
		
		PrintWriter writer;
		try {
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
			String path = "c:/Studia/Systemy ucz¹ce sie v2/zad1/";
			String nameRes = name + "_" +sdf.format(cal.getTime()) + "_" + folds + "_" + width + "_" + sectSize + ".csv";

			writer = new PrintWriter(path + nameRes, "UTF-8");
			writer.println("Accuracy;Precision;Recall;FScore");
			
			String acc = Double.toString(finResults[0]).replaceFirst("\\.", ",");
			String prec = Double.toString(finResults[1]).replaceFirst("\\.", ",");
			String recall = Double.toString(finResults[2]).replaceFirst("\\.", ",");
			String fscr = Double.toString(finResults[3]).replaceFirst("\\.", ",");
			
			writer.println(acc + ";" + prec + ";" + recall + ";" + fscr);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("==========FINAL AVERAGE===========");
		System.out.println("Accuracy = " + finResults[0] + " Precision = " + finResults[1] + " Recall = " + finResults[2] + " FScore " + finResults[3]);
	}
}
