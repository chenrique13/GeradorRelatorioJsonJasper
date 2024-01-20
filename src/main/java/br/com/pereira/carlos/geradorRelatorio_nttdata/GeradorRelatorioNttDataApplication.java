package br.com.pereira.carlos.geradorRelatorio_nttdata;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pereira.carlos.geradorRelatorio_nttdata.entidades.Lancamento;
import br.com.pereira.carlos.geradorRelatorio_nttdata.entidades.TipoFormatoRelatorio;
import br.com.pereira.carlos.geradorRelatorio_nttdata.entidades.TipoLancamento;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class GeradorRelatorioNttDataApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GeradorRelatorioNttDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File json = ResourceUtils.getFile("classpath:dados.json");
//		File json = ResourceUtils.getFile("classpath:dados2.json");
//		File json = ResourceUtils.getFile("classpath:dados3.json");
//		File json = ResourceUtils.getFile("classpath:dados4.json");

		System.out.println(gerarRelatorio(json));
	}

	/**
	 * Método geração de relatorio a partir de um arquivo Json, podendo ser
	 * exportado em PDF, CSV e XLSX.
	 * 
	 * @param json
	 * @return mensagem
	 * @throws Exception
	 * @author Carlos.Pereira
	 */
	private String gerarRelatorio(File json) throws Exception {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode objetoJson = objectMapper.readTree(json);

			JsonNode lista = objetoJson.get("lancamentos");
			List<Lancamento> lancamentos = new ArrayList<>();
			for (JsonNode lancamento : lista) {
				lancamentos.add(new Lancamento(lancamento.get("descricao").asText(),
						lancamento.get("valor").asDouble(),lancamento.get("mes").asInt(),
						lancamento.get("ano").asInt(),TipoLancamento.getTipo(lancamento.get("tipoLancamento").asText())));
			}

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lancamentos);
			TipoFormatoRelatorio formatoRelatorio = TipoFormatoRelatorio.getTipo(objetoJson.get("formatoRelatorio").asText());
			String titulo = objetoJson.get("titulo").asText();
			Image imagem = null;
			
			if (objetoJson.has("imagemBase64")) {
				String imagemBase64 = objetoJson.get("imagemBase64").asText();
				byte[] imagemByte = Base64.getDecoder().decode(imagemBase64);
				imagem = new ImageIcon(imagemByte).getImage();
				System.out.println("Imagem recebida com sucesso!");
			} else {
				System.out.println("O arquivo Json não contem a imagem em base64.");
			}
			
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("IMAGEM", "extrato.png");
			parametros.put("IMAGEM64", imagem);
			parametros.put("TITULO", titulo);

			ClassPathResource urlJasper = new ClassPathResource("relatorioExtratoBancario.jasper");
			InputStream jasperStream = urlJasper.getInputStream();
			JasperReport jasperReport = (JasperReport) net.sf.jasperreports.engine.util.JRLoader.loadObject(jasperStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

			LocalDateTime dataSistema = LocalDateTime.now();
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
			String dataHoraFormatada = dataSistema.format(formato);
			String mensagem = "";

			switch (formatoRelatorio) {
			case PDF:
				JasperExportManager.exportReportToPdfFile(jasperPrint, System.getProperty("user.home")
						+ String.format("/Desktop/extratoBancario_%s.pdf", dataHoraFormatada));
				mensagem = "Relatório gerado em PDF com sucesso!";
				break;
			case CSV:
				JRCsvExporter csvExporter = new JRCsvExporter();
				csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				csvExporter.setExporterOutput(
						new SimpleWriterExporterOutput(new OutputStreamWriter(new FileOutputStream(new File(
								System.getProperty("user.home") + String.format("/Desktop/extratoBancario_%s.csv"),
								dataHoraFormatada)), "UTF-8")));
				csvExporter.exportReport();
				mensagem = "Relatório gerado em CSV com sucesso!";
				break;
			case XLSX:
				JRXlsxExporter xlsxExporter = new JRXlsxExporter();
				xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
						new FileOutputStream(new File(System.getProperty("user.home")
								+ String.format("/Desktop/extratoBancario_%s.xlsx", dataHoraFormatada)))));
				xlsxExporter.exportReport();
				mensagem = "Relatório gerado em XLSX com sucesso!";
				break;
			default:
				mensagem = "O relatório não foi gerado, pois, o formato solicitado não foi encontrado.";
				break;
			}
			return mensagem;
		} catch (Exception e) {
			throw new Exception("Não foi possível renderizar o relatório");
		}
	}
}
