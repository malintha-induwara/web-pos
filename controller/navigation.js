console.log("hello world");

document
  .getElementById("customer-button")
  .addEventListener("click", function () {
    document.getElementById("order-page").style.display = "none";
    document.getElementById("customer-page").style.display = "flex";
  });

document.getElementById("order-button").addEventListener("click", function () {
  document.getElementById("order-page").style.display = "flex";
  document.getElementById("customer-page").style.display = "none";
});
