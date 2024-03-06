package cotuba.epub;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GeradorEPUBSujeito {
    private List<GeradorEPUBListener> listeners = new ArrayList<>();

    public void adicionarListener(GeradorEPUBListener listener) {
        listeners.add(listener);
    }

    public void removerListener(GeradorEPUBListener listener) {
        listeners.remove(listener);
    }

    public void notificarListeners(Path arquivoDeSaida) {
        for (GeradorEPUBListener listener : listeners) {
            listener.onEPUBGerado(arquivoDeSaida);
        }
    }
}

