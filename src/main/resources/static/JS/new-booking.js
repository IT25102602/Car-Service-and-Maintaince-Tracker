/* ════════════════════════════════════════════════
   CarService — New Booking Page JavaScript
   Handles: stepper, vehicle selection, service selection,
   scheduling, summary updates, and booking confirmation
════════════════════════════════════════════════ */

// ─── STATE ───
let currentStep = 1;
const totalSteps = 4;

let selectedVehicle = {
    name: 'Toyota Camry',
    plate: 'ABC-1234',
    year: '2021',
    notes: ''
};

let selectedServices = [];
let selectedDate = '';
let selectedTime = '';

// ─── DOM READY ───
document.addEventListener('DOMContentLoaded', () => {
    initDateInput();
    updateSummary();
    updateStepper();
});

// ═══════════════════════════════════════════════
// SIDEBAR
// ═══════════════════════════════════════════════
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');
    const collapseIcon = document.getElementById('collapseIcon');

    sidebar.classList.toggle('collapsed');
    mainContent.classList.toggle('expanded');

    // Rotate collapse icon
    if (sidebar.classList.contains('collapsed')) {
        collapseIcon.style.transform = 'rotate(180deg)';
    } else {
        collapseIcon.style.transform = 'rotate(0deg)';
    }
}

// ═══════════════════════════════════════════════
// STEPPER NAVIGATION
// ═══════════════════════════════════════════════
function nextStep() {
    if (!validateStep(currentStep)) return;

    if (currentStep < totalSteps) {
        currentStep++;
        showPanel(currentStep);
        updateStepper();
        updateNavButtons();
    } else {
        confirmBooking();
    }
}

function prevStep() {
    if (currentStep > 1) {
        currentStep--;
        showPanel(currentStep);
        updateStepper();
        updateNavButtons();
    }
}

function showPanel(step) {
    // Hide all panels
    document.querySelectorAll('.panel').forEach(panel => {
        panel.classList.remove('active');
    });

    // Show current panel
    const panel = document.getElementById(`panel-${step}`);
    if (panel) {
        panel.classList.add('active');
    }

    // Update confirmation panel if on step 4
    if (step === 4) {
        updateConfirmPanel();
    }
}

function updateStepper() {
    const steps = document.querySelectorAll('.step');
    steps.forEach((step, index) => {
        const stepNum = index + 1;
        step.classList.remove('active', 'done');

        if (stepNum === currentStep) {
            step.classList.add('active');
        } else if (stepNum < currentStep) {
            step.classList.add('done');
        }
    });
}

function updateNavButtons() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    // Show/hide previous button
    prevBtn.style.display = currentStep === 1 ? 'none' : 'inline-flex';

    // Update next button text
    if (currentStep === totalSteps) {
        nextBtn.innerHTML = `Confirm Booking
            <svg viewBox="0 0 24 24" fill="none"><path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg>`;
    } else {
        nextBtn.innerHTML = `Continue
            <svg viewBox="0 0 24 24" fill="none"><path d="M5 12h14M12 5l7 7-7 7" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg>`;
    }
}

function validateStep(step) {
    switch (step) {
        case 1:
            if (!selectedVehicle.name) {
                showToast('Please select a vehicle');
                return false;
            }
            selectedVehicle.notes = document.getElementById('vehicleNotes').value;
            return true;

        case 2:
            if (selectedServices.length === 0) {
                showToast('Please select at least one service');
                return false;
            }
            return true;

        case 3:
            selectedDate = document.getElementById('bookingDate').value;
            selectedTime = document.getElementById('bookingTime').value;
            if (!selectedDate) {
                showToast('Please select a date');
                return false;
            }
            if (!selectedTime) {
                showToast('Please select a time slot');
                return false;
            }
            return true;

        case 4:
            return true;

        default:
            return true;
    }
}

// ═══════════════════════════════════════════════
// VEHICLE SELECTION
// ═══════════════════════════════════════════════
function selectVehicle(card, name, plate, year) {
    // Remove selected class from all cards
    document.querySelectorAll('.vehicle-card').forEach(c => {
        c.classList.remove('selected');
    });

    // Add selected class to clicked card
    card.classList.add('selected');

    // Update state
    selectedVehicle = { name, plate, year, notes: selectedVehicle.notes };

    // Update summary
    updateSummary();
}

// ═══════════════════════════════════════════════
// SERVICE SELECTION
// ═══════════════════════════════════════════════
function toggleService(card) {
    const serviceId = card.dataset.id;
    const serviceName = card.dataset.name;
    const servicePrice = parseInt(card.dataset.price, 10);
    const serviceCat = card.dataset.cat;

    if (card.classList.contains('selected')) {
        // Deselect
        card.classList.remove('selected');
        selectedServices = selectedServices.filter(s => s.id !== serviceId);
    } else {
        // Select
        card.classList.add('selected');
        selectedServices.push({
            id: serviceId,
            name: serviceName,
            price: servicePrice,
            category: serviceCat
        });
    }

    updateSummary();
}

function filterServices(btn, category) {
    // Update active pill
    document.querySelectorAll('.cat-pill').forEach(pill => {
        pill.classList.remove('active');
    });
    btn.classList.add('active');

    // Filter service cards
    const cards = document.querySelectorAll('.service-card');
    cards.forEach(card => {
        if (category === 'all' || card.dataset.cat === category) {
            card.classList.remove('hidden');
        } else {
            card.classList.add('hidden');
        }
    });
}

// ═══════════════════════════════════════════════
// SCHEDULING
// ═══════════════════════════════════════════════
function initDateInput() {
    const dateInput = document.getElementById('bookingDate');
    const today = new Date().toISOString().split('T')[0];
    dateInput.setAttribute('min', today);

    dateInput.addEventListener('change', () => {
        selectedDate = dateInput.value;
        updateSummary();
    });

    document.getElementById('bookingTime').addEventListener('change', (e) => {
        selectedTime = e.target.value;
        updateTimeSlots();
        updateSummary();
    });
}

function selectSlot(btn, time) {
    // Remove selected from all slots
    document.querySelectorAll('.time-slot').forEach(slot => {
        if (!slot.disabled) {
            slot.classList.remove('selected');
        }
    });

    // Add selected to clicked slot
    btn.classList.add('selected');

    // Update dropdown
    document.getElementById('bookingTime').value = time;
    selectedTime = time;

    updateSummary();
}

function updateTimeSlots() {
    const dropdownValue = document.getElementById('bookingTime').value;

    document.querySelectorAll('.time-slot').forEach(slot => {
        if (!slot.disabled) {
            slot.classList.remove('selected');
            if (slot.textContent.trim() === dropdownValue) {
                slot.classList.add('selected');
            }
        }
    });
}

// ═══════════════════════════════════════════════
// SUMMARY SIDEBAR
// ═══════════════════════════════════════════════
function updateSummary() {
    // Vehicle
    document.getElementById('sumVehicle').textContent = selectedVehicle.name || '—';
    document.getElementById('sumPlate').textContent = selectedVehicle.plate || '—';

    // Services
    const servicesList = document.getElementById('sumServicesList');
    if (selectedServices.length === 0) {
        servicesList.innerHTML = '<p class="sum-empty">No services selected yet</p>';
    } else {
        servicesList.innerHTML = selectedServices.map(s => `
            <div class="sum-service-item">
                <span class="sum-service-name">${s.name}</span>
                <span class="sum-service-price">LKR ${s.price.toLocaleString()}</span>
            </div>
        `).join('');
    }

    // Date & Time
    document.getElementById('sumDate').textContent = selectedDate
        ? formatDate(selectedDate)
        : '—';
    document.getElementById('sumTime').textContent = selectedTime || '—';

    // Total
    const total = selectedServices.reduce((sum, s) => sum + s.price, 0);
    document.getElementById('sumTotal').textContent = `LKR ${total.toLocaleString()}`;
}

// ═══════════════════════════════════════════════
// CONFIRMATION PANEL
// ═══════════════════════════════════════════════
function updateConfirmPanel() {
    // Vehicle
    document.getElementById('confirmVehicle').textContent =
        `${selectedVehicle.name} — ${selectedVehicle.plate} (${selectedVehicle.year})`;

    // Services
    const servicesList = document.getElementById('confirmServices');
    if (selectedServices.length === 0) {
        servicesList.innerHTML = '<li class="confirm-none">No services selected</li>';
    } else {
        servicesList.innerHTML = selectedServices.map(s => `
            <li>
                <span>${s.name}</span>
                <span>LKR ${s.price.toLocaleString()}</span>
            </li>
        `).join('');
    }

    // Schedule
    const scheduleText = selectedDate && selectedTime
        ? `${formatDate(selectedDate)} at ${selectedTime}`
        : 'Not selected';
    document.getElementById('confirmSchedule').textContent = scheduleText;
}

// ═══════════════════════════════════════════════
// BOOKING CONFIRMATION
// ═══════════════════════════════════════════════
function confirmBooking() {
    // Generate reference number
    const now = new Date();
    const dateStr = now.toISOString().slice(0, 10).replace(/-/g, '');
    const randomNum = String(Math.floor(Math.random() * 9000) + 1000).padStart(4, '0');
    const ref = `BK-${dateStr}-${randomNum}`;

    document.getElementById('successRef').textContent = ref;
    document.getElementById('successOverlay').classList.add('show');
}

function closeSuccess() {
    document.getElementById('successOverlay').classList.remove('show');
    resetForm();
}

function resetForm() {
    currentStep = 1;
    selectedVehicle = { name: 'Toyota Camry', plate: 'ABC-1234', year: '2021', notes: '' };
    selectedServices = [];
    selectedDate = '';
    selectedTime = '';

    // Reset UI
    document.querySelectorAll('.vehicle-card').forEach((card, i) => {
        card.classList.toggle('selected', i === 0);
    });

    document.querySelectorAll('.service-card').forEach(card => {
        card.classList.remove('selected');
    });

    document.querySelectorAll('.time-slot').forEach(slot => {
        if (!slot.disabled) slot.classList.remove('selected');
    });

    document.getElementById('vehicleNotes').value = '';
    document.getElementById('bookingDate').value = '';
    document.getElementById('bookingTime').value = '';

    // Reset filter to 'all'
    document.querySelectorAll('.cat-pill').forEach(pill => {
        pill.classList.remove('active');
    });
    document.querySelector('.cat-pill').classList.add('active');
    document.querySelectorAll('.service-card').forEach(card => {
        card.classList.remove('hidden');
    });

    showPanel(1);
    updateStepper();
    updateNavButtons();
    updateSummary();
}

// ═══════════════════════════════════════════════
// UTILITIES
// ═══════════════════════════════════════════════
function formatDate(dateString) {
    const date = new Date(dateString + 'T00:00:00');
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('en-US', options);
}

function showToast(message) {
    // Remove existing toast
    const existing = document.querySelector('.toast-notification');
    if (existing) existing.remove();

    // Create toast
    const toast = document.createElement('div');
    toast.className = 'toast-notification';
    toast.style.cssText = `
        position: fixed;
        bottom: 2rem;
        left: 50%;
        transform: translateX(-50%);
        background: var(--bg-card);
        border: 1px solid var(--border);
        border-left: 3px solid var(--red);
        color: var(--text);
        padding: .875rem 1.5rem;
        border-radius: var(--radius-sm);
        font-size: .875rem;
        z-index: 300;
        animation: fadeUp .3s ease;
        box-shadow: 0 8px 32px rgba(0,0,0,.4);
    `;
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transition = 'opacity .3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}