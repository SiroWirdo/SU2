import java.util.ArrayList;
import java.util.HashMap;

public class Results {

	public int[][] confusionMatrix(ArrayList<String> names, ArrayList<String[]> result){
		/* Wiersze to actual a kolumny to predicted */
		int[][] confMatrix = new int[names.size()][names.size()];

		System.out.println("===============CONFUSION MATRIX===============");

		for(int i = 0; i < result.size(); i++){
			int row = names.indexOf(result.get(i)[0]);
			int column = names.indexOf(result.get(i)[1]);
			confMatrix[row][column]++;
		}

		for(int i = 0; i < names.size(); i++){
			System.out.print(names.get(i) + " ");
		}

		System.out.println("");

		for(int i = 0; i < names.size(); i++){
			System.out.print(names.get(i) + " ");

			for(int j = 0; j < names.size(); j++){
				System.out.print(confMatrix[i][j] + " ");
			}
			System.out.println("");
		}

		return confMatrix;
	}

	public double[] countTF(ArrayList<String> names, ArrayList<String[]> result){
		int[][] confMatrix = confusionMatrix(names, result);
		System.out.println("===============TP,TN,FP,FN===============");

		/* 0 - TP, 1 - TN, 2 - FP, 3 - FN */
		HashMap<String, int[]> classValues = new HashMap<String, int[]>();

		for(String className : names){
			classValues.put(className, new int[4]);
		}

		for(int i = 0; i < confMatrix.length; i++){
			for(int j = 0; j < confMatrix[i].length; j++){
				if(i == j){
					String className = names.get(i);
					int[] TP = classValues.get(className);
					TP[0] += confMatrix[i][j];
				}else{
					String className = names.get(i);
					int[] FN = classValues.get(className);
					FN[3] += confMatrix[i][j];

					className = names.get(j);
					int[] FP = classValues.get(className);
					FP[2] += confMatrix[i][j];
				}

				for(int k = 0; k < names.size(); k++){
					if(i != k || j != k){
						String className = names.get(k);
						int[] TN = classValues.get(className);
						TN[1] += confMatrix[i][j];
					}
				}
			}
		}

		double finAccuracy = 0.0;
		double finPrecision = 0.0;
		double finRecall = 0.0;
		double finFScore = 0.0;
		int outClass = 0;
		
		for(String className : names){
			int[] val = classValues.get(className);
			int TP = val[0];
			int TN = val[1];
			int FP = val[2];
			int FN = val[3];

			System.out.println("===============" + className + "==============");

			System.out.println("TP = " + TP + " TN = " + TN + " FP = " + FP + " FN = " + FN);

			System.out.println("===============Accuracy===============");

			double accuracy = (TP + FP + FN + TN) == 0 ? -1.0 : 1.0 * (TP + TN)/(TP + FP + FN + TN);

			System.out.println("Accuracy = " + accuracy);

			System.out.println("===============Precision===============");

			double precision = (TP + FP) == 0 ? -1.0 : 1.0 * TP/(TP + FP);

			System.out.println("Precision = " + precision);

			System.out.println("===============Recall===============");

			/* recall, sensitivity */
			double recall = (TP + FN) == 0 ? -1.0 : 1.0 * TP/(TP + FN);
			
			System.out.println("Recall = " + recall);

			System.out.println("===============F-Score===============");

			double fScore = (2*TP + FP + FN) == 0 ? -1.0 : 2.0 * TP/(2*TP + FP + FN);

			System.out.println("F-Score = " + fScore);
			
			if(recall != -1 && precision != -1 && accuracy != -1 && fScore != -1){
				finAccuracy += accuracy;
				finPrecision += precision;
				finRecall += recall;
				finFScore += fScore;
			}else{
				outClass++;
			}
		}

		int size = names.size() - outClass;
		finAccuracy /= size;
		finPrecision /= size;
		finRecall /= size;
		finFScore /= size;

		System.out.println("==========AVERAGE===========");
		System.out.println("Accuracy = " + finAccuracy + " Precision = " + finPrecision + " Recall = " + finRecall + " FScore " + finFScore);

		double[] fin = {finAccuracy, finPrecision, finRecall, finFScore};
		return fin;
	}

	public void test(){
		ArrayList<String> names = new ArrayList<String>();
		String[][] actual = {{"Kot"},{"Pies"},{"Osa"},{"Pies"},{"Kot"},{"Osa"},{"Osa"},{"Kot"},{"Kot"},{"Pies"},{"Osa"}};
		String[][] predicted = {{"Kot"},{"Osa"},{"Osa"},{"Pies"},{"Pies"},{"Osa"},{"Osa"},{"Pies"},{"Kot"},{"Pies"},{"Kot"}};

		names.add("Kot");
		names.add("Pies");
		names.add("Osa");

		//confusionMatrix(names, actual, predicted);
	}

	public void test2(){
		ArrayList<String> names = new ArrayList<String>();
		String[][] actual = {{"True"},{"False"},{"True"},{"True"},{"False"},{"False"},{"True"},{"True"},{"True"},{"False"},{"True"},{"True"}};
		String[][] predicted = {{"False"},{"True"},{"True"},{"True"},{"False"},{"True"},{"False"},{"True"},{"True"},{"False"},{"False"},{"False"}};

		names.add("True");
		names.add("False");

		//countTF(names, actual, predicted);
	}
}
