package com.proteccioncivil.capacitaciones_pc.service;

import com.proteccioncivil.capacitaciones_pc.dto.DashboardFiltroDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

@Service
public class DashboardPdfService {

    private final SpringTemplateEngine templateEngine;

    public DashboardPdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generarReporte(Context context) throws Exception {

        // Cargar el logo como Base64
        String logoBase64 = cargarImagenBase64("static/img/logo-pc.png");
        context.setVariable("logoBase64", logoBase64);

        String html = templateEngine.process("reporte-dashboard", context);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null); // ya no se necesita baseUrl para el logo
        builder.toStream(out);
        builder.run();

        return out.toByteArray();
    }

    private String cargarImagenBase64(String rutaClasspath) throws Exception {
        ClassPathResource resource = new ClassPathResource(rutaClasspath);
        try (InputStream is = resource.getInputStream()) {
            byte[] bytes = is.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    private void cargarDashboard(Model model, DashboardFiltroDTO filtro){}
}