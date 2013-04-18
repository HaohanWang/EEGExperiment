package experiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import svm.SVM;

public class Experiment {
	private int round;
	private String folder;
	private SVM svm = null;
	private int[] seed = { 0, 5, 0, 6, 0, 7, 0, 8, 0, 9, 1, 5, 1, 6, 1, 7, 1,
			8, 1, 9, 2, 5, 2, 6, 2, 7, 2, 8, 2, 9, 3, 5, 3, 6, 3, 7, 3, 8, 3,
			9, 4, 5, 4, 6, 4, 7, 4, 8, 4, 9 };
	private ArrayList<String> result = null;

	public Experiment(String folder) {
		this.round = 25;
		this.folder = folder;
		//shuffleArray(seed);
		result = new ArrayList<String>();
		svm = new SVM();
	}

	public void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i >= 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public void getArff() {
		String path = folder;
		String argu = "";
		argu = Integer.toString(seed[round * 2 - 1])
				+ Integer.toString(seed[round * 2 - 2]);
		ProcessBuilder builder = new ProcessBuilder("python",
				"src/python/getdata.py", path, argu);
		builder.redirectErrorStream(true);
		Process p = null;
		try {
			p = builder.start();
			// InputStream stdout = p.getInputStream();
			// BufferedReader reader = new BufferedReader(new InputStreamReader(
			// stdout));
			// String line = "";
			// while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (round > 0) {
			String[] ops = { "-t", "trainingdata/traindata.arff", "-T",
					"testingdata/testdata.arff", "-v", "-o" };
			getArff();
			result.add(svm.evaluate(ops));
			// svm.evaluationClassifier(ops);
			round -= 1;
		}
		FileWriter fstream;
		try {
			fstream = new FileWriter("result/out.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for (String a : result) {
				out.write(a);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getStatistics() {
		ProcessBuilder builder = new ProcessBuilder("python",
				"src/python/getstatistics.py");
		builder.redirectErrorStream(true);
		Process p = null;
		try {
			p = builder.start();
			InputStream stdout = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stdout));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		String path = "data/";
		Experiment experiment = new Experiment(path);
		experiment.run();
		experiment.getStatistics();
	}
}
