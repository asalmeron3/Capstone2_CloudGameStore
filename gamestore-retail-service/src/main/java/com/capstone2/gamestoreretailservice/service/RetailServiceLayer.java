package com.capstone2.gamestoreretailservice.service;

import com.capstone2.gamestoreretailservice.exception.NotFoundException;
import com.capstone2.gamestoreretailservice.feignClients.*;
import com.capstone2.gamestoreretailservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RetailServiceLayer {

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private InventoryClient inventoryClient;

    @Autowired
    private LevelUpClient levelUpClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private InvoiceCleint invoiceCleint;

    public RetailServiceLayer(
            CustomerClient customerClient,
            InventoryClient inventoryClient,
            LevelUpClient levelUpClient,
            ProductClient productClient,
            InvoiceCleint invoiceCleint) {
        this.customerClient = customerClient;
        this.inventoryClient = inventoryClient;
        this.invoiceCleint = invoiceCleint;
        this.levelUpClient = levelUpClient;
        this.productClient = productClient;
    }

    public Invoice createAnInvoice(Invoice invoice) {
        return invoiceCleint.createAnInvoice(invoice);
    }

    public Invoice getInvoiceById(int id) {
        return invoiceCleint.getInvoiceById(id);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceCleint.getAllInvoices();
    }

    public List<Invoice> getAllInvoicesByCustomerId(int id) {
        return invoiceCleint.getInvoicesByCustomerId(id);
    }

    public List<Product> getProductsInAInventory() {
        List<Product> products = new ArrayList<>();

        List<Inventory> inventories = inventoryClient.getAllInventories();

        inventories.stream().forEach( inv -> {
            try {
                Product product = productClient.getProductById(inv.getProductId());
                products.add(product);
            } catch (Exception e) {

            }

        });

        return products;
    }

    public Product getProductById(int id) {
        return productClient.getProductById(id);
    }

    public List<Product> getProductByInvoiceId(int id) {
        List<InvoiceItem> invoiceItems = invoiceCleint.getInvoiceItemsByInvoiceId(id);

        List<Inventory> inventories = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        invoiceItems.stream().forEach(item -> {
            try {
                Inventory inventory = inventoryClient.getInventoryById(item.getInventoryId());
                inventories.add(inventory);
            } catch (Exception e) {

            }
        });

        inventories.stream().forEach( inv -> {
            try {
                Product product = productClient.getProductById(inv.getProductId());
                products.add(product);
            } catch (Exception e) {

            }

        });

        return products;
    }

    public int getLevelUpPointByCustomerId(int id) {
        try {
            LevelUp levelUp = levelUpClient.getLevelUpByCustomerId(id);
            return levelUp.getPoints();
        } catch (Exception e) {
            throw new NotFoundException("No levelUp points found for customerId  " + id);
        }
    }
}
