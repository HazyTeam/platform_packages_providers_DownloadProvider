/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.providers.downloads.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;

import com.android.providers.downloads.Constants;

public class DownloadList extends Activity {

    static final int PAUSE_DOWNLOAD = 0;
    static final int RESUME_DOWNLOAD = 1;

    /**
     * @return an OnClickListener to pause or resume the given downloadId from the Download Manager
     */
    private DialogInterface.OnClickListener pauseResumeHandler(final long downloadId, final int operationId) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (operationId == PAUSE_DOWNLOAD) {
                    mDownloadManager.pauseDownload(downloadId);
                } else {
                    mDownloadManager.resumeDownload(downloadId);
                    // resumeDownload() in download manager only sets download status to
                    // running and then sends broadcast to start the service
                    Intent intent = new Intent(Constants.ACTION_RESUME);
                    intent.setClassName(Constants.PROVIDER_PACKAGE_NAME,
                                 "com.android.providers.downloads.DownloadReceiver");
                    sendBroadcast(intent);
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Trampoline over to new management UI
        final Intent intent = new Intent(DocumentsContract.ACTION_MANAGE_ROOT);
        intent.setData(DocumentsContract.buildRootUri(
                Constants.STORAGE_AUTHORITY, Constants.STORAGE_ROOT_ID));
        startActivity(intent);
        finish();
    }
}
