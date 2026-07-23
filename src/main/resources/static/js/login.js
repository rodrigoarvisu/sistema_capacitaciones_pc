document.addEventListener("DOMContentLoaded", () => {
     const notyf = new Notyf({
        duration: 3000,
        position: {
            x: "right",
            y: "top"
        }
    });

    const params = new URLSearchParams(window.location.search);

    if (params.has("error")) {
        notyf.error("Usuario o contraseña incorrectos.");
    }

    if (params.has("registrado")) {
        notyf.success("Cuenta creada correctamente.");
    }

    if (params.has("logout")) {
        notyf.success("Sesión cerrada correctamente.");
    }
});