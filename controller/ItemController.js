console.log("Item Controller");

// ----------Image input------------

var imageInput = document.getElementById("input-image");
var imageInputDiv = document.querySelector(".image-input");

// When the div is clicked, trigger the hidden input's click event
imageInputDiv.onclick = function () {
  imageInput.click();
};

imageInput.onchange = function () {
  var reader = new FileReader();

  reader.onload = function (e) {
    var img = document.createElement("img");
    img.src = e.target.result;
    img.style.width = "100px"; // Or any other size you want
    img.style.height = "100px"; // Or any other size you want

    // Clear the div and add the new image
    imageInputDiv.innerHTML = "";
    imageInputDiv.appendChild(img);
  };

  // Read the image file as a data URL
  reader.readAsDataURL(this.files[0]);
};

//-----Item Form Submit-----

var form = document.getElementById("item-form");
var tableList = document.getElementById("item-table-list");

form.onsubmit = function (e) {
  // Prevent the form from being submitted normally
  e.preventDefault();

  // Create a new table row
  var row = document.createElement("tr");

  // Create and append new cells for each form field
  var fields = [
    "customerID",
    "firstName",
    "lastName",
    "category",
    "input-image",
  ];
  fields.forEach(function (fieldId) {
    var cell = document.createElement("td");
    var field = document.getElementById(fieldId);
    if (fieldId === "input-image") {
      var img = document.createElement("img");
      img.src = field.files[0] ? URL.createObjectURL(field.files[0]) : "";
      img.style.width = "100px";
      img.style.height = "100px";
      cell.appendChild(img);
    } else {
      cell.textContent = field.value;
    }
    row.appendChild(cell);
  });

  // Append the new row to the table
  tableList.appendChild(row);

  // Clear the form
  form.reset();
};

//Item Controller Code

const itemModel = document.getElementById("item-modal");
const itemForm = document.getElementById("item-form");
const itemButton = document.getElementById("item-submit");

const openItemModal = () => {
  itemModel.style.display = "block";
};

let isItemUpdateMode = false;
let currentItemId = null;

const closeItemModal = () => {
  itemModel.style.display = "none";
  itemForm.reset();
  itemButton.textContent = "Add Item";
  isUpdateMode = false;
  currentItemId = null;
};

document.getElementById("add-item").addEventListener("click", openItemModal);
document
  .getElementById("item-modal-close")
  .addEventListener("click", closeItemModal);

// Load Items
