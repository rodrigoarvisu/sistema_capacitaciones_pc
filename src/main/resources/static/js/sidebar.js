document.addEventListener("DOMContentLoaded", function () {
    const btnSidebar = document.getElementById("btnSidebar");

    if(btnSidebar) {
        btnSidebar.addEventListener("click", function () {
            document.body.classList.toggle("sidebar-hidden");
        });
    }
});