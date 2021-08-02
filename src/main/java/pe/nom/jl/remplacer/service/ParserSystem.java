package pe.nom.jl.remplacer.service;

import pe.nom.jl.constants.Constants;

public class ParserSystem extends Parser {
	public String replace(String data) {
		return data.replace("@pre()", Constants.APP);
	}
	
}
