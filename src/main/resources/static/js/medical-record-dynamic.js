var _medicalRecordNextIndex = {
    vaccines: 0,
    diagnostics: 0,
    treatments: 0
};

(function () {
    'use strict';

    function countExisting(containerId) {
        var container = document.getElementById(containerId);
        return container ? container.children.length : 0;
    }

    _medicalRecordNextIndex.vaccines = countExisting('vaccines-container');
    _medicalRecordNextIndex.diagnostics = countExisting('diagnostics-container');
    _medicalRecordNextIndex.treatments = countExisting('treatments-container');
})();

function addVaccine() {
    'use strict';
    var container = document.getElementById('vaccines-container');
    var index = _medicalRecordNextIndex.vaccines++;
    var template =
        '<div class="vaccine-template row g-3 mb-3">' +
        '    <div class="col-md-3">' +
        '        <label class="form-label small fw-bold">Fecha Aplicaci\u00f3n</label>' +
        '        <input type="date" class="form-control" name="visits[0].vaccines[' + index + '].applicationDate" required>' +
        '    </div>' +
        '    <div class="col-md-3">' +
        '        <label class="form-label small fw-bold">Marca</label>' +
        '        <input type="text" class="form-control" name="visits[0].vaccines[' + index + '].brand" placeholder="Marca" required>' +
        '    </div>' +
        '    <div class="col-md-3">' +
        '        <label class="form-label small fw-bold">Lote</label>' +
        '        <input type="text" class="form-control" name="visits[0].vaccines[' + index + '].batch" placeholder="Lote" required>' +
        '    </div>' +
        '    <div class="col-md-2">' +
        '        <label class="form-label small fw-bold">Dosis</label>' +
        '        <input type="text" class="form-control" name="visits[0].vaccines[' + index + '].dose" placeholder="Dosis" required>' +
        '    </div>' +
        '    <div class="col-md-1 d-flex align-items-end">' +
        '        <button type="button" class="btn btn-danger w-100" aria-label="Eliminar vacuna" onclick="removeVaccine(this)"><i aria-hidden="true" class="bi bi-trash"></i></button>' +
        '    </div>' +
        '</div>';
    container.insertAdjacentHTML('beforeend', template);
}

function removeVaccine(button) {
    button.closest('.vaccine-template').remove();
}

function addDiagnostic() {
    'use strict';
    var container = document.getElementById('diagnostics-container');
    var index = _medicalRecordNextIndex.diagnostics++;
    var template =
        '<div class="diagnostic-template card mb-3 shadow-sm">' +
        '    <div class="card-body">' +
        '        <div class="row g-3 align-items-center">' +
        '            <div class="col-md-11">' +
        '                <label class="form-label small fw-bold">Problemas / Hallazgos</label>' +
        '                <input type="text" class="form-control" name="visits[0].diagnostics[' + index + '].problems[0]" placeholder="Descripci\u00f3n detallada del problema" required>' +
        '            </div>' +
        '            <div class="col-md-1 d-flex align-items-end">' +
        '                <button type="button" class="btn btn-danger w-100" aria-label="Eliminar diagn\u00f3stico" onclick="removeDiagnostic(this)"><i aria-hidden="true" class="bi bi-trash"></i></button>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '</div>';
    container.insertAdjacentHTML('beforeend', template);
}

function removeDiagnostic(button) {
    button.closest('.diagnostic-template').remove();
}

function addTreatment() {
    'use strict';
    var container = document.getElementById('treatments-container');
    var index = _medicalRecordNextIndex.treatments++;
    var template =
        '<div class="treatment-template card mb-3 shadow-sm">' +
        '    <div class="card-body">' +
        '        <div class="row g-3 align-items-center">' +
        '            <div class="col-md-3">' +
        '                <label class="form-label small fw-bold">Producto</label>' +
        '                <input type="text" class="form-control" name="visits[0].treatments[' + index + '].product" placeholder="Producto" required>' +
        '            </div>' +
        '            <div class="col-md-2">' +
        '                <label class="form-label small fw-bold">V\u00eda</label>' +
        '                <input type="text" class="form-control" name="visits[0].treatments[' + index + '].route" placeholder="V\u00eda" required>' +
        '            </div>' +
        '            <div class="col-md-2">' +
        '                <label class="form-label small fw-bold">Frecuencia</label>' +
        '                <input type="text" class="form-control" name="visits[0].treatments[' + index + '].frequency" placeholder="Frecuencia" required>' +
        '            </div>' +
        '            <div class="col-md-2">' +
        '                <label class="form-label small fw-bold">Inicio</label>' +
        '                <input type="date" class="form-control" name="visits[0].treatments[' + index + '].startDate" required>' +
        '            </div>' +
        '            <div class="col-md-2">' +
        '                <label class="form-label small fw-bold">Fin</label>' +
        '                <input type="date" class="form-control" name="visits[0].treatments[' + index + '].endDate">' +
        '            </div>' +
        '            <div class="col-md-1 d-flex align-items-end">' +
        '                <button type="button" class="btn btn-danger w-100" aria-label="Eliminar tratamiento" onclick="removeTreatment(this)"><i aria-hidden="true" class="bi bi-trash"></i></button>' +
        '            </div>' +
        '        </div>' +
        '    </div>' +
        '</div>';
    container.insertAdjacentHTML('beforeend', template);
}

function removeTreatment(button) {
    button.closest('.treatment-template').remove();
}
