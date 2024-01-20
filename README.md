# GeradorRelatorioJsonJasper
Projeto criado para receber um arquivo Json e exportar com o auxilio do JasperReports em Formatos PDF, CSV e XLSX.

#Json
A estrutura do Json é composta de uma lista de lançamentos, titulo, formato de saida e uma imagem em base64.
{
	"lancamentos": [
		{
			"descricao": "Salário",
			"valor": 3000.00,
			"mes": 1,
			"ano": 2023,
			"tipoLancamento": "V"
		},
		{
			"descricao": "Mercado",
			"valor": 470.00,
			"mes": 1,
			"ano": 2023,
			"tipoLancamento": "D"
		}
	],
	"titulo": "Relatório de Lançamentos",
	"formatoRelatorio": "PDF",
	"imagemBase64":
}
