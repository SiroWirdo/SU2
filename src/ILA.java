import java.util.ArrayList;
import java.util.HashMap;

public class ILA {
	private HashMap<String, ArrayList<String>> uniqCombinations;
	private HashMap<String, ArrayList<ArrayList<String>>> rules;

	public ILA(){
		uniqCombinations = new HashMap<String, ArrayList<String>>();
		rules = new HashMap<String, ArrayList<ArrayList<String>>>();
	}

	public void trainILA(Data trainData){
		int numComb = 1;
		HashMap<String, ArrayList<String>> tempCombinations = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<ArrayList<String>>> tempData = new HashMap<String, ArrayList<ArrayList<String>>>(); /* do sprawdzenia ile zosta³o */
		ArrayList<String> uniqClasses = trainData.getUniqClasses();
		ArrayList<String[]> arguments = trainData.getArguments();
		ArrayList<String> classes = trainData.getClasses();

		for(String className : uniqClasses){
			ArrayList<String> uniqArguments = new ArrayList<String>();
			ArrayList<String> tempArguments = new ArrayList<String>();
			ArrayList<ArrayList<String>> tempDataArg = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> rulesLine = new ArrayList<ArrayList<String>>();

			uniqCombinations.put(className, uniqArguments);
			tempCombinations.put(className, tempArguments);
			tempData.put(className, tempDataArg);
			rules.put(className, rulesLine);
		}

		fillTempData(tempData, trainData);

		for(int i = 0; i < arguments.size(); i++){
			String[] argument = arguments.get(i);
			String className = classes.get(i);
			ArrayList<String> uniqArguments = uniqCombinations.get(className);

			for(int j = 0; j < argument.length; j++){

				if(!uniqArguments.contains(j + argument[j])){
					/* pierwsza liczba w kombinacji oznacza do kórego argumentu nale¿y */
					uniqArguments.add(j + argument[j]);
				}
			}

		}

		System.out.println();

		fillTempComb(tempCombinations, trainData);
		for(String className : uniqClasses){
			ArrayList<String> combination = tempCombinations.get(className);

		}

		System.out.println();
		//fillUniqCombinations(trainData, numComb);

		//tempCombinations = cloneCombinations(trainData.getUniqClasses());
	}

	public ArrayList<String[]> testILA(Data testData){
		ArrayList<String[]> results = new ArrayList<String[]>();


		return results;
	}

	private void fillTempComb(HashMap<String, ArrayList<String>> tempCombinations, Data data){
		ArrayList<String> classes = data.getUniqClasses();

		for(String className : classes){
			ArrayList<String> uniqArguments = uniqCombinations.get(className);
			ArrayList<String> tempArguments = tempCombinations.get(className);

			int size = uniqArguments.size();

			for(int i = 0; i < uniqArguments.size(); i++){
				boolean contains = checkIfInAnother(uniqArguments.get(i), classes, className);

				if(!contains){
					tempArguments.add(uniqArguments.get(i));
				}
			}
		}
	}

	private boolean checkIfInAnother(String argument, ArrayList<String> classes, String actClass){
		for(String anotherClass : classes){
			if(!actClass.equals(anotherClass)){
				ArrayList<String> anotherArguments = uniqCombinations.get(anotherClass);

				if(anotherArguments.contains(argument)){
					return true;
				}
			}
		}

		return false;
	}

	private void fillTempData(HashMap<String, ArrayList<ArrayList<String>>> tempCombinations, Data data){
		ArrayList<String> classes = data.getClasses();
		ArrayList<String[]> arguments = data.getArguments();

		for(int i = 0; i < arguments.size(); i++){
			String[] argument = arguments.get(i);
			String className = classes.get(i);
			ArrayList<ArrayList<String>> newArguments = tempCombinations.get(className);
			ArrayList<String> values = new ArrayList<String>();

			for(int j = 0; j < argument.length; j++){
				values.add(argument[j]);
			}
			newArguments.add(values);
		}
	}
}
