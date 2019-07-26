package com.capstone2.gamestoreproductservice.dao;

import com.capstone2.gamestoreproductservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDaoJdbcTemplateImpl implements ProductDao {

    JdbcTemplate productJdbcTemplate;

    @Autowired
    public ProductDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        productJdbcTemplate = jdbcTemplate;
    }

    private final static String DELETE_A_PRODUCT = "delete from products where product_id = ?";
    private final static String FIND_A_PRODUCT = "select * from products where product_id = ?";
    private final static String GET_AlL_PRODUCTS = "select * from products";
    private final static String CREATE_A_PRODUCT =
            "insert into products (product_name, product_description, list_price, unit_cost) values (?, ?, ?, ?)";
    private final static String UPDATE_PRODUCT =
            "update products set product_name = ?, product_description = ?, list_price = ?, unit_cost = ? where product_id = ?";

    private Product mapRowToProduct(ResultSet rs, int rowsFound) throws SQLException {
        Product product = new Product(
                rs.getString("product_name"),
                rs.getString("product_description"),
                rs.getBigDecimal("list_price"),
                rs.getBigDecimal("unit_cost")
                );
        product.setId(rs.getInt("product_id"));

        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productJdbcTemplate.query(GET_AlL_PRODUCTS, this::mapRowToProduct);
    }

    @Override
    public Product getProductById(int id) {
        try {
            return productJdbcTemplate.queryForObject(FIND_A_PRODUCT, this::mapRowToProduct, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Product postAProduct(Product product) {
        productJdbcTemplate.update(
                CREATE_A_PRODUCT,
                product.getName(),
                product.getDescription(),
                product.getListPrice(),
                product.getUnitCost()
        );

        int id = productJdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);

        product.setId(id);

        return product;
    }

    @Override
    public String updateProduct(Product product) {
        int rowsUpdated = productJdbcTemplate.update(
                UPDATE_PRODUCT,
                product.getName(),
                product.getDescription(),
                product.getListPrice(),
                product.getUnitCost(),
                product.getId()
        );

        String successful = "Update successful: "+ product.toString();
        String unsuccessful = "Update NOT successful. Please try again.";

        return rowsUpdated == 1 ? successful : unsuccessful;
    }

    @Override
    public String deleteProduct(int id) {
        int rowsUpdated = productJdbcTemplate.update(DELETE_A_PRODUCT,id);

        String successful = "Deletion successful. Product with product_id '" + id + "' has been deleted";
        String unsuccessful = "Deletion NOT successful. No product with product_id '" + id + "' was found.";

        return rowsUpdated == 1 ? successful : unsuccessful;
    }
}
