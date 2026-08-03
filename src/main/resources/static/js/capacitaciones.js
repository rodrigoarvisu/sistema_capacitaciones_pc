/* form-capacitacion.js
   - Actualiza el panel de resumen lateral en tiempo real
   - Limpia el formulario con notificación
   - Muestra notificación al guardar exitosamente */

document.addEventListener('DOMContentLoaded', function () {

    function val(id) {
        var el = document.getElementById(id);
        if (!el) return '—';
        var v = el.value.trim();
        return v || '—';
    }

    function selectText(id) {
        var el = document.getElementById(id);
        if (!el) return '—';
        var v = el.options[el.selectedIndex];
        return (v && v.value) ? v.text : '—';
    }

    function formatFecha(raw) {
        if (!raw || raw === '—') return '—';
        var parts = raw.split('-');
        if (parts.length !== 3) return raw;
        var meses = ['enero','febrero','marzo','abril','mayo','junio',
                     'julio','agosto','septiembre','octubre','noviembre','diciembre'];
        return parts[2] + ' de ' + meses[parseInt(parts[1], 10) - 1] + ' de ' + parts[0];
    }

    function set(id, texto) {
        var el = document.getElementById(id);
        if (el) el.textContent = texto;
    }

    function update() {
        set('resumen-pc', val('pc'));
        set('resumen-op', val('op'));
        set('resumen-solicitante', val('nombreSolicitante'));
        set('resumen-inmueble',    selectText('tipoInmueble'));
        set('resumen-tipo',        selectText('tipoCapacitacion'));
        set('resumen-fecha',       formatFecha(val('fecha')));
        set('resumen-instructores', selectText('instructor'));
        set('resumen-contacto', val('contacto'));
        set('resumen-telefono', val('telefono'));
        set('resumen-estatus',     selectText('estatus'));
        set('resumen-beneficiarios', actualizarTotalBeneficiarios());

        var hi = val('horaInicio');
        var hf = val('horaFin');
        set('resumen-horario',
            (hi !== '—' && hf !== '—') ? hi + ' – ' + hf + ' hrs.' :
            (hi !== '—') ? hi + ' hrs.' : '—');
    }

    // --- Resumen en vivo: escucha cambios en los campos ---
    var ids = ['pc', 'op', 'nombreSolicitante','tipoInmueble','tipoCapacitacion',
               'fecha','horaInicio','horaFin', 'instructor', 'contacto', 
               'telefono', 'estatus', 'hombres', 'mujeres', 'ninos'];

    ids.forEach(function (id) {
        var el = document.getElementById(id);
        if (el) {
            el.addEventListener('change', update);
            el.addEventListener('input', update);
        }
    });

    // --- Botón limpiar ---
    var btnLimpiar = document.getElementById('btn-limpiar');
    if (btnLimpiar) {
        btnLimpiar.addEventListener('click', function () {
            var form = document.querySelector('.fc-form');
            if (form) form.reset();
            update();

            if (typeof Notyf !== 'undefined') {
                var notyfLimpiar = new Notyf();
                notyfLimpiar.success('Se limpiaron todos los campos.');
            }
        });
    }

    function actualizarTotalBeneficiarios() {

    const hombres = parseInt(document.getElementById("hombres").value) || 0;
    const mujeres = parseInt(document.getElementById("mujeres").value) || 0;
    const ninos = parseInt(document.getElementById("ninos").value) || 0;

    const total = hombres + mujeres + ninos;

    document.getElementById("totalBeneficiarios").textContent = total;

    return total;
}

["hombres","mujeres","ninos"].forEach(id => {
    document.getElementById(id).addEventListener("input", actualizarTotalBeneficiarios);
});

actualizarTotalBeneficiarios();

    // --- Notificación de guardado exitoso (vía query param ?guardado=true) ---
    var params = new URLSearchParams(window.location.search);
    if (params.get('guardado') === 'true') {
        if (typeof Notyf !== 'undefined') {
            var notyfGuardado = new Notyf({
                duration: 3500,
                position: { x: 'right', y: 'top' }
            });
            notyfGuardado.success('Capacitación guardada correctamente.');
        } else {
            alert('Capacitación guardada correctamente.');
        }
        window.history.replaceState({}, document.title, window.location.pathname);
    }

    // --- Inicializa el resumen con los valores actuales (por si vienen precargados) ---
    update();

});

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

const inputArchivo = document.getElementById("archivoLista");
const nombreArchivo = document.getElementById("nombreArchivo");

if (inputArchivo) {
    inputArchivo.addEventListener("change", function () {
        if (this.files.length > 0) {
            nombreArchivo.textContent = this.files[0].name;
            nombreArchivo.classList.add("success");
        } else {
            nombreArchivo.textContent = "Ningún archivo seleccionado";
            nombreArchivo.classList.remove("success");
        }
    });
}

const btnReemplazar = document.getElementById("btnReemplazar");
const archivoExistente = document.getElementById("archivoExistente");
const archivoNuevo = document.getElementById("archivoNuevo");

if (btnReemplazar) {
    btnReemplazar.addEventListener("click", function () {
        archivoExistente.style.display = "none";
        archivoNuevo.style.display = "block";
        document.getElementById("archivoLista").click();
    });
}