// ── Global state ────────────────────────────────────────────
let currentUserId   = null;
let currentUserName = null;
let currentRole     = 'customer';
let registerRole    = 'customer';

// ── Login role toggle ────────────────────────────────────────
function selectRole(role) {
    currentRole = role;
    const isCustomer = role === 'customer';

    document.getElementById('customerTab').classList.toggle('border-[#14b8a6]', isCustomer);
    document.getElementById('customerTab').classList.toggle('bg-[#14b8a6]/10', isCustomer);
    document.getElementById('adminTab').classList.toggle('border-[#14b8a6]', !isCustomer);
    document.getElementById('adminTab').classList.toggle('bg-[#14b8a6]/10', !isCustomer);
    document.getElementById('signInText').textContent = isCustomer ? 'Customer' : 'Admin';
}

// ── Login ────────────────────────────────────────────────────
async function login() {
    const email    = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const errBox   = document.getElementById('loginError');

    errBox.classList.add('hidden');

    if (!email || !password) {
        errBox.textContent = 'Please enter both email and password.';
        errBox.classList.remove('hidden');
        return;
    }

    try {
        const res  = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password, role: currentRole })
        });
        const data = await res.json();

        if (data.success) {
            currentUserId   = data.id;
            currentUserName = data.name;

            document.getElementById('loginPage').classList.add('hidden');
            document.getElementById('email').value    = '';
            document.getElementById('password').value = '';

            if (currentRole === 'admin') {
                // ── Route to Admin Dashboard ──
                document.getElementById('adminPage').classList.remove('hidden');
                document.getElementById('adminFullName').textContent = data.name;
                adminNavigate('dashboard');
                adminLoadDashboardCounts();
            } else {
                // ── Route to Customer Dashboard ──
                document.getElementById('dashboardPage').classList.remove('hidden');
                document.getElementById('userFullName').textContent    = data.name;
                document.getElementById('welcomeText').textContent     = `Welcome back, ${data.name}!`;
                document.getElementById('userRoleDisplay').textContent = 'Customer';
                document.getElementById('sidebarRole').textContent     = 'Customer Account';
                loadDashboard(data.id);
            }
        } else {
            errBox.textContent = data.message || 'Login failed. Please check your credentials.';
            errBox.classList.remove('hidden');
        }
    } catch (e) {
        errBox.textContent = 'Cannot connect to server. Is Spring Boot running?';
        errBox.classList.remove('hidden');
        console.error(e);
    }
}

// ── Logout ───────────────────────────────────────────────────
function logout() {
    fetch('/api/auth/logout', { method: 'POST' }).catch(() => {});
    currentUserId   = null;
    currentUserName = null;
    document.getElementById('dashboardPage').classList.add('hidden');
    document.getElementById('adminPage').classList.add('hidden');
    document.getElementById('adminDropdown').classList.add('hidden');
    document.getElementById('loginPage').classList.remove('hidden');
    navigate('dashboard');
}

// ── Navigation ───────────────────────────────────────────────
function navigate(page) {
    // Hide all pages
    ['dashboard', 'vehicles', 'bookings', 'history', 'reviews'].forEach(p => {
        document.getElementById('page-' + p).classList.add('hidden');
        document.getElementById('nav-' + p).classList.remove('bg-[#14b8a6]', 'text-white');
        document.getElementById('nav-' + p).classList.add('hover:bg-slate-800');
    });

    // Show selected page
    document.getElementById('page-' + page).classList.remove('hidden');
    document.getElementById('nav-' + page).classList.add('bg-[#14b8a6]', 'text-white');
    document.getElementById('nav-' + page).classList.remove('hover:bg-slate-800');
}

// ── Profile modal ────────────────────────────────────────────
async function showProfileModal() {
    if (!currentUserId) {
        alert('Please login first');
        return;
    }

    // Clear previous messages
    document.getElementById('profileError').classList.add('hidden');
    document.getElementById('profileSuccess').classList.add('hidden');
    document.getElementById('profilePassword').value = '';

    try {
        const res      = await fetch(`/api/customers/${currentUserId}`);
        const customer = await res.json();

        document.getElementById('profileName').value    = customer.name    || '';
        document.getElementById('profileEmail').value   = customer.email   || '';
        document.getElementById('profilePhone').value   = customer.phone   || '';
        document.getElementById('profileAddress').value = customer.address || '';

        document.getElementById('profileModal').classList.remove('hidden');
    } catch (e) {
        alert('Could not load profile data. Please try again.');
        console.error(e);
    }
}

function hideProfileModal() {
    document.getElementById('profileModal').classList.add('hidden');
}

// ── Update profile  ───────────────────────────────
async function updateProfile() {
    if (!currentUserId) {
        alert('User ID not found. Please login again.');
        return;
    }

    const name     = document.getElementById('profileName').value.trim();
    const email    = document.getElementById('profileEmail').value.trim();
    const phone    = document.getElementById('profilePhone').value.trim();
    const address  = document.getElementById('profileAddress').value.trim();
    const password = document.getElementById('profilePassword').value.trim();

    const errBox     = document.getElementById('profileError');
    const successBox = document.getElementById('profileSuccess');
    const saveBtn    = document.getElementById('saveBtn');

    errBox.classList.add('hidden');
    successBox.classList.add('hidden');

    if (!phone && !address && !password && !name && !email) {
        errBox.textContent = 'Please enter at least one field to update.';
        errBox.classList.remove('hidden');
        return;
    }

    if (!name) {
        errBox.textContent = 'Name cannot be empty.';
        errBox.classList.remove('hidden');
        return;
    }
    if (!email) {
        errBox.textContent = 'Email cannot be empty.';
        errBox.classList.remove('hidden');
        return;
    }
    if (!email.endsWith('@gmail.com')) {
        errBox.textContent = 'Email must be a @gmail.com address.';
        errBox.classList.remove('hidden');
        return;
    }
    if (phone && !/^\d{10}$/.test(phone)) {
        errBox.textContent = 'Phone number must be exactly 10 digits (e.g. 0771234567).';
        errBox.classList.remove('hidden');
        return;
    }
    if (password && password.length < 6) {
        errBox.textContent = 'New password must be at least 6 characters.';
        errBox.classList.remove('hidden');
        return;
    }

    const updateData = {
        name:     name,
        email:    email,
        phone:    phone    || null,
        address:  address  || null,
        password: password || null
    };

    saveBtn.disabled = true;
    saveBtn.textContent = 'Saving...';

    try {
        const res = await fetch(`/api/customers/${currentUserId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updateData)
        });

        if (res.ok) {
            successBox.textContent = '✅ Profile updated successfully!';
            successBox.classList.remove('hidden');
            document.getElementById('profilePassword').value = '';
            document.getElementById('userFullName').textContent = name;
            document.getElementById('welcomeText').textContent  = `Welcome back, ${name}!`;
            currentUserName = name;
            setTimeout(() => {
                hideProfileModal();
            }, 1500);
        } else {
            const errData = await res.json().catch(() => null);
            const msg = errData?.message || 'Failed to update profile. Please try again.';
            errBox.textContent = '❌ ' + msg;
            errBox.classList.remove('hidden');
        }
    } catch (e) {
        errBox.textContent = 'Error connecting to server.';
        errBox.classList.remove('hidden');
    } finally {
        saveBtn.disabled    = false;
        saveBtn.textContent = 'Save Changes';
    }
}

// ── Delete account ───────────────────────────────────────────
async function deleteAccount() {
    if (!confirm('⚠️ WARNING: This will permanently delete your account and ALL your data.\n\nThis cannot be undone. Are you absolutely sure?')) {
        return;
    }

    try {
        const res = await fetch(`/api/customers/${currentUserId}`, { method: 'DELETE' });
        if (res.ok) {
            alert('Your account has been permanently deleted.');
            logout();
        } else {
            alert('Failed to delete account. Please try again.');
        }
    } catch (e) {
        alert('Error deleting account.');
    }
}

// ── Register modal ───────────────────────────────────────────
function showRegisterModal() {
    document.getElementById('regError').classList.add('hidden');
    document.getElementById('regSuccess').classList.add('hidden');
    document.getElementById('registerModal').classList.remove('hidden');
    selectRegisterRole('customer');
}

function hideRegisterModal() {
    document.getElementById('registerModal').classList.add('hidden');
    ['regName', 'regEmail', 'regPhone', 'regAddress', 'regPassword', 'regConfirmPassword'].forEach(id => {
        document.getElementById(id).value = '';
    });
}

function selectRegisterRole(role) {
    registerRole = role;
    const isCustomer = role === 'customer';

    document.getElementById('regCustomerTab').classList.toggle('border-[#14b8a6]', isCustomer);
    document.getElementById('regCustomerTab').classList.toggle('bg-[#14b8a6]/10', isCustomer);
    document.getElementById('regCustomerTab').classList.toggle('border-transparent', !isCustomer);

    document.getElementById('regAdminTab').classList.toggle('border-[#14b8a6]', !isCustomer);
    document.getElementById('regAdminTab').classList.toggle('bg-[#14b8a6]/10', !isCustomer);
    document.getElementById('regAdminTab').classList.toggle('border-transparent', isCustomer);

    document.getElementById('regAddressField').classList.toggle('hidden', !isCustomer);
}

// ── Register user ────────────────────────────────
async function registerUser() {
    const name            = document.getElementById('regName').value.trim();
    const email           = document.getElementById('regEmail').value.trim();
    const phone           = document.getElementById('regPhone').value.trim();
    const address         = document.getElementById('regAddress').value.trim();
    const password        = document.getElementById('regPassword').value.trim();
    const confirmPassword = document.getElementById('regConfirmPassword').value.trim();

    const errBox     = document.getElementById('regError');
    const successBox = document.getElementById('regSuccess');

    errBox.classList.add('hidden');
    successBox.classList.add('hidden');

    if (!name || !email || !password) {
        errBox.textContent = 'Name, email and password are required.';
        errBox.classList.remove('hidden');
        return;
    }
    if (!email.endsWith('@gmail.com')) {
        errBox.textContent = 'Email must be a @gmail.com address.';
        errBox.classList.remove('hidden');
        return;
    }
    if (password !== confirmPassword) {
        errBox.textContent = 'Passwords do not match.';
        errBox.classList.remove('hidden');
        return;
    }

    const endpoint = registerRole === 'customer'
        ? '/api/auth/register/customer'
        : '/api/auth/register/staff';

    const bodyData = registerRole === 'customer'
        ? { name, email, phone, address, password, membershipType: 'Regular' }
        : { name, email, phone, password, role: 'STAFF' };

    try {
        const res  = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bodyData)
        });
        const data = await res.json();

        if (res.ok) {
            successBox.textContent = '✅ Registration successful! You can now sign in.';
            successBox.classList.remove('hidden');
            setTimeout(() => {
                hideRegisterModal();
            }, 2000);
        } else {
            errBox.textContent = data.message || '❌ Registration failed.';
            errBox.classList.remove('hidden');
        }
    } catch (e) {
        errBox.textContent = 'Error connecting to server.';
        errBox.classList.remove('hidden');
    }
}

// ── Load dashboard with real customer data ───────────────────
async function loadDashboard(customerId) {
    try {
        const res      = await fetch(`/api/customers/${customerId}`);
        const customer = await res.json();

        document.getElementById('membershipDisplay').textContent =
            customer.membershipType || 'Regular';

        if (customer.createdAt) {
            const date = new Date(customer.createdAt);
            const formatted = date.toLocaleDateString('en-US', {
                year: 'numeric', month: 'short', day: 'numeric'
            });
            document.getElementById('memberSinceDisplay').textContent = formatted;
        } else {
            document.getElementById('memberSinceDisplay').textContent = 'N/A';
        }

    } catch (e) {
        console.warn('Could not load customer data for dashboard:', e);
        document.getElementById('membershipDisplay').textContent = 'Regular';
        document.getElementById('memberSinceDisplay').textContent  = '—';
    }

    tryLoadCount(`/api/vehicles/customer/${customerId}/count`, 'vehiclesCount', 'vehiclesBadge');
    tryLoadCount(`/api/bookings/customer/${customerId}/active/count`, 'bookingsCount', 'bookingsBadge');
    tryLoadCount(`/api/bookings/customer/${customerId}/completed/count`, 'completedCount', 'completedBadge');
}

async function tryLoadCount(url, countElementId, badgeElementId) {
    try {
        const res = await fetch(url);
        if (res.ok) {
            const data = await res.json();
            const value = typeof data === 'number' ? data : (data.count ?? '—');
            document.getElementById(countElementId).textContent = value;
            const badge = document.getElementById(badgeElementId);
            badge.textContent = 'live';
            badge.className = 'text-xs bg-green-900/40 text-green-400 px-2 py-1 rounded-full';
        }
    } catch (e) {}
}

// ── Background particles ─────────────────────────────────────
function createBackground() {
    const bg = document.getElementById('bgAnimation');
    for (let i = 0; i < 40; i++) {
        const p           = document.createElement('div');
        p.className       = 'particle';
        p.textContent     = ['🔧', '🛠️', '🚗'][Math.floor(Math.random() * 3)];
        p.style.left      = Math.random() * 100 + 'vw';
        p.style.animationDuration = (20 + Math.random() * 20) + 's';
        p.style.animationDelay    = (Math.random() * 10) + 's';
        bg.appendChild(p);
    }
}

// ── Allow Enter key to submit login ─────────────────────────
document.addEventListener('keydown', function(e) {
    if (e.key === 'Enter') {
        const loginPage = document.getElementById('loginPage');
        if (loginPage && !loginPage.classList.contains('hidden')) {
            login();
        }
    }
});

// ── Add Staff ────────────────────────────────────────────────
async function addStaff() {
    const name     = document.getElementById('staffName').value.trim();
    const email    = document.getElementById('staffEmail').value.trim();
    const phone    = document.getElementById('staffPhone').value.trim();
    const password = document.getElementById('staffPassword').value.trim();
    const errBox   = document.getElementById('staffError');
    const okBox    = document.getElementById('staffSuccess');

    errBox.classList.add('hidden');
    okBox.classList.add('hidden');

    if (!name || !email || !password) {
        errBox.textContent = 'Required fields missing.';
        errBox.classList.remove('hidden');
        return;
    }

    try {
        const res  = await fetch('/api/auth/register/staff', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, phone, password, role: 'STAFF' })
        });
        if (res.ok) {
            okBox.textContent = '✅ Staff created!';
            okBox.classList.remove('hidden');
            loadStaffList();
        } else {
            errBox.textContent = 'Failed to create staff.';
            errBox.classList.remove('hidden');
        }
    } catch (e) {
        errBox.textContent = 'Connection error.';
        errBox.classList.remove('hidden');
    }
}

// ── Add Admin ────────────────────────────────────────────────
async function addAdmin() {
    const name     = document.getElementById('newAdminName').value.trim();
    const email    = document.getElementById('newAdminEmail').value.trim();
    const phone    = document.getElementById('newAdminPhone').value.trim();
    const password = document.getElementById('newAdminPassword').value.trim();
    const errBox   = document.getElementById('newAdminError');
    const okBox    = document.getElementById('newAdminSuccess');

    errBox.classList.add('hidden');
    okBox.classList.add('hidden');

    try {
        const res  = await fetch('/api/auth/register/staff', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, phone, password, role: 'ADMIN' })
        });
        if (res.ok) {
            okBox.textContent = '✅ Admin created!';
            okBox.classList.remove('hidden');
            loadStaffList();
        } else {
            errBox.textContent = 'Failed to create admin.';
            errBox.classList.remove('hidden');
        }
    } catch (e) {
        errBox.textContent = 'Connection error.';
        errBox.classList.remove('hidden');
    }
}

// ── Load Staff List ──────────────────────────────────────────
async function loadStaffList() {
    const tbody = document.getElementById('staffTableBody');
    tbody.innerHTML = '<tr><td colspan="5" class="px-6 py-8 text-center text-slate-500">Loading...</td></tr>';
    try {
        const res   = await fetch('/api/staff');
        if (!res.ok) {
            tbody.innerHTML = '<tr><td colspan="5" class="px-6 py-8 text-center text-slate-500">API error.</td></tr>';
            return;
        }
        const staff = await res.json();
        tbody.innerHTML = staff.map(s => `
            <tr class="border-b border-slate-700/50 hover:bg-slate-800/40">
                <td class="px-6 py-4 font-medium">${s.name || '—'}</td>
                <td class="px-6 py-4 text-slate-400">${s.email || '—'}</td>
                <td class="px-6 py-4 text-slate-300">${s.phone || '—'}</td>
                <td class="px-6 py-4">
                    <span class="px-2 py-1 rounded-full text-xs font-medium ${s.role === 'ADMIN' ? 'bg-purple-900/40 text-purple-400 border border-purple-700/40' : 'bg-blue-900/40 text-blue-400 border border-blue-700/40'}">
                        ${s.role || 'STAFF'}
                    </span>
                </td>
                <td class="px-6 py-4 text-right">
                    <button onclick="deleteStaff(${s.id})" class="text-slate-400 hover:text-red-400"><i class="fa fa-trash text-sm"></i></button>
                </td>
            </tr>
        `).join('');
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="5" class="px-6 py-8 text-center text-slate-500">API error.</td></tr>';
    }
}

async function deleteStaff(id) {
    if (!confirm('Delete staff?')) return;
    try {
        const res = await fetch(`/api/staff/${id}`, { method: 'DELETE' });
        if (res.ok) loadStaffList();
    } catch (e) {}
}

// ── Admin navigation ─────────────────────────────────────────
const adminPages = ['dashboard','customers','vehicles','services','bookings','adminpanel','history'];

function adminNavigate(page) {
    adminPages.forEach(p => {
        const pageEl = document.getElementById('apage-' + p);
        if (pageEl) pageEl.classList.add('hidden');
        const nav = document.getElementById('anav-' + p);
        if (nav) {
            nav.classList.remove('bg-[#14b8a6]', 'text-white');
            nav.classList.add('hover:bg-slate-800');
        }
    });
    const activePage = document.getElementById('apage-' + page);
    if (activePage) activePage.classList.remove('hidden');
    const activeNav = document.getElementById('anav-' + page);
    if (activeNav) {
        activeNav.classList.add('bg-[#14b8a6]', 'text-white');
        activeNav.classList.remove('hover:bg-slate-800');
    }
    if (page === 'customers') loadAdminCustomers();
    if (page === 'adminpanel') loadStaffList();
}

// ── Admin dropdown toggle ────────────────────────────────────
function toggleAdminDropdown() {
    const dropdown = document.getElementById('adminDropdown');
    if (dropdown) dropdown.classList.toggle('hidden');
}

// ── Sidebar collapse ─────────────────────────────────────────
let sidebarCollapsed = false;
function toggleSidebar() {
    sidebarCollapsed = !sidebarCollapsed;
    const sidebar = document.getElementById('adminSidebar');
    const labels  = document.querySelectorAll('.admin-nav-label');
    const text    = document.getElementById('adminSidebarText');
    const icon    = document.getElementById('collapseIcon');

    if (sidebarCollapsed) {
        sidebar.style.width = '64px';
        labels.forEach(l => l.classList.add('hidden'));
        if (text) text.classList.add('hidden');
        if (icon) {
            icon.classList.remove('fa-chevron-left');
            icon.classList.add('fa-chevron-right');
        }
    } else {
        sidebar.style.width = '256px';
        labels.forEach(l => l.classList.remove('hidden'));
        if (text) text.classList.remove('hidden');
        if (icon) {
            icon.classList.remove('fa-chevron-right');
            icon.classList.add('fa-chevron-left');
        }
    }
}

// ── Admin dashboard counts ───────────────────────────────────
async function adminLoadDashboardCounts() {
    try {
        const res       = await fetch('/api/customers');
        const customers = await res.json();
        if (Array.isArray(customers)) {
            document.getElementById('adminTotalCustomers').textContent = customers.length;
        }
    } catch (e) {}

    tryAdminCount('/api/vehicles/count',  'adminTotalVehicles');
    tryAdminCount('/api/bookings/active/count', 'adminActiveBookings');
}

async function tryAdminCount(url, elementId) {
    try {
        const res = await fetch(url);
        if (res.ok) {
            const data  = await res.json();
            const value = typeof data === 'number' ? data : (data.count ?? '—');
            document.getElementById(elementId).textContent = value;
        }
    } catch (e) {}
}

// ── Admin Customers table ────────────────────────────────────
async function loadAdminCustomers() {
    const tbody = document.getElementById('customerTableBody');
    tbody.innerHTML = '<tr><td colspan="6" class="px-6 py-10 text-center text-slate-500">Loading...</td></tr>';
    try {
        const res       = await fetch('/api/customers');
        const customers = await res.json();
        tbody.innerHTML = customers.map(c => `
            <tr class="border-b border-slate-700/50 hover:bg-slate-800/40" data-name="${(c.name||'').toLowerCase()}" data-email="${(c.email||'').toLowerCase()}">
                <td class="px-6 py-4 font-medium">${c.name || '—'}</td>
                <td class="px-6 py-4 text-slate-400">${c.email || '—'}</td>
                <td class="px-6 py-4 text-slate-300">${c.phone || '—'}</td>
                <td class="px-6 py-4 text-slate-300">${c.membershipType || 'Regular'}</td>
                <td class="px-6 py-4"><span class="px-2 py-1 rounded-full text-xs font-medium bg-teal-900/40 text-teal-400 border border-teal-700/40">Active</span></td>
                <td class="px-6 py-4 text-right">
                    <button onclick="adminEditCustomer(${c.id})" class="text-slate-400 hover:text-[#14b8a6] mr-3"><i class="fa fa-pen text-sm"></i></button>
                    <button onclick="adminDeleteCustomer(${c.id})" class="text-slate-400 hover:text-red-400"><i class="fa fa-trash text-sm"></i></button>
                </td>
            </tr>
        `).join('');
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="6" class="px-6 py-10 text-center text-red-400">Error.</td></tr>';
    }
}

function filterCustomers() {
    const q    = document.getElementById('customerSearchInput').value.toLowerCase();
    const rows = document.querySelectorAll('#customerTableBody tr[data-name]');
    rows.forEach(row => {
        const match = row.dataset.name.includes(q) || row.dataset.email.includes(q);
        row.style.display = match ? '' : 'none';
    });
}

async function adminDeleteCustomer(id) {
    if (!confirm('Delete customer?')) return;
    try {
        const res = await fetch(`/api/customers/${id}`, { method: 'DELETE' });
        if (res.ok) {
            loadAdminCustomers();
            adminLoadDashboardCounts();
        }
    } catch (e) {}
}

function adminEditCustomer(id) {
    alert(`Edit customer ID ${id} — endpoint stub.`);
}

function showAddCustomerModal() {
    document.getElementById('addCustomerModal').classList.remove('hidden');
}
function hideAddCustomerModal() {
    document.getElementById('addCustomerModal').classList.add('hidden');
}

async function adminAddCustomer() {
    const name     = document.getElementById('addCustName').value.trim();
    const email    = document.getElementById('addCustEmail').value.trim();
    const password = document.getElementById('addCustPassword').value.trim();
    if (!name || !email || !password) return;

    try {
        const res  = await fetch('/api/auth/register/customer', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password, membershipType: 'Regular' })
        });
        if (res.ok) {
            hideAddCustomerModal();
            loadAdminCustomers();
            adminLoadDashboardCounts();
        }
    } catch (e) {}
}

// ── Init ─────────────────────────────────────────────────────
window.onload = () => {
    createBackground();
    selectRole('customer');
    navigate('dashboard');
};
