import java.util.ArrayList;
import java.util.HashMap;

public class ILA {
	private HashMap<String, ArrayList<ArrayList<String>>> uniqCombinations;
	private ArrayList<String> rules;
	
	public ILA(){
		uniqCombinations = new HashMap<String, ArrayList<ArrayList<String>>>();
	}
	
	public void trainILA(Data trainData){
		int numbComb = 1;
		HashMap<String, ArrayList<ArrayList<String>>> tempCombinations = new HashMap<String, ArrayList<ArrayList<String>>>();
		
		fillUniqArguments(trainData);

		tempCombinations = cloneCombinations(trainData.getUniqClasses());
	}
	
	private void fillUniqArguments(Data data){
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
