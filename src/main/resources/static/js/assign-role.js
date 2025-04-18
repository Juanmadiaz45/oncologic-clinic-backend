document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    form.addEventListener("submit", function (event) {
        const selectedUser = document.querySelector("#user").value;
        const selectedRoles = document.querySelectorAll("input[name='roleIds']:checked");

        if (!selectedUser) {
            event.preventDefault();
            alert("Debe seleccionar un usuario");
            return;
        }

        if (selectedRoles.length === 0) {
            event.preventDefault();
            alert("Debe seleccionar al menos un rol");
        }
    });
});