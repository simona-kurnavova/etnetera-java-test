package com.etnetera.hr.controller;

import com.etnetera.hr.rest.Errors;
import com.etnetera.hr.rest.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

import javax.validation.*;
import java.util.*;

/**
 * Simple REST controller for accessing application logic.
 * 
 * @author Etnetera
 *
 */
@Validated
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

	private final JavaScriptFrameworkRepository repository;

    private final Validator validator;

	@Autowired
	public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository, Validator validator) {
		this.repository = repository;
        this.validator = validator;
    }

	@GetMapping("/frameworks")
	public Iterable<JavaScriptFramework> frameworks() {
		return repository.findAll();
	}

	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody JavaScriptFramework javaScriptFramework) {
        Errors error = validateEntity(javaScriptFramework);
        if(error != null)
            return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(repository.save(javaScriptFramework));
	}

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
	    repository.deleteById(id);
    }


    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody JavaScriptFramework javaScriptFramework) {
        Errors error = validateEntity(javaScriptFramework);
        if(error != null)
            return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);

        Optional<JavaScriptFramework> framework = repository.findById(id);
        if(framework.isPresent()) {
            javaScriptFramework.setId(framework.get().getId());
            javaScriptFramework = repository.save(javaScriptFramework);
            return ResponseEntity.ok(javaScriptFramework);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("framework/{id}")
    public ResponseEntity<?> retrieve(@PathVariable Long id) {
        Optional<JavaScriptFramework> framework = repository.findById(id);
        if(framework.isPresent())
            return ResponseEntity.ok(framework);
        else return ResponseEntity.noContent().build();
	}

    private Errors validateEntity(JavaScriptFramework javaScriptFramework) {
        Errors error = null;
        Set<ConstraintViolation<JavaScriptFramework>> violations = validator.validate(javaScriptFramework);

        if(!violations.isEmpty()) {
            List<ValidationError> validationErrors = new ArrayList<>();
            for (ConstraintViolation<JavaScriptFramework> v : violations) {
                validationErrors.add(new ValidationError(v.getPropertyPath().toString(),
                        v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()));
            }
            error = new Errors(validationErrors);
        }
        return error;
    }
}
