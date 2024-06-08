package com.example.authet.controller;

import com.example.authet.model.Employe;
import com.example.authet.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@CrossOrigin("*")
@RequestMapping("/employes")
public class EmployeController  
{
    private EmployeRepository employeRepository;
    
    @Autowired
    public EmployeController(EmployeRepository employeRepository){
        
        this.employeRepository = employeRepository;
    
    }

    @GetMapping
    public ResponseEntity<Page<Employe>> findAll(Pageable pageable)
    {
        
            try {
                Page<Employe> page = employeRepository.findAll(pageable);
                if (page.hasContent()) {
                    return ResponseEntity.ok().body(page);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            }catch (Exception e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
    
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Employe> findOne(@PathVariable Integer id)
    {
        
        try{
            Employe one = employeRepository.findById(id).orElse(null);
            if(one != null){
                return ResponseEntity.ok().body(one);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(one);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    
    }

    @PostMapping
    public ResponseEntity<Employe> insert(@RequestBody Employe employe)
    {
        
        try{
            Employe creation = employeRepository.save(employe);
            if(creation != null){
               return ResponseEntity.status(HttpStatus.CREATED).body(creation);
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(creation);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employe> update(@PathVariable Integer id, @RequestBody Employe employe)
    {
        
        try{
            Optional<Employe> opt = employeRepository.findById(id);
            if(opt.isPresent()){
                Employe update = opt.get();
                employe.setId(update.getId());
                employeRepository.save(employe);
                if(employe != null){
                    return ResponseEntity.ok().body(employe);
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body( employe);
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body( employe);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id)
    {
        
        employeRepository.deleteById(id);
    
    }

    

    

}

