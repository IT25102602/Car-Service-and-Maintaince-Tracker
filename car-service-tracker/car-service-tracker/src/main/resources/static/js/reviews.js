document.addEventListener('DOMContentLoaded', () => {
    loadReviews();
    loadStats();
});

async function loadReviews() {
    const reviews = await fetchData('/reviews');
    if (reviews) {
        const ratingFilter = document.getElementById('ratingFilter').value;
        let filtered = reviews;
        if (ratingFilter) {
            filtered = reviews.filter(r => r.rating == ratingFilter);
        }
        renderReviews(filtered);
    }
}

async function loadStats() {
    const stats = await fetchData('/reviews/stats');
    if (stats) {
        document.getElementById('avgRating').textContent = stats.averageRating.toFixed(1);
        document.getElementById('totalReviewsCount').textContent = stats.totalReviews;
        renderAvgStars(stats.averageRating);
    }
}

function renderReviews(reviews) {
    const container = document.getElementById('reviewsContainer');
    const emptyState = document.getElementById('reviewsEmptyState');
    container.innerHTML = '';

    if (reviews.length === 0) {
        emptyState.style.display = 'block';
        return;
    }

    emptyState.style.display = 'none';
    reviews.forEach(r => {
        const card = document.createElement('div');
        card.className = 'review-card';
        
        let stars = '';
        for (let i = 1; i <= 5; i++) {
            stars += `<i class="${i <= r.rating ? 'fas' : 'far'} fa-star"></i> `;
        }

        const badgeClass = r.reviewType === 'Verified' ? 'badge-verified' : 'badge-public';
        const badgeText = r.reviewType === 'Verified' ? '<i class="fas fa-check-circle"></i> Verified Service Customer' : 'Public Review';

        card.innerHTML = `
            <div class="rating-stars">${stars}</div>
            <p style="font-weight: 600; margin-bottom: 0.5rem;">${r.customerName}</p>
            <p style="color: var(--text-secondary); margin-bottom: 1rem;">"${r.reviewMessage}"</p>
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <span class="badge ${badgeClass}">${badgeText}</span>
                <span style="font-size: 0.75rem; color: #9ca3af;">${r.date}</span>
            </div>
        `;
        container.appendChild(card);
    });
}

function renderAvgStars(avg) {
    const container = document.getElementById('avgStars');
    container.innerHTML = '';
    for (let i = 1; i <= 5; i++) {
        const star = document.createElement('i');
        if (i <= Math.round(avg)) {
            star.className = 'fas fa-star';
        } else {
            star.className = 'far fa-star';
        }
        container.appendChild(star);
    }
}
