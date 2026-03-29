document.addEventListener('DOMContentLoaded', function () {
    'use strict';
    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!confirm(this.dataset.confirm)) {
                event.preventDefault();
            }
        });
    });
});
