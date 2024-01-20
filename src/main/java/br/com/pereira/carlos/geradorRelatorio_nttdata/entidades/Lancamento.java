package br.com.pereira.carlos.geradorRelatorio_nttdata.entidades;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Lancamento {

	private String descricao;

	private Double valor;

	private int mes;

	private int ano;

	private TipoLancamento tipoLancamento;

	public Lancamento() {

	}

	public Lancamento(String descricao, Double valor, int mes, int ano, TipoLancamento tipoLancamento) {
		this.descricao = descricao;
		this.valor = valor;
		this.mes = mes;
		this.ano = ano;
		this.tipoLancamento = tipoLancamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getValorFormatado() {
		final DecimalFormat decimalFormat = new DecimalFormat("###,##0.00",
				new DecimalFormatSymbols(new Locale("pt", "BR")));

		return decimalFormat.format(valor);
	}

	@Override
	public String toString() {
		return "Lancamentos [descricao=" + descricao + ", valor=" + valor + ", mes=" + mes + ", ano=" + ano
				+ ", tipoLancamento=" + tipoLancamento.getDescricao() + "]";
	}

}
