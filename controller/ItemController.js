let itemList = [
  {
    itemId: "I001",
    itemName: "Burger",
    itemPrice: "100",
    itemQty: "10",
    category: "Food",
    itemImage: "/assets/images/foodItems/burger.jpg",
  },
  {
    itemId: "I002",
    itemName: "Pizza",
    itemPrice: "150",
    itemQty: "10",
    category: "Food",
    itemImage: "/assets/images/foodItems/pizza.jpg",
  },
  {
    itemId: "I003",
    itemName: "Chiken Wings",
    itemPrice: "80",
    itemQty: "10",
    category: "Food",
    itemImage: "/assets/images/foodItems/rice.jpg",
  },
];

async function getBase64Image(url) {
  const response = await fetch(url);
  const blob = await response.blob();
  const reader = new FileReader();
  return new Promise((resolve, reject) => {
    reader.onloadend = () => resolve(reader.result);
    reader.onerror = reject;
    reader.readAsDataURL(blob);
  });
}

async function saveImagesToLocalStorage(itemList) {
  for (const item of itemList) {
    let imgDetails = await getBase64Image(item.itemImage);
    localStorage.setItem(item.itemId, JSON.stringify(imgDetails));
  }
}

function getImageFromLocalStorage(itemId) {
  return JSON.parse(localStorage.getItem(itemId));
}

saveImagesToLocalStorage(itemList);

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
    img.id = "item-image-id";
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
  let imageBase64 = getImageFromLocalStorage(item.itemId);
  image.src = imageBase64;
  image.className = "item-image";
  imageCell.appendChild(image);
  row.appendChild(imageCell);

  // Create 'Update' button
  const updateCell = document.createElement("td");
  const updateButton = document.createElement("button");
  updateButton.textContent = "Update";
  updateButton.className = "action-button";
  updateButton.addEventListener("click", () => {
    // Add your update logic here
    openItemModal();
    fillFormWithItemData(item);
    isItemUpdateMode = true;
    currentItemId = item.itemId;
    itemButton.textContent = "Update Item"; // Change button text
  });
  updateCell.appendChild(updateButton);
  row.appendChild(updateCell);

  // Create 'Remove' button
  const removeCell = document.createElement("td");
  const removeButton = document.createElement("button");
  removeButton.textContent = "Remove";
  removeButton.className = "action-button";
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

const fillFormWithItemData = (item) => {
  document.getElementById("itemId").value = item.itemId;
  document.getElementById("itemName").value = item.itemName;
  document.getElementById("itemPrice").value = item.itemPrice;
  document.getElementById("itemQty").value = item.itemQty;
  document.getElementById("category").value = item.category;

  //   Add The Image
  var img = document.createElement("img");
  img.id = "item-image-id";
  let imageBase64 = getImageFromLocalStorage(item.itemId);
  img.src = imageBase64;
  img.style.width = "100px"; // Or any other size you want
  img.style.height = "100px"; // Or any other size you want
  // Clear the div and add the new image
  imageInputDiv.innerHTML = "";
  imageInputDiv.appendChild(img);
};

itemForm.addEventListener("submit", (event) => {
  event.preventDefault();

  const itemData = {
    itemId: document.getElementById("itemId").value,
    itemName: document.getElementById("itemName").value,
    itemPrice: document.getElementById("itemPrice").value,
    itemQty: document.getElementById("itemQty").value,
    category: document.getElementById("category").value,
  };

  const itemTableList = document.getElementById("item-table-list");

  if (isItemUpdateMode) {
    const itemIndex = itemList.findIndex((i) => i.itemId === currentItemId);
    itemList[itemIndex] = itemData;

    let newImage = document.getElementById("item-image-id");
    let imageBase64 = newImage.src;
    localStorage.setItem(itemData.itemId, JSON.stringify(imageBase64));
    itemTableList.innerHTML = "";

    //Remove The image child
    loadItemsIntoTable();
  } else {
    let newImage = document.getElementById("item-image-id");
    let imageBase64 = newImage.src;
    localStorage.setItem(itemData.itemId, JSON.stringify(imageBase64));
    itemList.push(itemData);
    addItemToTable(itemData, itemTableList);
  }

  closeItemModal();
  itemForm.reset();
});

document.addEventListener("DOMContentLoaded", loadItemsIntoTable);
