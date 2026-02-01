const API = "/api/admin";
const ADMIN_PASSWORD = "crifadmin"; 

// Check authentication on page load
if (localStorage.getItem("adminLoggedIn") !== "true") {
  document.getElementById("loginSection").style.display = "block";
  document.getElementById("adminSection").style.display = "none";
}

// ---------- AUTH ----------
function checkPassword() {
  const input = document.getElementById("admin-pass").value;

  if (input === ADMIN_PASSWORD) {
    localStorage.setItem("adminLoggedIn", "true");
    showAdmin();
  } else {
    document.getElementById("login-error").innerText = "Incorrect password";
  }
}

function showAdmin() {
  document.getElementById("login").style.display = "none";
  document.getElementById("admin").style.display = "block";
  loadProjects();
  loadDocuments();
}

function goBack() {
  window.location.href = "/";
}

// Auto-login if already authenticated
if (localStorage.getItem("adminLoggedIn") === "true") {
  showAdmin();
}

// ---------- PROJECTS ----------
function loadProjects() {
  fetch(`${API}/projects`)
    .then(r => r.json())
    .then(data => {
      const div = document.getElementById("projects");
      div.innerHTML = "";
      data.forEach(p => {
        div.innerHTML += `
          <div>
            <input value="${p.projectName}" id="pn-${p.id}">
            <input value="${p.description}" id="pd-${p.id}">
            <input value="${p.keywords}" id="pk-${p.id}">
            <button onclick="updateProject(${p.id})">Save</button>
            <button onclick="deleteProject(${p.id})">Delete</button>
          </div><br>
        `;
      });
    });
}

function addProject() {
  fetch(`${API}/projects`, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({
      projectName: document.getElementById("p-name").value,
      description: document.getElementById("p-desc").value,
      keywords: document.getElementById("p-key").value
    })
  }).then(loadProjects);
}

function updateProject(id) {
  fetch(`${API}/projects/${id}`, {
    method: "PUT",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({
      projectName: document.getElementById(`pn-${id}`).value,
      description: document.getElementById(`pd-${id}`).value,
      keywords: document.getElementById(`pk-${id}`).value
    })
  }).then(loadProjects);
}

function deleteProject(id) {
  fetch(`${API}/projects/${id}`, { method: "DELETE" })
    .then(loadProjects);
}

// ---------- DOCUMENTS ----------
function loadDocuments() {
  fetch(`${API}/documents`)
    .then(r => r.json())
    .then(data => {
      const div = document.getElementById("documents");
      div.innerHTML = "";
      data.forEach(d => {
        div.innerHTML += `
          <div>
            <input value="${d.projectName}" id="dp-${d.id}">
            <input value="${d.title}" id="dt-${d.id}">
            <input value="${d.content}" id="dc-${d.id}">
            <input value="${d.keywords}" id="dk-${d.id}">
            <button onclick="updateDocument(${d.id})">Save</button>
            <button onclick="deleteDocument(${d.id})">Delete</button>
          </div><br>
        `;
      });
    });
}

function addDocument() {
  fetch(`${API}/documents`, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({
      projectName: document.getElementById("d-project").value,
      title: document.getElementById("d-title").value,
      content: document.getElementById("d-content").value,
      keywords: document.getElementById("d-key").value
    })
  }).then(loadDocuments);
}

function updateDocument(id) {
  fetch(`${API}/documents/${id}`, {
    method: "PUT",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify({
      projectName: document.getElementById(`dp-${id}`).value,
      title: document.getElementById(`dt-${id}`).value,
      content: document.getElementById(`dc-${id}`).value,
      keywords: document.getElementById(`dk-${id}`).value
    })
  }).then(loadDocuments);
}

function deleteDocument(id) {
  fetch(`${API}/documents/${id}`, { method: "DELETE" })
    .then(loadDocuments);
}

// ---------- LOGOUT ----------
document.addEventListener("DOMContentLoaded", () => {
  const logoutBtn = document.getElementById("logoutBtn");

  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem("adminLoggedIn");
      window.location.reload();
    });
  }
});

