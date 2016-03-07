package utils;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class FileTool {
	public static BufferedReader getBufferedReaderFromFile(String filePath,String encoding) throws UnsupportedEncodingException, FileNotFoundException{
		return new BufferedReader(new InputStreamReader(new FileInputStream(filePath),encoding));
	}
	public static BufferedReader getBufferedReaderFromFile(String filePath) throws UnsupportedEncodingException, FileNotFoundException{
		return getBufferedReaderFromFile(filePath,"utf-8");
	}
	public static PrintWriter getPrintWriterForFile(String filePath,boolean isAppend,String encoding) throws UnsupportedEncodingException, FileNotFoundException{
		guaranteeFileDirExist(filePath);
		return new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,isAppend), encoding)));
	}
	public static PrintWriter getPrintWriterForFile(String filePath,String encoding) throws UnsupportedEncodingException, FileNotFoundException{
		return getPrintWriterForFile(filePath,false,encoding);
	}
	public static PrintWriter getPrintWriterForFile(String filePath,boolean isAppend) throws UnsupportedEncodingException, FileNotFoundException{
		return getPrintWriterForFile(filePath,isAppend,"utf-8");
	}
	public static PrintWriter getPrintWriterForFile(String filePath) throws UnsupportedEncodingException, FileNotFoundException{
		return getPrintWriterForFile(filePath,false,"utf-8");
	}
	public static void guaranteeDirExist(String dirPath){
		File dir=new File(dirPath);
		if(!dir.exists()) dir.mkdirs();
	}
	public static void guaranteeFileDirExist(String file){
		File f=new File(file);
		guaranteeDirExist(f.getParent());
	}
	public static void glanceFileContent(String filePath,int start,int end) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(filePath));
		String curLine="";
		int lineNum=0;
		while((curLine=br.readLine())!=null){
			++lineNum;
			if(lineNum>=start&&lineNum<=end) System.out.println(curLine);
			if(lineNum>end) break;
		}
	}
	public static int getFileLineNum(String filePath) throws Exception{
		BufferedReader br=new BufferedReader(new FileReader(filePath));

		String curLine="";
		int lineNum=0;
		while((curLine=br.readLine())!=null){
			++lineNum;
		}
		return lineNum;
	}

	public static String getParentPath(String filePath){
		return getParentPath(new File(filePath));
	}
	public static String getParentPath(File file){
		return file.getParent();
	}
	public static String getFileName(String filePath){
		File f=new File(filePath);
		return f.getName();
	}
	public static String getFileFomatType(String filePath){
		File f=new File(filePath);
		return f.getName().split("\\.")[1];
	}
	public static String getPureFileName(String filePath){
		File f=new File(filePath);
		return f.getName().split("\\.")[0];
	}
	public static String getPureName(String path){
		File f=new File(path);
		if(f.isDirectory()) return f.getName();
		else return getPureFileName(path);
	}
	private static String[] getDirNodes(String file){
		File f=new File(file);
		String dirPath="";
		if(f.isDirectory()){
			dirPath=f.getAbsolutePath();
		}else{
			dirPath=f.getParent();
		}
		String[] dirNodes=dirPath.split("\\\\");
		return dirNodes;
	}
	private static List<String> getDirNodesList(String file){
		LinkedList<String> nodesList=new LinkedList<String>();
		String[] nodes=getDirNodes(file);
		for(String n:nodes) nodesList.add(n);
		return nodesList;
	}
	public static String backReplaceDirNode(String file,String replaceNode,String newNode){
		String[] nodes=getDirNodes(file);
		for(int i=nodes.length-1;i>=0;--i){
			if(nodes[i].equals(replaceNode)) {
				nodes[i]=newNode;
				break;
			}
		}
		StringBuilder stb=new StringBuilder();
		for(String s:nodes){
			stb.append(s+"\\");
		}
		return stb.toString();
	}
	public static String forwardReplaceDirNode(String file,String replaceNode,String newNode){
		String[] nodes=getDirNodes(file);
		for(int i=0;i<nodes.length;++i){
			if(nodes[i].equals(replaceNode)) {
				nodes[i]=newNode;
				break;
			}
		}
		StringBuilder stb=new StringBuilder();
		for(String s:nodes){
			stb.append(s+"\\\\");
		}
		return stb.toString();
	}
	public static void copy(String srcFile,int startLineNum,int endLineNum,String destFile) throws IOException{
		copy(new File(srcFile),startLineNum,endLineNum,new File(destFile));
	}

	public static void copy(File srcFile,int startLineNum,int endLineNum,File destFile) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(srcFile.getAbsolutePath());
		PrintWriter writer=FileTool.getPrintWriterForFile(destFile.getAbsolutePath());
		int lineNum=1;
		String line="";
		while((line=reader.readLine())!=null){
			if(lineNum>=startLineNum){
				if(lineNum>endLineNum){break;} 
				writer.write(line);
				writer.write("\r\n");
			}
			++lineNum;
		}
		writer.close();
	}

	public static void copy(String srcFile,String destFile) throws IOException, Exception{
		copy(srcFile,1,FileTool.getFileLineNum(srcFile),destFile);
	}
	public static void appendFileA2FileB(String a,String b) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(a);
		PrintWriter writer=FileTool.getPrintWriterForFile(b, true);
		String line="";
		while((line=reader.readLine())!=null)
		{
			writer.append(line+"\r\n");
		}
		writer.close();
	}

	public static boolean remove(String file)
	{
		File f=new File(file);
		if(file!=null&&!file.equals("")&&f.exists())
		{
			System.out.println("delete file "+file);
			return f.delete();
		}
		return false;
	}

	public static void cutFile2Slices(String filePath,int sliceNum,String destDir) throws Exception
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(filePath);
		int totalLineNum=FileTool.getFileLineNum(filePath);
		String line="";
		int lineNum=0;
		int mod=totalLineNum/sliceNum;
		int curFSlice=0;

		PrintWriter[] writers=new PrintWriter[sliceNum];
		String fname=FileTool.getPureFileName(filePath);
		for(int i=0;i<sliceNum;++i)
		{
			writers[i]=FileTool.getPrintWriterForFile(destDir+fname+"_slice"+(i+1)+".txt");
		}

		Random rnd=new Random(100);
		while((line=reader.readLine())!=null)
		{
			++lineNum;
			curFSlice=(int) Math.ceil(1.0*lineNum/(mod*1.0));
			if(curFSlice>sliceNum)
			{
				curFSlice=rnd.nextInt(sliceNum+1)+1;
			}
			writers[curFSlice-1].write(line+"\r\n");
		}

		for(PrintWriter writer:writers)
		{
			writer.close();
		}
	}


	public static void cutFile2Slices(String filePath,int sliceNum) throws Exception
	{
		cutFile2Slices(filePath,sliceNum,FileTool.getParentPath(filePath)+"\\");
	}


	//	public static void main(String[] args){
	//		TestTool.println(backReplaceDirNode("F:\\ExpData\\DataIntegate\\CooperateTrain\\","ExpData","wy"));
	//	}

}
