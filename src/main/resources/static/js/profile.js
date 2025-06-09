document.addEventListener("DOMContentLoaded", function () {
    const reviewsButton = document.getElementById("reviewsButton");
    const salesButton = document.getElementById("salesButton");
    const activeItemsButton = document.getElementById("activeItemsButton");

    const reviewsBlock = document.getElementById("reviewsBlock");
    const salesBlock = document.getElementById("salesBlock");
    const activeItemsBlock = document.getElementById("activeItemsBlock");

    reviewsBlock.classList.remove("d-none");

    function setActiveButton(activeButton) {
        [reviewsButton, salesButton, activeItemsButton].forEach(button => {
            button.classList.remove("btn-primary");
            button.classList.add("btn-outline-primary");
        });
        activeButton.classList.add("btn-primary");
        activeButton.classList.remove("btn-outline-primary");
    }
    reviewsButton.addEventListener("click", function () {
        setActiveButton(reviewsButton);
        reviewsBlock.classList.remove("d-none");
        salesBlock.classList.add("d-none");
        activeItemsBlock.classList.add("d-none");
    });
    salesButton.addEventListener("click", function () {
        setActiveButton(salesButton);
        salesBlock.classList.remove("d-none");
        reviewsBlock.classList.add("d-none");
        activeItemsBlock.classList.add("d-none");
    });
    activeItemsButton.addEventListener("click", function () {
        setActiveButton(activeItemsButton);
        activeItemsBlock.classList.remove("d-none");
        reviewsBlock.classList.add("d-none");
        salesBlock.classList.add("d-none");
    });
});