document.addEventListener("DOMContentLoaded", function () {
    const menuToggle = document.getElementById("menuToggle");
    const sidebarMenu = document.getElementById("sidebarMenu");
    const sidebarOverlay = document.getElementById("sidebarOverlay");

    menuToggle.addEventListener("click", () => {
        sidebarMenu.classList.toggle("active");
        sidebarOverlay.classList.toggle("active");
    });

    sidebarOverlay.addEventListener("click", () => {
        sidebarMenu.classList.remove("active");
        sidebarOverlay.classList.remove("active");
    });
});
