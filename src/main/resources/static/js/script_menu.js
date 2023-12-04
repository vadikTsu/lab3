
function closeNav() {
    document.getElementById("sidebar").style.width = "0";
    document.getElementById("main").style.marginLeft= "0";
}

function toggleNav() {
    var sidebar = document.getElementById("sidebar");
    var mainContent = document.getElementById("main");
    var searchContainer = document.querySelector(".search-container");

    if (sidebar.style.width === "250px") {
        sidebar.style.width = "0";
        mainContent.style.marginLeft = "0";
        searchContainer.style.marginLeft = "0";
    } else {
        sidebar.style.width = "250px";
        mainContent.style.marginLeft = "250px";
        searchContainer.style.marginLeft = "250px";
    }
}


function debounce(func, delay) {
    let debounceTimer;
    return function() {
        const context = this;
        const args = arguments;
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => func.apply(context, args), delay);
    };
}


//todo
document.getElementById('search-box').addEventListener('input', debounce(function(event) {
    const searchTerm = event.target.value;

    if (searchTerm.length > 2) {
        fetch('/search?term=' + encodeURIComponent(searchTerm))
            .then(response => response.json())
            .then(data => {
                let suggestions = data.map(item => `<div class='suggestion-item' data-value='${item.title}'>${item.title}</div>`).join('');
                const suggestionBox = document.getElementById('suggestion-box');
                suggestionBox.innerHTML = suggestions;
                suggestionBox.style.display = 'block';

                document.querySelectorAll('.suggestion-item').forEach(item => {
                    item.addEventListener('click', function() {
                        const value = this.getAttribute('data-value');
                        window.location.href = '/fetch?param=' + encodeURIComponent(value);
                    });
                });
            });
    } else {
        document.getElementById('suggestion-box').style.display = 'none';
    }
}, 300));

function copyEmailToClipboard() {
    const email = document.getElementById('emailLink').textContent;
    navigator.clipboard.writeText(email).then(() => {
        alert('copied to clipboard!');
    }).catch(err => {
        alert('shit!');
    });
}



function redirectToMenu() {
    closeNav();
    var ripple = document.createElement('div');
    ripple.classList.add('ripple');
    ripple.style.top = '50%';
    ripple.style.left = '50%';
    document.body.appendChild(ripple);
    ripple.addEventListener('animationend', function() {

        setTimeout(function() {
            window.location.href = '/';
        }, 2000);
    });
}

const dropZone = document.getElementById('drop-zone');
const fileInput = document.getElementById('file-upload');
const form = document.getElementById('file-upload-form');

dropZone.addEventListener('click', () => fileInput.click());

dropZone.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropZone.classList.add('active');
});

dropZone.addEventListener('dragleave', (e) => {
    dropZone.classList.remove('active');
});

dropZone.addEventListener('drop', (e) => {
    e.preventDefault();
    dropZone.classList.remove('active');
    if (e.dataTransfer.files.length) {
        fileInput.files = e.dataTransfer.files;
        form.submit();
    }
});

fileInput.addEventListener('change', () => {
    if (fileInput.files.length) {
        form.submit();
    }
});