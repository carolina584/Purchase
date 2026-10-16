# e2e-purchase Specification

## Purpose
Permite realizar la automatización del flujo de compra E2E en OpenCart como usuario invitado, asegurando la selección del producto, el proceso de checkout y la verificación de orden confirmada mediante Serenity BDD y Screenplay.

## Requirements

### Requirement: Guest user e2e purchase flow
The system SHALL allow a guest user to search for a product, add it to the cart, complete the guest checkout with valid shipping details, and verify that the order has been successfully placed.

#### Scenario: TC-COMPRA-001 - Successful guest product purchase
- **WHEN** the user searches for a product and completes the guest checkout with valid information
- **THEN** the system shows that the order has been successfully placed with the confirmation message
