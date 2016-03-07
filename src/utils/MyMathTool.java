package utils;

import Jama.Matrix;

public class MyMathTool {

	public static Matrix vec2Matrix(double[] vec)
	{
		Matrix mat=new Matrix(vec,1);
		return mat;
	}

	public static double[] elmentWiseMutliplicationForVec(double[] friAvgVec,double[] tVec)
	{
		int maxSize=friAvgVec.length>tVec.length?friAvgVec.length:tVec.length;
		double[] result=new double[maxSize];
		int minSize=friAvgVec.length<tVec.length?friAvgVec.length:tVec.length;
		for(int i=0;i<minSize;++i)
		{
			result[i]=friAvgVec[i]*tVec[i];
		}
		return result;
	}

	public static double  dotMultiplicationForVec(double[] vec1,double[] vec2)
	{   double result=0;
	int minSize=vec1.length<vec2.length?vec1.length:vec2.length;
	for(int i=0;i<minSize;++i)
	{
		result+=vec1[i]*vec2[i];
	}
	return result;
	}
	public static double[] numMultiplicationVec(double num,double[] vec)
	{
		double[] result=new double[vec.length];
		for(int i=0;i<vec.length;++i)
		{
			result[i]=num*vec[i];
		}
		return result;

	}

	public static double computeCosSim(double[] vec1,double[] vec2)
	{
		return MyMathTool.dotMultiplicationForVec(vec1, vec2)/(MyMathTool.vecLength(vec1)*MyMathTool.vecLength(vec2));  
	}
	public static double vecLength(double[] vec)
	{
		double len=0;
		for(int i=0;i<vec.length;++i)
		{
			len+=vec[i]*vec[i];
		}
		len=Math.sqrt(len);
		return len;
	}


}
