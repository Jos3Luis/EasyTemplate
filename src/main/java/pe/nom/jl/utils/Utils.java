package pe.nom.jl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import pe.nom.jl.constants.Constants;

public class Utils {
	public String getPre(String pre, String separator) {
		if (StringUtils.isEmpty(pre)) {
			return Constants.APP;
		}
		String[] pres = pre.split(separator);
		if (null != pres && pres.length > 1) {
			return StringUtils.capitalize(pres[pres.length - 2].toLowerCase())
					+ StringUtils.capitalize(pres[pres.length - 1].toLowerCase());
		}
		return Constants.APP;
	}

	public static List<String> listFilesUsingFileWalk(String dir) {
		try (Stream<Path> walk = Files.walk(Paths.get(dir))) {

			List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());

			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public static List<String> listFilesUsingFileWalkRelative(String dir) {
		Path start = Paths.get(dir);
		try (Stream<Path> walk = Files.walk(start)) {
			List<String> result = walk.filter(path -> Files.isRegularFile(path))
					.map(x -> start.relativize(x).toString()).collect(Collectors.toList());

			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public static void main(String[] args) {
		int count = 0;
		for (String string : Utils.listFilesUsingFileWalk("C:\\certificacion")) {
			System.out.println(string);
			count++;
		}
		System.out.println("------------------------------->" + count);

		count = 0;
		for (String string : Utils.listFilesUsingFileWalkRelative("C:\\certificacion")) {
			System.out.println(string);
			count++;
		}
	}

	public static String readFile(String filePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
			return sb.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	public static boolean saveFile(String fileTarget, String fileContent, boolean isCanMakeDir) {
		try {
			if (isCanMakeDir) {
				File f = new File(fileTarget);
				new File(f.getParent()).mkdirs();
			}
			PrintWriter pw = new PrintWriter(fileTarget);
			pw.print(fileContent);
			pw.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
