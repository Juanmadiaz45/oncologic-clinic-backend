document.addEventListener("DOMContentLoaded", function () {
    // Form validation
    const form = document.querySelector('.needs-validation');

    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }

        form.classList.add('was-validated');
    }, false);

    // Real-time validation for better UX
    const fields = form.querySelectorAll('input, select, textarea');
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

    // Password strength indicator (optional)
    const passwordInput = document.getElementById('password');
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            const password = this.value;
            const strengthIndicator = document.getElementById('password-strength');

            if (!strengthIndicator) return;

            // Simple strength check
            if (password.length === 0) {
                strengthIndicator.textContent = '';
            } else if (password.length < 6) {
                strengthIndicator.textContent = 'Débil';
                strengthIndicator.className = 'text-danger';
            } else if (password.length < 10) {
                strengthIndicator.textContent = 'Moderada';
                strengthIndicator.className = 'text-warning';
            } else {
                strengthIndicator.textContent = 'Fuerte';
                strengthIndicator.className = 'text-success';
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