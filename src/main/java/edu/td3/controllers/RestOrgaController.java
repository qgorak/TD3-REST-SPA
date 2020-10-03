package edu.td3.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.td3.models.Organization;
import edu.td3.repositories.OrgaRepository;









 
@RestController
@RequestMapping("/rest/")
public class RestOrgaController {
	
	@Autowired
    private OrgaRepository repo;

	@GetMapping("/orgas/")
	public List<Organization> read() {
		return repo.findAll();	
	}
	
	@GetMapping("/orgas/{id}")
	public Organization read(@PathVariable int id) {
		return repo.findById(id);
	}
	

	@PostMapping("/orgas/create")
    public Organization create(@RequestBody Organization orga) {
		repo.saveAndFlush(orga);
		return orga;
    }
	
	@DeleteMapping("/orgas/delete/{id}")
    public void delete(@PathVariable int id) {
		repo.deleteById(id);
    }
	
	@PostMapping("orgas/update/{id}")
    public Organization update(@PathVariable int id,@RequestBody Organization orga) {
		Organization orgaToUpdate = repo.findById(id);
		orgaToUpdate.setName(orga.getName());
		orgaToUpdate.setDomain(orga.getDomain());
		orgaToUpdate.setAliases(orga.getAliases());
		repo.save(orgaToUpdate);
		return orga;

    }
	
}