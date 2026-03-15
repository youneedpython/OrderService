package com.example.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductDto {

    public record CreateRequest(
            @NotBlank(message = "상품명은 필수입니다.")
            String name,

            @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
            long price,

            @Size(max = 1000, message = "설명은 1000자 이내여야 합니다.")
            String description
    ) {}

    public record UpdateRequest(
            @NotBlank(message = "상품명은 필수입니다.")
            String name,

            @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
            long price,

            @Size(max = 1000, message = "설명은 1000자 이내여야 합니다.")
            String description
    ) {}

    public record Response(
            Long id,
            String name,
            long price,
            String description
    ) {}
}
