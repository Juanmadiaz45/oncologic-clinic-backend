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

    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('successMessage')) {
        const toastBody = document.querySelector('.toast-body');
        if (toastBody) {
            toastBody.textContent = urlParams.get('successMessage');
            const successToast = new bootstrap.Toast(document.getElementById('successToast'));
            successToast.show();
        }
    }
});
