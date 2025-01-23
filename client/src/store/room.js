import { defineStore } from 'pinia';

export const useRoomStore = defineStore({
    id: 'room',
    state: () => ({
        userId: null,
        userName: '',
        roomMsgList: []
    }),
    actions: {
        addMessage(message) {
            this.roomMsgList.push(message);
            if (this.roomMsgList.length > 100) {
                this.roomMsgList.shift();
            }
        },
        clearMessages() {
            this.roomMsgList = [];
        },
        setUserInfo(userId, userName) {
            this.userId = userId;
            this.userName = userName;
        },
        getUserInfo() {
            return {
                userId: this.userId,
                userName: this.userName
            }
        }
    },
    persist: {
        enabled: true,
        strategies: [
            {
                key: 'room-store',
                storage: localStorage,
                paths: ['userId', 'userName']
            }
        ]
    }
});