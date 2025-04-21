document.querySelector('form[th\\:action="@{/roles/delete}"]')?.addEventListener('submit', function (e) {
    const hasUsers = document.querySelector('.user-list') !== null;

    if (hasUsers) {
        e.preventDefault();
        alert('No puede eliminar el rol mientras tenga usuarios asociados');
        return;
    }

    const roleName = this.querySelector('input[name="roleId"]').getAttribute('th:value');
    const confirmation = this.querySelector('input[name="confirmation"]').value;

    if (confirmation !== roleName) {
        e.preventDefault();
        alert('Debes escribir exactamente el nombre del rol para confirmar');
    }
});