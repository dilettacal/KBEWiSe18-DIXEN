import de.htw.ai.kbe.runmerunner.AnnotationUtil;
import utils.Cli;

public class App {

	//private static final String PATH_TO_CLASS = "de.htw.ai.kbe.";
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
