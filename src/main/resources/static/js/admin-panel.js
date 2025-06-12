document.addEventListener('DOMContentLoaded', function () {

    document.querySelectorAll('[data-user-action]').forEach(button => {
        button.addEventListener('click', handleButtonClick);
    });
});

function handleButtonClick(event) {
    event.preventDefault();

    const button = this;
    const action = button.getAttribute('data-user-action');
    const userId = button.getAttribute('data-user-id');

    if (!userId) {
        alert("Не удалось определить ID пользователя!");
        console.error("userId is null or undefined", button);
        return;
    }

    const userRow = button.closest('tr');

    disableUserButtons(userId, true);

    fetch('/api/admin/' + action + '/' + userId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            updateUserUI(userRow, action, userId);
        })
        .catch(error => {
            alert('Error performing action: ' + action + ' for user ' + userId + '\n' + error.message);
            disableUserButtons(userId, false);
        });
}

function disableUserButtons(userId, disabled) {
    document.querySelectorAll(`[data-user-id="${userId}"]`).forEach(btn => {
        btn.disabled = disabled;
    });
}

function updateUserUI(row, action, userId) {
    const statusCell = row.querySelector('td:nth-child(3)');
    const roleCell = row.querySelector('td:nth-child(4)');
    const actionsCell = row.querySelector('td:nth-child(5)');
    const currentRole = roleCell.querySelector('.badge')?.textContent.trim();
    const currentStatus = statusCell.querySelector('.badge')?.textContent.trim();

    let newStatus = currentStatus;
    let newRole = currentRole;

    switch (action) {
        case 'block':
            newStatus = 'Blocked';
            break;
        case 'unblock':
            newStatus = 'Active';
            break;
        case 'make-admin':
            newRole = 'ADMIN';
            break;
        case 'remove-admin':
            newRole = 'USER';
            break;
    }

    statusCell.innerHTML = `
        <span class="badge bg-${newStatus === 'Active' ? 'success' : 'danger'}">${newStatus}</span>
    `;

    roleCell.innerHTML = `
        <span class="badge bg-secondary">${newRole}</span>
    `;

    updateActionButtons(actionsCell, newStatus, newRole, userId);

    disableUserButtons(userId, false);
}


function updateActionButtons(cell, status, role, userId) {
    cell.innerHTML = '';

    let blockBtn = '';
    let adminBtn = '';

    if (status === 'Active') {
        blockBtn = `
            <button class="btn btn-outline-danger btn-sm me-1" data-user-action="block" data-user-id="${userId}" title="Block user">
                <i class="bi bi-x-circle-fill"></i> Block
            </button>`;
    } else {
        blockBtn = `
            <button class="btn btn-outline-success btn-sm me-1" data-user-action="unblock" data-user-id="${userId}" title="Unblock user">
                <i class="bi bi-unlock-fill"></i> Unblock
            </button>`;
    }

    if (role === 'USER') {
        adminBtn = `
            <button class="btn btn-outline-primary btn-sm" data-user-action="make-admin" data-user-id="${userId}" title="Make admin">
                <i class="bi bi-person-check-fill"></i> Make Admin
            </button>`;
    } else {
        adminBtn = `
            <button class="btn btn-outline-warning btn-sm" data-user-action="remove-admin" data-user-id="${userId}" title="Remove admin">
                <i class="bi bi-person-dash-fill"></i> Remove Admin
            </button>`;
    }

    cell.innerHTML = blockBtn + adminBtn;

    addEventListenersToButtons(cell);
}

function addEventListenersToButtons(cell) {
    cell.querySelectorAll('[data-user-action]').forEach(button => {
        button.removeEventListener('click', handleButtonClick); // избегаем дублирования
        button.addEventListener('click', handleButtonClick);
    });
}