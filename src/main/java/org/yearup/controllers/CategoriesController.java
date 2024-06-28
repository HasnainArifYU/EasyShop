package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import java.util.List;


// to make this a REST controller
//Tells spring that this class will handle web requests and return data directly
@RestController


@RequestMapping("/categories")
//sets the base URL for all the methods in this class
// http://localhost:8080/categories


@CrossOrigin
// allows other websites to access the methods in this class

public class CategoriesController {

    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    // automatically provides the needed objects for this class to work.
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping
    public List<Category> getAll() {
        // handles GET requests and returns all categories
        return categoryDao.getAllCategories();

    }

    // handles GET requests with an ID in the URL
    // @PathVariable extrsacts the ID from the URL and passes it to the method
    // returns category by ID, if not found, returns a 404 code error
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + id);
        }
        return category;
    }

    // returns a list of products for a given category ID
    // calls productDao
    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productDao.listByCategoryId(categoryId);
    }


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED) //sets the response status to 201 (Created) when a new category is added.
    @PreAuthorize("hasRole('ADMIN')") // restricts access to users with the 'ADMIN' role
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
    }


    // updates an existing category by its ID using PUT requests. ADMIN access
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        categoryDao.update(id, category);
    }

    // deletes a category by its ID provided in the URL
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT) //error status 204
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int id) {
        categoryDao.delete(id);
    }
}
