// Controller Code

// Select the Elements
const customerModel = document.getElementById("customer-modal");
const customerForm = document.getElementById("customer-form");
const customerButton = document.getElementById("customer-submit");
const customerTableList = document.getElementById("customer-table-list");

// Set up event listeners
const openCustomerModal = () => {
  customerModel.style.display = "block";
};

// Update Mode Switch
let isUpdateMode = false;
let currentCustomerId = null;

const closeCustomerModal = () => {
  customerModel.style.display = "none";
  customerForm.reset();
  customerButton.textContent = "Add Customer";
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
const loadCustomersIntoTable = async () => {
  await loadCustomersFromBackend();
  customerTableList.innerHTML = "";
  customerList.forEach((customer) => {
    console.log(customer.customerId);

    addCustomerToTable(customer, customerTableList);
  });
};

const loadCustomersFromBackend = async () => {
  try {
    const response = await fetch("http://localhost:8080/backend/customer"); // Replace with your actual servlet URL
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    customerList = data; // Assign the fetched data to customerList
  } catch (error) {
    console.error("Error fetching customers:", error);
  }
};

const addCustomerToTable = (customer, table) => {
  const row = document.createElement("tr");

  const nameList = [
    "customerId",
    "firstName",
    "lastName",
    "dob",
    "address",
    "mobile",
  ];

  // Create cells for each piece of customer data
  for (const key of nameList) {
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
    openCustomerModal();
    fillFormWithCustomerData(customer);
    isUpdateMode = true;
    currentCustomerId = customer.customerId;
    customerButton.textContent = "Update Customer";
  });
  updateCell.appendChild(updateButton);
  row.appendChild(updateCell);

  // Create 'Remove' button
  const removeCell = document.createElement("td");
  const removeButton = document.createElement("button");
  removeButton.textContent = "Remove";
  removeButton.className = "action-button";
  removeButton.addEventListener("click", () => {
    console.log(`Remove customer ${customer.customerId}`);
    table.removeChild(row);
    customerList = customerList.filter(
      (c) => c.customerId !== customer.customerId
    );
  });
  removeCell.appendChild(removeButton);
  row.appendChild(removeCell);

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

// Validation functions
const validateCustomerId = (id) => /^C\d{3}$/.test(id);
const validateName = (name) => /^[a-zA-Z\s]+$/.test(name);
const validateMobile = (mobile) => /^[0-9]{10}$/.test(mobile);
const validateAddress = (address) => address.trim() !== "";

// Handle form submission to add or update customer
customerForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  // Get form data
  const customerId = document.getElementById("customerID").value;
  const firstName = document.getElementById("firstName").value;
  const lastName = document.getElementById("lastName").value;
  const dob = document.getElementById("dob").value;
  const address = document.getElementById("address").value;
  const mobile = document.getElementById("mobile").value;

  // Validate form data
  if (!validateCustomerId(customerId)) {
    alert("Customer ID must be in the format 'C000'.");
    return;
  }
  if (!validateName(firstName) || !validateName(lastName)) {
    alert("First name and last name must contain only letters and spaces.");
    return;
  }
  if (!validateMobile(mobile)) {
    alert("Mobile number must be a valid 10-digit number.");
    return;
  }
  if (!validateAddress(address)) {
    alert("Address cannot be empty.");
    return;
  }

  // Create new customer object from form data
  const customerData = {
    customerId,
    firstName,
    lastName,
    dob,
    address,
    mobile,
  };

  try {
    const response = await fetch('http://localhost:8080/backend/customer', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(customerData),
    });

    if (response.ok) {
      const result = await response.text();
      alert(result);
      
      // If successful, add the new customer to the table
      addCustomerToTable(customerData, customerTableList);
      
      // Clear the form and close the modal
      closeCustomerModal();
    } else {
      const errorText = await response.text();
      alert(`Failed to save customer: ${errorText}`);
    }
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred while saving the customer.');
  }
});
