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
const registerForm = document.getElementById("registerForm");

if (registerForm) {
  registerForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const phone = document.getElementById("phone").value;
    const password = document.getElementById("password").value;
    const confirmPassword =
      document.getElementById("confirmPassword").value;

    // Check password confirmation
    if (password !== confirmPassword) {
      alert("Passwords do not match.");
      return;
    }

    try {
      const response = await fetch(
        "http://localhost:8081/register",
        {
          method: "POST",

          headers: {
            "Content-Type":
              "application/x-www-form-urlencoded",
          },

          body:
            `name=${encodeURIComponent(name)}` +
            `&email=${encodeURIComponent(email)}` +
            `&phone=${encodeURIComponent(phone)}` +
            `&password=${encodeURIComponent(password)}`,
        }
      );

      const message = await response.text();

      if (response.ok) {
        alert(message);

        registerForm.reset();

        console.log("Registration successful");
      } else {
        alert(message);

        console.log("Registration failed");
      }
    } catch (error) {
      console.error("Registration error:", error);

      alert(
        "Unable to connect to SmartPark server."
      );
    }
  });
}