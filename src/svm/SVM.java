package svm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import weka.classifiers.functions.LibSVM;
import weka.core.DenseInstance;
//import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * Weka LibSVM (WLSVM) combines the merits of the two tools. WLSVM can be viewed
 * as an implementation of the LibSVM running under Weka environment.
 * 
 * @author iceshirley
 * @version 1.0
 * 
 */
public class SVM implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String TrainingDateFile = "trainingdata/weather.nominal.arff";

	private LibSVM svm;

	private Instances inst = null;

	public SVM() {
		svm = new LibSVM();
	}

	public SVM(String[] ops) {
		svm = new LibSVM();
		setoption(ops);
	}

	public void evaluationClassifier(String[] option) {
		try {
			System.out.println(weka.classifiers.Evaluation.evaluateModel(
					this.svm, option));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String evaluate(String[] option){
		try {
			String r = weka.classifiers.Evaluation.evaluateModel(
					this.svm, option);
			return r;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public Instances getInstances() {
		return this.inst;
	}

	public boolean setoption(String[] option) {

		if (option.length == 0) {
			return false;
		}
		try {
			svm.setOptions(option);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 
	 * @param AttributeIndex
	 *            -classIndex number
	 * @return true if sucesses,otherwise false
	 */
	public boolean training(int AttributeIndex) {

		System.out.println("Start training.....");
		try {
			inst = new Instances(new FileReader(this.TrainingDateFile));
			if (AttributeIndex < 0 || AttributeIndex > inst.numAttributes()) {
				return false;
			}
			inst.setClassIndex(AttributeIndex);
			svm.buildClassifier(inst);
			System.out.println("build complete");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 
	 * @param ins
	 *            -the instance to be classified
	 * @return- -1 if take error,otherwise the predicted value
	 */
	public double predicted(String[] predictedInstance) {
		double predicted = 0;
		int attributeNum = inst.numAttributes();
		if (predictedInstance.length > attributeNum) {
			return -1;
		}
		Instance instance = new DenseInstance(attributeNum);
		for (int i = 0; i < predictedInstance.length; i++) {
			instance.setValue(inst.attribute(i), predictedInstance[i]);
		}
		instance.setDataset(inst);
		System.out.println("test sample:" + instance);
		try {
			predicted = svm.classifyInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return predicted;

	}

	/**
	 * save the trained classifier to Disk
	 * 
	 * @param classifier
	 *            -the classifier to be saved
	 * @param modelname
	 *            -file name
	 */
	public void saveModel(Object classifier, String modelname) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream("model/" + modelname));
			oos.writeObject(classifier);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * load the model from disk
	 * 
	 * @param file
	 *            -the model filename
	 * @return-the trained classifier
	 */
	public Object loadModel(String file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					file));
			Object classifier = ois.readObject();
			ois.close();
			return classifier;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String[] ops1 = { "-t", "trainingdata/traindata.arff",
				"-T", "testingdata/testdata.arff", "-v", "-o"
				// "-split-percentage","50",
				 };
		/*
		 * //smo option String[] ops2 = {
		 * "-t","trainingdata/weather.nominal.arff",
		 * 
		 * "-L", "0.0010", "-N", "1", "-V","10", //k-fold cross-validation "-K",
		 * "weka.classifiers.functions.supportVector.PolyKernel", "-W", "1" };
		 */

		SVM svm = new SVM();
		svm.evaluationClassifier(ops1);
//		if (svm.setoption(ops1)) {
//			svm.training(4);
//		}
		//String[] predictedData = { "sunny", "mild", "normal", "TRUE" };
		//svm = (SVM) svm.loadModel("model/svm.model");
		//double result = svm.predicted(predictedData);
		//System.out.println("good or not : "
			//	+ svm.getInstances().classAttribute().value((int) result));

	}
}