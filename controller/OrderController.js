//Set date
let date = new Date();
let options = { year: "numeric", month: "long", day: "numeric" };
let formattedDate = date.toLocaleDateString("en-US", options);

document.getElementById("current-date").textContent = formattedDate;

//Load Customer Numbers

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

//Add A event listner
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

//Populate Items

function populateOrderItems() {
  // Get the order-items container
  const orderItemsContainer = document.getElementById("order-items");

  // Clear existing content
  orderItemsContainer.innerHTML = "";

  // Populate with new item cards
  itemList.forEach((item) => {
    // Create item card
    const itemCard = document.createElement("div");
    itemCard.className = "item-card";

    // Create item image
    const itemImage = document.createElement("img");
    let imageBase64 = getImageFromLocalStorage(item.itemId);
    itemImage.src = imageBase64;
    itemImage.width = 160;
    itemImage.alt = item.itemName.toLowerCase();

    // Create item name
    const itemName = document.createElement("h4");
    itemName.className = "item-name";
    itemName.textContent = item.itemName;

    // Create item info container
    const itemInfo = document.createElement("div");
    itemInfo.className = "item-info";

    // Create item price
    const itemPrice = document.createElement("h2");
    itemPrice.className = "item-price";
    itemPrice.textContent = `$${item.itemPrice}`;

    // Create item count
    const itemCount = document.createElement("span");
    itemCount.className = "item-count";
    itemCount.textContent = `${item.itemQty} Items`;

    // Append elements to item info
    itemInfo.appendChild(itemPrice);
    itemInfo.appendChild(itemCount);

    // Append elements to item card
    itemCard.appendChild(itemImage);
    itemCard.appendChild(itemName);
    itemCard.appendChild(itemInfo);

    // Append item card to order items container
    orderItemsContainer.appendChild(itemCard);
  });
}
