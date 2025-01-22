import express from 'express';
import { roomController } from '../controllers/impl/roomController';
import authMiddleware from '../utils/authMiddleware';

const router = express.Router();

router.get('/', authMiddleware, roomController.getRoomList);
router.post('/',authMiddleware, roomController.createRoom);
router.post('/join',authMiddleware, roomController.joinRoom);

// router.get('/:id', authMiddleware, roomController.getRoomInfo);

export default router;
