function initPasswordValidation(passwordId, confirmPasswordId, errorContainerId, errorMessageId) {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        var password = document.getElementById(passwordId);
        var confirmPassword = document.getElementById(confirmPasswordId);
        var passwordError = document.getElementById(errorContainerId);
        var passwordErrorMessage = document.getElementById(errorMessageId);

        if (!password || !confirmPassword || !passwordError || !passwordErrorMessage) return;

        var form = password.closest('form');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            if (password.value !== confirmPassword.value) {
                event.preventDefault();
                passwordErrorMessage.textContent = 'Las contraseñas no coinciden';
                passwordError.style.display = 'block';
                confirmPassword.focus();
                return false;
            }
            if (password.value.length < 8) {
                event.preventDefault();
                passwordErrorMessage.textContent = 'La contraseña debe tener al menos 8 caracteres';
                passwordError.style.display = 'block';
                password.focus();
                return false;
            }
            passwordError.style.display = 'none';
            return true;
        });

        password.addEventListener('input', function () {
            passwordError.style.display = 'none';
        });
        confirmPassword.addEventListener('input', function () {
            passwordError.style.display = 'none';
        });
    });
}
