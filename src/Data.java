import java.util.ArrayList;

public class Data {

	private ArrayList<String[]> arguments;
	private ArrayList<String> classes;
	private ArrayList<String> names;
	private ArrayList<String> types;
	private ArrayList<String> uniqClasses;
	private ArrayList<ArrayList<String>> uniqArguments;

	public Data(){
		arguments = new ArrayList<String[]>();
		classes = new ArrayList<String>();
		names = new ArrayList<String>();
		types = new ArrayList<String>();
		uniqClasses = new ArrayList<String>();
	}
	
	public Data(ArrayList<String[]> arguments, ArrayList<String> classes, ArrayList<String> uniqClasses, ArrayList<String> names, ArrayList<String> types){
		this.arguments = arguments;
		this.classes = classes;
		this.names = names;
		this.types = types;
		this.uniqClasses = uniqClasses;
	}

	public void addArgument(String[] line){
		arguments.add(line);
	}

	public void addClass(String value){
		classes.add(value);
	}
	
	public void addName(String name){
		names.add(name);
	}
	
	public void addType(String type){
		types.add(type);
	}

	public ArrayList<String[]> getArguments(){
		return arguments;		
	}

	public ArrayList<String> getClasses(){
		return classes;
	}

	public ArrayList<String> getNames(){
		return names;
	}
	
	public ArrayList<String> getTypes(){
		return types;
	}
	
	public String getType(int i){
		return types.get(i);
	}
	
	public void addUniqArguments(ArrayList<ArrayList<String>> uniq){
		uniqArguments = uniq;
	}
	public ArrayList<ArrayList<String>> getUniqArguments(){
		return uniqArguments;
	}
	
	public void printUniqArguments(){
		for(ArrayList<String> sect : uniqArguments){
			for(String s : sect){
				System.out.print(s + " ");
			}
			System.out.println("");
		}
	}
	
	public void printData(){
		for(int i = 0; i < arguments.size(); i++){
			System.out.println("");
			for(int j = 0; j < arguments.get(i).length; j++){
				System.out.print("d " + arguments.get(i)[j] + " " );
			}
			System.out.print(classes.get(i));
		}
		System.out.println("");
	}
	
	public int getDataSize(){
		return arguments.size();
	}
	
	public void setUniqClasses(){
		uniqClasses = new ArrayList<String>();
		for(String cl : classes){
			if(!uniqClasses.contains(cl)){
				uniqClasses.add(cl);
			}
		}
	}
	
	public ArrayList<String> getUniqClasses(){
		return uniqClasses;
	}
}
