/**
 * Created by vanki on 2017/5/22.
 */

/**
 * 数字转义可读字符串（需先添加转义类型）。如：12345->1.2万，123456789->1.2亿
 * @param num               数值
 * @param exactNum          精确位数，为假时自动识别最大定义的类型
 * @param decimalNum        保留小数位，默认为2
 * @param isDecimalNumMust  小数位最后若为0是否强制保留，默认不保留
 * @returns {*}
 */
function numToHumanView(num, exactNum, decimalNum, isDecimalNumMust) {
    num = Number(num);
    if (!num) return 0;

    decimalNum = decimalNum || 2;	// 小数点后精确位数

    tInfo = {
        5: [10000, '万'],
        9: [100000000, '亿'],
        // 在此添加转义类型
    };
    var nArr;

    if (exactNum) {
        nArr = tInfo[exactNum];
    } else {	// 自适应最高位的转义
        var nTemp;
        for (i in tInfo) {
            nTemp = tInfo[i];
            if (nTemp[0] > num) break;
            nArr = nTemp;
        }
    }

    if (!nArr) return num;
    if (num < nArr[0]) return num;

    var result = Number(num / nArr[0]).toFixed(decimalNum);

    /**
     * 小数点后如果最后为0是否去除，默认去除
     */
    outIf: if (!isDecimalNumMust) {
        var rArr = result.split(".");
        result = rArr[0];
        var suffixArr = rArr[1];
        if (!suffixArr) break outIf;

        var decimalSuffix = '';

        for (var i = suffixArr.length; i > 0; i--) {
            var v = suffixArr[i - 1];
            if (v == '0' && decimalSuffix == '') continue;
            decimalSuffix = v + decimalSuffix;
        }
        if (!decimalSuffix) break outIf;
        result += "." + decimalSuffix;
    }
    return result + nArr[1];
}