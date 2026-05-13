document.addEventListener('DOMContentLoaded', () => {
    // Initial Load
    refreshAllData();

    // Star Rating Logic
    const stars = document.querySelectorAll('#starContainer i');
    const fbRating = document.getElementById('fbRating');
    
    stars.forEach(star => {
        star.addEventListener('click', () => {
            const val = star.getAttribute('data-val');
            fbRating.value = val;
            updateStarDisplay(val);
        });
        star.addEventListener('mouseover', () => {
            updateStarDisplay(star.getAttribute('data-val'), true);
        });
    });

    document.getElementById('starContainer').addEventListener('mouseleave', () => {
        updateStarDisplay(fbRating.value);
    });

    // Forms
    document.getElementById('feedbackForm').addEventListener('submit', handleFeedbackSubmit);
    document.getElementById('adminServiceForm').addEventListener('submit', handleAdminServiceSubmit);
});

// Navigation
function navigate(page) {
    const pages = ['dashboard', 'history', 'reviews', 'admin'];
    pages.forEach(p => {
        document.getElementById('page-' + p).classList.add('hidden');
        const nav = document.getElementById('nav-' + p);
        nav.classList.remove('bg-teal-500/10', 'text-teal-400', 'border-teal-500/20');
        nav.classList.add('hover:bg-slate-800', 'text-slate-400');
    });

    document.getElementById('page-' + page).classList.remove('hidden');
    const activeNav = document.getElementById('nav-' + page);
    activeNav.classList.add('bg-teal-500/10', 'text-teal-400', 'border-teal-500/20');
    activeNav.classList.remove('hover:bg-slate-800', 'text-slate-400');

    // Update Header
    const titles = {
        'dashboard': ['Dashboard Overview', 'Welcome back to your car service hub'],
        'history': ['Service History', 'Complete timeline of your vehicle maintenance'],
        'reviews': ['Customer Feedback', 'Share your experience with us'],
        'admin': ['System Admin', 'Manage records and moderate content']
    };
    document.getElementById('pageTitle').textContent = titles[page][0];
    document.getElementById('pageSubtitle').textContent = titles[page][1];

    if (page === 'admin' || page === 'dashboard') refreshAllData();
}

// Data Loading
async function refreshAllData() {
    const services = await fetchData('/services');
    const reviews = await fetchData('/reviews');
    const stats = await fetchData('/reviews/stats');

    if (services) {
        renderHistory(services);
        renderAdminServices(services);
        populateServiceDropdown(services);
        updateDashboardStats(services, stats);
        renderRecentActivity(services);
    }
    if (reviews) {
        renderReviews(reviews);
        renderAdminReviews(reviews);
        renderRecentReviews(reviews);
    }
}

function updateDashboardStats(services, stats) {
    document.getElementById('statTotalServices').textContent = services.length;
    document.getElementById('statActiveRequests').textContent = services.filter(s => s.status !== 'Completed').length;
    if (stats) {
        document.getElementById('statAvgRating').textContent = stats.averageRating.toFixed(1);
    }
}

function renderHistory(services) {
    const tbody = document.getElementById('historyTableBody');
    tbody.innerHTML = services.map(s => `
        <tr class="hover:bg-slate-800/30 transition-all group">
            <td class="px-8 py-5 text-sm font-mono text-slate-500 group-hover:text-teal-500">#${s.serviceId}</td>
            <td class="px-8 py-5 text-sm font-bold text-white">${s.vehicleNumber}</td>
            <td class="px-8 py-5 text-sm text-slate-400">${s.serviceType}</td>
            <td class="px-8 py-5 text-sm text-slate-500">${s.serviceDate}</td>
            <td class="px-8 py-5">
                <span class="px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider ${getStatusClass(s.status)}">
                    ${s.status}
                </span>
            </td>
            <td class="px-8 py-5 text-sm font-bold text-teal-400">$${s.cost.toFixed(2)}</td>
        </tr>
    `).join('');
}

function renderRecentActivity(services) {
    const list = document.getElementById('recentActivityList');
    const recent = services.slice(-4).reverse();
    list.innerHTML = recent.map(s => `
        <div class="flex items-center gap-4 group">
            <div class="w-10 h-10 rounded-xl bg-slate-800 flex items-center justify-center text-slate-500 group-hover:text-teal-500 transition-all">
                <i class="fa fa-screwdriver-wrench"></i>
            </div>
            <div class="flex-1">
                <p class="text-sm font-bold text-white">${s.serviceType}</p>
                <p class="text-xs text-slate-500">${s.vehicleNumber} • ${s.serviceDate}</p>
            </div>
            <div class="text-right">
                <p class="text-xs font-bold text-teal-500">$${s.cost.toFixed(0)}</p>
                <p class="text-[10px] text-slate-600 uppercase">${s.status}</p>
            </div>
        </div>
    `).join('');
}

function renderReviews(reviews) {
    const grid = document.getElementById('reviewsGrid');
    grid.innerHTML = reviews.map(r => `
        <div class="glass-card p-6 rounded-3xl border border-slate-800 space-y-4">
            <div class="flex justify-between items-start">
                <div class="flex gap-1 text-amber-400 text-xs">
                    ${getStarIcons(r.rating)}
                </div>
                <span class="text-[10px] bg-slate-800 text-slate-500 px-2 py-1 rounded-lg uppercase font-bold">${r.reviewType}</span>
            </div>
            <p class="text-sm text-slate-300 leading-relaxed italic">"${r.reviewMessage}"</p>
            <div class="flex items-center gap-3 pt-2">
                <div class="w-8 h-8 rounded-full bg-slate-700 flex items-center justify-center text-[10px] font-bold text-white">
                    ${r.customerName.charAt(0)}
                </div>
                <div>
                    <p class="text-xs font-bold text-white">${r.customerName}</p>
                    <p class="text-[10px] text-slate-500">${r.date}</p>
                </div>
            </div>
        </div>
    `).join('');
}

function renderRecentReviews(reviews) {
    const list = document.getElementById('recentReviewsList');
    const recent = reviews.slice(-3).reverse();
    list.innerHTML = recent.map(r => `
        <div class="flex gap-4">
            <div class="w-8 h-8 rounded-full bg-teal-500/10 text-teal-500 flex items-center justify-center flex-shrink-0 text-xs font-bold">
                ${r.customerName.charAt(0)}
            </div>
            <div>
                <div class="flex gap-1 text-[10px] text-amber-500 mb-1">
                    ${getStarIcons(r.rating)}
                </div>
                <p class="text-xs text-slate-400 line-clamp-2 italic">"${r.reviewMessage}"</p>
            </div>
        </div>
    `).join('');
}

// Handlers
async function handleFeedbackSubmit(e) {
    e.preventDefault();
    const rating = document.getElementById('fbRating').value;
    if (rating === '0') {
        showCustomToast('Please select a rating', 'danger');
        return;
    }

    const data = {
        reviewId: 'R' + Date.now(),
        serviceId: document.getElementById('fbServiceId').value,
        customerName: 'John Smith', // Simulated
        rating: parseInt(rating),
        reviewMessage: document.getElementById('fbComment').value,
        date: new Date().toISOString().split('T')[0],
        reviewType: 'Verified'
    };

    const success = await postData('/reviews', data);
    if (success) {
        showCustomToast('Thank you for your review!');
        e.target.reset();
        updateStarDisplay(0);
        refreshAllData();
    }
}

async function handleAdminServiceSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('editId').value || 'S' + Math.floor(Math.random() * 10000);
    const data = {
        serviceId: id,
        vehicleNumber: document.getElementById('mVehicle').value,
        customerName: document.getElementById('mCustomer').value,
        serviceType: document.getElementById('mType').value,
        serviceDate: document.getElementById('mDate').value,
        mechanicName: 'Admin',
        cost: parseFloat(document.getElementById('mCost').value),
        notes: document.getElementById('mNotes').value,
        status: document.getElementById('mStatus').value
    };

    const method = document.getElementById('editId').value ? 'PUT' : 'POST';
    const success = await postData('/services', data, method);
    if (success) {
        showCustomToast('Service record saved');
        closeAdminModal();
        refreshAllData();
    }
}

// Admin Moderation Helpers
function renderAdminServices(services) {
    const tbody = document.getElementById('adminServiceTable');
    tbody.innerHTML = services.map(s => `
        <tr>
            <td class="px-6 py-4 text-xs font-mono text-slate-500">#${s.serviceId}</td>
            <td class="px-6 py-4 font-bold text-white text-xs">${s.vehicleNumber}</td>
            <td class="px-6 py-4">
                <span class="text-[9px] font-bold px-2 py-0.5 rounded-full ${getStatusClass(s.status)}">${s.status}</span>
            </td>
            <td class="px-6 py-4 flex gap-3">
                <button onclick='editService(${JSON.stringify(s)})' class="text-teal-500 hover:text-white transition-all"><i class="fa fa-edit"></i></button>
                <button onclick="deleteServiceRecord('${s.serviceId}')" class="text-red-500 hover:text-white transition-all"><i class="fa fa-trash"></i></button>
            </td>
        </tr>
    `).join('');
}

function renderAdminReviews(reviews) {
    const tbody = document.getElementById('adminReviewTable');
    tbody.innerHTML = reviews.map(r => `
        <tr>
            <td class="px-6 py-4 text-xs font-mono text-slate-500">#${r.reviewId}</td>
            <td class="px-6 py-4 text-amber-500 text-xs">${r.rating} Stars</td>
            <td class="px-6 py-4 text-xs text-white">${r.customerName}</td>
            <td class="px-6 py-4">
                <button onclick="deleteReviewRecord('${r.reviewId}')" class="text-red-500 hover:text-white transition-all"><i class="fa fa-trash"></i></button>
            </td>
        </tr>
    `).join('');
}

function editService(s) {
    document.getElementById('editId').value = s.serviceId;
    document.getElementById('mVehicle').value = s.vehicleNumber;
    document.getElementById('mCustomer').value = s.customerName;
    document.getElementById('mType').value = s.serviceType;
    document.getElementById('mDate').value = s.serviceDate;
    document.getElementById('mCost').value = s.cost;
    document.getElementById('mStatus').value = s.status;
    document.getElementById('mNotes').value = s.notes;
    openAdminModal();
}

async function deleteServiceRecord(id) {
    if (confirm('Delete record?')) {
        const success = await deleteData(`/services/${id}`);
        if (success) { showCustomToast('Record deleted'); refreshAllData(); }
    }
}

async function deleteReviewRecord(id) {
    if (confirm('Delete review?')) {
        const success = await deleteData(`/reviews/${id}`);
        if (success) { showCustomToast('Review deleted'); refreshAllData(); }
    }
}

// Utilities
function getStatusClass(status) {
    if (status === 'Completed') return 'bg-teal-500/10 text-teal-400 border border-teal-500/20';
    if (status === 'Ongoing') return 'bg-blue-500/10 text-blue-400 border border-blue-500/20';
    return 'bg-amber-500/10 text-amber-400 border border-amber-500/20';
}

function getStarIcons(rating) {
    let icons = '';
    for (let i = 1; i <= 5; i++) {
        icons += `<i class="${i <= rating ? 'fa-solid' : 'fa-regular'} fa-star"></i>`;
    }
    return icons;
}

function updateStarDisplay(val, isHover = false) {
    const stars = document.querySelectorAll('#starContainer i');
    stars.forEach(s => {
        const starVal = s.getAttribute('data-val');
        if (starVal <= val) {
            s.classList.replace('text-slate-700', 'text-teal-400');
            s.classList.replace('fa-regular', 'fa-solid');
        } else {
            s.classList.replace('text-teal-400', 'text-slate-700');
            s.classList.replace('fa-solid', 'fa-regular');
        }
    });
}

function populateServiceDropdown(services) {
    const select = document.getElementById('fbServiceId');
    const val = select.value;
    select.innerHTML = '<option value="">Choose a service ID...</option>' + 
        services.map(s => `<option value="${s.serviceId}">${s.serviceType} - ${s.vehicleNumber} (#${s.serviceId})</option>`).join('');
    select.value = val;
}

function openAdminModal() { document.getElementById('adminModal').classList.remove('hidden'); }
function closeAdminModal() { 
    document.getElementById('adminModal').classList.add('hidden'); 
    document.getElementById('adminServiceForm').reset();
    document.getElementById('editId').value = '';
}

function showCustomToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    const toastMsg = document.getElementById('toastMessage');
    const toastIcon = document.getElementById('toastIcon');
    
    toastMsg.textContent = message;
    if (type === 'danger') {
        toast.querySelector('.glass-card').classList.replace('border-teal-500', 'border-red-500');
        toastIcon.className = 'text-red-500 text-xl';
        toastIcon.innerHTML = '<i class="fa fa-circle-exclamation"></i>';
    } else {
        toast.querySelector('.glass-card').classList.replace('border-red-500', 'border-teal-500');
        toastIcon.className = 'text-teal-500 text-xl';
        toastIcon.innerHTML = '<i class="fa fa-circle-check"></i>';
    }

    toast.classList.remove('translate-y-20', 'opacity-0');
    setTimeout(() => toast.classList.add('translate-y-20', 'opacity-0'), 3000);
}
