document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('productForm');
    const nameInput = document.getElementById('name');
    const descriptionInput = document.getElementById('description');

    const nameCounter = document.getElementById('nameCounter');
    const descriptionCounter = document.getElementById('descriptionCounter');

    nameInput.addEventListener('input', updateCounter(nameInput, nameCounter, 64, 'nameError'));
    descriptionInput.addEventListener('input', updateCounter(descriptionInput, descriptionCounter, 2048, 'descriptionError'));

    form.addEventListener('submit', function (event) {
        let hasErrors = false;

        if (nameInput.value.length > 64) {
            document.getElementById('nameError').classList.remove('d-none');
            hasErrors = true;
        }

        if (descriptionInput.value.length > 2048) {
            document.getElementById('descriptionError').classList.remove('d-none');
            hasErrors = true;
        }

        if (hasErrors) {
            event.preventDefault();
        }
    });

    function updateCounter(input, counter, maxLength, errorId) {
        return function () {
            const remaining = maxLength - input.value.length;
            counter.textContent = `${remaining} character${remaining !== 1 ? 's' : ''} remaining`;

            const errorElement = document.getElementById(errorId);
            if (remaining < 0) {
                errorElement.classList.remove('d-none');
            } else {
                errorElement.classList.add('d-none');
            }
        };
    }
});