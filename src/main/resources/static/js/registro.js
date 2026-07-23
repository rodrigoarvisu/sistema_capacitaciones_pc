document.addEventListener("DOMContentLoaded", () => {

    const notyf = new Notyf({
        duration: 3000,
        position: {
            x: "right",
            y: "top"
        }
    });

    const error = document.getElementById("mensajeError");

    if (error) {
        notyf.error(error.textContent);
    }

});