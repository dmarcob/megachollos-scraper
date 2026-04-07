package com.megachollos.pricealert.infrastructure.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "PRICE_ALERTS")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlertEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String productReference;

  @Column(nullable = false)
  private String ecommerce;

  @Column(nullable = false)
  private BigDecimal targetPrice;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private LocalDateTime createdAt;
}
