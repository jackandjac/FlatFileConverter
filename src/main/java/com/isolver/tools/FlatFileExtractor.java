package com.isolver.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import java.util.ArrayList;
import java.util.List;

public class FlatFileExtractor {
	// String,35;Date,25;
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
		}
		return rule;
	}

	public static ITextFileContent extractRawContent(List<Record> rule, File file) throws IOException {
		ITextFileContent body = new ITextFileContent();

		List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
		String header = lines.get(0);

		for (int i = 1; i < lines.size(); i++) {

		}
		return null;
	}


	public static List<Record> parseLineToRecord(String line, List<Record> rules) {
		ArrayList<Record> list=new ArrayList<>();
		for(int i=0;i<rules.size();i++) {
			list.add(null);
		}
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
		    }
		    
		    String res =line.substring(curidx, curidx + len);
            curidx =curidx +len;
            list.add(new Record(rule.getDatatype(),res,rule.getLength()) );
  			
		}
		
		return list;
	}

}
