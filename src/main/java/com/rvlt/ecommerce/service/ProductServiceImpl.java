package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseMessage<List<Product>> getAllProduct() {
        ResponseMessage<List<Product>> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            List<Product> products = productRepository.findAll();
            rs.setData(products);
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }

    @Override
    public ResponseMessage<Product> getProductById(Long id) {
        ResponseMessage<Product> rs = new ResponseMessage<>();
        Status status = new Status();

        try {
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()){
                rs.setData(product.get());
            } else {
                status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
                status.setMessage("Product not found");
            }
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }

    @Override
    @Transactional
    public ResponseMessage<Void> updateProduct(Long id, UpdateProductRq request) {
        ResponseMessage<Void> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()){
                Product productObj = product.get();

                if (request.getPrice() > 0){
                    productObj.setPrice(request.getPrice());
                    productRepository.save(productObj);
                } else {
                    status.setHttpStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
                    status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
                    status.setMessage("Invalid Price provided");
                }
            } else {
                status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
                status.setMessage("Product not found");
            }
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage("Error updating product: " + e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }
}
