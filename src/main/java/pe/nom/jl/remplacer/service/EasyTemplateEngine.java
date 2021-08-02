package pe.nom.jl.remplacer.service;

import java.io.File;
import java.util.HashMap;

import pe.nom.jl.config.Config;
import pe.nom.jl.constants.Constants;
import pe.nom.jl.utils.Utils;

public class EasyTemplateEngine {
	private Config config;
	private HashMap<String, String> keyValues;
	private ParserString replaceString;
	private ParserFunction remplaceFunction;
	private ParserSystem replaceSystem;
	private Analyzer analyzer;

	public EasyTemplateEngine() {
		config = new Config();
		analyzer= new Analyzer();
		keyValues = new HashMap<String, String>();
		
		replaceString = new ParserString();
		replaceString.setKeyValues(keyValues);
		
		remplaceFunction = new ParserFunction();
		remplaceFunction.setFunctions(analyzer.getFunctions());
		
		replaceSystem= new ParserSystem();		
	}

	public void put(String key, String value) {
		keyValues.put(key, value);
	}

	/*public static void main(String[] args) {
		EasyTemplateEngine easy= new EasyTemplateEngine(); 
		Config config= new Config();
		config.setPathFolderTemplate("C:\\temp\\zip");
		config.setPathFolderGenerate("C:\\temp\\xyz");
		easy.setConfig(config);
		
		easy.put("{{MICRO_NAME}}", "leg-xp-service-get-client");
        easy.put("{{MICRO_METHOD}}", "POST");
        easy.put("{{MICRO_CONTEXT}}", "/abc/xvu");
        easy.put("{{MICRO_METHOD_CONTROLLER}}", "getClients");
        easy.put("{{MICRO_PACKAGE}}", "com.jl.xyz");
        easy.put("{{MICRO_REQUEST_BODY}}", "{'ab': 'sasas'}");
        easy.put("{{MICRO_RESPONSE_BODY}}", "{'cc': 'sasas'}");
        easy.put("{{MICRO_DESCRIPTION}}", "descripcion de prueba");
        easy.put("{{SERVICE_URL}}", "http://www.ffkfk.com/sss/ert/clientes");
        easy.put("{{SERVICE_REQUEST_BODY}}", "{'zzz': 'ssss'}");
        easy.put("{{SERVICE_RESPONSE_BODY}}","{'rrrr': 'hhhh'}");
        easy.put("{{SERVICE_METHOD}}", "POST");
        easy.put("{{AUTOR_NAME}}", "Jose Luis Quispe");
        easy.put("{{AUTOR_EMAIL}}", "winpcjose@gmail.com");
        easy.put("{{AUTOR_BUSINESS}}", "Everis"); 
		if (easy.run()) {
			System.out.println("Good");
		};
		 
	}*/

	public boolean run() {
		for (String strPath : Utils.listFilesUsingFileWalkRelative(config.getPathFolderTemplate())) {
			String data = Utils.readFile(config.getPathFolderTemplate()+File.separator+strPath);
			data=replaceSystem.replace(data);
			data=replaceString.replace(data);
			String url = "";
			if (JavaVerificator.isFileJava(strPath)) {
				url = config.getPathFolderGenerate() +File.separator+Constants.SRC_JAVA+ File.separator + TransformJava.parsePackageToDirectory(TransformJava.getPackagePath(data))+File.separator+TransformJava.getFileNameJava(data)+Constants.POINT+ Constants.JAVA;
				if(TransformJava.isFileJavaIsTest(data)){ 
					url=config.getPathFolderGenerate() +File.separator+Constants.SRC_TEST+File.separator + TransformJava.parsePackageToDirectory(TransformJava.getPackagePath(data))+File.separator+TransformJava.getFileNameJava(data)+Constants.POINT+ Constants.JAVA;
                }
			} else {
				url = config.getPathFolderGenerate() + File.separator + strPath;
			}
			// aplicar cada funcion 
			data=remplaceFunction.replace(data);
			Utils.saveFile(url, data, true);
		}
		return true;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
