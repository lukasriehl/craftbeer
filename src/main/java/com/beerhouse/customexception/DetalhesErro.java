package com.beerhouse.customexception;

public class DetalhesErro {

	private String dataHoraFormatada;

	private String mensagem;

	private String detalhes;

	public DetalhesErro(String dataHoraFormatada, String mensagem, String detalhes) {
		super();

		this.dataHoraFormatada = dataHoraFormatada;
		this.mensagem = mensagem;
		this.detalhes = detalhes;
	}	

	public String getDataHoraFormatada() {
		return dataHoraFormatada;
	}

	public void setDataHoraFormatada(String dataHoraFormatada) {
		this.dataHoraFormatada = dataHoraFormatada;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}	
}

