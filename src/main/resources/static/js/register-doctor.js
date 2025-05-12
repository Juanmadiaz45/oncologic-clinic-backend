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

    const successMessage = document.getElementById('successMessage')?.textContent;
    const errorMessage = document.getElementById('errorMessage')?.textContent;

    if (successMessage || errorMessage) {
        const resultModal = new bootstrap.Modal(document.getElementById('resultModal'));
        const modalTitle = document.querySelector('#resultModal .modal-title');
        const modalHeader = document.querySelector('#resultModal .modal-header');
        const modalMessage = document.querySelector('#resultModal #modalMessage');
        const modalCloseBtn = document.querySelector('#resultModal .btn-close');
        const modalAcceptBtn = document.querySelector('#resultModal .btn-primary');

        if (successMessage) {
            modalHeader.className = 'modal-header bg-success text-white';
            modalTitle.innerHTML = '<i class="bi bi-check-circle-fill me-2"></i>Registro Exitoso';
            modalMessage.textContent = successMessage;

            const redirect = () => window.location.href = '/g5/siscom/dashboard';

            modalCloseBtn.addEventListener('click', redirect);

            modalAcceptBtn.addEventListener('click', redirect);

            document.getElementById('resultModal').addEventListener('hidden.bs.modal', redirect);

            setTimeout(redirect, 3000);

        } else if (errorMessage) {
            modalHeader.className = 'modal-header bg-danger text-white';
            modalTitle.innerHTML = '<i class="bi bi-exclamation-triangle-fill me-2"></i>Error en el registro';
            modalMessage.textContent = errorMessage;
        }

        resultModal.show();
    }

    function refreshSpecialities() {
        fetch('/users/register/doctor/specialities')
            .then(response => response.text())
            .then(html => {
                const container = document.querySelector('.specialities-container');
                if (container) {
                    container.innerHTML = new DOMParser()
                        .parseFromString(html, 'text/html')
                        .querySelector('.specialities-container').innerHTML;
                }
            })
            .catch(error => console.error('Error refreshing specialities:', error));
    }
});
