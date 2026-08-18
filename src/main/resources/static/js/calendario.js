document.addEventListener('DOMContentLoaded', function () {

    /* ===================== DATOS DE EJEMPLO =====================
       Reemplaza este array con la variable EVENTOS descrita arriba
       cuando conectes la BD. */
    var EVENTOS = JSON.parse(CAPACITACIONES).map(function(c) {
          return {
             id: c.id,
          pc: c.pc,
          op: c.op,
          solicitante: c.nombreSolicitante,
          tipoCapacitacion: c.tipoCapacitacion.nombre,
          tipoInmueble: c.tipoInmueble.nombre,
          instructor: c.instructor ? c.instructor.nombre : "Sin asignar",
          instructoresApoyo: c.instructoresApoyo || "-",
          tipoKey: obtenerTipoKey(c.tipoInmueble.nombre),
          fecha: c.fecha,
          horaInicio: c.horaInicio.substring(0, 5),
          horaFin: c.horaFin ? c.horaFin.substring(0, 5) : "",
          direccion: c.direccion,
          estatus: c.estatus.nombre,
          contacto: c.contacto,
          telefono: c.telefono,
          observaciones: c.observaciones,
          hombres: c.hombres,
          mujeres: c.mujeres,
          ninos: c.ninos,
          archivoLista: c.archivoLista,
          registradoPor: c.registradoPor,
          modificadoPor: c.modificadoPor,
          fechaModificacion: c.fechaModificacion,
          fechaRegistro: c.fechaRegistro
          };
      });
      
      
    var COLORES_BD = (typeof COLORES_TIPO_INMUEBLE !== 'undefined')
       ? JSON.parse(COLORES_TIPO_INMUEBLE)
        : {};

    function colorPorTipoInmueble(nombreTipo) {
    return COLORES_BD[nombreTipo] || '#9CA3AF';
    }

    function estiloEvento(ev) {
    var hex = colorPorTipoInmueble(ev.tipoInmueble);
    return 'background:' + hex + '22; border-left-color:' + hex + '; color:' + oscurecer(hex);
    }

    function oscurecer(hex) {
     try {
        var r = parseInt(hex.slice(1,3),16);
        var g = parseInt(hex.slice(3,5),16);
        var b = parseInt(hex.slice(5,7),16);
        r = Math.round(r*0.55); g = Math.round(g*0.55); b = Math.round(b*0.55);
        return 'rgb(' + r + ',' + g + ',' + b + ')';
     } catch(e) { return '#374151'; }
    }

    /* ===================== HELPERS ===================== */
    function hoy() { return new Date(); }

    function diasDesdeHoy(n) {
        var d = new Date();
        d.setDate(d.getDate() + n);
        return d.toISOString().slice(0, 10);
    }

    function fechaStr(date) { return date.toISOString().slice(0, 10); }

    function formatFechaLarga(isoStr) {
        var meses = ['enero','febrero','marzo','abril','mayo','junio',
                     'julio','agosto','septiembre','octubre','noviembre','diciembre'];
        var p = isoStr.split('-');
        return p[2] + ' de ' + meses[parseInt(p[1], 10) - 1] + ' de ' + p[0];
    }

    function inicioSemana(date) {
        var d = new Date(date);
        var dia = d.getDay();
        var diff = (dia === 0) ? -6 : 1 - dia; // lunes
        d.setDate(d.getDate() + diff);
        return d;
    }

    function claseEvento(tipoKey) {
        var mapa = { escuela: 'cal-evento-escuela', gobierno: 'cal-evento-gobierno',
                     empresa: 'cal-evento-empresa', unidad: 'cal-evento-unidad' };
        return mapa[tipoKey] || 'cal-evento-default';
    }

    function claseEventoMes(tipoKey) { return claseEvento(tipoKey); }

    function colorDot(ev) {
        return colorPorTipoInmueble(ev.tipoInmueble);
    }

    function eventosEnFecha(isoStr) {
        return EVENTOS.filter(function (e) { return e.fecha === isoStr; });
    }

    /* ===================== ESTADO ===================== */
    var vista = 'semana';
    var fechaActual = new Date();
    var fechaMiniActual = new Date();

    /* ===================== VISTA SEMANAL ===================== */
    var HORAS = [];
    for (var h = 7; h <= 19; h++) { HORAS.push(h < 10 ? '0' + h + ':00' : h + ':00'); }
    var DIAS_CORTOS = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];

    function renderSemana() {
        var grid = document.getElementById('semanaGrid');
        if (!grid) return;
        grid.innerHTML = '';

        var lunes = inicioSemana(fechaActual);
        var dias = [];
        for (var i = 0; i < 7; i++) {
            var d = new Date(lunes);
            d.setDate(d.getDate() + i);
            dias.push(d);
        }

        // Actualiza título
        var viernes = dias[6];
        var meses = ['enero','febrero','marzo','abril','mayo','junio',
                     'julio','agosto','septiembre','octubre','noviembre','diciembre'];
        document.getElementById('tituloPeriodo').textContent =
            lunes.getDate() + ' – ' + viernes.getDate() + ' de ' +
            meses[lunes.getMonth()] + ' de ' + lunes.getFullYear();

        // Celda vacía esquina
        var esquina = document.createElement('div');
        esquina.className = 'cal-hora-cell';
        esquina.style.borderBottom = '1px solid var(--pc-borde)';
        grid.appendChild(esquina);

        // Cabecera de días
        dias.forEach(function (d) {
            var hoyStr = fechaStr(new Date());
            var esHoy = fechaStr(d) === hoyStr;
            var div = document.createElement('div');
            div.className = 'cal-semana-dia-header' + (esHoy ? ' hoy' : '');
            div.innerHTML =
                '<span class="dia-nombre">' + DIAS_CORTOS[dias.indexOf(d)] + '</span>' +
                '<span class="dia-num">' + d.getDate() + '</span>';
            grid.appendChild(div);
        });

        // Filas de horas
        HORAS.forEach(function (hora) {
            // Columna de hora
            var horaCell = document.createElement('div');
            horaCell.className = 'cal-hora-cell';
            horaCell.innerHTML = '<span class="cal-hora-label">' + hora + '</span>';
            grid.appendChild(horaCell);

            // Celdas por día
            dias.forEach(function (d) {
                var celda = document.createElement('div');
                celda.className = 'cal-celda';

                var dStr = fechaStr(d);
                var evs = eventosEnFecha(dStr).filter(function (e) {
                    return e.horaInicio.slice(0, 2) === hora.slice(0, 2);
                });

                evs.forEach(function (ev) {
                    var div = document.createElement('div');
                    div.className = 'cal-evento ' + claseEvento(ev.tipoKey);
                    div.style.cssText = estiloEvento(ev);
                    div.innerHTML =
                        '<span class="ev-hora">' + ev.horaInicio + ' - ' + ev.horaFin + '</span>' +
                        '<span class="ev-nombre">' + ev.solicitante + '</span>' +
                        '<span class="ev-tipo">' + ev.tipoCapacitacion + '</span>';
                    div.addEventListener('click', function () { abrirModal(ev); });
                    celda.appendChild(div);
                });

                grid.appendChild(celda);
            });
        });
    }

    /* ===================== VISTA MENSUAL ===================== */
    function renderMes() {
        var header = document.getElementById('mesHeader');
        var gridEl = document.getElementById('mesGrid');
        if (!header || !gridEl) return;
        header.innerHTML = '';
        gridEl.innerHTML = '';

        var meses = ['enero','febrero','marzo','abril','mayo','junio',
                     'julio','agosto','septiembre','octubre','noviembre','diciembre'];
        var año = fechaActual.getFullYear();
        var mes = fechaActual.getMonth();

        document.getElementById('tituloPeriodo').textContent =
            meses[mes].charAt(0).toUpperCase() + meses[mes].slice(1) + ' ' + año;

        ['Lun','Mar','Mié','Jue','Vie','Sáb','Dom'].forEach(function (d) {
            var span = document.createElement('div');
            span.className = 'cal-mes-dia-nombre';
            span.textContent = d;
            header.appendChild(span);
        });

        var primerDia = new Date(año, mes, 1);
        var ultimoDia = new Date(año, mes + 1, 0);
        var inicioGrid = new Date(primerDia);
        var diaSemana = primerDia.getDay();
        var offset = (diaSemana === 0) ? 6 : diaSemana - 1;
        inicioGrid.setDate(inicioGrid.getDate() - offset);

        var hoyStr = fechaStr(new Date());

        for (var i = 0; i < 42; i++) {
            var d = new Date(inicioGrid);
            d.setDate(d.getDate() + i);
            var dStr = fechaStr(d);
            var fueraMes = d.getMonth() !== mes;
            var esHoy = dStr === hoyStr;

            var celda = document.createElement('div');
            celda.className = 'cal-mes-celda' +
                (fueraMes ? ' fuera-mes' : '') +
                (esHoy ? ' hoy' : '');

            var numSpan = document.createElement('div');
            numSpan.className = 'cal-mes-num';
            numSpan.textContent = d.getDate();
            celda.appendChild(numSpan);

            var evs = eventosEnFecha(dStr);
            var max = 5;
            evs.slice(0, max).forEach(function (ev) {
                var span = document.createElement('span');
                span.className = 'cal-mes-evento ' + claseEventoMes(ev.tipoKey);
                span.style.cssText = estiloEvento(ev);
                span.textContent = ev.solicitante;
                span.addEventListener('click', function () { abrirModal(ev); });
                celda.appendChild(span);
            });
            if (evs.length > max) {
                var mas = document.createElement('span');
                mas.className = 'cal-mas-eventos';
                mas.textContent = '+' + (evs.length - max) + ' más';
                celda.appendChild(mas);
            }

            gridEl.appendChild(celda);
        }
    }

    /* ===================== MINI CALENDARIO ===================== */
    function renderMini() {
        var miniGrid = document.getElementById('miniGrid');
        var miniTitulo = document.getElementById('miniTitulo');
        if (!miniGrid || !miniTitulo) return;
        miniGrid.innerHTML = '';

        var meses = ['Enero','Febrero','Marzo','Abril','Mayo','Junio',
                     'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
        var año = fechaMiniActual.getFullYear();
        var mes = fechaMiniActual.getMonth();
        miniTitulo.textContent = meses[mes] + ' ' + año;

        var primerDia = new Date(año, mes, 1);
        var diaSemana = primerDia.getDay();
        var offset = (diaSemana === 0) ? 6 : diaSemana - 1;
        var inicio = new Date(primerDia);
        inicio.setDate(inicio.getDate() - offset);

        var hoyStr = fechaStr(new Date());

        for (var i = 0; i < 42; i++) {
            var d = new Date(inicio);
            d.setDate(d.getDate() + i);
            var dStr = fechaStr(d);
            var fuera = d.getMonth() !== mes;

            var span = document.createElement('span');
            span.className = 'cal-mini-dia' +
                (fuera ? ' fuera' : '') +
                (dStr === hoyStr ? ' hoy' : '') +
                (eventosEnFecha(dStr).length > 0 && !fuera ? ' con-eventos' : '');
            span.textContent = d.getDate();
            span.addEventListener('click', function (dd) {
                return function () {
                    fechaActual = new Date(dd);
                    fechaMiniActual = new Date(dd);
                    renderTodo();
                };
            }(new Date(d)));
            miniGrid.appendChild(span);
        }
    }

    /* ===================== PRÓXIMAS CAPACITACIONES (SIDEBAR) ===================== */
    function renderProximas() {
        var contenedor = document.getElementById('listaProximas');
        if (!contenedor) return;
        contenedor.innerHTML = '';

        var hoyStr = fechaStr(new Date());
        var proximas = EVENTOS
            .filter(function (e) { return e.fecha >= hoyStr; })
            .sort(function (a, b) { return a.fecha.localeCompare(b.fecha); })
            .slice(0, 4);

        if (proximas.length === 0) {
            contenedor.innerHTML = '<p style="font-size:13px;color:var(--pc-texto-suave)">Sin próximas capacitaciones.</p>';
            return;
        }

        proximas.forEach(function (ev) {
            var item = document.createElement('div');
            item.className = 'cal-proxima-item';
            item.innerHTML =
                '<span class="cal-proxima-dot" style="background:' + colorDot(ev) + '"></span>' +
                '<div>' +
                    '<div class="cal-proxima-fecha">' + formatFechaLarga(ev.fecha) + ' · ' + ev.horaInicio + '</div>' +
                    '<div class="cal-proxima-nombre">' + ev.solicitante + '</div>' +
                    '<div class="cal-proxima-tipo">' + ev.tipoCapacitacion + '</div>' +
                '</div>';
            item.addEventListener('click', function () { abrirModal(ev); });
            contenedor.appendChild(item);
        });
    }

    /* ===================== KPIs ===================== */
    function renderKpis() {
    var hoyDate = new Date();
    var mesActual = hoyDate.getMonth();
    var añoActual = hoyDate.getFullYear();

    var delMes = EVENTOS.filter(function (e) {
        var p = e.fecha.split('-');
        return parseInt(p[1], 10) - 1 === mesActual && parseInt(p[0], 10) === añoActual;
    });

    // Total general
    var el = function (id) { return document.getElementById(id); };
    if (el('kpiTotal')) el('kpiTotal').textContent = delMes.length;

    // Genera una tarjeta por cada tipo de inmueble que exista en el catálogo
    var kpiRow = document.getElementById('kpiRow');
    if (!kpiRow) return;
    kpiRow.innerHTML = '';

    // Obtiene los tipos únicos presentes en COLORES_BD (viene del catálogo real)
    var tipos = Object.keys(COLORES_BD);

    tipos.forEach(function (nombreTipo) {
        var color = colorPorTipoInmueble(nombreTipo);
        var cantidad = delMes.filter(function (e) {
            return e.tipoInmueble === nombreTipo;
        }).length;

        var div = document.createElement('div');
        div.className = 'cal-kpi-mini';
        div.innerHTML =
            '<i class="ti ti-building" style="color:' + color + '"></i>' +
            '<div>' +
                '<span class="cal-kpi-num" style="color:' + color + '">' + cantidad + '</span>' +
                '<span class="cal-kpi-lbl">' + nombreTipo + '</span>' +
            '</div>';
        kpiRow.appendChild(div);
    });
}
    /* ===================== RENDERIZADO COMPLETO ===================== */
    function renderTodo() {
        if (vista === 'semana') {
            renderSemana();
        } else {
            renderMes();
        }
        renderMini();
        renderProximas();
        renderKpis();
    }

    /* ===================== MODAL DE DETALLE ===================== */
    var modal = document.getElementById('modalDetalle');

    function abrirModal(ev) {
        if (!modal) return;
        document.getElementById('modalTitulo').textContent      = ev.solicitante;
        document.getElementById('modalFecha').textContent       = formatFechaLarga(ev.fecha);
        document.getElementById('modalHorario').textContent     = ev.horaInicio + ' – ' + ev.horaFin + ' hrs.';
        document.getElementById('modalSolicitante').textContent = ev.solicitante;
        document.getElementById('modalDireccion').textContent   = ev.direccion || '—';
        document.getElementById('modalTipoCapacitacion').textContent = ev.tipoCapacitacion;
        document.getElementById('modalTipoInmueble').textContent     = ev.tipoInmueble;
        document.getElementById('modalInstructor').textContent     = ev.instructor;
        document.getElementById('modalInstructoresApoyo').textContent = ev.instructoresApoyo;
        document.getElementById('modalEstatus').textContent          = ev.estatus;
        document.getElementById('modalPc').textContent = ev.pc || '—';
        document.getElementById('modalOp').textContent = ev.op || '—';
        document.getElementById('modalContacto').textContent = ev.contacto || '—';
        document.getElementById('modalTelefono').textContent = ev.telefono || '—';
        document.getElementById('modalObservaciones').textContent = ev.observaciones || '—';
        document.getElementById('modalRegistradoPor').textContent =
        (ev.registradoPor || '—') + ' - ' + (ev.fechaRegistro ? new Date(ev.fechaRegistro).toLocaleString() : '');
        document.getElementById('modalModificadoPor').textContent =
        (ev.modificadoPor || '—') + ' - ' + (ev.fechaModificacion ? new Date(ev.fechaModificacion).toLocaleString() : '');

        var total = (ev.hombres || 0) + (ev.mujeres || 0) + (ev.ninos || 0);
        document.getElementById('modalBeneficiarios').textContent =
        (ev.hombres || 0) + ' hombres · ' + (ev.mujeres || 0) + ' mujeres · ' +
        (ev.ninos || 0) + ' niños (Total: ' + total + ')';

        var modalArchivo = document.getElementById('modalArchivo');
         if (ev.archivoLista) {
             modalArchivo.innerHTML = '<a href="/' + ev.archivoLista +
             '" target="_blank" style="color:var(--pc-rojo); text-decoration:underline;">Ver PDF</a>';
        } else {
             modalArchivo.textContent = 'Sin archivo adjunto';
        }

        var dot = document.getElementById('modalDot');
        if (dot) dot.style.background = colorDot(ev);

        // Link de editar — ajusta la URL cuando tengas el endpoint de edición
        var linkEditar = document.getElementById('modalLinkEditar');
        if (linkEditar) {
            linkEditar.onclick = function () {
                window.location.href = '/capacitaciones/editar/' + ev.id;
            };
        }
        modal.classList.add('activo');
    }

    function cerrarModal() {
        if (modal) modal.classList.remove('activo');
    }

    var btnCerrar  = document.getElementById('btnCerrarModal');
    var btnCerrar2 = document.getElementById('btnCerrarModal2');
    if (btnCerrar)  btnCerrar.addEventListener('click',  cerrarModal);
    if (btnCerrar2) btnCerrar2.addEventListener('click', cerrarModal);
    if (modal) modal.addEventListener('click', function (e) {
        if (e.target === modal) cerrarModal();
    });
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') cerrarModal();
    });

    /* ===================== NAVEGACIÓN ===================== */
    document.getElementById('btnAnterior').addEventListener('click', function () {
        if (vista === 'semana') {
            fechaActual.setDate(fechaActual.getDate() - 7);
        } else {
            fechaActual.setMonth(fechaActual.getMonth() - 1);
        }
        fechaMiniActual = new Date(fechaActual);
        renderTodo();
    });

    document.getElementById('btnSiguiente').addEventListener('click', function () {
        if (vista === 'semana') {
            fechaActual.setDate(fechaActual.getDate() + 7);
        } else {
            fechaActual.setMonth(fechaActual.getMonth() + 1);
        }
        fechaMiniActual = new Date(fechaActual);
        renderTodo();
    });

    document.getElementById('btnHoy').addEventListener('click', function () {
        fechaActual = new Date();
        fechaMiniActual = new Date();
        renderTodo();
    });

    document.getElementById('miniAnterior').addEventListener('click', function () {
        fechaMiniActual.setMonth(fechaMiniActual.getMonth() - 1);
        renderMini();
    });

    document.getElementById('miniSiguiente').addEventListener('click', function () {
        fechaMiniActual.setMonth(fechaMiniActual.getMonth() + 1);
        renderMini();
    });

    /* ===================== CAMBIO DE VISTA ===================== */
    document.getElementById('btnVistaSemana').addEventListener('click', function () {
        vista = 'semana';
        document.getElementById('vistaSemana').style.display = '';
        document.getElementById('vistaMes').style.display    = 'none';
        this.classList.add('active');
        document.getElementById('btnVistaMes').classList.remove('active');
        renderSemana();
    });

    document.getElementById('btnVistaMes').addEventListener('click', function () {
        vista = 'mes';
        document.getElementById('vistaSemana').style.display = 'none';
        document.getElementById('vistaMes').style.display    = '';
        this.classList.add('active');
        document.getElementById('btnVistaSemana').classList.remove('active');
        renderMes();
    });

    /* ===================== INIT ===================== */
    renderTodo();

});

function obtenerTipoKey(nombre) {
    switch (nombre) {
        case "Escuela":
            return "escuela";

        case "Empresa":
            return "empresa";

        case "Dependencia de Gobierno":
            return "gobierno";

        case "Unidad Habitacional":
            return "unidad";

        default:
            return "default";
    }
}

