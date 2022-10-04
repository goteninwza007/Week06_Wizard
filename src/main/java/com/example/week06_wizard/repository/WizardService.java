package com.example.week06_wizard.repository;

import com.example.week06_wizard.pojo.Wizard;
import com.example.week06_wizard.pojo.Wizards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository wizardRepository;
    public WizardService(){}
    public WizardService(WizardRepository wizardRepository){
        this.wizardRepository = wizardRepository;
    }
    public List<Wizard> retrieveWizards() {
        return wizardRepository.findAll();
    }
    public Wizard retrieveByID(String _id){
        return wizardRepository.findByID(_id);
    }
    public Wizard createWizard(Wizard wizard){
        return wizardRepository.save(wizard);
    }
    public Wizard updateWizard(Wizard wizard){
        return wizardRepository.save(wizard);
    }
    public Boolean deleteWizard(Wizard wizard){
        try { wizardRepository.delete(wizard); return true; }
        catch (Exception e){ return false;}
    }
}
