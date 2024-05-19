const addCustomerModel = document.getElementById("addCustomerModel");
const addCustomerForm = document.getElementById("addCustomerForm");

const openAddModal = () => {
  addCustomerModel.style.display = "block";
};

document.getElementById("add-customer").addEventListener("click", openAddModal);
