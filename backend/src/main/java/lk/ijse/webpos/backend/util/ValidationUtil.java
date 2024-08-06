package lk.ijse.webpos.backend.util;

import lk.ijse.webpos.backend.dto.CustomerDTO;
import lk.ijse.webpos.backend.dto.ItemDTO;
import lk.ijse.webpos.backend.dto.OrderDTO;
import lk.ijse.webpos.backend.dto.OrderDetailDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ValidationUtil {


    public static List<String> validateCustomer(CustomerDTO customer) {
        List<String> errors = new ArrayList<>();

        if (customer.getCustomerId() == null || !customer.getCustomerId().startsWith("C")) {
            errors.add("Customer ID must start with 'C'");
        }

        if (customer.getFirstName()==null) {
            errors.add("First Name doesnt found");
        }
        if (customer.getLastName()==null) {
            errors.add("Last Name doesnt found");
        }

        if (customer.getDob() == null) {
            errors.add("Date of birth is required");
        }

        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
            errors.add("Address is required");
        }

        if (customer.getMobile() == null || !customer.getMobile().matches("\\d{10}")) {
            errors.add("Mobile number must be exactly 10 digits");
        }

        return errors;
    }


    public static List<String> validateItem(ItemDTO item) {
        List<String> errors = new ArrayList<>();

        if (item.getItemId() == null || !item.getItemId().startsWith("I")) {
            errors.add("Item ID must start with 'I' followed by 3 digits");
        }

        if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
            errors.add("Item name is required");
        }

        if (item.getPrice() <= 0) {
            errors.add("Item price must be a positive number");
        }

        if (item.getQuantity() < 0) {
            errors.add("Item quantity must be a non-negative integer");
        }

        if (item.getCategory() == null || item.getCategory().trim().isEmpty()) {
            errors.add("Item category is required");
        }

        return errors;
    }

    public static List<String> validateOrder(OrderDTO order) {
        List<String> errors = new ArrayList<>();

        if (order.getOrderId() == null ||  order.getOrderId().startsWith("O")) {
            errors.add("Order ID must start with 'O' followed by 3 digits");
        }

        if (order.getCustomerId() == null || !order.getCustomerId().startsWith("C")) {
            errors.add("Customer ID in order must start with 'C'");
        }

        if (order.getSubtotal() < 0) {
            errors.add("Subtotal must be non-negative");
        }

        if (order.getDiscount() < 0 || order.getDiscount() > order.getSubtotal()) {
            errors.add("Discount must be non-negative and not exceed subtotal");
        }

        if (order.getAmount_payed() < 0 || order.getAmount_payed() < (order.getSubtotal() - order.getDiscount())) {
            errors.add("Amount paid must be non-negative and at least equal to (subtotal - discount)");
        }

        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            errors.add("Order must contain at least one order detail");
        } else {
            for (int i = 0; i < order.getOrderDetails().size(); i++) {
                OrderDetailDTO detail = order.getOrderDetails().get(i);
                errors.addAll(validateOrderDetail(detail, i));
            }
        }

        return errors;
    }

    private static List<String> validateOrderDetail(OrderDetailDTO detail, int index) {
            List<String> errors = new ArrayList<>();
            if (detail.getItemId() == null ||  detail.getItemId().startsWith("I")) {
                errors.add("Item ID in order detail " + (index + 1) + " must start with 'I' followed by 3 digits");
            }
            if (detail.getQuantity() <= 0) {
                errors.add("Quantity in order detail " + (index + 1) + " must be positive");
            }
            if (detail.getPrice() <= 0) {
                errors.add("Unit price in order detail " + (index + 1) + " must be positive");
            }
            return errors;
    }
}

