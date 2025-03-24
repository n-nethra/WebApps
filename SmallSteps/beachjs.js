let gameScene = document.getElementById("oil-spill");

let turtle = document.getElementById("turtle-clickable-game");
if (turtle) {
        turtle.addEventListener("click", function () {
            window.location.href = "turtlegame.html"; 
        });
}

let boat = document.getElementById("boat-clickable-game");
    if (boat) {
        boat.addEventListener("click", function () {
            window.location.href = "boatgame.html"; 
        });
    }

let oil = document.getElementById("spill");
if (oil) {
    oil.addEventListener("click", function () {
        alert("Good Job at Cleaning!")
        gameScene.src = "images/oil-clean.webp";

        // Create and add a "Go Back Home" button
        let goBackButton = document.createElement("button");
        goBackButton.textContent = "Go Back to the Beach";
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
            window.location.href = "beach.html"; // Change to your home page URL
        });
    });

    let turtleScene = document.getElementById("sad-turtle");

let sad = document.getElementById("sad-turtle");
if (sad) {
    sad.addEventListener("click", function () {
        alert("You saved the turtle!")
        turtleScene.src = "images/turtle-happy.webp";

        // Create and add a "Go Back Home" button
        let goBackButton = document.createElement("button");
        goBackButton.textContent = "End";
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
            window.location.href = "end.html"; // Change to your home page URL
        });
    });
}
}