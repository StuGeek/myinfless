// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.operator;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import util.tools.FileOperation;

public class TensorLstm
{
    static float embedInuse;
    static float encoderInuse;
    static float attenInuse;
    static float accInuse;
    static float otherInuse;
    
    static {
        TensorLstm.embedInuse = 0.0f;
        TensorLstm.encoderInuse = 0.0f;
        TensorLstm.attenInuse = 0.0f;
        TensorLstm.accInuse = 0.0f;
        TensorLstm.otherInuse = 0.0f;
    }
    
    public static void main(final String[] args) throws IOException {
        final Long start = System.currentTimeMillis();
        final int thread = 20;
        final FileOperation fo = new FileOperation();
        final List<String> NeedAddItemList = fo.readStringFile("C:\\Users\\DELL\\Desktop\\\u5ba2\u6237\u7aefbatch \u63a8\u7406\u65f6\u95f4\u91c7\u96c6\\lstm\\lstm-cpu-operatorAdd.csv");
        final List<String> metadataItemList = fo.readStringFile("C:\\Users\\DELL\\Desktop\\\u5ba2\u6237\u7aefbatch \u63a8\u7406\u65f6\u95f4\u91c7\u96c6\\lstm\\10core-20thread-old\\lstm-maxclass-2365_cpu_64.csv");
        float sum = 0.0f;
        float sum1embed = 0.0f;
        float sum2encoder = 0.0f;
        float sum3att = 0.0f;
        float sum4acc = 0.0f;
        float sumOther = 0.0f;
        for (final String item : metadataItemList) {
            if (item.contains("encod")) {
                sum2encoder += Integer.parseInt(Parse(item));
            }
            else if (item.contains("atten")) {
                sum3att += Integer.parseInt(Parse(item));
            }
            else if (item.contains("acc")) {
                sum4acc += Integer.parseInt(Parse(item));
            }
            else if (item.contains("embed")) {
                sum1embed += Integer.parseInt(Parse(item));
            }
            else {
                sumOther += Integer.parseInt(Parse(item));
            }
        }
        for (final String item : NeedAddItemList) {
            final int index = Integer.parseInt(item.split(",")[0]);
            sum += Integer.parseInt(parse(metadataItemList.get(index - 1)));
        }
        float currenceEncoder = 0.0f;
        float currenceAtt = 0.0f;
        float AttenMatMulSum = 0.0f;
        for (int i = 4; i <= 38; ++i) {
            AttenMatMulSum += Integer.parseInt(parse(metadataItemList.get(i - 1)));
        }
        AttenMatMulSum += Integer.parseInt(parse(metadataItemList.get(48)));
        AttenMatMulSum += Integer.parseInt(parse(metadataItemList.get(59)));
        AttenMatMulSum += Integer.parseInt(parse(metadataItemList.get(70)));
        AttenMatMulSum += Integer.parseInt(parse(metadataItemList.get(81)));
        AttenMatMulSum += Integer.parseInt(parse(metadataItemList.get(82)));
        currenceAtt += AttenMatMulSum * (thread - 1) / thread;
        AttenMatMulSum /= thread;
        sum += AttenMatMulSum;
        float AttenMatMul2Sum = 0.0f;
        for (int j = 39; j <= 48; ++j) {
            AttenMatMul2Sum += Integer.parseInt(parse(metadataItemList.get(j - 1)));
        }
        for (int j = 50; j <= 59; ++j) {
            AttenMatMul2Sum += Integer.parseInt(parse(metadataItemList.get(j - 1)));
        }
        for (int j = 61; j <= 70; ++j) {
            AttenMatMul2Sum += Integer.parseInt(parse(metadataItemList.get(j - 1)));
        }
        for (int j = 72; j <= 81; ++j) {
            AttenMatMul2Sum += Integer.parseInt(parse(metadataItemList.get(j - 1)));
        }
        currenceAtt += AttenMatMul2Sum * (thread - 1) / thread;
        AttenMatMul2Sum /= thread;
        sum += AttenMatMul2Sum;
        float attenTanhSum = 0.0f;
        for (int k = 86; k <= 125; ++k) {
            attenTanhSum += Integer.parseInt(parse(metadataItemList.get(k - 1)));
        }
        currenceAtt += attenTanhSum * (thread - 1) / thread;
        attenTanhSum /= thread;
        sum += attenTanhSum;
        float attenAddSum = 0.0f;
        for (int l = 126; l <= 165; ++l) {
            attenAddSum += Integer.parseInt(parse(metadataItemList.get(l - 1)));
        }
        currenceAtt += attenAddSum * (thread - 1) / thread;
        attenAddSum /= thread;
        sum += attenAddSum;
        float encoderConcatSum = 0.0f;
        for (int m = 597; m <= 636; ++m) {
            encoderConcatSum += Integer.parseInt(parse(metadataItemList.get(m - 1)));
        }
        currenceEncoder += encoderConcatSum * (4 * thread - 1) / (4 * thread);
        encoderConcatSum /= 4 * thread;
        sum += encoderConcatSum;
        float encoderAddSum = 0.0f;
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(180)));
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(188)));
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(190)));
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(192)));
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(194)));
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(182)));
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(184)));
        encoderAddSum += Integer.parseInt(parse(metadataItemList.get(186)));
        currenceEncoder += encoderAddSum / 2.0f;
        encoderAddSum /= 2.0f;
        sum += encoderAddSum;
        float encoderMulAdd = 0.0f;
        for (int i2 = 205; i2 <= 228; ++i2) {
            encoderMulAdd += Integer.parseInt(parse(metadataItemList.get(i2 - 1)));
        }
        currenceEncoder += encoderMulAdd / 3.0f;
        encoderMulAdd = encoderMulAdd * 2.0f / 3.0f;
        sum += encoderMulAdd;
        float encoderSigmodAdd = 0.0f;
        for (int i3 = 229; i3 <= 252; ++i3) {
            encoderSigmodAdd += Integer.parseInt(parse(metadataItemList.get(i3 - 1)));
        }
        currenceEncoder += encoderSigmodAdd / 3.0f;
        encoderSigmodAdd = encoderSigmodAdd * 2.0f / 3.0f;
        sum += encoderSigmodAdd;
        final Long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
    
    public static String Parse(final String s) {
        String split = "";
        if (s.contains("\"")) {
            split = s.split("\",")[1];
            if (split.startsWith("\"")) {
                split = split.split("\",")[0].replaceAll(",", "").replaceAll("\"", "");
            }
            else {
                split = split.split(",")[0];
            }
        }
        else {
            split = s.split(",")[6];
        }
        return split;
    }
    
    public static String parse(final String s) {
        String split = "";
        if (s.contains("\"")) {
            split = s.split("\",")[1];
            if (split.startsWith("\"")) {
                split = split.split("\",")[0].replaceAll(",", "").replaceAll("\"", "");
            }
            else {
                split = split.split(",")[0];
            }
        }
        else {
            split = s.split(",")[6];
        }
        if (s.contains("encod")) {
            TensorLstm.encoderInuse += Integer.parseInt(split);
        }
        else if (s.contains("atten")) {
            TensorLstm.attenInuse += Integer.parseInt(split);
        }
        else if (s.contains("acc")) {
            TensorLstm.accInuse += Integer.parseInt(split);
        }
        else if (s.contains("embed")) {
            TensorLstm.embedInuse += Integer.parseInt(split);
        }
        else {
            TensorLstm.otherInuse += Integer.parseInt(split);
        }
        return split;
    }
}
