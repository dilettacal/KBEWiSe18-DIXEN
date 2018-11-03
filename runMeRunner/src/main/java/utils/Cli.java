package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
/**
 * Class for parsing argument from terminal (Uebungsblatt 1)
 * @author dixen
 *
 */
public class Cli {

	private String[] args = null;
	private Options options = new Options();

	public Cli(String[] args) {
		this.args = args;
		options.addOption("c", "className", true, "Class name");
		options.addOption("o", "output", true, "Report file name");
	}

	public String[] parse() {

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd = null;
		String[] returnValues = new String[2];
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("c")) {
				String className = cmd.getOptionValue("c");
				System.out.println(className);
				returnValues[0] = className;

			} else {
				System.out.println(
						"Fehlerhafte Eingabe! Sie muessen einen Klassennamen angeben und ihn als Klasse kennzeichnen. (Z. Bsp.: -c className)");
				System.exit(1);
			}

			String out = null;
			
			if (cmd.hasOption("o")) {		
				try {
					out = cmd.getOptionValue("o"); 
					//MissingArgumentException
				}catch(Exception ex) {
					out = "report.txt";
				}	
			}
			returnValues[1] = out;

			File f = new File("report.txt");
			
//			FileWriter ausgabestrom = null;
//			try {
//				ausgabestrom = new FileWriter(f);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            PrintWriter ausgabe = new PrintWriter(ausgabestrom);
//            ausgabe.println("bla bla bla schrei in die datei");
//            ausgabe.close();
			
			System.out.println(f.exists());
			if (f.exists() && !f.isDirectory()) {
				System.out.println("Path: " + f.getAbsolutePath());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValues;
	}
}
