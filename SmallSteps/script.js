document.addEventListener("DOMContentLoaded", function () {

    let startgame = document.getElementById("start");
    if (startgame) {
        startgame.addEventListener("click", function () {
            window.location.href = "house.html";
        });
    }

    // Navigate to thermostat.html when clicking the thermostat
    let thermostat = document.getElementById("thermostat");
    if (thermostat) {
        thermostat.addEventListener("click", function () {
            window.location.href = "thermostat.html";
        });
    }

    let gameScene = document.getElementById("game-scene");

    // Navigate to sink.html when clicking the sink
    let sink = document.getElementById("sink");
    if (sink) {
        sink.addEventListener("click", function () {
            window.location.href = "sink.html";
        });
    }

    let lefttap = document.getElementById("left-tap");
    let righttap = document.getElementById("right-tap");
    if (righttap) {
        righttap.addEventListener("click", function () {
            alert("Success! You turned off the tap!");
            gameScene.src = "images/empty_sink.webp";
            // Create and add a "Go Back Home" button
            let goBackButton = document.createElement("button");
            goBackButton.textContent = "Go Back Home";
            goBackButton.style.position = "absolute";
            goBackButton.style.bottom = "200px"; // Position it wherever you want
            goBackButton.style.left = "50%";
            goBackButton.style.transform = "translateX(-50%)";
            goBackButton.style.padding = "10px 20px";
            goBackButton.style.fontSize = "16px";
            goBackButton.style.backgroundColor = "#4CAF50";
            goBackButton.style.color = "white";
            goBackButton.style.border = "none";
            goBackButton.style.borderRadius = "5px";
            goBackButton.style.cursor = "pointer";

            // Add the button to the DOM
            document.body.appendChild(goBackButton);

            // Add event listener to the "Go Back Home" button
            goBackButton.addEventListener("click", function () {
                window.location.href = "house.html"; // Change to your home page URL
            });
        });
    }
    if (lefttap) {
        lefttap.addEventListener("click", function () {
            alert("Success! You turned off the tap!");
            gameScene.src = "images/empty_sink.webp";
            // Create and add a "Go Back Home" button
            let goBackButton = document.createElement("button");
            goBackButton.textContent = "Go Back Home";
            goBackButton.style.position = "absolute";
            goBackButton.style.bottom = "200px"; // Position it wherever you want
            goBackButton.style.left = "50%";
            goBackButton.style.transform = "translateX(-50%)";
            goBackButton.style.padding = "10px 20px";
            goBackButton.style.fontSize = "16px";
            goBackButton.style.backgroundColor = "#4CAF50";
            goBackButton.style.color = "white";
            goBackButton.style.border = "none";
            goBackButton.style.borderRadius = "5px";
            goBackButton.style.cursor = "pointer";

            // Add the button to the DOM
            document.body.appendChild(goBackButton);

            // Add event listener to the "Go Back Home" button
            goBackButton.addEventListener("click", function () {
                window.location.href = "house.html"; // Change to your home page URL
            });
        });
    }



    // Navigate to town.html when clicking the door
    let door = document.getElementById("door");
    if (door) {
        door.addEventListener("click", function () {
            window.location.href = "town.html";
        });
    }

    // Power meter logic (only runs on sink.html)
    if (window.location.pathname.includes("thermostat.html")) {
        let indicator = document.getElementById("indicator");
        let position = 0;
        let speed = 3; // Speed of movement
        let maxPosition = 380; // Max distance indicator moves
        let gameScene = document.getElementById("game-scene");
        let powerMeter = document.getElementById("power-meter");

        function animateMeter() {
            position += speed;
            if (position >= maxPosition || position <= 0) {
                speed = -speed; // Reverse direction at edges
            }

            indicator.style.transform = `translateX(${position}px)`;
            requestAnimationFrame(animateMeter);
        }

        animateMeter();

        // Click event to check if user clicks in the green zone
        document.getElementById("power-meter").addEventListener("click", function () {
            
            if (position >= 100 && position <= 210) {
                alert("Success! You turned down the AC!");
                gameScene.src = "images/normal_ac.webp";
                
                // Remove the power meter
                powerMeter.style.display = "none";

                // Create and add a "Go Back Home" button
                let goBackButton = document.createElement("button");
                goBackButton.textContent = "Go Back Home";
                goBackButton.style.position = "absolute";
                goBackButton.style.bottom = "100px"; // Position it wherever you want
                goBackButton.style.left = "50%";
                goBackButton.style.transform = "translateX(-50%)";
                goBackButton.style.padding = "10px 20px";
                goBackButton.style.fontSize = "16px";
                goBackButton.style.backgroundColor = "#4CAF50";
                goBackButton.style.color = "white";
                goBackButton.style.border = "none";
                goBackButton.style.borderRadius = "5px";
                goBackButton.style.cursor = "pointer";

                // Add the button to the DOM
                document.body.appendChild(goBackButton);

                // Add event listener to the "Go Back Home" button
                goBackButton.addEventListener("click", function () {
                    window.location.href = "house.html"; // Change to your home page URL
                });



            } 
            
            else {
                alert("Try again!");
            }
        });
    }
});