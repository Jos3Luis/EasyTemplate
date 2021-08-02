package pe.nom.jl.remplacer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import pe.nom.jl.constants.Constants;
import pe.nom.jl.error.ParseError;

public class ParserFunction2{
	private List<String> functions;   
	private List<ParseError> errors;
	public ParserFunction2() {
		
	} 
	/*
	public String replace(String data) {
		StringTokenizer stk= new StringTokenizer(data,"\n");
		int pos=1;
		while (stk.hasMoreTokens()) {
			String strk=stk.nextToken();
			for (int i = 0; i < functions.size(); i++) {
				int index = strk.indexOf(functions.get(i));
				while (index >= 0) {
					//int indice = strk.indexOf(functions.get(i));
					String strEvaluate=strk.substring(index+functions.get(i).length()-1);//-1 empieza desde el parentesis
					Analyzer analyzer= new Analyzer();
					if (analyzer.validateToken(strEvaluate)) {
						List<String> params=analyzer.getParams();
						applyFunctions(functions.get(i),String variable,Map<String, String> params);
						//ana.getParams();
					}
					
				    //System.out.println(index+functions.get(i).length()-1);
				    index = strk.indexOf(functions.get(i), index + 1);
				}
				pos++;
			}
			
			
		}
		return data;
	}
	/*public String replace(String data) {
		StringTokenizer stk= new StringTokenizer(data,"\n");
		int pos=1;
		while (stk.hasMoreTokens()) {
			String strk=stk.nextToken();
			for (int i = 0; i < functions.size(); i++) {
				int index = strk.indexOf(functions.get(i));
				int matchLength = functions.get(i).length();
				while (index >= 0) {
					String strToEvaluate=strk.substring(index,strk.length());
					Analyzer ana= new Analyzer();
					if(ana.validateToken(strToEvaluate)) {
						//Getparams
						ana.getParams();
					}else {
						ParseError error= new ParseError();
						error.setNroLine(pos);
						error.setFunction(functions.get(i));
						errors.add(error);
						//ana.getErrors().add(e)
					}
				    //System.out.println(strToEvaluate+"<---------");
				    index = strk.indexOf(functions.get(i), index + matchLength);
				}
			}
			pos++;
		}
		return data;
	} 
	
	/*public String replace(String data) { 
		for (String key: functions) {
            data=replaceWithFunction(key,data);
        }
		return data;
	}
	public String replaceWithFunction(String function,String data){
		
		return data;
	}
	 
	public String replaceWithFunction(String function,String line){ 
        Map<String,String> kv=new HashMap<>();
        int index = line.indexOf(function);
        
        while (index >= 0) {
            String strSub=line.substring(index+function.length());
            char[] letras=strSub.toCharArray();
            int i=0;
            List<String> lista= new ArrayList<>();
            while (letras[i]!='\0') {
                if(letras[i]==')') {
                    break;
                }
                if(letras[i]!='(') {
                    lista.add(String.valueOf(letras[i]));
                }
                i++;
            }
            //funcion: function
            if (lista.size()>0){
                String contentFunction=String.join("",lista);
                Map<String, String> params=extractHashMap(function, contentFunction); 
                String result=applyFunctions(function,String.join("",lista),params);
                kv.put(function+"("+contentFunction+")",result);
            }
            index = line.indexOf(function, index + 1);
        }

        Iterator iterator = kv.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
            line=line.replace(me2.getKey().toString(),me2.getValue().toString());
        }
        return line;
    } 
	private Map<String, String> extractHashMap(String function, String content){  
        if (functions.contains(function)){ 
            HashMap<String,String> params= new HashMap<>();
            if (functions.get(4).equals(function) ){//funcion
                int pos=content.indexOf(',');
                if(pos>0){
                    params.put(Constants.SEPARATOR,content.substring(0,pos));
                    return params;
                }
                return null;
            }else if(functions.get(5).equals(function)){ //funcion substr
                StringTokenizer stk= new StringTokenizer(content,",");
                while (stk.hasMoreTokens()){
                    params.put(Constants.SEPARATOR,stk.nextToken());
                    if(stk.hasMoreTokens()){
                        params.put(Constants.POSITION,stk.nextToken());
                    }else{
                        params.put(Constants.POSITION,"0");
                    }
                    return params;
                }
            }
        }
        return null;
    }
	private String applyFunctions(String function,String variable,Map<String, String> params){
        try { 
            HashMap<String, Callable<String>> functions= new HashMap<>();
            functions.put(this.functions.get(0),  () -> extractLeft(variable));
            functions.put(this.functions.get(1),  () -> extractRight(variable));
            functions.put(this.functions.get(2),  variable::toUpperCase);
            functions.put(this.functions.get(3),  () -> variable.toLowerCase());
            functions.put(this.functions.get(4),  () -> camelcase(variable,params));
            functions.put(this.functions.get(5),  () -> substringFrom(variable,params));
            functions.put(this.functions.get(6),  () -> extractAccountEmail(variable));
            return functions.get(function).call();
        }catch (Exception ex){
           ex.printStackTrace();
        }
        return variable;
    }
	
	private String extractLeft(String data){
        int pos=data.lastIndexOf("/");
        if (pos<0){
            return data;
        }else{
            if(pos>=0 && pos+1<data.length()) { 
                return data.substring(0,pos+1);
            }
            //Agrego el slash, pues es una url sin folder 
            return data.substring(0,pos)+"/";
        }
        //return data;
    }
	private String extractRight(String data) throws Exception {
        int pos=data.lastIndexOf("/");
        if(pos>0 && pos+1<data.length()) {
            return data.substring(pos+1);
        }
        return data;
    }
	private String extractAccountEmail(String data) {
        return data.replaceAll("@.+$", "");
    }
	private String camelcase(String data,Map<String, String> params) {
        if (null==params){
            return StringUtils.capitalize(data.toLowerCase());
        }
        //Removing first COMA
        int pos=data.indexOf(',');
        if(pos>=0){//posible error
            data=data.substring(pos+1);
        } 
        return CaseUtils.toCamelCase(data, true, CharUtils.toChar(params.get(Constants.SEPARATOR)));
    }
	
	private String substringFrom(String data,Map<String, String> params){
        try { 
            String pre=params.get(Constants.SEPARATOR)+","+params.get(Constants.POSITION)+",";
            
            data=StringUtils.replaceOnce(data,pre,StringUtils.EMPTY);
            //splitting
            StringTokenizer stk= new StringTokenizer(data,params.get(Constants.SEPARATOR));
            List<String> substringList=new ArrayList<>();
            int position=Integer.parseInt(params.get(Constants.POSITION));
            int count=0;
            //validate position
            if(stk.countTokens()>position && position>0){
                while (stk.hasMoreTokens()) {
                    String cadena= stk.nextToken();
                    if (count>=position) {
                        substringList.add(cadena);
                    }
                    count++;
                }
                return String.join(params.get(Constants.SEPARATOR), substringList);
            }else{
                //error
            }
        }catch (Exception ex){
            //error
        	ex.printStackTrace();
        }
        return data;
    }
	private void getParameters() {
		
	}
	public List<String> getFunctions() {
		return functions;
	}
	public void setFunctions(List<String> functions) {
		this.functions = functions;
	} 
*/
}
