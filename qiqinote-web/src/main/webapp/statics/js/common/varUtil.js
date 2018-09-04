/**
 * Created by vanki on 2017/2/24.
 */

/**
 * 值是否为真
 * @param val
 * @returns {*}
 */
function isValTrue(val) {
    return val != undefined && val != null && val != '';
}

/**
 * 值是否为假
 * @param val
 * @returns {boolean}
 */
function isValFalse(val) {
    return !isValTrue(val)
}

/**
 * 中国移动拥有号码段为:134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188
 * 19个号段 中国联通拥有号码段为:130 131 132 145 155 156 175 176 185 186;10个号段
 * 中国电信拥有号码段为:133 153 177 180 181 189;6个号码段
 * 虚拟运营商:170
 *
 * 是否为手机号
 * @param val
 */
var regMobileStr = /^1(([3][456789])|([4][7])|([5][012789])|([7][8])|([8][23478]))[0-9]{8}$/;
var regUnicomStr = /^1(([3][012])|([4][5])|([5][56])|([7][5])|([8][56]))[0-9]{8}$/;
var regTelecomStr = /^1(([3][3])|([5][3])|([7][07])|([8][019]))[0-9]{8}$/;
function isPhone(val) {
    if (regMobileStr.exec(val)) return true;
    if (regUnicomStr.exec(val)) return true;
    if (regTelecomStr.exec(val)) return true;
    return false;
}
