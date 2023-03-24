package com.cognive.projects.casernkb.delegate;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import com.cognive.servicetasks.executesql.IExecuteSqlService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class SaveObjectListDelegateRnkb implements JavaDelegate {
    private IExecuteSqlService executeSqlService;

    public SaveObjectListDelegateRnkb(IExecuteSqlService executeSqlService) {
        this.executeSqlService = executeSqlService;
    }

    public void execute(DelegateExecution delegateExecution) {
        Session session = (Session)delegateExecution.getVariable("session");
        List<Object> saveObjectList = (List)delegateExecution.getVariable("saveObjectList");
        Iterator var4 = saveObjectList.iterator();
        List<Object> savedList = new ArrayList<>();
        while(var4.hasNext()) {
            Object saveObject = var4.next();
            this.executeSqlService.saveObject(session, saveObject);
            savedList.add(saveObject);
        }

        if (delegateExecution.hasVariable("outputVarName")) {
            delegateExecution.setVariable(delegateExecution.getVariable("outputVarName").toString(), savedList);
        }
    }
}
