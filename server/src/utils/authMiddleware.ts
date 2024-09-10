import express from 'express';
import jwt,{ JwtPayload }  from 'jsonwebtoken';
import config from '../config';
import user from "../models/User";
import {ApiResponse} from "./apiResponse";
// 假设你使用的是基于令牌的鉴权，比如 JWT
const authMiddleware = (req: express.Request, res: express.Response, next: express.NextFunction) => {
    const token = req.headers['authorization'];

    if (!token) {
        return res.status(401).send(ApiResponse.custom(401,"请先登录"));
    }
    try {
        // 假设使用jsonwebtoken库来验证JWT
        const decoded = jwt.verify(token, config.jwtSecret);

        if (typeof decoded !== 'string'){
            req.body.jwtUserId = Number(decoded.userId);
            next();
        }
    } catch (error) {
        return res.status(401).send(ApiResponse.custom(401,"登录过期"));
    }
};

export default authMiddleware;
