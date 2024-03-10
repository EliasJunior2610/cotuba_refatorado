package cotuba.pdf;

import com.itextpdf.kernel.geom.Path;

public interface GeradorPDFListener {
    void onPDFGerado(Path arquivoDeSaida);
}

