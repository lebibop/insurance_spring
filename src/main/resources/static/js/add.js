function formatPhoneNumber(value) {
    const cleaned = ('' + value).replace(/\D/g, '');

    if (cleaned.length > 0 && cleaned.length < 10) return value;

    const match = cleaned.match(/^(\d{3})(\d{3})(\d{2})(\d{2})$/);
    if (match) {
        return `+7(${match[1]})-${match[2]}-${match[3]}-${match[4]}`;
    }
    return value;
}

document.getElementById('phone').addEventListener('input', function (e) {
    e.target.value = formatPhoneNumber(e.target.value);
});


async function loadCompanies() {
    try {
        const response = await fetch('/api/insurances/companies');
        if (!response.ok) throw new Error('Network response was not ok');
        const companies = await response.json();
        const companySelect = document.getElementById('company');
        for (const [name, color] of Object.entries(companies)) {
            const option = document.createElement('option');
            option.value = name;
            option.textContent = name;
            option.style.backgroundColor = color;
            companySelect.appendChild(option);
        }
    } catch (error) {
        console.error('Error loading companies:', error);
    }
}


async function loadTypes() {
    try {
        const response = await fetch('/api/insurances/types');
        if (!response.ok) throw new Error('Network response was not ok');
        const types = await response.json();
        const typeSelect = document.getElementById('type');
        types.forEach(type => {
            const option = document.createElement('option');
            option.value = type;
            option.textContent = type;
            typeSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading types:', error);
    }
}

window.onload = function () {
    loadCompanies();
    loadTypes();
};

function submitContract() {
    const beginDate = new Date(document.getElementById('beginDate').value);
    const durationUnit = document.getElementById('durationUnit').value;
    const durationValue = parseInt(document.getElementById('durationValue').value, 10);

    if (isNaN(new Date(document.getElementById('conclusionDate').value).getTime())) {
        alert("Введите корректную дату");
        return;
    }

    if (document.getElementById('company').value === "") {
        alert("Выберите компанию");
        return;
    }

    if (document.getElementById('type').value === "") {
        alert("Выберите вид");
        return;
    }

    if (isNaN(beginDate.getTime())) {
        alert("Введите корректную дату начала");
        return;
    }

    if (document.getElementById('durationUnit').value === "") {
        alert("Выберите корректное значение продолжительности");
        return;
    }

    if (isNaN(durationValue) || durationValue < 0) {
        alert("Введите корректное число продолжительности");
        return;
    }

    if (document.getElementById('fio').value === "") {
        alert("Введите ФИО");
        return;
    }

    if (document.getElementById('contractNumber').value === "") {
        alert("Введите № договора");
        return;
    }

    if (document.getElementById('phone').value.length > 0 && document.getElementById('phone').value.length < 10) {
        alert("Некорректный номер телефона");
        return;
    }


    const cost = parseFloat(document.getElementById('cost').value);
    const percentage = parseFloat(document.getElementById('percentage').value);
    const paymentsNumber = parseInt(document.getElementById('paymentsNumber').value, 10);


    if (isNaN(cost) || isNaN(percentage) || isNaN(paymentsNumber) || cost < 0 || percentage < 0 || paymentsNumber <= 0) {
        alert("Стоимость, процент и количество платежей должны быть действительными. Количество платежей должно быть больше 0.");
        return;
    }

    const amountPerPayment = (cost * (percentage / 100)) / paymentsNumber;

    let kv2 = null;
    let status_kv2 = null;

    if (paymentsNumber === 2) {
        kv2 = amountPerPayment;
        status_kv2 = "НЕТ";
    }

    const newEndDate = new Date(beginDate);
    if (durationUnit === "days") {
        newEndDate.setDate(beginDate.getDate() + durationValue);
    } else if (durationUnit === "months") {
        newEndDate.setMonth(beginDate.getMonth() + durationValue);
    } else if (durationUnit === "years") {
        newEndDate.setFullYear(beginDate.getFullYear() + durationValue);
    }

    newEndDate.setDate(beginDate.getDate() - 1);

    const formData = {
        conclusionDate: document.getElementById('conclusionDate').value,
        company: document.getElementById('company').value,
        type: document.getElementById('type').value,
        beginDate: document.getElementById('beginDate').value,
        endDate: newEndDate.toISOString().split('T')[0],
        fio: document.getElementById('fio').value.toUpperCase(),
        contractNumber: document.getElementById('contractNumber').value.toUpperCase(),
        phone: document.getElementById('phone').value,
        cost: document.getElementById('cost').value,
        percentage: document.getElementById('percentage').value,
        paymentsNumber: document.getElementById('paymentsNumber').value,
        kv1: amountPerPayment,
        status_kv1: "НЕТ",
        kv2: kv2,
        status_kv2: status_kv2,
    };

    fetch('/api/insurances/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.text())
        .then(() => {
            alert("Данные успешно добавлены");
            window.location.href = '/';
        })
        .catch(error => {
            console.error('Error submitting data:', error);
            alert("Произошла ошибка при отправке данных. Пожалуйста, попробуйте снова.");
        });
}

document.getElementById('CancelButton').addEventListener('click', () => {
    window.location.href = '/';
});