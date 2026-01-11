package com.soat_tech_challenge4.app_order.bdd.steps;

import com.soat_tech_challenge4.app_order.core.entities.Order;
import com.soat_tech_challenge4.app_order.core.entities.OrderItem;
import com.soat_tech_challenge4.app_order.core.entities.OrderStatusEnum;
import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;

import io.cucumber.java.en.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderSteps {

    private List<OrderItem> items;
    private Order order;
    private Exception capturedException;

    @Given("an Order with the following items:")
    public void an_order_with_the_following_items(io.cucumber.datatable.DataTable dataTable) {
        items = new ArrayList<>();

        dataTable.asMaps().forEach(row -> {
            BigDecimal price = new BigDecimal(row.get("price"));
            int quantity = Integer.parseInt(row.get("quantity"));
            items.add(new OrderItem("P1", quantity, price));
        });
    }

    @When("the Order is created")
    public void the_order_is_created() {
        order = new Order(items);
    }

    @Then("the Order total should be {double}")
    public void the_order_total_should_be(Double expectedTotal) {
        assertNotNull(order);
        assertEquals(0, order.getTotal().compareTo(BigDecimal.valueOf(expectedTotal)));
    }

    @Given("an Order with initial status {string}")
    public void an_order_with_initial_status(String status) {
        order = new Order(
                1L,
                LocalDateTime.now(),
                OrderStatusEnum.valueOf(status),
                new ArrayList<>(),
                BigDecimal.ZERO,
                null
        );
    }

    @When("I update the Order status to {string}")
    public void i_update_the_order_status_to(String newStatus) {
        try {
            order.updateStatus(newStatus);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Then("the Order status should be {string}")
    public void the_order_status_should_be(String expectedStatus) {
        assertEquals(OrderStatusEnum.valueOf(expectedStatus), order.getOrderStatus());
    }

    @Then("an IllegalArgumentException should be thrown")
    public void an_illegal_argument_exception_should_be_thrown() {
        assertNotNull(capturedException);
        assertTrue(capturedException instanceof IllegalArgumentException);
    }

    @Then("an ErrorException should be thrown")
    public void an_error_exception_should_be_thrown() {
        assertNotNull(capturedException);
        assertTrue(capturedException instanceof ErrorException);
    }
}