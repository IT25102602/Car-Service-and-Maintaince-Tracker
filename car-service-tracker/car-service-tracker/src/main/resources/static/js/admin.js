document.addEventListener('DOMContentLoaded', () => {
    loadAdminData();

    document.getElementById('serviceForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('editServiceId').value || 'S' + Math.floor(Math.random() * 10000);
        
        const data = {
            serviceId: id,
            vehicleNumber: document.getElementById('vehNum').value,
            customerName: document.getElementById('custName').value,
            serviceType: document.getElementById('servType').value,
            serviceDate: document.getElementById('servDate').value,
            mechanicName: document.getElementById('mechName').value,
            cost: parseFloat(document.getElementById('servCost').value),
            notes: document.getElementById('servNotes').value,
            status: document.getElementById('servStatus').value
        };

        const method = document.getElementById('editServiceId').value ? 'PUT' : 'POST';
        const success = await postData('/services', data, method);
        if (success) {
            showToast('Service record saved!');
            closeModal();
            loadAdminData();
        }
    });
});

async function loadAdminData() {
    const services = await fetchData('/services');
    const reviews = await fetchData('/reviews');
    
    if (services) renderServices(services);
    if (reviews) renderReviews(reviews);
}

function renderServices(services) {
    const tbody = document.getElementById('adminServicesTable');
    tbody.innerHTML = '';
    services.forEach(s => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>#${s.serviceId}</td>
            <td>${s.vehicleNumber}</td>
            <td>${s.customerName}</td>
            <td><span class="badge badge-${s.status.toLowerCase()}">${s.status}</span></td>
            <td>
                <button onclick='editService(${JSON.stringify(s)})' style="background:none; color:var(--secondary-color);"><i class="fas fa-edit"></i></button>
                <button onclick="deleteService('${s.serviceId}')" style="background:none; color:var(--danger);"><i class="fas fa-trash"></i></button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function renderReviews(reviews) {
    const tbody = document.getElementById('adminReviewsTable');
    tbody.innerHTML = '';
    reviews.forEach(r => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>#${r.reviewId}</td>
            <td>${r.customerName}</td>
            <td>${r.rating} Stars</td>
            <td style="max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${r.reviewMessage}</td>
            <td>
                <button onclick="deleteReview('${r.reviewId}')" style="background:none; color:var(--danger);"><i class="fas fa-trash"></i></button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function openServiceModal() {
    document.getElementById('modalTitle').textContent = 'Add Service Record';
    document.getElementById('serviceForm').reset();
    document.getElementById('editServiceId').value = '';
    document.getElementById('serviceModal').style.display = 'flex';
}

function editService(s) {
    document.getElementById('modalTitle').textContent = 'Edit Service Record';
    document.getElementById('editServiceId').value = s.serviceId;
    document.getElementById('vehNum').value = s.vehicleNumber;
    document.getElementById('custName').value = s.customerName;
    document.getElementById('servType').value = s.serviceType;
    document.getElementById('servDate').value = s.serviceDate;
    document.getElementById('mechName').value = s.mechanicName;
    document.getElementById('servCost').value = s.cost;
    document.getElementById('servNotes').value = s.notes;
    document.getElementById('servStatus').value = s.status;
    document.getElementById('serviceModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('serviceModal').style.display = 'none';
}

async function deleteService(id) {
    if (confirm('Are you sure you want to delete this service record?')) {
        const success = await deleteData(`/services/${id}`);
        if (success) {
            showToast('Service deleted');
            loadAdminData();
        }
    }
}

async function deleteReview(id) {
    if (confirm('Are you sure you want to delete this review?')) {
        const success = await deleteData(`/reviews/${id}`);
        if (success) {
            showToast('Review deleted');
            loadAdminData();
        }
    }
}
