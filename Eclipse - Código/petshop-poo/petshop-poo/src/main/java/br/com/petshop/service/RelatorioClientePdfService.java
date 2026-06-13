package br.com.petshop.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import br.com.petshop.dto.RelatorioClienteDTO;
import br.com.petshop.dto.TotalPorDataDTO;
import br.com.petshop.dto.TotalPorServicoDTO;
import br.com.petshop.model.Animal;
import br.com.petshop.model.Atendimento;

/**
 * Service responsavel pela exportacao do relatorio personalizado em PDF.
 *
 * Esta classe transforma os dados consolidados do relatorio por cliente em um
 * documento PDF estruturado, contendo informacoes do tutor, animais vinculados,
 * atendimentos do periodo e totais agrupados por servico e por data. A criacao
 * dessa camada separa a geracao documental da regra de consulta do relatorio.
 */
@Service
public class RelatorioClientePdfService {

    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale LOCALE_BR = new Locale("pt", "BR");

    public byte[] gerarPdf(RelatorioClienteDTO relatorio) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 36, 36, 42, 42);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            adicionarCabecalho(document);
            adicionarResumoCliente(document, relatorio);
            adicionarAnimais(document, relatorio);
            adicionarAtendimentos(document, relatorio);
            adicionarTotaisPorServico(document, relatorio);
            adicionarTotaisPorData(document, relatorio);
            adicionarRodape(document);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Nao foi possivel gerar o relatorio em PDF.", exception);
        }
    }

    private void adicionarCabecalho(Document document) throws Exception {
        Paragraph titulo = new Paragraph("Relatorio de Servicos por Cliente", fonteTitulo());
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(8);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph("Sistema PetShop POO - Relatorio gerencial", fonteTextoCinza());
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(22);
        document.add(subtitulo);
    }

    private void adicionarResumoCliente(Document document, RelatorioClienteDTO relatorio) throws Exception {
        adicionarTituloSecao(document, "Dados do cliente e periodo");

        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[] { 1.2f, 2.8f });

        adicionarLinhaInformacao(tabela, "Cliente", relatorio.getProprietario().getNome());
        adicionarLinhaInformacao(tabela, "Telefone", relatorio.getProprietario().getTelefone());
        adicionarLinhaInformacao(tabela, "E-mail", valorOuPadrao(relatorio.getProprietario().getEmail(), "Nao informado"));
        adicionarLinhaInformacao(tabela, "Endereco", relatorio.getProprietario().getEndereco());
        adicionarLinhaInformacao(tabela, "Periodo",
                formatarData(relatorio.getDataInicio()) + " a " + formatarData(relatorio.getDataFim()));
        adicionarLinhaInformacao(tabela, "Valor total", formatarMoeda(relatorio.getValorTotal()));

        document.add(tabela);
        adicionarEspaco(document);
    }

    private void adicionarAnimais(Document document, RelatorioClienteDTO relatorio) throws Exception {
        adicionarTituloSecao(document, "Animais vinculados ao cliente");

        if (relatorio.getAnimais().isEmpty()) {
            document.add(new Paragraph("Nenhum animal vinculado ao cliente.", fonteTextoCinza()));
            adicionarEspaco(document);
            return;
        }

        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[] { 1.5f, 1.4f, 1.4f, 1.0f });

        adicionarCabecalhoTabela(tabela, "Nome");
        adicionarCabecalhoTabela(tabela, "Especie");
        adicionarCabecalhoTabela(tabela, "Raca");
        adicionarCabecalhoTabela(tabela, "Peso");

        for (Animal animal : relatorio.getAnimais()) {
            adicionarCelula(tabela, animal.getNome());
            adicionarCelula(tabela, animal.getEspecie());
            adicionarCelula(tabela, valorOuPadrao(animal.getRaca(), "Nao informada"));
            adicionarCelula(tabela, animal.getPeso() + " kg");
        }

        document.add(tabela);
        adicionarEspaco(document);
    }

    private void adicionarAtendimentos(Document document, RelatorioClienteDTO relatorio) throws Exception {
        adicionarTituloSecao(document, "Servicos prestados no periodo");

        if (relatorio.getAtendimentos().isEmpty()) {
            document.add(new Paragraph("Nenhum servico foi prestado ao cliente no periodo informado.", fonteTextoCinza()));
            adicionarEspaco(document);
            return;
        }

        PdfPTable tabela = new PdfPTable(5);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[] { 1.0f, 1.3f, 1.8f, 1.0f, 2.2f });

        adicionarCabecalhoTabela(tabela, "Data");
        adicionarCabecalhoTabela(tabela, "Animal");
        adicionarCabecalhoTabela(tabela, "Servico");
        adicionarCabecalhoTabela(tabela, "Valor");
        adicionarCabecalhoTabela(tabela, "Observacoes");

        for (Atendimento atendimento : relatorio.getAtendimentos()) {
            adicionarCelula(tabela, formatarData(atendimento.getDataAtendimento()));
            adicionarCelula(tabela, atendimento.getAnimal().getNome());
            adicionarCelula(tabela, atendimento.getServico().getNome());
            adicionarCelula(tabela, formatarMoeda(atendimento.getValorCobrado()));
            adicionarCelula(tabela, valorOuPadrao(atendimento.getObservacoes(), "Sem observacoes"));
        }

        document.add(tabela);
        adicionarEspaco(document);
    }

    private void adicionarTotaisPorServico(Document document, RelatorioClienteDTO relatorio) throws Exception {
        adicionarTituloSecao(document, "Total por servico");

        if (relatorio.getTotaisPorServico().isEmpty()) {
            document.add(new Paragraph("Sem totais por servico no periodo informado.", fonteTextoCinza()));
            adicionarEspaco(document);
            return;
        }

        PdfPTable tabela = new PdfPTable(3);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[] { 2.5f, 0.8f, 1.2f });

        adicionarCabecalhoTabela(tabela, "Servico");
        adicionarCabecalhoTabela(tabela, "Qtd.");
        adicionarCabecalhoTabela(tabela, "Total");

        for (TotalPorServicoDTO total : relatorio.getTotaisPorServico()) {
            adicionarCelula(tabela, total.getNomeServico());
            adicionarCelula(tabela, String.valueOf(total.getQuantidade()));
            adicionarCelula(tabela, formatarMoeda(total.getValorTotal()));
        }

        document.add(tabela);
        adicionarEspaco(document);
    }

    private void adicionarTotaisPorData(Document document, RelatorioClienteDTO relatorio) throws Exception {
        adicionarTituloSecao(document, "Total por data");

        if (relatorio.getTotaisPorData().isEmpty()) {
            document.add(new Paragraph("Sem totais por data no periodo informado.", fonteTextoCinza()));
            adicionarEspaco(document);
            return;
        }

        PdfPTable tabela = new PdfPTable(3);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[] { 1.5f, 0.8f, 1.2f });

        adicionarCabecalhoTabela(tabela, "Data");
        adicionarCabecalhoTabela(tabela, "Qtd.");
        adicionarCabecalhoTabela(tabela, "Total");

        for (TotalPorDataDTO total : relatorio.getTotaisPorData()) {
            adicionarCelula(tabela, formatarData(total.getData()));
            adicionarCelula(tabela, String.valueOf(total.getQuantidade()));
            adicionarCelula(tabela, formatarMoeda(total.getValorTotal()));
        }

        document.add(tabela);
        adicionarEspaco(document);
    }

    private void adicionarRodape(Document document) throws Exception {
        Paragraph rodape = new Paragraph(
                "Documento gerado automaticamente pelo Sistema PetShop POO.",
                fonteTextoCinza());
        rodape.setAlignment(Element.ALIGN_CENTER);
        rodape.setSpacingBefore(12);
        document.add(rodape);
    }

    private void adicionarTituloSecao(Document document, String texto) throws Exception {
        Paragraph titulo = new Paragraph(texto, fonteSecao());
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(8);
        document.add(titulo);
    }

    private void adicionarEspaco(Document document) throws Exception {
        Paragraph espaco = new Paragraph(" ");
        espaco.setSpacingAfter(4);
        document.add(espaco);
    }

    private void adicionarLinhaInformacao(PdfPTable tabela, String rotulo, String valor) {
        PdfPCell celulaRotulo = new PdfPCell(new Phrase(rotulo, fonteTabelaCabecalho()));
        celulaRotulo.setPadding(8);
        celulaRotulo.setBorderColor(corBorda());
        celulaRotulo.setBackgroundColor(corFundoCabecalho());
        tabela.addCell(celulaRotulo);

        PdfPCell celulaValor = new PdfPCell(new Phrase(valorOuPadrao(valor, "Nao informado"), fonteTexto()));
        celulaValor.setPadding(8);
        celulaValor.setBorderColor(corBorda());
        tabela.addCell(celulaValor);
    }

    private void adicionarCabecalhoTabela(PdfPTable tabela, String texto) {
        PdfPCell celula = new PdfPCell(new Phrase(texto, fonteTabelaCabecalho()));
        celula.setPadding(8);
        celula.setBorderColor(corBorda());
        celula.setBackgroundColor(corFundoCabecalho());
        tabela.addCell(celula);
    }

    private void adicionarCelula(PdfPTable tabela, String texto) {
        PdfPCell celula = new PdfPCell(new Phrase(valorOuPadrao(texto, "Nao informado"), fonteTexto()));
        celula.setPadding(8);
        celula.setBorderColor(corBorda());
        tabela.addCell(celula);
    }

    private String valorOuPadrao(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor;
    }

    private String formatarData(LocalDate data) {
        return data == null ? "Nao informada" : data.format(DATA_FORMATTER);
    }

    private String formatarMoeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(LOCALE_BR).format(valor == null ? BigDecimal.ZERO : valor);
    }

    private Font fonteTitulo() {
        return FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    }

    private Font fonteSecao() {
        return FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
    }

    private Font fonteTabelaCabecalho() {
        return FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    }

    private Font fonteTexto() {
        return FontFactory.getFont(FontFactory.HELVETICA, 9);
    }

    private Font fonteTextoCinza() {
        Font fonte = FontFactory.getFont(FontFactory.HELVETICA, 9);
        fonte.setColor(90, 103, 116);
        return fonte;
    }

    private Color corBorda() {
        return new Color(220, 226, 235);
    }

    private Color corFundoCabecalho() {
        return new Color(246, 248, 250);
    }
}