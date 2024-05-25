document
  .getElementById("customer-button")
  .addEventListener("click", function () {
    document.getElementById("order-page").style.display = "none";
    document.getElementById("customer-page").style.display = "flex";
    document.getElementById("item-page").style.display = "none";
  });

document.getElementById("order-button").addEventListener("click", function () {
  document.getElementById("order-page").style.display = "flex";
  document.getElementById("customer-page").style.display = "none";
  document.getElementById("item-page").style.display = "none";

  //Poupulate Methods
  populateOrderItems();
  populateCustomerDropdown();
});

document.getElementById("item-button").addEventListener("click", function () {
  document.getElementById("order-page").style.display = "none";
  document.getElementById("customer-page").style.display = "none";
  document.getElementById("item-page").style.display = "flex";
});
