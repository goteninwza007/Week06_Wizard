package com.example.week06_wizard.controller;

import com.example.week06_wizard.pojo.Wizard;
import com.example.week06_wizard.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;
    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public List getWizards(){
        List<Wizard> wizardsList = wizardService.retrieveWizards();
        System.out.println(wizardsList);
        return wizardsList;
    }
    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public String createWizards(@RequestBody Wizard wizard){
        Wizard wizards =  wizardService.createWizard(wizard);
        return "Wizard has been created";
    }
    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public String updateWizards(@RequestBody Wizard wizard){
        Wizard wizards = wizardService.retrieveByID(wizard.get_id());
        if (wizards != null){
            wizardService.updateWizard(wizard);
            return "Wizard has been updated";
        }else{
            return "Update fail";
        }
    }
    @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public String deleteWizards(@RequestBody Wizard wizard){
        Wizard wizards = wizardService.retrieveByID(wizard.get_id());
        boolean Status = wizardService.deleteWizard(wizards);
        return  Status?"Wizard has been deleted":"Nothing for delete";

    }
}
