document.addEventListener('DOMContentLoaded', () => {
    const stars = document.querySelectorAll('.rating-stars i');
    const ratingInput = document.getElementById('ratingValue');

    stars.forEach(star => {
        star.addEventListener('click', () => {
            const rating = star.getAttribute('data-rating');
            ratingInput.value = rating;
            updateStars(rating);
        });

        star.addEventListener('mouseover', () => {
            const rating = star.getAttribute('data-rating');
            updateStars(rating, true);
        });
    });

    document.querySelector('.rating-stars').addEventListener('mouseleave', () => {
        updateStars(ratingInput.value);
    });

    document.getElementById('feedbackForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const rating = parseInt(ratingInput.value);
        if (rating === 0) {
            showToast('Please select a rating', 'danger');
            return;
        }

        const data = {
            reviewId: 'R' + Date.now(),
            serviceId: document.getElementById('serviceId').value,
            customerName: document.getElementById('customerName').value,
            rating: rating,
            reviewMessage: document.getElementById('reviewMessage').value,
            date: new Date().toISOString().split('T')[0],
            reviewType: document.getElementById('reviewType').value
        };

        const success = await postData('/reviews', data);
        if (success) {
            showToast('Review submitted successfully!');
            document.getElementById('feedbackForm').reset();
            updateStars(0);
        }
    });
});

function updateStars(rating, isHover = false) {
    const stars = document.querySelectorAll('.rating-stars i');
    stars.forEach(s => {
        const sRating = s.getAttribute('data-rating');
        if (sRating <= rating) {
            s.classList.remove('far');
            s.classList.add('fas');
        } else {
            s.classList.remove('fas');
            s.classList.add('far');
        }
    });
}
