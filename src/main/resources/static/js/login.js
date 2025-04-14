document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    form.addEventListener("submit", function () {
        const button = form.querySelector("button[type='submit']");
        button.disabled = true;
        button.innerHTML = 'Cargando... <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';
    });
});
