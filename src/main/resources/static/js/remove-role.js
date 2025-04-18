document.addEventListener("DOMContentLoaded", function() {
    const userSelect = document.getElementById("user");
    const userRolesContainer = document.getElementById("userRolesContainer");
    const submitBtn = document.getElementById("submitBtn");

    userSelect.addEventListener("change", function() {
        const userId = this.value;

        if (!userId) {
            userRolesContainer.innerHTML = `
                <div class="col-12 text-muted">
                    Seleccione un usuario para ver sus roles
                </div>
            `;
            submitBtn.disabled = true;
            return;
        }

        fetch(`/users/roles/user-roles?userId=${userId}`)
            .then(response => response.json())
            .then(roles => {
                if (roles.length === 0) {
                    userRolesContainer.innerHTML = `
                        <div class="col-12 text-muted">
                            Este usuario no tiene roles asignados
                        </div>
                    `;
                    submitBtn.disabled = true;
                } else {
                    let html = '';
                    roles.forEach(role => {
                        html += `
                            <div class="col-md-4 mb-2">
                                <div class="form-check">
                                    <input class="form-check-input" 
                                           type="checkbox" 
                                           id="role_${role.id}" 
                                           value="${role.id}" 
                                           name="roleIds">
                                    <label class="form-check-label" 
                                           for="role_${role.id}">
                                           ${role.name}
                                    </label>
                                </div>
                            </div>
                        `;
                    });
                    userRolesContainer.innerHTML = html;
                    submitBtn.disabled = false;
                }
            })
            .catch(error => {
                console.error("Error al obtener roles:", error);
                userRolesContainer.innerHTML = `
                    <div class="col-12 text-danger">
                        Error al cargar los roles del usuario
                    </div>
                `;
                submitBtn.disabled = true;
            });
    });

    const form = document.querySelector("form");
    form.addEventListener("submit", function(event) {
        const selectedRoles = document.querySelectorAll("input[name='roleIds']:checked");
        if (selectedRoles.length === 0) {
            event.preventDefault();
            alert("Debe seleccionar al menos un rol para eliminar");
        }
    });
});