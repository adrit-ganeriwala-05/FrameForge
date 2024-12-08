const boxes = document.querySelectorAll('.box');
  const modalOverlay = document.getElementById('modalOverlay');
  const modalTitle = document.getElementById('modalTitle');
  const modalContent = document.getElementById('modalContent');
  const closeButton = document.getElementById('closeButton');
  const themeToggle = document.getElementById('themeToggle');
  const body = document.body;

  boxes.forEach(box => {
    box.addEventListener('click', () => {
      const title = box.getAttribute('data-title');
      const imgSrc = box.getAttribute('data-img');
      const cpuName = box.getAttribute('data-cpu-name');
      const cpuLink = box.getAttribute('data-cpu-link');
      const cpuPrice = box.getAttribute('data-cpu-price');
      const gpuName = box.getAttribute('data-gpu-name');
      const gpuLink = box.getAttribute('data-gpu-link');
      const gpuPrice = box.getAttribute('data-gpu-price');

      modalTitle.textContent = title;
      modalContent.innerHTML = `
        <img src="${imgSrc}" alt="${title}">
        <p><strong>Recommended CPU:</strong> <a href="${cpuLink}" target="_blank">${cpuName}</a> at ${cpuPrice}</p>
        <p><strong>Recommended GPU:</strong> <a href="${gpuLink}" target="_blank">${gpuName}</a> at ${gpuPrice}</p>
      `;

      modalOverlay.classList.add('show');
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
    if (body.classList.contains('dark-mode')) {
      themeToggle.textContent = '☀';
    } else {
      themeToggle.textContent = '☾';
    }
  });
