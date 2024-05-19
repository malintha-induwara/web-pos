const addCustomerModel = document.getElementById("add-customer-modal");
const addCustomerForm = document.getElementById("add-customer-form");

const openAddCustomerModal = () => {
  addCustomerModel.style.display = "block";
};

const closeAddCustomerModal = () => {
  addCustomerModel.style.display = "none";
};

document
  .getElementById("add-customer")
  .addEventListener("click", openAddCustomerModal);

document
  .getElementById("add-customer-modal-close")
  .addEventListener("click", closeAddCustomerModal);
