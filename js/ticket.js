// Get booking information from URL
const params = new URLSearchParams(window.location.search);


// Get values
const bookingId = params.get("bookingId");
const vehicleNumber = params.get("vehicleNumber");
const vehicleType = params.get("vehicleType");
const slot = params.get("slot");
const bookingDate = params.get("bookingDate");
const entryTime = params.get("entryTime");
const exitTime = params.get("exitTime");


// Display values
document.getElementById("ticketBookingId").textContent =
    bookingId || "-";

document.getElementById("ticketVehicleNumber").textContent =
    vehicleNumber || "-";

document.getElementById("ticketVehicleType").textContent =
    vehicleType || "-";

document.getElementById("ticketSlot").textContent =
    slot || "-";

document.getElementById("ticketDate").textContent =
    bookingDate || "-";

document.getElementById("ticketEntryTime").textContent =
    entryTime || "-";

document.getElementById("ticketExitTime").textContent =
    exitTime || "-";