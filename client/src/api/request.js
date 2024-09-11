import config from '../config'; // 确保路径正确

import { useUserStore } from "../store/user";


//拼接url
const baseURL = config.baseUrl ? `http${config.https ? 's' : ''}://${config.baseUrl}` : '';

export function request(options) {
    return new Promise((resolve, reject) => {
        uni.request({
            ...options,
            url: baseURL + '/api/' + options.url ,
            header: {
                ...options.header,
                Authorization: useUserStore().getToken() ? `${useUserStore().getToken()}` : '',
            },
            success: (res) => {
                if (res.statusCode === 200){
                    resolve(res.data);
                }else if (res.statusCode === 401) {
                    useUserStore().clearUser();
                    uni.navigateTo({
                        url: '/pages/user/login?isTo=true'
                    })
                }
                else {
                    uni.showToast({
                        title: res.data['message'],
                        icon: 'none'
                    })
                }
            }
        });
    });
}
