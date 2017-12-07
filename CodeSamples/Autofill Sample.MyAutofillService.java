package org.jssec.android.autofillframework.autofillservice;

import android.app.assist.AssistStructure;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import org.jssec.android.autofillframework.R;

import java.util.List;

public class MyAutofillService extends AutofillService {
    private static final String TAG = "JssecAutofillSample";

    @Override
    public void onFillRequest(FillRequest request, CancellationSignal cancellationSignal,
                              FillCallback callback) {
        AssistStructure structure = request.getFillContexts()
                .get(request.getFillContexts().size() - 1).getStructure();


        String packageName = structure.getActivityComponent().getPackageName();
        Log.d(TAG, "MyAutofillService::onFillRequest() packageName=" + packageName);

        //必要に応じて、異なるパッケージのアプリからのリクエストを処理しないようにする

        StructureParser parser = new StructureParser(structure);
        parser.parseForFill();
        Database db = new Database(this);
        FillResponse response = AutofillHelper.createFillResponse(this, parser.getFieldId(View.AUTOFILL_HINT_USERNAME), parser.getFieldId(View.AUTOFILL_HINT_PASSWORD), db.getAllList());
        callback.onSuccess(response);
    }

    @Override
    public void onSaveRequest(SaveRequest request, SaveCallback callback) {
        List<FillContext> context = request.getFillContexts();
        final AssistStructure structure = context.get(context.size() - 1).getStructure();
        String packageName = structure.getActivityComponent().getPackageName();
        Log.d(TAG, "MyAutofillService::onSaveRequest() packageName=" + packageName);

        //必要に応じて、異なるパッケージのアプリからのリクエストを処理しないようにする

        StructureParser parser = new StructureParser(structure);
        parser.parseForSave();

        //保存処理
        Database db = new Database(this);
        db.add(parser.getValue(View.AUTOFILL_HINT_USERNAME), parser.getValue(View.AUTOFILL_HINT_PASSWORD));
    }



}
