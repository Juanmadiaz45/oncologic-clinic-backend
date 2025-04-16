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

    // Validación especial para número de identificación (ejemplo)
    const idNumberInput = document.getElementById('idNumber');
    if (idNumberInput) {
        idNumberInput.addEventListener('input', function() {
            // Validación básica de longitud
            if (this.value.length < 6) {
                this.setCustomValidity("El número de identificación debe tener al menos 6 caracteres");
            } else {
                this.setCustomValidity("");
            }
        });
    }
});