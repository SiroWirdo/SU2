import java.util.ArrayList;
import java.util.HashMap;

public class Bayes {
	private HashMap<String, ArrayList<ArrayList<Double>>> occurence;
	private ArrayList<String> uniqClasses;
	private HashMap<String, Double> classOccur;

	public void trainBayes(Data trainingData){
		uniqClasses = trainingData.getUniqClasses();
		ArrayList<String[]> arguments = trainingData.getArguments();
		ArrayList<String> classes = trainingData.getClasses();
		ArrayList<ArrayList<String>> uniqArguments = trainingData.getUniqArguments();
		ArrayList<String> types = trainingData.getTypes();
		occurence = new HashMap<String, ArrayList<ArrayList<Double>>>();
		classOccur = new HashMap<String, Double>();
		
		/* Stwórz macierz wyst¹pieñ dla ka¿dej klasy */
		for(String uniqClass : uniqClasses){
			ArrayList<ArrayList<Double>> values = new ArrayList<ArrayList<Double>>();

			for(ArrayList<String> line : uniqArguments){
				ArrayList<Double> occ = new ArrayList<Double>();

				for(String val : line){
					occ.add(0.0);
				}

				values.add(occ);
			}
			occurence.put(uniqClass, values);
		}
		//System.out.println("WESZ£O3");
		for(int i = 0; i < classes.size(); i++){
			String actClass = classes.get(i);
			String[] actArg = arguments.get(i);
			ArrayList<ArrayList<Double>> occurList = occurence.get(actClass);
//System.out.println("WESZ£O");
			for(int j = 0; j < types.size() - 1; j++){
				ArrayList<String> uniqArg = uniqArguments.get(j);
				ArrayList<Double> occurArg = occurList.get(j);

				if(types.get(j).equals("continuous")){
					int idx = getIndexOfSection(uniqArg, actArg[j]);
					double val = occurArg.get(idx);
					occurArg.set(idx, val + 1);
				//	System.out.println("IDX "+idx+", "+occurArg.get(idx));
				}else{
					int idx = uniqArg.indexOf(actArg[j]);
					double val = occurArg.get(idx);
					occurArg.set(idx, val + 1);
				}
				/*for (Double d: occurArg)
					System.out.println("arg "+d);
				System.out.println("===");*/
			}
			//System.out.println("===============");
		}

		for(String uniqClass : uniqClasses){
			ArrayList<ArrayList<Double>> occurList = occurence.get(uniqClass);

			fillWithProb(occurList);

		}

		fillClassOccur(trainingData.getClasses());
	}

	public ArrayList<String[]> testBayes(Data testData){
		ArrayList<String> types = testData.getTypes();
		ArrayList<ArrayList<String>> uniqArguments = testData.getUniqArguments();
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String[]> finalResults = new ArrayList<String[]>();
		
		for(String[] arguments : testData.getArguments()){
			double prob = 0.0;
			String bestClass = "";
			
			for(String uniqClass : uniqClasses){
				double argProb = 1.0;
				
				for(int i = 0; i < arguments.length; i++){
					String actArg = arguments[i];
					String type = types.get(i);
					int index = 0;

					if(type.equals("continuous")){
						index = getIndexOfSection(uniqArguments.get(i), actArg);
					}else{
						index = getIndexOfArgument(uniqArguments.get(i), actArg);
					}
					
					ArrayList<ArrayList<Double>> probArguments = occurence.get(uniqClass);
					
					argProb *= probArguments.get(i).get(index);
				}
				
				double classProb = classOccur.get(uniqClass);
				double actProb = argProb * classProb;
				
				if(actProb > prob){
					bestClass = uniqClass;
					prob = actProb;
				}
			}
			
			results.add(bestClass);
		}
		
		for(int i = 0; i < testData.getClasses().size(); i++){
			String[] value = {testData.getClasses().get(i), results.get(i)};
			System.out.println("Correct = " + value[0] + " predicted = " + value[1]);
			
			finalResults.add(value);
		}
		
		return finalResults;
	}

	private void fillClassOccur(ArrayList<String> classes){
		for(String uniqClass : uniqClasses){
			classOccur.put(uniqClass, 0.0);
		}

		for(String actClass : classes){
			double value = classOccur.get(actClass);
			classOccur.put(actClass, value + 1);
		}

		for(String uniqClass : uniqClasses){
			double value = classOccur.get(uniqClass) + 1;
			classOccur.put(uniqClass, value/classes.size());
		}

	}

	private void fillWithProb(ArrayList<ArrayList<Double>> occur){
		for(ArrayList<Double> argument : occur){
			//System.out.println("ARG "+argument);
			double amount = 0;

			for(double val : argument){
				amount += val;
			}

			for(int i = 0; i < argument.size(); i++){
				double value = argument.get(i) + 1;

				argument.set(i, 1.0*value/amount);
				//System.out.println(i +" >> "+value+", "+amount+","+(1.0*value/amount));
			}
		}
	}

	private int getIndexOfSection(ArrayList<String> sections, String argument){
		double value = Double.parseDouble(argument);

		if(value <= Double.parseDouble(sections.get(0))){
			return 0;
		}

		if(value > Double.parseDouble(sections.get(sections.size() - 1))){
			return sections.size() - 1;
		}

		for(int i = 1; i < sections.size() - 1; i++){
			String[] section = sections.get(i).split("_");

			if(value > Double.parseDouble(section[0]) && value <= Double.parseDouble(section[1])){
				return i;
			}
		}

		return -1;
	}

	public int getIndexOfArgument(ArrayList<String> arguments, String argument){
		for(int i = 0; i < arguments.size(); i++){
			if(argument.equals(arguments.get(i))){
				return i;
			}
		}

		return -1;
	}

	public void printOccurence(){
		for (String uniqClass : uniqClasses){
			System.out.println("========" + uniqClass + "=======");
			ArrayList<ArrayList<Double>> occur = occurence.get(uniqClass);

			for(ArrayList<Double> argument : occur){
				System.out.println("");
				for(Double val : argument){
					System.out.print(val + " ");
				}
			}
		}
	}
}
