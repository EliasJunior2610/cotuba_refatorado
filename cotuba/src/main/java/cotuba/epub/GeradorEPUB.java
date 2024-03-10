package cotuba.epub;

import cotuba.application.GeradorEbook;
import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class GeradorEPUB implements GeradorEbook {

    private static final Logger LOGGER = Logger.getLogger(GeradorEPUB.class.getName());

    private GeradorEPUBSujeito sujeito = new GeradorEPUBSujeito();

    public void adicionarListener(GeradorEPUBListener listener) {
        sujeito.adicionarListener(listener);
    }

    public void removerListener(GeradorEPUBListener listener) {
        sujeito.removerListener(listener);
    }

    public void gera(Ebook ebook) {
        Path arquivoDeSaida = ebook.getArquivoDeSaida();
        var epub = new Book();
        for (Capitulo capitulo : ebook.getCapitulos()) {
            String html = capitulo.getConteudoHTML();
            String tituloDoCapitulo = capitulo.getTitulo();
            epub.addSection(tituloDoCapitulo, new Resource(html.getBytes(), MediatypeService.XHTML));
        }

        // Adiciona uma página com o texto "teste" ao final do EPUB
        String htmlTeste = "<html><body><h1>Teste</h1></body></html>";
        epub.addSection("Teste", new Resource(htmlTeste.getBytes(), MediatypeService.XHTML));

        var epubWriter = new EpubWriter();
        try {
            epubWriter.write(epub, Files.newOutputStream(arquivoDeSaida));

            LOGGER.info("Arquivo EPUB gerado com sucesso: " + arquivoDeSaida.toAbsolutePath());

            // Notificar observadores
            sujeito.notificarListeners(arquivoDeSaida);

        } catch (IOException ex) {
            throw new IllegalStateException("Erro ao criar arquivo EPUB: " + arquivoDeSaida.toAbsolutePath(), ex);
        }
    }
}
