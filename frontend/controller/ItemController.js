//Select Elements
var imageInput = document.getElementById("input-image");
var imageInputDiv = document.querySelector(".image-input");
const itemTableList = document.getElementById("item-table-list");
const itemModel = document.getElementById("item-modal");
const itemForm = document.getElementById("item-form");
const itemButton = document.getElementById("item-submit");

// When the div is clicked, trigger the hidden input's click event
imageInputDiv.onclick = function () {
  imageInput.click();
};

imageInput.onchange = function () {
  var reader = new FileReader();

  reader.onload = function (e) {
    var img = document.createElement("img");
    img.id = "item-image-id";
    img.src = e.target.result;
    img.style.width = "100px";
    img.style.height = "100px";

    // Clear the div and add the new image
    imageInputDiv.innerHTML = "";
    imageInputDiv.appendChild(img);
  };

  // Read the image file as a data URL
  reader.readAsDataURL(this.files[0]);
};

// Item Controller Code

const openItemModal = () => {
  itemModel.style.display = "block";

  if (!isItemUpdateMode) {
    imageInputDiv.innerHTML = "";
  }
};

let isItemUpdateMode = false;
let currentItemId = null;

const closeItemModal = () => {
  itemModel.style.display = "none";
  itemForm.reset();
  itemButton.textContent = "Add Item";
  isItemUpdateMode = false;
  currentItemId = null;
};

document.getElementById("add-item").addEventListener("click", openItemModal);
document
  .getElementById("item-modal-close")
  .addEventListener("click", closeItemModal);

// Load Items
const loadItemsIntoTable = async () => {
  await loadItemsFromBackend();

  itemTableList.innerHTML = "";
  itemList.forEach((item) => {
    addItemToTable(item, itemTableList);
    console.log(item);
  });
};

const loadItemsFromBackend = async () => {
  try {
    const response = await fetch("http://localhost:8080/backend/item");
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    itemList = data;
  } catch (error) {
    console.error("Error fetching customers:", error);
  }
};

const addItemToTable = (item, table) => {
  const row = document.createElement("tr");
  const keys = ["itemId", "itemName", "price", "quantity", "category"];
  keys.forEach((key) => {
    const cell = document.createElement("td");
    cell.textContent = item.item[key];
    row.appendChild(cell);
  });

  const imageCell = document.createElement("td");
  const image = document.createElement("img");

  const base64Image = item.image;
  //Find image format
  const imageFormat = getImageFormat(base64Image)
  image.src = `data:${imageFormat};base64,${base64Image}`;
  image.className = "item-image";
  imageCell.appendChild(image);
  row.appendChild(imageCell);

  // Create 'Update' button
  const updateCell = document.createElement("td");
  const updateButton = document.createElement("button");
  updateButton.textContent = "Update";
  updateButton.className = "action-button";
  updateButton.addEventListener("click", () => {
    openItemModal();
    fillFormWithItemData(item);
    isItemUpdateMode = true;
    currentItemId = item.itemId;
    itemButton.textContent = "Update Item";
  });
  updateCell.appendChild(updateButton);
  row.appendChild(updateCell);

  // Create 'Remove' button
  const removeCell = document.createElement("td");
  const removeButton = document.createElement("button");
  removeButton.textContent = "Remove";
  removeButton.className = "action-button";
  removeButton.addEventListener("click", () => {
    console.log(`Remove item ${item.itemId}`);
    table.removeChild(row);
    itemList = itemList.filter((i) => i.itemId !== item.itemId);
  });
  removeCell.appendChild(removeButton);
  row.appendChild(removeCell);

  // Append the row to the table
  table.appendChild(row);
};

function getImageFormat(base64String) {
  const prefix = base64String.substr(0, 5);
  if (prefix === "/9j/4") return "image/jpeg";
  if (prefix === "iVBOR") return "image/png";
  return "image/png";
}

const fillFormWithItemData = (item) => {
  document.getElementById("itemId").value = item.itemId;
  document.getElementById("itemName").value = item.itemName;
  document.getElementById("itemPrice").value = item.itemPrice;
  document.getElementById("itemQty").value = item.itemQty;
  document.getElementById("category").value = item.category;

  // Add The Image
  var img = document.createElement("img");
  img.id = "item-image-id";
  let imageBase64 = getImageFromLocalStorage(item.itemId);
  img.src = imageBase64;
  img.style.width = "100px";
  img.style.height = "100px";
  // Clear the div and add the new image
  imageInputDiv.innerHTML = "";
  imageInputDiv.appendChild(img);
};

// Validation functions
const validateItemId = (id) => /^I\d{3}$/.test(id);
const validateItemName = (name) => /^[a-zA-Z\s]+$/.test(name);
const validateItemPrice = (price) =>
  /^[0-9]+(\.[0-9]{1,2})?$/.test(price) && parseFloat(price) > 0;
const validateItemQty = (qty) => /^[0-9]+$/.test(qty) && parseInt(qty, 10) > 0;
const validateCategory = (category) => category.trim() !== "";

// Handle form submission to add or update item
itemForm.addEventListener("submit", (event) => {
  event.preventDefault();

  // Get form data
  const itemId = document.getElementById("itemId").value;
  const itemName = document.getElementById("itemName").value;
  const itemPrice = document.getElementById("itemPrice").value;
  const itemQty = document.getElementById("itemQty").value;
  const category = document.getElementById("category").value;

  // Validate form data
  if (!validateItemId(itemId)) {
    alert("Item ID must be in the format 'I00'.");
    return;
  }
  if (!validateItemName(itemName)) {
    alert(
      "Item name cannot be empty and must not contain numbers or special characters."
    );
    return;
  }
  if (!validateItemPrice(itemPrice)) {
    alert("Item price must be a valid positive number.");
    return;
  }
  if (!validateItemQty(itemQty)) {
    alert("Item quantity must be a valid positive integer.");
    return;
  }
  if (!validateCategory(category)) {
    alert("Category cannot be empty.");
    return;
  }

  // Create new item object from form data
  const itemData = {
    itemId,
    itemName,
    itemPrice,
    itemQty,
    category,
  };

  const itemTableList = document.getElementById("item-table-list");

  if (isItemUpdateMode) {
    // Update existing item
    const itemIndex = itemList.findIndex((i) => i.itemId === currentItemId);
    itemList[itemIndex] = itemData;

    let newImage = document.getElementById("item-image-id");
    let imageBase64 = newImage.src;
    localStorage.setItem(itemData.itemId, JSON.stringify(imageBase64));
    itemTableList.innerHTML = "";

    // Remove the image child
    loadItemsIntoTable();
  } else {
    // Add new item to itemList array
    let newImage = document.getElementById("item-image-id");
    let imageBase64 = newImage.src;
    localStorage.setItem(itemData.itemId, JSON.stringify(imageBase64));
    itemList.push(itemData);
    addItemToTable(itemData, itemTableList);
  }

  // Close the modal and reset the form
  closeItemModal();
  itemForm.reset();
});
