import { AuthReply } from '../proto/bbuhot/service/auth_pb';

export class AuthError extends Error {
  errorCode: AuthReply.AuthErrorCode;

  constructor(m: AuthReply.AuthErrorCode) {
    super('AuthError');
    Object.setPrototypeOf(this, AuthError.prototype);
    this.errorCode = m;
  }
}
