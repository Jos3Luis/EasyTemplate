package pe.nom.jl.error;

public class ParseError {
	private int nroLine;
	private String function;
	private String details;

 

	public int getNroLine() {
		return nroLine;
	}

	public void setNroLine(int nroLine) {
		this.nroLine = nroLine;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
