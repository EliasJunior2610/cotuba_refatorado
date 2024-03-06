package cotuba.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import cotuba.application.GeradorEbook;
import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public class GeradorPDF implements GeradorEbook {

    private static final Logger LOGGER = Logger.getLogger(GeradorPDF.class.getName());

    private GeradorPDFSujeito sujeito = new GeradorPDFSujeito();

    public void adicionarListener(GeradorPDFListener listener) {
        sujeito.adicionarListener(listener);
    }

    public void removerListener(GeradorPDFListener listener) {
        sujeito.removerListener(listener);
    }

    public void gera(Ebook ebook) {
        Path arquivoDeSaida = ebook.getArquivoDeSaida();
        PdfWriter writer = null;
        PdfDocument pdf = null;
        Document pdfDocument = null;
        try {
            writer = new PdfWriter(Files.newOutputStream(arquivoDeSaida));
            pdf = new PdfDocument(writer);
            pdfDocument = new Document(pdf);

            for (Capitulo capitulo : ebook.getCapitulos()) {
                String html = capitulo.getConteudoHTML();
                List<IElement> convertToElements = HtmlConverter.convertToElements(html);
                for (IElement element : convertToElements) {
                    pdfDocument.add((IBlockElement) element);
                }

                if (!ebook.isUltimoCapitulo(capitulo)) {
                    pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }

            // Adiciona uma página com o texto "teste" ao final do documento
            //pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            pdfDocument.add(new Paragraph("teste"));

            LOGGER.info("Arquivo PDF gerado com sucesso: " + arquivoDeSaida.toAbsolutePath());

            // Notificar observadores
            sujeito.notificarListeners(arquivoDeSaida);

        } catch (IOException ex) {
            throw new IllegalStateException("Erro ao criar arquivo PDF: " + arquivoDeSaida.toAbsolutePath(), ex);
        } finally {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            if (pdf != null) {
                pdf.close();
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
