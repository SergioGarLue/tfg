package com.daw.tfg.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.tfg.Repository.EditorRepository;
import com.daw.tfg.models.Editor;

@Service
public class EditorService {

    private final EditorRepository editorRepository;

    public EditorService(EditorRepository editorRepository) {
        this.editorRepository = editorRepository;
    }

    public List<Editor> findAll() {
        return editorRepository.findAll();
    }

    public Optional<Editor> findById(Long id) {
        return editorRepository.findById(id);
    }

    public Optional<Editor> findByNombre(String nombre) {
        return editorRepository.findByNombre(nombre);
    }

    public List<Editor> findByNombreContaining(String fragmento) {
        return editorRepository.findByNombreContainingIgnoreCase(fragmento);
    }

    public List<Editor> findByJuegoTituloContaining(String tituloFragmento) {
        return editorRepository.findByJuegoTituloContainingIgnoreCase(tituloFragmento);
    }

    public List<Editor> findByJuegoId(Long idJuego) {
        return editorRepository.findByJuegoIdJuego(idJuego);
    }

    public Editor save(Editor editor) {
        return editorRepository.save(editor);
    }

    public void deleteById(Long id) {
        editorRepository.deleteById(id);
    }
}

