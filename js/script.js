const loginForm = document.getElementById("loginForm");

if (loginForm) {
  loginForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const email = document.getElementById("email").value;

    const password = document.getElementById("password").value;

    try {
      const response = await fetch("http://localhost:8081/login", {
        method: "POST",

        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },

        body:
          `email=${encodeURIComponent(email)}` +
          `&password=${encodeURIComponent(password)}`,
      });

      const message = await response.text();

      if (response.ok) {
        alert(message);

        console.log("Login successful");
      } else {
        alert(message);

        console.log("Login failed");
      }
    } catch (error) {
      console.error("Login error:", error);

      alert("Unable to connect to SmartPark server.");
    }
  });
}
