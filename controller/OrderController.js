// Set date
let date = new Date();
let options = { year: "numeric", month: "long", day: "numeric" };
let formattedDate = date.toLocaleDateString("en-US", options);
document.getElementById("current-date").textContent = formattedDate;

// Load Customer Numbers
function populateCustomerDropdown() {
  const customerSelect = document.getElementById("customerDropDown");
  customerSelect.innerHTML = "";


  let defaultOption = document.createElement("option");
  defaultOption.value = "";
  defaultOption.text = "Select Customer";
  customerSelect.appendChild(defaultOption);

  // Add the customer options
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
    itemPrice.textContent = `Rs${item.itemPrice}`;

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

  const discount = parseFloat(document.getElementById("discount").value) || 0;
  const cash = parseFloat(document.getElementById("cash").value) || 0;
  const total = subTotal - discount;
  const balance = cash - total;

  document.getElementById("sub-total").textContent = `Rs${subTotal.toFixed(2)}`;
  document.getElementById("total").textContent = `Rs${total.toFixed(2)}`;
  document.getElementById("balance").textContent = `Rs${balance.toFixed(2)}`;
}

// Add event listeners to update the cart display when cash or discount inputs change
document.getElementById("cash").addEventListener("input", updateCartDisplay);
document
  .getElementById("discount")
  .addEventListener("input", updateCartDisplay);

document.getElementById("place-order").addEventListener("click", placeOrder);

function placeOrder() {
  const cashInput = document.getElementById("cash").value;

  const customer = document.getElementById("customerDropDown").value;


  const balance = parseFloat(
    document.getElementById("balance").textContent.slice(1)
  );

  
  if (customer ==="") {
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
    orderId: document.getElementById("order-id").textContent,
    customer: customer,
    items: cart,
    subTotal: parseFloat(
      document.getElementById("sub-total").textContent.slice(1)
    ),
    discount: parseFloat(document.getElementById("discount").value) || 0,
    total: parseFloat(document.getElementById("total").textContent.slice(1)),
    cash: parseFloat(document.getElementById("cash").value) || 0,
    balance: parseFloat(
      document.getElementById("balance").textContent.slice(1)
    ),
  };

  console.log("Order Placed:", orderDetails);
  alert("Order placed successfully!");


  //Update the Order ID

  let orderId = document.getElementById("order-id");
  let currentOrderId = parseInt(orderId.textContent.split(": ")[1]);
  currentOrderId++;
  orderId.textContent = "Order ID: " + currentOrderId;



  cart.length = 0; // Clear the cart
  document.getElementById("cash").value = ""; // Clear the cash input
  document.getElementById("discount").value = ""; // Clear the discount input
  updateCartDisplay(); // Update the display
}

// Initialize the dropdown and items
populateCustomerDropdown();
populateOrderItems();
