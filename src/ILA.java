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

		System.out.println();
		//fillUniqCombinations(trainData, numComb);

		//tempCombinations = cloneCombinations(trainData.getUniqClasses());
	}
	
	public ArrayList<String[]> testILA(Data testData){
		ArrayList<String[]> results = new ArrayList<String[]>();
		
		
		return results;
	}
	
	private void fillUniqCombinations(Data data, int numComb){
		ArrayList<String[]> arguments = data.getArguments();
		ArrayList<String> classes = data.getClasses();
		
		
	}
	
	private HashMap<String, ArrayList<ArrayList<String>>> cloneCombinations(ArrayList<String> classes){
		HashMap<String, ArrayList<ArrayList<String>>> tempComb = new HashMap<String, ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> arguments = new ArrayList<ArrayList<String>>();
		
		for(String actClass : classes){
			
		}
		
		return tempComb;
	}
}
