const urlParams = new URLSearchParams(window.location.search);
const isSuccess = urlParams.get('success');
if (isSuccess === 'true') {
    const toast = document.getElementById('toast');
    toast.classList.add('show');
}


