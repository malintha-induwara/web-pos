// Controller Code

//Select the Elements
const customerModel = document.getElementById("customer-modal");
const customerForm = document.getElementById("customer-form");
const customerButton = document.getElementById("customer-submit");

//Set up event listeners
const openCustomerModal = () => {
  customerModel.style.display = "block";
};

// Update Mode Switch
let isUpdateMode = false;
let currentCustomerId = null;

const closeCustomerModal = () => {
  customerModel.style.display = "none";
  customerForm.reset();
  customerButton.textContent = "Add Customer"; // Reset button text
  isUpdateMode = false;
  currentCustomerId = null;
};

document
  .getElementById("add-customer")
  .addEventListener("click", openCustomerModal);

document
  .getElementById("customer-modal-close")
  .addEventListener("click", closeCustomerModal);

// Load Customers
const loadCustomersIntoTable = () => {
  const customerTableList = document.getElementById("customer-table-list");

  customerList.forEach((customer) => {
    addCustomerToTable(customer, customerTableList);
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
  updateButton.className = "action-button";
  updateButton.addEventListener("click", () => {
    // Add your update logic here
    openCustomerModal();
    fillFormWithCustomerData(customer);
    isUpdateMode = true;
    currentCustomerId = customer.customerId;
    customerButton.textContent = "Update Customer"; // Change button text
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
customerForm.addEventListener("submit", (event) => {
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

  const customerTableList = document.getElementById("customer-table-list");

  if (isUpdateMode) {
    // Update existing customer
    const customerIndex = customerList.findIndex(
      (c) => c.customerId === currentCustomerId
    );
    customerList[customerIndex] = customerData;

    // Clear the table and reload all customers
    customerTableList.innerHTML = "";
    loadCustomersIntoTable();
  } else {
    // Add new customer to customerList array
    customerList.push(customerData);

    // Add new customer to the table
    addCustomerToTable(customerData, customerTableList);
  }

  // Close the modal and reset the form
  closeCustomerModal();
  customerForm.reset();
});

// Ensure the DOM is fully loaded before executing
document.addEventListener("DOMContentLoaded", loadCustomersIntoTable);
