//Elements
let orderIdElement = document.getElementById("order-id");
let subTotalElement = document.getElementById("sub-total");
let discountElement = document.getElementById("discount");
let totalElement = document.getElementById("total");
let cashElement = document.getElementById("cash");
let balanceElement = document.getElementById("balance");
let customerElement = document.getElementById("customerDropDown");
let dateElement = document.getElementById("current-date");
let orderItemElement = document.getElementById("order-items");

// Set date
let date = new Date();
let options = { year: "numeric", month: "long", day: "numeric" };
let formattedDate = date.toLocaleDateString("en-US", options);
dateElement.textContent = formattedDate;

// Load Customer ids
async function populateCustomerDropdown() {
  //Getting data from backend
  try {
    const response = await fetch("http://localhost:8080/backend/customer");
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    customerList = data; // Assign the fetched data to customerList
  } catch (error) {
    console.error("Error fetching customers:", error);
  }

  const customerSelect = customerElement;
  customerSelect.innerHTML = "";

  let defaultOption = document.createElement("option");
  defaultOption.value = "";
  defaultOption.text = "Select Customer";
  customerSelect.appendChild(defaultOption);

  // Add the customer options
  customerList.forEach((customer) => {
    let option = document.createElement("option");
    option.value = customer.customerId;
    option.text = customer.customerId;
    customerSelect.appendChild(option);
  });
}

// Load Items
async function populateOrderItems() {
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

  const orderItemsContainer = orderItemElement;
  orderItemsContainer.innerHTML = "";

  itemList.forEach((item) => {
    const itemCard = document.createElement("div");
    itemCard.className = "item-card";
    itemCard.dataset.itemId = item.item.itemId;

    const itemImage = document.createElement("img");

    let itemObject = itemList.find((itemInList) => {
      return itemInList.item.itemId == item.item.itemId;
    });
    let base64Image = itemObject.image;

    const imageFormat = getImageFormat(base64Image);
    itemImage.src = `data:${imageFormat};base64,${base64Image}`;
    itemImage.width = 160;
    itemImage.alt = item.item.itemName.toLowerCase();

    const itemName = document.createElement("h4");
    itemName.className = "item-name";
    itemName.textContent = item.item.itemName;

    const itemInfo = document.createElement("div");
    itemInfo.className = "item-info";

    const itemPrice = document.createElement("h2");
    itemPrice.className = "item-price";
    itemPrice.textContent = `Rs${item.item.price}`;

    const itemCount = document.createElement("span");
    itemCount.className = "item-count";
    itemCount.textContent = `${item.item.quantity} Items`;
    itemCount.dataset.quantity = item.item.quantity;

    itemInfo.appendChild(itemPrice);
    itemInfo.appendChild(itemCount);
    itemCard.appendChild(itemImage);
    itemCard.appendChild(itemName);
    itemCard.appendChild(itemInfo);
    orderItemsContainer.appendChild(itemCard);

    itemCard.addEventListener("click", () =>
      addItemToCart(item.item, itemCount)
    );
  });
}

async function loadOrderId() {
  try {
    const response = await fetch("http://localhost:8080/backend/order");
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    orderIdElement.textContent = "Order ID: " + data.orderId;
  } catch (error) {
    console.error("Error fetching orderId", error);
  }
}

// Add an event listener
customerElement.addEventListener("change", function () {
  let selectedCustomerId = this.value;
  let selectedCustomer = customerList.find(
    (customer) => customer.customerId === selectedCustomerId
  );
  if (selectedCustomer) {
    document.getElementById("name-holder").textContent =
      "Name: " + selectedCustomer.firstName;
  } else {
    document.getElementById("name-holder").textContent = "Name: ";
  }
});

const cart = [];

function addItemToCart(item, itemCountElement) {
  const itemCount = parseInt(itemCountElement.dataset.quantity, 10);

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

  itemCountElement.dataset.quantity = itemCount - 1;
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
  const currentQty = parseInt(itemCountElement.dataset.quantity, 10);
  itemCountElement.dataset.quantity = currentQty + item.quantity;
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
    const itemTotalPrice = cartItem.price * cartItem.quantity;
    cartItemPrice.textContent = `Rs${itemTotalPrice.toFixed(2)}`;

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

  const discount = parseFloat(discountElement.value) || 0;
  const cash = parseFloat(cashElement.value) || 0;
  const total = subTotal - discount;
  const balance = cash - total;

  subTotalElement.textContent = `Rs${subTotal.toFixed(2)}`;
  totalElement.textContent = `Rs${total.toFixed(2)}`;
  balanceElement.textContent = `Rs${balance.toFixed(2)}`;
}

// Add event listeners to update the cart display when cash or discount inputs change
document.getElementById("cash").addEventListener("input", updateCartDisplay);
document
  .getElementById("discount")
  .addEventListener("input", updateCartDisplay);
document.getElementById("place-order").addEventListener("click", placeOrder);

async function placeOrder() {
  const orderId = orderIdElement.textContent.slice(10);
  const cashInput = cashElement.value;
  const customer = customerElement.value;
  const balance = parseFloat(balanceElement.textContent.slice(2));
  const subTotal = parseFloat(subTotalElement.textContent.slice(2));
  const discount = parseFloat(discountElement.value) || 0;
  const total = parseFloat(totalElement.textContent.slice(2));
  const cash = parseFloat(cashElement.value) || 0;

  if (customer === "") {
    alert("Customer is required.");
    return;
  }

  if (cart.length === 0) {
    alert("No items in the cart.");
    return;
  }

  if (!cashInput) {
    alert("Cash input is required.");
    return;
  }

  if (balance < 0) {
    alert("Insufficient cash provided.");
    return;
  }

  const orderDetails = {
    orderId: orderId,
    customerId: customer,
    orderDetails: cart,
    subtotal: subTotal,
    discount: discount,
    total: total,
    amount_payed: cash,
    balance: balance,
  };

  console.log(orderDetails);

  try {
    const response = await fetch("http://localhost:8080/backend/order", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(orderDetails),
    });

    if (response.ok) {
      const result = await response.text();
      alert(result);
      resetOrder();
    } else {
      const errorText = await response.text();
      alert(`Operation failed: ${errorText}`);
    }
  } catch (error) {
    console.error("Error:", error);
    alert("An error occurred while processing the order data.");
  }
}

function resetOrder() {
  //Update the Order ID
  loadOrderId();

  // Clear the cart
  cart.length = 0;
  cashElement.value = "";
  discountElement.value = "";
  updateCartDisplay();
}

// Initialize the dropdown and items
populateCustomerDropdown();
populateOrderItems();
