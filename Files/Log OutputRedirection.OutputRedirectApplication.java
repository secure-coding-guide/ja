package org.jssec.android.log.outputredirection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import android.app.Application;

public class OutputRedirectApplication extends Application {

    // �ǂ��ɂ��o�͂��Ȃ�PrintStream
    private final PrintStream emptyStream = new PrintStream(new OutputStream() {
        public void write(int oneByte) throws IOException {
            // do nothing
        }
    });

    @Override
    public void onCreate() {
        // �����[�X�r���h����System.out/err���ǂ��ɂ��o�͂��Ȃ�PrintStream�Ƀ��_�C���N�g����

        // System.out/err�̖{���̃X�g���[����ޔ�����
        PrintStream savedOut = System.out;
        PrintStream savedErr = System.err;

        // ��U�ASystem.out/err���ǂ��ɂ��o�͂��Ȃ�PrintStream�Ƀ��_�C���N�g����
        System.setOut(emptyStream);
        System.setErr(emptyStream);

        // �f�o�b�O���̂ݖ{���̃X�g���[���ɖ߂�(�����[�X�r���h�ł͉��L1�s��ProGuard�ɂ��폜�����)
        resetStreams(savedOut, savedErr);
    }

    // �����[�X����ProGuard�ɂ�艺�L���\�b�h���܂邲�ƍ폜�����
    private void resetStreams(PrintStream savedOut, PrintStream savedErr) {
        System.setOut(savedOut);
        System.setErr(savedErr);
    }
}
