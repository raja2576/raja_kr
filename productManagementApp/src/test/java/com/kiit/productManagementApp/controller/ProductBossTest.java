package com.kiit.productManagementApp.controller;

import com.kiit.productManagementApp.ProductManagementAppApplication;
import com.kiit.productManagementApp.model.ProductItem;
import com.kiit.productManagementApp.service.ProductEngine;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductBoss.class)
public class ProductBossTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductEngine productEngine;

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void testFetchAllProducts() throws Exception {
        ProductItem product = new ProductItem(1L, "Phone", "Smartphone", "Electronics", 599.0, 4.5, "img.jpg");
        product.setImageUrl("https://example.com/phone.jpg");

        when(productEngine.allProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Phone"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateProduct() throws Exception {
        ProductItem product = new ProductItem(1L, "Phone", "Smartphone", "Electronics", 599.0, 4.5, "img.jpg");
        product.setImageUrl("https://example.com/phone.jpg");

        when(productEngine.saveProduct(any(ProductItem.class))).thenReturn(product);

        String productJson = """
                {
                    "id": 1,
                    "name": "Phone",
                    "description": "Smartphone",
                    "category": "Electronics",
                    "price": 599.0,
                    "rating": 4.5,
                    "imageName": "img.jpg",
                    "imageUrl": "https://example.com/phone.jpg"
                }
                """;

        mockMvc.perform(post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}




