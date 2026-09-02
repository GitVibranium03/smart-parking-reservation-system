// Get the booking form
const bookingForm = document.getElementById("bookingForm");

// Get all available parking slots
const parkingSlots = document.querySelectorAll(".parking-slot.available");

// Get selected slot display
const selectedSlot = document.getElementById("selectedSlot");

// Store selected slot
let selectedSlotNumber = null;

// ===============================
// Parking Slot Selection
// ===============================

parkingSlots.forEach(function (slot) {
  slot.addEventListener("click", function () {
    // Remove previous selection
    parkingSlots.forEach(function (slot) {
      slot.classList.remove("selected");
    });

    // Select clicked slot
    slot.classList.add("selected");

    // Get slot number
    selectedSlotNumber = slot.dataset.slot;

    // Display selected slot
    selectedSlot.textContent = selectedSlotNumber;
  });
});

// ===============================
// Booking Form Validation
// ===============================

bookingForm.addEventListener("submit", function (event) {
  // Prevent page from refreshing
  event.preventDefault();

  // Get form values
  const vehicleNumber = document.getElementById("vehicleNumber").value.trim();

  const vehicleType = document.getElementById("vehicleType").value;

  const bookingDate = document.getElementById("bookingDate").value;

  const entryTime = document.getElementById("entryTime").value;

  const exitTime = document.getElementById("exitTime").value;

  // ===============================
  // Vehicle Number Validation
  // ===============================

  const vehiclePattern = /^[A-Z]{2}[0-9]{1,2}[A-Z]{1,3}[0-9]{4}$/;

  if (!vehiclePattern.test(vehicleNumber.toUpperCase())) {
    alert("Please enter a valid vehicle number.");

    return;
  }

  // ===============================
  // Vehicle Type Validation
  // ===============================

  if (vehicleType === "") {
    alert("Please select a vehicle type.");

    return;
  }

  // ===============================
  // Date Validation
  // ===============================

  if (bookingDate === "") {
    alert("Please select a booking date.");

    return;
  }

  // ===============================
  // Time Validation
  // ===============================

  if (entryTime === "" || exitTime === "") {
    alert("Please select entry and exit time.");

    return;
  }

  if (entryTime >= exitTime) {
    alert("Exit time must be after entry time.");

    return;
  }

  // ===============================
  // Parking Slot Validation
  // ===============================

  if (selectedSlotNumber === null) {
    alert("Please select a parking slot.");

    return;
  }

  // ===============================
  // Booking Successful
  // ===============================

  // Generate temporary booking ID
  const bookingId =
    "SP" + new Date().getFullYear() + Date.now().toString().slice(-5);

  // Create ticket URL
  const ticketURL =
    "ticket.html?" +
    "bookingId=" +
    encodeURIComponent(bookingId) +
    "&vehicleNumber=" +
    encodeURIComponent(vehicleNumber.toUpperCase()) +
    "&vehicleType=" +
    encodeURIComponent(vehicleType) +
    "&slot=" +
    encodeURIComponent(selectedSlotNumber) +
    "&bookingDate=" +
    encodeURIComponent(bookingDate) +
    "&entryTime=" +
    encodeURIComponent(entryTime) +
    "&exitTime=" +
    encodeURIComponent(exitTime);

  // Open ticket page
  window.location.href = ticketURL;
});
