import { defineStore } from 'pinia';

export const useRoomStore = defineStore({
    id: 'room',
    state: () => ({
        roomMsgList: []
    }),
    actions: {
        addMsg(msg) {
                this.roomMsgList.push(msg);
        },
        clearMsg() {
            this.roomMsgList = [];
        },
    },
});