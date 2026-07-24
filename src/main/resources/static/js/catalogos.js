document.addEventListener('DOMContentLoaded', function () {

    /* ===================== NOTYF ===================== */
    var notyf = (typeof Notyf !== 'undefined')
        ? new Notyf({
            duration: 3500,
            position: { x: 'right', y: 'top' }
        })
        : null;

    function notify(msg, tipo) {
        if (notyf) {
            if (tipo === 'error') {
                notyf.error(msg);
            } else {
                notyf.success(msg);
            }
        } else {
            alert(msg);
        }
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
        tipoInmueble: '/catalogos/tipoInmueble/guardar',
        tipoCapacitacion: '/catalogos/tipoCapacitacion/guardar',
        estatus: '/catalogos/estatus/guardar'
    };

    /* ===================== SWEETALERT ELIMINAR ===================== */

    document.querySelectorAll('.cat-btn-eliminar').forEach(function (btn) {

        btn.addEventListener('click', function (e) {

            e.preventDefault();

            const form = this.closest('form');

            Swal.fire({
                title: '¿Eliminar registro?',
                text: 'Esta acción no se puede deshacer.',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar',
                confirmButtonColor: '#b24040',
                cancelButtonColor: '#6c757d'
            }).then((result) => {

                if (result.isConfirmed) {
                    form.submit();
                }

            });

        });

    });

    function ocultarForms() {
        Object.values(FORMS).forEach(function (f) {
            if (f) f.style.display = 'none';
        });
    }

    function abrirModal(catalogo, titulo, datos) {

        if (!modal) return;

        modalTitulo.textContent = titulo;

        ocultarForms();

        var form = FORMS[catalogo];

        form.style.display = 'flex';
        form.action = URLS[catalogo];
        form.reset();

        if (catalogo === 'tipoInmueble') {

            document.getElementById('tiInmuebleId').value = datos ? datos.id : '';
            document.getElementById('tiInmuebleNombre').value = datos ? datos.nombre : '';

            var color = datos ? datos.color : '#FACC15';

            document.getElementById('tiInmuebleColor').value = color;
            document.getElementById('tiInmuebleColorPicker').value = color;
            document.getElementById('tiInmueblePreview').style.background = color;

        } else if (catalogo === 'tipoCapacitacion') {

            document.getElementById('tiCapacitacionId').value = datos ? datos.id : '';
            document.getElementById('tiCapacitacionNombre').value = datos ? datos.nombre : '';
            document.getElementById('tiCapacitacionDesc').value = datos ? datos.descripcion : '';

        } else if (catalogo === 'estatus') {

            document.getElementById('estatusId').value = datos ? datos.id : '';
            document.getElementById('estatusNombre').value = datos ? datos.nombre : '';
            document.getElementById('estatusDesc').value = datos ? datos.descripcion : '';

        }

        modal.classList.add('activo');
    }

    function cerrarModal() {
        modal.classList.remove('activo');
    }

    if (btnCerrar)
        btnCerrar.addEventListener('click', cerrarModal);

    if (modal) {

        modal.addEventListener('click', function (e) {

            if (e.target === modal)
                cerrarModal();

            if (e.target.closest('[data-cerrar]'))
                cerrarModal();

        });

    }

    document.addEventListener('keydown', function (e) {

        if (e.key === 'Escape')
            cerrarModal();

    });

    /* ===================== BOTONES NUEVO ===================== */

    document.querySelectorAll('.cat-btn-nuevo').forEach(function (btn) {

        btn.addEventListener('click', function () {

            abrirModal(
                btn.getAttribute('data-catalogo'),
                btn.getAttribute('data-titulo'),
                null
            );

        });

    });

    /* ===================== BOTONES EDITAR ===================== */

    document.querySelectorAll('.cat-btn-editar').forEach(function (btn) {

        btn.addEventListener('click', function () {

            abrirModal(
                btn.getAttribute('data-catalogo'),
                btn.getAttribute('data-titulo'),
                {
                    id: btn.getAttribute('data-id'),
                    nombre: btn.getAttribute('data-nombre'),
                    descripcion: btn.getAttribute('data-descripcion') || '',
                    color: btn.getAttribute('data-color') || '#FACC15'
                }
            );

        });

    });

    /* ===================== COLOR PICKER ===================== */

    var colorPicker = document.getElementById('tiInmuebleColorPicker');
    var colorTexto = document.getElementById('tiInmuebleColor');
    var colorPreview = document.getElementById('tiInmueblePreview');

    function esHexValido(hex) {
        return /^#[0-9A-Fa-f]{6}$/.test(hex);
    }

    if (colorPicker) {

        colorPicker.addEventListener('input', function () {

            colorTexto.value = this.value;
            colorPreview.style.background = this.value;

        });

    }

    if (colorTexto) {

        colorTexto.addEventListener('input', function () {

            var c = this.value.trim();

            if (!c.startsWith('#')) {
                c = '#' + c;
                this.value = c;
            }

            if (esHexValido(c)) {

                colorPicker.value = c;
                colorPreview.style.background = c;

            }

        });

    }

    /* ===================== NOTIFICACIONES ===================== */

    var params = new URLSearchParams(window.location.search);

    if (params.get('guardado') === 'true') {
        notify('Registro guardado correctamente.');
    }

    if (params.get('eliminado') === 'true') {
        notify('Registro eliminado.');
    }

    if (params.get('noEliminar') === 'true') {
        notify('No se puede eliminar porque este registro está siendo utilizado.', 'error');
    }

    if (
        params.get('guardado') === 'true' ||
        params.get('eliminado') === 'true' ||
        params.get('noEliminar') === 'true'
    ) {
        window.history.replaceState({}, document.title, window.location.pathname);
    }

    const userToggle = document.getElementById("userToggle");
const userDropdown = document.getElementById("userDropdown");

if(userToggle){

    userToggle.addEventListener("click",(e)=>{

        e.stopPropagation();

        userDropdown.classList.toggle("show");

        userToggle.classList.toggle("active");

    });

    document.addEventListener("click",()=>{

        userDropdown.classList.remove("show");

        userToggle.classList.remove("active");

    });

}

});