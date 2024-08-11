let companyColors = {};
let selectedRowId = null;

async function fetchCompanyColors() {
    try {
        const response = await fetch('/api/insurances/companies');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        companyColors = await response.json();
    } catch (error) {
        console.error('Error fetching company colors:', error);
    }
}

async function fetchInsuranceData() {
    try {
        await fetchCompanyColors(); // Ensure company colors are loaded

        const response = await fetch('/api/insurances');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const insurances = await response.json();
        renderTable(insurances);
    } catch (error) {
        console.error('Error fetching insurance data:', error);
    }
}

function renderTable(insurances) {
    const tableBody = document.querySelector('#insuranceTableBody');
    tableBody.innerHTML = '';

    insurances.forEach(insurance => {
        const row = document.createElement('tr');
        row.dataset.id = insurance.id;

        const properties = [
            formatDate(insurance.conclusionDate),
            insurance.company,
            insurance.type,
            formatDate(insurance.beginDate),
            formatDate(insurance.endDate),
            insurance.fio,
            insurance.contractNumber || 'N/A',
            insurance.phone,
            insurance.cost,
            insurance.percentage,
            insurance.paymentsNumber || 'N/A',
            insurance.kv1,
            insurance.status_kv1,
            insurance.kv2,
            insurance.status_kv2
        ];

        properties.forEach((property, index) => {
            const cell = document.createElement('td');
            cell.textContent = property;
            applyCellStyling(cell, index, property, insurance);
            row.appendChild(cell);
        });

        row.addEventListener('dblclick', () => {
            selectedRowId = insurance.id;
            showEditModal(insurance);
        });

        tableBody.appendChild(row);
    });
}

function applyCellStyling(cell, index, property, insurance) {
    // Change background color based on status_kv1
    if (index === 12) { // Index of status_kv1 cell
        switch (property) {
            case 'НЕТ':
                cell.style.backgroundColor = 'white';
                break;
            case 'АКТ':
                cell.style.backgroundColor = '#e7c58a';
                break;
            case 'ВЫПЛАТА':
                cell.style.backgroundColor = '#86b05d';
                cell.style.color = 'white';
                break;
            default:
                cell.style.backgroundColor = 'white';
        }
    }

    // Check kv2 and status_kv2
    if (index === 13 || index === 14) { // Indexes of kv2 and status_kv2 cells
        const kv2 = insurance.kv2;
        if (kv2 === 'N/A' || kv2 === '' || kv2 === null) {
            cell.style.backgroundColor = 'grey';
            cell.textContent = ''; // Clear text
        } else if (index === 14) { // For status_kv2, if kv2 is not empty
            switch (property) {
                case 'НЕТ':
                    cell.style.backgroundColor = 'white';
                    break;
                case 'АКТ':
                    cell.style.backgroundColor = '#e7c58a';
                    break;
                case 'ВЫПЛАТА':
                    cell.style.backgroundColor = '#86b05d';
                    cell.style.color = 'white';
                    break;
                default:
                    cell.style.backgroundColor = 'white';
            }
        }
    }

    // Color the first 8 cells based on company
    if (index < 8) {
        cell.style.backgroundColor = companyColors[insurance.company] || "#ffffff";
    }
}

function formatDate(date) {
    if (date) {
        return new Date(date).toLocaleDateString();
    }
    return 'N/A';
}

function showEditModal(insurance) {
    const statusKV1Select = document.getElementById('statusKV1');
    const statusKV2Select = document.getElementById('statusKV2');
    const kv2Group = document.getElementById('kv2Group');

    statusKV1Select.value = insurance.status_kv1;
    statusKV2Select.value = insurance.status_kv2 || null;
    document.getElementById('rowId').value = insurance.id; // Use real ID

    // Show or hide KV2 select based on paymentsNumber
    if (insurance.paymentsNumber === 2) {
        kv2Group.classList.remove('d-none');
    } else {
        kv2Group.classList.add('d-none');
    }

    $('#editModal').modal('show');
}

document.getElementById('saveChanges').addEventListener('click', async () => {
    const statusKV1 = document.getElementById('statusKV1').value;
    const statusKV2 = document.getElementById('statusKV2').value;
    const rowId = document.getElementById('rowId').value;

    if (!rowId) {
        alert('No row selected');
        return;
    }

    try {
        const response = await fetch(`/api/insurances/update-status/${rowId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                status_kv1: statusKV1,
                status_kv2: statusKV2,
            }),
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // Refresh data
        await fetchInsuranceData();

        // Close modal
        $('#editModal').modal('hide');
    } catch (error) {
        console.error('Error updating insurance data:', error);
    }
});

document.getElementById('addInsurance').addEventListener('click', () => {
    window.location.href = '/add';  // Redirect to the /add page
});

document.addEventListener('DOMContentLoaded', () => {
    fetchInsuranceData();

    // Add click event listener to rows
    document.querySelector('#insuranceTableBody').addEventListener('click', function (e) {
        const row = e.target.closest('tr');
        if (!row) return; // Click outside the rows

        // Clear previous selection
        document.querySelectorAll('#insuranceTableBody tr').forEach(tr => {
            tr.classList.remove('selected');
        });

        // Highlight selected row
        row.classList.add('selected');

        // Set selected row ID
        selectedRowId = row.dataset.id;
    });
});

document.getElementById('deleteSelected').addEventListener('click', async () => {
    if (!selectedRowId) {
        alert('No row selected');
        return;
    }

    // Ask for confirmation
    const confirmDelete = confirm('Are you sure you want to delete this record?');
    if (!confirmDelete) {
        return;
    }

    try {
        const response = await fetch(`/api/insurances/delete/${selectedRowId}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // Refresh data
        await fetchInsuranceData();

        // Clear the selected row ID
        selectedRowId = null;
    } catch (error) {
        console.error('Error deleting insurance data:', error);
    }
});

document.getElementById('searchButton').addEventListener('click', async () => {
    const query = document.getElementById('searchInput').value;

    if (!query) {
        alert('Search query cannot be empty.');
        return; // Stop execution if the query is empty
    }

    try {
        const response = await fetch(`/api/insurances/search?query=${encodeURIComponent(query)}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();

        if (data.length === 0) {
            alert('No results found.');
            window.location.href = '/';
        } else {
            renderTable(data);
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});


document.getElementById('showAll').addEventListener('click', () => {
    window.location.href = '/';
});




document.getElementById('NearP').addEventListener('click', () => {
    fetchInsurancesNearConclusionDate();
});

async function fetchInsurancesNearConclusionDate() {
    try {
        const response = await fetch('/api/insurances/near-conclusion-date');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const insurances = await response.json();
        renderTable(insurances); // Обновляем таблицу с новыми данными
    } catch (error) {
        console.error('Error fetching insurances near conclusion date:', error);
    }
}



document.getElementById('calculateProfitButton').addEventListener('click', () => {
    $('#profitModal').modal('show');
});

// Calculate profit when button is clicked
document.getElementById('calculateProfit').addEventListener('click', async () => {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate || !endDate) {
        alert('Please enter both start and end dates.');
        return;
    }

    if (startDate > endDate) {
        alert('Start date cannot be later than end date.');
        return;
    }

    try {
        const response = await fetch(`/api/insurances/profit?startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`, {
            method: 'POST',
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const profit = await response.json();
        document.getElementById('profitResult').textContent = `Total Profit: ${profit}`;
    } catch (error) {
        console.error('Error calculating profit:', error);
        document.getElementById('profitResult').textContent = 'Error calculating profit.';
    }
});