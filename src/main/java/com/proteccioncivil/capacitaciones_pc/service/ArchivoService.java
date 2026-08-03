package com.proteccioncivil.capacitaciones_pc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ArchivoService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String guardarLista(MultipartFile archivo, Long idCapacitacion) throws IOException {

        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        if (!"application/pdf".equals(archivo.getContentType())) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF.");
        }

        Path ruta = Paths.get(uploadDir).toAbsolutePath().normalize();

        if (!Files.exists(ruta)) {
            Files.createDirectories(ruta);
        }

        String nombreArchivo = "CAP-" + idCapacitacion + ".pdf";
        Path destino = ruta.resolve(nombreArchivo);

        try (var inputStream = archivo.getInputStream()) {
            Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
        }

        return "listas-asistencia/" + nombreArchivo;
    }
}

