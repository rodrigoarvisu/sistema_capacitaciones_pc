document.addEventListener('DOMContentLoaded', function () {
    var notyf = (typeof Notyf !== 'undefined')
    ? new Notyf({ duration: 3500, position: { x: 'right', y: 'top'} })
    : null;

    function notyf(msg, tipo) {
        if (notyf) tipo === 'error' ? notyf.error(msg) : notyf.success(msg);
        else alert(msg);
    }

    var modal = document.getElementById('modalCatalogo');
    var modalTitulo = document.getElementById('modalTitulo');
    var btnCerrar = document.getElementById('btnCerrarModal');

    var FORMS = {
        tipoInmueble: document.getElementById('formTipoInmueble'),
        tipoCapacitacion: document.getElementById('formTipoCapacitacion'),
        estatus: document.getElementById('formEstatus')
    };

    var URLS = {
        tipoInmueble:    '/catalogos/tipoInmueble/guardar',
        tipoCapacitacion: '/catalogos/tipoCapacitacion/guardar',
        estatus:         '/catalogos/estatus/guardar'
    };

    function ocultarForms() {
        Object.values(FORMS).forEach(function (f) { if (f) f.style.display = 'none'; });
    }

    function abrirModal(catalogo, titulo, datos) {
        if(!modal) return;
        if (modalTitulo) modalTitulo.textContent = titulo;
        ocultarForms();

        var form = FORMS[catalogo];
        if(!form) return;
        form.style.display = 'flex';
        form.action = URLS[catalogo];
        form.reset();

        if(catalogo === 'tipoInmueble') {
            document.getElementById('tiInmuebleId').value = datos ? datos.id : '';
            document.getElementById('tiInmuebleNombre').value = datos ? datos.nombre : '';
            var color = datos ? datos.color : '#FACC15';
            document.getElementById('tiInmuebleColor').value   = color;
            document.getElementById('tiInmuebleColorPicker').value = color;
            document.getElementById('tiInmueblePreview').style.background = color;
        }else if (catalogo === 'tipoCapacitacion') {
            document.getElementById('tiCapacitacionId').value    = datos ? datos.id          : '';
            document.getElementById('tiCapacitacionNombre').value = datos ? datos.nombre     : '';
            document.getElementById('tiCapacitacionDesc').value   = datos ? datos.descripcion : '';
 
        } else if (catalogo === 'estatus') {
            document.getElementById('estatusId').value     = datos ? datos.id          : '';
            document.getElementById('estatusNombre').value = datos ? datos.nombre      : '';
            document.getElementById('estatusDesc').value   = datos ? datos.descripcion : '';
        }
        modal.classList.add('activo');
    }

    function cerrarModal() {
        if (modal) modal.classList.remove('activo');
    }
 
    if (btnCerrar) btnCerrar.addEventListener('click', cerrarModal);
 
    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === modal) cerrarModal();
        });
        modal.addEventListener('click', function (e) {
            if (e.target.closest('[data-cerrar]')) cerrarModal();
        });
    }
 
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') cerrarModal();
    });
 
    /* ===================== BOTONES NUEVO ===================== */
    document.querySelectorAll('.cat-btn-nuevo').forEach(function (btn) {
        btn.addEventListener('click', function () {
            abrirModal(btn.getAttribute('data-catalogo'), btn.getAttribute('data-titulo'), null);
        });
    });
 
    /* ===================== BOTONES EDITAR ===================== */
    document.querySelectorAll('.cat-btn-editar').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var cat = btn.getAttribute('data-catalogo');
            var titulo = btn.getAttribute('data-titulo');
            var datos = {
                id:          btn.getAttribute('data-id'),
                nombre:      btn.getAttribute('data-nombre'),
                descripcion: btn.getAttribute('data-descripcion') || '',
                color:       btn.getAttribute('data-color') || '#FACC15'
            };
            abrirModal(cat, titulo, datos);
        });
    });
 
    /* ===================== COLOR PICKER (TipoInmueble) ===================== */
    var colorPicker  = document.getElementById('tiInmuebleColorPicker');
    var colorTexto   = document.getElementById('tiInmuebleColor');
    var colorPreview = document.getElementById('tiInmueblePreview');
 
    function esHexValido(hex) { return /^#[0-9A-Fa-f]{6}$/.test(hex); }
 
    if (colorPicker) {
        colorPicker.addEventListener('input', function () {
            var c = colorPicker.value;
            colorTexto.value = c;
            colorPreview.style.background = c;
        });
    }
 
    if (colorTexto) {
        colorTexto.addEventListener('input', function () {
            var c = colorTexto.value.trim();
            if (!c.startsWith('#')) { c = '#' + c; colorTexto.value = c; }
            if (esHexValido(c)) {
                colorPicker.value = c;
                colorPreview.style.background = c;
            }
        });
    }
 
    /* ===================== NOTIFICACIÓN DE GUARDADO ===================== */
    var params = new URLSearchParams(window.location.search);
    if (params.get('guardado') === 'true') {
        notify('Registro guardado correctamente.');
        window.history.replaceState({}, document.title, window.location.pathname);
    }
    if (params.get('eliminado') === 'true') {
        notify('Registro eliminado.');
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});