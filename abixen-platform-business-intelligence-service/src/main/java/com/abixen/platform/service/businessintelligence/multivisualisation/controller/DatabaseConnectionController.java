/**
 * Copyright (c) 2010-present Abixen Systems. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.abixen.platform.service.businessintelligence.multivisualisation.controller;

import com.abixen.platform.common.dto.FormErrorDto;
import com.abixen.platform.common.dto.FormValidationResultDto;
import com.abixen.platform.common.util.ValidationUtil;
import com.abixen.platform.common.util.WebModelJsonSerialize;
import com.abixen.platform.service.businessintelligence.multivisualisation.dto.DataSourceColumnDto;
import com.abixen.platform.service.businessintelligence.multivisualisation.dto.DatabaseConnectionDto;
import com.abixen.platform.service.businessintelligence.multivisualisation.facade.DatabaseConnectionFacade;
import com.abixen.platform.service.businessintelligence.multivisualisation.form.DatabaseConnectionForm;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/api/service/abixen/business-intelligence/control-panel/multi-visualisation/database-connections")
public class DatabaseConnectionController {

    private final DatabaseConnectionFacade databaseConnectionFacade;

    @Autowired
    public DatabaseConnectionController(DatabaseConnectionFacade databaseConnectionFacade) {
        this.databaseConnectionFacade = databaseConnectionFacade;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<DatabaseConnectionDto> findAllDatabaseConnections(@PageableDefault(size = 1, page = 0) Pageable pageable) {
        return databaseConnectionFacade.findAllDatabaseConnections(pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DatabaseConnectionDto findDatabaseConnection(@PathVariable Long id) {
        return databaseConnectionFacade.findDatabaseConnection(id);
    }

    @JsonView(WebModelJsonSerialize.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public FormValidationResultDto createDatabaseConnection(@RequestBody @Valid DatabaseConnectionForm databaseConnectionForm, BindingResult bindingResult) {
        log.debug("createDatabaseConnection() - databaseConnectionForm: " + databaseConnectionForm);

        if (bindingResult.hasErrors()) {
            List<FormErrorDto> formErrors = ValidationUtil.extractFormErrors(bindingResult);
            return new FormValidationResultDto(databaseConnectionForm, formErrors);
        }

        databaseConnectionFacade.createDatabaseConnection(databaseConnectionForm);

        return new FormValidationResultDto(databaseConnectionForm);
    }

    @JsonView(WebModelJsonSerialize.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public FormValidationResultDto updateDatabaseConnection(@PathVariable("id") Long id, @RequestBody @Valid DatabaseConnectionForm databaseConnectionForm, BindingResult bindingResult) {
        log.debug("updateDatabaseConnection() - id: " + id + ", databaseConnectionForm: " + databaseConnectionForm);

        if (bindingResult.hasErrors()) {
            List<FormErrorDto> formErrors = ValidationUtil.extractFormErrors(bindingResult);
            return new FormValidationResultDto(databaseConnectionForm, formErrors);
        }

        databaseConnectionFacade.updateDatabaseConnection(databaseConnectionForm);

        return new FormValidationResultDto(databaseConnectionForm);
    }

    @JsonView(WebModelJsonSerialize.class)
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public FormValidationResultDto testDatabaseConnection(@RequestBody @Valid DatabaseConnectionForm databaseConnectionForm, BindingResult bindingResult) {
        log.debug("testDatabaseConnection() - databaseConnectionForm: " + databaseConnectionForm);

        if (bindingResult.hasErrors()) {
            List<FormErrorDto> formErrors = ValidationUtil.extractFormErrors(bindingResult);
            return new FormValidationResultDto(databaseConnectionForm, formErrors);
        }

        databaseConnectionFacade.testDatabaseConnection(databaseConnectionForm);

        return new FormValidationResultDto(databaseConnectionForm);
    }

    @RequestMapping(value = "/{id}/tables", method = RequestMethod.GET)
    public List<String> getTables(@PathVariable("id") Long id) {
        log.debug("getTables()");
        return databaseConnectionFacade.getTables(id);
    }

    @RequestMapping(value = "/{id}/tables/{tableName}/columns", method = RequestMethod.GET)
    public List<DataSourceColumnDto> getTableColumns(@PathVariable("id") Long id, @PathVariable("tableName") String tableName) {
        return databaseConnectionFacade.getTableColumns(id, tableName);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteFileData(@PathVariable("id") long id) {
        log.debug("delete() - id: " + id);
        databaseConnectionFacade.deleteDatabaseConnection(id);
        return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
    }


}
