package pe.nom.jl.remplacer.service;

import java.util.Map;

public class ParserString extends Parser{ 

	public String replace(String data) {
		if (null != keyValues) {
			for (Map.Entry<String, String> entry : keyValues.entrySet()) {
				data = data.replace(entry.getKey(), entry.getValue());
			}
		}
		return data;
	}

}
