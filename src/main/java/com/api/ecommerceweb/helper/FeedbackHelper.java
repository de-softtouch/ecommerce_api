package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.model.Feedback;
import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.reponse.FeedbackResponse;
import com.api.ecommerceweb.service.FeedBackService;
import com.api.ecommerceweb.service.ProductService;
import com.api.ecommerceweb.service.ShopService;
import com.api.ecommerceweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("feedbackHelper")
@RequiredArgsConstructor
public class FeedbackHelper {

    private final FeedBackService feedBackService;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ShopService shopService;
    private final UserService userService;

    public ResponseEntity<?> getFeedbacks(Long productId) {
        Product product = productService.getById(productId);
        if (product == null)
            return ResponseEntity.badRequest().body(BaseResponseBody.badRequest("Can not get product id " + productId + " feedbacks"));
        List<Feedback> feedbacks = feedBackService.getFeedbacks(product);
        List<FeedbackResponse> rs = feedbacks.stream()
                .map(f -> modelMapper.map(f, FeedbackResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseBody.success(rs, "get product feedbacks success", "success"));
    }

    public ResponseEntity<?> getShopFeedbacks() {
        Shop shop = shopService.getShop(userService.getCurrentUser());
        List<Feedback> shopFeedbacks = feedBackService.getShopFeedbacks(shop);
        List<FeedbackResponse> data = shopFeedbacks.stream()
                .map(f -> modelMapper.map(f, FeedbackResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseBody.success(data, "get shop feedbacks success", "success"));

    }
}
