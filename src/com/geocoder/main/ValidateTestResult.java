package com.geocoder.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ValidateTestResult {

	public static void main(String[] args) throws IOException, ParseException {

		String ipFile = "Dec_21_Apr_22_Address_SAC.tsv";
//				"/home/shatam-25/Downloads/Builders/Dec_21_Apr_22_Address_SAC.tsv"; 
//				"/home/shatam-25/Downloads/Builders/Apr_2023_Address_SAC.tsv"; 
		
		BufferedReader br = new BufferedReader(new FileReader(ipFile));
		String line = null;
		int total = 0;
		int totalCorrect = 0;
		int totalWrongResult = 0;
		int notFound = 0;
		
		JSONParser parser = new JSONParser();
		while((line = br.readLine()) != null) {
			total++;
			
			String json = line.split("\t")[1];
			if(json.equals("null")) {
				notFound++;
				continue;
			}
			json = json.replace("[[", "[").replace("]]", "]");
			
			JSONArray array = (JSONArray) parser.parse(json);
//			System.out.println(array);
			if(array.get(4).toString().isEmpty()) {
				System.out.println(line);
				totalWrongResult++;
			}else {
				totalCorrect++;
			}
		}
		
		br.close();
		
		
		System.out.println("total ::"+ total);
		System.out.println("totalCorrect ::"+totalCorrect);
		System.out.println("totalWrongResult ::"+totalWrongResult);
		System.out.println("notFound ::"+notFound);
	}

}
