package pe.nom.jl.remplacer.core;

public class Ver {
public static void main(String[] args) {
	String ver="01234AAAA56789AAAA121314151611";
	String buscar="AAAA";
	int index = ver.indexOf(buscar);
	while (index >= 0) {
	    System.out.println(index+buscar.length()-1);
	    index = ver.indexOf(buscar, index + 1);
	}
}
}
