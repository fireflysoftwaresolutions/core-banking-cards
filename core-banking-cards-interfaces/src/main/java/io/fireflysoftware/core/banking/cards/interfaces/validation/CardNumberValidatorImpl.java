/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.core.banking.cards.interfaces.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for CardNumber annotation.
 * Validates card numbers using the Luhn algorithm.
 */
public class CardNumberValidatorImpl implements ConstraintValidator<CardNumber, String> {

    @Override
    public void initialize(CardNumber constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }

        // Remove any spaces or dashes
        String cleanCardNumber = cardNumber.replaceAll("[\\s-]", "");

        // Check if it contains only digits
        if (!cleanCardNumber.matches("^[0-9]+$")) {
            return false;
        }

        // Check length (13-19 digits for most card types)
        if (cleanCardNumber.length() < 13 || cleanCardNumber.length() > 19) {
            return false;
        }

        // Validate using Luhn algorithm
        return isValidLuhn(cleanCardNumber);
    }

    /**
     * Validates a card number using the Luhn algorithm.
     * 
     * @param cardNumber the card number to validate
     * @return true if the card number passes the Luhn check, false otherwise
     */
    private boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        // Process digits from right to left
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return (sum % 10) == 0;
    }
}
