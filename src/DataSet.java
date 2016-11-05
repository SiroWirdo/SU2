import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DataSet {
	private Data data;
	private int crossvalCounter;
	private int folds;
	private ArrayList<Data> crossValData;
	private ArrayList<Integer> contIndex;
	private ArrayList<Integer> normalIndex;
	private ArrayList<ArrayList<String>> uniqArguments;

	public DataSet(Data data, int folds, boolean width, int sect){
		this.data = data;
		this.crossvalCounter = 0;
		this.folds = folds;
		this.crossValData = new ArrayList<Data>();
		this.contIndex = new ArrayList<Integer>();
		this.normalIndex = new ArrayList<Integer>();
		this.uniqArguments = new ArrayList<ArrayList<String>>();

		for(int i = 0; i < data.getArguments().get(0).length; i++){
			uniqArguments.add(new ArrayList<String>());
		}

		fillIndexes();
		fillUniqArguments();

		if(width){
			equalWidth(sect);
		}else{
			equalFrequency(sect);
		}
		crossvalidation();

		this.data.addUniqArguments(uniqArguments);
		for(Data crossData : crossValData){
			crossData.addUniqArguments(uniqArguments);
		}
	}

	private void crossvalidation(){
		int dataSize = data.getDataSize();
		int counter = 0;

		ArrayList<String[]> arguments = (ArrayList<String[]>)data.getArguments().clone();
		ArrayList<String> classes = (ArrayList<String>)data.getClasses().clone();

		for(int i = 0; i < folds; i++){
			crossValData.add(new Data());
		}

		for(int i = 0; i < dataSize; i++){
			Random rand = new Random();
			int argument = rand.nextInt(arguments.size());

			crossValData.get(counter).addArgument(arguments.get(argument));
			crossValData.get(counter).addClass(classes.get(argument));

			arguments.remove(argument);
			classes.remove(argument);

			if(counter == crossValData.size() - 1){
				counter = 0;
			}else{
				counter++;
			}
		}
	}

	public Data getTrainingData(){
		ArrayList<String[]> trainingArg = new ArrayList<String[]>();
		ArrayList<String> trainingClass = new ArrayList<String>();

		for(int i = 0; i < folds; i++){//zmieniæ?
			if(i != crossvalCounter){
				trainingArg.addAll(crossValData.get(i).getArguments());
				trainingClass.addAll(crossValData.get(i).getClasses());

			}
		}
		//System.out.println("UNIQ "+trainingClass.size()+", "+data.getNames().size());
		//for (String s: trainingClass) System.out.println("name "+s);
		Data newTrain = new Data(trainingArg, trainingClass, data.getUniqClasses(), data.getNames(), data.getTypes());
		newTrain.addUniqArguments(uniqArguments);

		return newTrain;
	}

	public Data getTestData(){
		ArrayList<String[]> trainingArg = new ArrayList<String[]>();
		ArrayList<String> trainingClass = new ArrayList<String>();

		trainingArg = (ArrayList<String[]>)crossValData.get(crossvalCounter).getArguments().clone();
		trainingClass = (ArrayList<String>)crossValData.get(crossvalCounter).getClasses().clone();

		Data testData = new Data(trainingArg, trainingClass, data.getUniqClasses(), data.getNames(), data.getTypes());
		testData.addUniqArguments(uniqArguments);
		return testData;
	}

	public void equalWidth(int numSect){
		ArrayList<String[]> arguments = data.getArguments();
		ArrayList<double[]> minMax = new ArrayList<double[]>();
		String[] argument = arguments.get(0);

		for(Integer index : contIndex){
			minMax.add(new double[2]);
		}
		
		/* Zainicjowanie pierwszych wartoœci */
		for(int i = 0; i < contIndex.size(); i++){
			minMax.get(i)[0] = Double.parseDouble(argument[contIndex.get(i)]);
			minMax.get(i)[1] = Double.parseDouble(argument[contIndex.get(i)]);
		}
		
		/* Znalezienie maksymalnych i minimalnych wartoœci argumentów */
		for(int i = 1; i < arguments.size(); i++){
			argument = arguments.get(i);
			for(int j = 0; j < contIndex.size(); j++){
				double value = Double.parseDouble(argument[contIndex.get(j)]);
				if(value < minMax.get(j)[0]){
					minMax.get(j)[0] = value;
				}

				if(value > minMax.get(j)[1]){
					minMax.get(j)[1] = value;
				}
			}
		}

		double sect_size = 0;
		
		/* Podzia³ na równe przedzia³y */
		for(int i = 0; i < contIndex.size(); i++){
			double[] min_max = minMax.get(i);
			int idx = contIndex.get(i);

			sect_size = (min_max[1]-min_max[0])/numSect;

			uniqArguments.set(idx, getSections(sect_size, min_max, numSect));
		}
	}

	private void equalFrequency(int sectLength){
		ArrayList<String[]> arguments = data.getArguments();
		ArrayList<ArrayList<Double>> argValues = new ArrayList<ArrayList<Double>>();
		ArrayList<String> types = data.getTypes();

		for(int i = 0; i < arguments.get(0).length; i++){
			argValues.add(new ArrayList<Double>());
		}

		for(String[] argument : arguments){
			for(int i = 0; i < argument.length; i++){
				argValues.get(i).add(Double.parseDouble(argument[i]));
			}
		}

		int sizeSect = (int)(1.0 * arguments.size()/sectLength);

		if(sizeSect == 0){
			sizeSect++;
		}

		for(int i = 0; i < argValues.size(); i++){
			if(types.get(i).equals("continuous")){
				ArrayList<Double> argValue = argValues.get(i);
				Collections.sort(argValue);

				ArrayList<String> sections = new ArrayList<String>();

				String sect = Double.toString(argValue.get(sizeSect));

				//System.out.println("Sect = " + sect);

				sections.add(sect);

				for(int j = sizeSect;  j < argValue.size(); j += sizeSect){
					if(j + sizeSect >= argValue.size()){
						sect = Double.toString(argValue.get(j));
					}else{
						sect = argValue.get(j) + "_" + argValue.get(j + sizeSect);
					}

				//	System.out.println("sect2 = " + sect);

					if(!sections.contains(sect)){
						sections.add(sect);
					}
				}
				//for (String s: sections)System.out.println("sec "+s);
				uniqArguments.set(i, sections);
			}else{
				ArrayList<Double> argValue = argValues.get(i);
				ArrayList<String> sections = new ArrayList<String>();
				
				for(Double val : argValue){
					String newVal = String.format("%.2f", val).replace(",", ".");
					
					if(!sections.contains(newVal)){
						sections.add(newVal);
					}
				}
				
				uniqArguments.set(i, sections);
			}
		}
	}

	private void fillIndexes(){
		ArrayList<String> types = data.getTypes();

		for(int i = 0; i < types.size(); i++){
			if(types.get(i).equals("continuous")){
				contIndex.add(i);
			}else{
				if(!types.get(i).equals("class")){
					normalIndex.add(i);
				}
			}
		}
	}

	private void fillUniqArguments(){
		ArrayList<String[]> arguments = data.getArguments();

		for(String[] argument : arguments){
			for(Integer i : normalIndex){
				if(!uniqArguments.get(i).contains(argument[i])){
					uniqArguments.get(i).add(argument[i]);
				}
			}
		}
	}

	private ArrayList<String> getSections(double sect_size, double[] minMax, int numSect){
		ArrayList<String> newSections = new ArrayList<String>();

		newSections.add("" + minMax[0]);

		double low = 0;
		double high = 0;
		for(int i = 1; i <= numSect; i++){
			low = minMax[0] + (i - 1) * sect_size;
			high = minMax[0] + i * sect_size;

			newSections.add(low + "_" + high);
		}

		newSections.add("" + high);

		return newSections;
	}


	public void nextFold(){
		if(crossvalCounter == folds - 1){
			crossvalCounter = 0;
		}else{
			crossvalCounter++;
		}
	}
}
