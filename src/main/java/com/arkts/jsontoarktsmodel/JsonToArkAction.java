package com.arkts.jsontoarktsmodel;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class JsonToArkAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        JsonToArk arkDialog = new JsonToArk();
        arkDialog.show();

    }
}
