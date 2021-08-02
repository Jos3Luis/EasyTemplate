package pe.nom.jl.remplacer.service;

import java.util.ArrayList;
import java.util.List;
 

public class Analyzer {
	private List<String> params;
	private List<String> functions; 
	private String origin;

	public Analyzer() {
		functions=new ArrayList<String>();
		functions.add("@leftr");
		functions.add("@rightr");
		functions.add("@upper");
		functions.add("@lower");
		functions.add("@camelcase");
		functions.add("@substr");
		functions.add("@aemail");
		//functions.add("@pre");
		
		params= new ArrayList<>();
	}
	/*
	public void readString(String data) {
		for (int i = 0; i < functions.size(); i++) {
			StringTokenizer stk= new StringTokenizer(data,functions.get(i));
			while (stk.hasMoreTokens()) {
				String str=stk.nextToken();
				
			}
		}
		
	}*/
	public void printParams() {
		for (int i = 0; i < params.size(); i++) {
			System.out.println(params.get(i));
		}
	}
	public boolean validateToken(String data) {
		origin="";
		String aux="";
		char[] chars=data.toCharArray();
		int estado=0;
		for (int i = 0; i < chars.length; i++) {
			switch (estado) {
			case 0:{
				estado=-1;
				if(chars[i]=='(') {
					estado=1;
					origin+=chars[i];
				}
			}break; 
			case 1:{
				if (isIdentifierOrSigns(chars[i])) {
					aux+=chars[i];
					origin+=chars[i];
					estado=2;
				}else if (chars[i]==')') {
					origin+=chars[i];
					estado=4;
				}else {
					estado=-1;
				}
			}break;
			case 2:{
				if (chars[i]==',') {
					origin+=chars[i];
					params.add(aux);
					aux="";
					estado=3;
				}else if(chars[i]==')'){
					origin+=chars[i];
					params.add(aux);
					aux="";
					estado=4;
				}else if(isIdentifierOrSigns(chars[i])){
					origin+=chars[i];
					aux+=chars[i];
					estado=2;
				}else{
					estado=-1;
				}
			}break;
			case 3:{
				if (isIdentifierOrSigns(chars[i])) {
					origin+=chars[i];
					aux+=chars[i];
					estado=2;
				}else {
					estado=-1;
				}
			}break;
			default:{estado=-1;}break;
			}
			if (estado==-1 || estado==4) {
				break;
			}
		}
		if (estado==4) { 
			return true;
		}
		return false;
	}
	
	
	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public List<String> getFunctions() {
		return functions;
	}

	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	 
private boolean isIdentifierOrSigns(char ch) {
	if (Character.isJavaIdentifierPart(ch) || ch=='+' ||ch=='-' ||ch=='*'||ch=='/'||ch=='$'||ch=='=' || ch=='%' || ch=='#'|| ch=='<'|| ch=='>'|| ch==';' || ch=='@' || ch=='.'|| ch==':') {
		return true;
	}
	return false;
}
	 

}
