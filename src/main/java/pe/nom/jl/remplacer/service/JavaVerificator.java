package pe.nom.jl.remplacer.service;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pe.nom.jl.constants.Constants;

public class JavaVerificator {
	public static boolean isFileJava(String uri) {
		String extension = uri.substring(uri.lastIndexOf("."));
		if (null != extension && extension.equals(Constants.POINT + Constants.JAVA)) {
			return true;
		}
		return false;
	}
	public String getPackagePath(String content){
            String regex =  "package\\s+([\\w\\.]+);";
            Pattern patron = Pattern.compile(regex);
            Matcher emparejador = patron.matcher(content);
            emparejador.find();
            if (emparejador.groupCount()>0){
                return emparejador.group(0).replace("package ","").replace(".",File.separator).replace(";","");
            }
        return content;
    }
	public static boolean isFilePhp(String uri) {
		String extension = uri.substring(uri.lastIndexOf("."));
		if (null != extension && extension.equals(Constants.POINT + Constants.PHP)) {
			return true;
		}
		return false;
	}

}
