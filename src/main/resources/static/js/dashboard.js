document.addEventListener('DOMContentLoaded', function () {

    var g = JSON.parse(GRAFICAS);
    var proximas = JSON.parse(PROXIMAS);

    /* ===================== HELPERS ===================== */
    function hexAlpha(hex, alpha) {
        // Convierte #RRGGBB a rgba con transparencia
        try {
            var r = parseInt(hex.slice(1,3),16);
            var g = parseInt(hex.slice(3,5),16);
            var b = parseInt(hex.slice(5,7),16);
            return 'rgba(' + r + ',' + g + ',' + b + ',' + alpha + ')';
        } catch(e) { return hex; }
    }

    function buildLegend(containerId, labels, data, colors) {
        var c = document.getElementById(containerId);
        if (!c) return;
        var total = data.reduce(function(a,b){return a+b;},0);
        c.innerHTML = '';
        labels.forEach(function(label, i) {
            var pct = total > 0 ? Math.round(data[i]*100/total) : 0;
            c.innerHTML +=
                '<div class="db-legend-item">' +
                    '<span class="db-legend-dot" style="background:' + colors[i] + '"></span>' +
                    '<span>' + label + '</span>' +
                    '<span class="db-legend-count">' + data[i] + ' (' + pct + '%)</span>' +
                '</div>';
        });
    }

    var chartDefaults = {
        font: { family: 'Arial, sans-serif' },
        plugins: { legend: { display: false } }
    };

    /* ===================== GRÁFICA 1: DONA — TIPO DE INMUEBLE ===================== */
    var ctxInmueble = document.getElementById('chartInmueble');
    if (ctxInmueble && g.inmuebleLabels && g.inmuebleLabels.length > 0) {
        new Chart(ctxInmueble, {
            type: 'doughnut',
            data: {
                labels: g.inmuebleLabels,
                datasets: [{
                    data: g.inmuebleData,
                    backgroundColor: g.inmuebleColores,
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                cutout: '65%',
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(ctx) {
                                var total = ctx.dataset.data.reduce(function(a,b){return a+b;},0);
                                var pct = Math.round(ctx.parsed*100/total);
                                return ' ' + ctx.label + ': ' + ctx.parsed + ' (' + pct + '%)';
                            }
                        }
                    }
                }
            }
        });
        buildLegend('legendInmueble', g.inmuebleLabels, g.inmuebleData, g.inmuebleColores);
    } else if (ctxInmueble) {
        ctxInmueble.parentElement.innerHTML += '<p style="text-align:center;color:#9CA3AF;font-size:13px;padding:20px">Sin datos en el período.</p>';
    }

    /* ===================== GRÁFICA 2: BARRAS — TIPO DE CAPACITACIÓN ===================== */
    var ctxCap = document.getElementById('chartCapacitacion');
    if (ctxCap && g.capLabels && g.capLabels.length > 0) {
        new Chart(ctxCap, {
            type: 'bar',
            data: {
                labels: g.capLabels,
                datasets: [{
                    data: g.capData,
                    backgroundColor: '#8d0000b8',
                    borderRadius: 4,
                    borderSkipped: false
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: true,
                plugins: { legend: { display: false } },
                scales: {
                    x: { grid: { color: '#F3F4F6' }, ticks: { font: { size: 11 } } },
                    y: { grid: { display: false }, ticks: { font: { size: 11 } } }
                }
            }
        });
    }

    /* ===================== GRÁFICA 3: LÍNEA — POR SEMANA ===================== */
    var ctxSemana = document.getElementById('chartSemana');
    if (ctxSemana && g.semanaLabels && g.semanaLabels.length > 0) {
        new Chart(ctxSemana, {
            type: 'line',
            data: {
                labels: g.semanaLabels,
                datasets: [{
                    data: g.semanaData,
                    borderColor: '#2563EB',
                    backgroundColor: 'rgba(37,99,235,0.08)',
                    borderWidth: 2,
                    pointBackgroundColor: '#2563EB',
                    pointRadius: 4,
                    fill: true,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: { legend: { display: false } },
                scales: {
                    x: { grid: { color: '#F3F4F6' }, ticks: { font: { size: 11 } } },
                    y: { grid: { color: '#F3F4F6' }, ticks: { font: { size: 11 }, stepSize: 1 }, beginAtZero: true }
                }
            }
        });
    }

    /* ===================== GRÁFICA 4: DONA — ESTATUS ===================== */
    var ctxEstatus = document.getElementById('chartEstatus');
    if (ctxEstatus && g.estatusLabels && g.estatusLabels.length > 0) {
        new Chart(ctxEstatus, {
            type: 'doughnut',
            data: {
                labels: g.estatusLabels,
                datasets: [{
                    data: g.estatusData,
                    backgroundColor: g.estatusColores,
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                cutout: '65%',
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(ctx) {
                                var total = ctx.dataset.data.reduce(function(a,b){return a+b;},0);
                                var pct = Math.round(ctx.parsed*100/total);
                                return ' ' + ctx.label + ': ' + ctx.parsed + ' (' + pct + '%)';
                            }
                        }
                    }
                }
            }
        });
        buildLegend('legendEstatus', g.estatusLabels, g.estatusData, g.estatusColores);
    }

    /* ===================== TABLA PRÓXIMAS ===================== */
    var tabla = document.getElementById('tablaProximas');
    if (tabla) {
        if (!proximas || proximas.length === 0) {
            tabla.innerHTML = '<tr><td colspan="5" class="db-proximas-empty">Sin próximas capacitaciones.</td></tr>';
        } else {
            proximas.forEach(function(cap) {
                var fecha = cap.fecha || '—';
                var hi = cap.horaInicio ? cap.horaInicio.substring(0,5) : '—';
                var hf = cap.horaFin   ? cap.horaFin.substring(0,5)   : '—';
                var sol = cap.nombreSolicitante || '—';
                var tipo = (cap.tipoCapacitacion && cap.tipoCapacitacion.nombre) ? cap.tipoCapacitacion.nombre : '—';
                var inst = (cap.instructor && cap.instructor.nombre) ? cap.instructor.nombre : 'Sin asignar';
                tabla.innerHTML +=
                    '<tr>' +
                        '<td>' + fecha + '</td>' +
                        '<td>' + hi + ' - ' + hf + '</td>' +
                        '<td>' + sol + '</td>' +
                        '<td>' + tipo + '</td>' +
                        '<td>' + inst + '</td>' +
                    '</tr>';
            });
        }
    }

});