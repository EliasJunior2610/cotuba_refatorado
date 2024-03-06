package cotuba.pdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.geom.Path;


public class GeradorPDFSujeito {
    private List<GeradorPDFListener> listeners = new ArrayList<>();

    public void adicionarListener(GeradorPDFListener listener) {
        listeners.add(listener);
    }

    public void removerListener(GeradorPDFListener listener) {
        listeners.remove(listener);
    }

    public void notificarListeners(java.nio.file.Path arquivoDeSaida) {
        for (GeradorPDFListener listener : listeners) {
            listener.onPDFGerado((Path) arquivoDeSaida);
        }
    }
}
