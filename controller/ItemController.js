let itemList = [
  {
    itemId: "I001",
    itemImage: "/assets/images/foodItems/burger.jpg",
    itemName: "Burger",
    itemPrice: "100",
    itemQty: "10",
    category: "Food",
  },
  {
    itemId: "I002",
    itemImage: "/assets/images/foodItems/pizza.jpg",
    itemName: "Pizza",
    itemPrice: "150",
    itemQty: "10",
    category: "Food",
  },
  {
    itemId: "I003",
    itemImage: "/assets/images/foodItems/chikenWIngs.jpg",
    itemName: "Chiken Wings",
    itemPrice: "80",
    itemQty: "10",
    category: "Food",
  },
  {
    itemId: "I004",
    itemImage: "/assets/images/foodItems/rice.jpg",
    itemName: "Rice",
    itemPrice: "120",
    itemQty: "10",
    category: "Food",
  },
  {
    itemId: "I005",
    itemImage: "/assets/images/foodItems/seaFood.jpg",
    itemName: "Sea Food",
    itemPrice: "50",
    itemQty: "10",
    category: "Food",
  },
];

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

const loadItemsIntoTable = () => {
  const itemTableList = document.getElementById("item-table-list");

  itemList.forEach((item) => {
    addItemToTable(item, itemTableList);
  });
};

const addItemToTable = (item, table) => {
  const row = document.createElement("tr");

  const keys = ["itemId", "itemName", "itemPrice", "itemQty", "category"];
  keys.forEach((key) => {
    const cell = document.createElement("td");
    cell.textContent = item[key];
    row.appendChild(cell);
  });

  const imageCell = document.createElement("td");
  const image = document.createElement("img");
  image.src = item.itemImage;
  image.className = "item-image";
  imageCell.appendChild(image);
  row.appendChild(imageCell);

  // Create 'Update' button
  const updateCell = document.createElement("td");
  const updateButton = document.createElement("button");
  updateButton.textContent = "Update";
  updateButton.addEventListener("click", () => {
    // Add your update logic here
    openItemModal();
    // fillFormWithItemData(item);
    isUpdateMode = true;
    currentItemId = item.itemId;
    itemButton.textContent = "Update Item"; // Change button text
  });
  updateCell.appendChild(updateButton);
  row.appendChild(updateCell);

  // Create 'Remove' button
  const removeCell = document.createElement("td");
  const removeButton = document.createElement("button");
  removeButton.textContent = "Remove";
  removeButton.addEventListener("click", () => {
    // Add your remove logic here
    console.log(`Remove item ${item.itemId}`);
    table.removeChild(row);
    itemList = itemList.filter((i) => i.itemId !== item.itemId);
  });
  removeCell.appendChild(removeButton);
  row.appendChild(removeCell);

  // Append the row to the table
  table.appendChild(row);
};
document.addEventListener("DOMContentLoaded", loadItemsIntoTable);
