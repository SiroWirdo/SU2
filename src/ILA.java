import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ILA {
	private HashMap<String, ArrayList<String>> uniqCombinations;
	private HashMap<String, ArrayList<String>> uniqFirstCombinations;
	private HashMap<String, ArrayList<ArrayList<String>>> rules;

	public ILA(){
		uniqCombinations = new HashMap<String, ArrayList<String>>();
		uniqFirstCombinations = new HashMap<String, ArrayList<String>>();
		rules = new HashMap<String, ArrayList<ArrayList<String>>>();
	}

	public void trainILA(Data trainData){
		HashMap<String, ArrayList<String>> tempCombinations = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<ArrayList<String>>> tempData = new HashMap<String, ArrayList<ArrayList<String>>>(); /* do sprawdzenia ile zosta³o */
		ArrayList<String> uniqClasses = trainData.getUniqClasses();
		ArrayList<String[]> arguments = trainData.getArguments();
		ArrayList<String> classes = trainData.getClasses();

		//System.out.println("s1");

		for(String className : uniqClasses){
			//	System.out.println("CLASS "+className);
			ArrayList<String> uniqArguments = new ArrayList<String>();
			ArrayList<String> uniqFirstArguments = new ArrayList<String>();
			ArrayList<String> tempArguments = new ArrayList<String>();
			ArrayList<ArrayList<String>> tempDataArg = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> rulesLine = new ArrayList<ArrayList<String>>();

			uniqCombinations.put(className, uniqArguments);
			uniqFirstCombinations.put(className, uniqFirstArguments);
			tempCombinations.put(className, tempArguments);
			tempData.put(className, tempDataArg);
			rules.put(className, rulesLine);
		}

		//System.out.println("s2");

		fillTempData(tempData, trainData);

		//	System.out.println("s3");

		for(int i = 0; i < arguments.size(); i++){
			String[] argument = arguments.get(i);
			String className = classes.get(i);
			ArrayList<String> uniqArguments = uniqCombinations.get(className);
			ArrayList<String> uniqFirstArguments = uniqFirstCombinations.get(className);

			for(int j = 0; j < argument.length; j++){

				if(!uniqArguments.contains(j + ":" + argument[j])){
					/* pierwsza liczba odzielona : w kombinacji oznacza do kórego argumentu nale¿y */
					uniqArguments.add(j + ":" + argument[j]);
					uniqFirstArguments.add(j + ":" + argument[j]);
				}
			}

		}

		//System.out.println("s4");

		//System.out.println();

		int numComb = 1;

		while(!checkIfFinished(tempData, uniqClasses)){
			//	System.out.println("s5");

			/*	if(numComb >2){ 
				System.out.println("oooooooo");
				for(String className : uniqClasses){
					System.out.println("klasa "+className);
					ArrayList<ArrayList<String>> temo = tempData.get(className);
					//if (temo!=null)
					for(ArrayList<String> kok : temo){
						if(kok==null){
							System.out.println("kok null");
							//continue;
						}
						for(String kk : kok){
							System.out.print(kk + " ");
						}
						System.out.println();
					}
				}
				}*/

			if(numComb == 6){
				int z = 2/0;
				System.out.println(z);
			}

			fillTempComb(tempCombinations, trainData);

			//	System.out.println("s6");
			///	
			for(String className : uniqClasses){

				//	System.out.println("s7");

				String maxComb = "";
				ArrayList<String> combinations = tempCombinations.get(className);
				//for (String s: combinations) System.out.println("COMB "+s);
				ArrayList<ArrayList<String>> tempArguments = tempData.get(className);

				if(tempArguments.size() > 0){
					ArrayList<String> combinationsToRemove = new ArrayList<String>();
					//	System.out.println("s8");

					maxComb = findMaxCombination(combinations, tempArguments);
					//		System.out.println("MAX COMB "+maxComb);

					while(maxComb != ""){
						ArrayList<ArrayList<String>> combRules = rules.get(className);
						//	for (ArrayList<String> row : combRules){
						//		System.out.println("Rule : ");
						//		for (String s: row)System.out.println(s);
						//		}
						ArrayList<String> comb = new ArrayList<String>();

						//		System.out.println("s9");

						String[] newComb = maxComb.split(";");

						for(String values : newComb){
							comb.add(values);
						}

						combRules.add(comb);
						combinationsToRemove.add(maxComb);

						combinations.remove(maxComb);

						maxComb = findMaxCombination(combinations, tempArguments);
					}

					for(String comb : combinationsToRemove){
						removeArguments(comb, tempArguments);
					}
				}
			}

			increaseCombinations(uniqClasses, numComb, tempData);
			numComb++;
		}

		//System.out.println();
		//fillUniqCombinations(trainData, numComb);

		//tempCombinations = cloneCombinations(trainData.getUniqClasses());
	}

	public ArrayList<String[]> testILA(Data testData){
		ArrayList<String[]> results = new ArrayList<String[]>();
		ArrayList<String[]> arguments = testData.getArguments();
		ArrayList<String> classes = testData.getClasses();
		ArrayList<String> uniqClasses = testData.getUniqClasses();
		//	System.out.println("tt");

		for(int i = 0; i < arguments.size(); i++){
			//	System.out.println(i);

			String[] arg = arguments.get(i);

			for(int j = 0; j < uniqClasses.size(); j++){
				String className = uniqClasses.get(j);
				ArrayList<ArrayList<String>> combinations = rules.get(className);
				boolean equals = true;

				for(ArrayList<String> comb : combinations){
					for(String value : comb){
						String[] tempVal = value.split(":");
						if(!tempVal[1].equals(arg[Integer.parseInt(tempVal[0])])){
							equals = false;
						}
					}

					if(equals){
						String[] res = {classes.get(i), className};
						results.add(res);
						break;
					}
				}

				if(equals){
					break;
				}

				if(j == uniqClasses.size() - 1){
					int temp = j > 0 ? -1 : 1;
					
					String[] res = {uniqClasses.get(j), uniqClasses.get(j + temp)};
					results.add(res);
				}
			}
		}

		return results;
	}

	private void increaseCombinations(ArrayList<String> classes , int numComb, HashMap<String, ArrayList<ArrayList<String>>> tempData){

	//	System.out.println("------oldCombination------");
	//	printCombinations(uniqCombinations, classes);
	//	System.out.println("------firstCombination------");
	//	printCombinations(uniqFirstCombinations, classes);

		for(String className : classes){
			ArrayList<String> oldCombinations = uniqCombinations.get(className);
			ArrayList<String> firstCombinations = uniqFirstCombinations.get(className);
			ArrayList<String> newCombinations = new ArrayList<String>();
			ArrayList<ArrayList<String>> tempArguments = tempData.get(className);
			uniqCombinations.replace(className, newCombinations);

			for(int i = 0; i < oldCombinations.size(); i++){
				String oldCom = oldCombinations.get(i);
				String[] oldValues = oldCom.split(";");

				for(int j = 0; j < firstCombinations.size(); j++){
					String firstCom = firstCombinations.get(j);
					String[] firstVal = firstCom.split(":");
					boolean contain = false;

					for(String oldValue : oldValues){
						String[] oldVal = oldValue.split(":");

						if(firstVal[0].equals(oldVal[0])){
							contain = true;
						}
					}

					if(!contain){
						boolean belong = false;
						String newCom = oldCom + ";" + firstCom;
						String[] tempComb = newCom.split(";");
						ArrayList<String> tempArray = new ArrayList<String>();

						for(int k = 0; k < tempComb.length; k++){
							tempArray.add(tempComb[k]);
						}

						Collections.sort(tempArray);

						newCom = tempArray.get(0);
						for(int k = 1; k < tempArray.size(); k++){
							newCom += ";" + tempArray.get(k);
						}

						for(ArrayList<String> tempArg : tempArguments){
							boolean tempBelong = true;

							for(String tComb : tempArray){
								String[] tVal = tComb.split(":");

								if(!tVal[1].equals(tempArg.get(Integer.parseInt(tVal[0])))){
									tempBelong = false;
								}
							}

							if(tempBelong){
								belong = true;
							}
						}

						if(!newCombinations.contains(newCom) && belong){
							newCombinations.add(newCom);
						}
					}
				}
			}
		}

	//	System.out.println("------newCombination------");
	//	printCombinations(uniqCombinations, classes);

	}

	private boolean checkIfFinished(HashMap<String, ArrayList<ArrayList<String>>> tempData, ArrayList<String> classes){
		boolean finished = true;
		System.out.println("KLASES "+classes.size());
		for(String className : classes){
			ArrayList<ArrayList<String>> tempArg = tempData.get(className);

			if(tempArg.size() > 0){

				for (int i=tempArg.size()-1;i>=0;i--){
					if (tempArg.get(i)==null){
						tempArg.remove(i);
						System.out.println("clasremove "+className);
					}
				}
				if(tempArg.size() > 0){
					return false;}
			}
		}

		return finished;
	}

	private void removeArguments(String comb, ArrayList<ArrayList<String>> arguments){
		String[] newComb = comb.split(";");
		ArrayList<ArrayList<String>> argumentsToRemove = new ArrayList<ArrayList<String>>();

		for(ArrayList<String> arg : arguments){
			boolean contains = true;
			for(String values : newComb){
				String[] val = values.split(":");
				int index = Integer.parseInt(val[0]);
				String value = val[1];

				if(!arg.get(index).equals(value)){
					contains = false;
				}
			}

			if(contains){
				argumentsToRemove.add(arg);
			}
		}

		//	System.out.println("::: "+arguments.size() +", "+argumentsToRemove.size());

		/*	for (ArrayList<String> arg: arguments){
			System.out.println("***");
			for (String a: arg) System.out.println("a: "+a);
		}

		for (ArrayList<String> arg: argumentsToRemove){
			System.out.println("rrr");
			for (String a: arg) System.out.println("r: "+a);
		} */

		for(ArrayList<String> arg : argumentsToRemove){
			arguments.remove(arg);
		}
		/*
		for (ArrayList<String> arg: arguments){
			System.out.println(">>>");
			for (String a: arg) System.out.println("a2: "+a);
		}
		System.out.println("after "+arguments.size());*/
	}

	private String findMaxCombination(ArrayList<String> combinations, ArrayList<ArrayList<String>> arguments){
		String maxComb = "";

		int maxCount = 0;
		for(String comb : combinations){
			int count = 0;
			/* wartoœci w kombinacjach rodzielone s¹ ;, a pierwsza litera z kombinacji odzielona : oznacza do którego argumentu nalezy */
			String[] valuesTable = comb.split(";");

			for(ArrayList<String> arg : arguments){
				boolean contains = true;

				for(String val : valuesTable){
					String[] valTab = val.split(":");
					int index = Integer.parseInt(valTab[0]);
					String value = valTab[1];

					if(!arg.get(index).equals(value)){
						contains = false;
					}
				}

				if(contains){
					count++;
				}
			}

			if(count > maxCount){
				maxComb = comb;
				maxCount = count;
			}
		}

		return maxComb;
	}

	private void fillTempComb(HashMap<String, ArrayList<String>> tempCombinations, Data data){
		ArrayList<String> classes = data.getUniqClasses();

		for(String className : classes){
			ArrayList<String> uniqArguments = uniqCombinations.get(className);
			ArrayList<String> tempArguments = new ArrayList<String>();
			tempCombinations.replace(className, tempArguments);

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

				for(String anotherArg : anotherArguments){
					if(anotherArg.equals(argument)){
						return true;
					}
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

	private void printCombinations(HashMap<String, ArrayList<String>> tempCombinations, ArrayList<String> classes){
		for(String className : classes){
			System.out.println("----" + className + "----");

			ArrayList<String> tempComb = tempCombinations.get(className);
			for(String comb : tempComb){
				System.out.println(comb);
			}
			System.out.println();
		}
	}
}
