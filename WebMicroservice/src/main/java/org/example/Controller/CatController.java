package org.example.Controller;


import lombok.AllArgsConstructor;
import org.example.Cat;
import org.example.Service.CatService;
import org.example.DTO.CatDTO;
import org.example.Security.MyUserDetails;
import org.example.Span.CatSpan;
import org.example.kafka.CatProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cats")
@AllArgsConstructor
public class CatController {

    private CatService catService;
    private CatProducer catProducer;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CatSpan> saveCat(@RequestBody CatSpan catSpan){
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        catSpan.setOwnerId(myUserDetails.getId());
        catProducer.push(catSpan);
        return new ResponseEntity<>(catSpan, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public HttpStatus deleteCat(@PathVariable("id") Integer id){
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if(((MyUserDetails)auth.getPrincipal()).getAuthority().equals("ROLE_USER")){
            if(((MyUserDetails) auth.getPrincipal()).getId() == catService.getOwnerId(id)){
                catProducer.delete(id);
                return HttpStatus.OK;
            }
        }
        else {
            catProducer.delete(id);
            return HttpStatus.OK;
        }
        return HttpStatus.EXPECTATION_FAILED;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Cat> updateCat(@PathVariable(name = "id") int id, @RequestBody CatSpan catSpan){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            if(((MyUserDetails) auth.getPrincipal()).getId() == id){
                catSpan.setId(id);
                catProducer.update(catSpan);
            }
        }
        else {
            catSpan.setId(id);
            catProducer.update(catSpan);
        }
        return new ResponseEntity<>(catService.update(catSpan), HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CatDTO> getCat(@PathVariable(name = "id") int id){
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if(((MyUserDetails)auth.getPrincipal()).getAuthority().equals("ROLE_USER")){
            if(((MyUserDetails) auth.getPrincipal()).getId() == catService.getOwnerId(id)){
                return new ResponseEntity<>(catService.getById(id), HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>(catService.getById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER') ")
    public ResponseEntity<List<CatDTO>> getAllCats(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            cats = catService.getAll().stream().filter(x -> x.getOwner() == ((MyUserDetails) auth
                    .getPrincipal()).getId()).collect(Collectors.toList());
        }
        else {
            cats = catService.getAll();
        }
        return new ResponseEntity<>(cats, HttpStatus.OK);
    }

    @GetMapping("/color/{color}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER') ")
    public ResponseEntity<List<CatDTO>> findByColor(@PathVariable (name = "color") String color){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            cats = catService.findByColor(color).stream().filter(x -> x.getOwner() == ((MyUserDetails) auth
                    .getPrincipal()).getId()).collect(Collectors.toList());
        }
        else {
            cats = catService.findByColor(color);
        }
        return new ResponseEntity<>(cats, HttpStatus.OK);
    }

    @GetMapping("/breed/{breed}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER') ")
    public ResponseEntity<List<CatDTO>> findByBreed(@PathVariable (name = "breed") String breed){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            cats = catService.findByBreed(breed).stream().filter(x -> x.getOwner() == ((MyUserDetails) auth
                    .getPrincipal()).getId()).collect(Collectors.toList());
        }
        else {
            cats = catService.findByBreed(breed);
        }
        return new ResponseEntity<>(cats, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CatDTO>> findByName(@PathVariable (name = "name") String name){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            cats = catService.findByName(name).stream().filter(x -> x.getOwner() == ((MyUserDetails) auth
                    .getPrincipal()).getId()).collect(Collectors.toList());
        }
        else {
            cats = catService.findByName(name);
        }
        return new ResponseEntity<>(cats, HttpStatus.OK);
    }

    @GetMapping("/owner/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CatDTO>> findBOwnerId(@PathVariable (name = "id") int id){
        if(catService.findByOwnerId(id) == null || catService.findByOwnerId(id).isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(catService.findByOwnerId(id), HttpStatus.OK);
    }
}
