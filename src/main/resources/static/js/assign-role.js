document.addEventListener("DOMContentLoaded", function () {
    const userSelect = document.querySelector("#user");
    const form = document.querySelector("form");

    function updateRoles() {
        const userId = parseInt(userSelect.value);

        if (!userId) {
            document.querySelectorAll("input[name='roleIds']").forEach(checkbox => {
                checkbox.checked = false;
                checkbox.disabled = false;
            });
            return;
        }

        const userRoles = userRolesMap[userId] || [];

        document.querySelectorAll("input[name='roleIds']").forEach(checkbox => {
            const roleId = parseInt(checkbox.value);

            if (userRoles.includes(roleId)) {
                checkbox.checked = true;
                checkbox.disabled = true;
            } else {
                checkbox.checked = false;
                checkbox.disabled = false;
            }
        });
    }

    userSelect.addEventListener("change", updateRoles);

    form.addEventListener("submit", function(event) {
        const selectedUser = userSelect.value;

        if (!selectedUser) {
            event.preventDefault();
            alert("Debe seleccionar un usuario");
            return;
        }
    });

    updateRoles();
});