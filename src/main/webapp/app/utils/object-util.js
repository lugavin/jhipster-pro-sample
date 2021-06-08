import pickBy from 'lodash/pickBy';
import stubObject from 'lodash/stubObject';
import moment from 'moment';

export function cleanEmptyField(object) {
  if (object !== null && typeof object === 'object') {
    return pickBy({ ...object });
  } else {
    return object;
  }
}
export function cleanObject(object) {
  if (object !== null && typeof object === 'object') {
    Object.keys(object).forEach(key => {
      object[key] = cleanEmptyField(object[key]);
    });
    return pickBy(object, stubObject());
  }
  return object;
}

/**
 * 去除对象中所有符合条件的对象
 * @param {Object} obj 来源对象
 * @param {Function} fn 函数验证每个字段
 */
export function compactObj(obj, fn) {
  for (var i in obj) {
    if (typeof obj[i] === 'object') {
      if (obj[i]) {
        const proto = Object.getPrototypeOf(obj[i]);
        if (proto !== Object.getPrototypeOf(moment())) {
          compactObj(obj[i], fn);
        }
      } else {
        compactObj(obj[i], fn);
      }
    }
    if (fn(obj[i])) {
      delete obj[i];
    }
  }
}

// 删除空对象 删除'', null, undefined
export function isEmpty(foo) {
  if (typeof foo === 'object') {
    for (var i in foo) {
      return false;
    }
    return true;
  } else {
    return foo === '' || foo === null || foo === undefined;
  }
}
