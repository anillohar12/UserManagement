const form = document.getElementById("registerForm");
const message = document.getElementById("message");
const registerButton = document.querySelector(".register-button");


if (form) {

    form.addEventListener("submit", async function (event) {

        event.preventDefault();

        const button = document.querySelector(".register-button");

        const user = {
            fullname: document.getElementById("fullname").value.trim(),
            email: document.getElementById("email").value.trim(),
            mobile: Number(document.getElementById("mobile").value),
            gender: document.getElementById("gender").value,
            dob: document.getElementById("dob").value,
            ssn: Number(document.getElementById("ssn").value)
        };


        if (!user.fullname ||
            !user.email ||
            !user.mobile ||
            !user.gender ||
            !user.dob ||
            !user.ssn) {

            showMessage("Please fill all fields.", "error");
            return;
        }


        button.disabled = true;
        button.innerHTML = "Registering...";

        message.innerHTML = "";


        try {

            const response = await fetch("/user", {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(user)
            });


            const result = await response.text();


            if (response.ok) {

                showMessage(result, "success");

                form.reset();

            } else {

                showMessage(
                    "Registration failed: " + result,
                    "error"
                );
            }


        } catch (error) {

            console.error("Error:", error);

            showMessage(
                "Unable to connect with server.",
                "error"
            );


        } finally {

            button.disabled = false;

            button.innerHTML = `
                <span>➤</span>
                Register
            `;
        }

    });

}


/* =========================
   MESSAGE FUNCTION
========================= */

function showMessage(text, type) {

    if (!message) {
        console.log(text);
        return;
    }


    message.innerText = text;


    if (type === "success") {

        message.style.color = "#00e5ff";

    } else {

        message.style.color = "#ff5577";
    }


    message.style.opacity = "0";
    message.style.transform = "translateY(-5px";


    setTimeout(() => {

        message.style.transition = "0.4s";
        message.style.opacity = "1";
        message.style.transform = "translateY(0)";

    }, 50);
}


/* =========================
   INPUT ANIMATION
========================= */

const inputs = document.querySelectorAll(
    ".input-box input, .input-box select"
);


inputs.forEach(input => {

    input.addEventListener("focus", function () {

        this.parentElement.classList.add("focused");

    });


    input.addEventListener("blur", function () {

        this.parentElement.classList.remove("focused");

    });

});


/* =========================
   BUTTON RIPPLE EFFECT
========================= */

if (registerButton) {

    registerButton.addEventListener("click", function (event) {

        const ripple = document.createElement("span");

        ripple.classList.add("ripple");


        const rect =
            registerButton.getBoundingClientRect();


        ripple.style.left =
            `${event.clientX - rect.left}px`;

        ripple.style.top =
            `${event.clientY - rect.top}px`;


        registerButton.appendChild(ripple);


        setTimeout(() => {

            ripple.remove();

        }, 600);

    });

}