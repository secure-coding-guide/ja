package org.jssec.android.autofillframework.autofillservice;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.app.assist.AssistStructure.WindowNode;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;

import java.util.Arrays;

import static android.view.View.AUTOFILL_HINT_USERNAME;

final public class StructureParser {
    private static final String TAG = "JssecAutofillSample";

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
        Log.d(TAG, "StructureParser::parseForFill() node num=" + Integer.toString(nodes));
        for (int i = 0; i < nodes; i++) {
            AssistStructure.WindowNode node = mStructure.getWindowNodeAt(i);
            AssistStructure.ViewNode view = node.getRootViewNode();
            parse(true, view);
        }
    }

    void parseForSave() {
        int nodes = mStructure.getWindowNodeCount();
        for (int i = 0; i < nodes; i++) {
            AssistStructure.WindowNode node = mStructure.getWindowNodeAt(i);
            AssistStructure.ViewNode view = node.getRootViewNode();
            parse(false, view);
        }
    }

    void parse(boolean forFill, ViewNode viewNode) {
        dumpNode(viewNode, forFill);

        if (viewNode.getAutofillHints() != null) {
            String[] filteredHints = filterSupportedHints(viewNode.getAutofillHints());
            if (filteredHints != null && filteredHints.length == 1) {
                //サンプルではHintsが1つ指定されている場合のみ扱う
                Log.d(TAG, "StructureParser::parse() filteredHints=" + filteredHints[0]);
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
                parse(forFill, viewNode.getChildAt(i));
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

    private void dumpNode(ViewNode viewNode, boolean forFill) {
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
        String ids = (viewNode.getAutofillId() != null ? viewNode.getAutofillId().toString() : "N/A");
        String texts = (viewNode.getText() != null ? viewNode.getText().toString() : "N/A");
        String hints = (viewNode.getAutofillHints() != null) ? viewNode.getAutofillHints()[0] : "N/A";
        int type = viewNode.getAutofillType();
        Log.d(TAG, "StructureParse::parse(): forFill=" + forFill + ", ids=" + ids + ", type=" + type + ", values=" + autofillStringValue + ", text=" + texts + ",hints=" + hints);
        Log.d(TAG, "StructureParser::parse() child viewNode num=" + Integer.toString(viewNode.getChildCount()));
    }
}
