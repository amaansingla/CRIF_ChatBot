// ==========================
// CRIF Internal Chatbot UI
// ==========================

// Elements
const messagesEl = document.getElementById("messages");
const inputEl = document.getElementById("input");
const sendBtn = document.getElementById("send");
const clearBtn = document.getElementById("clear"); // optional (only works if button exists)



// Config
const API_ENDPOINT = "/api/chat";

const statusDot = document.querySelector(".dot");
const statusText = document.querySelector(".conn-text");
const HEALTH_ENDPOINT = "/api/health";


// --------------------------
// UI Helpers
// --------------------------

function scrollToBottom() {
  messagesEl.scrollTop = messagesEl.scrollHeight;
}

function createMessageRow(who, html) {
  const row = document.createElement("div");
  row.className = `msg-row ${who}`;

  const bubble = document.createElement("div");
  bubble.className = "msg";
  bubble.innerHTML = html;

  row.appendChild(bubble);
  return row;
}

function addMessage(text, who = "bot") {
  const safe = escapeHtml(text);
  const clickable = linkify(safe);

  const row = createMessageRow(who, clickable);
  messagesEl.appendChild(row);
  scrollToBottom();
}

function setSending(isSending) {
  sendBtn.disabled = isSending;
  sendBtn.textContent = isSending ? "Sending..." : "Send";
  inputEl.disabled = isSending;
}

function showTyping() {
  // Avoid multiple typing bubbles
  if (document.getElementById("typing")) return;

  const row = document.createElement("div");
  row.className = "msg-row bot";
  row.id = "typing";

  const bubble = document.createElement("div");
  bubble.className = "msg";
  bubble.textContent = "Typing...";

  row.appendChild(bubble);
  messagesEl.appendChild(row);
  scrollToBottom();
}

function hideTyping() {
  const typingEl = document.getElementById("typing");
  if (typingEl) typingEl.remove();
}

// --------------------------
// API Call
// --------------------------

async function fetchReply(message) {
  const res = await fetch(API_ENDPOINT, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ message }),
  });

  // If server sends non-200
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
  }

  const data = await res.json();
  return data.reply ?? "No reply received.";
}

// --------------------------
// Core Actions
// --------------------------

async function handleSend(text) {
  const msg = text.trim();
  if (!msg) return;

  // Show user message
  addMessage(msg, "user");

  // Reset input
  inputEl.value = "";
  inputEl.focus();

  // UI loading state
  setSending(true);
  showTyping();

  try {
    const reply = await fetchReply(msg);
    hideTyping();
    addMessage(reply, "bot");
  } catch (err) {
    hideTyping();

    // Helpful user-facing message
    addMessage("❌ Could not reach server. Make sure Spring Boot is running on port 8080.", "bot");
    console.error("Chat API error:", err);
  } finally {
    setSending(false);
  }
}

// --------------------------
// Event Listeners
// --------------------------

sendBtn.addEventListener("click", () => handleSend(inputEl.value));

inputEl.addEventListener("keydown", (e) => {
  if (e.key === "Enter") handleSend(inputEl.value);
});

document.querySelectorAll(".chip").forEach((btn) => {
  btn.addEventListener("click", () => {
    const raw = btn.dataset.q.toLowerCase();

    // Normalize phrases → keywords
    const normalized = raw
      .replace("jira link", "jira")
      .replace("crif portal", "portal")
      .replace("password policy", "password")
      .replace("data classification", "classification")
      .replace("raise ticket", "ticket")
      .replace("request access", "access")
      .replace("password reset", "password")
      .replace("cjaas documentation", "cjaas")
      .replace("s1 india documentation", "s1 india");

    handleSend(normalized);
  });
});


// Clear chat (if button exists)
if (clearBtn) {
  clearBtn.addEventListener("click", () => {
    messagesEl.innerHTML = "";
    addMessage("👋 Chat cleared. Ask me for CRIF links, project info, or documentation.", "bot");
  });
}

// --------------------------
// Text Safety Helpers
// --------------------------

function escapeHtml(str) {
  return str
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function linkify(str) {
  const urlRegex = /(https?:\/\/[^\s]+)/g;
  return str.replace(urlRegex, (url) => `<a href="${url}" target="_blank" rel="noopener noreferrer">${url}</a>`);
}

async function healthCheck() {
  try {
    const res = await fetch(HEALTH_ENDPOINT, { cache: "no-store" });
    if (!res.ok) throw new Error("Backend not healthy");

    // Connected UI
    if (statusDot) statusDot.style.background = "#35d07f";
    if (statusText) statusText.textContent = "Connected";
  } catch (err) {
    // Disconnected UI
    if (statusDot) statusDot.style.background = "#ff4d4d";
    if (statusText) statusText.textContent = "Disconnected";
  }
}

// Run immediately and then every 5 seconds
healthCheck();
setInterval(healthCheck, 5000);


// --------------------------
// Welcome Message
// --------------------------

addMessage(
  "👋 Hi! Ask me for CRIF links, project info, or documentation.\nTry: jira link, crif portal, cjaas documentation",
  "bot"
);

