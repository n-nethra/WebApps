document.addEventListener("DOMContentLoaded", function () {
    // Add click event listener for trash can to navigate to trashgame.html
    let trash = document.getElementById("trash-clickable-game");
    if (trash) {
        trash.addEventListener("click", function () {
            window.location.href = "trashgame.html"; 
        });
    }

    let car = document.getElementById("car-game");
    if (car) {
        car.addEventListener("click", function () {
        
            window.location.href = "cargame.html"; 
        });
    }
    let carclick = document.getElementById("car-clickable");
    if (carclick) {
        carclick.addEventListener("click", function () {
            alert("That's not very good for the environment...");
        });
    }

    let bikeclick = document.getElementById("bike-clickable");
    if (bikeclick) {
        bikeclick.addEventListener("click", function () {
            alert("Good Choice!");
            // Create and add a "Go Back Home" button
            let goBackButton = document.createElement("button");
            goBackButton.textContent = "Go Back to Town";
            goBackButton.style.position = "absolute";
            goBackButton.style.bottom = "700px"; // Position it wherever you want
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
                window.location.href = "town.html"; // Change to your home page URL
            });
            
        });
        
    }
    let beachclick = document.getElementById("beach-clickable");
    if (beachclick) {
        beachclick.addEventListener("click", function () {
            window.location.href = "beach.html"; 
        });
    }


    let droppedCount = 0;

    // Drag and drop functionality
    const draggables = document.querySelectorAll('.draggable');
    const dropzone = document.getElementById('dropzone1'); // Use the correct dropzone element here

    draggables.forEach(draggable => {
        draggable.addEventListener('dragstart', (e) => {
            e.dataTransfer.setData('text', draggable.id);
        });
    });

    dropzone.addEventListener('dragover', (e) => {
        e.preventDefault(); // Allow the drop by preventing the default action
    });

    dropzone.addEventListener('drop', (e) => {
        e.preventDefault();

        // Get the draggable element being dropped
        const id = e.dataTransfer.getData('text');
        const draggedElement = document.getElementById(id);

        // Position the dragged item in the dropzone (trash can)
        draggedElement.style.position = 'absolute';
        draggedElement.style.top = `${e.offsetY - draggedElement.height / 2}px`;
        draggedElement.style.left = `${e.offsetX - draggedElement.width / 2}px`;

        // Hide the dragged element after it's dropped in the trash
        draggedElement.style.visibility = 'hidden';
        droppedCount++;

        // Check if all 3 items are dropped
        if (droppedCount === 3) {
            alert("Success! You threw away all the trash!");

            // Change the trash can image after all items are dropped
            trash.src = "images/trash.webp";

            // Create and add a "Go Back Home" button
            let goBackButton = document.createElement("button");
            goBackButton.textContent = "Go Back to Town";
            goBackButton.style.position = "absolute";
            goBackButton.style.bottom = "150px"; // Position it wherever you want
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
                window.location.href = "town.html"; // Change to your home page URL
            });
        }
    });

});
