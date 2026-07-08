document.addEventListener("DOMContentLoaded", function () {

    const notyf = new Notyf({
        duration: 3000,
        position: { x: "right", y: "top" }
    });

    // Confirmación de eliminación
    document.querySelectorAll(".btn-eliminar").forEach(function(btn){

        btn.addEventListener("click", function(e){

            e.preventDefault();

            const form = this.closest("form");

            Swal.fire({
                title: "¿Eliminar capacitación?",
                text: "Esta acción no se puede deshacer.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#b24040",
                cancelButtonColor: "#6c757d",
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar"
            }).then((result)=>{

                if(result.isConfirmed){
                    form.submit();
                }

            });

        });

    });

    // Notificaciones
    const params = new URLSearchParams(window.location.search);

    if(params.get("eliminado") === "true"){
        notyf.success("Capacitación eliminada correctamente.");
        history.replaceState({}, document.title, window.location.pathname);
    }

    if(params.get("noEliminar") === "true"){
        notyf.error("No se pudo eliminar la capacitación.");
        history.replaceState({}, document.title, window.location.pathname);
    }

});