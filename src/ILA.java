import java.util.ArrayList;
import java.util.HashMap;

public class ILA {
	private HashMap<String, ArrayList<ArrayList<String>>> uniqCombinations;
	private ArrayList<String> rules;

	public ILA(){
		uniqCombinations = new HashMap<String, ArrayList<ArrayList<String>>>();
	}

	public void trainILA(Data trainData){
		int numComb = 1;
		HashMap<String, ArrayList<ArrayList<String>>> tempCombinations = new HashMap<String, ArrayList<ArrayList<String>>>();
		ArrayList<String> uniqClasses = trainData.getUniqClasses();
		ArrayList<String[]> arguments = trainData.getArguments();
		ArrayList<String> classes = trainData.getClasses();

		for(String className : uniqClasses){
			ArrayList<ArrayList<String>> uniqArguments = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> tempArguments = new ArrayList<ArrayList<String>>();

			for(ArrayList<String> temp : trainData.getUniqArguments()){
				uniqArguments.add(new ArrayList<String>());
				tempArguments.add(new ArrayList<String>());
			}

			uniqCombinations.put(className, uniqArguments);
			tempCombinations.put(className, tempArguments);
		}

		for(int i = 0; i < arguments.size(); i++){
			String[] argument = arguments.get(i);
			String className = classes.get(i);

			ArrayList<ArrayList<String>> uniqArguments = uniqCombinations.get(className);

			for(int j = 0; j < argument.length; j++){
				ArrayList<String> uniqArgument = uniqArguments.get(j);

				if(!uniqArgument.contains(argument[j])){
					uniqArgument.add(argument[j]);
				}
			}

		}

		fillTempComb(tempCombinations, trainData);

		System.out.println();
		//fillUniqCombinations(trainData, numComb);

		//tempCombinations = cloneCombinations(trainData.getUniqClasses());
	}

	public ArrayList<String[]> testILA(Data testData){
		ArrayList<String[]> results = new ArrayList<String[]>();


		return results;
	}

	private void fillTempComb(HashMap<String, ArrayList<ArrayList<String>>> tempCombinations, Data data){
		ArrayList<String> classes = data.getUniqClasses();

		for(String className : classes){
			ArrayList<ArrayList<String>> uniqArguments = uniqCombinations.get(className);
			ArrayList<ArrayList<String>> tempArguments = tempCombinations.get(className);

			int size = uniqArguments.size();

			for(int i = 0; i < size; i++){
				ArrayList<String> uniqArg = uniqArguments.get(i);
				ArrayList<String> tempArg = tempArguments.get(i);

				for(int j = 0; j < uniqArguments.size(); j++){
					boolean contains = checkIfInAnother(uniqArg.get(j), classes, i, j, className);

					if(!contains){
						tempArg.add(uniqArg.get(j));
					}
				}
			}
		}
	}

	private boolean checkIfInAnother(String argument, ArrayList<String> classes, int i, int j, String actClass){
		for(String anotherClass : classes){
			if(!actClass.equals(anotherClass)){
				ArrayList<ArrayList<String>> anotherArguments = uniqCombinations.get(anotherClass);
				ArrayList<String> anotherArg = anotherArguments.get(i);
				String value = anotherArg.get(j);
				
				if(value.equals(argument)){
					return true;
				}
			}
		}
		
		return false;
	}
}
