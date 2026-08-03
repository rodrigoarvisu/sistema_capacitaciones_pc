document.addEventListener('DOMContentLoaded', function () {

    const userToggle = document.getElementById("userToggle");
    const userDropdown = document.getElementById("userDropdown");

    if (userToggle) {
        userToggle.addEventListener("click", (e) => {
            e.stopPropagation();
            userDropdown.classList.toggle("show");
            userToggle.classList.toggle("active");
        });

        document.addEventListener("click", () => {
            userDropdown.classList.remove("show");
            userToggle.classList.remove("active");
        });
    }

    function configurarModal(idModal, idBtnAbrir, idBtnCerrar, idBtnCancelar) {
        var modal = document.getElementById(idModal);
        var btnAbrir = document.getElementById(idBtnAbrir);
        var btnCerrar = document.getElementById(idBtnCerrar);
        var btnCancelar = document.getElementById(idBtnCancelar);
        if (!modal) return;

        if (btnAbrir) {
            btnAbrir.addEventListener("click", function (e) {
                e.preventDefault();
                modal.classList.add("activo");
                if (userDropdown) userDropdown.classList.remove("show");
            });
        }
        [btnCerrar, btnCancelar].forEach(function (btn) {
            if (btn) btn.addEventListener("click", function () {
                modal.classList.remove("activo");
            });
        });
        modal.addEventListener("click", function (e) {
            if (e.target === modal) modal.classList.remove("activo");
        });
    }

    configurarModal("modalPerfil", "btnAbrirPerfil", "btnCerrarPerfil", "btnCancelarPerfil");
    configurarModal("modalPassword", "btnAbrirPassword", "btnCerrarPassword", "btnCancelarPassword");

    var params = new URLSearchParams(window.location.search);
    if (typeof Notyf !== 'undefined') {
        var notyf = new Notyf({ duration: 3500, position: { x: 'right', y: 'top' } });
        if (params.get('perfilActualizado') === 'true') notyf.success('Perfil actualizado correctamente.');
        if (params.get('passwordActualizada') === 'true') notyf.success('Contraseña actualizada correctamente.');
        if (params.get('errorPerfil') === 'usuario_existe') notyf.error('Ese nombre de usuario ya está en uso.');
        if (params.get('errorPassword') === 'actual_incorrecta') notyf.error('La contraseña actual no es correcta.');
        if (params.get('errorPassword') === 'no_coincide') notyf.error('Las contraseñas nuevas no coinciden.');
    }
});