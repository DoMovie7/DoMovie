document.addEventListener('DOMContentLoaded', function() {
            const rating = document.querySelector('.rating');
            const ratingInputs = rating.querySelectorAll('.rating__input');
            const stars = rating.querySelectorAll('.star-icon');
            const focusBar = rating.querySelector('.rating__focus');

            function updateStars(activeIndex) {
                stars.forEach((star, index) => {
                    if (index <= activeIndex) {
                        star.classList.add('filled');
                    } else {
                        star.classList.remove('filled');
                    }
                });
            }

            function handleMouseMove(event) {
                const rect = rating.getBoundingClientRect();
                const width = rect.width;
                const x = event.clientX - rect.left;
                const activeIndex = Math.floor((x / width) * stars.length);
                
                updateStars(activeIndex);
                focusBar.style.width = `${(activeIndex + 1) * (100 / stars.length)}%`;
            }

            function handleMouseLeave() {
                const checkedInput = rating.querySelector('.rating__input:checked');
                const activeIndex = checkedInput ? Array.from(ratingInputs).indexOf(checkedInput) : -1;
                
                updateStars(activeIndex);
                focusBar.style.width = `${(activeIndex + 1) * (100 / stars.length)}%`;
            }

            function handleClick(event) {
                if (event.target.classList.contains('rating__input')) {
                    const activeIndex = Array.from(ratingInputs).indexOf(event.target);
                    updateStars(activeIndex);
                    focusBar.style.width = `${(activeIndex + 1) * (100 / stars.length)}%`;
                }
            }

            rating.addEventListener('mousemove', handleMouseMove);
            rating.addEventListener('mouseleave', handleMouseLeave);
            rating.addEventListener('click', handleClick);
        });