package com.geocoder.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.json.simple.JSONArray;

import net.sourceforge.jgeocoder.AddressComponent;
import net.sourceforge.jgeocoder.us.AddressParser;
import net.sourceforge.jgeocoder.us.AddressStandardizer;

public class Test {
	static int notParseCount = 0;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();

/*		String address[] = {
				"123 FISH AND GAME rd philadelphia pa 12345",
				"KING ST & WARREN AVENUE, MALVERN, PA, 19355",
				"18th and south board, philadelphia pa 13245",
				"123 Avenue of art, philadelphia pa 12345",
				"PO box 123, abc city, ca 24656",
				"123 Route 29 South, new jersey, 12323",
				"Hessey Road, Mount Juliet, TN 37122",
		};
		for(String add : address) {
			System.out.println(add);
			Map<AddressComponent, String> addressComponents = AddressParser.parseAddress(add);
			Map<AddressComponent, String> normalizedAddr  = AddressStandardizer.normalizeParsedAddress(addressComponents);
			System.out.println(addressComponents);
			System.out.println(normalizedAddr);
			System.out.println(getSACInputAddressAsJson(normalizedAddr));
		}
*/		
//		Map<AddressComponent, String> addressComponents = AddressParser.parseAddress("123 FISH AND GAME rd philadelphia pa 12345");
		
//		writeBulkTest();
		
		
		singleTest("690 Zion Hill Rd, Spartanburg, SC 29307");
//		singleTest("0 zion hill rd, spartanburg, sc 29307");
//		singleTest("zion hill road, spartanburg, sc 29307");
//		singleTest("s875 e & e 400 s, zionsville, in 46077");
/*		singleTest("anderson ridge rd, five forks, sc 29651");
		singleTest("five points, raeford, nc 28376");
		singleTest("hwy 101, greer, sc 29651");
		singleTest("taylor rd & hwy 92, seffner, fl 33584");
//		singleTest("taylor rd, seffner, fl 33584");
		singleTest("spur 149, magnolia, tx 77354");
		singleTest("1 pioneer trail, cedar springs, mi 49319");
		singleTest("co road 123, mckinney, tx 75071");
		singleTest("arizona blvd, three rivers, mi 49093");
		singleTest("151 cedar springs loop n, kalama, wa 98625");
		singleTest("1501 ne park st, grimes, ia 50111");
*/		System.out.println("Total time to exec in (ms) ::"+(System.currentTimeMillis()- start));

	}
	
	static void singleTest(String address) {
		System.out.println("=>"+address);
		Map<AddressComponent, String> addressComponents = AddressParser.parseAddress(address);
		System.out.println(addressComponents);

		Map<AddressComponent, String> normalizedAddr  = AddressStandardizer.normalizeParsedAddress(addressComponents);
		System.out.println(normalizedAddr);
		System.out.println(">>>"+getSACInputAddressAsJson(normalizedAddr));
		System.out.println("--------------------------");
	}
	
	static void writeBulkTest() throws IOException {
		String ipFile = 
//				"/home/shatam-25/Downloads/Builders/Apr_2023_Address.txt"; 
//		"/home/shatam-25/Downloads/Builders/Dec_21_Apr_22_Address.txt";
		"Dec_21_Apr_22_Address.txt";

		String opFile = 
//				"/home/shatam-25/Downloads/Builders/Apr_2023_Address_SAC.tsv"; 
//				"/home/shatam-25/Downloads/Builders/Dec_21_Apr_22_Address_SAC.tsv";
		"Dec_21_Apr_22_Address_SAC.tsv";

		FileWriter fw = new FileWriter(opFile);
		BufferedReader br = new BufferedReader(new FileReader(ipFile));
		String line = null;
		int totalCount = 0;
		while((line = br.readLine()) != null) {
			fw.write(line+"\t"+convertSingleAddressToSacInputJson(line)+"\n");
			totalCount++;
		}
		
		br.close();
		fw.flush();
		fw.close();
		System.out.println("Total Address Count ::"+totalCount);
		System.out.println("Not Parse Count ::"+notParseCount);

	}
	
	static String convertSingleAddressToSacInputJson(String address) {
		Map<AddressComponent, String> normalizedAddr = null;
	
		try {
			normalizedAddr = AddressStandardizer.normalizeParsedAddress(AddressParser.parseAddress(address));
		}catch(NullPointerException e) {
			System.out.println("Address is not parse :"+address);
			notParseCount++;
			return null;
		}
		return getSACInputAddressAsJson(normalizedAddr);
	}
	
	@SuppressWarnings("unchecked")
	static String getSACInputAddressAsJson(Map<AddressComponent, String> m) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(m.get(AddressComponent.NUMBER) != null ? m.get(AddressComponent.NUMBER)+" ": "");
		sb.append(m.get(AddressComponent.PREDIR) != null ? m.get(AddressComponent.PREDIR) +" " : "");
		sb.append(m.get(AddressComponent.STREET) != null ? m.get(AddressComponent.STREET) +" " : "");
		sb.append(m.get(AddressComponent.TYPE) != null ? m.get(AddressComponent.TYPE) +" " : "");
		sb.append(m.get(AddressComponent.POSTDIR) != null ? m.get(AddressComponent.POSTDIR) +" " : "");
		sb.append(m.get(AddressComponent.LINE2) != null ? m.get(AddressComponent.LINE2) +" " : "");
		sb.append(m.get(AddressComponent.PREDIR2) != null ? "& "+m.get(AddressComponent.PREDIR2) +" " : "");
		sb.append(m.get(AddressComponent.STREET2) != null ? (m.get(AddressComponent.PREDIR2) == null ? "& ":"")+m.get(AddressComponent.STREET2)+" " : "");
		sb.append(m.get(AddressComponent.TYPE2) != null ? m.get(AddressComponent.TYPE2) +" " : "");
		sb.append(m.get(AddressComponent.POSTDIR2) != null ? m.get(AddressComponent.POSTDIR2) +" " : "");
		
		JSONArray address = new JSONArray();
		address.add(0, "1");
		address.add(1, sb.toString().trim()); //address1
		address.add(2, ""); //address2
		address.add(3, m.get(AddressComponent.CITY) != null ? m.get(AddressComponent.CITY): (m.get(AddressComponent.COUNTY) != null ? m.get(AddressComponent.COUNTY): "")); //city
		address.add(4, m.get(AddressComponent.STATE) != null ? m.get(AddressComponent.STATE): ""); //state
		address.add(5, m.get(AddressComponent.ZIP) != null ? m.get(AddressComponent.ZIP): ""); //zip
		
		JSONArray sacAddress = new JSONArray();
		sacAddress.add(address);
		
		return sacAddress.toJSONString();
	}

}
