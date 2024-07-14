package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.product.AddProductRq;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseMessage<List<Product>> getAllProducts() {
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
    public ResponseMessage<Void> addProduct(RequestMessage<AddProductRq> rq){
        ResponseMessage<Void> rs = new ResponseMessage<>();
        Status status = new Status();
        AddProductRq input = rq.getData();
        try {
            // check if exist
            if(productRepository.existsById(input.getId())){
                status.setHttpStatusCode(String.valueOf(HttpStatus.CONFLICT.value()));
                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
                status.setMessage("Product already exists");
            }
            else {
                Product product = new Product();
                product.setId(input.getId());
                product.setName(input.getName());
                product.setInStock(input.getInStock());
                product.setPrice(input.getPrice());

                productRepository.save(product);

                status.setHttpStatusCode(String.valueOf(HttpStatus.CREATED.value()));
                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SUCCESS);
                status.setMessage("Product created");
            }
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
            // Manual rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        rs.setStatus(status);
        return rs;
    }
}
