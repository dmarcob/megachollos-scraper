# Kata 5 — New Feature: Price Alerts

## Goal

Implement a complete feature from scratch: entity, repository, API, and integration tests. Apply everything you've learned.

## Feature

Users can create **price alerts** — they want to be notified when a product drops below a target price. We need to store these alerts.

---

## Part 1 — Write the integration tests first (TDD)

Write the tests before the implementation. They will fail at first — that's expected.

### Step 1 — Create the test class

Create `src/test/java/com/megachollos/pricealert/integration/PriceAlertIT.java`:
- Extend `BaseIntegrationTest`
- Add a constant: `public static final String PATH = "/price-alerts/";`

### Step 2 — Write `createPriceAlert_OK()`

Use REST-Assured to POST a JSON body to `/price-alerts/`:
```json
{
  "productReference": "134029164",
  "ecommerce": "mediamarkt",
  "targetPrice": 59.99,
  "email": "student@test.com"
}
```
Expect HTTP status **201 (Created)**.

### Step 3 — Write `getPriceAlerts_OK()`

1. First, save a PriceAlert directly to the database (you'll need the repository)
2. Then GET `/price-alerts/` with REST-Assured
3. Expect status **200** and assert:
   - The response contains 1 element
   - The email matches

### Step 4 — Run the tests

```bash
mvn failsafe:integration-test -Dit.test=PriceAlertIT
```

They should fail with **404** — the endpoint doesn't exist yet. This is expected.

---

## Part 2 — Implement the feature

### Step 5 — Create the JPA entity

Create `src/main/java/com/megachollos/pricealert/infrastructure/jpa/entities/PriceAlertEntity.java`:

| Field | Type | Notes |
|-------|------|-------|
| id | Long | `@Id`, auto-generated (use `@GeneratedValue(strategy = GenerationType.IDENTITY)`) |
| productReference | String | not null |
| ecommerce | String | not null |
| targetPrice | BigDecimal | not null |
| email | String | not null |
| createdAt | LocalDateTime | not null |

Use `@Entity(name = "PRICE_ALERTS")`.

### Step 6 — Create the repository

Create `src/main/java/com/megachollos/pricealert/infrastructure/jpa/repositories/JpaPriceAlertRepository.java`:
```java
public interface JpaPriceAlertRepository extends JpaRepository<PriceAlertEntity, Long> {
}
```

### Step 7 — Create the controller

Create `src/main/java/com/megachollos/pricealert/infrastructure/rest/PriceAlertController.java`:

- `@PostMapping("/price-alerts/")` — receives a request body, saves it, returns **201**
- `@GetMapping("/price-alerts/")` — returns all price alerts as a list

> Tip: Look at `BrandController.java` for reference on how controllers are structured in this project.

### Step 8 — Run the tests again

```bash
mvn failsafe:integration-test -Dit.test=PriceAlertIT
```

Both tests should pass now.

---

## Part 3 — Push and PR

### Step 9 — Commit and push

### Step 10 — Create a Pull Request

Go to GitHub and create a PR from your branch to `main`. The CI pipeline will run your tests automatically. Verify it passes.

---
