package runMeRunner;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

//package runMeRunner;

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
				//System.out.println("Argument is: " + args[1]);
				String argument = cmd.getOptionValue("c");
				System.out.println(argument);

			} else {
				System.out.println(
						"Fehlerhafte Eingabe! Sie müssen einen Klassennamen angeben und ihn als Klasse kennzeichnen. (Z. Bsp.: -c className)");
				System.exit(1);
			}

			if (cmd.hasOption("o")) {
				//System.out.println(args[3]);
				String argument2 = cmd.getOptionValue("o");
				System.out.println(argument2);

				// prüfen ob 4 Argumente mitgegeben wurden
				// wenn nur 3 Argumente -> -o ___ -> Standardnamen für Reportfile

				String filePathString = "../resourses";
				File f = new File(filePathString);
				if (f.exists() && !f.isDirectory()) {

				}

			} else {

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
