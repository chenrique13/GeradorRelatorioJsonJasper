package br.com.pereira.carlos.geradorRelatorio_nttdata.entidades;

public enum TipoFormatoRelatorio {

	PDF (0, "PDF"), 
	XLSX (1, "XLSX"),
	CSV (2, "CSV");

    public final int codigo;
    public final String descricao;

	private TipoFormatoRelatorio(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
	

	public static TipoFormatoRelatorio getTipo(String Descricao) {
		TipoFormatoRelatorio formato = null;
		
		switch (Descricao) {
        case "PDF":
            return PDF;
        case "XLSX":
            return XLSX;
        case "CSV":
            return CSV;
        default:
            break;
        }
		return formato;
	}
	
}
