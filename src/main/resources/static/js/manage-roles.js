document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("rolesForm");
    const checkboxes = document.querySelectorAll('.role-checkbox');

    checkboxes.forEach(checkbox => {
        checkbox.disabled = false;
    });

    form.addEventListener("submit", function (event) {
        const userRoles = {};

        const userIdToUsername = {};
        document.querySelectorAll("tbody tr[data-user-id]").forEach(row => {
            const userId = row.getAttribute("data-user-id");
            userIdToUsername[userId] = row.getAttribute("data-username");
        });

        checkboxes.forEach(checkbox => {
            const userId = checkbox.name.match(/\[(\d+)]/)[1];
            if (checkbox.checked) {
                if (!userRoles[userId]) {
                    userRoles[userId] = [];
                }
                userRoles[userId].push(checkbox.value);
            }
        });

        const users = Array.from(new Set(
            Array.from(checkboxes).map(cb => cb.name.match(/\[(\d+)]/)[1])
        ));

        const usersWithoutRoles = users.filter(userId => !userRoles[userId]);

        if (usersWithoutRoles.length > 0) {
            event.preventDefault();
            const usernamesWithoutRoles = usersWithoutRoles.map(id => userIdToUsername[id] || `ID ${id}`);
            alert(`Los siguientes usuarios deben tener al menos un rol: ${usernamesWithoutRoles.join(', ')}`);
            return false;
        }

        return true;
    });
});
