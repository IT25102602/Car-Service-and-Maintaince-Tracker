document.addEventListener('DOMContentLoaded', () => {
    loadServices();
});

async function loadServices() {
    const services = await fetchData('/services');
    if (services) {
        renderTable(services);
        updateStats(services);
    }
}

async function searchServices() {
    const query = document.getElementById('searchInput').value;
    if (!query) {
        loadServices();
        return;
    }
    const services = await fetchData(`/services/search?query=${query}`);
    if (services) {
        renderTable(services);
    }
}

function renderTable(services) {
    const tbody = document.getElementById('servicesTableBody');
    const emptyState = document.getElementById('emptyState');
    tbody.innerHTML = '';
    
    if (services.length === 0) {
        emptyState.style.display = 'block';
        return;
    }
    
    emptyState.style.display = 'none';
    services.forEach(s => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>#${s.serviceId}</td>
            <td><strong>${s.vehicleNumber}</strong></td>
            <td>${s.customerName}</td>
            <td>${s.serviceType}</td>
            <td>${s.serviceDate}</td>
            <td>${s.mechanicName}</td>
            <td>$${s.cost.toFixed(2)}</td>
            <td><span class="badge badge-${s.status.toLowerCase()}">${s.status}</span></td>
        `;
        tbody.appendChild(row);
    });
}

function updateStats(services) {
    document.getElementById('totalServices').textContent = services.length;
    const ongoing = services.filter(s => s.status === 'Ongoing' || s.status === 'Pending').length;
    document.getElementById('ongoingServices').textContent = ongoing;
    const revenue = services.reduce((acc, curr) => acc + curr.cost, 0);
    document.getElementById('totalRevenue').textContent = `$${revenue.toFixed(2)}`;
}
