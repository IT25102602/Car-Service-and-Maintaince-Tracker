const API_BASE = '/api';

async function fetchData(endpoint) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`);
        if (!response.ok) throw new Error('Network response was not ok');
        return await response.json();
    } catch (error) {
        console.error('Fetch error:', error);
        showToast('Error loading data', 'danger');
        return null;
    }
}

async function postData(endpoint, data, method = 'POST') {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) throw new Error('Action failed');
        return true;
    } catch (error) {
        console.error('Post error:', error);
        showToast('Action failed', 'danger');
        return false;
    }
}

async function deleteData(endpoint) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Delete failed');
        return true;
    } catch (error) {
        console.error('Delete error:', error);
        showToast('Delete failed', 'danger');
        return false;
    }
}

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.style.display = 'block';
    toast.style.background = type === 'danger' ? '#ef4444' : '#10b981';
    
    setTimeout(() => {
        toast.style.display = 'none';
    }, 3000);
}
