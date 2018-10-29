package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {

	private String[] args = null;
	private Options options = new Options();

	public Cli(String[] args) {
		this.args = args;
		options.addOption("c", "className", true, "show help.");
		options.addOption("o", "output", true, "Output if required");
		//Mindestens 2 Sachen: 1 Option + Argument: Die Klasse!
		//Oder 3: 1 Option (c) + Argument (className) + Option (o) mit leerem Argument
	}

	public void parse() {

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("c")) {
				String argument = cmd.getOptionValue("c");
				System.out.println(argument);

			} else {
				System.out.println(
						"Fehlerhafte Eingabe! Sie muessen einen Klassennamen angeben und ihn als Klasse kennzeichnen. (Z. Bsp.: -c className)");
				System.exit(1);
			}

			String c, o;
			
			if (cmd.hasOption("o")) {		
				try {
					o = cmd.getOptionValue("o"); 
					//MissingArgumentException
				}catch(Exception ex) {
					o = "report.txt";
				}	
			}
			
			//wird nicht angelegt, warum auch immer
			File f = new File("report.txt");
			
			System.out.println(f.exists());
			if (f.exists() && !f.isDirectory()) {
				System.out.println("Path: " + f.getPath());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
