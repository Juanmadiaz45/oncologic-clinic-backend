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

    // Validación en tiempo real
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

    // Validación especial para licencia médica (ejemplo)
    const licenseInput = document.getElementById('medicalLicenseNumber');
    if (licenseInput) {
        licenseInput.addEventListener('input', function() {
            // Puedes añadir validación personalizada aquí
            if (this.value.length < 6) {
                this.setCustomValidity("La licencia debe tener al menos 6 caracteres");
            } else {
                this.setCustomValidity("");
            }
        });
    }
});