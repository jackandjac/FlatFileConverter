package com.isolver.tools;

import java.util.ArrayList;
import java.util.List;

public class ITextFileContent {
	private List<String> header =new ArrayList<>();
	private List<List<Record>> content=new ArrayList<>();
	public List<String> getHeader() {
		return header;
	}
	public void setHeader(List<String> header) {
		this.header = header;
	}
	public List<List<Record>> getContent() {
		return content;
	}
	public void setContent(List<List<Record>> content) {
		this.content = content;
	}
	
	public void addNewLine(List<Record> line) {
		this.content.add(line);
	}


}
