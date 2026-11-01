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
 * Validator implementation for ValidBIN annotation.
 * Validates Bank Identification Numbers (BIN).
 */
public class BINValidatorImpl implements ConstraintValidator<ValidBIN, String> {

    @Override
    public void initialize(ValidBIN constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String binNumber, ConstraintValidatorContext context) {
        if (binNumber == null || binNumber.trim().isEmpty()) {
            return false;
        }

        String cleanBIN = binNumber.trim();

        // Check if it contains only digits
        if (!cleanBIN.matches("^[0-9]+$")) {
            return false;
        }

        // Check length (6-8 digits for BIN)
        if (cleanBIN.length() < 6 || cleanBIN.length() > 8) {
            return false;
        }

        // Additional BIN-specific validations can be added here
        // For example, checking against known BIN ranges for specific card networks
        
        return true;
    }
}
