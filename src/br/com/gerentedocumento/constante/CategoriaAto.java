package br.com.gerentedocumento.constante;

public enum CategoriaAto {
	consumo("consumo"),obras("obras");
	
	private String categoria;
	
	private CategoriaAto (String categoria){
		this.categoria = categoria;
	}
	
	public String getCategoria(){
		return categoria;
	}
}
