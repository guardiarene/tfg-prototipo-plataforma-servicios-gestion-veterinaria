(function () {
    'use strict';

    var speciesSelect = document.getElementById('species');
    var breedSelect = document.getElementById('breed');

    if (!speciesSelect || !breedSelect) return;

    function filterBreeds(resetSelection) {
        var selectedSpecies = speciesSelect.value;
        var allOptions = breedSelect.querySelectorAll('option[data-species]');
        allOptions.forEach(function (opt) {
            opt.style.display = opt.dataset.species === selectedSpecies ? '' : 'none';
        });
        breedSelect.disabled = !selectedSpecies;
        if (resetSelection) {
            var current = breedSelect.querySelector('option:checked');
            if (current && current.dataset.species !== selectedSpecies) {
                breedSelect.value = '';
            }
        }
    }

    speciesSelect.addEventListener('change', function () {
        filterBreeds(true);
    });

    filterBreeds(false);
})();
