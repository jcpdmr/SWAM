package com.swam.catalog;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qesm.ProductIstance;
import com.swam.multimodule.commons.ProductDTO;

public interface ProductIstanceRepository extends MongoRepository<ProductDTO<ProductIstance>, String>{

}
