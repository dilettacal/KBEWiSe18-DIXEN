package utils;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
//import org.apache.commons.cli.MissingArgumentException;
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
	private String zusatz = "de.htw.ai.kbe.";

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
			//TODO: Das kann org.apache.commons.cli.MissingArgumentException werfen!
			cmd = parser.parse(options, args);

			if (cmd.hasOption("c")) {
				String className = zusatz + cmd.getOptionValue("c");
				System.out.println(className);
				returnValues[0] = className;

			} else {
				System.out.println(
						"Fehlerhafte Eingabe! Sie muessen einen Klassennamen angeben und ihn als Klasse kennzeichnen. (Z. Bsp.: -c className)");
				System.exit(1);
			}

			String out = "report.txt";
			
			if (cmd.hasOption("o")) {		
				try {
					out = cmd.getOptionValue("o"); 
					System.out.println("OUT: "+ out);
				}catch(Exception ex) {
					out = "report.txt";
				}	
			}
			returnValues[1] = out;

			File f = new File(out);
			f.createNewFile();
			
		} catch (ParseException e) {
			//e.printStackTrace();
			return null;
		} catch(Exception e) {
			//e.printStackTrace();
			return null;
		}
		return returnValues;
	}
}
