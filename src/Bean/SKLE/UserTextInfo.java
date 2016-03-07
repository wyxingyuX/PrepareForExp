package Bean.SKLE;

import java.util.ArrayList;
import java.util.List;

public class UserTextInfo {
	private List<String> tagList;
	
	
	public List<String> getTagList()
	{
		return this.tagList;
	}
	public void addTag(String tag)
	{
		if(tagList==null)
		{
			tagList=new ArrayList<String>();
		}
		if(tag==null||tag.equals(""))
		{
			return ;
		}
		tagList.add(tag);
	}
	public void addTags(String tagsLine,String seperater)
	{
		String[] tags=tagsLine.split(seperater);
		for(String tag:tags)
		{
			this.addTag(tag);
		}
	}
	public int getTagsSize()
	{
		if(tagList==null)
		{
			return -1;
		}
		return tagList.size();
	}
}
