package org.fs.dx.utilities;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

/**
 * Created by Fatih on 06/07/16.
 * as org.fs.dx.utilities.ChemisteryUtility
 */
public final class ChemistryUtility {

    private final static float BASE_SIZE = 0.5f;

    /**
     *
     * @param str String to put spannable
     * @return SpannableString instance that show text as Chemistry formula
     */
    public static SpannableString toChemistryText(String str) {
        SpannableString spanStr = new SpannableString(str);
        for (int i = 0, z = str.length(); i < z; i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                spanStr.setSpan(new RelativeSizeSpan(BASE_SIZE), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanStr;
    }
}
