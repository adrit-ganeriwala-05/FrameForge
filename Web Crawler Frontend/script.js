const boxes = document.querySelectorAll('.box');
const modalOverlay = document.getElementById('modalOverlay');
const modalTitle = document.getElementById('modalTitle');
const modalContent = document.getElementById('modalContent');
const closeButton = document.getElementById('closeButton');
const themeToggle = document.getElementById('themeToggle');
const body = document.body;

const spinner = document.createElement('div');
spinner.className = 'spinner';

boxes.forEach(box => {
    box.addEventListener('click', async() => {
        // Show spinner
        modalOverlay.classList.add('show');
        modalContent.innerHTML = '';
        modalContent.appendChild(spinner);
        spinner.style.display = 'block';

        const choice = box.getAttribute('data-choice');
        const title = box.getAttribute('data-title');
        const imgSrc = box.getAttribute('data-img');
        modalTitle.textContent = title;

        try {
            // Send request to backend
            const response = await fetch(`http://127.0.0.1:8080/recommendations?choice=${encodeURIComponent(choice)}`);
            const textResponse = await response.text();

            // Parsing
            const [cpuDetails, gpuDetails] = textResponse.split('\n').filter(Boolean);

            const cpuMatch = cpuDetails.match(/Cheapest option for CPU: (.*?) - (\$\d+\.\d+) - (https:\/\/.*)/);
            const gpuMatch = gpuDetails.match(/Cheapest option for GPU: (.*?) - (\$\d+\.\d+) - (https:\/\/.*)/);

            if (cpuMatch && gpuMatch) {
                const [_, cpuName, cpuPrice, cpuLink] = cpuMatch;
                const [__, gpuName, gpuPrice, gpuLink] = gpuMatch;

                modalContent.innerHTML = `
                    <img src="${imgSrc}" alt="${title}">
                    <p><strong>Recommended CPU:</strong> <a href="${cpuLink}" target="_blank">${cpuName}</a> at ${cpuPrice}</p>
                    <p><strong>Recommended GPU:</strong> <a href="${gpuLink}" target="_blank">${gpuName}</a> at ${gpuPrice}</p>
                `;
            } else {
                modalContent.textContent = 'Error parsing backend response.';
            }
        } catch (error) {
            modalContent.textContent = 'Failed to fetch recommendations.';
        } finally {
            // Hide spinner and show modal
            spinner.style.display = 'none';
        }
    });
});

closeButton.addEventListener('click', () => {
    modalOverlay.classList.remove('show');
});

modalOverlay.addEventListener('click', (e) => {
    if (e.target === modalOverlay) {
        modalOverlay.classList.remove('show');
    }
});

// Dark mode / Light mode toggle with dynamic sun/moon icon
themeToggle.addEventListener('click', () => {
    body.classList.toggle('dark-mode');
    themeToggle.textContent = body.classList.contains('dark-mode') ? '☀' : '☾';
});