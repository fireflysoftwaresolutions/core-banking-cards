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


package io.fireflysoftware.core.banking.cards.core.services.issuer.impl;

import io.fireflysoftware.core.banking.cards.core.services.issuer.IssuerService;

import org.fireflyframework.core.queries.PaginationRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.core.queries.PaginationUtils;
import io.fireflysoftware.core.banking.cards.core.mappers.issuer.IssuerMapper;
import io.fireflysoftware.core.banking.cards.interfaces.dtos.issuer.IssuerDTO;
import io.fireflysoftware.core.banking.cards.models.entities.issuer.Issuer;
import io.fireflysoftware.core.banking.cards.models.repositories.issuer.IssuerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class IssuerServiceImpl implements IssuerService {

    @Autowired
    private IssuerRepository repository;

    @Autowired
    private IssuerMapper mapper;

    @Override
    public Mono<PaginationResponse<IssuerDTO>> listIssuers(PaginationRequest paginationRequest) {
        return PaginationUtils.paginateQuery(
                paginationRequest,
                mapper::toDTO,
                repository::findAllBy,
                repository::count
        );
    }

    @Override
    public Mono<IssuerDTO> createIssuer(IssuerDTO issuerDTO) {
        Issuer entity = mapper.toEntity(issuerDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<IssuerDTO> getIssuer(UUID issuerId) {
        return repository.findByIssuerId(issuerId)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<IssuerDTO> updateIssuer(UUID issuerId, IssuerDTO issuerDTO) {
        return repository.findByIssuerId(issuerId)
                .flatMap(existingIssuer -> {
                    Issuer updatedIssuer = mapper.toEntity(issuerDTO);
                    updatedIssuer.setIssuerId(existingIssuer.getIssuerId());
                    return repository.save(updatedIssuer);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteIssuer(UUID issuerId) {
        return repository.findByIssuerId(issuerId)
                .flatMap(repository::delete);
    }
}
