package pe.nom.jl.remplacer.service;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformJava {
	public static String parsePackageToDirectory(String str) {
		return str.replace(".", File.separator);
	}

	public static String getPackagePath(String content) {
		String regex = "package\\s+([a-zA_Z_][\\.\\w]*);";
		Pattern patron = Pattern.compile(regex);
		Matcher emparejador = patron.matcher(content);
		if (emparejador.find()) {
			if (emparejador.groupCount() > 0) {
				return emparejador.group(1).replace("package ", "").replace(".", File.separator).replace(";", "");
			}
		}
		return content;
	}

	public static String getFileNameJava(String content) {
		String regex = "(?<=\\n|\\A)(?:public\\s)?(class|interface|enum)\\s([^\\n\\s]*)";
		Pattern patron = Pattern.compile(regex);
		Matcher emparejador = patron.matcher(content);
		emparejador.find();
		if (emparejador.groupCount() > 0) {
			return emparejador.group(0).replace("public class ", "").replace("public interface ", "")
					.replace("public enum ", "");
		}

		return content;
	}

	public static boolean isFileJavaIsTest(String content) {
		if (null != content && content.contains("@Test")) {
			return true;
		}
		return false;
	}

}
