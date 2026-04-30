// Tab switching
function switchTab(tabIndex) {
    document.querySelectorAll('.tab-button').forEach(tab => {
        tab.classList.remove('active');
    });

    if (tabIndex === 0) {
        document.getElementById('tab-upcoming').classList.add('active');
        document.getElementById('upcoming-content').classList.remove('hidden');
        document.getElementById('past-content').classList.add('hidden');
    } else {
        document.getElementById('tab-past').classList.add('active');
        document.getElementById('upcoming-content').classList.add('hidden');
        document.getElementById('past-content').classList.remove('hidden');
    }
}

// Demo actions
function newBooking() {
    window.location.href = '/HTML/new-booking.html';
}

function payNow() {
    const btn = event.currentTarget;
    btn.innerHTML = `
        <i class="fa-solid fa-spinner fa-spin mr-3"></i>
        Processing...
    `;
    btn.disabled = true;

    setTimeout(() => {
        alert("✅ Payment successful!\n\nBooking marked as Paid");
        btn.innerHTML = `
            <i class="fa-solid fa-check mr-3"></i>
            Paid
        `;
        btn.classList.remove('bg-[#2563eb]');
        btn.classList.add('bg-emerald-500');
    }, 1500);
}

// Console message
console.log('%c✅ CarService My Bookings UI ready!', 'color:#14b8a6; font-family:monospace; font-size:13px');

window.addEventListener('load', () => {
    console.log('🚗 CarService pages connected and ready!');
});