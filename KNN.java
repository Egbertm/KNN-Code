package suanfa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class KNN {
	
	public static ArrayList<ArrayList<String>> loadFile(String path){
		ArrayList<ArrayList<String>> lineList = new ArrayList<ArrayList<String>>();
		File file = new File(path);
		if (!file.exists()) {
			return null;
		} else {
			try {
				FileReader fin = new FileReader(file);
				BufferedReader br = new BufferedReader(fin);
				String line = "";
				line=br.readLine(); //定位到第二行。
				while ((line = br.readLine()) != null) {
					if ("".equals(line)){
						break;
					} else {
						ArrayList<String> lineWord = new ArrayList<String>();
						String[] words = line.split(","); 
						for (String st: words) {
							lineWord.add(st);
						}
						lineList.add(lineWord);
					}
				}
				br.close();
				fin.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineList;
	}
	public static ArrayList<String> getColum(ArrayList<ArrayList<String>> dataset,int index) {
		ArrayList<String> columList = new ArrayList<String>();
		for (int i=0;i<dataset.size();i++){
			columList.add(dataset.get(i).get(index));
		}
		Collections.sort(columList,new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				if ((Double.parseDouble(o1)-Double.parseDouble(o2))>0) {
					return 1;
				} else if ((Double.parseDouble(o1)-Double.parseDouble(o2))<0) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		return columList;
	}
	public static ArrayList<ArrayList<String>> getMaxMin(ArrayList<ArrayList<String>> dataset) {
		ArrayList<ArrayList<String>> maxMinList = new ArrayList<ArrayList<String>>();
		for (int i=0;i<dataset.get(0).size();i++) {
			ArrayList<String> maxMin = new ArrayList<String>();
			ArrayList<String> colum = getColum(dataset, i);
			maxMin.add(colum.get(0));
			maxMin.add(colum.get(dataset.size()-1));
			maxMinList.add(maxMin);
		}
		return maxMinList;
	}
	public static String calData(ArrayList<String> array,String str) {
		double max = Double.parseDouble(array.get(1));
		double min = Double.parseDouble(array.get(0));
		return String.valueOf((Double.parseDouble(str)-min)*1.0/(max-min));
	}
	public static ArrayList<ArrayList<String>> formatData(ArrayList<ArrayList<String>> dataset) {
		ArrayList<ArrayList<String>> maxMinList = getMaxMin(dataset);
		ArrayList<ArrayList<String>> autoData = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> array : dataset) {
			ArrayList<String> data = new ArrayList<String>();
			for (int k=0;k<array.size();k++) {
				data.add(calData(maxMinList.get(k), array.get(k)));
			}
			autoData.add(data);
		}
		return autoData;
	}
	public static void showArray(ArrayList<ArrayList<String>> arrayList) {
		for (ArrayList<String> array : arrayList){
			for(String str : array){
				System.out.print(str + ",");
			}
			System.out.print("\n");
		}
	}

	public static ArrayList<String> loadFeature(String path) {
		ArrayList<String> featureList = new ArrayList<String>();
		File file = new File(path);
		if (!file.exists()) {
			return null;
		} else {
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String[] features=br.readLine().split(",");
				for (String str : features) {
					featureList.add(str);
				}
				br.close();
				fr.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
		return featureList;
	}
	public static double caldistance(ArrayList<String> arr1,ArrayList<String> arr2) {
		double distance = 0.0;
		double dx = 0.0;
		for (int i=0;i<arr1.size()-1;i++) {
			dx = Double.parseDouble(arr1.get(i))-Double.parseDouble(arr2.get(i));
			distance = Math.sqrt(dx * dx);
		}
		return distance;
	}
	public static String getFeatureLabel(ArrayList<ArrayList<String>> dataset, ArrayList<String> item,int k) {
		HashMap<Integer,Double> resultMap = new HashMap<Integer,Double>();
		ArrayList<String> featureList = new ArrayList<String>();
		String label = null;
		int max=-1;
		int i=0;
		for (ArrayList<String> arr : dataset) {
//			System.out.println(caldistance(arr, item));
			resultMap.put(i++, caldistance(arr, item));
		}
		ArrayList<Map.Entry<Integer, Double>> list = new ArrayList<>();
		for (Map.Entry<Integer, Double> ent : resultMap.entrySet()){
			list.add(ent);
		}
		Collections.sort(list,new Comparator<Map.Entry<Integer, Double>>() {

			@Override
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				// TODO Auto-generated method stub
				if ((o1.getValue()-o2.getValue())>0) {
					
					return 1;
				} else if ((o1.getValue()-o2.getValue())<0) {
					return -1;
				} else {
					return 0;
				}
			}
		});
//		show(list);
		for (int j=0;j<k;j++) {
			featureList.add(dataset.get(list.get(j).getKey()).get(dataset.get(0).size()-1));
		}
//		System.out.println(featureList.size());
//		showArray1(featureList);
		Map<String,Integer> map = new HashMap<String,Integer>();
		for (String str : featureList) {
			if (map.containsKey(str)) {
				map.put(str, map.get(str)+1);
			} else {
				map.put(str, 1);
			}
		}
		Set<Entry<String, Integer>> entry = map.entrySet();
		for (Entry<String,Integer> ent: entry) {
			if (ent.getValue()>max) {
				max = ent.getValue();
				label = ent.getKey();
			}
		}
		return label;
	}
	public static void showArray1(ArrayList<String> arr) {
		for (String str : arr) {
			System.out.println(str+"！！");
		}
	}
	public static void show(ArrayList<Map.Entry<Integer, Double>> list) {
		for (Map.Entry<Integer, Double> en : list) {
			System.out.println("key"+en.getKey()+"value"+en.getValue());
		}
	}
	public static void classfy(ArrayList<ArrayList<String>> dataset ,ArrayList<ArrayList<String>> testData,int k) {
//		int ss = 0;
		int index = 0;
		int count = 0;
		for (ArrayList<String> testItem : testData) {
			String str=getFeatureLabel(dataset, testItem, k);
			System.out.println(str);
			if (testItem.get((testData.get(0).size()-1)).equals(str)) {
				System.out.println("testItem"+index+"分类正确");
				count ++;
			} else {
				System.out.println("testItem"+index+"分类错误");
			}
			index ++;
		}
		System.out.println("正确率为"+count*1.0/testData.size());
	}
//	public static double 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
//		map.put(1, 4.9);
//		map.put(2, 5.0);
//		map.put(4, 5.1);
//		map.put(3, 2.0);
//		ArrayList<Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>();
//		for (Map.Entry<Integer, Double> ent: map.entrySet()) {
//			list.add(ent);
//		}
//		System.out.println(map.get(2));
//		for (Map.Entry<Integer, Double> ent: list){
//			System.out.println(ent.getKey()+""+ent.getValue());
//		}
		String path = new String("D:/weather/fweathertrain10.txt.txt");
		String path2 = new String("D:/weather/fweathertest10.txt.txt");
		ArrayList<ArrayList<String>> dataSet = formatData(loadFile(path));
		ArrayList<ArrayList<String>> testSet = formatData(loadFile(path2));
		classfy(dataSet, testSet, 5);;
//		ArrayList<String> featureList = loadFeature(path);
//		showArray(loadFile(path));
	}

}
