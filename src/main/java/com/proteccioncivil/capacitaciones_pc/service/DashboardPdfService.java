package com.proteccioncivil.capacitaciones_pc.service;

import com.proteccioncivil.capacitaciones_pc.dto.DashboardFiltroDTO;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;

@Service
public class DashboardPdfService {

    private final SpringTemplateEngine templateEngine;

    public DashboardPdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generarReporte(Context context) throws Exception {

        String html = templateEngine.process("reporte-dashboard", context);

        String baseUrl = new java.io.File("src/main/resources/static")
                .toURI()
                .toString();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, baseUrl);
        builder.toStream(out);
        builder.run();

        return out.toByteArray();
    }

    private void cargarDashboard(Model model, DashboardFiltroDTO filtro){}
}
