document.addEventListener("DOMContentLoaded", function () {
    // Validación de formulario
    const form = document.querySelector('.needs-validation');

    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }

        form.classList.add('was-validated');
    }, false);

    const fields = form.querySelectorAll('input, select');
    fields.forEach(field => {
        field.addEventListener('input', function() {
            if (this.checkValidity()) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            } else {
                this.classList.remove('is-valid');
                this.classList.add('is-invalid');
            }
        });
    });

    const licenseInput = document.getElementById('medicalLicenseNumber');
    if (licenseInput) {
        licenseInput.addEventListener('input', function() {
            if (this.value.length < 6) {
                this.setCustomValidity("La licencia debe tener al menos 6 caracteres");
            } else {
                this.setCustomValidity("");
            }
        });
    }

    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('successMessage') || urlParams.has('error') || document.getElementById('errorMessage')?.textContent) {
        if (typeof bootstrap !== 'undefined') {
            const resultModal = new bootstrap.Modal(document.getElementById('resultModal'));
            resultModal.show();
        }
    }
});

function redirectToDashboard() {
    window.location.href = '/g5/siscom/dashboard';
}