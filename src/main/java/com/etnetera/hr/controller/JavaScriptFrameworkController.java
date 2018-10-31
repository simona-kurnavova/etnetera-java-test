package com.etnetera.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

import java.util.Optional;

/**
 * Simple REST controller for accessing application logic.
 * 
 * @author Etnetera
 *
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

	private final JavaScriptFrameworkRepository repository;

	@Autowired
	public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/frameworks")
	public Iterable<JavaScriptFramework> frameworks() {
		return repository.findAll();
	}

	@PostMapping("/add")
	public ResponseEntity<Object> add(@RequestBody JavaScriptFramework javaScriptFramework) {
        JavaScriptFramework js = null;

        try {
            js = repository.save(javaScriptFramework);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(js);
	}

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
	    repository.deleteById(id);
    }


    @PutMapping("update/{id}")
    public JavaScriptFramework update(@PathVariable Long id, @RequestBody JavaScriptFramework javaScriptFramework) {
        Optional<JavaScriptFramework> framework = repository.findById(id);
        if(framework.isPresent()) {
            javaScriptFramework.setId(framework.get().getId());
            repository.save(javaScriptFramework);
        }
        return framework.orElse(null);
    }

}
