import utils.AnnotationUtil;
import utils.Cli;

/**
 * Main class to the project
 * @author dixen
 *
 */
public class App {

	public static void main(String[] args) {
		String[] arguments = new Cli(args).parse();
		boolean programRun = false;
		if (arguments == null) {
			System.out.println("Ein Fehler ist aufgetreten. Ueberpruefen Sie bitte die Inputeingabe oder die zu parsende Klasse.");
		}
		else {
			try {
				programRun = AnnotationUtil.analyzeClass(arguments[0], arguments[1]);
			} catch (InstantiationException e) {
				System.out.println("Klasse ist nicht instanzierbar!");
				System.exit(1);
			} catch (IllegalAccessException e) {
				//passiert wenn Klasse nicht zugreifbar
				System.out.println("Klasse ist nicht zugreifbar! Ueberpruefen Sie Ihre Klasse!");
				System.exit(1);
			}
		}
		if(programRun) {
			System.out.println("Das Programm wurde korrekt ausgefuerht und wird jetzt beendet.");
		} else {
			//Passiert abhaengig von den Exceptions in analyzeClass, die false zurueckgeben
			System.out.println("Das Programm konnte nicht korrekt ausgefuehrt werden.");
		}
		
	}

}
