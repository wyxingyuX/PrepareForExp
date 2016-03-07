package other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.FileTool;

public class TempTool {

	public TempTool() {
		// TODO Auto-generated constructor stub
	}
	public static void god(String allId,List<String> haveAssignIds,List<String> newAssignIds) throws IOException
	{
		Set<String> haveAssignSet=new HashSet<String>();
		for(String f:haveAssignIds)
		{
			BufferedReader reader=FileTool.getBufferedReaderFromFile(f);
			String line="";
			while((line=reader.readLine())!=null)
			{
				haveAssignSet.add(line.trim());
			}
		}
		
		Set<String> newAssignSet=new HashSet<String>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(allId);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String id=line.trim();
			if(haveAssignSet.contains(id))
			{
				id="";
				continue;
			}
			newAssignSet.add(id);
		}
		
		PrintWriter[] writers=new PrintWriter[newAssignIds.size()];
		for(int i=0;i< newAssignIds.size();++i)
		{
			writers[i]=FileTool.getPrintWriterForFile(newAssignIds.get(i));
		}
		
		int count=0;
		int foldNum=newAssignIds.size();
		int mod=newAssignSet.size()/foldNum;
		for(String id:newAssignSet)
		{
			++count;
			int curFold=(int) Math.ceil(1.0*count/(1.0*mod));
			if(curFold>foldNum)
			{
				curFold=foldNum;
			}
			writers[curFold-1].write(id+"\r\n");
			
		}
		
		for(PrintWriter writer:writers)
		{
			writer.close();
		}
		
	}

}
