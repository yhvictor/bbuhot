import { AdminGameStatusReply, BetReply } from '../proto/bbuhot/service/game_pb';

export class GameStatusError extends Error {
  errorCode: AdminGameStatusReply.ErrorCode;

  constructor(m: AdminGameStatusReply.ErrorCode) {
    super('GameStatusError');
    Object.setPrototypeOf(this, GameStatusError.prototype);
    this.errorCode = m;
  }
}

export class BetError extends Error {
  errorCode: BetReply.BetErrorCode;

  constructor(m: BetReply.BetErrorCode) {
    super('BetError');
    Object.setPrototypeOf(this, BetError.prototype);
    this.errorCode = m;
  }
}
