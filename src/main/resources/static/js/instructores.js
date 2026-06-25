/* instructores.js
   - Modal nuevo/editar instructor
   - Selección de instructor en el catálogo
   - Actualiza panel derecho con datos del instructor seleccionado
   - Actualiza el form de inactivar con el id correcto
   - Búsqueda y filtro por estatus
   - Switch visual de estatus en el modal
   - Notificación de guardado (?guardado=true) */

document.addEventListener('DOMContentLoaded', function () {

    var notyf = (typeof Notyf !== 'undefined')
        ? new Notyf({ duration: 3500, position: { x: 'right', y: 'top' } })
        : null;

    function notify(msg, tipo) {
        if (notyf) {
            tipo === 'error' ? notyf.error(msg) : notyf.success(msg);
        } else {
            alert(msg);
        }
    }

    /* ===================== REFERENCIAS AL DOM ===================== */
    var modal          = document.getElementById('modalInstructor');
    var modalTitulo    = document.getElementById('modalTitulo');
    var formInstructor = document.getElementById('formInstructor');
    var btnNuevo       = document.getElementById('btn-nuevo-instructor');
    var btnEditar      = document.getElementById('btn-editar-instructor');
    var btnCerrar      = document.getElementById('btnCerrarModal');
    var btnCancelar    = document.getElementById('btnCancelarModal');

    var inputNombre    = document.getElementById('nombre');
    var inputTelefono  = document.getElementById('telefono');
    var inputId        = formInstructor ? formInstructor.querySelector('input[name="id"]') : null;
    var checkActivo    = document.getElementById('activo');
    var textoEstatus   = document.getElementById('estatusTexto');

    var detalleNombre      = document.getElementById('detalleNombre');
    var detalleTelefono    = document.getElementById('detalleTelefono');
    var detalleEstatus     = document.getElementById('detalleEstatus');
    var btnInactivarLabel  = document.getElementById('btnInactivarLabel');
    var formInactivar      = document.getElementById('formInactivar');

    var lista          = document.getElementById('listaInstructores');
    var inputBuscar    = document.getElementById('buscarInstructor');
    var selectFiltro   = document.getElementById('filtroEstatus');

    var instructorSeleccionado = null;

    /* ===================== SWITCH DE ESTATUS ===================== */
    function actualizarSwitch() {
        if (textoEstatus && checkActivo) {
            textoEstatus.textContent = checkActivo.checked ? 'Activo' : 'Inactivo';
        }
    }
    if (checkActivo) checkActivo.addEventListener('change', actualizarSwitch);

    /* ===================== MODAL ===================== */
    function abrirModal(modo, datos) {
        if (!modal) return;

        if (modalTitulo) {
            modalTitulo.textContent = (modo === 'editar') ? 'Editar instructor' : 'Nuevo instructor';
        }

        if (modo === 'nuevo') {
            if (formInstructor) formInstructor.reset();
            if (inputId) inputId.value = '';
            if (checkActivo) checkActivo.checked = true;
        } else if (modo === 'editar' && datos) {
            if (inputNombre)   inputNombre.value   = datos.nombre    || '';
            if (inputTelefono) inputTelefono.value = datos.telefono  || '';
            if (inputId)       inputId.value       = datos.id        || '';
            if (checkActivo)   checkActivo.checked = (datos.activo === 'true' || datos.activo === true);
        }

        actualizarSwitch();
        modal.classList.add('activo');
    }

    function cerrarModal() {
        if (modal) modal.classList.remove('activo');
    }

    if (btnNuevo)   btnNuevo.addEventListener('click',   function () { abrirModal('nuevo'); });
    if (btnCerrar)  btnCerrar.addEventListener('click',  cerrarModal);
    if (btnCancelar) btnCancelar.addEventListener('click', cerrarModal);

    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === modal) cerrarModal();
        });
    }

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && modal && modal.classList.contains('activo')) {
            cerrarModal();
        }
    });

    if (btnEditar) {
        btnEditar.addEventListener('click', function () {
            if (instructorSeleccionado) {
                abrirModal('editar', instructorSeleccionado);
            } else {
                notify('Selecciona un instructor primero.', 'error');
            }
        });
    }

    /* ===================== SELECCIÓN EN EL CATÁLOGO ===================== */
    function seleccionarInstructor(item) {
        // Quita selección previa
        lista.querySelectorAll('.ins-item.selected').forEach(function (el) {
            el.classList.remove('selected');
        });
        item.classList.add('selected');

        var nombre   = item.getAttribute('data-nombre')   || '';
        var telefono = item.getAttribute('data-telefono') || '';
        var activo   = item.getAttribute('data-activo') === 'true';
        var id       = item.getAttribute('data-id')       || '';

        instructorSeleccionado = { id: id, nombre: nombre, telefono: telefono, activo: activo };

        fetch(`/instructores/resumen/${id}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("kpiProximas").textContent =
            data.proximas;

            document.getElementById("kpiRealizadasMes").textContent = 
            data.realizadasMes;

            document.getElementById("kpiTotales").textContent = 
            data.totales; 

            document.getElementById("kpiProximaCapacitacion").textContent =
            data.proximaCapacitacion;

            const tablaProximas = 
            document.getElementById("tablaProximas");

            tablaProximas.innerHTML = "";

            data.proximasCapacitaciones.forEach(cap => {
                tablaProximas.innerHTML += `
                <tr>
                    <td>${cap.fecha}</td>
                    <td>${cap.horaInicio.substring(0, 5)} - ${cap.horaFin.substring(0, 5)}</td>
                    <td>${cap.nombreSolicitante}</td>
                    <td>${cap.tipoCapacitacion.nombre}</td>
                    <td>${cap.tipoInmueble.nombre}</td>
                </tr>
                `;
            })


             const tablaHistorial = 
            document.getElementById("tablaHistorial");

            tablaHistorial.innerHTML = "";

            data.historial.forEach(cap => {
                tablaHistorial.innerHTML += `
                <tr>
                    <td>${cap.fecha}</td>
                    <td>${cap.nombreSolicitante}</td>
                    <td>${cap.tipoCapacitacion.nombre}</td>
                    <td>${cap.horaInicio.substring(0, 5)} - ${cap.horaFin.substring(0, 5)}</td>
                    <td>
                    <span class="ins-pill ins-pill-realizada">
                    Realizada
                    </span>
                    </td>
                </tr>
                `;
            })


        }).catch(error => {
            console.error("Error cargando resumen: ", error);
        });

        // Actualiza panel de detalle
        if (detalleNombre)   detalleNombre.textContent   = nombre;
        if (detalleTelefono) detalleTelefono.textContent = telefono;

        if (detalleEstatus) {
            detalleEstatus.textContent = activo ? 'Activo' : 'Inactivo';
            detalleEstatus.className   = 'ins-badge ' + (activo ? 'ins-badge-activo' : 'ins-badge-inactivo');
        }

        if (btnInactivarLabel) {
            btnInactivarLabel.textContent = activo ? 'Inactivar' : 'Activar';
        }

        // Actualiza la action del form de inactivar con el id real
        if (formInactivar && id) {
            formInactivar.action = '/instructores/inactivar/' + id;
        }
    }

    // Selecciona el primer instructor automáticamente al cargar
    if (lista) {
        var primero = lista.querySelector('.ins-item');
        if (primero) seleccionarInstructor(primero);

        lista.addEventListener('click', function (e) {
            var item = e.target.closest('.ins-item');
            if (item) seleccionarInstructor(item);
        });
    }

    /* ===================== BÚSQUEDA Y FILTRO ===================== */
    function aplicarFiltros() {
        if (!lista) return;
        var texto  = inputBuscar  ? inputBuscar.value.trim().toLowerCase()  : '';
        var filtro = selectFiltro ? selectFiltro.value : 'todos';

        lista.querySelectorAll('.ins-item').forEach(function (item) {
            var nombre = (item.getAttribute('data-nombre') || '').toLowerCase();
            var activo = item.getAttribute('data-activo') === 'true';

            var coincideTexto  = nombre.indexOf(texto) !== -1;
            var coincideFiltro =
                filtro === 'todos' ||
                (filtro === 'activo'   && activo) ||
                (filtro === 'inactivo' && !activo);

            item.style.display = (coincideTexto && coincideFiltro) ? 'flex' : 'none';
        });
    }

    if (inputBuscar)  inputBuscar.addEventListener('input',   aplicarFiltros);
    if (selectFiltro) selectFiltro.addEventListener('change', aplicarFiltros);

    /* ===================== NOTIFICACIÓN DE GUARDADO ===================== */
    var params = new URLSearchParams(window.location.search);
    if (params.get('guardado') === 'true') {
        notify('Instructor guardado correctamente.');
        window.history.replaceState({}, document.title, window.location.pathname);
    }

});

const personasPorPagina = 3;
let paginaActual = 1;

const personas = document.querySelectorAll(".ins-item");
const btnPrev = document.getElementById("pagPrev");
const btnNext = document.getElementById("pagNext");
const pagInfo = document.getElementById("pagInfo");

const totalPaginas = Math.ceil(personas.length / personasPorPagina);

function mostrarPagina(pagina) {
    const inicio = (pagina - 1) * personasPorPagina;
    const fin = inicio + personasPorPagina;

    personas.forEach((persona, index) => {
        persona.style.display = index >= inicio && index < fin ? "" : "none";
    });

    pagInfo.textContent = `${paginaActual} / ${totalPaginas}`;

    btnPrev.disabled = paginaActual === 1;
    btnNext.disabled = paginaActual === totalPaginas;
}

btnPrev.addEventListener("click", () => {
    if (paginaActual > 1) {
        paginaActual--;
        mostrarPagina(paginaActual);
    }
});

btnNext.addEventListener("click", () => {
    if (paginaActual < totalPaginas) {
        paginaActual++;
        mostrarPagina(paginaActual);
    }
});

mostrarPagina(paginaActual);