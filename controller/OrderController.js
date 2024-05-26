// Set date
let date = new Date();
let options = { year: "numeric", month: "long", day: "numeric" };
let formattedDate = date.toLocaleDateString("en-US", options);
document.getElementById("current-date").textContent = formattedDate;

// Load Customer Numbers
function populateCustomerDropdown() {
  const customerSelect = document.getElementById("customerDropDown");
  customerSelect.innerHTML = "";
  customerList.forEach((customer) => {
    let option = document.createElement("option");
    option.value = customer.customerId;
    option.text = customer.customerMobile;
    customerSelect.appendChild(option);
  });
}

// Add an event listener
document
  .getElementById("customerDropDown")
  .addEventListener("change", function () {
    let selectedCustomerId = this.value;
    let selectedCustomer = customerList.find(
      (customer) => customer.customerId === selectedCustomerId
    );
    if (selectedCustomer) {
      document.getElementById("name-holder").textContent =
        "Name: " + selectedCustomer.customerFirstName;
    } else {
      document.getElementById("name-holder").textContent = "Name: ";
    }
  });

// Populate Items
function populateOrderItems() {
  const orderItemsContainer = document.getElementById("order-items");
  orderItemsContainer.innerHTML = "";

  itemList.forEach((item) => {
    const itemCard = document.createElement("div");
    itemCard.className = "item-card";
    itemCard.dataset.itemId = item.itemId;

    const itemImage = document.createElement("img");
    let imageBase64 = getImageFromLocalStorage(item.itemId);
    itemImage.src = imageBase64;
    itemImage.width = 160;
    itemImage.alt = item.itemName.toLowerCase();

    const itemName = document.createElement("h4");
    itemName.className = "item-name";
    itemName.textContent = item.itemName;

    const itemInfo = document.createElement("div");
    itemInfo.className = "item-info";

    const itemPrice = document.createElement("h2");
    itemPrice.className = "item-price";
    itemPrice.textContent = `$${item.itemPrice}`;

    const itemCount = document.createElement("span");
    itemCount.className = "item-count";
    itemCount.textContent = `${item.itemQty} Items`;
    itemCount.dataset.itemQty = item.itemQty;

    itemInfo.appendChild(itemPrice);
    itemInfo.appendChild(itemCount);
    itemCard.appendChild(itemImage);
    itemCard.appendChild(itemName);
    itemCard.appendChild(itemInfo);
    orderItemsContainer.appendChild(itemCard);

    itemCard.addEventListener("click", () => addItemToCart(item, itemCount));
  });
}

const cart = [];

function addItemToCart(item, itemCountElement) {
  const itemCount = parseInt(itemCountElement.dataset.itemQty, 10);

  if (itemCount <= 0) {
    alert(`Item ${item.itemName} is out of stock.`);
    return;
  }

  const existingItem = cart.find((cartItem) => cartItem.itemId === item.itemId);
  if (existingItem) {
    existingItem.quantity += 1;
  } else {
    cart.push({ ...item, quantity: 1 });
  }

  itemCountElement.dataset.itemQty = itemCount - 1;
  itemCountElement.textContent = `${itemCount - 1} Items`;

  updateCartDisplay();
}

function removeItemFromCart(itemId) {
  const itemIndex = cart.findIndex((cartItem) => cartItem.itemId === itemId);
  if (itemIndex === -1) return;

  const item = cart[itemIndex];
  const itemCountElement = document.querySelector(
    `.item-card[data-item-id='${itemId}'] .item-count`
  );
  const currentQty = parseInt(itemCountElement.dataset.itemQty, 10);
  itemCountElement.dataset.itemQty = currentQty + item.quantity;
  itemCountElement.textContent = `${currentQty + item.quantity} Items`;

  cart.splice(itemIndex, 1);

  updateCartDisplay();
}

function updateCartDisplay() {
  const cartItemsContainer = document.getElementById("cart-items");
  cartItemsContainer.innerHTML = "";
  let subTotal = 0;

  cart.forEach((cartItem) => {
    const cartItemDiv = document.createElement("div");
    cartItemDiv.className = "cart-item";

    const cartItemName = document.createElement("p");
    cartItemName.className = "cart-item-name";
    cartItemName.textContent = `${cartItem.itemName} x ${cartItem.quantity}`;

    const cartItemPrice = document.createElement("p");
    const itemTotalPrice = cartItem.itemPrice * cartItem.quantity;
    cartItemPrice.textContent = `$${itemTotalPrice.toFixed(2)}`;

    const removeButton = document.createElement("button");
    removeButton.textContent = "⛔";
    removeButton.className = "remove-button-cart";
    removeButton.addEventListener("click", () =>
      removeItemFromCart(cartItem.itemId)
    );

    subTotal += itemTotalPrice;

    cartItemDiv.appendChild(cartItemName);
    cartItemDiv.appendChild(cartItemPrice);
    cartItemDiv.appendChild(removeButton);
    cartItemsContainer.appendChild(cartItemDiv);
  });

  const tax = subTotal * 0.1;
  const total = subTotal + tax;

  document.getElementById("sub-total").textContent = `$${subTotal.toFixed(2)}`;
  document.getElementById("tax").textContent = `$${tax.toFixed(2)}`;
  document.getElementById("total").textContent = `$${total.toFixed(2)}`;
}

document.getElementById("place-order").addEventListener("click", placeOrder);

function placeOrder() {
  if (cart.length === 0) {
    alert("No items in the cart.");
    return;
  }

  const orderDetails = {
    orderId: document.getElementById("order-id").textContent,
    customer: document.getElementById("customerDropDown").value,
    items: cart,
    subTotal: parseFloat(
      document.getElementById("sub-total").textContent.slice(1)
    ),
    tax: parseFloat(document.getElementById("tax").textContent.slice(1)),
    total: parseFloat(document.getElementById("total").textContent.slice(1)),
  };

  console.log("Order Placed:", orderDetails);
  alert("Order placed successfully!");

  cart.length = 0; // Clear the cart
  updateCartDisplay(); // Update the display
}

// Initialize the dropdown and items
