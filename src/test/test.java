package test;

import java.io.IOException;
import java.util.regex.Pattern;

import Bean.SKLE.UserMemDB;
import utils.MyMathTool;

public class test {

	public static void main(String[] args) throws IOException {
		double[] v1={1,2};
		double[] v2={3,3};
		double sim=MyMathTool.computeCosSim(v1, v2);
		System.out.println(MyMathTool.vecLength(v2));
	
	}

}
