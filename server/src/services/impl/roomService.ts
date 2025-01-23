import { IRoomService } from "../iRoomService.";
import Room, { RoomModel } from "../../models/Room";
import { UserModel } from "../../models/User";
import RoomUser, { RoomUserModel } from "../../models/associations/RoomUserModel";
import {Sequelize} from "sequelize";

class RoomService implements IRoomService {
    async addRoom(room: RoomModel): Promise<RoomModel> {
        if (!room.roomName || !room.userId || !room.roomVideoUrl)
            return Promise.reject("房间信息不完整");
        const newRoom = await Room.create({
            roomName: room.roomName,
            roomVideoUrl: room.roomVideoUrl,
            userId: room.userId,
            password: room.password,
            isVisible: room.isVisible,
        })
        if (!newRoom)
            return Promise.reject("创建房间失败");
        this.joinRoom(newRoom, room.userId).catch(
            (err) => {
                return Promise.reject(err);
            }
        )
        return newRoom;
    }

    getRoomInfo(roomId: number): Promise<RoomModel|null> {
        return Room.findOne({
            where: {
                id: roomId
            }
        })
    }
    async getRoomList(userId: number): Promise<RoomModel[]> {
        try {
            const publicRooms = await Room.findAll({
                where: {
                    isVisible: true,
                },
                attributes: {
                    exclude:['password'],
                    include: [
                        [Sequelize.literal('false'), 'isInRoom']
                    ]
                }
            });
            const myRoomList = await this.getMyRoomList(userId)
            const filteredPublicRooms = publicRooms.filter((room) => !myRoomList.some((myRoom) => myRoom.id === room.id));
            return [...myRoomList,...filteredPublicRooms];
        } catch (err) {
            console.log("获取房间列表时发生错误:", err)
            return Promise.reject(err);
        }
    }
    async getMyRoomList(userId: number): Promise<RoomModel[]> {
        try {
            // 查找用户所在的所有房间
            const roomUsers = await RoomUser.findAll({
                where: {
                    userId: userId
                }
            });
            const roomIds = roomUsers.map((roomUser) => roomUser.roomId);
            return await Room.findAll({
                where: {
                    id: roomIds
                },
                attributes: {
                    include: [
                        [Sequelize.literal('true'), 'isInRoom'] // MySQL 或 PostgreSQL 会返回 true 作为布尔值
                    ]
                },
            });
        } catch (err) {
            return Promise.reject(err);
        }
    }


    async joinRoom(room: RoomModel, userId: number): Promise<RoomUserModel> {
        if (!room.id || !userId) {
            return Promise.reject("加入房间信息不完整");
        }
        try {
            const roomDb = await this.getRoomInfo(room.id);
            if (!roomDb) {
                return Promise.reject("房间不存在");
            }
            if (roomDb.password && roomDb.password !== room.password) {
                return Promise.reject("密码错误");
            }
            const existingEntry = await RoomUser.findOne({
                where: {
                    userId: userId,
                    roomId: room.id
                }
            });
            if (existingEntry) {
                return Promise.reject("用户已在房间中");
            }
            const roomUser = await RoomUserModel.create({
                userId: userId,
                roomId: room.id,
                isVisible: room.isVisible,
                joinedAt: new Date()
            });
            return Promise.resolve(roomUser);
        } catch (err) {
            console.error("加入房间时发生错误:", err);
            return Promise.reject(err);
        }
    }

    init(): void {
        try {
            Room.sync().then();
            RoomUserModel.sync().then();
        } catch (error) {
            console.error('Room初始化失败:', error);
        }
    }
}

export const roomService = new RoomService();
