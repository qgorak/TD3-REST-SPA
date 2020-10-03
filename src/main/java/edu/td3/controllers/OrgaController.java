package edu.td3.controllers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import edu.td3.models.Organization;
import io.github.jeemv.springboot.vuejs.AbstractVueJS;
import io.github.jeemv.springboot.vuejs.utilities.Http;




@Controller



 
public class OrgaController {
	
	@Autowired
	private AbstractVueJS vue;
    
	@RequestMapping("/orgas/")
    public String index(ModelMap model) {


	    //Adding Required data to vue
	    AddDatas();
	   //Adding Required computed to vue
		vue.addComputed("formTitle", "return this.editedIndex === -1 ? 'Nouvelle Organisation' : 'Editer Organisation'");
		//Adding Required methods to vue
		AddMethods();
	    model.put("vue", vue);
        return "index";
       }
	
	public void AddDatas() {
		vue.addDataRaw("organizations","[]");
		vue.addDataRaw("headers", " [{\r\n" + 
				"                        \"text\": \"Organizations\",\r\n" + 
				"                        \"align\": \"start\",\r\n" + 
				"                        \"sortable\": false,\r\n" + 
				"                        \"value\": \"name\"\r\n" + 
				"                    }, {\r\n" + 
				"                        \"text\": \"Aliases\",\r\n" + 
				"                        \"value\": \"aliases\"\r\n" + 
				"                    }, {\r\n" + 
				"                        \"text\": \"Domain\",\r\n" + 
				"                        \"value\": \"domain\"\r\n" + 
				"                    }, {\r\n" + 
				"                        \"text\": \"Actions\",\r\n" + 
				"                        \"value\": \"actions\"\r\n" + 
				"                    }, {\r\n" + 
				"                        \"value\": \"data-table-expand\"\r\n" + 
				"                    }]");
		vue.addDataRaw("editedItem", "{ name: '',aliases: '', domain: '',settings: '',users: []}");
		vue.addData("form",false);
		vue.addData("confirmDelete",false);
		vue.addDataRaw("defaultItem", "{ name: '',aliases: '', domain: '',settings: '',users: []}");
		vue.addData("editedIndex",-1);
		vue.addData("showAlert",false);
		vue.addData("messageAlert","");
	}
	
	public void AddMethods() {
		
		vue.addMethod("editItem" , "this.editedIndex = this.organizations.indexOf(item)\r\n" + 
				"      this.editedItem = Object.assign({}, item)\r\n" + 
				"      this.form = true","item");

		vue.addMethod("save" , "let self = this; \r\n"
				+ "if (this.editedIndex > -1) {\r\n"
				+ "        this.$http['post']('http://localhost:8080/rest/orgas/update/'+ this.organizations[this.editedIndex].id,this.editedItem).then(function(response) {\n"
				+ "                    console.log(self.organizations)\r\n"
				+ "                    self.showAlert = true\r\n "
				+ "                    self.messageAlert = 'Organisation '+response.data.name+' éditée'\r\n "
				+ "                    setTimeout(() => self.showAlert = false, 5000)})\r\n"                                
				+ "      } else {\r\n"	
				+ "        this.$http['post']('http://localhost:8080/rest/orgas/create', this.editedItem).then(function(response) {\n"
				+ "                    self.showAlert = true\r\n "
				+ "                    self.organizations.push(response.data);\n"
				+ "                    self.messageAlert = 'Organisation '+response.data.name+' ajoutée'\r\n "
				+ "                    setTimeout(() => self.showAlert = false, 5000)\r\n"
				+ "                });"
				+ "      }\r\n"
				+ "      this.close()");
		vue.addMethod("close" , " this.form = false\r\n"
				+ "      this.$nextTick(() => {\r\n"
				+ "        this.editedItem = Object.assign({}, this.defaultItem)\r\n"
				+ "        this.editedIndex = -1\r\n"
				+ "      })");
		vue.addMethod("deleteItem" , "this.showAlert = true\r\n"
				+ "      this.confirmDelete = false\r\n"
				+ "      const index = this.organizations.indexOf(item)\r\n"
				+ "       this.organizations.splice(index, 1)\r\n"
				+ "       this.$http['delete']('http://localhost:8080/rest/orgas/delete/'+ item.id)\r\n"
				+ "       this.messageAlert = 'Organisation '+item.name+' supprimée'\r\n "
				+ "       setTimeout(() => this.showAlert = false, 5000)\r\n","item");
		vue.onBeforeMount("let self=this;" + Http.get("http://localhost:8080/rest/orgas/", "self.organizations=response.data;"));
		vue.addMethod("showDeleteConfirm","","item");
	}
	
	
}