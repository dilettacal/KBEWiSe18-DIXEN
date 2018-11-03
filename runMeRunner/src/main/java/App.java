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
		try {
			AnnotationUtil.analyzeClass(arguments[0]);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
