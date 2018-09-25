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

		converter(new File("C:\\temp_dir\\rule.txt"),new File("C:\\\\temp_dir\\\\test_data.txt"),new File("C:\\temp_dir\\res.csv"));
	}
	/**
	 * Extract the content from a flat file based on the rule and the write it to a csv file. 
	 * <p>
	 * This method read the rules of the flat file from a configuration file and then based on 
	 * the rule extract all the content from to the flat file and write it into a csv file. 
	 * 
	 * @param      rule the file that contain the rules we need read the information from the flat file
	 * @param      source The flat file that we are going to read from
	 * @param      tar The csv file that we are going to write to
	 * @exception  IOException  If an I/O error occurs
	 */		
	public static void converter(File rule,File source,File tar) throws IOException {
		List<Record> rules =readRuleFromFile(rule);
		ITextFileContent body=extractRawContent(rules,source);
		toCsv(body,tar);
	}

	/**
	 * Write the extracted content to a csv file. 
	 * <p>
	 * This method write the flat file content that extracted to the 
	 * ITextFileContent and write it to the csv file. 
	 * ITextFileContent can be extracted from extractRawContent method. 
	 * @param      content  the extracted content that contain all the information from the flat file
	 * @param      csv The csv file that we are going to write to
	 * @exception  IOException  If an I/O error occurs
	 */	
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
	/**
	 * read the rules from a text file
	 * <p>
	 * This method read the predefined rules from the text file,
	 * and store the rules in the form of List<Record>
	 * @param       file  the file that contains the rules
	 * @return      the list of rules in the form of List<Record> we read from the file
	 * @exception   IOException  If an I/O error occurs
	 */		
	
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
	/**
	 * Extract the flat file content based on the rules
	 * <p>
	 * This method extract the flat file content based on the rule that we passed in.
	 * The result will save in a ITextFileContent instance and return 
	 * ITextFileContent can be extracted from extractRawContent method. 
	 * @param      rule  the rules that we will use to extract content from the flat file
	 * @param      file The flat file that we are going read from
	 * @exception  IOException  If an I/O error occurs
	 */
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
