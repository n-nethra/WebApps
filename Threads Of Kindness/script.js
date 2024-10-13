var clothes = [
    { type: "T-Shirt", size: "S"},
    { type: "Shirt", size: "M"},
    { type: "Tank Top", size: "L"}
];

function getClothesName(name){
  for (clothes in clothes){
    if (clothes[clothes].type === name){
      return clothes[clothes].type;
    }
  }
}

function getClothesName(name){
  for (clothes in clothes){
    if (clothes[clothes].type === name){
      return clothes[clothes].size;
    }
  }
}

function runPythonScript() {
// Get the path to the Python script.
var pythonScriptPath = "C:\\Users\\shiyo\\PycharmProjects\\ComputerVision1\\compVis.py";

  // Run the Python script.
    subprocess.run(["python", pythonScriptPath]);
  }