package pe.nom.jl.remplacer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.commons.validator.EmailValidator;

import pe.nom.jl.constants.Constants;
import pe.nom.jl.error.ParseError;

public class ParserFunction{
	private List<String> functions;   
	private List<ParseError> errors;
	public ParserFunction() {
		
	}
	public List<ParseError> getErrors() {
		return errors;
	}
	public void setErrors(List<ParseError> errors) {
		this.errors = errors;
	}
	/*
	public static void main(String[] args) {
		List<String> functions=new ArrayList<String>();
		functions.add("@leftr");
		functions.add("@rightr");
		functions.add("@upper");
		functions.add("@lower");
		functions.add("@camelcase");
		functions.add("@substr");
		functions.add("@aemail");
		
		ParserFunction p= new ParserFunction();
		p.setFunctions(functions);
		String data="health_probes: 'on'\r\n"
				+ "health_path: /ssssss/v1/actuator/health\r\n"
				+ "health_initialdelayseconds: 180\r\n"
				+ "health_timeoutseconds: 10\r\n"
				+ "health_periodSeconds: 30\r\n"
				+ "resource: @substr(-,2,abc-123-456-ff) ddd gfg \r\n"
				+ "domain: portability\r\n"
				+ "appgroup: tv-web-app\r\n"
				+ "tiermsa: experience\r\n"
				+ "";
		String resultado=p.replace(data);
		System.out.println(resultado);
		if(p.getErrors().size()>0) {
			for (int i = 0; i < p.getErrors().size(); i++) {
				System.out.println(p.getErrors().get(i).getNroLine()+","+p.getErrors().get(i).getFunction()+","+p.getErrors().get(i).getDetails());
			}
		}else {
			System.out.println("Sin errores");
		}
		
	}*/
	public String replace(String data) {
		HashMap<String, String> remplaced=new HashMap<>();
		errors= new ArrayList<>();
		StringTokenizer stk= new StringTokenizer(data,"\n");//linea xlinea
		int pos=1;
		while (stk.hasMoreTokens()) {
			String strk=stk.nextToken();
			
			for (int i = 0; i < functions.size(); i++) { //cada funcion
				int index = strk.indexOf(functions.get(i));
				while (index >= 0) {
					//int indice = strk.indexOf(functions.get(i));
					String strEvaluate=strk.substring(index+functions.get(i).length());// empieza desde el parentesis
					Analyzer analyzer= new Analyzer();
					if (analyzer.validateToken(strEvaluate)) {
						try {
							String result=applyFunctions(functions.get(i),analyzer.getParams());
							String functionParam=functions.get(i)+analyzer.getOrigin();
							//data=data.replace(functionParam, result);
							remplaced.put(functionParam, result);
						} catch (Exception e) {
							ParseError p= new ParseError();
							p.setNroLine(pos);
							p.setFunction(functions.get(i));
							p.setDetails(e.getMessage());;
							errors.add(p);
						} 
					} 
				    index = strk.indexOf(functions.get(i), index + 1);
				}
				pos++;
			}
		}
		//reading remplaced
		Iterator iterator = remplaced.entrySet().iterator();
        while (iterator.hasNext()) {
             Map.Entry me2 = (Map.Entry) iterator.next();
             data=data.replace(me2.getKey().toString(), me2.getValue().toString()); 
        }  
		return data;
	}
	private String applyFunctions(String function,List<String> params) throws Exception{
            HashMap<String, Callable<String>> functions= new HashMap<>();
            functions.put(this.functions.get(0),  () -> extractLeft(params.get(0)));
            functions.put(this.functions.get(1),  () -> extractRight(params.get(0)));
            functions.put(this.functions.get(2),  () -> params.get(0).toUpperCase());
            functions.put(this.functions.get(3),  () -> params.get(0).toLowerCase());
            functions.put(this.functions.get(4),  () -> camelcase(params));
            functions.put(this.functions.get(5),  () -> substringFrom(params));
            functions.put(this.functions.get(6),  () -> extractAccountEmail(params.get(0)));//ok
            return functions.get(function).call();
         
    }
	
	private String extractLeft(String data)  throws Exception {/////////por mejorar para la excepcion
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
        }else {
        	throw new Exception("No es una URL valida!!");
        }
    }
	private String extractAccountEmail(String data) throws Exception {
		System.out.println("+++Email:"+data.replaceAll("@.+$", ""));
		if(EmailValidator.getInstance().isValid(data)) {
			return data.replaceAll("@.+$", "");
		}
        throw new Exception("Email invalido!");
    }
	private String camelcase(List<String> params) {
        if (1==params.size()){
            return StringUtils.capitalize(params.get(0).toLowerCase());
        }
        return CaseUtils.toCamelCase(params.get(1), true, CharUtils.toChar(params.get(0)));
    }
	
	private String substringFrom(List<String> params) throws Exception{
        	//@substr(-,1,{{MICRO_NAME}}) 
            //splitting
            StringTokenizer stk= new StringTokenizer(params.get(2),params.get(0));//1: posicion, 2:data
            List<String> substringList=new ArrayList<>();
            int position=Integer.parseInt(params.get(1));//position
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
                return String.join(params.get(0), substringList);
            }else{
                throw new Exception("Error de Sintaxis del Substring");
            } 
    }
	public List<String> getFunctions() {
		return functions;
	}
	public void setFunctions(List<String> functions) {
		this.functions = functions;
	} 

}
