package com.isolver.tools;

public class Record {
	
	
	public Record() {

	}
	public Record(String datatype, String content, int length) {
		super();
		this.datatype = datatype;
		this.content = content;
		this.length = length;
	}
	private String datatype;
    public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	private String content;
    private int length;
   
}
