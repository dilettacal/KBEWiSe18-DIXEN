import de.htw.ai.kbe.runmerunner.AnnotationUtil;
import utils.Cli;

/**
 * Main class to the project
 * @author dixen
 *
 */
public class App {

	public static void main(String[] args) {
		String[] arguments = new Cli(args).parse();
		if (arguments == null) {
			System.out.println("Ein Fehler ist aufgetreten. Ueberpruefen Sie bitte die Inputeingabe oder die zu parsende Klasse.");
		}
		else {
			try {
				AnnotationUtil.analyzeClass(arguments[0], arguments[1]);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
	}

}
