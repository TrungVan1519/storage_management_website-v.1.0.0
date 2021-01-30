package com.trungvan.validation;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.trungvan.entity.ProductInfo;
import com.trungvan.service.ProductInfoService;

@Component
public class ProductInfoValidator implements Validator {

	@Autowired
	private ProductInfoService productInfoService;

	@Override
	public boolean supports(Class<?> clazz) {

		return clazz == ProductInfo.class;
	}

	@Override
	public void validate(Object target, Errors errors) {

		ProductInfo productInfo = (ProductInfo) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "msg.required");
		if(productInfo.getId() != null) {
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "multipartFile", "msg.required");
		}

		if (productInfo.getCode() != null) {

			List<ProductInfo> productInfos = productInfoService.findByProductInfoProperty("code", productInfo.getCode());
			if (productInfos != null && !productInfos.isEmpty()) {
				
				// Dung validate khi update
				if (productInfo.getId() != 0) {

					if (productInfos.get(0).getId() != productInfo.getId()) {

						errors.rejectValue("code", "msg.code.existing");
					}
				} 
				// Dung validate khi them moi
				else {

					errors.rejectValue("code", "msg.code.existing");
				}
			}
			
			if(!productInfo.getMultipartFile().getOriginalFilename().isEmpty()) {
				
				String extension = FilenameUtils.getExtension(productInfo.getMultipartFile().getOriginalFilename());
				if(!extension.equals("png") && !extension.equals("jpg")) {
					
					errors.rejectValue("multipartFile", "msg.file.extension");
				}
			}
		}
	}

}
