/*
 * Encrypt the setting file
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.BufferedReader;

public class Encode {
	public static void main(String[] args) throws Exception{

		FileReader fr = null;
		FileWriter fw = null;
		BufferedReader br = null;

		if(args.length != 1){
			System.out.println("Error: too few arguments,need a filepath to be encrypted");
			return ;
		}

		try{

			br = new BufferedReader(new FileReader(args[0]));

			String string = "";
			String readStr = null;
			
			while((readStr = br.readLine()) != null){
				string +=readStr + "\n";
			}
			string = string.substring(0,string.lastIndexOf("\n"));

			/* encode */
			String strEncode = Encrypt.encode(string.getBytes());
			

			// save the encryption file 
			File file = new File(args[0]);
			String strFileName = file.getName();
			String settingFileName = strFileName.substring(0,strFileName.indexOf('.')) + ".dat";

			fw = new FileWriter(new File(file.getParent(),settingFileName));
			String encodedStr = upToLow(strEncode);
			fw.write(encodedStr);
			fw.close();
			
			System.out.println("Encrypt finished");
			System.out.println(encodedStr);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String upToLow(String string){
		char[] array = string.toCharArray();
		for (int i = 0 ; i < array.length ; ++i) {
			
			char c = array[i];

			if(c >= 'A' && c <= 'Z'){
				c += 32;
			}
			else if (c >= 'a' && c <= 'z') {
				c -= 32;
			}
			array[i] = c;
		}
		return new String(array);
	}
}
