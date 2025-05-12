document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('.needs-validation');

    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);

    const fields = form.querySelectorAll('input, textarea');
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

    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('successMessage') || urlParams.has('error')) {
        const toast = new bootstrap.Toast(document.getElementById('successToast'));
        toast.show();
    }
});