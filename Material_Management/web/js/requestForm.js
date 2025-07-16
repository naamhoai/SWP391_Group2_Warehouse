function setupAwesompleteInputs() {
    if (typeof Awesomplete === 'undefined') return;
    if (typeof materialNames !== 'undefined') {
        document.querySelectorAll('.materialNameInput').forEach(function (input) {
            if (!input.awesomplete) {
                input.awesomplete = new Awesomplete(input, {
                    list: materialNames,
                    minChars: 1,
                    autoFirst: true,
                    maxItems: 50
                });
            }
        });
    }
}

if (typeof Awesomplete !== 'undefined') {
    Awesomplete.ITEM = function (text) {
        const item = document.createElement("li");
        item.textContent = text;
        return item;
    };
    Awesomplete.REPLACE = function (text) {
        this.input.value = text;
    };
}

function addRow() {
    const tbody = document.getElementById('itemsBody');
    const newRow = tbody.rows[0].cloneNode(true);

    newRow.querySelectorAll('input').forEach(input => {
        input.value = '';
        input.removeAttribute('data-available-quantity');
    });
    newRow.querySelectorAll('select').forEach(select => select.selectedIndex = 0);

    const infoDiv = newRow.querySelector('.inventory-info');
    if (infoDiv) infoDiv.remove();

    tbody.appendChild(newRow);
    document.getElementById('itemCount').value = tbody.rows.length;

    setupAwesompleteInputs();
    setupMaterialNameAutoFillUnit(newRow);
}

function removeRow(btn) {
    const tbody = document.getElementById('itemsBody');
    if (tbody.rows.length > 1) {
        btn.closest('tr').remove();
        document.getElementById('itemCount').value = tbody.rows.length;
    }
}

function setupMaterialNameAutoFillUnit(row) {
    const materialInput = row.querySelector('.materialNameInput');
    const unitInput = row.querySelector('.unitInput');

    if (!materialInput || !unitInput) return;

    function update() {
        const selectedName = materialInput.value;
        if (window.materialUnitMap && window.materialUnitMap[selectedName]) {
            unitInput.value = window.materialUnitMap[selectedName];
            unitInput.readOnly = true;
        } else {
            unitInput.value = "";
            unitInput.readOnly = true;
        }
    }

    materialInput.addEventListener('awesomplete-selectcomplete', update);
    materialInput.addEventListener('input', update);

    if (!materialInput.value) {
        unitInput.value = "";
        unitInput.readOnly = true;
    } else {
        update();
    }
}

document.addEventListener('DOMContentLoaded', function () {
    setupAwesompleteInputs();

    document.querySelectorAll("#itemsBody tr").forEach(function (row) {
        setupMaterialNameAutoFillUnit(row);
    });

});
