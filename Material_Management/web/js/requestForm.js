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

function setupMaterialConditionListener(row, material) {
    const conditionSelect = row.querySelector('select[name="materialCondition"]');
    const inventoryInput = row.querySelector('.inventoryInput');
    const materialInput = row.querySelector('input[name="materialName"]');

    if (!conditionSelect || !inventoryInput || !materialInput) return;

    function updateInventory() {
        const selectedCondition = conditionSelect.value.trim().toLowerCase();
        const inventoryMap = material.inventoryMap || {};
        const inventory = inventoryMap[selectedCondition] ?? 0;
        inventoryInput.value = inventory;
        materialInput.dataset.availableQuantity = inventory;
    }

    conditionSelect.addEventListener('change', updateInventory);
    updateInventory();
}

function setupMaterialNameAutoFillUnit(row) {
    const materialInput = row.querySelector('.materialNameInput');
    const unitInput = row.querySelector('.unitInput');
    const inventoryInput = row.querySelector('.inventoryInput');
    const conditionSelect = row.querySelector('select[name="materialCondition"]');

    if (!materialInput || !unitInput || !inventoryInput || !conditionSelect) return;

    function update() {
        const selectedName = materialInput.value.trim();
        const material = findMaterialByName(selectedName);

        if (material) {
            unitInput.value = material.unit;
            unitInput.readOnly = true;
            setupMaterialConditionListener(row, material);
        } else {
            clearMaterialInfo(row);
        }
    }

    materialInput.addEventListener('awesomplete-selectcomplete', update);
    materialInput.addEventListener('input', update);
    conditionSelect.addEventListener('change', update);

    if (materialInput.value.trim()) update();
}

function findMaterialByName(materialName) {
    const normalizedName = materialName.trim().toLowerCase();
    for (let i = 0; i < window.materialNames.length; i++) {
        const name = window.materialNames[i].trim();
        if (name.toLowerCase() === normalizedName) {
            const inventoryByCondition = window.materialInventoryMap[name] || {};
            const normalizedInventory = {};
            for (const key in inventoryByCondition) {
                normalizedInventory[key.toLowerCase()] = inventoryByCondition[key];
            }
            return {
                name: name,
                unit: window.materialUnitMap[name],
                inventoryMap: normalizedInventory
            };
        }
    }
    return null;
}

function handleMaterialNameInput(input) {
    const row = input.closest('tr');
    const material = findMaterialByName(input.value.trim());
    const unitInput = row.querySelector('.unitInput');

    if (material) {
        unitInput.value = material.unit;
        unitInput.readOnly = true;
        setupMaterialConditionListener(row, material);
    } else {
        clearMaterialInfo(row);
    }
}

function checkQuantity(quantityInput) {
    const row = quantityInput.closest('tr');
    const materialName = row.querySelector('input[name="materialName"]').value.trim();
    const materialCondition = row.querySelector('select[name="materialCondition"]').value.trim().toLowerCase();
    const requestedQuantity = parseInt(quantityInput.value) || 0;

    const material = findMaterialByName(materialName);
    const inventory = material?.inventoryMap?.[materialCondition] ?? 0;

    const errorDiv = quantityInput.parentNode.querySelector('.error-message');
    if (requestedQuantity > inventory) {
        showWarning(quantityInput, `️ Vượt tồn kho (${inventory})`);
    } else if (errorDiv) {
        errorDiv.textContent = '';
    }
}

function showWarning(input, message) {
    let errorDiv = input.parentNode.querySelector('.error-message');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.style.cssText = `color: orange; font-size: 12px; margin-top: 2px;`;
        input.parentNode.appendChild(errorDiv);
    }
    errorDiv.textContent = message;
}

function clearMaterialInfo(row) {
    const unitInput = row.querySelector('.unitInput');
    const inventoryInput = row.querySelector('.inventoryInput');
    const materialInput = row.querySelector('input[name="materialName"]');

    if (unitInput) {
        unitInput.value = '';
        unitInput.readOnly = true;
    }
    if (inventoryInput) {
        inventoryInput.value = '';
    }
    if (materialInput) {
        delete materialInput.dataset.availableQuantity;
    }
}

document.addEventListener('DOMContentLoaded', function () {
    setupAwesompleteInputs();

    document.querySelectorAll("#itemsBody tr").forEach(function (row) {
        setupMaterialNameAutoFillUnit(row);
    });

    document.addEventListener('input', function (e) {
        if (e.target.name === 'materialName') handleMaterialNameInput(e.target);
        if (e.target.name === 'quantity') checkQuantity(e.target);
    });

    document.getElementById('requestForm').addEventListener('submit', function () {
        document.querySelectorAll('#itemsBody tr').forEach(row => {
            const quantityInput = row.querySelector('input[name="quantity"]');
            if (quantityInput) checkQuantity(quantityInput);
        });
    });

    const phoneInput = document.querySelector('input[name="contactPhone"]');
    if (phoneInput) {
        phoneInput.addEventListener('input', function () {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    }
});
