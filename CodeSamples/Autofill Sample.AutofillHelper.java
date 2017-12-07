package org.jssec.android.autofillframework.autofillservice;

import android.content.Context;
import android.service.autofill.Dataset;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveInfo;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import org.jssec.android.autofillframework.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AutofillHelper {
    private static final String TAG = "JssecAutofillSample";

    public static FillResponse createFillResponse(Context context, AutofillId usernameId, AutofillId passwdId, ArrayList<Database.Credential> data) {
        if ((usernameId == null) || (passwdId == null)) {
            Log.e(TAG, "AutofillHelper::createFillResponse() arg is incorrect");
            return null;
        }

        FillResponse.Builder responseBuilder = new FillResponse.Builder();
        //Autofillèàóù
        if (data != null) {
            for (int i=0; i<data.size(); i++) {
                Database.Credential cred = data.get(i);
                responseBuilder
                        .addDataset(new Dataset.Builder()
                                .setValue(usernameId, AutofillValue.forText(cred.username), newRemoteViews(context.getPackageName(), cred.username))
                                .setValue(passwdId, AutofillValue.forText(cred.pass), newRemoteViews(context.getPackageName(), "Password for " + cred.username))
                                .build());
            }
        }

        return responseBuilder.setSaveInfo(new SaveInfo.Builder(SaveInfo.SAVE_DATA_TYPE_USERNAME | SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                new AutofillId[] { usernameId, passwdId }).build())
                .build();
    }

    private static RemoteViews newRemoteViews(String packageName, String remoteViewsText,
                                             @DrawableRes int drawableId) {
        RemoteViews presentation =
                new RemoteViews(packageName, R.layout.multidataset_service_list_item);
        presentation.setTextViewText(R.id.text, remoteViewsText);
        presentation.setImageViewResource(R.id.icon, drawableId);
        return presentation;
    }

    private static RemoteViews newRemoteViews(String packageName, String remoteViewsText) {
        RemoteViews presentation =
                new RemoteViews(packageName, R.layout.multidataset_service_list_item);
        presentation.setTextViewText(R.id.text, remoteViewsText);
        return presentation;
    }

}
