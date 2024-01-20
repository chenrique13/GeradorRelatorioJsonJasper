package br.com.pereira.carlos.geradorRelatorio_nttdata.entidades;

public enum TipoLancamento {

	VANTAGEM ("V", "Vantagem"), 
	DESCONTO ("D", "Desconto");

    public final String codigo;
    public final String descricao;

	private TipoLancamento(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
	

	public static TipoLancamento getTipo(String codigo) {
		TipoLancamento tipo = null;
		
		switch (codigo) {
        case "V":
            return VANTAGEM;
        case "D":
            return DESCONTO;
        default:
            break;
        }
		return tipo;
	}
	
}
