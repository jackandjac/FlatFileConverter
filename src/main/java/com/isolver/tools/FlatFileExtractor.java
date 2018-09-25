package com.isolver.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlatFileExtractor {
	
	public static void main(String args[]) throws IOException {
		List<Record> rules =readRuleFromFile(new File("C:\\temp_dir\\rule.txt") );
		ITextFileContent body=extractRawContent(rules,new File("C:\\\\temp_dir\\\\test_data.txt"));
		File tar =new File("C:\\temp_dir\\res.csv");
		toCsv(body,tar);
//	    for(List<Record> line: body.getContent() ) {
//	    	for(Record rec:line) {
//	    		System.out.print("[");
//	    	    if(rec != null) {
//	    	    	System.out.print(rec.getContent());
//	    	    }else {
//	    	    	System.out.print("null");
//	    	    }
//	    		
//	    		System.out.print("]");
//	    	}
//	    	System.out.println();
//	    }
	}
	// String,35;Date,25;
	
	public static void toCsv(ITextFileContent content, File csv) throws IOException {
		StringBuffer body =new StringBuffer("");
		
		List<String> header = content.getHeader();
		for(String item:header) {
			body.append(item).append(",");
		}
		body.replace(body.lastIndexOf(","), body.length(), "");// removing the last ,
		body.append('\r').append('\n');
		
	    for(List<Record> line: content.getContent() ) {
	    	for(Record rec:line) {
	    	    if(rec != null) {
	    	    	body.append(rec.getContent()).append(",");
	    	    }else {
	    	    	body.append("null").append(",");
	    	    }                   	
	    	}
	    	body.replace(body.lastIndexOf(","), body.length(), "");// removing the last ,
	    	body.append('\r').append('\n');
	    }
	    
	    Files.asCharSink(csv, Charset.forName("utf-8")).write(body.toString());
	}
	public static List<Record> readRuleFromFile(File file) throws IOException {

		ArrayList<Record> rule = new ArrayList<>();

		List<String> lines = Files.readLines(file, Charset.forName("utf-8"));

		for (String line : lines) {
			Record re = new Record();
			int sep = line.indexOf(",");
			String type = line.substring(0, sep);
			int length = Integer.parseInt(line.substring(sep + 1));
			re.setDatatype(type);
			re.setLength(length);
			rule.add(re);
		}
		return rule;
	}

	public static ITextFileContent extractRawContent(List<Record> rule, File file) throws IOException {
		ITextFileContent body = new ITextFileContent();

		List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
		String header = lines.get(0);
	    List<String>  headers = Arrays.asList(header.split("\\s"));
	    body.setHeader(headers);
		for (int i = 1; i < lines.size(); i++) {
                   String line = lines.get(i);
                   List<Record>  row =parseLineToRecord(line,rule);
                   body.addNewLine(row);
		}
		return body;
	}


	public static List<Record> parseLineToRecord(String line, List<Record> rules) {
		ArrayList<Record> list=new ArrayList<>();

		if (line == null || line.isEmpty()) {
            return list;
		}
		boolean nullflag=false;
		int curidx =0;
		for(int i=0;i<rules.size();i++) {
			if(nullflag) {
				list.add(null);
				continue;
			}
			Record rule= rules.get(i);
			int len =rule.getLength();
		    if(len > line.length() - curidx) {
		    	nullflag=true;
		    	len =  line.length() - curidx;
		    }
		    
		    String res =line.substring(curidx, curidx + len);
            curidx =curidx +len;
            list.add(i,new Record(rule.getDatatype(),res,rule.getLength()) );
  			
		}
		
		return list;
	}

}
