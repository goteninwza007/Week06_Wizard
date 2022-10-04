package com.example.week06_wizard.view;

import com.example.week06_wizard.pojo.Wizard;
import com.example.week06_wizard.pojo.Wizards;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Route(value = "index")
public class MainWizardView extends VerticalLayout {
    private Wizards wis;
    private TextField fullName, dollars;
    private Select<String> position,school, house;
    private RadioButtonGroup<String> genderRadio;
    private Button prevBtn, createBtn, updateBtn, deleteBtn, nextBtn;
    private Notification notify;
    private int wisIndex;
    public MainWizardView() {
        wis = new Wizards();
        fullName = new TextField("", "Fullname");
        dollars = new TextField("Dollars");
        dollars.setPrefixComponent(new Div(new Text("$")));
        genderRadio = new RadioButtonGroup<String>("Gender :");
        genderRadio.setItems("Male", "Female");
        position = new Select<>();
        position.setPlaceholder("Position");
        position.setItems("","Student", "Teacher");
        school = new Select<>();
        school.setPlaceholder("School");
        school.setItems("","Hogwarts", "Beauxbatons", "Durmstang");
        house = new Select<>();
        house.setPlaceholder("House");
        house.setItems("", "Gryffindor", "Ravenclaw", "Hufflepuff", "Slytherin");
        prevBtn = new Button("<<");
        createBtn = new Button("Create");
        updateBtn = new Button("Update");
        deleteBtn = new Button("Delete");
        nextBtn = new Button(">>");
        notify = new Notification();
        HorizontalLayout h1 = new HorizontalLayout();
        h1.add(prevBtn, createBtn, updateBtn, deleteBtn, nextBtn);
        this.add(fullName, genderRadio, position, dollars, school, house, h1);
        wisIndex = 0;
        this.fetchData();

        prevBtn.addClickListener(event->{
            if (wisIndex == 0){
                wisIndex = 0;
            }
            else{
                wisIndex -= 1;
            }
            this.onTimeData();
        });

        nextBtn.addClickListener(event->{
            if (wisIndex == wis.getModel().size()-1){
                wisIndex = wis.getModel().size()-1;
            }
            else{
                wisIndex += 1;
            }
            this.onTimeData();
        });

        createBtn.addClickListener(event->{
            String name = fullName.getValue();
            int money = Integer.parseInt(dollars.getValue());
            String sex = genderRadio.getValue().equals("Male")?"m":"f";
            String pos = position.getValue().equals("Teacher")?"teacher":"student";
            String sch = school.getValue();
            String hs = house.getValue();
            Wizard newWis = new Wizard(null,sex, name, sch, hs, pos, money);
            String output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .body(Mono.just(newWis), Wizard.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            notify.show(output);
            this.fetchData();
            this.onTimeData();
        });

        updateBtn.addClickListener(event->{
            String name = fullName.getValue();
            int money = Integer.parseInt(dollars.getValue());
            String sex = genderRadio.getValue().equals("Male")?"m":"f";
            String pos = position.getValue().equals("Teacher")?"teacher":"student";
            String sch = school.getValue();
            String hs = house.getValue();
            Wizard updateWis = new Wizard(wis.getModel().get(wisIndex).get_id(),sex, name, sch, hs, pos, money);
            String output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(updateWis), Wizard.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            notify.show(output);
            this.fetchData();
            this.onTimeData();
        });

        deleteBtn.addClickListener(event->{
            String output = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .body(Mono.just(wis.getModel().get(wisIndex)), Wizard.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            notify.show(output);
            this.wisIndex = this.wisIndex != 0?this.wisIndex-1:this.wisIndex+1;
            this.fetchData();
            this.onTimeData();
        });
    }

    private void fetchData() {
        ArrayList<Wizard> wisAll = WebClient.create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Wizard>>() {})
                .block();
        wis.setModel(wisAll);
    }
    public void onTimeData(){
        if (wis.getModel().size() != 0){
            this.fullName.setValue(wis.getModel().get(wisIndex).getName());
            this.genderRadio.setValue(wis.getModel().get(wisIndex).getSex().equals("m")?"Male":"Female");
            this.dollars.setValue(String.valueOf(wis.getModel().get(wisIndex).getMoney()));
            this.position.setValue(wis.getModel().get(wisIndex).getPosition().equals("teacher")?"Teacher":"Student");
            this.school.setValue(wis.getModel().get(wisIndex).getSchool());
            this.house.setValue(wis.getModel().get(wisIndex).getHouse());
        }
        else{
            this.fullName.setValue("");
            this.genderRadio.setValue("");
            this.dollars.setValue("");
            this.position.setValue("");
            this.school.setValue("");
            this.house.setValue("");
        }
    }

}
