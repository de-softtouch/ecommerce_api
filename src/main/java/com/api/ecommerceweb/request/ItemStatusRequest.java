package com.api.ecommerceweb.request;

import com.api.ecommerceweb.enumm.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemStatusRequest implements Serializable {
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
