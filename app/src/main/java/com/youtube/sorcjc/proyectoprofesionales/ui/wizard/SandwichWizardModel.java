package com.youtube.sorcjc.proyectoprofesionales.ui.wizard;

import android.content.Context;

import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.MultipleFixedChoicePage;
import com.tech.freak.wizardpager.model.NumberPage;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;
import com.tech.freak.wizardpager.model.TextPage;
import com.youtube.sorcjc.proyectoprofesionales.ui.wizard.pages.CustomerInfoPage;

public class SandwichWizardModel extends AbstractWizardModel {
    public SandwichWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new SingleFixedChoicePage(this, "Puntualidad")
                        .setChoices("Muy satisfecho", "Satisfecho", "Poco satisfecho", "Nada satisfecho")
                        .setRequired(true),

                new SingleFixedChoicePage(this, "Profesionalismo")
                        .setChoices("Muy satisfecho", "Satisfecho", "Poco satisfecho", "Nada satisfecho")
                        .setRequired(true),

                new SingleFixedChoicePage(this, "Cumplimiento")
                        .setChoices("Muy satisfecho", "Satisfecho", "Poco satisfecho", "Nada satisfecho")
                        .setRequired(true),

                new SingleFixedChoicePage(this, "Precio")
                        .setChoices("Muy satisfecho", "Satisfecho", "Poco satisfecho", "Nada satisfecho")
                        .setRequired(true),

                new CustomerInfoPage(this, "Tipo de trabajo").setRequired(true),

                new SingleFixedChoicePage(this, "Lo recomendaría?")
                        .setChoices("Sí", "No sé", "No")
                        .setRequired(true),

                /*new MultipleFixedChoicePage(this, "Multiple choice")
                        .setChoices("Pepperoni", "Turkey", "Ham", "Pastrami"),*/

                new BranchPage(this, "Desea añadir un comentario?")
                        .addBranch("Sí",
                                new TextPage(this, "Comentarios").setRequired(true).setRequired(true))
                        .addBranch("No").setValue("No")
        );
    }
}