//根据key获取对象值
export const getObj = (key, obj) => {
  let value = obj;
  if (key.indexOf('.') > -1) {
    const keysArr = key.split('.');
    for (let i = 0; i < keysArr.length; i++) {
      const k = keysArr[i];
      value = value[k];
    }
  } else {
    value = value[key];
  }
  return value;
};
//获取数据,测试用
export const getDataMethod = obj => {
  console.log(obj);
  return new Promise(function (reslove, reject) {
    setTimeout(function () {
      let result = [];
      const page = obj.PageIndex + 1;
      for (let i = 0; i < 5; i++) {
        const json = {
          key: `${page}ID_${i}`,
          Id: `${page}ID_${i}`,
          date: '2019-10-12',
          input1: `第${page}页测试`,
          input: `第${page}页测试`,
          select: i % 2 ? 1 : 2,
          number: page + i,
          switch: false,
          checkbox: true,
          textarea: `第${page}页内容`,
        };
        result.push(json);
      }
      reslove({
        Data: {
          Datas: result,
          Total: 30,
        },
      });
      if (!result.length) {
        reject();
      }
    }, 1000);
  });
};
