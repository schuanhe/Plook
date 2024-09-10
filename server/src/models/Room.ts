import {DataTypes, Model} from 'sequelize';
import sequelize  from '../utils/db';
import User from "./User";


class RoomModel extends Model {
    id!: number;
    userId!: number;
    roomName!: string;
    isVisible!: boolean;
    password: string|undefined;
    roomVideoUrl!: string;
}

let Room = RoomModel.init({
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    userId: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: User,
            key: 'id'
        }
    },
    roomName: {
        type: DataTypes.STRING,
        allowNull: false
    },
    roomVideoUrl: {
        type: DataTypes.STRING,
        allowNull: false
    },
    isVisible: {
        type: DataTypes.BOOLEAN,
        allowNull: false,
        defaultValue: true
    },
    password: {
        type: DataTypes.STRING,
        allowNull: true
    },
    // 数据库没有的字段
    isPassword: {
        type: DataTypes.VIRTUAL,
        get() {
            return this.password !== null;
        }
    }

}, {
    sequelize,
    underscored: true,
    modelName: 'Room',
    tableName: 'Rooms'
})

export type RoomInstance = typeof RoomModel.prototype;
export default Room;
export {RoomModel};
