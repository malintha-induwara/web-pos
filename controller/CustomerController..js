// Customer Data
let customerList = [
  {
    customerId: "C001",
    customerFirstName: "John",
    customerLastName: "Doe",
    customerDateOfBirth: "1990-01-01",
    customerAddress: "Galle",
    customerMobile: "0712345678",
  },
  {
    customerId: "C002",
    customerFirstName: "John",
    customerLastName: "Doe",
    customerDateOfBirth: "1990-01-01",
    customerAddress: "Galle",
    customerMobile: "0712345678",
  },
  {
    customerId: "C003",
    customerFirstName: "John",
    customerLastName: "Doe",
    customerDateOfBirth: "1990-01-01",
    customerAddress: "Galle",
    customerMobile: "0712345678",
  },
  {
    customerId: "C004",
    customerFirstName: "John",
    customerLastName: "Doe",
    customerDateOfBirth: "1990-01-01",
    customerAddress: "Galle",
    customerMobile: "0712345678",
  },
];

// Controller Code
const addCustomerModel = document.getElementById("add-customer-modal");
const addCustomerForm = document.getElementById("add-customer-form");
const addCustomerButton = addCustomerForm.querySelector(
  "button[type='submit']"
);
let isUpdateMode = false;
let currentCustomerId = null;

const openAddCustomerModal = () => {
  addCustomerModel.style.display = "block";
};

const closeAddCustomerModal = () => {
  addCustomerModel.style.display = "none";
  addCustomerForm.reset();
  addCustomerButton.textContent = "Add Customer"; // Reset button text
  isUpdateMode = false;
  currentCustomerId = null;
};

document
  .getElementById("add-customer")
  .addEventListener("click", openAddCustomerModal);

document
  .getElementById("add-customer-modal-close")
  .addEventListener("click", closeAddCustomerModal);

// Load Customers
const loadCustomersIntoTable = () => {
  const inventoryTable = document.getElementById("inventory-table");

  customerList.forEach((customer) => {
    addCustomerToTable(customer, inventoryTable);
  });
};

const addCustomerToTable = (customer, table) => {
  const row = document.createElement("tr");

  // Create cells for each piece of customer data
  for (const key in customer) {
    const cell = document.createElement("td");
    cell.textContent = customer[key];
    row.appendChild(cell);
  }

  // Create 'Update' button
  const updateCell = document.createElement("td");
  const updateButton = document.createElement("button");
  updateButton.textContent = "Update";
  updateButton.addEventListener("click", () => {
    // Add your update logic here
    openAddCustomerModal();
    fillFormWithCustomerData(customer);
    isUpdateMode = true;
    currentCustomerId = customer.customerId;
    addCustomerButton.textContent = "Update Customer"; // Change button text
  });
  updateCell.appendChild(updateButton);
  row.appendChild(updateCell);

  // Create 'Remove' button
  const removeCell = document.createElement("td");
  const removeButton = document.createElement("button");
  removeButton.textContent = "Remove";
  removeButton.addEventListener("click", () => {
    // Add your remove logic here
    console.log(`Remove customer ${customer.customerId}`);
    table.removeChild(row);
    customerList = customerList.filter(
      (c) => c.customerId !== customer.customerId
    );
  });
  removeCell.appendChild(removeButton);
  row.appendChild(removeCell);

  // Append the row to the table
  table.appendChild(row);
};

// Fill form with customer data
const fillFormWithCustomerData = (customer) => {
  document.getElementById("customerID").value = customer.customerId;
  document.getElementById("firstName").value = customer.customerFirstName;
  document.getElementById("lastName").value = customer.customerLastName;
  document.getElementById("dob").value = customer.customerDateOfBirth;
  document.getElementById("address").value = customer.customerAddress;
  document.getElementById("mobile").value = customer.customerMobile;
};

// Handle form submission to add or update customer
addCustomerForm.addEventListener("submit", (event) => {
  event.preventDefault(); // Prevent form from submitting the traditional way

  // Create new customer object from form data
  const customerData = {
    customerId: document.getElementById("customerID").value,
    customerFirstName: document.getElementById("firstName").value,
    customerLastName: document.getElementById("lastName").value,
    customerDateOfBirth: document.getElementById("dob").value,
    customerAddress: document.getElementById("address").value,
    customerMobile: document.getElementById("mobile").value,
  };

  const inventoryTable = document.getElementById("inventory-table");

  if (isUpdateMode) {
    // Update existing customer
    const customerIndex = customerList.findIndex(
      (c) => c.customerId === currentCustomerId
    );
    customerList[customerIndex] = customerData;

    // Clear the table and reload all customers
    inventoryTable.innerHTML = "";
    loadCustomersIntoTable();
  } else {
    // Add new customer to customerList array
    customerList.push(customerData);

    // Add new customer to the table
    addCustomerToTable(customerData, inventoryTable);
  }

  // Close the modal and reset the form
  closeAddCustomerModal();
  addCustomerForm.reset();
});

// Ensure the DOM is fully loaded before executing
document.addEventListener("DOMContentLoaded", loadCustomersIntoTable);
