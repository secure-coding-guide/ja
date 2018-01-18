package org.jssec.android.autofillframework.autofillservice;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;

import java.util.Arrays;

import static android.view.View.AUTOFILL_HINT_USERNAME;

final public class StructureParser {
    private final AssistStructure mStructure;
    AutofillId mUsernameFieldId = null;
    AutofillId mPasswordFieldId = null;
    String mUsernameValue = "";
    String mPasswordValue = "";

    StructureParser(AssistStructure structure) {
        mStructure = structure;
    }

    void parseForFill() {
        int nodes = mStructure.getWindowNodeCount();
        printLog("StructureParser::parseForFill");
        for (int i = 0; i < nodes; i++) {
            AssistStructure.WindowNode node = mStructure.getWindowNodeAt(i);
            AssistStructure.ViewNode view = node.getRootViewNode();
            parse(true, view, 0);
        }
    }

    void parseForSave() {
        printLog("StructureParser::parseForSave");
        int nodes = mStructure.getWindowNodeCount();
        for (int i = 0; i < nodes; i++) {
            AssistStructure.WindowNode node = mStructure.getWindowNodeAt(i);
            AssistStructure.ViewNode view = node.getRootViewNode();
            parse(false, view, 0);
        }
    }

    void parse(boolean forFill, ViewNode viewNode, int layer) {
        dumpNode(viewNode, forFill, layer);

        if (viewNode.getAutofillHints() != null) {
            String[] filteredHints = filterSupportedHints(viewNode.getAutofillHints());
            if (filteredHints != null && filteredHints.length == 1) {
                //サンプルではHintsが1つ指定されている場合のみ扱う
//                printLog("StructureParser::parse() filteredHints=" + filteredHints[0]);
                if (forFill) {
                    if (filteredHints[0].equals(AUTOFILL_HINT_USERNAME)) {
                        mUsernameFieldId = viewNode.getAutofillId();
                    } else if (filteredHints[0].equals(View.AUTOFILL_HINT_PASSWORD)) {
                        mPasswordFieldId = viewNode.getAutofillId();
                    }
                } else {
                    if (filteredHints[0].equals(AUTOFILL_HINT_USERNAME)) {
                        mUsernameValue = viewNode.getText().toString();
                    } else if (filteredHints[0].equals(View.AUTOFILL_HINT_PASSWORD)) {
                        mPasswordValue = viewNode.getText().toString();
                    }
                }
            }
        }
        int childrenSize = viewNode.getChildCount();
        if (childrenSize > 0) {
            for (int i = 0; i < childrenSize; i++) {
                parse(forFill, viewNode.getChildAt(i), layer+1);
            }
        }
    }
    public AutofillId getFieldId(String hint) {
        if (hint.equals(View.AUTOFILL_HINT_USERNAME)) {
            return mUsernameFieldId;
        } else if (hint.equals(View.AUTOFILL_HINT_PASSWORD)) {
            return mPasswordFieldId;
        }
        return null;
    }

    public String getValue(String hint) {
        if (hint.equals(View.AUTOFILL_HINT_USERNAME)) {
            return mUsernameValue;
        } else if (hint.equals(View.AUTOFILL_HINT_PASSWORD)) {
            return mPasswordValue;
        }
        return null;
    }


    //このサービスが対象とするhintsを指定
    static private String[] filterSupportedHints(String[] hints) {
        if (Arrays.asList(hints).contains(AUTOFILL_HINT_USERNAME) || Arrays.asList(hints).contains(View.AUTOFILL_HINT_PASSWORD)) {
            return hints;
        }
        return new String[] {};
    }

    //デバッグ用の出力
    //AutofillServiceは重要な情報を扱うため、本来はこのようなデバッグ用の出力をするべきではない
    private void dumpNode(ViewNode viewNode, boolean forFill, int layer) {
        String autofillStringValue = "N/A";
        AutofillValue value = viewNode.getAutofillValue();
        if (value != null) {
            if (value.isText()) {
                autofillStringValue = value.getTextValue() + "(text)";
            } else if (value.isList()) {
                autofillStringValue = Integer.toString(value.getListValue()) + "(list)";
            } else {
                autofillStringValue = "no type";
            }
        }
        String id = (viewNode.getAutofillId() != null ? viewNode.getAutofillId().toString() : "N/A");
        String texts = (viewNode.getText() != null ? viewNode.getText().toString() : "N/A");
        String hints = (viewNode.getAutofillHints() != null) ? viewNode.getAutofillHints()[0] : "N/A";
        int type = viewNode.getAutofillType();
        String layerSpace = "";
        for (int i=0; i<layer; i++) {
            layerSpace += "    ";
        }
        printLog("StructureParse::parse(): " + layerSpace + "forFill=" + forFill + ",id=" + id + ",type=" + type + ",values=" + autofillStringValue + ",text=" + texts + ",hints=" + hints + ",class=" + viewNode.getClassName()+",childNum="+Integer.toString(viewNode.getChildCount()));
    }

    private void printLog(String msg) {
        Log.d("JssecAutofillSample", msg);
    }
}
